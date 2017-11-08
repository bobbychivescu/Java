import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Main {

	public static void main(String[] args) {

		BusinessApp app = DataPersistence.getState();
		if(app==null)
			app = new BusinessApp();
		app.init();
		
		new ClientApp().init();

/*
 * the business.data file has a possible configuration, to save time when testing
 * if the busines.data file is missing from the root folder, the app will start
 * from point zero (no drones, staff, ingredients etc). There are 2 client accounts:
 * 		user1 : pass		user2 : pass
 * 
 * in the given configuration, the drones have very high speed (1000), in order for
 * them to process tasks fast (otherwise it would take a few minutes to do anything)
 * When adding new drones, I recommend giving them a high speed for the purpose of testing
 * 
 * in the SushiDish class, the waiting time for preparation is 20-60 seconds. Again, for
 * the purpose of testing, I recommend deleting a zero
 * 
 * the apps are coded such that more client apps could run at the same time
 * the client and business apps could run on different machines if "localhost"
 * would be changed to the server IP in the Comms.connectClient method. I tested it
 * and it worked fine.
 * 
 * the exception from parsing numbers are ignored, because they don't affect the app in any way
 * 
 * to update the orders and the menu, the client has to press refresh
 * 
 */
		
	}
}
