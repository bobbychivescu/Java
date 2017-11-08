
public enum Postcode {
	SO182NU (5),
	SO164JK (3),
	SO039WE (2),
	SO124ER (7),
	ES014LX (9),
	PO093MC (11),
	PO105ZP (13);
	
	private final int distance; //in km
	
	Postcode(int d){
		distance = d;
	}
	
	int getDistance(){
		return distance;
	}
}
