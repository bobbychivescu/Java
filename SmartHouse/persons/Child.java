package persons;


import java.util.ArrayList;

import tasks.Task;

/**
 * @author Bogdan
 *
 */
public class Child extends Person {

	ArrayList<String> forbiddenTasks;
	
	/**
	 * Constructor of Child, from super. Also sets the forbidden tasks of a child.
	 * Here we append any tasks that are forbidden for a child.
	 * @param name - name of the person
	 * @param age - his age (<18)
	 * @param gender - his gender
	 * @throws Exception - if super throws an Exception
	 */
	public Child(String name, int age, String gender) throws Exception 
	{
		super(name, age, gender);
		forbiddenTasks = new ArrayList<String>();
		
		forbiddenTasks.add("Cook");
		forbiddenTasks.add("Boil");
		forbiddenTasks.add("TurnOffTV");
	}

	@Override
	public void timePasses() 
	{
		for(Task task: taskList){
			if(task.getStartTime() == time){
				try {
					System.out.println(getName() + " wants to " + task.getName());
					if(forbiddenTasks.contains(task.getName())){
						throw new Exception("Children cannot or aren't allowed to " + task.getName());
					}
					task.doTask();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}
		time++;
	}

}
