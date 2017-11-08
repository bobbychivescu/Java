package meters;


/**
 * @author Bogdan
 * The meter class, superclass of all the meters in the house
 */
public abstract class Meter {
	
	private int consumed;
	private int generated;
	private boolean canGenerate;
	
	/**
	 * Constructor using fields
	 * @param consumed - start value of consumed
	 * @param canGenerate - if the resource can be generated
	 */
	public Meter(int consumed, boolean canGenerate)
	{
		this.consumed = consumed;
		generated = 0;
		this.canGenerate = canGenerate;
	}
	
	/**
	 * incrementing the consumed by one unit
	 */
	public void incrementConsumed()
	{
		//if we have any generated energy, we use it
		if(generated>0)
			generated--;
		else
			consumed++;
	}
	
	/**
	 * incrementing the consumed by a certain value
	 * @param value - the value to be added to consumed
	 */
	public void incrementConsumed(int value)
	{
		
		for( ;value>0; value--)
			incrementConsumed();
	}
	
	/**
	 * incrementing the generated by one unit
	 */
	public void incrementGenerated()
	{
		generated++;
	}
	
	/**
	 * incrementing the generated by a certain value
	 * @param value - the value to be added to generated
	 */
	public void incrementGenerated(int value)
	{
		for( ;value>0; value--)
			incrementGenerated();
	}
	
	/**
	 * getter for canGenerate
	 * @return - if the resource can be generated
	 */
	public boolean canGenerate()
	{
		return canGenerate;
	}

	/**
	 * getter for consumed
	 * @return - consumed
	 */
	public int getConsumed() 
	{
		return consumed;
	}

	/**
	 * getter for generated
	 * @return - generated
	 */
	public int getGenerated() 
	{
		return generated;
	}
	
	/**
	 * getter for the type, to be implemented in subclasses
	 * @return - the type
	 */
	public abstract String getType();
	
}