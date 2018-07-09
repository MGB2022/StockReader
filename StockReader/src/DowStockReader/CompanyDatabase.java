package DowStockReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CompanyDatabase {

	private ArrayList<Company> companies = new ArrayList<>();
	private int numCompanies;
	
	private static CompanyDatabase firstInstance = null;
	
	private CompanyDatabase(){};
	
	public ArrayList<Company> getCompanies() {
		return companies;
	}
	
	public static CompanyDatabase getInstance() {
		if (firstInstance == null)
		{
			firstInstance = new CompanyDatabase();
		}
		return firstInstance;
	}
	
	public void loadCompanies(String filePath) throws FileNotFoundException {
		
		Scanner input;
		input = new Scanner(new File(filePath));

		while (input.hasNext()) {
			String line = input.nextLine();
			String[] fields = line.split(",");
			String realTimeAddr = "https://www.nasdaq.com/symbol/" + fields[1] + "/real-time";
			String archiveAddr = "https://www.nasdaq.com/symbol/" + fields[1];
			
			try {	
				
				Company newCompany = new Company(fields[0], fields[1], fields[2], fields[3], fields[4], realTimeAddr, archiveAddr);
				// https://www.nasdaq.com/symbol/mmm/real-time
				// https://www.nasdaq.com/symbol/mmm
				// Add company to the array list storing all of the companies from the database
				companies.add(newCompany);
			}
			catch(NumberFormatException n)
			{
				AlertBox.display("Database File Error", "Number Format Exception on line " + (numCompanies + 1) + "!");
			}
			
			numCompanies++;
		}
		
		input.close();
		
	}
}
