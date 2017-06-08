package redempt.inputscripter2.gui;

import java.io.File;
import java.io.IOException;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import redempt.inputscripter2.utils.Hotkey;

public class GroupList extends Stage {
	
	private GridPane grid;
	
	public GroupList() { 
		this.setTitle("Group Selector");
		ScrollPane pane = new ScrollPane();
		pane.setFitToHeight(true);
		pane.setFitToWidth(true);
		Scene scene = new Scene(pane);
		this.setScene(scene);
		this.setMinWidth(250);
		this.setMinHeight(400);
		pane.setMinWidth(250);
		pane.setMinHeight(400);
		
		grid = new GridPane();
		pane.setContent(grid);
		
		refresh();
		this.show();
	}
	
	public void refresh() {
		grid.getChildren().clear();
		
		Button newDir = new Button("New group");
		newDir.setMinWidth(240);
		newDir.setMinHeight(30);
		newDir.setOnAction(event -> {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("New group");
			dialog.setHeaderText("New group");
			dialog.setContentText("Enter the name for the new group:");
			dialog.showAndWait().ifPresent(text -> {
				File newGroup = new File(text.replace(".", "").replace("/", "").replace("\\", ""));
				newGroup.mkdirs();
				refresh();
			});
		});
		grid.add(newDir, 0, 0);
		
		File file = new File(".");
		int number = 1;
		for (File f : file.listFiles()) {
			if (f.isDirectory()) {
				Button button = new Button(f.getName());
				button.setStyle("-fx-font: 18px \"Arial\"");
				button.setMinHeight(50);
				button.setMinWidth(240);
				button.setOnAction(event -> {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Select action");
					alert.setHeaderText("Select action");
					alert.setContentText("What do you want to do?");
					alert.getButtonTypes().clear();
					alert.getButtonTypes().setAll(new ButtonType("Switch"), new ButtonType("Delete"), ButtonType.CANCEL);
					alert.showAndWait().ifPresent(buttonType -> {
						if (buttonType.getText().equals("Switch")) {
							try {
								GroupList.this.close();
								Hotkey.setDir(f.getName());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (buttonType.getText().equals("Delete")) {
							Alert confirm = new Alert(AlertType.CONFIRMATION);
							confirm.setTitle("Confirm delete");
							confirm.setHeaderText("Confirm delete");
							confirm.setContentText("Are you sure you want to delete this?");
							confirm.getButtonTypes().clear();
							confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
							confirm.showAndWait().ifPresent(buttonChoice -> {
								if (buttonChoice.equals(ButtonType.YES)) {
									GroupList.this.close();
									f.delete();
								}
							});
						}
					});
				});
				grid.add(button, 0, number);
				number++;
			}
		}
	}
	
}
