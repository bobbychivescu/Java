package meters;

/**
 * @author Bogdan
 * The model class of an electric meter
 */
public class ElectricMeter extends Meter {

	private String type;
	
	private static final int CONSUMED = 0;
	//needed in the constructor class
	public static final boolean CAN_GENERATE = true;
	
	/**
	 * Constructor from super
	 * @param consumed - initial value of consumed or the default
	 * @param canGenerate - if it can generate
	 */
	public ElectricMeter(int consumed, boolean canGenerate)
	{
		//passing the default value in case of bad input
		super(	consumed>=0? consumed: CONSUMED,
				canGenerate);
		type = "electric";
	}
	
	@Override
	public String getType() {
		return type;
	}
}
