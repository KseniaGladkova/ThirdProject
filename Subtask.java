public class Subtask extends Task {
    private int epicID;

    public Subtask(String name,  Status status, String description, int epicID, int id) {
        super(name, status, description, id);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
