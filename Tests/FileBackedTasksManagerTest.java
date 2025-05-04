package Tests;
import TasksManagers.*;
import History.*;
import Tasks.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy,HH:mm");
    private ByteArrayOutputStream output;
    FileBackedTasksManager taskManager;
    InMemoryHistoryManager historyManager;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    public void createObjects() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new FileBackedTasksManager(historyManager, "test.csv");
    }

    public void createOneTask() throws IOException {
        task = new Task("name", Status.NEW, "description",
                InMemoryTaskManager.generateID, TypeOfTask.TASK, Duration.ofMinutes(30), "01.01.2025,13:00");
        taskManager.createTask(task);
    }

    public void createOneEpic() throws IOException {
        epic = new Epic("name", Status.NEW, "description", new ArrayList<AllTasks>(),
                InMemoryTaskManager.generateID, TypeOfTask.EPIC, Duration.ofMinutes(0), "01.01.2025,14:00");
        taskManager.createEpic(epic);
    }

    public void createOneSubtask() throws IOException {
        subtask = new Subtask("name", Status.NEW, "description", epic.getId(),
                InMemoryTaskManager.generateID, TypeOfTask.SUBTASK, Duration.ofMinutes(30), "01.01.2025,14:30");
        taskManager.createSubtask(subtask, epic.getId());
    }

    public void getAllTasks() throws IOException {
        taskManager.getTaskByID(task.getId());
        taskManager.getEpicByID(epic.getId());
        taskManager.getSubtaskByID(subtask.getId());
    }

    public void setUpStreams() {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    public void cleanUpStreams() throws IOException {
        output.close();
    }

    @AfterEach
    public void removeTasks() throws IOException {
        taskManager.removeAllTasks();
    }

    @Test
    public void shouldSaveAllTasksInFile() throws IOException {
        String[] linesFromFile = new String[5];
        createOneTask();
        createOneEpic();
        createOneSubtask();
        getAllTasks();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/test.csv"))) {
            while(br.ready()) {
                for (int i = 0; i < linesFromFile.length; i++) {
                      linesFromFile[i] = br.readLine();
                }
            }
        }
        assertEquals("id;type;name;status;description;duration;startTime;epicID", linesFromFile[0]);
        assertEquals("1;TASK;name;NEW;description;30;01.01.2025,13:00;",linesFromFile[1]);
        assertEquals("2;EPIC;name;NEW;description;0;01.01.2025,14:30;",linesFromFile[2]);
        assertEquals("3;SUBTASK;name;NEW;description;30;01.01.2025,14:30;2",linesFromFile[3]);
        assertEquals("h;1;2;3;",linesFromFile[4]);
    }

    @Test
    public void saveInFileTestWhenHistoryIsEmpty() throws IOException {
        String[] linesFromFile = new String[3];
        createOneTask();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/test.csv"))) {
            while(br.ready()) {
                for (int i = 0; i < linesFromFile.length; i++) {
                    linesFromFile[i] = br.readLine();
                }
            }
        }
        assertEquals("id;type;name;status;description;duration;startTime;epicID", linesFromFile[0]);
        assertEquals("1;TASK;name;NEW;description;30;01.01.2025,13:00;",linesFromFile[1]);
        assertEquals("h;",linesFromFile[2]);
    }

    @Test
    public void saveInFileEpicWithoutSubtasks() throws IOException {
        String[] linesFromFile = new String[3];
        createOneEpic();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/test.csv"))) {
            while(br.ready()) {
                for (int i = 0; i < linesFromFile.length; i++) {
                    linesFromFile[i] = br.readLine();
                }
            }
        }
        assertEquals("1;EPIC;name;NEW;description;0;" + epic.getStartTime().format(formatter)
                + ";",linesFromFile[1]);
    }

    @Test
    public void saveInFileWhenTasksListIsEmpty() throws IOException {
        String lineFromFile = "";
        taskManager.save();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/test.csv"))) {
            while(br.ready()) {
                    lineFromFile = br.readLine();
            }
        }
        assertEquals("",lineFromFile);
    }

    @Test
    public void loadFromFileTest() throws IOException {
        createOneTask();
        createOneEpic();
        createOneSubtask();
        getAllTasks();
        FileBackedTasksManager newTaskManager =
                FileBackedTasksManager.loadFromFile(new File("resources/test.csv"));
        assertEquals(task, newTaskManager.tasks.get(task.getId()));
        assertEquals(subtask, newTaskManager.subtasks.get(subtask.getId()));
        setUpStreams();
        newTaskManager.printHistory();
        assertEquals("История просмотров: \n" +
                "1. Имя: name. Статус: NEW.  Описание задачи: description. Время старта: " +
                task.getStartTime().format(formatter) + "\n" +
                "2. Имя: name. Статус: NEW.  Описание задачи: description. Время старта: " +
                epic.getStartTime().format(formatter) + "\n" +
                "3. Имя: name. Статус: NEW.  Описание задачи: description. Время старта: " +
                               subtask.getStartTime().format(formatter) + "\n", output.toString());
                cleanUpStreams();
    }

    @Test
    public void loadFromFileWithEmptyHistoryTest() throws IOException {
        createOneTask();
        FileBackedTasksManager newTaskManager =
                FileBackedTasksManager.loadFromFile(new File("resources/test.csv"));
        assertEquals(task, newTaskManager.tasks.get(task.getId()));
        assertTrue(historyManager.linkedList.isEmpty());
        setUpStreams();
        newTaskManager.printHistory();
        assertEquals("История просмотров пуста.\n", output.toString());
        cleanUpStreams();
    }

    @Test
    public void loadFromFileWithEmptyListOfTasksTest() throws IOException {
        createOneTask();
        createOneEpic();
        createOneSubtask();
        getAllTasks();
        taskManager.removeAllTasks();
        FileBackedTasksManager newTaskManager =
                FileBackedTasksManager.loadFromFile(new File("resources/test.csv"));
        assertTrue(newTaskManager.tasks.isEmpty());
        assertTrue(newTaskManager.epics.isEmpty());
        assertTrue(newTaskManager.subtasks.isEmpty());
        setUpStreams();
        newTaskManager.printHistory();
        assertEquals("История просмотров пуста.\n", output.toString());
        cleanUpStreams();
    }

}
