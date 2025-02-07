import java.util.List;

public interface HistoryManager {
    //Добавление в историю просмотров
    void add(AllTasks tasks);

    //Возвращает историю просмотров
    CustomLinkedList<AllTasks> getHistory();

    //Удаление из истории просмотров
    void remove(int id);

    void clearHistory();

}
