package TasksManagers;

import Tasks.*;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Scanner;

public interface TaskManager {
    //Методы для меню
    void toCreate(Scanner scanner);

    void toGetTask(Scanner scanner);

    void toChangeTask(Scanner scanner);

    void toDeleteTask(Scanner scanner);

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

    void getPrioritizedTasks();

    //Методы получения
    void getTaskByID(int uniqueNumber) throws IOException;

    void getEpicByID(int uniqueNumber) throws IOException;

    void getSubtaskByID(int uniqueNumber) throws IOException;

    //Методы изменения
    void changeTask(int uniqueID, String newName, Status newStatus, String newDescription,
                    Duration duration, String startTime) throws IOException;

    void changeSubtask(int uniqueID, String newName, Status newStatus, String newDescription, int newEpicID,
                       Duration duration, String startTime)
            throws IOException;

    void changeEpic(String newName, Status newStatus, String newDescription, ArrayList<AllTasks> newSubtaskID,
                    int uniqueID, Duration duration, String startTime) throws IOException;

    void changeEpicStatus(Epic epic) throws IOException;

    int getSubtaskIndex(Epic epic, int subtaskID);

    //Методы меню
    void printMenu();

    void menuForDelete();

    //вывести историю просмотров
    void printHistory();

}
