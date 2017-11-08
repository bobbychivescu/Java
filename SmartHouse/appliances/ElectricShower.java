package appliances;
/*
 * The class of an Electric Shower. Overrides toString()
 */
/**
 * @author Bogdan
 * Model class of an electric shower
 */
public class ElectricShower extends Shower {

	private static final int ELECTRICITY_USE = 12;
	private static final int GAS_USE = 0;
	private static final int WATER_USE = 4;
	private static final int TIME_ON = 1;
	
	/**
	 * Constructor of an electric shower from super. In case the parameters are
	 * missing or don't respect the format in the input file, we use 
	 * the default values, hence the constants.
	 * @param electricityUse - the electricity use, parsed from the input
	 * @param gasUse - the gas use, parsed from the input
	 * @param waterUse - the water use, parsed from the input
	 */
	public ElectricShower(int electricityUse, int gasUse, int waterUse)
	{
		//another class is handling the parameters
		//the default values will be used in case they are missing from the text file(==-1)
		super(	electricityUse>=0? electricityUse : ELECTRICITY_USE ,
				gasUse>=0? gasUse : GAS_USE,
				waterUse>=0? waterUse : WATER_USE,
				TIME_ON);
	}

	@Override
	public String toString() {
		return "ElectricShower";
	}

}
