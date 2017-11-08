import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Gallery extends JPanel {

	public static final int MAX_COMP = 12;
	
	public Gallery(){
		this.setLayout(new FlowLayout(0, 0, FlowLayout.LEFT));
	}
	
	public void addImage(JPanel image){
		if(this.getComponentCount()<MAX_COMP)
			this.add(image);
	}
}
