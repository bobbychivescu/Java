package utils;
import java.util.HashMap;
import java.util.HashSet;

import tasks.Task;

/**
 * @author Bogdan
 * The class that interprets the input and calls the right method
 * of the Creator, in order to create the right object
 */
public class Interpreter {
	
	private Creator creator;
	
	//A set of all the available appliances
	private HashSet<String> appliances;
	
	//A HashMap that maps a task to an appliance
	private HashMap<String, String> tasks;
	
	/**
	 * The constructor - initializes the private fields of the class
	 * and adds the defined appliances and tasks to their lists
	 * This is where we easily append newly defined appliances and tasks that can be performed on them
	 */
	public Interpreter()
	{
		creator = new Creator();
		appliances = new HashSet<String>();
		tasks = new HashMap<String, String>();
		
		//adding all the available appliances
		appliances.add("WashingMachine");
		appliances.add("Refrigerator");
		appliances.add("Kettle");
		appliances.add("DishWasher");
		appliances.add("TV");
		appliances.add("ElectricShower");
		appliances.add("PowerShower");
		appliances.add("ElectricCooker");
		appliances.add("GasCooker");
		appliances.add("NightLight");
		appliances.add("Boiler");
		appliances.add("SolarPanel");
		appliances.add("WindTurbine");
		
		//mapping all task to a specific appliance
		tasks.put("DoWashing", "WashingMachine");
		tasks.put("Boil", "Kettle");
		tasks.put("WashDishes", "DishWasher");
		tasks.put("TurnOnTV", "TV");
		tasks.put("TurnOffTV", "TV");
		tasks.put("Shower", "Shower"); 	//power or electric
		tasks.put("Cook", "Cooker");	//gas or electric
		tasks.put("TurnOnNightLight", "NightLight");
		tasks.put("TurnOffNightLight", "NightLight");
	}

	/**
	 * Method that checks what object is to be created and calls
	 * the appropriate method of the constructor
	 * @param indicator - the name of the object
	 * @param params - its list of parameters
	 * @return the appropriate object (Appliance, Meter, Person or Task)
	 * @throws Exception - if the construction of an object fails or the indicator is not recognized
	 */
	public Object resolve(String indicator, String[] params) throws Exception
	{	
		if(isMeter(indicator))
			return creator.resolveMeter(indicator, params);
		else if(isAppliance(indicator)) 
			return creator.resolveAppliance(indicator, params);
		else if(isPerson(indicator))
			return creator.resolvePerson(params);
		else if (isTask(indicator)) 
			return creator.resolveTask(indicator, params);
		else
			throw new Exception(indicator + " cannot be resolved to a type");
	}

	
	/**
	 * Method called when we need the name of the 
	 * specific appliance if a task
	 * @param task - a certain Task
	 * @return the name of its specific appliance
	 */
	public String getTaskAppliance(Task task)
	{
		String name = task.getName();
		return tasks.get(name);
	}
	
	/*
	 * private methods to identify the type of object to be created
	 */
	
	private boolean isMeter(String indicator) 
	{
		if(indicator.equals("ElectricMeter"))return true;
		if(indicator.equals("GasMeter"))return true;
		if(indicator.equals("WaterMeter"))return true;
		return false;
	}

	private boolean isAppliance(String indicator) 
	{
		if(appliances.contains(indicator))return true;
		return false;
	}

	private boolean isPerson(String indicator) 
	{
		if(indicator.equals("Person"))return true;
		return false;
	}

	private boolean isTask(String indicator) {
		if(tasks.containsKey(indicator)) return true;
		return false;
	}
}
