import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StockManagement implements Serializable{

	ArrayList<Supplier> suppliers;
	ArrayList<Ingredient> ingredients;
	ArrayList<SushiDish> dishes;
	Queue<SushiDish> preparingQ;
	Queue<BigOrder> deliveryQ;
	ArrayList<BigOrder> waitingQ;
	Queue<Ingredient> restockQ;
	
	//initalized in app.init()
	transient Object lockStaff, lockDrone, lockOrder;
	transient DefaultListModel<BigOrder> orderList;
	transient ArrayList<BigOrder> removed;

	public StockManagement(){
		suppliers = new ArrayList<Supplier>();
		ingredients = new ArrayList<Ingredient>();
		dishes = new ArrayList<SushiDish>();
		preparingQ = new ArrayDeque<SushiDish>();
		deliveryQ = new ArrayDeque<BigOrder>();
		waitingQ = new ArrayList<BigOrder>();
		restockQ = new ArrayDeque<Ingredient>();
	}

	public void order(BigOrder o){
		waitingQ.add(o);
		//to update in the GUI
		orderList.addElement(o);
		synchronized(lockOrder){
			lockOrder.notify();
		}
	}

	//checks restock for a dish and its ingredients
	public void checkRestock(SushiDish d){
		System.out.println("Checking for "+d.getName());
		for(Ingredient i: d.getRecipe().keySet())
			if(i.getQuantity()<i.getRestockLevel())
				//dont restock the same ingredient multiple times
				if(!restockQ.contains(i))
					restockQ.add(i);
		if(!restockQ.isEmpty()){
			synchronized(lockDrone){
				lockDrone.notifyAll();
			}
		}
		int n = d.getRestockLevel();
		if(n-d.getQuantity()>0){
			while(n-->0){
				preparingQ.add(d);
			}
			synchronized (lockStaff){
				lockStaff.notifyAll();
			}
		}
	}
	
	public void removeDish(SushiDish sd, DefaultListModel list){
		//will remove the dish from the menu, however clients will still be able
		//to see it and order it if they don't press refresh
		dishes.remove(sd);
		list.removeElement(sd);
	}
	
	public void addOrder(){
		JFrame frame = new SmallFrame(toString());
		frame.add(new JLabel("Cannot add orders, you are not a client"));
		frame.setVisible(true);
	}
	
	public void removeOrder(BigOrder o){
		//this will remove the order from the list, however it won't
		//cancel it, cuz a respectable business doesn't do that
		orderList.removeElement(o);
		//add it to removed only if complete
		if(o.getStatus().equals("Dispatched") || o.getStatus().equals("Delivered")){
			if(!removed.contains(o))
				removed.add(o);
			//will substract removed from waitingQ when serializing
		}
	}
	
	public void removeAllCompleted(){
		for(BigOrder o : waitingQ)
			if(o.getStatus().equals("Dispatched") || o.getStatus().equals("Delivered")){
				orderList.removeElement(o);
				if(!removed.contains(o))
					removed.add(o);
				//will substract removed from waitingQ when serializing
			}
	}
	
	public void setOrderList(DefaultListModel l){
		orderList=l;
	}

	//small GUIs to manage adding and removing suppliers, ingredients and dishes
	
	public void addSupplier(DefaultListModel list) {
		JFrame frame = new SmallFrame("Add Supplier");
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Name: "));
		JTextField t1 = new JTextField(10);
		p.add(t1);
		frame.add(p);

		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Distance: "));
		JTextField t2 = new JTextField(10);
		p.add(t2);
		frame.add(p);

		JButton b = new JButton("OK");
		b.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = t1.getText();
				int d = Integer.valueOf(t2.getText());
				if(name.equals("")){
					new SmallFrame("Error").message("Plese give the supplier a name");
				}else{
					Supplier sup =new Supplier(name, d); 
					suppliers.add(sup);
					list.addElement(sup);
					frame.dispose();
				}
			}
		});

		frame.add(b);
		frame.setVisible(true);
	}

	public void removeSupplier(Supplier sup, DefaultListModel list){
		for(Ingredient i: ingredients)
			if(i.getSup().equals(sup)){
				new SmallFrame("Error").message("Please change supplier of "+i.getName()+" before removing");
				return;
			}
		suppliers.remove(sup);
		list.removeElement(sup);
	}

	public void addIngredient(DefaultListModel list){
		JFrame frame = new SmallFrame("Add Ingredient");
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Name: "));
		JTextField t1 = new JTextField(10);
		p.add(t1);
		frame.add(p);

		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Supplier"));
		Supplier[] s =new Supplier[suppliers.size()];
		suppliers.toArray(s);
		JComboBox<Supplier> box = new JComboBox<Supplier>(s);
		p.add(box);
		frame.add(p);

		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Restock level: "));
		JTextField t2 = new JTextField(5);
		p.add(t2);
		frame.add(p);

		JCheckBox liq = new JCheckBox("liquid");
		frame.add(liq);

		StockManagement sm =this;
		JButton b = new JButton("OK");
		b.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = t1.getText();
				Supplier sup = (Supplier) box.getSelectedItem();
				int d = Integer.valueOf(t2.getText());
				if(name.equals("")){
					new SmallFrame("Error").message("Plese give the supplier a name");
				}else{
					Ingredient i = new Ingredient(name, liq.isSelected(), sup, d, sm);
					ingredients.add(i);
					list.addElement(i);
					frame.dispose();
				}
			}
		});
		frame.add(b);
		frame.setVisible(true);
	}

	public void removeIngredient(Ingredient i, DefaultListModel list){
		for(SushiDish sd: dishes)
			if(sd.getRecipe().containsKey(i)){
				new SmallFrame("Error").message(i.getName()+" is in the recipe of "+sd.getName());
				return;
			}
		ingredients.remove(i);
		list.removeElement(i);
	}

	public void addDish(DefaultListModel list){
		JFrame frame = new SmallFrame("Add Dish");
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Name: "));
		JTextField t = new JTextField(10);
		p.add(t);
		frame.add(p);

		//the restock
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Restock level"));
		JTextField t1 = new JTextField(5);
		p.add(t1);
		frame.add(p);
		//the price
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Price"));
		JTextField t2 = new JTextField(5);
		p.add(t2);
		frame.add(p);
		//the description
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Description"));
		JTextField t3 = new JTextField(15);
		p.add(t3);
		frame.add(p);
		//the recipe... add and remove one ingredient at a time
		HashMap<Ingredient, Integer> recipe = new HashMap<Ingredient, Integer>();
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Recipe: "));
		Ingredient[] ing = new Ingredient[ingredients.size()];
		ingredients.toArray(ing);
		JComboBox<Ingredient> box = new JComboBox<Ingredient>(ing);
		JTextField t4 = new JTextField(5); //quantity of the selected ingredient
		JButton add = new JButton("Add");
		JButton remove = new JButton("Remove");
		p.add(box);
		p.add(t4);
		p.add(add);
		p.add(remove);
		add.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Ingredient i = (Ingredient) box.getSelectedItem();
				int q = Integer.valueOf(t4.getText());
				if(i!=null)
					recipe.put(i, q);
			}
		});
		remove.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Ingredient i = (Ingredient) box.getSelectedItem();
				//no need for quantity when removing
				if(i!=null)
					recipe.remove(i);
			}
		});
		frame.add(p);
		StockManagement sm =this;
		//buttons
		JButton b = new JButton("OK");
		b.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = t.getText();
				int restockLevel=Integer.valueOf(t1.getText());
				int price=Integer.valueOf(t2.getText());
				String description=t3.getText();
				if(name.equals("")){
					new SmallFrame("Error").message("Plese give the supplier a name");
				}else{
					SushiDish d = new SushiDish(name, description, price, recipe, restockLevel, sm);
					dishes.add(d);
					list.addElement(d);
					checkRestock(d);
					frame.dispose();
				}
			}
		});

		frame.add(b);
		frame.setVisible(true);
	}

}
