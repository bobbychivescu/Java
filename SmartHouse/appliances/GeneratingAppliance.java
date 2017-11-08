package appliances;
import utils.House;

public abstract class GeneratingAppliance extends Appliance {

	private House house;
	
	public GeneratingAppliance(int electricityUse, int gasUse, int waterUse, int timeOn) 
	{
		super(electricityUse, gasUse, waterUse, timeOn);
	}
	
	/**
	 * links the appliance to the house, in order to get the weather
	 * @param house - a House Object
	 */
	public void setHouse(House house)
	{
		this.house = house;
	}
	
	@Override
	public void timePasses() 
	{
		if(house.getWeather().equals(getSpecificWeather())){
			incrementGenerated();
			System.out.println("The " + toString() + " is generating energy");
		}
	}

	
	/**
	 * Getter for the specific weather necessary for generating electricity
	 * @return - a type of weather
	 */
	protected abstract String getSpecificWeather();
	
	@Override
	public abstract String toString();
}
