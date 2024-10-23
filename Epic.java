import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskID;

    public Epic(String name, String status, String description, ArrayList<Integer> subtaskID) {
        super(name, status, description);
        this.subtaskID = subtaskID;
    }

    public ArrayList<Integer> getSubtaskID() {
        return subtaskID;
    }

    public void setSubtaskID(ArrayList<Integer> subtaskID) {
        this.subtaskID = subtaskID;
    }

    @Override
    public String toString() {
        return super.toString() + "Входящие поздадачи: " + subtaskID;
    }
}
