import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Controller {
    
    private Timer timer;
    private javax.swing.Timer swingTimer;
    private WeeklyCalendar calendar;
    private Task activeTask;
    private NetworkClient network;
    private String currentUser;

    private final String API_KEY = "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";
    private final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    
    public Controller() {
        this.timer = new Timer(25, 5);
        this.swingTimer = new javax.swing.Timer(1000, e -> timer.tick());
        swingTimer.start();
    }
    
    public Controller(String username) {
        this.currentUser = username;
        this.network = new NetworkClient(username);
        this.timer = new Timer(25, 5);
        this.calendar = new WeeklyCalendar(new TaskManager());
        this.swingTimer = new javax.swing.Timer(1000, e -> timer.tick());
        swingTimer.start();
    }

    public void addTask(String taskName,
                        LocalDateTime dueDate,
                        int estimatedTime,
                        double classGrade,
                        boolean status,
                        int priority) {
        try {
            network.addTask(taskName, dueDate, estimatedTime, classGrade, priority);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean removeTask(long taskID) {
        try {
            network.deleteTask(taskID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editTask(long taskID,
                        String newTaskName,
                        LocalDateTime newDueDate,
                        int newEstimatedTime,
                        double newClassGrade,
                        int newPriority) {
        try {
            network.editTask(taskID,
                    newTaskName,
                    newDueDate,
                    newEstimatedTime,
                    newClassGrade,
                    newPriority);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean markTaskComplete(long taskID) {
        return false;
    }

    public ArrayList<Task> getAllTasks() {
        try {
            return network.getMyTasks();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<Task> getTasksSortedBy(SortMode mode, String currentTime) {
        try {
            ArrayList<Task> tasks = network.getMyTasks();
            TaskManager temp = new TaskManager();
            for (Task t : tasks) temp.addTask(t);
            return temp.getTasksSortedBy(mode, currentTime);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void startPomodoro(int work, int rest) {
        timer.reset();
        timer.start();
    }

    public NetworkClient getNetwork() {
        return network;
    }

    public WeeklyCalendar getCalendar() {
        return calendar;
    }

    public ArrayList<Task> getCalendarTasks() {
        try {
            return network.getCalendar();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Timer getTimer() {
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
            
            for (Task t : getAllTasks()) {
                context.append(String.format("[%s, Priority: %d, Class Grade: %.1f%%] ",
                        t.getTaskName(), t.getPriority(), t.getClassGrade()));
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
                return extracted.replace("\\\"", "\"").replace("\\\\", "\\");
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