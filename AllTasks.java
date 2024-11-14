public abstract  class AllTasks {
    private String name;
    private Status status;
    private String description;

    public AllTasks(String name, Status status, String description) {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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
