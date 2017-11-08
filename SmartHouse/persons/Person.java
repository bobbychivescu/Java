package persons;


import java.util.ArrayList;

import tasks.Task;

/**
 * @author Bogdan
 * The class of a person, holds basic information and a lsit of tasks
 */
/**
 * @author Bogdan
 *
 */
/**
 * @author Bogdan
 *
 */
public abstract class Person {

	private String name;
	private int age;
	private String gender;
	protected ArrayList<Task> taskList;
	protected int time;
	
	/**
	 * The constructor of a person
	 * @param name - the name of the person
	 * @param age - his age
	 * @param gender - his gender
	 * @throws Exception - if the parameters are missing or are not appropriate
	 */
	public Person(String name, int age, String gender) throws Exception 
	{
		if(name == null){
			throw new Exception("The person doesn't have a name");
		}
		this.name = name;
		if(age<0){
			throw new Exception("The age is missing or is negative");
		}
		this.age = age;
		if(gender == null || (!gender.equals("M") && !gender.equals("F"))){
			throw new Exception("The gender is missing or is not defined");
		}
		this.gender = gender;
		taskList = new ArrayList<Task>();
		time = 0;
	}

	/**
	 * Getter for the name
	 * @return the name of the person
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for the age
	 * @return the age of the person
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Getter for the gender
	 * @return the gener of the person
	 */
	public String getGender() {
		return gender;
	}
	
	/**
	 * Adds a task to the list
	 * @param task - a certain task to be added
	 */
	public void addTask(Task task)
	{
		taskList.add(task);
	}

	/**
	 * Method called to simulate a period of 15 minutes for a person
	 */
	public abstract void timePasses();
	
}
