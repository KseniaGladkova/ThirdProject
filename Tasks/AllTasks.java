package Tasks;
import java.util.Objects;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class AllTasks {
    private String name;
    private Status status;
    private String description;
    private final int id;
    private TypeOfTask type;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy,HH:mm");

    public AllTasks(String name, Status status, String description, int id,
                    TypeOfTask type, Duration duration, String startTime) {
        this.name = name;
        this.status = status;
        this.description = description;
        this.id = id;
        this.type = type;
        this.duration = duration;
        this.startTime = calculateStartTime(startTime);
        this.endTime = calculateEndTime(this.startTime, this.duration);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime calculateStartTime(String startTime) {
        return LocalDateTime.parse(startTime, formatter);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime calculateEndTime(LocalDateTime startTime, Duration duration) {
        return startTime.plusMinutes(duration.toMinutes());
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllTasks tasks = (AllTasks) o;
        return id == tasks.id && Objects.equals(name, tasks.name) && status == tasks.status
                && Objects.equals(description, tasks.description) && type == tasks.type
                && Objects.equals(startTime, tasks.startTime) && Objects.equals(endTime, tasks.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status, description, id, type, startTime, endTime);
    }

    public String toStringForFiles() {
        return id + ";" + type + ";" + name + ";" + status + ";" + description + ";" + duration.toMinutes() +
                ";" + startTime.format(formatter) + ";";
    }

    @Override
    public String toString() {
        return "Имя: " + getName() + ". Статус: " + getStatus() + ".  Описание задачи: " + getDescription()
                + ". Время старта: " + getStartTime().format(formatter) ;
    }
}

