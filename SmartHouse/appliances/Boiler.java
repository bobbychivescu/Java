package appliances;
import utils.House;

/**
 * @author Bogdan
 * Model class of an automated boiler, that keeps the temperature in
 * the house above the desired temperature. Part of the extension
 */
public class Boiler extends Appliance {

	private static final int ELECTRICITY_USE = 0;
	private static final int GAS_USE = 1;
	private static final int WATER_USE = 0;
	private static final int TIME_ON = -1;
	//how many degrees it can heat in one unit of time
	private static final double POWER = 2.5;
	
	private House house;
	
	/**
	 * Constructor of a boiler from super. In case the parameters are
	 * missing or don't respect the format in the input file, we use 
	 * the default values, hence the constants.
	 * @param electricityUse - the electricity use, parsed from the input
	 * @param gasUse - the gas use, parsed from the input
	 * @param waterUse - the water use, parsed from the input
	 */
	public Boiler(int electricityUse, int gasUse, int waterUse)
	{
		//another class is handling the parameters
		//the default values will be used in case they are missing from the text file(==-1)
		super(	electricityUse>=0? electricityUse : ELECTRICITY_USE ,
				gasUse>=0? gasUse : GAS_USE,
				waterUse>=0? waterUse : WATER_USE,
				TIME_ON);
	}

	/**
	 * links the boiler to the house, in order to modify the temperature
	 * @param house - a House Object
	 */
	public void setHouse(House house)
	{
		this.house = house;
	}
	
	//the boiler starts automatically if the temperature is below the disered
	@Override
	public void timePasses() 
	{
		if(house.getTemp() < House.DESIRED_TEMP){
			System.out.println("The boiler is on");
			house.modifyTemp(POWER);
			incrementMeters();
		}
	}

	@Override
	public String toString() 
	{
		return "Boiler";
	}

}
