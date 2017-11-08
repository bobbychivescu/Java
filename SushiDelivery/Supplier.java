
import java.io.Serializable;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Supplier implements Displayable, Serializable {
	
	private String name;
	private int distance;
	
	public Supplier(String name, int distance) {
		this.name = name;
		this.distance = distance;
	}

	public String getName() {
		return name;
	}

	public int getDistance() {
		return distance;
	}

	@Override
	public void view() {
		JFrame frame = new SmallFrame(name);
		frame.add(new JLabel("Distance to store: "+ distance));
		frame.setVisible(true);
		
	}

	@Override
	public void edit() {
		JFrame frame = new SmallFrame(name);
		frame.add(new JLabel("Cannot edit suppliers!"));
		frame.add(new JLabel("Only add new ones or remove old ones"));
		frame.setVisible(true);
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public boolean equals(Supplier sup){
		if(this.name.equals(sup.getName()) && this.distance == sup.getDistance())
			return true;
		return false;
	}
	
}