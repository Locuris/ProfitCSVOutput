package com.jpmorgan.profittocsv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jpmorgan.profittocsv.Instrument;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
/**
 * This class takes user input from console in the form of a filepath and filename for a .csv file and then
 * generates a new .csv file from it according to object specific formulas. It uses spring to allow the changing
 * of these objects and formulae without changing the code within this class just but adding new classes and 
 * editing the config.xml file
 * @author LouisKolodziejS
 */
public class CSVGeneratorServiceImp implements CSVGeneratorService
{
	private String headers;					// The string used to hold the column headers for the .csv file. They will be injected from the config.xml file
	private CSVReader reader;				// A CSVReader class from the opencsv library
	private Scanner scanner;				// An IO Scanner for reading user input from console
	/**
	 * Used to set the headers from the config.xml file
	 * @param headers String should never be used by code only through spring config file 
	 */
	public void setHeaders(String headers)
	{
		this.headers = headers;
	}
	/**
	 * The code for reading and writing the .csv files and for calculating the data being written.
	 * Uses Spring framework so that more complex calculations can be performed without editing this
	 * code
	 */
	@Override
	public void CreateProfitCSV() 
	{		
		int check = 0;		// Creates a local integer for moving in and out of while loops 
		
		while(check == 0)	
		{
			scanner = new Scanner(System.in);																			// Creates a new scanner for getting input from the console
			System.out.println("Please enter file location in correct format (e.g: 'C:\\Users\\Louis\\Documents')");
			String path = scanner.nextLine();							
			System.out.println("Please enter filename in the correct format (e.g 'thefile')");
			String filename = scanner.nextLine();
		
			String inDir = path+"\\"+filename+".csv";																	// Stores the filepath for the existing csv file in local string
			String outDir = path+"\\"+filename+"_profit.csv";															// Stores the filepath for the csv file that will be created.
		
			List<String[]> allRows = null;										// Creates a list for holding the rows parsed from the csv file
		
			while(check == 0)				
			{
				try
				{					
					System.out.println("Reading: '"+inDir+"'");				
					check = 1;
					reader = new CSVReader(new FileReader(inDir),',','"',1);	// Creates a csvreader from opencsv library with the existing csv filepath as the parameter
					allRows = reader.readAll();									// Stores all the parsed rows from the csv file
				}
				catch(FileNotFoundException e)									// If the file path is incorrect the error is caught and the check value is set so that the user is asked to fix input
				{
					check = 0;
					System.out.println(e.getMessage());							
				}
				catch(IOException e)											// Will be called if the reader fails to read the file for any reason. Should not really happen.
				{
					check = 0;
					System.out.println("File was not the correct format. Please make sure you have reference the correct .csv file");
				}
				if(check == 0)													// If an error is caught then the user will be able to fix the input
				{
					System.out.println("Please enter file location in correct format (e.g: 'C:\\Users\\Louis\\Documents')");
					String p = scanner.nextLine();
					System.out.println("Please enter filename in the correct format (e.g 'thefile')");
					String fn = scanner.nextLine();
					inDir = p+"\\"+fn+".csv";
					outDir = p+"\\"+fn+"_profit.csv";
				}
			}		
		
			ArrayList<Instrument> instruments = new ArrayList<Instrument>();	// Creates a local list for holding the instrument objects created from the rows
			
			for(String[] row : allRows)															// for loop that executes code for every row not including the column headers 
			{	
				ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");	// Creates the applicationcontext for initializing classes from the spring config file
				Instrument instrument = (Instrument) context.getBean("instrument");				// Creates an implementation of the Instrument interface as specified in config
				instrument.ReadCVS(Arrays.toString(row));										// Runs the generic method from the Instrument interface that will be spefic to the implementation. It should always calculate data from the rows within the existing csv file
				instruments.add(instrument);													
			}
			check = 0;
			while(check == 0)
			{
				try
				{		
					check = 1;																	
					reader.close();
					CSVWriter writer = new CSVWriter(new FileWriter(outDir));					// Creates a new CSVWriter from the opencsv library pointing at the filepath for the new .csv profit file
				
					writer.writeNext(headers.split(","));										// Writes the column headers to the first row as defined in the config file
				
					for(Instrument instrument : instruments)									// For loop for each instrument generated from the existing .csv file
						writer.writeNext(instrument.csvOutput());								// Gets the string[] that holds each value calculated from the instrument and writes to the new .csv file 
				
					writer.close();				
				}
				catch (IOException e)														
				{
					check = 2;
					System.out.println("Could not write to File. The file may already exist and be open or you lack write permissions.");					
				}
				if(check == 1)
					System.out.println("Created file: '"+outDir+"'");							// If the write is successful a message is diaplyed to the user
			}
			if(check == 2)
				check = 0;
		}
	}	
	
	
	
}
