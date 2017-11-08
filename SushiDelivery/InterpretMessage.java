import java.io.IOException;
import java.util.ArrayList;

public class InterpretMessage implements Runnable {

	BusinessApp app;
	
	public InterpretMessage(BusinessApp app) {
		this.app = app;
	}

	@Override
	public void run() {
		while(true){
			Comms.pickClient();
			System.out.println("picked a client, now listening");
			try {
				Object o = Comms.receiveMessage();
				
				if(o instanceof Client){
					//case new client -> send menu or null
					if(app.newClient((Client) o)){
						Comms.sendMessage(app.getSM().dishes);
					}else{
						Comms.sendMessage("");
					}
				}else if(o instanceof String && ((String)o).contains(" ")){
					//case login request -> send previous orders and menu
					ArrayList<BigOrder> orders = app.validClient((String) o);
					if(orders!=null){
						Comms.sendMessage(orders);
						Comms.sendMessage(app.getSM().dishes);
					}else{
						Comms.sendMessage("");
					}
				}else if(o instanceof BigOrder){
					//case order ->just add the order
					app.addOrderToClient((BigOrder) o);
				}else{
					//case refresh ->send updated orders and menu
					Comms.sendMessage(app.clientOrders((String) o));
					Comms.sendMessage(app.getSM().dishes);
				}
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
