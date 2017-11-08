import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

//used for small GUIs or for reporting errors
public class SmallFrame extends JFrame {

	public SmallFrame(String title){
		super(title);
		this.setSize(400, 220);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
	}
	
	public void message(String message){
		this.add(new JLabel(message));
		this.setVisible(true);
	}
}
