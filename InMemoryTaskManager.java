import java.io.IOException;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    static Scanner scanner = new Scanner(System.in);
    List<AllTasks> allTasksList = new ArrayList<>();
    Map<Integer, Task> tasks = new HashMap<>();
    Map<Integer, Subtask> subtasks = new HashMap<>();
    Map<Integer, Epic> epics = new HashMap<>();
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    public int generateId = 1;

    public InMemoryTaskManager(HistoryManager historyManager) {
    }

    public void runApp() throws NumberFormatException, IOException {
        while (true) {
            try {
                printMenu();
                String comm = scanner.nextLine();
                int command = Integer.parseInt(comm);
                switch (command) {
                    case 1:
                        toCreate();
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
                        toGetTask();
                        break;
                    case 5:
                        toChangeTask();
                        break;
                    case 6:
                        toDeleteTask();
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
            } catch (NumberFormatException exception) {
                System.out.println(Constants.PRINT_NUMBER);
                continue;
            } catch (IOException exception) {
                System.out.println(Constants.IO_EXCEPTION_FOR_SAVE);
                return;
            }
        }
    }

    @Override
    public void toCreate() throws NumberFormatException {
        try {
            System.out.println(Constants.PRINT_NAME);
            String name = scanner.nextLine();
            System.out.println(Constants.PRINT_DESCRIPTION);
            String description = scanner.nextLine();
            System.out.println(Constants.PRINT_TYPE_OF_TASK);
            int typeOfTask = Integer.parseInt(scanner.nextLine());
            switch (typeOfTask) {
                case 1:
                    createTask(new Task(name, Status.NEW, description, generateId, TypeOfTask.TASK));
                    break;
                case 2:
                    createEpic(new Epic(name, Status.NEW, description, new ArrayList<>(), generateId,
                            TypeOfTask.EPIC));
                    break;
                case 3:
                    System.out.println(Constants.CHOOSE_EPIC);
                    String numberOfEpic = scanner.nextLine();
                    int epicID = Integer.parseInt(numberOfEpic);
                    if (!epics.containsKey(epicID)) {
                        System.out.println(Constants.EPIC_IS_NOT_EXIST);
                        break;
                    }
                    createSubtask(new Subtask(name, Status.NEW, description, epicID, generateId,
                            TypeOfTask.SUBTASK), epicID);
                    break;
                default:
                    System.out.println(Constants.NOT_COMMAND);
                    break;
            }
        } catch (NumberFormatException exception) {
            System.out.println(Constants.PRINT_NUMBER);
            return;
        } catch (IOException exception) {
            System.out.println(Constants.IO_EXCEPTION_FOR_SAVE);
            return;
        }
    }

    @Override
    public void toGetTask() throws NumberFormatException {
            try {
                System.out.println(Constants.PRINT_ID);
                String tasksID = scanner.nextLine();
                int taskID = Integer.parseInt(tasksID);
                if ((!tasks.containsKey(taskID)) && (!epics.containsKey(taskID)) && (!subtasks.containsKey(taskID))) {
                    System.out.println(Constants.ID_IS_NOT_EXIST);
                    return;
                }
                System.out.println(Constants.PRINT_TYPE_OF_TASK);
                String typeOfTasks = scanner.nextLine();
                int typeOf = Integer.parseInt(typeOfTasks);
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
            } catch (NumberFormatException  exception) {
                System.out.println(Constants.PRINT_NUMBER);
                return;
            } catch (IOException exception) {
                System.out.println(Constants.IO_EXCEPTION_FOR_SAVE);
                return;
            }
    }

    @Override
    public void toChangeTask() throws NumberFormatException {
        try {
            System.out.println(Constants.CHOOSE_TYPE_FOR_CHANGE);
            int typeForChange = Integer.parseInt(scanner.nextLine());
            if (!((typeForChange == 1)) && (!(typeForChange == 2))) {
                System.out.println(Constants.NOT_COMMAND);
                return;
            }
            System.out.println(Constants.PRINT_ID);
            int newID = Integer.parseInt(scanner.nextLine());
            if ((!tasks.containsKey(newID)) && (!epics.containsKey(newID)) && (!subtasks.containsKey(newID))) {
                System.out.println(Constants.ID_IS_NOT_EXIST);
                return;
            }
            System.out.println(Constants.PRINT_NAME);
            String newName = scanner.nextLine();
            System.out.println(Constants.PRINT_DESCRIPTION);
            String newDescription = scanner.nextLine();
            System.out.println(Constants.PRINT_STATUS);
            String status = scanner.nextLine();
            if ((!status.equals("NEW")) && (!status.equals("DONE")) && (!status.equals("IN_PROGRESS"))) {
                System.out.println(Constants.NOT_FOUND);
                return;
            }
            Status newStatus = Status.valueOf(status);
            switch (typeForChange) {
                case 1:
                    changeTask(newID, newName, newStatus, newDescription);
                    break;
                case 2:
                    System.out.println(Constants.CHOOSE_EPIC);
                    String newEpic = scanner.nextLine();
                    int newEpicID = Integer.parseInt(newEpic);
                    changeSubtask(newID, newName, newStatus, newDescription, newEpicID);
                    break;
                default:
                    System.out.println(Constants.NOT_COMMAND);
                    break;
            }
        } catch (NumberFormatException exception) {
            System.out.println(Constants.PRINT_NUMBER);
            return;
        } catch (IOException exception) {
            System.out.println(Constants.IO_EXCEPTION_FOR_SAVE);
            return;
        }
    }

    @Override
    public void toDeleteTask() throws NumberFormatException {
        try {
            System.out.println(Constants.PRINT_ID);
            int IDForDelete = Integer.parseInt(scanner.nextLine());
            if ((!tasks.containsKey(IDForDelete)) && (!epics.containsKey(IDForDelete)) &&
                    (!subtasks.containsKey(IDForDelete))) {
                System.out.println(Constants.ID_IS_NOT_EXIST);
                return;
            }
            menuForDelete();
            int pointOfMenu = Integer.parseInt(scanner.nextLine());
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
        } catch (NumberFormatException  exception) {
            System.out.println(Constants.PRINT_NUMBER);
            return;
        } catch (IOException exception) {
            System.out.println(Constants.IO_EXCEPTION_FOR_SAVE);
            return;
        }
    }

    @Override
    public void createTask(Task task) throws IOException {
        tasks.put(generateId, task);
        allTasksList.add(task);
        System.out.println(Constants.TASK_CREATED + generateId);
        ++generateId;
    }

    @Override
    public void createEpic(Epic epic) throws IOException {
        epics.put(generateId, epic);
        allTasksList.add(epic);
        System.out.println(Constants.TASK_CREATED + generateId);
        ++generateId;
    }

    @Override
    public void createSubtask(Subtask subtask, int epicID) throws IOException {
        subtasks.put(generateId, subtask);
        allTasksList.add(subtask);
        for (Integer key : epics.keySet()) {
            if (key.equals(epicID)) {
                epics.get(epicID).getSubtaskID().add(generateId);
            }
        }
        System.out.println(Constants.TASK_CREATED + generateId);
        ++generateId;
    }

    @Override
    public void removeAllTasks() throws IOException {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        allTasksList.clear();
        inMemoryHistoryManager.clearHistory();
        generateId = 1;
        System.out.println(Constants.LIST_IS_EMPTY);
    }

    @Override
    public void printAllTasks() throws IOException {
        if (tasks.isEmpty()) {
            System.out.println(Constants.TASK_LIST_IS_EMPTY);
            return;
        }
        System.out.println(Constants.TASKS);
        for (Integer k : tasks.keySet()) {
            System.out.print(k + ". ");
            System.out.println(tasks.get(k).toString());
            inMemoryHistoryManager.add(tasks.get(k));
        }
    }

    @Override
    public void printAllEpics() throws IOException {
        if (epics.isEmpty()) {
            System.out.println(Constants.EPIC_LIST_IS_EMPTY);
            return;
        }
        System.out.println(Constants.EPICS);
        for (Integer k : epics.keySet()) {
            System.out.print(k + ". ");
            System.out.println(epics.get(k).toString());
            inMemoryHistoryManager.add(epics.get(k));
        }
    }

    @Override
    public void printAllSubtasks() throws IOException {
        if (subtasks.isEmpty()) {
            System.out.println(Constants.SUBTASK_LIST_IS_EMPTY);
            return;
        }
        System.out.println(Constants.SUBTASKS);
        for (Integer k : subtasks.keySet()) {
            System.out.print(k + ". ");
            System.out.println(subtasks.get(k).toString());
            inMemoryHistoryManager.add(subtasks.get(k));
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
    public void getTaskByID(int uniqueNumber) throws IOException {
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
    public void getEpicByID(int uniqueNumber) throws IOException {
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
    public void getSubtaskByID(int uniqueNumber) throws IOException {
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
    public void changeTask(int uniqueID, String newName, Status newStatus, String newDescription) throws IOException {
        if (!tasks.containsKey(uniqueID)) {
            System.out.println(Constants.NOT_FOUND);
            return;
        }
        Task newTask = new Task(newName, newStatus, newDescription, uniqueID,TypeOfTask.TASK);
        allTasksList.removeIf(task -> task.getId() == uniqueID);
        allTasksList.add(newTask);
        tasks.put(uniqueID, newTask);
        System.out.println(Constants.TASK_CHANGED);
    }

    @Override
    public void changeSubtask(int uniqueID, String newName, Status newStatus, String newDescription, int newEpicID)
            throws IOException {
        if (!subtasks.containsKey(uniqueID)) {
            System.out.println(Constants.NOT_FOUND);
            return;
        }
        Subtask newSubtask = new Subtask(newName, newStatus, newDescription, newEpicID, uniqueID,
                TypeOfTask.SUBTASK);
        allTasksList.removeIf(task -> task.getId() == uniqueID);
        allTasksList.add(newSubtask);
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
    public void changeEpic(Epic epic) throws IOException {
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
    public void deleteTaskByID(int taskID) throws IOException {
        if (!tasks.containsKey(taskID)) {
            System.out.println(Constants.NOT_FOUND);
            return;
        }
        tasks.remove(taskID);
        allTasksList.removeIf(task -> task.getId() == taskID);
        inMemoryHistoryManager.remove(taskID);
        System.out.println(Constants.TASK_DELETED);
    }

    @Override
    public void deleteSubtaskByID(int subtaskID) throws IOException {
        if (!subtasks.containsKey(subtaskID)) {
            System.out.println(Constants.NOT_FOUND);
            return;
        }
        subtasks.remove(subtaskID);
        allTasksList.removeIf(task -> task.getId() == subtaskID);
        inMemoryHistoryManager.remove(subtaskID);
        System.out.println(Constants.SUBTASK_DELETED);
    }

    @Override
    public void deleteEpicByID(int epicID) throws IOException {
        if (!epics.containsKey(epicID)) {
            System.out.println(Constants.NOT_FOUND);
            return;
        }
        for (Integer k : epics.get(epicID).getSubtaskID()) {
            allTasksList.removeIf(task -> epics.get(epicID).getSubtaskID().contains(task.getId()));
            subtasks.remove(k);
            inMemoryHistoryManager.remove(k);
        }
        allTasksList.removeIf(task -> task.getId() == epicID);
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
        String historyLine = history.toString();
        String[] historyArray = historyLine.split(",");
        for (int i = 0; i < historyArray.length; i++) {
            System.out.println((i+ 1) + ". " + historyArray[i]);
        }
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

}

