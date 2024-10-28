public class Task {
    private String name;
    private String status;
    private String description;

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

    @Override
    public String toString() {
        return "Имя: " + getName() + ". Статус: " + getStatus() + ".  Описание задачи: " + getDescription() + ". ";
    }
}
