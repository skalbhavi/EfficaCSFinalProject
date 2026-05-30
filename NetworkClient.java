import java.net.URI;
import java.net.http.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Handles all communication between the client application 
 * and the backend TaskServer
 * Sends HTTP requests for user authentication & task management
 * Converts server responses into Task objects 
 * Bridge between UI and remote server
 */

public class NetworkClient {

    private final String SERVER = "http://localhost:8080";
    private HttpClient client = HttpClient.newHttpClient();

    private String currentUser;

    public NetworkClient(String user) {
        this.currentUser = user;
    }

    /**
     * Sends a registration request to the server
     * @param username
     * @param password
     * @throws Exception
     */
    public void register(String username, String password) throws Exception {
        String body = username + "|" + password;
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(SERVER + "/register"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        client.send(req, HttpResponse.BodyHandlers.ofString());
    }
    
    /**
     * Logs a user into the system by validating credentials with the server
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public boolean login(String username, String password) throws Exception {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(SERVER + "/login"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        username + "|" + password))
                .build();

        String res = client.send(req,
                HttpResponse.BodyHandlers.ofString()).body();

        return res.equals("SUCCESS");
    }

    /**
     * Sends a new task to the server for the current user
     * @param name
     * @param due
     * @param time
     * @param grade
     * @param priority
     * @throws Exception
     */
    public void addTask(String name,
                        LocalDateTime due,
                        int time,
                        double grade,
                        int priority) throws Exception {

        String body =
                currentUser + "|" +
                name + "|" +
                due + "|" +
                time + "|" +
                grade + "|" +
                priority;

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(SERVER + "/addTask"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Retrieves all tasks belonging to the current user
     * @return
     * @throws Exception (if HTTP request fails)
     */
    public ArrayList<Task> getMyTasks() throws Exception {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(SERVER + "/tasks?user=" + currentUser))
                .GET()
                .build();

        String res = client.send(req,
                HttpResponse.BodyHandlers.ofString()).body();

        return parseTasks(res);
    }

    /**
     * Retrieves all tasks from calendar endpoint 
     * @return
     * @throws Exception (if HTTP request fails) 
     */
    public ArrayList<Task> getCalendar() throws Exception {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(SERVER + "/calendar"))
                .GET()
                .build();

        String res = client.send(req,
                HttpResponse.BodyHandlers.ofString()).body();

        return parseTasks(res);
    }

    /**
     * Deletes a task by its ID for the current user
     * @param taskID
     * @throws Exception
     */
    public void deleteTask(long taskID) throws Exception {
        String body = currentUser + "|" + taskID;
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(SERVER + "/deleteTask"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Convverts a server response string to a list of Task Objects
     * @param res
     * @return
     */
    private ArrayList<Task> parseTasks(String res) {

        ArrayList<Task> list = new ArrayList<>();

        String[] lines = res.split("\n");

        for (String line : lines) {

            if (line.isBlank()) continue;

            String[] p = line.split("\\|");

            Task t = new Task(
                p[1],
                LocalDateTime.parse(p[2]), 
                Integer.parseInt(p[3]), 
                (int) Double.parseDouble(p[4]), 
                Boolean.parseBoolean(p[6]), 
                Integer.parseInt(p[5]), 
                Long.parseLong(p[0]) 
            );


            list.add(t);
        }

        return list;
    }

    /**
     * Sends a request to update an existing task on the server 
     * @param taskID
     * @param name
     * @param due
     * @param time
     * @param grade
     * @param priority
     * @throws Exception
     */
    public void editTask(long taskID,
                      String name,
                      LocalDateTime due,
                      int time,
                      double grade,
                      int priority) throws Exception {

        String body =
                currentUser + "|" +
                taskID + "|" +
                name + "|" +
                due + "|" +
                time + "|" +
                grade + "|" +
                priority;

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(SERVER + "/editTask"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        client.send(req, HttpResponse.BodyHandlers.ofString());
    }
}