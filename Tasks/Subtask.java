package Tasks;

import java.util.Objects;
import java.time.Duration;

public class Subtask extends Task {
    private int epicID;

    public Subtask(String name, Status status, String description, int epicID, int id, TypeOfTask type,
                   Duration duration, String startTime) {
        super(name, status, description, id, type, duration, startTime);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicID == subtask.epicID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicID);
    }

    @Override
    public String toStringForFiles() {
        return super.toStringForFiles() + epicID;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
