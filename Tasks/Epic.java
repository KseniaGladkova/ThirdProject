package Tasks;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Objects;


public class Epic extends Task {
    private ArrayList<AllTasks> subtasks;

    public Epic(String name, Status status, String description, ArrayList<AllTasks> subtasks, int id, TypeOfTask type,
                Duration duration, String startTime) {
        super(name, status, description, id, type, duration, startTime);
        this.subtasks = subtasks;
        setStartTime(calculateStartTime(startTime));
        setEndTime(calculateEndTime(getStartTime(), duration));
    }

    public ArrayList<AllTasks> getSubtasks() {
        return subtasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public LocalDateTime calculateStartTime(String startTime) {
        if (subtasks == null) {
            return null;
        }
        if (subtasks.isEmpty()) {
            return LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        }
        subtasks.sort(new AllTasksComparator());
        return subtasks.getFirst().getStartTime();
    }

    @Override
    public LocalDateTime calculateEndTime(LocalDateTime startTime, Duration duration) {
        if (subtasks == null) {
            return null;
        }
        if (subtasks.isEmpty()) {
            return super.calculateEndTime(startTime, duration);
        }
        LocalDateTime endTime = startTime;
        for (AllTasks subtask : subtasks) {
            endTime.plusMinutes(subtask.getDuration().toMinutes());
        }
        return endTime;
    }
}
