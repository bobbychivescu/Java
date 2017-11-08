import java.io.FileNotFoundException;
import java.io.PrintStream;

import appliances.Appliance;
import meters.Meter;
import persons.Person;
import tasks.Task;
import utils.House;
import utils.Interpreter;
import utils.Reader;

/**
 * @author Bogdan
 * The Main class, in which we initialize and run the simulation
 */
public class Main {

	public static void main (String args[])
	{
		PrintStream out = null;
		PrintStream stdOut = System.out;
		try {
			out = new PrintStream("output.txt");
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		System.setOut(out);
		System.setErr(out);
		Reader reader = null;
		try {
			reader = new Reader(args[0]);
		} catch (FileNotFoundException e1) {
			System.err.println(e1.getMessage());
			return;
		}
		
		House house;
		
		try {
			if(reader.getFirstWord().equals("House"))
			{
				house = new House();
				Interpreter interpreter = new Interpreter();
				String first;
				String[] params;
				Object object = null;
				
				while(reader.hasNext()){
					first = reader.getFirstWord();
					params = reader.getParamsList();
					try {
						object = interpreter.resolve(first, params);
					} catch (Exception e) {
						System.err.println(e.getMessage());
						continue;
					}

					if(object instanceof Meter){
						house.addMeter((Meter)object);
					}

					if(object instanceof Appliance){
						try {
							house.addAppliance((Appliance)object);
						} catch (Exception e) {
							System.err.println(e.getMessage());
						}
					}

					if(object instanceof Person){
						house.addPerson((Person)object);
					}

					if(object instanceof Task){
						Person person = house.getLastPerson();
						String applianceName = interpreter.getTaskAppliance((Task) object);
						Appliance appliance = null;

						try {
							//search for the specific appliance in the house
							appliance = house.searchAppliance(applianceName, 1);
							if(appliance == null){
								throw new Exception("There is no appliance in the house that could perform " + ((Task) object).getName());
							}else{
								int counter = 2;
								//setting the appliances for the task, if there are more
								while(appliance!=null){
									((Task) object).setAppliance(appliance);
									appliance = house.searchAppliance(applianceName, counter);
									counter++;
								}
								//adding the task to the last person in the house
								if(person!=null){
									person.addTask((Task) object);
								}else{
									throw new Exception("There is no one in the house to give tasks to");
								}
							}
						} catch (Exception e) {
							
						}
					}
				}
			}else{
				throw new Exception("The text file doesn't respect the format");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return;
		}
		for(int i = 0; i < 96; ++i)
			house.timePasses();
		System.setOut(stdOut);
		house.displayMeters();
	}
}
