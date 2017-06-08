package redempt.inputscripter2.gui.docs;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import redempt.inputscripter2.utils.DocData;

public class ArticleWindow extends Stage {
	
	public ArticleWindow(DocData article) {
		this.setTitle(article.getName());
		ScrollPane pane = new ScrollPane();
		pane.setFitToWidth(true);
		pane.setFitToHeight(true);
		Scene scene = new Scene(pane);
		this.setScene(scene);
		
		pane.setPadding(new Insets(0, 15, 15, 15));
		pane.setContent(new Label(article.getInfo()));
		
		this.sizeToScene();
		this.setMinHeight(this.getHeight());
		this.setMinWidth(this.getWidth());
		this.show();
	}
	
}
