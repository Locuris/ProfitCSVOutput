package com.jpmorgan.profittocsv;
/**
 * The interface for calculating data from a string and returning the results in a string[] format for writing to a .csv file
 * @author LouisKolodziej
 *
 */
public interface Instrument {
	/**
	 * Method for reading a row from .csv file
	 * @param csvData String the row from .csv file converted to a string
	 */
	public void ReadCVS(String csvData);	
	/**
	 * Method for returning and calculating data in format for writing to .csv
	 * @return .csv write format
	 */
	public String[] csvOutput();
	
}
