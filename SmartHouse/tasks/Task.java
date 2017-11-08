package tasks;


import java.util.ArrayList;

import appliances.Appliance;

/*
 * 
 */
/**
 * @author Bogdan
 * The class of a task. Holds a list of the needed appliances to perform.
 * We hold a LIST of appliances in case there are more appliances of the 
 * same type or for those tasks that can be performed on more than one
 * appliance e.g. Shower() can be done with Electric or Power
 */
public abstract class Task {

	private String name;
	private int startTime;
	//need access from subclasses
	protected ArrayList<Appliance> specificAppliances;


	/**
	 * Constructor of a task, using fields
	 * @param name - the name of the task
	 * @param startTime - the time in the simulation
	 * @throws Exception - if the time is out of bounds (0-95)
	 */
	public Task(String name, int startTime) throws Exception
	{
		this.name = name;
		if(startTime<0 || startTime>95){
			throw new Exception("The startTime is missing or is out of bounds (0-95)");
		}
		this.startTime = startTime;
		specificAppliances = new ArrayList<Appliance>();
	}

	/**
	 * Adding an appliance to the Task
	 * @param appliance - the appliance to be added
	 */
	public void setAppliance(Appliance appliance)
	{
		specificAppliances.add(appliance);
	}

	/**
	 * Getter for the name
	 * @return the name of the task
	 */
	public String getName() 
	{
		return name;
	}

	/**
	 * Getter for the startTime
	 * @return the time the task should be performed
	 */
	public int getStartTime()
	{
		return startTime;
	}
	
	
	/**
	 * Called when a person does a Task. Overridden in subclasses
	 * @throws Exception - if the task cannot be performed due to availability of appliances
	 */
	public abstract void doTask() throws Exception;
}
