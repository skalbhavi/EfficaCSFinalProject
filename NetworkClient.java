import java.net.URI;
import java.net.http.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class NetworkClient {

    private final String SERVER = "http://localhost:8080";
    private HttpClient client = HttpClient.newHttpClient();

    private String currentUser;

    public NetworkClient(String user) {
        this.currentUser = user;
    }


    public void register(String username, String password) throws Exception {
        String body = username + "|" + password;
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(SERVER + "/register"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        client.send(req, HttpResponse.BodyHandlers.ofString());
    }
    
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

    public ArrayList<Task> getMyTasks() throws Exception {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(SERVER + "/tasks?user=" + currentUser))
                .GET()
                .build();

        String res = client.send(req,
                HttpResponse.BodyHandlers.ofString()).body();

        return parseTasks(res);
    }

    public ArrayList<Task> getCalendar() throws Exception {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(SERVER + "/calendar"))
                .GET()
                .build();

        String res = client.send(req,
                HttpResponse.BodyHandlers.ofString()).body();

        return parseTasks(res);
    }

    public void deleteTask(long taskID) throws Exception {
        String body = currentUser + "|" + taskID;
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(SERVER + "/deleteTask"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        client.send(req, HttpResponse.BodyHandlers.ofString());
    }

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