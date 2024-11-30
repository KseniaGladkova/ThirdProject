import java.util.ArrayList;

public interface HistoryManager {

    void add(AllTasks tasks);

    ArrayList<AllTasks> getHistory();

}
