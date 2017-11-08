import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Comms {

	private ObjectInputStream fromBusiness;
	private ObjectOutputStream toBusiness;
	private Socket business;
	
	//the business will use the statics, so it can communicate
	//with multiple clients at the same time
	static private ServerSocket ss;
	static private Socket client;
	static private ObjectInputStream fromClient;
	static private ObjectOutputStream toClient;
	

	//called by business thread
	static public void connectServer(){
		try {
			ss=new ServerSocket(60300);
			System.out.println("connected server");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static public void pickClient(){
		try {
			client=ss.accept();
			toClient = new ObjectOutputStream(client.getOutputStream());
			fromClient = new ObjectInputStream(client.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//called by business
	static public void sendMessage(Object content) throws IOException{
		//"" means access denied -> content=""
		String type = "";
		if(content instanceof ArrayList<?>)
			type="Update";
		Message m = new Message(type, content);
		toClient.writeObject(m);
		toClient.flush();
	}

	//called by business
	static public Object receiveMessage() throws ClassNotFoundException, IOException{
		Message m = (Message) fromClient.readObject();
		if (m.getType().equals("NewUser"))
			return (Client) m.getContent();
		if (m.getType().equals("Login"))
			return (String) m.getContent();
		if (m.getType().equals("Order"))
			return (BigOrder) m.getContent();
		//case refresh (type == "" /content == username)
		return (String) m.getContent();
	}

	public void connectClient(){
		try {
			business=new Socket("localhost", 60300);
			System.out.println("connected client");
			toBusiness = new ObjectOutputStream(business.getOutputStream());
			fromBusiness = new ObjectInputStream(business.getInputStream());


		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//called by client
	public void cSendMessage(Object content) throws IOException{
		//case refresh (content == username)
		String type = "";
		if(content instanceof Client)
			type="NewUser";
		if(content instanceof BigOrder)
			type="Order";
		if(content instanceof String && ((String)content).contains(" ")) //has space
			type="Login";
		Message m = new Message(type, content);
		toBusiness.writeObject(m);
		toBusiness.flush();
	}

	//called by client
	public Object cReceiveMessage() throws IOException, ClassNotFoundException{
		Message m = (Message) fromBusiness.readObject();
		if (m.getType().equals("Update"))
			return (ArrayList<?>) m.getContent();
		//either receive arrays (dish/order) or null (access denied)
		return null;
	}
}
