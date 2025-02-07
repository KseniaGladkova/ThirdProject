import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    static Scanner scanner = new Scanner(System.in);
    Map<Integer, Task> tasks = new HashMap<>();
    Map<Integer, Subtask> subtasks = new HashMap<>();
    Map<Integer, Epic> epics = new HashMap<>();
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    public int generateId = 1;

    public InMemoryTaskManager(HistoryManager historyManager) {
    }

    public void runApp() {
        while (true) {
            printMenu();
            String comm = scanner.nextLine();
            if (!isNumeric(comm)) {
                System.out.println(Constants.PRINT_NUMBER);
                continue;
            }
            int command = Integer.parseInt(comm);
            switch (command) {
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
                            Task task = new Task(name, Status.NEW, description, generateId);
                            createTask(task);
                            break;
                        case 2:
                            Epic epic = new Epic(name, Status.NEW, description, new ArrayList<>(), generateId);
                            createEpic(epic);
                            break;
                        case 3:
                            System.out.println(Constants.CHOOSE_EPIC);
                            String numberOfEpic = scanner.nextLine();
                            if (!isNumeric(numberOfEpic)) {
                                System.out.println(Constants.PRINT_NUMBER);
                                continue;
                            }
                            int epicID = Integer.parseInt(numberOfEpic);
                            if (!epics.containsKey(epicID)) {
                                System.out.println(Constants.EPIC_IS_NOT_EXIST);
                                continue;
                            }
                            Subtask subtask = new Subtask(name, Status.NEW, description, epicID, generateId);
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
                    removeAllTasks();
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
                    String tasksID = scanner.nextLine();
                    if (!isNumeric(tasksID)) {
                        System.out.println(Constants.PRINT_NUMBER);
                        continue;
                    }
                    int taskID = Integer.parseInt(tasksID);
                    switch (typeOf) {
                        case 1:
                            getTaskByID(taskID);
                            break;
                        case 2:
                            getEpicByID(taskID);
                            break;
                        case 3:
                            getSubtaskByID(taskID);
                            break;
                        default:
                            System.out.println(Constants.NOT_COMMAND);
                            break;
                    }
                    break;
                case 5:
                    System.out.println(Constants.CHOOSE_TYPE_FOR_CHANGE);
                    String variant = scanner.nextLine();
                    if (!isNumeric(variant)) {
                        System.out.println(Constants.PRINT_NUMBER);
                        continue;
                    }
                    int typeForChange = Integer.parseInt(variant);
                    System.out.println(Constants.PRINT_ID);
                    String changeID = scanner.nextLine();
                    if (!isNumeric(changeID)) {
                        System.out.println(Constants.PRINT_NUMBER);
                        continue;
                    }
                    int newID = Integer.parseInt(changeID);
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
                    switch (typeForChange) {
                        case 1:
                            changeTask(newID, newName, newStatus, newDescription);
                            break;
                        case 2:
                            System.out.println(Constants.CHOOSE_EPIC);
                            String newEpic = scanner.nextLine();
                            if (!isNumeric(newEpic)) {
                                System.out.println(Constants.PRINT_NUMBER);
                                continue;
                            }
                            int newEpicID = Integer.parseInt(newEpic);
                            changeSubtask(newID, newName, newStatus, newDescription, newEpicID);
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
                            deleteTaskByID(IDForDelete);
                            break;
                        case 2:
                            deleteSubtaskByID(IDForDelete);
                            break;
                        case 3:
                            deleteEpicByID(IDForDelete);
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
                    printHistory();
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
        tasks.put(generateId, task);
        System.out.println(Constants.TASK_CREATED + generateId);
        ++generateId;
    }

    @Override
    public void createEpic(Epic epic) {
        epics.put(generateId, epic);
        System.out.println(Constants.TASK_CREATED + generateId);
        ++generateId;
    }

    @Override
    public void createSubtask(Subtask subtask, int epicID) {
        subtasks.put(generateId, subtask);
        for (Integer key : epics.keySet()) {
            if (key.equals(epicID)) {
                epics.get(epicID).getSubtaskID().add(generateId);
            }
        }
        System.out.println(Constants.TASK_CREATED + generateId);
        ++generateId;
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        inMemoryHistoryManager.clearHistory();
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
            for (Integer subtaskNumber : epic.getSubtaskID()) {
                for (Integer key : subtasks.keySet()) {
                    if (Objects.equals(key, subtaskNumber)) {
                        System.out.println(subtasks.get(subtaskNumber).toString());
                    }
                }
            }
        }
    }

    @Override
    public void getTaskByID(int uniqueNumber) {
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
    public void getEpicByID(int uniqueNumber) {
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
    public void getSubtaskByID(int uniqueNumber) {
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
        Task newTask = new Task(newName, newStatus, newDescription, uniqueID);
        tasks.put(uniqueID, newTask);
        System.out.println(Constants.TASK_CHANGED);
    }

    @Override
    public void changeSubtask(int uniqueID, String newName, Status newStatus, String newDescription, int newEpicID) {
        if (!subtasks.containsKey(uniqueID)) {
            System.out.println(Constants.NOT_FOUND);
            return;
        }
        Subtask newSubtask = new Subtask(newName, newStatus, newDescription, newEpicID, uniqueID);
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
        return generateId == manager.generateId && Objects.equals(tasks, manager.tasks)
                && Objects.equals(subtasks, manager.subtasks) && Objects.equals(epics, manager.epics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tasks, subtasks, epics, generateId);
    }

    @Override
    public void deleteTaskByID(int taskID) {
        tasks.remove(taskID);
        inMemoryHistoryManager.remove(taskID);
        System.out.println(Constants.TASK_DELETED);
    }

    @Override
    public void deleteSubtaskByID(int subtaskID) {
        subtasks.remove(subtaskID);
        inMemoryHistoryManager.remove(subtaskID);
        System.out.println(Constants.SUBTASK_DELETED);
    }

    @Override
    public void deleteEpicByID(int epicID) {
        for (Integer k : epics.get(epicID).getSubtaskID()) {
            subtasks.remove(k);
            inMemoryHistoryManager.remove(k);
        }
        epics.remove(epicID);
        inMemoryHistoryManager.remove(epicID);
        System.out.println(Constants.EPIC_AND_SUBTASKS_DELETED);
    }

    @Override
    public void printHistory() {
        CustomLinkedList<AllTasks> history = inMemoryHistoryManager.getHistory();
        if (history.size == 0) {
            System.out.println(Constants.HISTORY_IS_EMPTY);
            return;
        }
        System.out.println(Constants.HISTORY);
        System.out.println(history);
    }

    @Override
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

    @Override
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

