package redempt.inputscripter2.gui.docs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import redempt.inputscripter2.Main;
import redempt.inputscripter2.utils.DocData;
import redempt.inputscripter2.utils.DocsParser;

public class DocsWindow extends Stage {
	
	private static Map<String, List<DocData>> docs = new HashMap<>();
	
	static {
		String combine = "";
		String line = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/docs.inscdoc")));
		try {
			while ((line = reader.readLine()) != null) {
				combine += line + "\n";
			}
			combine = combine.replace("	", "");
			combine = combine.trim().replaceAll("^\n", "");
			docs = DocsParser.parseDoc(combine);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public DocsWindow() {
		this.setTitle("Tutorials and Documentation");
		this.setMinWidth(400);
		this.setMinHeight(200);
		
		ScrollPane pane = new ScrollPane();
		pane.setFitToWidth(true);
		pane.setFitToHeight(true);
		pane.setMinWidth(400);
		pane.setMinHeight(200);
		
		Scene scene = new Scene(pane);
		this.setScene(scene);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		pane.setContent(grid);
		grid.setAlignment(Pos.CENTER);
		
		int x = 0;
		int y = 0;
		for (String sectionName : docs.keySet()) {
			Button sectionButton = new Button(sectionName);
			sectionButton.setStyle("-fx-font:16px \"Serif\"");
			sectionButton.setMinWidth(180);
			sectionButton.setMinHeight(70);
			sectionButton.setOnAction(event -> {
				new SectionWindow(sectionName, docs.get(sectionName));
			});
			grid.add(sectionButton, x, y);
			x++;
			if (x > 1) {
				x = 0;
				y = 1;
			}
		}
		
		this.show();
	}
	
}
