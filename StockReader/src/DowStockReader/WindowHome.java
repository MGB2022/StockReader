package DowStockReader;

import java.io.File;

//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public class WindowHome {
	
	private Stage stage;
	private BorderPane pane;
	private Scene scene;
	private WindowCompanyDatabase databaseWindow;
    private File dowjonesFile = new File("resources/images/dowjones.png");
    private Image dowjones = new Image(dowjonesFile.toURI().toString());
    private ImageView iv = new ImageView();
	private Button enterBtn;
	private Label logoLabel;
	private Label disclaimer;
	private VBox labelContainer;
	private VBox logolabelContainer;
	private HBox buttonContainer;
	
	
	public WindowHome(Stage stage, CompanyDatabase database) {
		this.stage = stage;
		
		setNodes();
		styleNodes();
		setStage();
		
		enterBtn.setOnAction(e -> {
			databaseWindow = new WindowCompanyDatabase(stage, database);
			databaseWindow.show();
		});
	}
	
	public void show() {
		stage.show();
	}
	
	private void setNodes() {
		logoLabel = new Label("Stock Reader");
		enterBtn = new Button("Enter");
		disclaimer = new Label("Disclaimer: This is a real-time Dow Jones stock reader \n"
				+ "program that provides company information for the \n"
				+ "30 Dow Jones companies and retrieves the current \n"
				+ "stock prices from the Nasdaq stock exchange");
		
		iv.setImage(dowjones);
		iv.setFitWidth(100);
		iv.setPreserveRatio(true);
		
		labelContainer = new VBox(2);
		labelContainer.getChildren().addAll(logoLabel, disclaimer);
		
		logolabelContainer = new VBox(2);
		logolabelContainer.getChildren().addAll(iv, labelContainer);
		
		buttonContainer = new HBox(1);
		buttonContainer.getChildren().addAll(enterBtn);
	}
	
	private void styleNodes() {
		logoLabel.setTextFill(Color.web("#191927"));
		logoLabel.setFont(Font.font(null, FontWeight.NORMAL, 16));
		logoLabel.setPadding(new Insets(0, 0, 20, 0));
		
		disclaimer.setTextFill(Color.web("#191927"));
		disclaimer.setPadding(new Insets(0, 0, 20, 0));
		disclaimer.setFont(Font.font(null, FontWeight.NORMAL, 10));
		
		enterBtn.setPrefWidth(120.0);
		
		labelContainer.setAlignment(Pos.CENTER);
		labelContainer.setPadding(new Insets(0, 20, 0, 20));
		
		logolabelContainer.setAlignment(Pos.CENTER);
		
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(0, 20, 35, 20));
		buttonContainer.setSpacing(40);
		
	}
	
	private void setStage() {
		stage.setTitle("Home");
		pane = new BorderPane();
		scene = new Scene(pane);
		stage.setScene(scene);
		stage.setHeight(380);
		stage.setWidth(340);
		stage.centerOnScreen();
		
		pane.setCenter(logolabelContainer);
		pane.setBottom(buttonContainer);
		
		pane.setStyle("-fx-background-color: white;");
	}
}
