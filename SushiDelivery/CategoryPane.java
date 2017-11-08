import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class CategoryPane<T> extends JPanel {

	ArrayList<T> elements=null;
	DefaultListModel<T> lm;
	JList<T> list;
	BusinessApp app;

	public CategoryPane(ArrayList<T> elements, String title, BusinessApp app){
		super();
		this.elements=elements;
		this.app=app;
		lm=new DefaultListModel<T>();
		//the order list needs to be updated when a client makes an order
		if(title.equals("Orders"))
			app.getSM().setOrderList(lm);
		list=new JList<T>();
		init(title);
	}

	private void init(String title) {
		this.setLayout(new BorderLayout());
		JLabel label = new JLabel(title);
		label.setFont(new Font("Arial", Font.BOLD, 20));
		this.add(label, BorderLayout.NORTH);

		if(elements!=null)
			for(T e: elements)lm.addElement(e);
		list.setModel(lm);

		JPanel buttons = makeButtons(title);

		this.add(buttons, BorderLayout.EAST);
		this.add(new JScrollPane(list), BorderLayout.CENTER);
	}

	private JPanel makeButtons(String title){
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

		JButton add = new JButton("Add");
		JButton view = new JButton("View");
		JButton edit = new JButton("Edit");
		JButton remove = new JButton("Remove");

		add.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(title.equals("Suppliers"))
					app.getSM().addSupplier(lm);

				if(title.equals("Ingredients"))
					app.getSM().addIngredient(lm);

				if(title.equals("Dishes"))
					app.getSM().addDish(lm);

				if(title.equals("Staff"))
					app.addStaff(lm);

				if(title.equals("Drones"))
					app.addDrone(lm);

				if(title.equals("Orders"))
					app.getSM().addOrder();
			}
		});
		view.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				T el = list.getSelectedValue();
				if(el!=null)
					((Displayable)el).view();

			}
		});
		edit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				T el = list.getSelectedValue();
				if(el!=null)
					((Displayable)el).edit();

			}
		});
		remove.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				T el = list.getSelectedValue();
				if(el!=null){
					if(title.equals("Suppliers"))
						app.getSM().removeSupplier((Supplier)el, lm);

					if(title.equals("Ingredients"))
						app.getSM().removeIngredient((Ingredient)el, lm);

					if(title.equals("Dishes"))
						app.getSM().removeDish((SushiDish)el, lm);

					if(title.equals("Staff")){
						((KitchenStaff) el).deactivate();
						elements.remove(el);
						lm.removeElement(el);
					}

					if(title.equals("Drones")){
						((Drone) el).deactivate();
						elements.remove(el);
						lm.removeElement(el);
					}
					
					if(title.equals("Orders"))
						app.getSM().removeOrder((BigOrder) el);
				}
			}
		});

		pane.add(add);
		pane.add(view);
		pane.add(edit);
		pane.add(remove);
		
		//additional button for the orders panel
		if(title.equals("Orders")){
			JButton remAll = new JButton("Remove all complete");
			remAll.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					app.getSM().removeAllCompleted();
				}
			});
			pane.add(remAll);
		}
		return pane;
	}
}
