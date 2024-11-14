public interface TaskManager {

    //Методы создания
    void createTask(Task task);
    void createEpic(Epic epic);
    void createSubtask(Subtask subtask, int epicID);

    //Методы удаления
    void RemoveAllTasks();
    void deleteTask(int uniqueID);
    void deleteSubtask(int uniqueID);
    void deleteEpic(int uniqueID);

    //Методы вывода на экран
    void printAllTasks();
    void printAllEpics();
    void printAllSubtasks();
    void printSubtasksForEpic();

    //Методы получения
    void takeTask(int uniqueNumber);
    void takeEpic(int uniqueNumber);
    void takeSubtask(int uniqueNumber);

    //Методы изменения
    void changeTask(int uniqueID, String newName, Status newStatus, String newDescription);
    void changeSubtask(int uniqueID, String newName, Status newStatus, String newDescription, int newEpicID);
    void changeEpic(Epic epic);


}
