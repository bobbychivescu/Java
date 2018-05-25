import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClient {

	//constant to use when computing the key
	private static final int b = 11;
	
	private Socket s;
	private PrintWriter out;
	private BufferedReader in;


	public MyClient(String host) throws UnknownHostException, IOException {
		//socket used to communicate
		//port is a mandatory argument
		s = new Socket (host, 60300);
		out = new PrintWriter(s.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader (s.getInputStream()));
	}

	public int powMod(int b, int p, int m) {
		int i=1;
		//take modulo at each step
		for(int j=1; j<=p; ++j) i = (i*b)%m;
		return i;
	}

	public String substitute(String s, int n) {
		char [] chars = s.toCharArray();
		for(int i=0; i<chars.length; ++i)
			if(chars[i]-n < 'A')
				chars[i]+=(26-n);
			else chars[i]-=n;
		return new String(chars);
	}
	
	public String transpose(String s, int n) {
		String a = s.substring(n);
		return a+s.substring(0, n);
	}
	
	public String decode(String text, int t, int s) {
		//spliting in chunks of 8
		String [] chunks = text.split("(?<=\\G........)");
		
		//applying the two functions to decode
		for(int i=0; i<chunks.length; ++i) {
			chunks[i] = substitute(chunks[i], s);
			chunks[i] = transpose(chunks[i], t);
			chunks[i] = substitute(chunks[i], s);
			chunks[i] = transpose(chunks[i], t);
		}
		return String.join("", chunks);
	}
	
	public String request(String username) throws IOException {
		int x, p, g, key;
		
		//request secure connection
		out.println(username);

		//read and parse the x, p and g
		String [] num = in.readLine().split(" ");
		x=Integer.parseInt(num[0]);
		p=Integer.parseInt(num[1]);
		g=Integer.parseInt(num[2]);

		//send client's y to the server
		out.println(this.powMod(g, b, p));
		out.flush();
		
		//computing the key
		key = this.powMod(x, b, p);
		
		//return decoded string
		return decode(in.readLine(), key%8, key%26);

	}

	public static void main(String[] args) throws IOException {

		//start client with given arguments
		String text = new MyClient(args[0]).request(args[1]);
		System.out.println(text);
	}
}