package DowStockReader;

import javafx.stage.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class AlertBox 
{
	public static void display(String title, String message)
	{
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(400);
		window.setMinHeight(200);
		
		Label label = new Label();
		label.setText(message);
		label.setTextFill(Color.web("white"));

		
		Button closeButton = new Button("Okay");
		closeButton.setStyle("-fx-background-color: white");
		closeButton.setOnAction(e -> window.close());
		
		VBox layout = new VBox(3);
		layout.getChildren().addAll(label, closeButton);
		layout.setAlignment(Pos.CENTER);
		layout.setSpacing(30);
		layout.setStyle("-fx-background-color: #191927");
		
		Scene scene = new Scene(layout);
		window.centerOnScreen();
		
		window.setScene(scene);
		window.showAndWait();
		
	}
}
