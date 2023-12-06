

import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ToDoListGUI extends Application{
	
	private Stage primaryStage;

	public static void main(String[] args) {

		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		this.primaryStage = primaryStage;
		
		Scene initialStage = createInitialScene(); //create the initial scene
		
		primaryStage.setScene(initialStage); //set  the initial scene to the stage
		
		primaryStage.setTitle("To-Do List");		//Set the title of the stage
		
		primaryStage.show();		//show the stage
		
	}

	private Scene createInitialScene() {
		
		VBox vbox = new VBox(10);		//create a Vbox layout
		vbox.setAlignment(Pos.CENTER); // set alignment to  center
				
		Label label = new Label("Welcome to the To-Do List");		//create a label
				
		Button enterButton = new Button("Enter");		//create  a button
		
		vbox.getChildren().addAll(label,enterButton);		//add the label and button to the root layout
		
		//set the action to  the button
		enterButton.setOnAction(e -> {
			navigateToMenu();
		});
		return new Scene(vbox, 500,300); //create the scene with the layout
	}
	
	private void navigateToMenu() {
		//create content for the Menu page

		VBox menuPageLayout = new VBox(15);
		menuPageLayout.setAlignment(Pos.CENTER);
		
		//Add labels to the VBox
		List<String> labels = Arrays.asList(
				"Add Task",
				"View Tasks",
				"Mark Task as Completed",
				"Delete Task",
				"Save and Exit"
				);
		
		labels.forEach(labelText -> {
			
			HBox labelAndButtonBox = new HBox(10);
			labelAndButtonBox.setAlignment(Pos.CENTER);
			
			Label label = new Label(labelText);
			Button goButton = new Button("Go ->");
			
			labelAndButtonBox.getChildren().addAll(label, goButton);
			menuPageLayout.getChildren().addAll(labelAndButtonBox);
			
			goButton.setOnAction(e -> handleGoButtonClick(labelText));			//add action event for each button
		});
		
		Scene menuScene = new Scene(menuPageLayout, 500,400);//Create the new page scene
		primaryStage.setScene(menuScene); //set the menu scene to the primary stage
	}
	
	private void handleGoButtonClick(String labelText) {
		System.out.println(labelText + " Clicked");
	}
}
