
//used in the client app to display the contents of the current order in a JList
public class SushiDishClientDisplay{

	private SushiDish d;
	private int q;
	
	public SushiDishClientDisplay(SushiDish d, int q) {
		super();
		this.d = d;
		this.q = q;
	}

	public SushiDish getD() {
		return d;
	}

	public int getQ() {
		return q;
	}

	@Override
	public String toString(){
		return q+" x "+d.getName();
	}

}
