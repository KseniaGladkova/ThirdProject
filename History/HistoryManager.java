package History;

import Tasks.AllTasks;

public interface HistoryManager {
    //Добавление в историю просмотров
    void add(AllTasks tasks);

    //Возвращает историю просмотров
    String getHistory();

    //Удаление из истории просмотров
    void remove(int id);

    void clearHistory();

}
