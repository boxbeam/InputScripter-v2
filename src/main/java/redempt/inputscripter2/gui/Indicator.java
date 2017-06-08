package redempt.inputscripter2.gui;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Indicator extends Stage {
	
	private static List<Indicator> indicators = new CopyOnWriteArrayList<>();
	private Label label;
	
	public Indicator(String text) {
		this.initStyle(StageStyle.UNDECORATED);
		this.setTitle(" ");
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setHeight(30);
		GridPane pane = new GridPane();
		pane.setPadding(new Insets(0, 10, 0, 10));
		pane.setStyle("-fx-background-color:LawnGreen;-fx-border:2px black;-fx-font:16px \"Arial\";");
		pane.setAlignment(Pos.CENTER);
		Scene scene = new Scene(pane);
		label = new Label(text);
		this.setScene(scene);
		label.setAlignment(Pos.CENTER);
		pane.add(label, 0, 0);
		this.setX(0);
		this.setY(indicators.size() * 30);
		indicators.add(this);
		this.show();
	}
	
	public void dispose() {
		Platform.runLater(() -> {
			this.close();
		});
		indicators.remove(this);
		for (Indicator indicator : indicators) {
			indicator.setY(indicators.indexOf(indicator) * 30);
		}
	}
	
	public void setText(String text) {
		Platform.runLater(() -> {
			label.setText(text);
		});
	}
	
	public String getText() {
		return label.getText();
	}
	
}
