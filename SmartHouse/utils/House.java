package utils;
import java.util.ArrayList;
import java.util.Random;

import appliances.Appliance;
import appliances.Boiler;
import appliances.GeneratingAppliance;
import meters.Meter;
import persons.Person;

/**
 * @author Bogdan
 * The class of the house, in which we hold the lists of meters, appliances
 * and people. Here, we define the timePasses() method, which runs the simulation
 */
public class House {

	private ArrayList<Meter> meters;
	private ArrayList<Appliance> appliances;
	private ArrayList<Person> people;
	private int time;
	private double temp;
	private String weather;
	//maximum number of appliances in a house
	private static final int MAX_APP = 25;
	//initial temperature in the house
	private static final int INIT_TEMP = 16;
	//the desired temperature (must not drop below) need acces from boiler
	public static final double DESIRED_TEMP = 21.5;

	/**
	 * The constructor, initializes the fields of the class (the lists)
	 */
	public House()
	{
		meters = new ArrayList<Meter>();
		appliances = new ArrayList<Appliance>();
		people = new ArrayList<Person>();
		time = 0;
		temp = INIT_TEMP;
	}

	/**
	 * Adds a meter to the house
	 * @param meter - A meter object(ElectricMeter, GasMeter or WaterMeter)
	 */
	public void addMeter(Meter meter)
	{
		meters.add(meter);
	}

	/**
	 * Adds a person to the house
	 * @param person - The Person object to be added (GrownUp or Child)
	 */
	public void addPerson(Person person)
	{
		people.add(person);
	}

	/**
	 * Attaches the meters to an appliance and adds it to the house
	 * @param appliance - The Appliance object to be added
	 * @throws Exception - if the maximum number of appliances is reached
	 */
	public void addAppliance(Appliance appliance) throws Exception
	{
		if(appliances.size() == MAX_APP){
			throw new Exception("Cannot add more appliances");
		}
		//attaching the meters before adding the appliance to the house
		appliance.attachMeters(meters);
		//if its a Boiler
		if(appliance instanceof Boiler){
			((Boiler)appliance).setHouse(this);
		}
		//if its a GeneratingAppliance
		if(appliance instanceof GeneratingAppliance){
			((GeneratingAppliance)appliance).setHouse(this);
		}
		appliances.add(appliance);
	}

	/**
	 * removes an appliance from the house
	 * @param appliance - the Appliance to be removed
	 */
	public void removeAppliance(Appliance appliance)
	{
		appliances.remove(appliance);
	}

	/**
	 * Searches for an appliance in a house. We might have more instances of the same appliance
	 * @param applianceName - the name of the appliance
	 * @param occurence - which instance we are looking for (its index)
	 * @return the searched appliance or null if it's not found
	 */
	public Appliance searchAppliance(String applianceName, int occurence)
	{
		int index = 1;
		for(Appliance appliance : appliances){
			if(appliance.toString().contains(applianceName)){
				if(index == occurence){
					return appliance;
				}else{
					index++;
				}
			}
		}
		return null;
	}

	/**
	 * Query for the number of appliances in the house
	 * @return the number of appliances in the house
	 */
	public int numAppliances()
	{
		return appliances.size();
	}


	/**
	 * Get the last person added to the house
	 * @return the last Person added to the house or null if there's no one in the house
	 */
	public Person getLastPerson()
	{
		if(!people.isEmpty())
			return people.get(people.size()-1);
		return null;
	}

	/**
	 * Runs the simulation of a 15 minute period
	 */
	public void timePasses()
	{
		System.out.println(timeString(time));
		modelWeather(this);
		System.out.println("It's " + weather);
		for(Person person : people){
			person.timePasses();
		}
		for (Appliance appliance : appliances) {
			appliance.timePasses();
		} 
		System.out.println("There are " + temp + " degrees C in the house");
		displayMeters();
		System.out.println();
		time++;
	}

	/**
	 * Print the consumption of energy
	 */
	public void displayMeters()
	{
		for(Meter meter: meters)
			System.out.println(meter.getType() + " " + meter.getConsumed());
		if(meters.isEmpty())
			System.out.println("There are no meters in the house.");
	}

	/**
	 * Getter for the current time
	 * @return - time
	 */
	public int getTime()
	{
		return time;
	}

	/**
	 * setter for the weather
	 * @param weather - a type of weather
	 */
	public void setWeather(String weather)
	{
		this.weather = weather;
	}

	/**
	 * Getter for the weather
	 * @return the weather
	 */
	public String getWeather()
	{
		return weather;
	}
	
	/**
	 * Modify the temperature
	 * @param value - number of degrees Celsius (can be negative)
	 */
	public void modifyTemp(double value)
	{
		temp+=value;
	}
	
	/**
	 * getter for the temperature
	 * @return temp
	 */
	public double getTemp()
	{
		return temp;
	}

	//private static method to model the weather and its effect on the temperature
	private static void modelWeather(House house)
	{
		int time = house.getTime();
		int choice;
		Random random = new Random();
		//if it's night, it cannot be sunny, and the temp drops
		if(time<32 || time>79){
			choice = random.nextInt(3);
			house.modifyTemp(-1);
		}else{
			choice = random.nextInt(4);
		}

		switch(choice){
		case 0:
			house.setWeather("rainy");
			house.modifyTemp(-1);
			break;
		case 1:
			house.setWeather("windy");
			house.modifyTemp(-1);
			break;
		case 2:
			house.setWeather("cloudy");
			break;
		case 3:
			house.setWeather("sunny");
			house.modifyTemp(0.5);
			break;
		}
	}

	//Static method to make a string of the actual time.
	private static String timeString(int time)
	{
		int hour = time/4;
		int min = time%4;
		String timeDisplay;
		if(hour>=10){
			timeDisplay = hour + ":";
		}else{
			timeDisplay = "0" + hour + ":";
		}
		
		switch(min){
		case 1:
			timeDisplay += "15";
			break;
		case 2:
			timeDisplay += "30";
			break;
		case 3:
			timeDisplay += "45";
			break;
		default:
			timeDisplay += "00";
			break;	
		}

		return timeDisplay;
	}
}
