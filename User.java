public class User {
    
    private String username;
    private String password;
    private final TaskManager tasks;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.tasks = new TaskManager();
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String inputPassword) {
        return (inputPassword.equals(password));
    }

    public TaskManager getTaskManager() {
        return tasks;
    }

    public void changePassword(String newPassword) {
        password = newPassword;
    }

}
