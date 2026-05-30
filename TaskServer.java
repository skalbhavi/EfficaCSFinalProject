import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.*;

/**
 * HTTP server that takes care of user authentication &
 * task management requests for the application. 
 * Provides endpoints for creating, editing, deleting, &
 * retreiving tasks as well as user login registration. 
 */

public class TaskServer {

    private static ArrayList<User> users = new ArrayList<>();

    /**
     * Starts the HTTP server & initializes API endpoints
     * for user & task operations 
     * 
     * @param args (not used)
     * @throws Exception (if server fails to start)
     */
    public static void main(String[] args) throws Exception {

        users.add(new User("aarthi", "1234"));
        users.add(new User("shripriya", "1234"));
        users.add(new User("manaka", "1234"));

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/login", TaskServer::login);
        server.createContext("/addTask", TaskServer::addTask);
        server.createContext("/tasks", TaskServer::getTasks);
        server.createContext("/calendar", TaskServer::getCalendar);
        server.createContext("/deleteTask", TaskServer::deleteTask);
        server.createContext("/editTask", TaskServer::editTask);
        server.createContext("/register", TaskServer::register);

        server.setExecutor(null);

        System.out.println("Server running on 8080");
        server.start();
    }

    /**
     * Registers a new user if the username has not already been used
     * @param ex
     * @throws IOException
     */
    private static void register(HttpExchange ex) throws IOException {

        String body = read(ex);
        String[] p = body.split("\\|");

        String username = p[0].trim();
        String password = p[1].trim();

        if (findUser(username) != null) {
            write(ex, "USER_EXISTS");
            return;
        }

        users.add(new User(username, password));
        write(ex, "SUCCESS");
    }

    /**
     * Authenticates a user by using their username & password
     * @param ex
     * @throws IOException
     */
    private static void login(HttpExchange ex) throws IOException {

        String body = read(ex);
        String[] p = body.split("\\|");

        String username = p[0].trim();
        String password = p[1].trim();

        for (User u : users) {
            if (u.getUsername().equals(username)
                    && u.checkPassword(password)) {
                write(ex, "SUCCESS");
                return;
            }
        }

        write(ex, "FAIL");
    }

    /**
     * Adds a new task to a user's task manager
     * @param ex
     * @throws IOException
     */
    private static void addTask(HttpExchange ex) throws IOException {

        String body = read(ex);
        String[] p = body.split("\\|");

        String username = p[0];

        User u = findUser(username);

        if (u == null) {
            write(ex, "USER_NOT_FOUND");
            return;
        }

        Task t = new Task(
                p[1],
                LocalDateTime.parse(p[2]),
                Integer.parseInt(p[3]),
                Double.parseDouble(p[4]),
                false,
                Integer.parseInt(p[5])
        );

        u.getTaskManager().addTask(t);
        write(ex, "OK");
    }

    /**
     * Returns all tasks for a specific user
     * @param ex
     * @throws IOException
     */
    private static void getTasks(HttpExchange ex) throws IOException {

        String query = ex.getRequestURI().getQuery();
        String user = query.split("=")[1];

        User u = findUser(user);

        if (u == null) {
            write(ex, "");
            return;
        }

        write(ex, serialize(u.getTaskManager().getAllTasks()));
    }

    /**
     * Returns all tasks across all users for the calendar display 
     * @param ex
     * @throws IOException
     */

    private static void getCalendar(HttpExchange ex) throws IOException {

        ArrayList<Task> all = new ArrayList<>();

        for (User u : users) {
            all.addAll(u.getTaskManager().getAllTasks());
        }

        write(ex, serialize(all));
    }

    /**
     * Deletes a task for a given user by task ID 
     * @param ex
     * @throws IOException
     */
    private static void deleteTask(HttpExchange ex) throws IOException {

        String body = read(ex);
        String[] p = body.split("\\|");

        String username = p[0];
        long id = Long.parseLong(p[1]);

        User u = findUser(username);

        if (u != null) {
            u.getTaskManager().removeTask(id);
        }

        write(ex, "OK");
    }

    /**
     * Updates an existing task's details for the user
     * @param ex
     * @throws IOException
     */
    private static void editTask(HttpExchange ex) throws IOException {

        String body = read(ex);
        String[] p = body.split("\\|");

        String username = p[0];
        long taskID = Long.parseLong(p[1]);
        String name = p[2];
        LocalDateTime due = LocalDateTime.parse(p[3]);
        int time = Integer.parseInt(p[4]);
        double grade = Double.parseDouble(p[5]);
        int priority = Integer.parseInt(p[6]);

        User u = findUser(username);

        if (u == null) {
            write(ex, "USER_NOT_FOUND");
            return;
        }

        Task t = u.getTaskManager().findTaskById(taskID);

        if (t != null) {
            t.setTaskName(name);
            t.setDueDate(due);
            t.setEstimatedTime(time);
            t.setClassGrade(grade);
            t.setPriority(priority);
        }

        write(ex, "OK");
    }

    /**
     * Searches for a user via username 
     * @param username
     * @return the matched User object; null if not found
     */
    private static User findUser(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Reads the full request body from an HTTP exchange
     * @param ex
     * @return
     * @throws IOException
     */
    private static String read(HttpExchange ex) throws IOException {

        BufferedReader br = new BufferedReader(
                new InputStreamReader(ex.getRequestBody()));

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

    /**
     * Sends a plain text response to the client
     * @param ex
     * @param response
     * @throws IOException
     */
    private static void write(HttpExchange ex, String response)
            throws IOException {

        ex.sendResponseHeaders(200, response.getBytes().length);

        OutputStream os = ex.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    /**
     * Converts a list of tasks into a string format 
     * @param tasks
     * @return
     */

    private static String serialize(ArrayList<Task> tasks) {

        StringBuilder sb = new StringBuilder();

        for (Task t : tasks) {
            sb.append(t.getTaskID()).append("|")
                    .append(t.getTaskName()).append("|")
                    .append(t.getDueDate()).append("|")
                    .append(t.getEstimatedTime()).append("|")
                    .append(t.getClassGrade()).append("|")
                    .append(t.getPriority()).append("|")
                    .append(t.getStatus()).append("\n");
        }

        return sb.toString();
    }
}