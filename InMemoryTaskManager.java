import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class InMemoryTaskManager implements TaskManager {
    static Scanner scanner = new Scanner(System.in);
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    public int generateID = 1;

    public void runApp() {
        while (true) {
            printMenu();
            String command = scanner.nextLine();
            if (!isNumeric(command)) {
                System.out.println(Constants.PRINT_NUMBER);
                continue;
            }
            int comm = Integer.parseInt(command);
            switch (comm) {
                case 1:
                    System.out.println(Constants.PRINT_NAME);
                    String name = scanner.nextLine();
                    System.out.println(Constants.PRINT_DESCRIPTION);
                    String description = scanner.nextLine();
                    System.out.println(Constants.PRINT_TYPE_OF_TASK);
                    String type = scanner.nextLine();
                    if (!isNumeric(type)) {
                        System.out.println(Constants.PRINT_NUMBER);
                        continue;
                    }
                    int typeOfTask = Integer.parseInt(type);
                    switch (typeOfTask) {
                        case 1:
                            Task task = new Task(name, Status.NEW, description);
                            createTask(task);
                            break;
                        case 2:
                            Epic epic = new Epic(name, Status.NEW, description, new ArrayList<>());
                            createEpic(epic);
                            break;
                        case 3:
                            System.out.println(Constants.CHOOSE_EPIC);
                            String numberOdEpic = scanner.nextLine();
                            if (!isNumeric(numberOdEpic)) {
                                System.out.println(Constants.PRINT_NUMBER);
                                continue;
                            }
                            int epicID = Integer.parseInt(numberOdEpic);
                            if (!epics.containsKey(epicID)) {
                                System.out.println(Constants.EPIC_IS_NOT_EXIST);
                                continue;
                            }
                            Subtask subtask = new Subtask(name, Status.NEW, description, epicID);
                            createSubtask(subtask, epicID);
                            break;
                        default:
                            System.out.println(Constants.NOT_COMMAND);
                            break;
                    }
                    break;
                case 2:
                    printAllTasks();
                    printAllEpics();
                    printAllSubtasks();
                    break;
                case 3:
                    RemoveAllTasks();
                    break;
                case 4:
                    System.out.println(Constants.PRINT_TYPE_OF_TASK);
                    String typeOfTasks = scanner.nextLine();
                    if (!isNumeric(typeOfTasks)) {
                        System.out.println(Constants.PRINT_NUMBER);
                        continue;
                    }
                    int typeOf = Integer.parseInt(typeOfTasks);
                    System.out.println(Constants.PRINT_ID);
                    String taskID = scanner.nextLine();
                    if (!isNumeric(taskID)) {
                        System.out.println(Constants.PRINT_NUMBER);
                        continue;
                    }
                    int uniqueNumber = Integer.parseInt(taskID);
                    switch (typeOf) {
                        case 1:
                            takeTask(uniqueNumber);
                            break;
                        case 2:
                            takeEpic(uniqueNumber);
                            break;
                        case 3:
                            takeSubtask(uniqueNumber);
                            break;
                        default:
                            System.out.println(Constants.NOT_COMMAND);
                            break;
                    }
                    break;
                case 5:
                    System.out.println(Constants.CHOOSE_TYPE_FOR_CHANGE);
                    String var = scanner.nextLine();
                    if (!isNumeric(var)) {
                        System.out.println(Constants.PRINT_NUMBER);
                        continue;
                    }
                    int variant = Integer.parseInt(var);
                    System.out.println(Constants.PRINT_ID);
                    String uniqueNewID = scanner.nextLine();
                    if (!isNumeric(uniqueNewID)) {
                        System.out.println(Constants.PRINT_NUMBER);
                        continue;
                    }
                    int uniqueID = Integer.parseInt(uniqueNewID);
                    System.out.println(Constants.PRINT_NAME);
                    String newName = scanner.nextLine();
                    System.out.println(Constants.PRINT_DESCRIPTION);
                    String newDescription = scanner.nextLine();
                    System.out.println(Constants.PRINT_STATUS);
                    String status1 = scanner.nextLine();
                    if ((!status1.equals("DONE")) && (!status1.equals("IN_PROGRESS"))) {
                        System.out.println(Constants.NOT_FOUND);
                        continue;
                    }
                    Status newStatus = Status.valueOf(status1);
                    switch (variant) {
                        case 1:
                            changeTask(uniqueID, newName, newStatus, newDescription);
                            break;
                        case 2:
                            System.out.println(Constants.CHOOSE_EPIC);
                            String newEpic = scanner.nextLine();
                            if (!isNumeric(newEpic)) {
                                System.out.println(Constants.PRINT_NUMBER);
                                continue;
                            }
                            int newEpicID = Integer.parseInt(newEpic);
                            changeSubtask(uniqueID, newName, newStatus, newDescription, newEpicID);
                            break;
                        default:
                            System.out.println(Constants.NOT_COMMAND);
                            break;
                    }
                    break;
                case 6:
                    menuForDelete();
                    String point = scanner.nextLine();
                    if (!isNumeric(point)) {
                        System.out.println(Constants.PRINT_NUMBER);
                        continue;
                    }
                    int pointOfMenu = Integer.parseInt(point);
                    System.out.println(Constants.PRINT_ID);
                    String deleteID = scanner.nextLine();
                    if (!isNumeric(deleteID)) {
                        System.out.println(Constants.PRINT_NUMBER);
                        continue;
                    }
                    int IDForDelete = Integer.parseInt(deleteID);
                    switch (pointOfMenu) {
                        case 1:
                            deleteTask(IDForDelete);
                            break;
                        case 2:
                            deleteSubtask(IDForDelete);
                            break;
                        case 3:
                            deleteEpic(IDForDelete);
                            break;
                        default:
                            System.out.println(Constants.NOT_COMMAND);
                            break;
                    }
                    break;
                case 7:
                    printSubtasksForEpic();
                    break;
                case 8:
                    System.out.println(Constants.HISTORY);
                    int i = 1;
                    for (AllTasks task : inMemoryHistoryManager.getHistory()) {
                        System.out.print(i + ". ");
                        System.out.println(task);
                        i++;
                    }
                    break;
                case 9:
                    return;
                default:
                    System.out.println(Constants.NOT_COMMAND);
                    break;
            }
        }
    }

    @Override
    public void createTask(Task task) {
        tasks.put(generateID, task);
        System.out.println(Constants.TASK_CREATED + generateID);
        ++generateID;
    }

    @Override
    public void createEpic(Epic epic) {
        epics.put(generateID, epic);
        System.out.println(Constants.TASK_CREATED + generateID);
        ++generateID;
    }

    @Override
    public void createSubtask(Subtask subtask, int epicID) {
        subtasks.put(generateID, subtask);
        for (Integer key : epics.keySet()) {
            if (key == epicID) {
                epics.get(epicID).getSubtaskID().add(generateID);
            }
        }
        System.out.println(Constants.TASK_CREATED + generateID);
        ++generateID;
    }

    @Override
    public void RemoveAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        System.out.println(Constants.LIST_IS_EMPTY);
    }

    @Override
    public void printAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println(Constants.TASK_LIST_IS_EMPTY);
            return;
        }
        System.out.println(Constants.TASKS);
        for (Integer k : tasks.keySet()) {
            System.out.print(k + ". ");
            System.out.println(tasks.get(k).toString());
        }
    }

    @Override
    public void printAllEpics() {
        if (epics.isEmpty()) {
            System.out.println(Constants.EPIC_LIST_IS_EMPTY);
            return;
        }
        System.out.println(Constants.EPICS);
        for (Integer k : epics.keySet()) {
            System.out.print(k + ". ");
            System.out.println(epics.get(k).toString());
        }
    }

    @Override
    public void printAllSubtasks() {
        if (subtasks.isEmpty()) {
            System.out.println(Constants.SUBTASK_LIST_IS_EMPTY);
            return;
        }
        System.out.println(Constants.SUBTASKS);
        for (Integer k : subtasks.keySet()) {
            System.out.print(k + ". ");
            System.out.println(subtasks.get(k).toString());
        }
    }

    @Override
    public void printSubtasksForEpic() {
        for (Epic epic : epics.values()) {
            System.out.println("Эпику " + epic.getName() + " принадлежат следующие подзадачи: ");
            for (Integer subtaskID : epic.getSubtaskID()) {
                for (Integer subtaskKey : subtasks.keySet()) {
                    if (Objects.equals(subtaskID, subtaskKey)) {
                        System.out.print(subtaskKey + ". ");
                        System.out.println(subtasks.get(subtaskKey).toString());
                    }
                }
            }
        }
    }

    @Override
    public void takeTask(int uniqueNumber) {
        if (!tasks.containsKey(uniqueNumber)) {
            System.out.println(Constants.NOT_FOUND);
            return;
        }
        for (Integer k : tasks.keySet()) {
            if (Objects.equals(k, uniqueNumber)) {
                System.out.println(tasks.get(k).toString());
                inMemoryHistoryManager.add(tasks.get(k));
            }
        }
    }

    @Override
    public void takeEpic(int uniqueNumber) {
        if (!epics.containsKey(uniqueNumber)) {
            System.out.println(Constants.NOT_FOUND);
        } else {
            for (Integer k : epics.keySet()) {
                if (Objects.equals(k, uniqueNumber)) {
                    System.out.println(epics.get(k).toString());
                    inMemoryHistoryManager.add(epics.get(k));
                }
            }
        }
    }

    @Override
    public void takeSubtask(int uniqueNumber) {
        if (!subtasks.containsKey(uniqueNumber)) {
            System.out.println(Constants.NOT_FOUND);
            return;
        }
        for (Integer k : subtasks.keySet()) {
            if (Objects.equals(k, uniqueNumber)) {
                System.out.println(subtasks.get(k).toString());
                inMemoryHistoryManager.add(subtasks.get(k));
            }
        }
    }

    @Override
    public void changeTask(int uniqueID, String newName, Status newStatus, String newDescription) {
        if (!tasks.containsKey(uniqueID)) {
            System.out.println(Constants.NOT_FOUND);
            return;
        }
        Task newTask = new Task(newName, newStatus, newDescription);
        tasks.put(uniqueID, newTask);
        System.out.println(Constants.TASK_CHANGED);
    }

    @Override
    public void changeSubtask(int uniqueID, String newName, Status newStatus, String newDescription, int newEpicID) {
        if (!subtasks.containsKey(uniqueID)) {
            System.out.println(Constants.NOT_FOUND);
            return;
        }
        Subtask newSubtask = new Subtask(newName, newStatus, newDescription, newEpicID);
        subtasks.put(uniqueID, newSubtask);
        for (Subtask sub : subtasks.values()) {
            for (Integer k : epics.keySet()) {
                if (Objects.equals(sub.getEpicID(), k)) {
                    changeEpic(epics.get(k));
                }
            }
        }
        System.out.println(Constants.SUBTASK_CHANGED);
    }

    @Override
    public void changeEpic(Epic epic) {
        int statusNew = 0;
        int done = 0;
        for (Integer k : subtasks.keySet()) {
            if (epic.getSubtaskID().contains(k)) {
                if (subtasks.get(k).getStatus().equals(Status.DONE)) {
                    ++done;
                } else if (subtasks.get(k).getStatus().equals(Status.NEW)) {
                    ++statusNew;
                }
            }
        }
        if ((statusNew == epic.getSubtaskID().size()) || (epic.getSubtaskID().isEmpty())) {
            epic.setStatus(Status.NEW);
        } else if (done == epic.getSubtaskID().size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager manager = (InMemoryTaskManager) o;
        return generateID == manager.generateID && Objects.equals(tasks, manager.tasks)
                && Objects.equals(subtasks, manager.subtasks) && Objects.equals(epics, manager.epics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tasks, subtasks, epics, generateID);
    }

    @Override
    public void deleteTask(int uniqueID) {
        tasks.remove(uniqueID);
        System.out.println(Constants.TASK_DELETED);
    }

    @Override
    public void deleteSubtask(int uniqueID) {
        subtasks.remove(uniqueID);
        System.out.println(Constants.SUBTASK_DELETED);
    }

    @Override
    public void deleteEpic(int uniqueID) {
        for (Integer k : epics.get(uniqueID).getSubtaskID()) {
            subtasks.remove(k);
        }
        epics.remove(uniqueID);
        System.out.println(Constants.EPIC_AND_SUBTASKS_DELETED);
    }

    public void printMenu() {
        System.out.println(Constants.CHOOSE_COMMAND);
        System.out.println(Constants.CREATE_TASK);
        System.out.println(Constants.PRINT_ALL_TASKS);
        System.out.println(Constants.REMOVE_ALL_TASKS);
        System.out.println(Constants.PRINT_TASK);
        System.out.println(Constants.UPDATE_TASK);
        System.out.println(Constants.REMOVE_TASK);
        System.out.println(Constants.PRINT_SUBTASKS_FOR_EPIC);
        System.out.println(Constants.TAKE_HISTORY);
        System.out.println(Constants.EXIT);
    }

    public void menuForDelete() {
        System.out.println(Constants.DELETE_TASK);
        System.out.println(Constants.DELETE_SUBTASK);
        System.out.println(Constants.DELETE_EPIC);
        System.out.println(Constants.WARNING);
    }

    public static boolean isNumeric(String command) {
        if (command == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(command);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}

