public class Subtask extends Task {
    private int epicID;

    public Subtask(String name, String status, String description, int epicID) {
        super(name, status, description);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
