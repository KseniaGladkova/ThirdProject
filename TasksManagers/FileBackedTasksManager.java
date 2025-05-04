package TasksManagers;

import Tasks.*;
import History.*;
import Main.Constants;
import java.io.*;
import java.time.Duration;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    static String historyLineFromFile = "";
    private String path = "";

    public FileBackedTasksManager(HistoryManager historyManager, String filePath) {
        super(historyManager);
        this.path = filePath;
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
        if (!file.exists() || file.length() == 0) {
            return new FileBackedTasksManager(new InMemoryHistoryManager(), file.getName());
        } else {
            InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
            FileBackedTasksManager manager = new FileBackedTasksManager(inMemoryHistoryManager,
                    file.getName());
            List<String[]> allTasksFromFile = getArrayOfTasks(readFile(file.getName()));
            addTasksToMap(manager, allTasksFromFile);
            createHistory(manager);
            manager.save();
            return manager;
        }
    }

    public static List<String> readFile(String fileName) throws IOException {
        List<String> allLinesFromFile = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/" + fileName))) {
            while (br.ready()) {
                allLinesFromFile.add(br.readLine());
            }
            allLinesFromFile.removeFirst();
            if (allLinesFromFile.getLast().startsWith("h;")) {
                historyLineFromFile = allLinesFromFile.getLast();
                allLinesFromFile.removeLast();
            }
            return allLinesFromFile;
        } catch (IOException exception) {
            throw new IOException();
        }
    }

    public static List<String[]> getArrayOfTasks(List<String> allLinesFromFile) {
        List<String[]> allTasksFromFile = new ArrayList<>();
        for (String task : allLinesFromFile) {
            allTasksFromFile.add(task.split(";"));
        }
        return allTasksFromFile;
    }

    public static void addTasksToMap(FileBackedTasksManager manager, List<String[]> allTasksFromFile) {
        List<Integer> listForID = new ArrayList<>();
        for (String[] array : allTasksFromFile) {
            int id = Integer.parseInt(array[0]);
            listForID.add(id);
            TypeOfTask type = TypeOfTask.valueOf(array[1]);
            Status status = Status.valueOf(array[3]);
            Duration duration = Duration.ofMinutes(Long.parseLong(array[5]));
            switch (type) {
                case TASK:
                    Task task = new Task(array[2], status, array[4], id, type, duration, array[6]);
                    manager.tasks.put(id, task);
                    manager.allTasksList.add(task);
                    break;
                case SUBTASK:
                    Subtask subtask = new Subtask(array[2], status, array[4], Integer.parseInt(array[7]), id, type,
                            duration, array[6]);
                    manager.subtasks.put(id, subtask);
                    manager.allTasksList.add(subtask);
                    break;
                case EPIC:
                    Epic epic = new Epic(array[2], status, array[4], new ArrayList<>(), id, type, duration, array[6]);
                    manager.epics.put(id, epic);
                    manager.allTasksList.add(epic);
                    break;
            }
        }
        Collections.sort(listForID);
        InMemoryTaskManager.generateID = listForID.getLast() + 1;
        addSubtaskIDForEpics(manager);
    }

    public static void addSubtaskIDForEpics(FileBackedTasksManager manager) {
        if (manager.epics.isEmpty()) {
            return;
        }
        for (Map.Entry<Integer, Subtask> subtask: manager.subtasks.entrySet()) {
            for (Epic epic: manager.epics.values()) {
                if (epic.getId() == subtask.getValue().getEpicID()) {
                    epic.getSubtasks().add(subtask.getValue());
                }
            }
        }
    }

    public static void createHistory(FileBackedTasksManager manager) {
        String[] numbers = historyLineFromFile.split(";");
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] == null) {
                continue;
            }
            int id = Integer.parseInt(numbers[i]);
            if (manager.tasks.containsKey(id)) {
                manager.inMemoryHistoryManager.add(manager.tasks.get(id));
            } else if (manager.epics.containsKey(id)) {
                manager.inMemoryHistoryManager.add(manager.epics.get(id));
            } else if (manager.subtasks.containsKey(id)) {
                manager.inMemoryHistoryManager.add(manager.subtasks.get(id));
            }
        }
    }

    public void save() throws IOException {
        allTasksList.sort((task1, task2) -> task1.getId() - task2.getId());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("resources/" + path, false))) {
            if (allTasksList.isEmpty()) {
                return;
            }
            bw.write("id;type;name;status;description;duration;startTime;epicID" + "\n");
            for (AllTasks task : allTasksList) {
                bw.write(task.toStringForFiles() + "\n");
            }
            bw.write(inMemoryHistoryManager.toStringForFiles());
        } catch (IOException exception) {
            System.out.println(Constants.IO_EXCEPTION_FOR_SAVE);
            return;
        }
    }

    @Override
    public void createTask(Task task) throws IOException {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) throws IOException {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask, int epicID) throws IOException {
        super.createSubtask(subtask, epicID);
        save();
    }

    @Override
    public void printAllTasks() throws IOException {
        super.printAllTasks();
        save();
    }

    @Override
    public void printAllEpics() throws IOException {
        super.printAllEpics();
        save();
    }

    @Override
    public void printAllSubtasks() throws IOException {
        super.printAllSubtasks();
        save();
    }

    @Override
    public void removeAllTasks() throws IOException {
        super.removeAllTasks();
        save();
    }

    @Override
    public void getTaskByID(int uniqueNumber) throws IOException {
        super.getTaskByID(uniqueNumber);
        save();
    }

    @Override
    public void getEpicByID(int uniqueNumber) throws IOException {
        super.getEpicByID(uniqueNumber);
        save();
    }

    @Override
    public void getSubtaskByID(int uniqueNumber) throws IOException {
        super.getSubtaskByID(uniqueNumber);
        save();
    }

    @Override
    public void changeTask(int uniqueID, String newName, Status newStatus, String newDescription,
                           Duration duration, String startTime) throws IOException {
        super.changeTask(uniqueID, newName, newStatus, newDescription, duration, startTime);
        save();
    }

    @Override
    public void changeSubtask(int uniqueID, String newName, Status newStatus, String newDescription, int newEpicID,
                              Duration duration, String startTime)
            throws IOException {
        super.changeSubtask(uniqueID, newName, newStatus, newDescription, newEpicID, duration, startTime);
        save();
    }

    @Override
    public void changeEpic(String newName, Status newStatus, String newDescription, ArrayList<AllTasks> newSubtasks,
                           int uniqueID, Duration duration, String startTime) throws IOException {
        super.changeEpic(newName, newStatus, newDescription, newSubtasks, uniqueID, duration, startTime);
        save();
    }

    @Override
    public void changeEpicStatus(Epic epic) throws IOException {
        super.changeEpicStatus(epic);
        save();
    }

    @Override
    public void deleteTaskByID(int taskID) throws IOException {
        super.deleteTaskByID(taskID);
        save();
    }

    @Override
    public void deleteSubtaskByID(int subtaskID) throws IOException {
        super.deleteSubtaskByID(subtaskID);
        save();
    }

    @Override
    public void deleteEpicByID(int epicID) throws IOException {
        super.deleteEpicByID(epicID);
        save();
    }
}
