public interface TaskManager {

    //Методы создания
    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask, int epicID);

    //Методы удаления
    void removeAllTasks();

    void deleteTaskByID(int taskID);

    void deleteSubtaskByID(int subtaskID);

    void deleteEpicByID(int epicID);

    //Методы вывода на экран
    void printAllTasks();

    void printAllEpics();

    void printAllSubtasks();

    void printSubtasksForEpic();

    //Методы получения
    void getTaskByID(int uniqueNumber);

    void getEpicByID(int uniqueNumber);

    void getSubtaskByID(int uniqueNumber);

    //Методы изменения
    void changeTask(int uniqueID, String newName, Status newStatus, String newDescription);

    void changeSubtask(int uniqueID, String newName, Status newStatus, String newDescription, int newEpicID);

    void changeEpic(Epic epic);

    //Методы меню
    void printMenu();

    void menuForDelete();

    //вывести историю просмотров
    void printHistory();

}
