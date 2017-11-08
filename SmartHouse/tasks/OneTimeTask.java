package tasks;
import appliances.Appliance;
import appliances.OneTimeAppliance;

/**
 * @author Bogdan
 * Subclass of Task. Meant for appliances that automaticly turn off
 * after being used e.g. WashingMachine, Kettle...
 */
public class OneTimeTask extends Task {

	/**
	 * Constructor frim super
	 * @param name - the name of the Task
	 * @param startTime - the time in the simulation
	 * @throws Exception - if super throws an Exception
	 */
	public OneTimeTask(String name, int startTime) throws Exception
	{
		super(name, startTime);
	}

	@Override
	public void doTask() throws Exception
	{
		boolean allAreNotAvailable = true;
		for(Appliance appliance : specificAppliances){
			if(appliance.isAvailable()){
				((OneTimeAppliance) appliance).use();
				allAreNotAvailable = false;
				break;
			}
		}
		if(allAreNotAvailable){
			throw new Exception("All the " + specificAppliances.get(0).toString() + "s are not available");
		}
	}
}
