import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Central controller for the Effica application
 * Is the bridge between user interface, networking layer, 
 * timer system, calendar, & AI assistant
 */
public class Controller {
    
    private Timer timer;
    private javax.swing.Timer swingTimer;
    private WeeklyCalendar calendar;
    private Task activeTask;
    private NetworkClient network;
    private String currentUser;

    private final String API_KEY = "";
    private final String API_URL = "https://api.groq.com/openai/v1/chat/completions";

    /**
     * Creates a controller without user authentication 
     */
    public Controller() {
        this.timer = new Timer(25, 5);
        this.swingTimer = new javax.swing.Timer(1000, e -> timer.tick());
        swingTimer.start();
    }
    
    /**
     * Creates a controller for a logged in user
     * @param username
     */
    public Controller(String username) {
        this.currentUser = username;
        this.network = new NetworkClient(username);
        this.timer = new Timer(25, 5);
        this.calendar = new WeeklyCalendar(new TaskManager());
        this.swingTimer = new javax.swing.Timer(1000, e -> timer.tick());
        swingTimer.start();
    }

    /**
     * Adds a task to the server
     * @param taskName
     * @param dueDate
     * @param estimatedTime
     * @param classGrade
     * @param status
     * @param priority
     */
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

    /**
     * Deletes a task on the server
     * @param taskID
     * @return
     */
    public boolean removeTask(long taskID) {
        try {
            network.deleteTask(taskID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates an existing task on the server
     * @param taskID
     * @param newTaskName
     * @param newDueDate
     * @param newEstimatedTime
     * @param newClassGrade
     * @param newPriority
     * @return
     */
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

    /**
     * Marks taste as complete
     * @param taskID
     * @return false 
     */
    public boolean markTaskComplete(long taskID) {
        return false;
    }

    /**
     * Retreives all tasks belonging to the current user
     * @return list of tasks or empty list if request fails
     */
    public ArrayList<Task> getAllTasks() {
        try {
            return network.getMyTasks();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves & sorts tasks according to the selected
     * sorting mode
     * @param mode
     * @param currentTime
     * @return
     */
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

    /**
     * Starts the Pomodoro timer session 
     * @param work
     * @param rest
     */
    public void startPomodoro(int work, int rest) {
        timer.reset();
        timer.start();
    }

    // getter & setter methods 

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

    /**
     * Sends the user's prompt and current task context to the
     * AI Model
     * @param userPrompt
     * @return AI generated response or error msg
     */
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

    /**
     * Parses & extracts the response content from AI JSON result
     * @param json
     * @return
     */
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