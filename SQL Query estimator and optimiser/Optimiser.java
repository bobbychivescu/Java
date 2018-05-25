package sjdb;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class Optimiser {

	private Catalogue c;
	private ArrayList<NamedRelation> rels;
	private ArrayList<Predicate> preds;
	private ArrayList<Attribute> projects;
	private Estimator est;

	public Optimiser(Catalogue cat) {
		c = cat;
		rels = new ArrayList<NamedRelation>();
		preds = new ArrayList<Predicate>();
		projects = new ArrayList<Attribute>();
		est = new Estimator();
	}

	public Operator optimise(Operator plan) {
		breakdownPlan(plan);
		ArrayList<Operator> scans = getScans();
		Operator o = scans.size()>=2 ? orderJoins(scans) : scans.get(0);
		o.accept(est);
		if(!projects.isEmpty())
			o = pushProject(o, projects);
		return o;
	}

	private Operator pushProject(Operator o, ArrayList<Attribute> prj) {
		switch (o.getClass().getName()) {

		case "sjdb.Select" :
			if (o.getOutput().getAttributes().size() == prj.size())
				return o;
			else
				return new Project(o, prj);
		case "sjdb.Product" :
			Product p = (Product) o;
			ArrayList<Attribute> left = new ArrayList<Attribute>();
			ArrayList<Attribute> right = new ArrayList<Attribute>();
			for(Attribute a : prj)
				if(p.getLeft().getOutput().getAttributes().contains(a))
					left.add(a);
				else right.add(a);
			Product result = new Product(pushProject(p.getLeft(), left), pushProject(p.getRight(), right));
			result.accept(est);
			if (result.getOutput().getAttributes().size() == prj.size())
				return result;
			else
				return new Project(result, prj);
		case "sjdb.Scan" :
			if (o.getOutput().getAttributes().size() == prj.size())
				return o;
			else
				return new Project(o, prj);
		case "sjdb.Join" :
			Join j = (Join) o;
			ArrayList<Attribute> left2 = new ArrayList<Attribute>();
			ArrayList<Attribute> right2 = new ArrayList<Attribute>();
			for(Attribute a : prj)
				if(j.getLeft().getOutput().getAttributes().contains(a))
					left2.add(a);
				else right2.add(a);
			
			Attribute l = j.getPredicate().getLeftAttribute();
			Attribute r = j.getPredicate().getRightAttribute();
			
			if(j.getLeft().getOutput().getAttributes().contains(l)) {
				if (!left2.contains(l)) left2.add(l);
			}
			else if (!right2.contains(l)) right2.add(l);
			
			if(j.getLeft().getOutput().getAttributes().contains(r)) {
				if (!left2.contains(r)) left2.add(r);
			}
			else if (!right2.contains(r)) right2.add(r);
			
			Join result2 = new Join(pushProject(j.getLeft(), left2), pushProject(j.getRight(), right2), j.getPredicate());
			result2.accept(est);
			if (result2.getOutput().getAttributes().size() == prj.size())
				return result2;
			else
				return new Project(result2, prj);
		}
		return null;
	}

	private Operator orderJoins(ArrayList<Operator> scans) {
		//use all predicates
		PriorityQueue<Operator> q = new PriorityQueue<Operator>(Math.max(1, preds.size()), new Comparator<Operator>() {
			@Override
			public int compare(Operator arg0, Operator arg1) {
				return arg0.getOutput().getTupleCount() - arg1.getOutput().getTupleCount();
			}

		});

		for(Operator op : scans) op.accept(est);

		Join next=null;
		while(!preds.isEmpty()) {
			for(Predicate p : preds) {
				Join j = makeJoin(scans, p);
				j.accept(est);
				q.add(j);
			}
			//construct tree
			next =(Join) q.poll();
			q.removeAll(q);
			preds.remove(next.getPredicate());
			scans.remove(next.getLeft());
			scans.remove(next.getRight());
			scans.add(next);
		}
		scans.remove(next);
		if(!scans.isEmpty()) {
			q.addAll(scans);
			Product prod = new Product(next!=null ? next : q.poll(), q.poll());
			while(!q.isEmpty()) prod = new Product(prod, q.poll());
			return prod;
		}
		else return next;
	}

	private Join makeJoin(ArrayList<Operator> stuff, Predicate p) {
		Operator l=null, r=null;
		for(Operator o : stuff)
			if(o.getOutput().getAttributes().contains(p.getLeftAttribute())) {
				l=o;
				break;
			}

		for(Operator o : stuff)
			if(o.getOutput().getAttributes().contains(p.getRightAttribute())) {
				r=o;
				break;
			}
		if(r.getOutput().getTupleCount()<l.getOutput().getTupleCount()) {
			Operator aux = r;
			r=l;
			l=aux;
		}
		return new Join(l, r, p);
	}

	private ArrayList<Operator> getScans() {
		ArrayList<Operator> scans = new ArrayList<Operator>();
		for(NamedRelation r : rels) {
			Operator op = new Scan(r);
			Iterator<Predicate> it = preds.iterator();
			while(it.hasNext()) {
				Predicate p = it.next();
				if(p.equalsValue() && r.getAttributes().contains(p.getLeftAttribute())) {
					op = makeSelect(op, p);
					it.remove();
				}
			}
			scans.add(op);
		}
		return scans;
	}

	private Operator makeSelect(Operator op, Predicate p) {
		if (op instanceof Scan)
			return new Select(op, p);
		else {
			//order selects
			Operator input =  ( (Select) op).getInput();
			Operator aux = new Select(input, p);
			op.accept(est);
			aux.accept(est);
			if(op.getOutput().getTupleCount()<= aux.getOutput().getTupleCount())
				return new Select(op, p);
			else
				return new Select(makeSelect(input, p), ((Select) op).getPredicate());
		}
	}

	private void breakdownPlan(Operator plan) {
		switch (plan.getClass().getName()) {

		case "sjdb.Project" :
			Iterator<Attribute> i = ((Project)plan).getAttributes().iterator();
			while(i.hasNext()) projects.add(i.next());
			breakdownPlan (((UnaryOperator)plan).getInput());
			break;
		case "sjdb.Select" :
			preds.add(((Select)plan).getPredicate());
			breakdownPlan(((UnaryOperator)plan).getInput());
			break;
		case "sjdb.Product" :
			breakdownPlan(((BinaryOperator)plan).getLeft());
			breakdownPlan(((BinaryOperator)plan).getRight());
			break;
		case "sjdb.Scan" :
			rels.add((NamedRelation) ((Scan)plan).getRelation());
			break;
		}

	}



}
