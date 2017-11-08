package appliances;

/**
 * @author Bogdan
 * The model class of appliances that are used and then turned off.
 */
public abstract class OneTimeAppliance extends Appliance {

	private int count;
	
	/**
	 * Constructor from super
	 * @param electricityUse - the electricity use
	 * @param gasUse - the gas use
	 * @param waterUse - the water use
	 * @param timeOn - time to complete a task
	 */
	public OneTimeAppliance(int electricityUse, int gasUse, int waterUse, int timeOn) 
	{
		super(electricityUse, gasUse, waterUse, timeOn);
	}

	//the methid is the same for all appliances that are used and then turned off
	@Override
	public void timePasses() 
	{
		if(!isAvailable()){
			count++;
			incrementMeters();
			if(count == getTimeOn())
				turnOnOrOff(false);
		}
	}

	/**
	 * Method to turn on the appliance and use it. Called from a Task object
	 */
	public void use() 
	{
		turnOnOrOff(true);
		count = 0;
		performSpecificAction();
	}

	@Override
	public abstract String toString();
	
	/**
	 * Each appliance performs a task, that is printed to the outstream.
	 * To be implemented in subclasses
	 */
	public abstract void performSpecificAction();
}
