import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<AllTasks> history = new ArrayList<>();

    public void setHistory(ArrayList<AllTasks> history) {
        this.history = history;
    }

    @Override
    public void add(AllTasks tasks) {
        if (!history.contains(tasks)) {
            history.add(tasks);
        }
    }

    @Override
    public ArrayList<AllTasks> getHistory() {
        if (history.size() == 10) {
            history.removeFirst();
        }
        return history;
    }

}
