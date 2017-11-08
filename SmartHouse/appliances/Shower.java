package appliances;

/**
 * @author Bogdan
 * Model class of a shower. Superclass of electric and power showers.
 */
public abstract class Shower extends OneTimeAppliance {

	/**
	 * Constructor from super
	 * @param electricityUse - the electricity use
	 * @param gasUse - the gas use
	 * @param waterUse - the water use
	 * @param timeOn - time to complete a task
	 */
	public Shower(int electricityUse, int gasUse, int waterUse, int timeOn) 
	{
		super(electricityUse, gasUse, waterUse, timeOn);
	}

	@Override
	public void performSpecificAction() 
	{
		System.out.println("Someone is taking a shower in the " + toString());
	}
	
	@Override
	public abstract String toString();

}
