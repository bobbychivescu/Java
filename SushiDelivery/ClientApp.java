import java.io.IOException;
import java.util.HashMap;

import javax.swing.JLabel;

public class ClientApp {

	private Comms comms;
	private String username;
	private HashMap<SushiDish, Integer> order;
	private int price;
	private JLabel priceTag;

	public ClientApp(){
		comms = new Comms();
		order = new HashMap<SushiDish, Integer>();
		price = 0;
		priceTag = new JLabel(price+"");
	}

	public void init(){
		new ClientAppLoginView(this);
	}

	public Comms getComms() {
		return comms;
	}

	public String getUsername() {
		return username;
	}

	//current logged client
	public void setUsername(String username) {
		this.username = username;
		//case logout
		if(username==null){
			price=0;
			priceTag.setText("0");
			order = new HashMap<SushiDish, Integer>();
		}
	}

	public int getPrice(){
		return price;
	}
	
	public void setPrice(int v){
		price = v;
		priceTag.setText(price+"");
	}
	
	public JLabel getPriceTag(){
		return priceTag;
	}
	
	public void placeOrder() {

		comms.connectClient();
		try {
			comms.cSendMessage(new BigOrder(order, username));
		} catch (IOException e) {
			e.printStackTrace();
		}
		price=0;
		priceTag.setText(price+"");
		order = new HashMap<SushiDish, Integer>();
	}

	public void addToORder(SushiDish sd, Integer q){
		if(order.containsKey(sd))
			order.put(sd, q+order.get(sd));
		else
			order.put(sd,  q);
	}

	public void removeFromOrder(SushiDish sd, int q){
		if(order.get(sd)>q)
			order.put(sd, order.get(sd)-q);
		else
			order.remove(sd);
	}
}
