import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ToDoList {
	
	private static Scanner scan = new Scanner(System.in); // create Scanner object
	private static List<TaskWithStat> taskList = new ArrayList<>(); // Create an ArrayList to store tasks in the order
	private static boolean isMenuOpen = true;  // Create a boolean value to check conditions of the menu

	public static void main(String[] args) {
		
		// initialize menu
		while(isMenuOpen) {
			System.out.println("\nWelcome to the \"To-Do List\" App! ");
			System.out.println("\n1. Add Task");
			System.out.println("\n2. View Tasks");
			System.out.println("\n3. Mark Task as Completed");
			System.out.println("\n4. Delete Task");
			System.out.println("\n5. Save and Exit");
			
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
				System.out.println("Tasks marking as completed : ");
				markTasks(); // calling the mark  Tasks method
				break;
			case 4:
				System.out.println("Deleting your tasks, are you sure? : ");
				delTasks();  // calling the delete tasks method
				break;
			case 5:
				System.out.println("Save and Exit!");
				saveExit();  // calling the save and exit method
				break;
			default:
				System.out.println("Something went wrong. Exiting the program");
				isMenuOpen = false;  // exit the loop if something else happens
			}
			
		} // end of the while  loop
		
	}

	
	public static void addTasks() { // add tasks method
		
		
		System.out.println("Add your tasks here : ");
		
		String task = scan.next();
		TaskWithStat.status stat = TaskWithStat.status.OPEN; // using the enum value OPEN
		
		TaskWithStat taskStat = new TaskWithStat(task,stat); // creating an object with both task and status
		
		taskList.add(taskStat); // add task with status to the array list
		
		System.out.println("\nYour task added to the list successfully!!");
		System.out.println("\nGo back to Menu or Exit: (y/n)");
		String response = scan.next().toLowerCase(); // storing the next user input as lower case 
		
		if(response.equals("n")) {
			System.out.println("See you again!!");
			isMenuOpen = false; // stopping the current loop
		}
		else if(response.equals("y")) {
			return; // return to menu
		}
		else {
			System.out.println("you kicked out! :( ");
			isMenuOpen= false; // stopping the current loop
		}
		
	}
	
	public static void viewTasks() {
		System.out.println("\nYour Tasks are showing below : ");
		taskList.forEach(items -> System.out.println(items)); // for each loop to iterate arraylist and print items
		
		System.out.println("\nGo back to Menu or Exit: (y/n)");
		String response = scan.next().toLowerCase(); // storing the next user input as lower case 
		
		if(response.equals("n")) {
			System.out.println("See you again!!");
			isMenuOpen = false; // stopping the current loop
		}
		else if(response.equals("y")) {
			return; // return to menu
		}
		else {
			System.out.println("you kicked out! :( ");
			isMenuOpen= false; // stopping the current loop
		}
	}
	
	public static void markTasks() {
		System.out.println("in mark tasks");
	}
	
	public static void delTasks() {
		System.out.println("in del tasks");
	}
	
	public static void saveExit() {
		System.out.println("Bye Bye!");
		isMenuOpen = false;
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
		
		return "\n"+ task + " ---> " + stat ;
	}
	
}
