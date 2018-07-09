package DowStockReader;

import java.io.BufferedReader;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStreamReader;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class WindowCompanyDatabase {
	
	private Stage stage;
	private BorderPane pane;
	private Scene scene;
	
	private ObservableList<Company> selectedList = FXCollections.observableArrayList();
	private ArrayList<String> highLowStats = new ArrayList<String>();
	private ArrayList<Double> prices = new ArrayList<Double>();
	private TableView<Company> companyTable;
	private TableView<Company> stockTable;
	private File nasdaqFile = new File("resources/images/nasdaq.png");
	private File dowjonesFile = new File("resources/images/dowjonesflat.png");
	private File upArr = new File("resources/images/upArrow.png");
	private File downArr = new File("resources/images/downArrow.png");
	private File dashSym = new File("resources/images/dash.png");
	private Image up = new Image(upArr.toURI().toString());
	private Image down = new Image(downArr.toURI().toString());
	private Image dash = new Image(dashSym.toURI().toString());
	private Image nasdaq = new Image(nasdaqFile.toURI().toString());
	private Image dowjones = new Image(dowjonesFile.toURI().toString());
	private ImageView iv1 = new ImageView();
	private ImageView iv2 = new ImageView();
	private ImageView iv3 = new ImageView();
	private Label writtenBy = new Label("Written By Adam Churchwell");
	private Label poweredByLabel = new Label("Powered By ");
	private Label coName;
	private Label priceLabel;
	private Label newSymbol;
	private VBox tableContainer;
	private VBox headerContainer;
	private VBox imageBox;
	private HBox subHeader;
	private HBox poweredBy;
	private VBox bodyContainer;
	private VBox footerSection;
	private HBox nameContainer;
	private HBox symContainer;
	private HBox dataHeader;
	private HBox dataContainer;
	private HBox arrowBox;
	private HBox priceUpdater;
	
	// Primitives
	private String newPrice;

	// Concurrancy Obect
	private Service<Void> backgroundThread;

	public WindowCompanyDatabase(Stage stage, CompanyDatabase database) {
		this.stage = stage;

		setLayout(database);
		dataHeader.setVisible(false);
		dataContainer.setVisible(false);
		arrowBox.setVisible(false);
		
		companyTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				
				prices.clear();
				arrowBox.setVisible(false);	
				
				dataHeader.setVisible(true);
				dataContainer.setVisible(true);
				coName.setText(newSelection.getCompany());
				newSymbol.setText(newSelection.getSymbol());
				stockTable.getItems().remove(oldSelection);
				
				highLowStats = getStats(newSelection);
				newSelection.loadArchivedData(highLowStats);
				selectedList.add(newSelection);
				stockTable.setItems(selectedList);
				highLowStats.removeAll(highLowStats);
				
				backgroundThread = new Service<Void>() {

					@Override
					protected Task<Void> createTask() {
						
						return new Task<Void>() {

							@Override
							protected Void call() throws Exception {
								
								for (int j = 0; j < 200; j++) {
									newPrice = getPrice(newSelection);
									
									double priceAsDouble = Double.parseDouble(newPrice.substring(2));
									prices.add(priceAsDouble);
									
									if (prices.size() > 1) {
										updateMessage("updating");
										Thread.sleep(700);
										if (prices.get(j).compareTo(prices.get(j-1)) == 0) {
											iv3.setImage(dash);
										} else if (prices.get(j).compareTo(prices.get(j-1)) > 0) {
											iv3.setImage(up);
										} else {
											iv3.setImage(down);
										}
										arrowBox.setVisible(true);	
									}					
													
									updateMessage(newPrice);
									Thread.sleep(3600);
								}
							
								return null;
							}
						};
					}				
				};
				
				backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					public void handle(WorkerStateEvent event) {
						System.out.println("Done");
						priceLabel.textProperty().unbind();
					}
				});
				
				priceLabel.textProperty().bind(backgroundThread.messageProperty());
				
				backgroundThread.restart();
		    }
		});
	}
	
	public void show() {
		stage.show();
	}
	
	@SuppressWarnings("unchecked")
	private void setLayout(CompanyDatabase database) {
		
		// -----------------------------------------------
		// ----------------LOGO & IMAGES------------------
		// -----------------------------------------------
		iv1.setImage(nasdaq);
		iv1.setFitWidth(70);
		iv1.setPreserveRatio(true);
		
		iv2.setImage(dowjones);
		iv2.setFitWidth(300);
		iv2.setPreserveRatio(true);
		
		iv3.setFitWidth(22);
		iv3.setPreserveRatio(true);
		
		// -----------------------------------------------
		// --------------------TOP TABLE------------------
		// -----------------------------------------------
		ObservableList<Company> companies = FXCollections.observableArrayList(database.getCompanies());
		
		TableColumn<Company, String> companyColumn = new TableColumn<>("Company");
		companyColumn.setMinWidth(204);
		companyColumn.setCellValueFactory(new PropertyValueFactory<>("company"));
		
		TableColumn<Company, String> symbolColumn = new TableColumn<>("Symbol");
		symbolColumn.setMinWidth(70);
		symbolColumn.setCellValueFactory(new PropertyValueFactory<>("symbol"));
		
		TableColumn<Company, String> ceoColumn = new TableColumn<>("CEO");
		ceoColumn.setMinWidth(150);
		ceoColumn.setCellValueFactory(new PropertyValueFactory<>("ceo"));
		
		TableColumn<Company, String> foundedColumn = new TableColumn<>("Founded");
		foundedColumn.setMinWidth(60);
		foundedColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
		
		TableColumn<Company, String> sectorColumn = new TableColumn<>("Sector");
		sectorColumn.setMinWidth(284);
		sectorColumn.setCellValueFactory(new PropertyValueFactory<>("sector"));
		
		companyTable = new TableView<>();
		companyTable.getColumns().addAll(companyColumn, symbolColumn, ceoColumn, foundedColumn, sectorColumn);
		companyTable.setItems(companies);
		
		// -----------------------------------------------
		// -----------------BOTTOM TABLE------------------
		// -----------------------------------------------
		
		TableColumn<Company, String> todayHighColumn = new TableColumn<>("Today's High");
		todayHighColumn.setMinWidth(100);
		todayHighColumn.setCellValueFactory(new PropertyValueFactory<>("todayHigh"));
		
		TableColumn<Company, String> todayLowColumn = new TableColumn<>("Today's Low");
		todayLowColumn.setMinWidth(100);
		todayLowColumn.setCellValueFactory(new PropertyValueFactory<>("todayLow"));
		
		TableColumn<Company, String> fiftyHighColumn = new TableColumn<>("52-Wk High");
		fiftyHighColumn.setMinWidth(100);
		fiftyHighColumn.setCellValueFactory(new PropertyValueFactory<>("fiftyTwoHigh"));
		
		TableColumn<Company, String> fiftyLowColumn = new TableColumn<>("52-Wk Low");
		fiftyLowColumn.setMinWidth(100);
		fiftyLowColumn.setCellValueFactory(new PropertyValueFactory<>("fiftyTwoLow"));
		
		TableColumn<Company, String> epsColumn = new TableColumn<>("EPS");
		epsColumn.setMinWidth(70);
		epsColumn.setCellValueFactory(new PropertyValueFactory<>("eps"));
			
		stockTable = new TableView<Company>();
		stockTable.getColumns().addAll(todayHighColumn, todayLowColumn, fiftyHighColumn, fiftyLowColumn, epsColumn);
		
		// -----------------------------------------------
		// ------------BORDER PANE PLACEMENT--------------
		// -----------------------------------------------
		tableContainer = new VBox(1);
		tableContainer.getChildren().addAll(companyTable);
		
		imageBox = new VBox(1);
		imageBox.getChildren().add(iv1);
		imageBox.setPadding(new Insets(-4,0,0,0));
		
		poweredBy = new HBox(2);
		poweredBy.getChildren().addAll(poweredByLabel,imageBox);
		
		subHeader = new HBox(2);
		subHeader.getChildren().addAll(writtenBy, poweredBy);

		headerContainer = new VBox(2);
		headerContainer.getChildren().addAll(iv2, subHeader);
		
		bodyContainer = new VBox(2);
		bodyContainer.getChildren().addAll(tableContainer);
		
		priceLabel = new Label();
		priceLabel.setFont(Font.font(22));
		
		coName = new Label();
		coName.setFont(Font.font(28));
		
		newSymbol = new Label();
		newSymbol.setFont(Font.font(22));
		
		nameContainer = new HBox(1);
		nameContainer.getChildren().add(coName);
		nameContainer.setMaxWidth(300);
		nameContainer.setMinWidth(300);
		nameContainer.setAlignment(Pos.CENTER);
		
		symContainer = new HBox(1);
		symContainer.getChildren().add(newSymbol);
		symContainer.setMaxWidth(100);
		symContainer.setMinWidth(100);
		symContainer.setAlignment(Pos.CENTER_RIGHT);
		
		priceUpdater = new HBox(1);
		priceUpdater.getChildren().add(priceLabel);
		priceUpdater.setPadding(new Insets(0,0,0,0));
		priceUpdater.setMinWidth(130);
		priceUpdater.setMaxWidth(130);
		priceUpdater.setAlignment(Pos.CENTER);
		
		arrowBox = new HBox(1);
		arrowBox.getChildren().add(iv3);
		arrowBox.setPadding(new Insets(0,0,0,0));
		arrowBox.setMinWidth(100);
		arrowBox.setMaxWidth(100);
		arrowBox.setAlignment(Pos.CENTER_LEFT);
		
		dataHeader = new HBox(3);
		dataHeader.getChildren().addAll(symContainer, priceUpdater, arrowBox);
		dataHeader.setSpacing(20);
		dataHeader.setAlignment(Pos.CENTER);

		dataContainer = new HBox(1);
		dataContainer.getChildren().addAll(stockTable);
		dataContainer.setSpacing(100);
		dataContainer.setAlignment(Pos.CENTER);
	
		footerSection = new VBox(3);
		footerSection.getChildren().addAll(nameContainer, dataHeader, dataContainer);
		footerSection.setSpacing(15);
		
		footerSection.setPadding(new Insets(-250,50,0,50));
		footerSection.setStyle("-fx-border-style: solid inside;" +
								"-fx-border-color: black;" +
								"-fx-border-width: 2px;");
		
		footerSection.setAlignment(Pos.CENTER);
		
		// -----------------------------------------------
		// -------------------STYLING---------------------
		// -----------------------------------------------
		companyTable.setMinHeight(300);
		companyTable.setMaxHeight(300);
		
		stockTable.setMaxHeight(60);
		stockTable.setMinWidth(300);
		
		headerContainer.setPadding(new Insets(10,50,10,50));
		headerContainer.setSpacing(20);
		headerContainer.setAlignment(Pos.CENTER);
		
		bodyContainer.setPadding(new Insets(0,50,0,50));
		
		subHeader.setSpacing(470.0);
		
		// -----------------------------------------------
		// -----------------BORDER PANE-------------------
		// -----------------------------------------------
		stage.setTitle("Real Time Dow Jones Data");
		pane = new BorderPane();
		scene = new Scene(pane);
		
		stage.setScene(scene);
		stage.setHeight(740);
		stage.setWidth(900);
		stage.centerOnScreen();
		
		pane.setTop(headerContainer);
		pane.setCenter(bodyContainer);
		pane.setBottom(footerSection);
		pane.setStyle("-fx-background-color: white;");
	}
	

	
	private String getPrice(Company newSelection) {
		
		URL url = null;
		try {
			url = new URL(newSelection.getRealTimeAddr());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		URLConnection urlConn = null;
		try {
			urlConn = url.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		InputStreamReader inStream = null;
		try {
			inStream = new InputStreamReader(urlConn.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedReader buff = new BufferedReader(inStream);
		String line = null;
		try {
			line = buff.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String price = "not found";
		
		while(line != null)
		{	
			if(line.contains("<td>$") && !line.contains("Previous")) {

				int target = line.indexOf("<td>$");
				int deci = line.indexOf(".", target);
				
				int start = deci;
				while(line.charAt(start) != '$')
				{
					start--;
				}
				
				price = line.substring(start + 1, deci + 3);
				String priceFormatted = "$ " + price;
				return priceFormatted;
			}
			try {
				line = buff.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String priceFormatted = "$ " + price;
		return priceFormatted;
	}
	
	private ArrayList<String> getStats(Company newSelection) {
		
		String price = "not found";
		ArrayList<String> prices = new ArrayList<String>();
		String SYM = newSelection.getSymbol();

		URL url = null;
		try {
			url = new URL("https://www.nasdaq.com/symbol/" + SYM);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		URLConnection urlConn = null;
		try {
			urlConn = url.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
		InputStreamReader inStream = null;
		try {
			inStream = new InputStreamReader(urlConn.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		BufferedReader buff = new BufferedReader(inStream);
		String line = null;
		try {
			line = buff.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		while(line != null)
		{
//					System.out.println(line);
			
			if(line.contains("$&nbsp;")) {

				int target = line.indexOf("$&nbsp;");
				int deci = line.indexOf(".", target);
				
				int start = deci;
				while(line.charAt(start) != ';')
				{
					start--;
				}
				
				price = line.substring(start + 1, deci + 3);
				
				prices.add("$ " + price);
			}
			try {
				line = buff.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return prices;
	}
}
