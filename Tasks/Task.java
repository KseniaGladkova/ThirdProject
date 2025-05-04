package Tasks;
import java.time.Duration;

public class Task extends AllTasks {

    public Task(String name, Status status, String description, int id, TypeOfTask type,
                Duration duration, String startTime) {
        super(name, status, description, id, type, duration, startTime);
    }
}
