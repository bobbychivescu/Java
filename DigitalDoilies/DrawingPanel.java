import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

//the panel on which we will paint the doily
public class DrawingPanel extends JPanel {

	public static final int DEFAULT_SECTORS = 8, DEFAULT_SIZE = 1,
			DEFAULT_WIDTH = 800, DEFAULT_HEIGHT = 800;
	
	private Color color;
	private int pointSize, sectors;
	private boolean visibleLines, reflect;
	private ArrayList<Line> lines;

	public DrawingPanel(){
		super();
		color = Color.BLUE;
		pointSize=DEFAULT_SIZE;
		sectors = DEFAULT_SECTORS;
		visibleLines=true;
		reflect=false;
		lines = new ArrayList<Line>();
		this.setBackground(Color.WHITE);
		init();
	}

	//adding listeners to the panel
	private void init(){
		this.addMouseListener(new MouseAdapter(){
			//creating a new line
			public void mousePressed(MouseEvent e) {
				Line l = new Line(color, pointSize, reflect);
				//add twice to draw a point
				l.add(e.getPoint());
				l.add(e.getPoint());
				lines.add(l);
				repaint();
			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter(){
			//adding points to the last created line
			public void mouseDragged(MouseEvent e) {
				lines.get(lines.size()-1).add(e.getPoint());
				repaint();
			}
		});
	}
	
	
	//used for save
	public ArrayList<Line> getLines() {
		return lines;
	}

	//used to save the image
	public DrawingPanel save(){
		return this;
	}
	
	//used for load
	public void setLines(ArrayList<Line> lines) {
		this.lines = lines;
	}

	public void setColor(Color c){
		color=c;
	}

	public void setSectors(int s){
		sectors = s;
		repaint();
	}

	public void setPointSize(int s){
		pointSize=s;
	}

	public void setReflection(boolean r){
		reflect=r;
	}

	public void setVisibleLines(boolean v){
		visibleLines=v;
		repaint();
	}

	//new empty list of lines
	public void clear(){
		lines = new ArrayList<Line>();
		repaint();
	}

	//deleting the last line
	public void undo(){
		if(!lines.isEmpty()){
			lines.remove(lines.size()-1);
			repaint();
		}
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(this.getWidth()/2, this.getHeight()/2);

		if(visibleLines)
			for(int i=0; i<sectors; ++i){
				g2d.drawLine(0, 0, 0, this.getHeight()*2/5);
				g2d.rotate(Math.PI*2/sectors);
			}
		
		for(int i=0; i<sectors; ++i){
			for (int j=0; j<lines.size(); ++j){
				Line line = lines.get(j);
				boolean r = line.isReflect();
				ArrayList<Point> points = line.getPoints();
				g2d.setColor(line.getColor());
				g2d.setStroke(new BasicStroke(line.getSize()));

				for(int k=0; k<points.size()-1; ++k){
					Point p1 = points.get(k);
					Point p2 = points.get(k + 1);
					g2d.drawLine(p1.x-this.getWidth()/2, p1.y-this.getHeight()/2, 
							p2.x-this.getWidth()/2, p2.y-this.getHeight()/2);
					
					if(r){
						g2d.drawLine(-p1.x+this.getWidth()/2, p1.y-this.getHeight()/2, 
								-p2.x+this.getWidth()/2, p2.y-this.getHeight()/2);
					}
				}
			}
			g2d.rotate(Math.PI*2/sectors);
		}
	}
}
