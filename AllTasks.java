public abstract  class AllTasks {
    private String name;
    private Status status;
    private String description;
    private int id;

    public AllTasks(String name, Status status, String description, int id) {
        this.name = name;
        this.status = status;
        this.description = description;
        this.id = id;
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
    public String toString() {
        return "Имя: " + getName() + ". Статус: " + getStatus() + ".  Описание задачи: " + getDescription() + ". ";
    }
}
