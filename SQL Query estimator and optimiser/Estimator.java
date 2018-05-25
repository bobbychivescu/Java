package sjdb;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Estimator implements PlanVisitor {


	public Estimator() {
		// empty constructor
	}

	/* 
	 * Create output relation on Scan operator
	 *
	 * Example implementation of visit method for Scan operators.
	 */
	public void visit(Scan op) {
		Relation input = op.getRelation();
		Relation output = new Relation(input.getTupleCount());
		
		Iterator<Attribute> iter = input.getAttributes().iterator();
		while (iter.hasNext()) {
			output.addAttribute(new Attribute(iter.next()));
		}
		
		op.setOutput(output);
	}

	public void visit(Project op) {
		
		Relation input = op.getInput().getOutput();
		Relation output = new Relation(input.getTupleCount());
		
		List<Attribute> atts = input.getAttributes();
		for(Attribute a : op.getAttributes()) {
			//throw exp if att doesn't exist in rel?
			
			int i = atts.indexOf(a);
			output.addAttribute(new Attribute(atts.get(i)));
		}
		
		op.setOutput(output);
	}
	
	public void visit(Select op) {
		Relation input = op.getInput().getOutput();
		Predicate p = op.getPredicate();
		Relation output;
		if(p.equalsValue()) {
			//attr=value
			output=new Relation (input.getTupleCount()/input.getAttribute(p.getLeftAttribute()).getValueCount());
			for(Attribute a : input.getAttributes()) {
				int n;
				if (a.equals(p.getLeftAttribute()))
					n=1;
				else
					n=Math.min(output.getTupleCount(), a.getValueCount());
				output.addAttribute(new Attribute(a.getName(), n));
			}
			
		}else {
			//attr = attr
			int left = input.getAttribute(p.getLeftAttribute()).getValueCount();
			int right = input.getAttribute(p.getRightAttribute()).getValueCount();
			output = new Relation(input.getTupleCount()/Math.max(left, right));
			for(Attribute a : input.getAttributes()) {
				int n;
				if (a.equals(p.getLeftAttribute()) || a.equals(p.getRightAttribute()))
					n=Math.min(left, right);
				else
					n=Math.min(output.getTupleCount(), a.getValueCount());
				output.addAttribute(new Attribute(a.getName(), n));
			}
			
			
		}
		
		op.setOutput(output);
		
	}
	
	public void visit(Product op) {
		Relation leftInput = op.getLeft().getOutput();
		Relation rightInput = op.getRight().getOutput();
		
		Relation output = new Relation(leftInput.getTupleCount() * rightInput.getTupleCount());
		for(Attribute a : leftInput.getAttributes())
			output.addAttribute(new Attribute(a));
		for(Attribute a : rightInput.getAttributes())
			output.addAttribute(new Attribute(a));
		
		op.setOutput(output);
	}
	
	public void visit(Join op) {
		Relation leftInput = op.getLeft().getOutput();
		Relation rightInput = op.getRight().getOutput();
		Predicate p = op.getPredicate();

		int left , right;
		left = right = 0;
		
		for(Attribute a : leftInput.getAttributes())
			if(a.equals(p.getLeftAttribute()))
				left = a.getValueCount();
			else if (a.equals(p.getRightAttribute()))
				right = a.getValueCount();
		
		for(Attribute a : rightInput.getAttributes())
			if(a.equals(p.getLeftAttribute()))
				left = a.getValueCount();
			else if (a.equals(p.getRightAttribute()))
				right = a.getValueCount();
		
		Relation output = new Relation(leftInput.getTupleCount() * rightInput.getTupleCount() / Math.max(left, right));
		for(Attribute a : leftInput.getAttributes()){
			int n;
			if (a.equals(p.getLeftAttribute()) || a.equals(p.getRightAttribute()))
				n=Math.min(left, right);
			else
				n=Math.min(output.getTupleCount(), a.getValueCount());
			output.addAttribute(new Attribute(a.getName(), n));
		}
		
		for(Attribute a : rightInput.getAttributes()){
			int n;
			if (a.equals(p.getLeftAttribute()) || a.equals(p.getRightAttribute()))
				n=Math.min(left, right);
			else
				n=Math.min(output.getTupleCount(), a.getValueCount());
			output.addAttribute(new Attribute(a.getName(), n));
		}
		
		op.setOutput(output);
		
	}
}
