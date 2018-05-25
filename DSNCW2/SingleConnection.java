import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SingleConnection implements Runnable{

	private int a;
	private CiphertextInterface stub;
	private PrintWriter out ;
	private BufferedReader in;

	public SingleConnection(Socket s, int a, CiphertextInterface stub) throws IOException {
		super();
		
		//construction of the object from arguments
		this.a = a;
		this.stub = stub;
		out = new PrintWriter(s.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}

	public int powMod(int b, int p, int m) {
		int i=1;
		//take modulo at each step
		for(int j=1; j<=p; ++j) i = (i*b)%m;
		return i;
	}

	@Override
	public void run() {
		String username;
		//System.out.println("Trying to communicate...");
		try {
			//recieve connection request
			username = in.readLine();
			
			//computing and sending x, p and g
			out.println(this.powMod(MyServer.g, a, MyServer.p) + " " + MyServer.p + " " + MyServer.g);
			
			//read y from client and compute key
			int key, y = Integer.parseInt(in.readLine());
			key=this.powMod(y, a, MyServer.p);
			
			//send ciphertext to client
			out.println(stub.get(username, key));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
