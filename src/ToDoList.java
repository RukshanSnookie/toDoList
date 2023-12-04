import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class ToDoList{
	
	private static Scanner scan = new Scanner(System.in); // create Scanner object
	private static List<TaskWithStat> taskList = new ArrayList<>(); // Create an ArrayList to store tasks in the order
	private static boolean isMenuOpen = true;  // Create a boolean value to check conditions of the menu
	private static String taskName;
	private static String FILE_PATH = "Tasks.txt"; // file name
	
	public static void main(String[] args) {
		
		// call task load method to find if any file exists
		taskLoad();
		// initialize menu
		while(isMenuOpen) {
			System.out.println("\nWelcome to the \"To-Do List\" App! ");
			System.out.println("\n1. Add Task");
			System.out.println("\n2. View Tasks");
			System.out.println("\n3. Mark Task as Completed");
			System.out.println("\n4. Delete Task");
			System.out.println("\n5. Save and Exit");
			
			try { 
				int menuInput = scan.nextInt(); // taking user input from the menu
				scan.nextLine(); // consume the new line character
				
				switch(menuInput) {
				case 1: // adding Tasks
					addTasks(); // calling add tasks method to add new task
					break;
				case 2:
					viewTasks(); // calling the view tasks method
					break;
				case 3:
					markTasks(); // calling the mark  Tasks method
					break;
				case 4:
					delTasks();  // calling the delete tasks method
					break;
				case 5:
					saveExit();  // calling the save and exit method
					break;
				default:
					System.out.println("Something went wrong. Exiting the program");
					isMenuOpen = false;  // exit the loop if something else happens
				}
			}
			catch(Exception e) {
				System.out.println("Invalid input : please enter a valid input");
				scan.nextLine();
				continue; // to restart the loop
			}
		} // end of the while  loop
	}

	
	public static void addTasks() { // add tasks method
		
		
		System.out.println("Add your tasks here : ");
		
		try {
			String task = scan.nextLine();
			TaskWithStat.status stat = TaskWithStat.status.OPEN; // using the enum value OPEN
			
			TaskWithStat taskStat = new TaskWithStat(task,stat); // creating an object with both task and status
			
			taskList.add(taskStat); // add task with status to the array list
			
			System.out.println("\nYour task added to the list successfully!!");
			backToMenu();
			
		}
		catch(Exception e) {
			System.out.println(e);
		}
		
	}
	
	public static void viewTasks() {
		System.out.println("\nYour Tasks are showing below : ");
		taskList.forEach(items -> System.out.println(items)); // for each loop to iterate arraylist and print items
		backToMenu();
	}
	
	public static void markTasks() {
		
		try {
			System.out.println("\nEnter your task name : ");
			
			boolean taskFound = false;
			taskName = scan.nextLine(); //taking user input, task name
			
			
			for(TaskWithStat items : taskList) {  //iterating through the tasks list
				if(items.task.equalsIgnoreCase(taskName)) {  // checking the case sensitive comparison
					items.stat = TaskWithStat.status.COMPLETED;  // update the status if user input matches the item
					taskFound = true;
					System.out.println("\nTask Updated" + items);
					break;
				}
			}
			
			
			if(!taskFound) {
				System.out.println(taskName + " Not Found");
			}
			backToMenu(); // calling back t menu function 
			
		}
		catch(Exception e) {
			System.out.println("Invalid Input : Please try again!!");
			
		}
	}
	
	public static void delTasks() {
		
		System.out.println("\nDeleting your tasks, are you sure? : (y/n)");
		String delCommand = scan.next().toLowerCase(); // taking user input and convert it to lowercase
		boolean taskFound = false;
		
		if(delCommand.equals("n")) {
			backToMenu();
		}
		else if(delCommand.equals("y")) { // if ans is yes, taking the task name
			try {
				System.out.println("\nEnter your task name : ");
				scan.nextLine(); // consume the next input character
				taskName = scan.nextLine();
				
				Iterator<TaskWithStat> itr = taskList.iterator();  //using iterator to iterate through  list
				while(itr.hasNext()) {
					TaskWithStat item = itr.next();
					if(item.task.trim().equalsIgnoreCase(taskName.trim())) {
						itr.remove(); // deleting the matching item from the list
						taskFound = true;
						System.out.println("Your task deleted : " + item);
						backToMenu();
						break;
					}
				}
				if(!taskFound) {
					System.out.println(taskName + " Not Found");
					backToMenu();
				}
			}
			catch(Exception e) {
				System.out.println(e);
			}
		}
		else {
			System.out.println("Invalid Input !!!");
			saveExit();
		}
	}
	
	public static void saveExit() {
		
		System.out.println("\nSaving to a file and Exit!");
		
		try (FileWriter writer = new FileWriter(FILE_PATH)){ // try with resources to create writer obj
			for(TaskWithStat items : taskList) {
				writer.write(items.task + " , " + items.stat + System.lineSeparator()); // write to the file
			}
			
		}
		catch(IOException e) {
			System.out.println("\nError saving to file " + e.getMessage());
		}
		
		isMenuOpen = false;
	}
	
	public static void backToMenu() {
		System.out.println("\nGo back to Menu or Exit: (y/n)");

			String response = scan.next().toLowerCase(); // storing the next user input as lower case 
			
			if(response.equals("n")) {
				System.out.println("\nSee you again!!");
				saveExit();
			}
			else if(response.equals("y")) {
				return; // return to menu
			}
			else {
				System.out.println("\nSomething Wrong!!1");
				saveExit();
			}		
		
	}
	
	public static void taskLoad() {
		System.out.println("#############################################");
		System.out.println("###   Loading previous file \"" + FILE_PATH + "\"   ###");
		System.out.println("#############################################");
		
		try (Scanner fileScan = new Scanner(new File(FILE_PATH))){
			
			while(fileScan.hasNextLine()) {
				String[] taskData = fileScan.nextLine().trim().split(",");  // read the line and split
				String task = taskData[0]; // Extract the task name
				TaskWithStat.status stat = TaskWithStat.status.valueOf(taskData[1].trim().toUpperCase()); /// extract the task status
				taskList.add(new TaskWithStat(task,stat)); // create TaskWithStat obj and add new items to the task list
			}
		}
		catch(FileNotFoundException e) {
			System.out.println("\nFile not found. Starting with Empty list : ");
		}
		catch(IOException e) {
			System.out.println("\nError loading file " + e.getMessage());
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

