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

        return task.getTitle() + ";" +
        task.getEstimatedMins() + ";" +
        task.getClassName() + ";" +
        task.getGrade() + ";" +
        task.getDueDate() + ";" +
        task.getStatus() + ";" +
        task.getPriority() + ";" +
        task.getID();
    }

    public static Task fileStringToTask(String line) {

        String[] parts = line.split(";");

        String title = parts[0];
        int estimatedMins = Integer.parseInt(parts[1]);
        String className = parts[2];
        double grade = Double.parseDouble(parts[3]);
        LocalDateTime dueDate = LocalDateTime.parse(parts[4]);
        boolean status = Boolean.parseBoolean(parts[5]);
        int priority = Integer.parseInt(parts[6]);
        long ID = Long.parseLong(parts[7]);

        return new Task(title,
                        estimatedMins,
                        className,
                        grade,
                        dueDate,
                        status,
                        priority,
                        ID);
    }
}
