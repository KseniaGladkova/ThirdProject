import java.util.Objects;

public abstract  class AllTasks {
    private String name;
    private Status status;
    private String description;
    private final int id;
    private TypeOfTask type;

    public AllTasks(String name, Status status, String description, int id, TypeOfTask type) {
        this.name = name;
        this.status = status;
        this.description = description;
        this.id = id;
        this.type = type;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllTasks tasks = (AllTasks) o;
        return id == tasks.id && Objects.equals(name, tasks.name) && status == tasks.status
                && Objects.equals(description, tasks.description) && type == tasks.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status, description, id, type);
    }

    public String toStringForFiles() {
        return id + ";" + type + ";" + name + ";" + status + ";" + description;
    }

    @Override
    public String toString() {
        return "Имя: " + getName() + ". Статус: " + getStatus() + ".  Описание задачи: " + getDescription() + ". ";
    }
}
