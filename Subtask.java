import java.util.HashMap;

public class Subtask extends Task {
    private int epicID;
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Subtask(String name, String status, String description, int epicID) {
        super(name, status, description);
        this.epicID = epicID;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(HashMap<Integer, Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }




}
