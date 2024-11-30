public interface TaskManager {

    //Методы создания
    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask, int epicId);

    //Методы удаления
    void removeAllTasks();

    void deleteTaskById(int taskId);

    void deleteSubtaskById(int subtaskId);

    void deleteEpicById(int epicId);

    //Методы вывода на экран
    void printAllTasks();

    void printAllEpics();

    void printAllSubtasks();

    void printSubtasksForEpic(int epicId);

    //Методы получения
    void getTaskById(int uniqueNumber);

    void getEpicById(int uniqueNumber);

    void getSubtaskById(int uniqueNumber);

    //Методы изменения
    void changeTask(int uniqueID, String newName, Status newStatus, String newDescription);

    void changeSubtask(int uniqueID, String newName, Status newStatus, String newDescription, int newEpicID);

    void changeEpic(Epic epic);


}
