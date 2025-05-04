package TasksManagers;

import Tasks.*;
import History.*;
import Main.Constants;

import java.util.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.io.IOException;

public class InMemoryTaskManager implements TaskManager {
    Scanner scanner = new Scanner(System.in);
    public List<AllTasks> allTasksList = new ArrayList<>();
    public Map<Integer, Task> tasks = new HashMap<>();
    public Map<Integer, Subtask> subtasks = new HashMap<>();
    public Map<Integer, Epic> epics = new HashMap<>();
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    public NavigableMap<LocalDateTime, Duration> periods = new TreeMap<>(LocalDateTime::compareTo);
    public static int generateID = 1;
    public static Status newStatus;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy,HH:mm");
    Duration duration;
    String startTime;

    public InMemoryTaskManager(HistoryManager historyManager) {
    }

    public void runApp() throws NumberFormatException, IOException {
        while (true) {
            try {
                printMenu();
                int command = Integer.parseInt(scanner.nextLine());
                switch (command) {
                    case 1:
                        toCreate(scanner);
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
                        toGetTask(scanner);
                        break;
                    case 5:
                        toChangeTask(scanner);
                        break;
                    case 6:
                        toDeleteTask(scanner);
                        break;
                    case 7:
                        printSubtasksForEpic();
                        break;
                    case 8:
                        printHistory();
                        break;
                    case 9:
                        getPrioritizedTasks();
                        break;
                    case 10:
                        return;
                    default:
                        System.out.print(Constants.NOT_COMMAND + "\n");
                        break;
                }
            } catch (NumberFormatException exception) {
                System.out.print(Constants.PRINT_NUMBER + "\n");
                continue;
            } catch (IOException exception) {
                System.out.print(Constants.IO_EXCEPTION_FOR_SAVE + "\n");
                continue;
            }
        }
    }

    public boolean isReplayExists(LocalDateTime start, Duration duration) {
        boolean result = false;
        LocalDateTime end = start.plusMinutes(duration.toMinutes());
        Map.Entry<LocalDateTime, Duration> upperBound = periods.ceilingEntry(start);
        Map.Entry<LocalDateTime, Duration> lowerBound = periods.floorEntry(start);
        if (upperBound != null) {
            LocalDateTime upperBoundEnd = upperBound.getKey().plusMinutes(upperBound.getValue().toMinutes());
            if ((end.isAfter(upperBound.getKey())) && (end.isBefore(upperBoundEnd)) ||
                    ((start.isBefore(upperBoundEnd)) && (start.isAfter(upperBound.getKey())))) {
                result = true;
            }
            if (((start.isAfter(upperBound.getKey())) && (start.isBefore(upperBoundEnd)))
                    && ((end.isAfter(upperBound.getKey())) && (end.isBefore(upperBoundEnd)))) {
                result = true;
            }
        }
        if (lowerBound != null) {
            LocalDateTime lowerBoundEnd = lowerBound.getKey().plusMinutes(lowerBound.getValue().toMinutes());
            if ((end.isAfter(lowerBound.getKey())) && (end.isBefore(lowerBoundEnd)) ||
                    ((start.isBefore(lowerBoundEnd)) && (start.isAfter(lowerBound.getKey())))) {
                result = true;
            }
            if (((start.isAfter(lowerBound.getKey())) && (start.isBefore(lowerBoundEnd)))
                    && ((end.isAfter(lowerBound.getKey())) && (end.isBefore(lowerBoundEnd)))) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public void toCreate(Scanner scanner) throws NumberFormatException {
        try {
            System.out.print(Constants.PRINT_NAME + "\n");
            String name = scanner.nextLine();
            System.out.print(Constants.PRINT_DESCRIPTION + "\n");
            String description = scanner.nextLine();
            System.out.print(Constants.PRINT_TYPE_OF_TASK + "\n");
            int typeOfTask = Integer.parseInt(scanner.nextLine());
            switch (typeOfTask) {
                case 1:
                    getTimeParameters();
                    if (isReplayExists(LocalDateTime.parse(startTime, formatter), duration)) {
                        System.out.print(Constants.CROSSING_EXISTS + "\n");
                        return;
                    }
                    createTask(new Task(name, Status.NEW, description, generateID, TypeOfTask.TASK, duration, startTime));
                    break;
                case 2:
                    createEpic(new Epic(name, Status.NEW, description, new ArrayList<>(), generateID, TypeOfTask.EPIC,
                            Duration.ofMinutes(0), LocalDateTime.ofInstant(Instant.now(),
                            ZoneId.systemDefault()).format(formatter)));
                    break;
                case 3:
                    getTimeParameters();
                    if (isReplayExists(LocalDateTime.parse(startTime, formatter), duration)) {
                        System.out.print(Constants.CROSSING_EXISTS + "\n");
                        return;
                    }
                    System.out.print(Constants.CHOOSE_EPIC + "\n");
                    int epicID = Integer.parseInt(scanner.nextLine());
                    createSubtask(new Subtask(name, Status.NEW, description, epicID, generateID,
                            TypeOfTask.SUBTASK, duration, startTime), epicID);
                    break;
                default:
                    System.out.print(Constants.NOT_COMMAND + "\n");
                    break;
            }
        } catch (NumberFormatException exception) {
            System.out.print(Constants.PRINT_NUMBER + "\n");
            return;
        } catch (IOException exception) {
            System.out.print(Constants.IO_EXCEPTION_FOR_SAVE + "\n");
            return;
        }
    }

    public void getTimeParameters() {
        System.out.print(Constants.PRINT_DURATION + "\n");
        duration = Duration.ofMinutes(Long.parseLong(scanner.nextLine()));
        System.out.print(Constants.PRINT_START_TIME + "\n");
        startTime = scanner.nextLine();
    }

    @Override
    public void toGetTask(Scanner scanner) throws NumberFormatException {
        try {
            System.out.print(Constants.PRINT_ID + "\n");
            int taskID = Integer.parseInt(scanner.nextLine());
            System.out.print(Constants.PRINT_TYPE_OF_TASK + "\n");
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
                    System.out.print(Constants.NOT_COMMAND + "\n");
                    break;
            }
        } catch (NumberFormatException exception) {
            System.out.print(Constants.PRINT_NUMBER + "\n");
            return;
        } catch (IOException exception) {
            System.out.print(Constants.IO_EXCEPTION_FOR_SAVE + "\n");
            return;
        }
    }

    @Override
    public void toChangeTask(Scanner scanner) throws NumberFormatException {
        try {
            System.out.print(Constants.CHOOSE_TYPE_FOR_CHANGE + "\n");
            int typeForChange = Integer.parseInt(scanner.nextLine());
            System.out.print(Constants.PRINT_ID + "\n");
            int newID = Integer.parseInt(scanner.nextLine());
            System.out.println(Constants.PRINT_NAME);
            String newName = scanner.nextLine();
            System.out.println(Constants.PRINT_DESCRIPTION);
            String newDescription = scanner.nextLine();
            switch (typeForChange) {
                case 1:
                    Duration taskDuration = tasks.get(newID).getDuration();
                    String taskStartTime = tasks.get(newID).getStartTime().format(formatter);
                    getNewStatus(scanner);
                    changeTask(newID, newName, newStatus, newDescription, taskDuration, taskStartTime);
                    break;
                case 2:
                    Duration subtaskDuration = subtasks.get(newID).getDuration();
                    String subtaskStartTime = subtasks.get(newID).getStartTime().format(formatter);
                    getNewStatus(scanner);
                    System.out.println(Constants.CHOOSE_EPIC);
                    String newEpic = scanner.nextLine();
                    int newEpicID = Integer.parseInt(newEpic);
                    changeSubtask(newID, newName, newStatus, newDescription, newEpicID,
                            subtaskDuration, subtaskStartTime);
                    break;
                case 3:
                    Duration epicDuration = epics.get(newID).getDuration();
                    String epicStartTime = epics.get(newID).getStartTime().format(formatter);
                    ArrayList<AllTasks> newSubtaskID = epics.get(newID).getSubtasks();
                    if (newSubtaskID.isEmpty()) {
                        getNewStatus(scanner);
                        changeEpic(newName, newStatus, newDescription, newSubtaskID, newID, epicDuration, epicStartTime);
                    } else {
                        Status epicStatus = epics.get(newID).getStatus();
                        changeEpic(newName, epicStatus, newDescription, newSubtaskID, newID, epicDuration, epicStartTime);
                    }
                    break;
                default:
                    System.out.print(Constants.NOT_COMMAND + "\n");
                    break;
            }
        } catch (NumberFormatException exception) {
            System.out.print(Constants.PRINT_NUMBER + "\n");
            return;
        } catch (IOException exception) {
            System.out.print(Constants.IO_EXCEPTION_FOR_SAVE + "\n");
            return;
        }
    }

    public void getNewStatus(Scanner scanner) {
        System.out.println(Constants.PRINT_STATUS);
        String status = scanner.nextLine();
        if ((!status.equals("NEW")) && (!status.equals("DONE")) && (!status.equals("IN_PROGRESS"))) {
            System.out.print(Constants.NOT_FOUND + "\n");
            return;
        }
        newStatus = Status.valueOf(status);
    }

    @Override
    public void toDeleteTask(Scanner scanner) throws NumberFormatException {
        try {
            System.out.print(Constants.PRINT_ID + "\n");
            int IDForDelete = Integer.parseInt(scanner.nextLine());
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
                    System.out.print(Constants.NOT_COMMAND + "\n");
                    break;
            }
        } catch (NumberFormatException exception) {
            System.out.print(Constants.PRINT_NUMBER + "\n");
            return;
        } catch (IOException exception) {
            System.out.print(Constants.IO_EXCEPTION_FOR_SAVE + "\n");
            return;
        }
    }

    @Override
    public void createTask(Task task) throws IOException {
        tasks.put(generateID, task);
        allTasksList.add(task);
        periods.put(task.getStartTime(), task.getDuration());
        System.out.print(Constants.TASK_CREATED + generateID + "\n");
        ++generateID;
    }

    @Override
    public void createEpic(Epic epic) throws IOException {
        epics.put(generateID, epic);
        allTasksList.add(epic);
        System.out.print(Constants.TASK_CREATED + generateID + "\n");
        ++generateID;
    }

    @Override
    public void createSubtask(Subtask subtask, int epicID) throws IOException {
        if (!epics.containsKey(epicID)) {
            System.out.print(Constants.EPIC_IS_NOT_EXIST + "\n");
            return;
        }
        subtasks.put(generateID, subtask);
        allTasksList.add(subtask);
        periods.put(subtask.getStartTime(), subtask.getDuration());
        for (Integer key : epics.keySet()) {
            if (key.equals(epicID)) {
                epics.get(epicID).getSubtasks().add(subtasks.get(generateID));
            }
        }
        Epic epic = epics.get(subtask.getEpicID());
        changeEpic(epic.getName(), epic.getStatus(), epic.getDescription(), epic.getSubtasks(), epic.getId(),
                epic.getDuration(), epic.getStartTime().toString());
        System.out.println(Constants.TASK_CREATED + generateID);
        ++generateID;
    }

    @Override
    public void removeAllTasks() throws IOException {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        allTasksList.clear();
        inMemoryHistoryManager.clearHistory();
        periods.clear();
        generateID = 1;
        System.out.print(Constants.LIST_IS_EMPTY + "\n");
    }

    @Override
    public void printAllTasks() throws IOException {
        if (tasks.isEmpty()) {
            System.out.print(Constants.TASK_LIST_IS_EMPTY + "\n");
            return;
        }
        System.out.print(Constants.TASKS + "\n");
        for (Integer k : tasks.keySet()) {
            System.out.print(k + ". ");
            System.out.print(tasks.get(k).toString() + "\n");
            inMemoryHistoryManager.add(tasks.get(k));
        }
    }

    @Override
    public void printAllEpics() throws IOException {
        if (epics.isEmpty()) {
            System.out.print(Constants.EPIC_LIST_IS_EMPTY + "\n");
            return;
        }
        System.out.print(Constants.EPICS + "\n");
        for (Integer k : epics.keySet()) {
            System.out.print(k + ". ");
            System.out.print(epics.get(k).toString() + "\n");
            inMemoryHistoryManager.add(epics.get(k));
        }
    }

    @Override
    public void printAllSubtasks() throws IOException {
        if (subtasks.isEmpty()) {
            System.out.print(Constants.SUBTASK_LIST_IS_EMPTY + "\n");
            return;
        }
        System.out.print(Constants.SUBTASKS + "\n");
        for (Integer k : subtasks.keySet()) {
            System.out.print(k + ". ");
            System.out.print(subtasks.get(k).toString() + "\n");
            inMemoryHistoryManager.add(subtasks.get(k));
        }
    }

    @Override
    public void printSubtasksForEpic() {
        for (Epic epic : epics.values()) {
            System.out.print("Эпику " + epic.getName() + " принадлежат следующие подзадачи: \n");
            for (AllTasks subtask : epic.getSubtasks()) {
                System.out.print(subtask.toString() + "\n");
            }
        }
    }

    @Override
    public void getTaskByID(int uniqueNumber) throws IOException {
        if (tasks.isEmpty()) {
            System.out.print(Constants.TASK_LIST_IS_EMPTY + "\n");
            return;
        }
        if (!tasks.containsKey(uniqueNumber)) {
            System.out.print(Constants.ID_IS_NOT_EXIST + "\n");
            return;
        }
        for (Integer k : tasks.keySet()) {
            if (Objects.equals(k, uniqueNumber)) {
                System.out.print(tasks.get(k).toString() + "\n");
                inMemoryHistoryManager.add(tasks.get(k));
            }
        }
    }

    @Override
    public void getEpicByID(int uniqueNumber) throws IOException {
        if (epics.isEmpty()) {
            System.out.print(Constants.EPIC_LIST_IS_EMPTY + "\n");
            return;
        }
        if (!epics.containsKey(uniqueNumber)) {
            System.out.print(Constants.ID_IS_NOT_EXIST + "\n");
        } else {
            for (Integer k : epics.keySet()) {
                if (Objects.equals(k, uniqueNumber)) {
                    System.out.print(epics.get(k).toString() + "\n");
                    inMemoryHistoryManager.add(epics.get(k));
                }
            }
        }
    }

    @Override
    public void getSubtaskByID(int uniqueNumber) throws IOException {
        if (subtasks.isEmpty()) {
            System.out.print(Constants.SUBTASK_LIST_IS_EMPTY + "\n");
            return;
        }
        if (!subtasks.containsKey(uniqueNumber)) {
            System.out.print(Constants.ID_IS_NOT_EXIST + "\n");
            return;
        }
        for (Integer k : subtasks.keySet()) {
            if (Objects.equals(k, uniqueNumber)) {
                System.out.print(subtasks.get(k).toString() + "\n");
                inMemoryHistoryManager.add(subtasks.get(k));
            }
        }
    }

    @Override
    public void changeTask(int uniqueID, String newName, Status newStatus, String newDescription,
                           Duration duration, String startTime) throws IOException {
        if (tasks.isEmpty()) {
            System.out.print(Constants.TASK_LIST_IS_EMPTY + "\n");
            return;
        }
        if (!tasks.containsKey(uniqueID)) {
            System.out.print(Constants.ID_FOR_CHANGE_TASK_IS_NOT_EXIST + "\n");
            return;
        }
        Task newTask = new Task(newName, newStatus, newDescription, uniqueID, TypeOfTask.TASK, duration, startTime);
        allTasksList.removeIf(task -> task.getId() == uniqueID);
        allTasksList.add(newTask);
        tasks.put(uniqueID, newTask);
        System.out.print(Constants.TASK_CHANGED + "\n");
    }

    @Override
    public void changeSubtask(int uniqueID, String newName, Status newStatus, String newDescription, int newEpicID,
                              Duration duration, String startTime)
            throws IOException {
        if (subtasks.isEmpty()) {
            System.out.print(Constants.SUBTASK_LIST_IS_EMPTY + "\n");
            return;
        }
        if (!subtasks.containsKey(uniqueID)) {
            System.out.print(Constants.ID_FOR_CHANGE_SUBTASK_IS_NOT_EXIST + "\n");
            return;
        }
        if (!epics.containsKey(newEpicID)) {
            System.out.print(Constants.EPIC_IS_NOT_EXIST + "\n");
            return;
        }
        int index = 0;
        for (Epic epic : epics.values()) {
            if (epic.getSubtasks().contains(subtasks.get(uniqueID))) {
                index = getSubtaskIndex(epic, uniqueID);
                epic.getSubtasks().remove(index);
            }
        }
        Subtask newSubtask = new Subtask(newName, newStatus, newDescription, newEpicID, uniqueID,
                TypeOfTask.SUBTASK, duration, startTime);
        allTasksList.removeIf(task -> task.getId() == uniqueID);
        allTasksList.add(newSubtask);
        subtasks.put(uniqueID, newSubtask);
        for (Integer k : epics.keySet()) {
            if (k == newEpicID) {
                epics.get(k).getSubtasks().add(newSubtask);
            }
        }
        System.out.print(Constants.SUBTASK_CHANGED + "\n");
    }

    @Override
    public int getSubtaskIndex(Epic epic, int subtaskID) {
        int index = 0;
        for (AllTasks subtask : epic.getSubtasks()) {
            if (subtask.getId() == subtaskID) {
                break;
            }
            index++;
        }
        return index;
    }

    @Override
    public void changeEpic(String newName, Status newStatus, String newDescription, ArrayList<AllTasks> newSubtaskID,
                           int uniqueID, Duration duration, String startTime) throws IOException {
        if (epics.isEmpty()) {
            System.out.print(Constants.EPIC_LIST_IS_EMPTY + "\n");
            return;
        }
        if (!epics.containsKey(uniqueID)) {
            System.out.print(Constants.ID_FOR_CHANGE_EPIC_IS_NOT_EXIST + "\n");
            return;
        }
        Epic newEpic = new Epic(newName, newStatus, newDescription, newSubtaskID, uniqueID, TypeOfTask.EPIC,
                duration, startTime);
        allTasksList.removeIf(task -> task.getId() == uniqueID);
        allTasksList.add(newEpic);
        epics.put(uniqueID, newEpic);
        System.out.print(Constants.EPIC_CHANGED + "\n");
    }

    @Override
    public void changeEpicStatus(Epic epic) throws IOException {
        int statusNew = 0;
        int done = 0;
        for (AllTasks subtask: epic.getSubtasks()) {
            if (subtask.getStatus().equals(Status.DONE)) {
                ++done;
            } else if (subtask.getStatus().equals(Status.NEW)) {
                ++statusNew;
            }
        }
        if ((statusNew == epic.getSubtasks().size()) || (epic.getSubtasks().isEmpty())) {
            epic.setStatus(Status.NEW);
        } else if (done == epic.getSubtasks().size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void deleteTaskByID(int taskID) throws IOException {
        if (tasks.isEmpty()) {
            System.out.print(Constants.TASK_LIST_IS_EMPTY + "\n");
            return;
        }
        if (!tasks.containsKey(taskID)) {
            System.out.print(Constants.NOT_FOUND + "\n");
            return;
        }
        periods.remove(tasks.get(taskID).getStartTime());
        tasks.remove(taskID);
        allTasksList.removeIf(task -> task.getId() == taskID);
        inMemoryHistoryManager.remove(taskID);
        System.out.print(Constants.TASK_DELETED + "\n");
    }

    @Override
    public void deleteSubtaskByID(int subtaskID) throws IOException {
        if (subtasks.isEmpty()) {
            System.out.print(Constants.SUBTASK_LIST_IS_EMPTY + "\n");
            return;
        }
        if (!subtasks.containsKey(subtaskID)) {
            System.out.print(Constants.NOT_FOUND + "\n");
            return;
        }
        Epic epic = epics.get(subtasks.get(subtaskID).getEpicID());
        epic.getSubtasks().remove(getSubtaskIndex(epic, subtaskID));
        periods.remove(subtasks.get(subtaskID).getStartTime());
        subtasks.remove(subtaskID);
        allTasksList.removeIf(task -> task.getId() == subtaskID);
        inMemoryHistoryManager.remove(subtaskID);
        changeEpic(epic.getName(), epic.getStatus(), epic.getDescription(), epic.getSubtasks(), epic.getId(),
                epic.getDuration(), epic.getStartTime().toString());
        System.out.print(Constants.SUBTASK_DELETED + "\n");
    }

    @Override
    public void deleteEpicByID(int epicID) throws IOException {
        if (epics.isEmpty()) {
            System.out.print(Constants.EPIC_LIST_IS_EMPTY + "\n");
            return;
        }
        if (!epics.containsKey(epicID)) {
            System.out.print(Constants.NOT_FOUND + "\n");
            return;
        }
        for (AllTasks subtask : epics.get(epicID).getSubtasks()) {
            periods.remove(subtask.getStartTime());
            allTasksList.removeIf(task -> epics.get(epicID).getSubtasks().contains(subtask));
            inMemoryHistoryManager.remove(subtask.getId());
            subtasks.remove(subtask.getId());
        }
        allTasksList.removeIf(task -> task.getId() == epicID);
        epics.remove(epicID);
        inMemoryHistoryManager.remove(epicID);
        System.out.print(Constants.EPIC_AND_SUBTASKS_DELETED + "\n");
    }

    @Override
    public void printHistory() {
        if (inMemoryHistoryManager.linkedList.isEmpty()) {
            System.out.print(Constants.HISTORY_IS_EMPTY + "\n");
            return;
        }
        System.out.print(Constants.HISTORY + "\n");
        System.out.print(inMemoryHistoryManager.getHistory());
    }

    @Override
    public void getPrioritizedTasks() {
        List <AllTasks> sortedTaskList = new ArrayList<>(allTasksList);
        sortedTaskList.sort(new AllTasksComparator());
        int i = 1;
        for (AllTasks task : sortedTaskList) {
            System.out.print(i + ". " + task.toString() + "\n");
            i++;
        }
        i = 1;
    }

    @Override
    public void printMenu() {
        System.out.print(Constants.CHOOSE_COMMAND + "\n");
        System.out.print(Constants.CREATE_TASK + "\n");
        System.out.print(Constants.PRINT_ALL_TASKS + "\n");
        System.out.print(Constants.REMOVE_ALL_TASKS + "\n");
        System.out.print(Constants.PRINT_TASK + "\n");
        System.out.print(Constants.UPDATE_TASK + "\n");
        System.out.print(Constants.REMOVE_TASK + "\n");
        System.out.print(Constants.PRINT_SUBTASKS_FOR_EPIC + "\n");
        System.out.print(Constants.TAKE_HISTORY + "\n");
        System.out.print(Constants.PRINT_TASKS_WITH_TIME + "\n");
        System.out.print(Constants.EXIT + "\n");
    }

    @Override
    public void menuForDelete() {
        System.out.print(Constants.DELETE_TASK + "\n");
        System.out.print(Constants.DELETE_SUBTASK + "\n");
        System.out.print(Constants.DELETE_EPIC + "\n");
        System.out.print(Constants.WARNING + "\n");
    }

}

