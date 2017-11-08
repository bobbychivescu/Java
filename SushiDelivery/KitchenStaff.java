import java.io.Serializable;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class KitchenStaff implements Runnable, Displayable, Serializable {

	private String name;
	private StockManagement sm;
	private String status;
	private boolean active;

	public KitchenStaff(String name, StockManagement s){
		sm=s;
		this.name = name;
		status="Waiting";
		active=true;
	}

	@Override
	public void run() {
		System.out.println("Thread started: "+name);
		SushiDish d;
		while(active){
			d=sm.preparingQ.poll();
			if(d==null)
				synchronized(sm.lockStaff){
					while(d==null){
						try {
							status="Waiting";
							sm.lockStaff.wait();
						} catch (InterruptedException e) {e.printStackTrace();}
						d=sm.preparingQ.poll();
					}
				}
			status="Preparing "+d.getName();
			d.prepare();
			System.out.println(name + " prepared one " + d.getName());
			synchronized(sm.lockOrder){
				//only one thread there, so no need for notifyAll
				sm.lockOrder.notify();
			}
		}
		System.out.println("Thread stopped: "+name);
	}


	@Override
	public void view() {
		JFrame frame = new SmallFrame(name);
		frame.add(new JLabel("Status: "+ status));
		frame.setVisible(true);

	}

	@Override
	public void edit() {
		JFrame frame = new SmallFrame(name);
		frame.add(new JLabel("Cannot edit members of staff!"));
		frame.add(new JLabel("Only add new ones or remove old ones"));
		frame.setVisible(true);

	}
	
	public String toString(){
		return name;
	}
	
	//after it finishes what it is doing, it will stop
	public void deactivate(){
		active=false;
	}
}
