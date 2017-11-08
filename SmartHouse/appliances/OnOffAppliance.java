package appliances;

/**
 * @author Bogdan
 * The model class of appliances that need to be turned on and off (TV, NightLight...)
 */
public abstract class OnOffAppliance extends Appliance {

	/**
	 * Constructor from super
	 * @param electricityUse - the electricity use
	 * @param gasUse - the gas use
	 * @param waterUse - the water use
	 * @param timeOn - time to complete a task
	 */
	public OnOffAppliance(int electricityUse, int gasUse, int waterUse, int timeOn) 
	{
		super(electricityUse, gasUse, waterUse, timeOn);
	}
	
	@Override
	public void timePasses() {
		// TODO Auto-generated method stub
		if(!isAvailable())
			incrementMeters();
	}
	
	/**
	 * turning the appliance on or off. Called from a Task Object
	 * @param state - true==ON and false==OFF
	 */
	public void turn(boolean state)
	{
		turnOnOrOff(state);
		turnIt(state);
	}

	/**
	 * The specific action, printed to the outstream
	 * @param state - true==ON and false==OFF
	 */
	private void turnIt(boolean state)
	{
		String stateWord = state ? "on" : "off";
		System.out.println("Someone turned " + stateWord + " the " + toString());
	}
	
	@Override
	public abstract String toString();
	
}
