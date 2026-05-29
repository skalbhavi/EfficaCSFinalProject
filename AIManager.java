import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class AIManager {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private final String apiKey;

    public AIManager(String apiKey) {
        this.apiKey = apiKey;
    }

    public String askAI(String userPrompt, ArrayList<Task> tasks) {
        try {
            StringBuilder context = new StringBuilder();
            context.append("You are Effica AI, a STEM-focused productivity assistant. ");
            context.append("Here is the user's current task list:\n");
            
            for (Task t : tasks) {
                context.append(String.format("- %s (Due: %s, Class: %s, Priority: %d/5)\n", 
                    t.getTaskName(), t.getDueDate(), t.getPriority()));
            }

            context.append("\nUser Question: ").append(userPrompt);

            String jsonPayload = "{"
                + "\"model\": \"gpt-4o-mini\"," 
                + "\"messages\": [{\"role\": \"user\", \"content\": \"" + context.toString().replace("\"", "\\\"").replace("\n", "\\n") + "\"}]"
                + "}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String body = response.body();
            if (body.contains("\"content\": \"")) {
                int start = body.indexOf("\"content\": \"") + 12;
                int end = body.indexOf("\"", start);
                return body.substring(start, end).replace("\\n", "\n");
            }
            return body;
            
        } catch (Exception e) {
            return "Error connecting to AI: " + e.getMessage();
        }

    }
}