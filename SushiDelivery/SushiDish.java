import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SushiDish implements Displayable, Serializable{

	private String name;
	private String description;
	private int price;
	//an ingredient maps to its quantity needed in this dish
	private HashMap<Ingredient, Integer> recipe;
	private int quantity;
	private int restockLevel;
	private StockManagement sm;

	public SushiDish(String name, String description, int price, HashMap<Ingredient, Integer> recipe, int rL, StockManagement sm) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.recipe = recipe;
		this.quantity=0;
		this.restockLevel=rL;
		this.sm=sm;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getPrice() {
		return price;
	}

	public HashMap<Ingredient, Integer> getRecipe() {
		return recipe;
	}

	public int getRestockLevel() {
		return restockLevel;
	}

	public int getQuantity(){
		return quantity;
	}

	//will be called by waiting order
	public synchronized void dispatch(int q){
		quantity-=q;
	}

	//more staff should prepare at the same time, so no sync
	public void prepare(){
		for(Ingredient i: recipe.keySet())
			i.use(recipe.get(i));
		try {
			//for testing, delete a zero
			Thread.sleep( (int) (Math.random()*40000+20000) );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized (this){
			quantity++;
		}
		//notify waitingOrder - in the staff class
	}

	@Override
	public void view() {
		JFrame frame = new SmallFrame(name);
		frame.add(new JLabel("Curent quantity in stock: "+ quantity));
		frame.add(new JLabel("Restock level: "+ restockLevel));
		frame.add(new JLabel("Price: "+ price));
		frame.add(new JLabel(description));
		frame.add(new JLabel("Recipe:"));
		for(Ingredient i: recipe.keySet())
			frame.add(new JLabel("    "+i.getName()+" "+recipe.get(i)));
		frame.setVisible(true);

	}

	@Override
	public void edit() {
		JFrame frame = new SmallFrame(name);
		//the restock
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Restock level"));
		JTextField t1 = new JTextField(5);
		t1.setText(""+restockLevel);
		p.add(t1);
		frame.add(p);
		//the price
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Price"));
		JTextField t2 = new JTextField(5);
		t2.setText(""+price);
		p.add(t2);
		frame.add(p);
		//the description
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Description"));
		JTextField t3 = new JTextField(15);
		t3.setText(description);
		p.add(t3);
		frame.add(p);
		//the recipe... add and remove one ingredient at a time
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Recipe: "));
		Ingredient[] ing = new Ingredient[sm.ingredients.size()];
		sm.ingredients.toArray(ing);
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

		//buttons
		JButton b = new JButton("OK");
		b.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				restockLevel=Integer.valueOf(t1.getText());
				price=Integer.valueOf(t2.getText());
				description=t3.getText();
				frame.dispose();
			}
		});

		frame.add(b);
		frame.setVisible(true);
	}

	@Override
	public String toString(){
		return name;
	}
}
