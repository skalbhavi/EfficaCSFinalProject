
public class Main {
    public static void main(String[] args) {
        // Explicitly create YOUR controller
        Controller controller = new Controller(); 
        
        // Ensure you are passing it to your UI
        new EfficaUI(controller); 
        
    }
}