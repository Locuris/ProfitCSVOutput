package com.jpmorgan.profittocsv;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * Class for executing main code
 * @author LouisKolodziej
 *
 */
public class Client {

	public static void main(String[] args) 
	{		
		ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");	// Instantiates a new Spring Framework Context and finds the xml config file for defining beans inside the class directory
		CSVGeneratorService cgs = (CSVGeneratorService) context.getBean("profitcsv");	// Instantiates an interface that is then made a solid class from the spring config file		 
		cgs.CreateProfitCSV();															// Runs the instances method for getting an instruments profit from a csv and creating a new csv file in the same location with the filename appended with _profit.csv
	}

}
