package DowStockReader;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		
		CompanyDatabase database = CompanyDatabase.getInstance();
		database.loadCompanies("resources/data/companies.csv");
		
		WindowHome home = new WindowHome(primaryStage, database);
		home.show();
		
	}
}

// Data Format:       Company	Symbol	CEO	Founded	Sector
