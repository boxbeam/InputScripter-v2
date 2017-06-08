package redempt.inputscripter2.gui.docs;

import java.util.List;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import redempt.inputscripter2.utils.DocData;

public class SectionWindow extends Stage {
	
	public SectionWindow(String sectionName, List<DocData> section) {
		this.setTitle(sectionName);
		this.setMinHeight(400);
		this.setMinWidth(250);
		
		ScrollPane pane = new ScrollPane();
		pane.setMinHeight(400);
		pane.setMinWidth(250);
		pane.setFitToHeight(true);
		pane.setFitToWidth(true);
		Scene scene = new Scene(pane);
		
		GridPane grid = new GridPane();
		pane.setContent(grid);
		this.setScene(scene);
		
		int number = 0;
		for (DocData data : section) {
			Button articleButton = new Button(data.getName());
			articleButton.setMinWidth(240);
			articleButton.setMinHeight(50);
			articleButton.setStyle("-fx-font:16px \"Serif\"");
			articleButton.setOnAction(event -> {
				new ArticleWindow(data);
			});
			grid.add(articleButton, 0, number);
			number++;
		}
		this.show();
	}
	
}
