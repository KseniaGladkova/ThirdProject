import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskID;

    public Epic(String name, Status status, String description, ArrayList<Integer> subtaskID, int id) {
        super(name, status, description, id);
        this.subtaskID = subtaskID;
    }

    public ArrayList<Integer> getSubtaskID() {
        return subtaskID;
    }

    @Override
    public String toString() {
        return super.toString() +  "Входящие поздадачи: " + subtaskID;
    }



}
