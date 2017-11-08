import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

public class ClientAppView extends JFrame {

	ClientApp app;
	DefaultListModel<SushiDish> menu;
	DefaultListModel<BigOrder> orders;
	DefaultListModel<SushiDishClientDisplay> currentOrder;
	public ClientAppView(ClientApp app, ArrayList<SushiDish> menu, ArrayList<BigOrder> orders) {
		super("SushiDelivery");
		this.setSize(1200, 500);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.app = app;

		this.menu = new DefaultListModel<SushiDish>();
		for(SushiDish sd: menu)
			this.menu.addElement(sd);

		this.orders = new DefaultListModel<BigOrder>();
		for(BigOrder o: orders)
			this.orders.addElement(o);
		currentOrder = new DefaultListModel<SushiDishClientDisplay>();
		init();
		this.setVisible(true);
	}

	private void init(){
		this.getContentPane().setLayout(new BorderLayout());

		JButton ref, logout;
		ref = new JButton("Refresh");
		logout = new JButton("Log out");

		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(ref);
		p.add(logout);
		this.add(p, BorderLayout.NORTH);

		JFrame frame = this;
		logout.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				app.setUsername(null);
				new ClientAppLoginView(app);
				frame.dispose();
			}
		});

		ref.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Comms c = app.getComms();
				c.connectClient();
				try {
					//when refreshing, we get the updated menu and orders
					c.cSendMessage(app.getUsername());
					Object o = c.cReceiveMessage();
					Object other = c.cReceiveMessage();
					menu.removeAllElements();
					for(SushiDish sd: (ArrayList<SushiDish>) other)
						menu.addElement(sd);
					orders.removeAllElements();
					for(BigOrder or: (ArrayList<BigOrder>) o)
						orders.addElement(or);

				}catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});

		//the lists
		p = new JPanel();
		p.setLayout(new GridLayout(1, 3));

		CategoryPane2<SushiDish> cp =new CategoryPane2<SushiDish>(menu, "Sushi Dishes", app);
		cp.addSouthPanel(menuButtons(cp));
		p.add(cp);

		CategoryPane2<SushiDishClientDisplay> cp2 = new CategoryPane2<SushiDishClientDisplay>(currentOrder, "Current Order", app);
		cp2.addSouthPanel(basketButtons(cp2));
		p.add(cp2);

		p.add(new CategoryPane2<BigOrder>(orders, "Orders", app));

		this.add(p, BorderLayout.CENTER);
	}

	private JPanel basketButtons(CategoryPane2<SushiDishClientDisplay> cp2) {
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT, 50, 5));

		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout());
		p1.add(new JLabel("Total price: £"));
		p1.add(app.getPriceTag());
		p.add(p1);

		JButton remove = new JButton("Remove");
		p.add(remove);

		remove.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				SushiDishClientDisplay d = cp2.getSelected();
				if(d!=null){
					app.removeFromOrder(d.getD(), d.getQ());
					currentOrder.removeElement(d);
					app.setPrice(app.getPrice()-d.getQ()*d.getD().getPrice());
				}
			}
		});

		return p;
	}

	private JPanel menuButtons(CategoryPane2<SushiDish> cp){
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Quantity: "));
		Integer [] val = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		JComboBox<Integer> box = new JComboBox<Integer>(val);
		p.add(box);
		JButton add = new JButton("Add");
		p.add(add);

		add.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				SushiDish d = cp.getSelected();
				if(d!=null){
					int q = (int) box.getSelectedItem();
					app.addToORder(d, q);
					currentOrder.addElement(new SushiDishClientDisplay(d, q));
					app.setPrice(app.getPrice()+q*d.getPrice());
				}
			}	
		});
		return p;
	}

}
