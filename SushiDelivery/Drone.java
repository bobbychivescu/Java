import java.io.Serializable;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Drone implements Runnable,Displayable, Serializable {

	private String id;
	private int speed;
	BusinessApp app;
	StockManagement sm;
	private String status;
	private boolean active;

	public Drone(String id, int speed, BusinessApp app){
		sm=app.getSM();
		this.app = app;
		this.id = id;
		this.speed = speed;
		status = "Idle";
		active=true;
	}

	@Override
	public void run() {
		//first - restock
		//second - deliver
		System.out.println("Thread started: Drone "+id);
		Ingredient i=null;
		BigOrder o=null;
		while(active){
			i = sm.restockQ.poll();
			if(i==null){
				o=sm.deliveryQ.poll();
				if(o==null)
					synchronized(sm.lockDrone){
						while(i==null && o == null){
							status = "Idle";
							try {
								sm.lockDrone.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							i = sm.restockQ.poll();
							if(i==null)
								o=sm.deliveryQ.poll();
						}
					}
				
			}
			if(i!=null){
				status = "Restocking "+i.getName();
				try {
					//time in hours
					double t = (double)i.getSup().getDistance()/speed;
					Thread.sleep((int) (t *3600 *1000 *2)); //*2 for return
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i.restock(i.getRestockLevel());
				System.out.println("drone "+id+" restocked "+i.getName());
			}else{
				status = "Delivering to "+o.getTo();
				o.changeStatus("Dispatched");
				try {
					double t = (double) app.getClient(o.getTo()).getDistance()/speed;
					Thread.sleep((int) (t *3600000));
					o.changeStatus("Delivered");
					//and to get back
					Thread.sleep((int) (t *3600000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				System.out.println("drone "+id+" delivered to " + o.getTo());
			}
		}
		System.out.println("Thread stopped: "+toString());
	}

	@Override
	public void view() {
		JFrame frame = new SmallFrame(toString());
		frame.add(new JLabel("Status: "+ status));
		frame.add(new JLabel("Speed: "+ speed+ "km/h"));
		frame.setVisible(true);

	}

	@Override
	public void edit() {
		JFrame frame = new SmallFrame(toString());
		frame.add(new JLabel("Cannot edit drones!"));
		frame.add(new JLabel("Only add new ones or remove old ones"));
		frame.setVisible(true);

	}
	
	public String toString(){
		return "Drone "+id;
	}

	public void deactivate(){
		active=false;
	}
}
