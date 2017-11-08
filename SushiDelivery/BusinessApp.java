import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BusinessApp implements Serializable {

	private StockManagement sm;
	ArrayList<KitchenStaff> staff;
	ArrayList<Drone> drones;
	ArrayList<Client> clients;
	
	public BusinessApp(){
		sm = new StockManagement();
		staff = new ArrayList<KitchenStaff>();
		drones = new ArrayList<Drone>();
		clients = new ArrayList<Client>();
	}
	
	public void init(){
		sm.lockStaff = new Object();
		sm.lockDrone = new Object();
		sm.lockOrder = new Object();
		sm.removed = new ArrayList<BigOrder>();
		//initializing the hread that deals with orders
		new Thread(new WaitingOrder(sm)).start();
		//start listening to clients
		Comms.connectServer();
		new Thread(new InterpretMessage(this)).start();
		
		//start staff and drone threads
		for(KitchenStaff s: staff)
			new Thread(s).start();
		for(Drone d: drones)
			new Thread(d).start();
		
		//the GUI
		new BusinessAppView(this);
	}
	
	public StockManagement getSM(){
		return sm;
	}
	
	public boolean newClient(Client c){
		for(Client client: clients)
			if(client.getUsername().equals(c.getUsername()))
				return false;
		clients.add(c);
		return true;
	}
	
	public Client getClient(String username){
		for(Client client: clients)
			if(client.getUsername().equals(username))
				return client;
		return null;
	}
	
	//used for login
	public ArrayList<BigOrder> validClient(String s){
		String[] fields = s.split(" ");
		for(Client client: clients)
			if(client.getUsername().equals(fields[0]) && client.getPassword().equals(fields[1]))
				return client.getOrders();
		return null;
	}
	
	public ArrayList<BigOrder> clientOrders(String username){
		for(Client client: clients)
			if(client.getUsername().equals(username))
				return client.getOrders();
		return null;
	}
	
	public void addOrderToClient(BigOrder o){
		for(Client client: clients)
			if(client.getUsername().equals(o.getTo())){
				client.addOrder(o);
				break;
			}
		sm.order(o);
	}
	
	//GUI for adding new staff
	//here we'll start each new staff and drone as we add them 
	public void addStaff(DefaultListModel list){
		JFrame frame = new SmallFrame("Add Staff");
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Name: "));
		JTextField t = new JTextField(10);
		p.add(t);
		frame.add(p);
		
		//buttons
		JButton b = new JButton("OK");
		b.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = t.getText();
				if(name.equals("")){
					new SmallFrame("Error").message("Plese give the staff a name");
				}else{
					KitchenStaff s = new KitchenStaff(name, sm);
					staff.add(s);
					new Thread(s).start();
					list.addElement(s);
					frame.dispose();
				}
			}
		});

		frame.add(b);
		frame.setVisible(true);
	}
	
	//GUI for adding a drone
	public void addDrone(DefaultListModel list){
		JFrame frame = new SmallFrame("Add Drone");
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("ID: "));
		JTextField t = new JTextField(10);
		p.add(t);
		frame.add(p);
		
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("Speed: "));
		JTextField t1 = new JTextField(5);
		p.add(t1);
		frame.add(p);
		
		//buttons
		BusinessApp app = this;
		JButton b = new JButton("OK");
		b.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = t.getText();
				int speed = Integer.valueOf(t1.getText()); //exception won't crash the app
				if(name.equals("")){
					new SmallFrame("Error").message("Plese give the drone an ID");
				}else{
					Drone d = new Drone(name, speed, app);
					drones.add(d);
					new Thread(d).start();
					list.addElement(d);
					frame.dispose();
				}
			}
		});

		frame.add(b);
		frame.setVisible(true);
	}
	
}
