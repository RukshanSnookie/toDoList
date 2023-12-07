

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ToDoListGUI extends Application{
	
	private Stage primaryStage;
	private static String FILE_PATH = "Tasks.txt";  // file name
	private static List<TaskWithStat> tasksList = new ArrayList<>();// Create an ArrayList to store tasks in the order
	private static int WIDTH = Dimensions.getSceneWidth();
	private static int HEIGHT = Dimensions.getSceneHeight();

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
		
		VBox vbox = new VBox(20);		//create a Vbox layout
		vbox.setAlignment(Pos.CENTER); // set alignment to  center
				
		Label label = new Label("Welcome to the To-Do List");		//create a label
				
		Button enterButton = new Button("Enter");		//create  a button
		
		vbox.getChildren().addAll(label,enterButton);		//add the label and button to the root layout
		
		//set the action to  the button
		enterButton.setOnAction(e -> {
			navigateToMenu();
			taskLoad();
		});
		return new Scene(vbox, WIDTH,HEIGHT); //create the scene with the layout
	}
	
	// loading tasks from file if available
	private void taskLoad() {
		System.out.println("#############################################");
		System.out.println("###   Loading previous file \"" + FILE_PATH + "\"   ###");
		System.out.println("#############################################");
		
		
		try (Scanner fileScan = new Scanner(new File(FILE_PATH))){
			while(fileScan.hasNext()) {
				String [] taskData = fileScan.nextLine().trim().split(","); // read the line and split
				String task = taskData[0]; // extract the task name
				TaskWithStat.status stat = TaskWithStat.status.valueOf(taskData[1].trim().toUpperCase());
				tasksList.add(new TaskWithStat(task,stat));
			}
		}
		catch(FileNotFoundException e) {
			System.out.println("\nFile not found. Starting with an Empty list!!!!");
		}
	}
	
	//create content for the Menu page
	private void navigateToMenu() {

		VBox menuPageLayout = new VBox(20);
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
			
			HBox labelAndButtonBox = new HBox(25);
			labelAndButtonBox.setAlignment(Pos.CENTER);
			
			Label label = new Label(labelText);
			Button goButton = new Button("Go ->");
			
			labelAndButtonBox.getChildren().addAll(label, goButton);
			menuPageLayout.getChildren().addAll(labelAndButtonBox);
			
			HBox.setHgrow(label, Priority.ALWAYS);//center the label horizontally
			HBox.setHgrow(goButton, Priority.ALWAYS);//center the button horizontally
			label.setAlignment(Pos.CENTER_LEFT);//Set label text alignment in the box
			
			goButton.setOnAction(e -> handleGoButtonClick(labelText));			//add action event for each button
		});
		
		Scene menuScene = new Scene(menuPageLayout, 500,400);//Create the new page scene
		primaryStage.setScene(menuScene); //set the menu scene to the primary stage
	}
	
	private void handleGoButtonClick(String labelText) {
		if(labelText.equals("Add Task")) {
			addTasks();
		}
		else if(labelText.equals("View Tasks")) {
			viewTasks();
		}
		else if (labelText.equals("Delete Task")) {
			if(showDeleteConfirmation()) {
				deleteTask();
			}
			else {
				System.out.println("Delete task cancelled by user");
			}
		}
		
		else if(labelText.equals("Save and Exit")) {
			saveTasksList();
			primaryStage.close();
		}
		else {
			System.out.println("return something");
		}
	}
	
	// addtasks method- setting add task scene to primary stage
	private void addTasks() {
		primaryStage.setScene(addTaskPage());
	}
	
	// create add task page/scene
	private Scene addTaskPage() {
		VBox addTaskLayout = new VBox(20);
		addTaskLayout.setAlignment(Pos.CENTER);
		
		addBackButton(addTaskLayout,this::navigateToMenu); // adding the back button
		
		TextField taskTextField = new TextField(); // creating a text field
		taskTextField.setPromptText("Enter your Task"); // enter a display value inside the text field
		
		Label statusLabel = new Label("Status : OPEN");// showing status label for tasks, by default OPEN
		
		Button addTaskButton = new Button("Add Task!"); // create addtask button
		
		//click add task button
		addTaskButton.setOnAction(e -> {
			String taskText = taskTextField.getText();//get the entered task text
			//validating the taskText is not empty
			if(!taskText.isEmpty()) {
				TaskWithStat newTask = new TaskWithStat(taskText, TaskWithStat.status.OPEN);//create a new task with the text and status
				tasksList.add(newTask);// add the task to the array list
				System.out.println("new task added" + tasksList);
				taskTextField.setText("");
				showAlert("New Task!!","Your Task Added Successfully!!");
			}
		});
		addTaskLayout.getChildren().addAll(taskTextField, statusLabel, addTaskButton);//add UI components to the layout
		
		return new Scene(addTaskLayout, WIDTH,HEIGHT);
	}
	
	//View tasks method - setting viewTask scene to primary  page
	private void viewTasks() {
		primaryStage.setScene(viewTasksPage());
	}
	
	//create & design viewTasks scene
	private Scene viewTasksPage() {
		VBox viewTasksLayout = new VBox(20);
		viewTasksLayout.setAlignment(Pos.CENTER);
		
		addBackButton(viewTasksLayout, this :: navigateToMenu);  //adding a back button
		
		List<Label> tasksLabelsList = new ArrayList<>(); // declare a list of labels to hold tasks info
		
		tasksList.forEach(tasks -> {
			Label taskLabel  = new Label(tasks.toString());
			tasksLabelsList.add(taskLabel);
		});
		
		viewTasksLayout.getChildren().addAll(tasksLabelsList);
		return new Scene(viewTasksLayout, WIDTH, HEIGHT);
	}
	
	//delete the task
	private void deleteTask() {
		primaryStage.setScene(deleteTaskPage());
	}
	
	//delete confirmation pop up handle
	private boolean showDeleteConfirmation() {
		//create a confirmation dialog
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete Confirmation");
		alert.setHeaderText("Are you sure???");
		
		ButtonType yesButton = new ButtonType("Yes"); //create a 'yes' button
		ButtonType noButton = new ButtonType("No"); // create a 'no' button 
		
		alert.getButtonTypes().setAll(yesButton, noButton); //setting buttons in alert box
		
		//show alert and wait for the user response
		Optional<ButtonType> results = alert.showAndWait();
		
		return results.isPresent() && results.get()==yesButton;
	}
	
	private Scene deleteTaskPage() {
		VBox deleteTasksLayout = new VBox(20);
		deleteTasksLayout.setAlignment(Pos.CENTER);
		
		addBackButton(deleteTasksLayout, this :: navigateToMenu);// adding a back button
		
		Label textLabel = new Label("Enter the task name");
		TextField taskName = new TextField(); // creating a text field
		taskName.setPromptText("Name of the deleting task"); // setting a prompt text
		
		Button delButton = new Button("Delete");	
		
		delButton.setOnAction(e -> {
			String taskNameText = taskName.getText();
			try {
				boolean taskFound = false;
				Iterator<TaskWithStat> iterate = tasksList.iterator(); // using Iterator to iterate through arraylist
				while(iterate.hasNext()) {
					TaskWithStat item = iterate.next();
					if(item.task.trim().equalsIgnoreCase(taskNameText.trim())) {
						if(showDeleteConfirmation()) {
							iterate.remove(); // remove task from the list
							System.out.println("Task deleted : ");
							taskFound = true;
							taskName.setText("");
						}
						else {
							System.out.println("Delete cancelled");
							taskFound = true;
						}
						break;
					}
				}
				if(!taskFound) {
					System.out.println("Wrong input of deletion");
					Alert notFoundAlert = new Alert(Alert.AlertType.INFORMATION);
					notFoundAlert.setTitle("Something went wrong!!!");
					notFoundAlert.setHeaderText("Entered task not in the list.");
					notFoundAlert.showAndWait();
				}
				
			}
			catch (Exception e2) {
				System.out.println(e2);
			}
		});
		
		deleteTasksLayout.getChildren().addAll(textLabel,taskName,delButton);
		return new Scene(deleteTasksLayout,WIDTH,HEIGHT);
	}
	
	//Alert display
	private void showAlert(String title, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	// creating a back button
	private Button createBackButton(Runnable backAction) {
		Button back = new Button("Back To Menu");
		back.setOnAction(event -> backAction.run());
		return back;
	}
	// add back button to particular layout
	private void addBackButton(VBox layout, Runnable backAction) {
		HBox backButtonBox = new HBox(15);
		backButtonBox.setAlignment(Pos.TOP_RIGHT);
		
		Button backButton = createBackButton(backAction);
		
		backButtonBox.getChildren().addAll(backButton);
		layout.getChildren().add(0, backButtonBox); // Add to the top of the layout
	}
	
	//save list to a file
	private void saveTasksList() {
		System.out.println("\nSaving to a file");
		
		try(FileWriter writer = new FileWriter(FILE_PATH)){
			for(TaskWithStat items : tasksList) {
				writer.write(items.task + " , " + items.stat + System.lineSeparator()); // writing to the file
			}
			showAlert("File Saved","Exiting the program");
		}
		catch(IOException e) {
			System.out.println("\nFile Save Error!!" + e.getMessage());
		}
	}
	
}

class TaskWithStat{
	
	String task;
	status stat;
	
	public static enum status{
		OPEN , COMPLETED
	}
	
	public TaskWithStat(String task, status stat) {
		this.task = task;
		this.stat = stat;
	}
	
	@Override
	public String toString() {
		return "\n"+ task + " , " + stat ;
	}
}
abstract class Dimensions{
	private static final int SCENE_WIDTH = 500;
	private static final int SCENE_HEIGHT = 300;
	
	public static int getSceneWidth(){
		return SCENE_WIDTH;
	}
	public static int getSceneHeight() {
		return SCENE_HEIGHT;
	}
}