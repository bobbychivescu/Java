import java.io.Serializable;

public class Order implements Serializable {

	private SushiDish dish;
	private int quantity;
	private boolean complete;
	
	//quantity <= dish.restock , otherwise the waiting thread could block
	public Order(SushiDish dish, int quantity) {
		this.dish = dish;
		this.quantity = quantity;
		complete=false;
	}

	public SushiDish getDish() {
		return dish;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public void complete(){
		complete=true;
	}
	
	public boolean isComplete(){
		return complete;
	}
}
