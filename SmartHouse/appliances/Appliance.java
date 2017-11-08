package appliances;



import java.util.ArrayList;

import meters.Meter;

/**
 * @author Bogdan
 * The model class of an appliance, superclass of all the appliances in the house.
 */
public abstract class Appliance {

	private int electricityUse;
	private int gasUse;
	private int waterUse;
	private int timeOn;
	private boolean currentState;

	private ArrayList<Meter> meters;
	
	/**
	 * Constructor using fields
	 * @param electricityUse - the electricity use
	 * @param gasUse - the gas use
	 * @param waterUse - the water use
	 * @param timeOn - the time it stays on for
	 */
	public Appliance(int electricityUse, int gasUse, int waterUse, int timeOn) 
	{
		this.electricityUse = electricityUse;
		this.gasUse = gasUse;
		this.waterUse = waterUse;
		this.timeOn = timeOn;
		currentState = false;
	}
	
	/**
	 * Turning the appliance on or off
	 * @param currentState - true==ON and false==OFF
	 */
	public void turnOnOrOff(boolean currentState)
	{
		this.currentState = currentState;
	}
	
	/**
	 * query for availability
	 * @return if the appliances is off or on
	 */
	public boolean isAvailable()
	{
		return currentState == false;
	}
	
	
	/**
	 * called in the house, to attach the meters
	 * @param meters - ArrayList of meters in the house
	 */
	public void attachMeters(ArrayList<Meter> meters)
	{
		this.meters = meters;
	}
	
	/**
	 * called only from subclasses, increments the meters for one unit of time
	 */
	protected void incrementMeters()
	{
		for(Meter meter : meters){
			if(meter.getType() == "electric") meter.incrementConsumed(electricityUse);
			if(meter.getType() == "gas") meter.incrementConsumed(gasUse);
			if(meter.getType() == "water") meter.incrementConsumed(waterUse);
		}
	}
	
	/**
	 * called from subclasses of appliances that can generate energy.
	 * in our case, only electricity can be generated
	 */
	protected void incrementGenerated()
	{
		for(Meter meter : meters){
			if(meter.getType() == "electric") meter.incrementGenerated(electricityUse);
		}
	}
	
	/**
	 * getter for the timeOn
	 * @return - timeOn
	 */
	public int getTimeOn()
	{
		return timeOn;
	}
	
	/**
	 * simulate a 15 min period for an appliance. Implemented in subclasses
	 */
	public abstract void timePasses();
	
	@Override
	public abstract String toString();
}
