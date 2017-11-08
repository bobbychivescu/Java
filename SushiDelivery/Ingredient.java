import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Ingredient implements Displayable, Serializable {
	private String name;
	//if true, then quantity is measured in liters
	private boolean liquid;
	private int quantity;
	private Supplier sup;
	private int restockLevel;
	private StockManagement sm;

	public Ingredient(String name, boolean liquid, Supplier sup, int restockLevel, StockManagement sm) {
		this.name = name;
		this.liquid = liquid;
		this.quantity = 0;
		this.sup = sup;
		this.restockLevel = restockLevel;
		this.sm=sm;
	}

	public String getName(){
		return name;
	}

	public int getQuantity() {
		return quantity;
	}

	public Supplier getSup() {
		return sup;
	}

	public int getRestockLevel() {
		return restockLevel;
	}

	//called by kitchen staff
	public synchronized void use(int q){
		while(q>quantity){
			try {
				//wait for restock
				wait();
			} catch (InterruptedException e) {}
		}
		quantity-=q;
		notifyAll(); //noify the others waiting
	}
	
	//called by drones
	public synchronized void restock(int q){
		quantity+=q;
		notifyAll();
	}

	@Override
	public void view() {
		JFrame frame = new SmallFrame(name);
		frame.add(new JLabel("Curent quantity in stock: "+ quantity));
		frame.add(new JLabel("Restock level: "+ restockLevel));
		frame.add(new JLabel("Liquid: "+ liquid));
		frame.add(new JLabel("Supplier: "+ sup.toString()));
		frame.setVisible(true);
		
	}

	@Override
	public void edit() {
		JFrame frame = new SmallFrame(name);
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Restock level"));
		JTextField t = new JTextField(5);
		t.setText(""+restockLevel);
		p.add(t);
		frame.add(p);
		
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Supplier"));
		Supplier[] s =new Supplier[sm.suppliers.size()];
		sm.suppliers.toArray(s);
		JComboBox<Supplier> box = new JComboBox<Supplier>(s);
		box.setSelectedItem(sup);
		p.add(box);
		frame.add(p);
		
		JButton b = new JButton("OK");
		b.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				restockLevel=Integer.valueOf(t.getText());
				sup = (Supplier) box.getSelectedItem();
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
