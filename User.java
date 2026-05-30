
/**
 * Represents a user of the task management system
 * Each user has a username and password
 */
public class User {
    
    private String username;
    private String password;
    private final TaskManager tasks;

    /**
     * Creates a new user with the specified username and password
     * @param username
     * @param password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.tasks = new TaskManager();
    }

    /**
     * returns the username of the user
     * @return the username 
     */

    public String getUsername() {
        return username;
    }
    
    /**
     * Verifies whether the provided password matches the user's password
     * @param inputPassword
     * @return true if the password matches; false otherwise
     */
    public boolean checkPassword(String inputPassword) {
        return (inputPassword.equals(password));
    }

    /**
     * Returns the user's task manager
     * @return the task manager associated with the user
     */

    public TaskManager getTaskManager() {
        return tasks;
    }

    /**
     * Updates the user's password
     * @param newPassword
     */

    public void changePassword(String newPassword) {
        password = newPassword;
    }

}
