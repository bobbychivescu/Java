import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataPersistence {
	
	static void saveState(BusinessApp app){
		//before closing, we properly remove the completed orders
		for(BigOrder o: app.getSM().removed)
			app.getSM().waitingQ.remove(o);
		//and complete any dispatched orders
		for(BigOrder o: app.getSM().waitingQ)
			if(o.getStatus().equals("Dispatched"))
				o.changeStatus("Delivered");
		File outFile = new File("business.data");
        FileOutputStream fos;
        ObjectOutputStream oos;
		try {
			fos = new FileOutputStream(outFile);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(app);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	static BusinessApp getState(){
		File inFile = new File( "business.data" );
        FileInputStream fis;
        ObjectInputStream ois;
        BusinessApp app = null;
		try {
			fis = new FileInputStream(inFile);
			ois = new ObjectInputStream(fis);
			app = (BusinessApp) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return app;
	}
}
