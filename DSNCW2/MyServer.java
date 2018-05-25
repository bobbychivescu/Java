import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MyServer {

	//constants to use when computing the ley
	public static final int p=199, g=133;

	private ServerSocket ss;
	
	public MyServer() throws IOException {
		ss=new ServerSocket(60300);
		//debug code
		//System.out.println("connected server");
	}

	

	public void listen(CiphertextInterface stub) throws IOException {
		Socket s;
		//variable used to compute key
		//will be increased for each new client
		int a = 13;
		while(true) {
			//System.out.println("Listening...");
			s = ss.accept();
			//System.out.println("Found connection");
			
			//start new thread for the client and continue to listen
			new Thread(new SingleConnection(s, a, stub)).start();;
			a++;
		}
	}


	public static void main(String[] args) throws IOException, NotBoundException {
		
		//setting the proprieties of the system as in the given example
		//same policy file as in the example
		System.setProperty("java.security.policy", "mypolicy");
		if (System.getSecurityManager() == null) 
			System.setSecurityManager(new SecurityManager());
		
		//getting the remote object reference
		Registry r = LocateRegistry.getRegistry("svm-tjn1f15-comp2207.ecs.soton.ac.uk", 12345);
		CiphertextInterface stub = (CiphertextInterface)r.lookup("CiphertextProvider");
		//System.out.println(stub.get("bac1g16", 84));
		
		//starting the server, giving it the remote object reference
		new MyServer().listen(stub);
	}

}
