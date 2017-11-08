import java.io.Serializable;
import java.util.ArrayList;

public class Client implements Serializable {

	private String username;
	private String password;
	private String address;
	private Postcode postcode;
	private ArrayList<BigOrder> orders;
	
	public Client(String username, String password, String address, Postcode postcode) {
		this.username = username;
		this.password = password;
		this.address = address;
		this.postcode = postcode;
		orders = new ArrayList<BigOrder>();
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getAddress() {
		return address;
	}

	public int getDistance() {
		return postcode.getDistance();
	}
	
	public void addOrder(BigOrder o){
		orders.add(o);
	}

	public ArrayList<BigOrder> getOrders() {
		return orders;
	}
	
}
