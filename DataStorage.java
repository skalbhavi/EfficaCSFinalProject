import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Task information is saved to files & loaded from files, 
 * allowing users to preserve tasks between sessions. 
 */
public class DataStorage {

    /**
     * Saves a list of tasks to a file 
     * @param tasks
     * @param filename
     */
    public static void saveTasks(ArrayList<Task> tasks, String filename) {
        try {
            PrintWriter  writer = new PrintWriter(new File(filename)); 
            for (Task task: tasks) {
                writer.println(taskToFileString(task));
            } 
            writer.close(); 
        }

        catch (FileNotFoundException e) {
            System.out.println("Error saving file."); 

        }
    }

    /**
     * Loads tasks to a file
     * @param filename
     * @return
     */

    public static ArrayList<Task> loadTasks(String filename) {
        ArrayList<Task> tasks = new ArrayList<>(); 

        try {
            Scanner fileScanner = new Scanner(new File(filename)); 
            while(fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                tasks.add(fileStringToTask(line));
            }
            fileScanner.close(); 
        }

        catch(FileNotFoundException e) {
            System.out.println("File not found."); 
        }

        return tasks; 
    }

    /**
     * Converts a task into a string format
     * @param task
     * @return
     */
    public static String taskToFileString(Task task) {

        return task.getTaskName() + ";" +
        task.getDueDate() + ";" +
        task.getEstimatedTime() + ";" +
        task.getClassGrade() + ";" +
        task.getStatus() + ";" +
        task.getPriority() + ";" +
        task.getTaskID();

    }

    /**
     * Converts a stored string back into a Task object
     * @param line
     * @return
     */
    public static Task fileStringToTask(String line) {

        String[] parts = line.split(";");

        String taskName = parts[0];
        LocalDateTime dueDate = LocalDateTime.parse(parts[1]);
        int estimatedTime = Integer.parseInt(parts[2]);
        int classGrade = Integer.parseInt(parts[3]);
        boolean status = Boolean.parseBoolean(parts[4]);
        int priority = Integer.parseInt(parts[5]);
        long taskID = Long.parseLong(parts[6]);

        return new Task(taskName,
                        dueDate,
                        estimatedTime,
                        classGrade,
                        status,
                        priority,
                        taskID);
    }
}
