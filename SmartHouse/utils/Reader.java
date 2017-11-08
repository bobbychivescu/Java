package utils;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/*
 * The class that reads the input line by line and separates
 * the identifier word from the list of parameters
 */
public class Reader {
	
	Scanner in;
	String params;
	
	/**
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public Reader(String fileName) throws FileNotFoundException
	{
			in = new Scanner(new FileReader(fileName));
	}
	
	//geting the input line by line
	//used only in the class -> private
	private String getNextLine()
	{
		String line = null;
		if(in.hasNextLine()){
			line = in.nextLine();
		}
		return line;
	}
	
	//getting the first word and setting the params array
	public String getFirstWord()
	{
		String[] linePieces = getNextLine().split(":");
		if(linePieces != null){
			if(linePieces.length > 1){
				params = linePieces[1];
			}
			else params = null;
			return linePieces[0].trim();
		}
		return null;
	}
	
	//getting an array of the params
	public String[] getParamsList()
	{
		String [] trimmedParams;
		if(params != null){
			trimmedParams = params.split(",");
			for(int i = 0; i<trimmedParams.length; ++i){
				trimmedParams[i] = trimmedParams[i].trim();
			}
			return trimmedParams;
		}
		return null;
	}
	
	//check if there's more input
	public boolean hasNext()
	{
		if(in != null){
			return in.hasNext();
		}
		return false;
	}
}
