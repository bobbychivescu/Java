package tasks;
import appliances.Appliance;
import appliances.OnOffAppliance;

/**
 * @author Bogdan
 * Subclass of the Task class. Meant for appliances which are turned
 * on or off, e.g. TV, night light, boiler...
 */
public class OnOffTask extends Task {

	//needed for the type of task: Turn ON or Turn OFF
	private boolean state;

	/**
	 * Constructor from super
	 * @param name - the name of the task
	 * @param startTime - the time in the simulation
	 * @param state - true==ON and false==OFF
	 * @throws Exception if super throws an Exception
	 */
	public OnOffTask(String name, int startTime, boolean state) throws Exception 
	{
		super(name, startTime);
		this.state = state;
	}

	@Override
	public void doTask() throws Exception
	{
		boolean allAreNotAvailable = true;
		for(Appliance appliance : specificAppliances){
			if(appliance.isAvailable() == state){
				((OnOffAppliance) appliance).turn(state);
				allAreNotAvailable = false;
				break;
			}
		}
		if(allAreNotAvailable){
			String status = state? "on": "off";
			throw new Exception("All the " + specificAppliances.get(0).toString() + "s are already " + status);
		}
	}
}
