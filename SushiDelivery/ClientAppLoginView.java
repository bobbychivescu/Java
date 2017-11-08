import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ClientAppLoginView extends JFrame {

	JButton signUp, logIn;
	JTextField user, address, userLog;
	JPasswordField pass, passLog;
	JComboBox<Postcode> postcode;
	ClientApp app;
	
	public ClientAppLoginView(ClientApp app){
		super("SushiDelivery");
		this.setSize(500, 250);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.app = app;
		signUp = new JButton("Sign up");
		logIn = new JButton("Log in");
		user = new JTextField(10);
		pass = new JPasswordField(10);
		address = new JTextField(10);
		userLog = new JTextField(10);
		passLog = new JPasswordField(10);
		postcode = new JComboBox<Postcode>(Postcode.values());
		init();
		this.setVisible(true);
	}
	
	private void init(){
		this.setLayout(new FlowLayout());
		
		JPanel newUser = new JPanel();
		newUser.setLayout(new BoxLayout(newUser, BoxLayout.Y_AXIS));

		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		JLabel l = new JLabel("New User");
		l.setFont(new Font("Arial", Font.BOLD, 16));
		p.add(l);
		newUser.add(p);
		
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("username"));
		p.add(user);
		newUser.add(p);
		
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("password"));
		p.add(pass);
		newUser.add(p);
		
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("address"));
		p.add(address);
		newUser.add(p);
		
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("postcode"));
		p.add(postcode);
		newUser.add(p);
		
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(signUp);
		newUser.add(p);
		
		this.add(newUser);
		
		JPanel existingUser = new JPanel();
		existingUser.setLayout(new BoxLayout(existingUser, BoxLayout.Y_AXIS));

		p = new JPanel();
		p.setLayout(new FlowLayout());
		l = new JLabel("Existing User");
		l.setFont(new Font("Arial", Font.BOLD, 16));
		p.add(l);
		existingUser.add(p);
		
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("username"));
		p.add(userLog);
		existingUser.add(p);
		
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(new JLabel("password"));
		p.add(passLog);
		existingUser.add(p);
		
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(logIn);
		existingUser.add(p);
		
		this.add(existingUser);
		
		addListeners();
	}

	private void addListeners() {
		JFrame frame = this;
		signUp.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String username = user.getText();
				String password = new String(pass.getPassword());
				String addr = address.getText();
				Postcode pc = (Postcode) postcode.getSelectedItem();
				if(username.equals("") || password.equals("") || addr.equals("")){
					new SmallFrame("Error").message("Please introduce valid information in all fields");
				}else{
					Comms c = app.getComms();
					c.connectClient();
					Client client = new Client(username, password, addr, pc);
					try {
						//try to create a new user
						c.cSendMessage(client);
						Object o = c.cReceiveMessage();
						if(o instanceof ArrayList<?>){
							//got a good response, the menu
							app.setUsername(username);
							new ClientAppView(app, (ArrayList<SushiDish>) o, new ArrayList<BigOrder>());
							frame.dispose();
						}
						else{
							new SmallFrame("Error").message("Username already used!");
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		logIn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String username = userLog.getText();
				String password = new String(passLog.getPassword());
				if(username.equals("") || password.equals("")){
					new SmallFrame("Error").message("Please introduce valid information in all fields");
				}else{
					Comms c = app.getComms();
					c.connectClient();
					try {
						//try to log the user
						c.cSendMessage(username+" "+password);
						Object o = c.cReceiveMessage();
						if(o instanceof ArrayList<?>){
							//got the menu and the clinet's previous orders
							Object other = c.cReceiveMessage();
							app.setUsername(username);
							new ClientAppView(app, (ArrayList<SushiDish>) other, (ArrayList<BigOrder>) o);
							frame.dispose();
						}
						else{
							new SmallFrame("Error").message("Wrong username and/or password!");
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
