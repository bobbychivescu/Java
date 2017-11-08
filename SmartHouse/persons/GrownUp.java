package persons;
import tasks.Task;

/**
 * @author Bogdan
 * The model class of an adult
 */
public class GrownUp extends Person {

	/**
	 * Constructor of GrownUp, from super class.
	 * @param name - name of the person
	 * @param age - his age (>18)
	 * @param gender - his gender
	 * @throws Exception - if super throws an Exception
	 */
	public GrownUp(String name, int age, String gender) throws Exception 
	{
		super(name, age, gender);
	}

	@Override
	public void timePasses() 
	{
		for(Task task: taskList){
			if(task.getStartTime() == time){
				try {
					System.out.println(getName() + " wants to " + task.getName());
					task.doTask();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}
		time++;
	}
}
