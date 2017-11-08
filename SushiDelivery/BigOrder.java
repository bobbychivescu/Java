import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class BigOrder implements Displayable, Serializable{
	
	//id in the current session
	private static int n=1;
	private HashMap<SushiDish, Integer> order;
	private String to;
	//to break it into small, processable orders
	private ArrayList<Order> list;
	private String status;
	private int id;
	
	public BigOrder(HashMap<SushiDish, Integer> order, String to) {
		this.order = order;
		this.to = to;
		list = new ArrayList<Order>();
		breakdown();
		status = "Queued";
		id=n++;
	}
	
	private void breakdown(){ 
		for(SushiDish d: order.keySet()){
			int q = order.get(d);
			int r = d.getRestockLevel();
			while(q>r){
				list.add(new Order(d, r));
				q-=r;
			}
			list.add(new Order(d, q));
		}
	}
	
	public String getTo(){
		return to;
	}
	
	public ArrayList<Order> getList(){
		return list;
	}
	
	public boolean isComplete(){
		boolean complete = true;
		for(Order o: list)
			if(!o.isComplete())
				complete=false;
		return complete;
	}
	
	public void changeStatus(String status){
		this.status = status;
	}
	
	public String getStatus(){
		return status;
	}

	@Override
	public void view() {
		JFrame frame = new SmallFrame(toString());
		frame.add(new JLabel("To: "+ to));
		frame.add(new JLabel("Status: "+ status));
		frame.add(new JLabel("Dishes:"));
		int price=0;
		for(SushiDish i: order.keySet()){
			frame.add(new JLabel("      "+order.get(i)+" "+i.getName()));
			price+=(order.get(i)*i.getPrice());
		}
		frame.add(new JLabel("Total price: £"+ price));
		frame.setVisible(true);
	}

	@Override
	public void edit() {
		JFrame frame = new SmallFrame(toString());
		frame.add(new JLabel("Cannot edit orders!"));
		frame.setVisible(true);
	}
	
	@Override
	public String toString(){
		return "Order "+id+" to "+to;
	}
	
}
