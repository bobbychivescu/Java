package appliances;
/*
 * The class of a kettle. Overrides toString() and performSpecificAction()
 */
/**
 * @author Bogdan
 * Model class of a kettle
 */
public class Kettle extends OneTimeAppliance {
	
	private static final int ELECTRICITY_USE = 20;
	private static final int GAS_USE = 0;
	private static final int WATER_USE = 1;
	private static final int TIME_ON = 1;
	
	/**
	 * Constructor of a kettle from super. In case the parameters are
	 * missing or don't respect the format in the input file, we use 
	 * the default values, hence the constants.
	 * @param electricityUse - the electricity use, parsed from the input
	 * @param gasUse - the gas use, parsed from the input
	 * @param waterUse - the water use, parsed from the input
	 */
	public Kettle(int electricityUse, int gasUse, int waterUse)
	{
		//another class is handling the parameters
		//the default values will be used in case they are missing from the text file(==-1)
		super(	electricityUse>=0? electricityUse : ELECTRICITY_USE ,
				gasUse>=0? gasUse : GAS_USE,
				waterUse>=0? waterUse : WATER_USE,
				TIME_ON);
	}

	@Override
	public String toString() 
	{
		return "Kettle";
	}

	@Override
	public void performSpecificAction() 
	{
		System.out.println("Someone is boiling water in the kettle");
	}
}
