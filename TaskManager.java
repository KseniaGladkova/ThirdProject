import java.io.IOException;

public interface TaskManager {
    //Методы для меню
    void toCreate();

    void toGetTask();

    void toChangeTask();

    void toDeleteTask();

    //Методы создания
    void createTask(Task task) throws IOException;

    void createEpic(Epic epic) throws IOException;

    void createSubtask(Subtask subtask, int epicID) throws IOException;

    //Методы удаления
    void removeAllTasks() throws IOException;

    void deleteTaskByID(int taskID) throws IOException;

    void deleteSubtaskByID(int subtaskID) throws IOException;

    void deleteEpicByID(int epicID) throws IOException;

    //Методы вывода на экран
    void printAllTasks() throws IOException;

    void printAllEpics() throws IOException;

    void printAllSubtasks() throws IOException;

    void printSubtasksForEpic();

    //Методы получения
    void getTaskByID(int uniqueNumber) throws IOException;

    void getEpicByID(int uniqueNumber) throws IOException;

    void getSubtaskByID(int uniqueNumber) throws IOException;

    //Методы изменения
    void changeTask(int uniqueID, String newName, Status newStatus, String newDescription) throws IOException;

    void changeSubtask(int uniqueID, String newName, Status newStatus, String newDescription, int newEpicID)
            throws IOException;

    void changeEpic(Epic epic) throws IOException;

    //Методы меню
    void printMenu();

    void menuForDelete();

    //вывести историю просмотров
    void printHistory();

}
