import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Task {
    private String name;
    private String status;
    private String description;
    HashMap<Integer, Task> tasks = new HashMap<>();
    public static final String TASK_CREATED = "Задача создана";
    AtomicInteger generateID = new AtomicInteger(1);

    public Task(String name, String status, String description) {
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void createTask (Task task){
        int result = generateID.getAndIncrement();
        generateID = new AtomicInteger(result);
        tasks.put(result, task);
        System.out.println(TASK_CREATED);
        System.out.println(tasks);
        System.out.println(tasks.size());
    }



}
