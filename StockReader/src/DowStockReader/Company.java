package DowStockReader;

import java.util.ArrayList;

public class Company {

	private String company;
	private String symbol;
	private String ceo;
	private String year;
	private String sector;
	private String realTimeAddr;
	private String archiveAddr;
	private String todayHigh;
	private String todayLow;
	private String fiftyTwoHigh;
	private String fiftyTwoLow;
	private String eps;
	
	public Company(String company, String symbol, String ceo, String year, String sector, String realTimeAddr, String archiveAddr) {
		this.company = company;
		this.symbol = symbol;
		this.ceo = ceo;
		this.year = year;
		this.sector = sector;
		this.realTimeAddr = realTimeAddr;
		this.archiveAddr = archiveAddr;
	}
	
	public String getCompany() {
		return company;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public String getCeo() {
		return ceo;
	}
	
	public String getYear() {
		return year;
	}
	
	public String getSector() {
		return sector;
	}
	
	public String getRealTimeAddr() {
		return realTimeAddr;
	}
	
	public String getArchiveAddr() {
		return archiveAddr;
	}
	
	public String getTodayHigh() {
		return todayHigh;
	}
	
	public String getTodayLow() {
		return todayLow;
	}
	
	public String getFiftyTwoHigh() {
		return fiftyTwoHigh;
	}
	
	public String getFiftyTwoLow() {
		return fiftyTwoLow;
	}
	
	public String getEps() {
		return this.eps;
	}
	
	public void loadArchivedData(ArrayList<String> stats) {
		this.todayHigh = stats.get(5);
		this.todayLow = stats.get(7);
		this.fiftyTwoHigh = stats.get(6);
		this.fiftyTwoLow = stats.get(8);
		this.eps = stats.get(4);
	}
}
