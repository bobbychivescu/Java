import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Thumbnail extends JPanel {

	public static final int DEFAULT_WIDTH = 100, DEFAULT_HEIGHT = 100;
	
	//used for saving to pc
	private static int number=1;
			
	private ArrayList<Line> lines;
	private BufferedImage image;
	private boolean selected;
	
	public Thumbnail(DrawingPanel panel){
		lines = panel.getLines();
		int w = panel.getWidth();
	    int h = panel.getHeight();
	    image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = image.createGraphics();
	    panel.paint(g);
	    //we set the dimension here, as this component is added on the run
	    this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
	    selected=false;
	}
	
	public void setSelected(boolean s){
		selected=s;
	}

	//for load
	public ArrayList<Line> getLines() {
		return new ArrayList<Line>(lines);
	}

	//to view the doily in a separate window
	public void view() {
		JLabel label = new JLabel(new ImageIcon(image));
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.getContentPane().add(label);
        f.pack();
        f.setLocation(200,200);
        f.setVisible(true);
		
	}
	
	//to save as .png file
	public void saveToPc() throws IOException{
		ImageIO.write(image, "PNG", new File("image" + number + ".png"));
		number++;
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int w = this.getWidth();
		int h = this.getHeight();
		Image i = image.getScaledInstance(w*4/5, h*4/5, Image.SCALE_DEFAULT);
		g.drawImage(i, w/10, h/10, null);
		if(selected){
			g.drawRect(w/20, h/20, w*9/10, h*9/10);
		}
	}

	
}

