import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;


public class DataStorage {
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

    public static String taskToFileString(Task task) {

        return task.getTaskName() + ";" +
        task.getDueDate() + ";" +
        task.getEstimatedTime() + ";" +
        task.getClassGrade() + ";" +
        task.getStatus() + ";" +
        task.getPriority() + ";" +
        task.getTaskID();

    }

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
