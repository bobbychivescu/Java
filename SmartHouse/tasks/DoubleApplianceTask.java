package tasks;


import java.util.Random;

import appliances.Appliance;
import appliances.OneTimeAppliance;

/**
 * @author Bogdan
 * Subclass of Task. Meant for tasks that can be performed on more 
 * than one appliance (e.g. shower, cook) hence the use of Random
 */
public class DoubleApplianceTask extends Task {

	private Random random;
	
	/**
	 * Constructor frim super
	 * @param name - the name of the Task
	 * @param startTime - the time in the simulation
	 * @throws Exception - if super throws an Exception
	 */
	public DoubleApplianceTask(String name, int startTime) throws Exception 
	{
		super(name, startTime);
		random = new Random();
	}

	
	// we use a random appliance
	@Override
	public void doTask() throws Exception {
		//first, we check if there's any available appliance
		int size = specificAppliances.size();
		int i;
		for(i = 0; i < size; ++i){
			if(specificAppliances.get(i).isAvailable()){
				break;
			}
		}
		if(i == size){
			String appliance = specificAppliances.get(0).toString();
			throw new Exception("All the " + appliance.substring(appliance.length()-6) + "s are not available");
		}else{
			int randomNum = random.nextInt(size);
			Appliance app = specificAppliances.get(randomNum);
			while(!app.isAvailable()){
				randomNum = random.nextInt(size);
				app = specificAppliances.get(randomNum);
			}
			((OneTimeAppliance)app).use();
		}
	}

}
