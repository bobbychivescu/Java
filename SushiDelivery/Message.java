import java.io.Serializable;

//wrapper class used for communication
public class Message implements Serializable{
	
	private String type;
	private Object content;
	
	public Message(String type, Object content) {
		this.type = type;
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public Object getContent() {
		return content;
	}
	
	
}
