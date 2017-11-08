package utils;
import appliances.Appliance;
import appliances.Boiler;
import appliances.DishWasher;
import appliances.ElectricCooker;
import appliances.ElectricShower;
import appliances.GasCooker;
import appliances.Kettle;
import appliances.NightLight;
import appliances.PowerShower;
import appliances.Refrigerator;
import appliances.SolarPanel;
import appliances.TV;
import appliances.WashingMachine;
import appliances.WindTurbine;
import meters.ElectricMeter;
import meters.GasMeter;
import meters.Meter;
import meters.WaterMeter;
import persons.Child;
import persons.GrownUp;
import persons.Person;
import tasks.DoubleApplianceTask;
import tasks.OnOffTask;
import tasks.OneTimeTask;
import tasks.Task;

/**
 * @author Bogdan
 * The class responsable for resolving the list of parameters
 * and creating the right object (Appliance, Meter, Person or Task)
 */
public class Creator {

	/**
	 * The method that creates a meter
	 * @param indicator - the type of meter (case sensitive)
	 * @param params - its list of parameters (might be empty)
	 * @return the appropriate meter (electric, gas or water)
	 */
	public Meter resolveMeter(String indicator, String[] params)
	{
		int consumed = resolveIntValue(params, 1);
		boolean canGenerate;

		switch(indicator){
		case "ElectricMeter":
			canGenerate = resolveBooleanValue(params, 2, ElectricMeter.CAN_GENERATE);
			return new ElectricMeter(consumed, canGenerate);

		case "GasMeter":
			canGenerate = resolveBooleanValue(params, 2, GasMeter.CAN_GENERATE);
			return new GasMeter(consumed, canGenerate);

		case "WaterMeter":
			canGenerate = resolveBooleanValue(params, 2, WaterMeter.CAN_GENERATE);
			return new WaterMeter(consumed, canGenerate);

		default:
			return null;
		}
	}

	/**
	 * The method that creates an appliance. Should we define 
	 * a new appliance, this is where we append it, in the switch-case
	 * @param indicator - the type of appliance (case sensitive)
	 * @param params - its list of parameters (might be empty)
	 * @return the appropriate appliance
	 */
	public Appliance resolveAppliance(String indicator, String[] params) {
		int electricityUse = resolveIntValue(params, 1);
		int gasUse = resolveIntValue(params, 2);
		int waterUse = resolveIntValue(params, 3);

		switch(indicator){
		case "WashingMachine":
			return new WashingMachine(electricityUse, gasUse, waterUse);
		case "Refrigerator":
			return new Refrigerator(electricityUse, gasUse, waterUse);
		case "Kettle":
			return new Kettle(electricityUse, gasUse, waterUse);
		case "DishWasher":
			return new DishWasher(electricityUse, gasUse, waterUse);
		case "TV":
			return new TV(electricityUse, gasUse, waterUse);
		case "ElectricShower":
			return new ElectricShower(electricityUse, gasUse, waterUse);
		case "PowerShower":
			return new PowerShower(electricityUse, gasUse, waterUse);
		case "ElectricCooker":
			return new ElectricCooker(electricityUse, gasUse, waterUse);
		case "GasCooker":
			return new GasCooker(electricityUse, gasUse, waterUse);
		case "NightLight":
			return new NightLight(electricityUse, gasUse, waterUse);
		case "Boiler":
			return new Boiler(electricityUse, gasUse, waterUse);
		case "SolarPanel":
			return new SolarPanel(electricityUse, gasUse, waterUse);
		case "WindTurbine":
			return new WindTurbine(electricityUse, gasUse, waterUse);
		default:
			return null;
		}
	}

	/**
	 * The method that creates a person
	 * @param params - the list of parameters (name, age, gender)
	 * @return the appropriate person (GrownUp or Child)
	 * @throws Exception - if the construction fails
	 */
	public Person resolvePerson(String[] params) throws Exception {
		String name = resolveStringValue(params, 1);
		int age = resolveIntValue(params, 2);
		String gender = resolveStringValue(params, 3);

		if(age >= 18){
			return new GrownUp(name, age, gender);

		}else{
			return new Child(name, age, gender);
		}

	}

	/**
	 * The method that creates a task. Should we define 
	 * a new appliance, this is where we append the tasks 
	 * that can be performed on it, in the switch-case
	 * @param indicator - name of the task (case sensitive)
	 * @param params - its list of parameters (startTime)
	 * @return the appropriate Task
	 * @throws Exception - if the construction fails
	 */
	public Task resolveTask(String indicator, String[] params) throws Exception 
	{
		int startTime = resolveIntValue(params, 1);

		switch(indicator){
		case "DoWashing":
			return new OneTimeTask(indicator, startTime);
		case "Boil":
			return new OneTimeTask(indicator, startTime);
		case "WashDishes":
			return new OneTimeTask(indicator, startTime);
		case "TurnOnTV":
			return new OnOffTask(indicator, startTime, true);
		case "TurnOffTV":
			return new OnOffTask(indicator, startTime, false);
		case "Shower":
			return new DoubleApplianceTask(indicator, startTime);
		case "Cook":
			return new DoubleApplianceTask(indicator, startTime);
		case "TurnOnNightLight":
			return new OnOffTask(indicator, startTime, true);
		case "TurnOffNightLight":
			return new OnOffTask(indicator, startTime, false);
		default:
			return null;
		}
	}

	/*
	 * Private methods that resolve the list of parameters
	 */

	//@return int value of the parameter, or -1 in case of bad input
	private int resolveIntValue(String[] params, int index)
	{
		int value;
		if(params != null && params.length >= index){
			try {
				value = Integer.parseInt(params[index-1]);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				value = -1;
			}
		}else{
			value = -1;
		}
		return value;
	}

	//@return String on params[index-1] or null in case of bad input
	private String resolveStringValue(String[] params, int index)
	{
		if(params != null && params.length >= index){
			return params[index-1];
		}
		return null;
	}

	//@return truth value of params[index-1] or the specific constant of the meter in case of bad input
	private boolean resolveBooleanValue(String[] params, int index, boolean constant)
	{
		if(params != null && params.length >= index){
			if(params[index-1].equals("true")) return true;
			else if (params[index-1].equals("false")) return false;
		}
		return constant;
	}
}
