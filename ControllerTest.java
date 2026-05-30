import java.time.LocalDateTime;

public class ControllerTest {

    public static void main(String[] args) {

        Controller c = new Controller("shripriya");

        c.addTask(
            "Physics HW",
            LocalDateTime.now(),
            60,
            95,
            false,
            5
        );

        System.out.println(c.getAllTasks());


        Controller aarthi = new Controller("aarthi");

        aarthi.addTask(
            "Calculus",
            LocalDateTime.now(),
            45,
            92,
            false,
            4
        );

        System.out.println(aarthi.getAllTasks());

    }
}