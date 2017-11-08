package appliances;
/**
 * @author Bogdan
 * Model class of a cooker. Superclass of Electric and Gas coolers
 */
public abstract class Cooker extends OneTimeAppliance {

	/**
	 * Constructor from super
	 * @param electricityUse - the electricity use
	 * @param gasUse - the gas use
	 * @param waterUse - the water use
	 * @param timeOn - time to complete a task
	 */
	public Cooker(int electricityUse, int gasUse, int waterUse, int timeOn) 
	{
		super(electricityUse, gasUse, waterUse, timeOn);
	}

	@Override
	public void performSpecificAction() 
	{
		System.out.println("Someone is cooking in the " + toString());
	}
	
	@Override
	public abstract String toString();

}
