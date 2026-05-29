import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Controller {
    
    private TaskManager taskManager;
    private Timer timer;
    private WeeklyCalendar calendar;
    private Task activeTask;
    
    private final String API_KEY = "";
    private final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    
    public Controller() {
        this.taskManager = new TaskManager();
        this.timer = new Timer(25, 5);
        this.calendar = new WeeklyCalendar(taskManager);
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }
    
    public void addTask(String taskName, LocalDateTime dueDate, int estimatedTime, int classGrade, boolean status, int priority) {
        Task task = new Task(taskName, dueDate, estimatedTime, classGrade, status, priority);
        taskManager.addTask(task);
    }

    public boolean removeTask(long taskID) {
        return taskManager.removeTask(taskID);
    }

    public boolean editTask(long taskID, String newTaskName, LocalDateTime newDueDate, int newEstimatedTime, int newClassGrade, int newPriority) {
        return taskManager.editTask(taskID, newTaskName, newDueDate, newEstimatedTime, newClassGrade, newPriority);
    }

    public boolean markTaskComplete(long taskID) {
        return taskManager.markComplete(taskID);
    }

    public ArrayList<Task> getAllTasks() {
        return taskManager.getAllTasks();
    }

    public ArrayList<Task> getTasksSortedBy(SortMode mode, String currentTime) {
        return taskManager.getTasksSortedBy(mode, currentTime);
    }

    public void startPomodoro(int work, int breakMin) {
        timer = new Timer(work, breakMin);
        timer.start();
    }

    public WeeklyCalendar getCalendar() {
        return calendar;
    }

    public Timer getTimer() {
        if (timer == null) {
            timer = new Timer(25, 5);
        }
        return timer;
    }

    public void setActiveTask(Task task) {
        activeTask = task;
    }

    public Task getActiveTask() {
        return activeTask;
    }

    public String getAIAdvice(String userPrompt) {
        try {
            StringBuilder context = new StringBuilder();
            context.append("You are Effica AI, a STEM-focused academic assistant. ");
            context.append("Current tasks: ");
            
            for (Task t : taskManager.getAllTasks()) {
                    context.append(String.format("[%s, Priority: %d, Class Grade: %.1f%%] ", t.getTaskName(), t.getPriority(), t.getClassGrade()));
            }
            context.append(". User question: ").append(userPrompt);

            String escapedPrompt = context.toString().replace("\"", "\\\"").replace("\n", " ");

            String jsonBody = "{"
                + "\"model\": \"llama-3.3-70b-versatile\","
                + "\"messages\": [{\"role\": \"user\", \"content\": \"" + escapedPrompt + "\"}]"
                + "}";

            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return parseAIResponse(response.body());

        } catch (Exception e) {
            return "Groq AI offline: " + e.getMessage();
        }
    }

    private String parseAIResponse(String json) {
        try {
            String cleanJson = json.replace("\\n", "\n");

            String targetMarker = "\"content\":\"";
            String compactJson = cleanJson.replaceAll("\\s+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", "");

            if (compactJson.contains(targetMarker)) {
                int start = compactJson.indexOf(targetMarker) + targetMarker.length();
                int end = compactJson.indexOf("\"", start);
                
                String extracted = compactJson.substring(start, end);
                
                return extracted.replace("\\\"", "\"")
                                .replace("\\\\", "\\");
            }
            
            if (cleanJson.contains("\"message\": \"")) {
                int start = cleanJson.indexOf("\"message\": \"") + 12;
                int end = cleanJson.indexOf("\"", start);
                return "API Error: " + cleanJson.substring(start, end);
            }
            
        } catch (Exception e) {
            return "Error parsing AI data: " + e.getMessage();
        }
        
        return "Unexpected response format. Raw data received.";
    }
}