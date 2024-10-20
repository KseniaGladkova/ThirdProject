import java.util.HashMap;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskID;
    HashMap<Integer, Epic> epics = new HashMap<>();

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

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void setEpics(HashMap<Integer, Epic> epics) {
        this.epics = epics;
    }
}
