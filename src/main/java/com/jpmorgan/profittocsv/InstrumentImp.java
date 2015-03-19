package com.jpmorgan.profittocsv;
/**
 * This class a simple profit calculator that holds values to represent very simple trades in the form of
 * Equities and Bonds. It is implemendted using the Spring framework using a simple interface so will be 
 * to replace this class with more complicated instrument calculations
 * @author LouisKolodziej
 *
 */
public class InstrumentImp implements Instrument
{
	private String type;		// Equity or Bond
	private String name;		
	private int quantity;
	private int buy_price;
	private int sell_price;
	private int coupon;
	/**
	 * Takes the data from the csv generator and fills the instruments values with it so it can perform 
	 * the getProfit calculation. 
	 * The String that is is used as a parameter must be in the format "[Type,Name,quantity,buy_price,sell_price,coupon]"
	 */
	@Override
	public void ReadCVS(String csvData) {
		String tempS = "";
		int instValue = 0;
		// This for loop executes code for each character within the row string
		for(int i = 0; i < csvData.length(); i++)											
		{				
			String tempC = csvData.substring(i, i+1);													// This is a local string to hold the one character string at te position of i 
			if(!tempC.equals(" ") && !tempC.equals("[") && !tempC.equals(",") && !tempC.equals("]"))
				tempS += tempC;																			// Each char is added to another string until ',' is reached  
			else if(tempC.equals(",") || tempC.equals("]"))												// Then depending on how where it is in the row the newly formed word will be stored as a value for this object
			{	
				switch(instValue){
				case 0:
					type = tempS;																		// 1. Type
					break;
				case 1:
					name = tempS;																		// 2. Name
					break;
				case 2:
					quantity = Integer.parseInt(tempS);													// 3. Quantity
					break;
				case 3:
					if(!tempS.equals("")) buy_price = getPennies(tempS);								// 4. buy_price (stored as an integer that holds the penny amount.
					break;
				case 4:
					if(!tempS.equals("")) sell_price = getPennies(tempS);								// 5. sell_price (stored as an integer that holds the penny amount.
					break;
				case 5:
					if(!tempS.equals("")) coupon = getPennies(tempS);									// 6. coupon value (stored as an integer that holds the penny amount.
					break;
				}
				tempS ="";
				instValue++;
			}			
		}
	}
	/**
	 * Returns the calculated data in a format that can be parsed to .csv using opencsv
	 */
	@Override
	public String[] csvOutput() {
		String[] output = new String[4];
		output[0] = type;
		output[1] = name;
		output[2] = ""+quantity;
		output[3] =Profit();				// Profit is calculated and retunred as a string in the format "0.00"
		return output;
	}
	/**
	 * Returns a decimal as a string as an int representing it's values in hundredths
	 * @param s String must have '.' within in it. 
	 * @return int the pennies of a decimal value with two places.
	 */
	private int getPennies(String s) {
		s = s.replace(".", "");
		return Integer.parseInt(s);
	}
	/**
	 * Returns an integer as a string formated as a decimal equal to the int divided by 100. For switching the penny prices back to a more readable format
	 * @param i int The pennies you wish to display as a currency decimal
	 * @return String in the format of "0.00"
	 */
	private String getDecimal(int i)
	{
		String s = ""+i;
		if(i > 99)
			return s.substring(0, s.length() - 2) + "." + s.substring(s.length() - 2, s.length());
		else if(i < 0 && i > -100)
			return "-0."+s.replace("-", "");
		else
			return "0."+s;
	}
	/**
	 * Calculates the profit depending on what type of instrument the object is.
	 * @return String as a decimal so that it can be written to .csv file 
	 * 
	 */
	private String Profit()
	{		
		int profit = 0;
		if(type.equals("Equity"))
			profit = (sell_price - buy_price) * quantity;	// The formula for calculating equity profit
		else if(type.equals("Bond"))			
			profit = coupon * quantity;						// The formula for calculating bond profit
		return getDecimal(profit);
	}
	
}
