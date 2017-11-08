import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

//a line that will be drown in the panel
public class Line {

	private ArrayList<Point> points;
	private Color color;
	private Integer size;
	private boolean reflect;
	
	public Line(Color color, Integer size, boolean reflect) {
		points = new ArrayList<Point>();
		this.color = color;
		this.size = size;
		this.reflect = reflect;
	}
	
	public void add(Point p){
		points.add(p);
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public Color getColor() {
		return color;
	}

	public Integer getSize() {
		return size;
	}

	public boolean isReflect() {
		return reflect;
	}
	
	
}
