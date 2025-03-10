import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtaskID;

    public Epic(String name, Status status, String description, ArrayList<Integer> subtaskID, int id, TypeOfTask type) {
        super(name, status, description, id, type);
        this.subtaskID = subtaskID;
    }

    public ArrayList<Integer> getSubtaskID() {
        return subtaskID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskID, epic.subtaskID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskID);
    }

    @Override
    public String toString() {
        return super.toString();
    }



}
