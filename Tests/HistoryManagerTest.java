package Tests;
import TasksManagers.*;
import History.*;
import Tasks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static java.time.Month.JANUARY;

public class HistoryManagerTest {
    private ByteArrayOutputStream output;
    InMemoryHistoryManager historyManager;
    InMemoryTaskManager taskManager;
    Task task1;
    Task task2;
    Task task3;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy,HH:mm");

    @BeforeEach
    public void createTasks() throws IOException {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);
        task1 = new Task("name1", Status.NEW, "description1",
                InMemoryTaskManager.generateID, TypeOfTask.TASK, Duration.ofMinutes(30), "01.01.2025,12:00");
        taskManager.createTask(task1);
        task2 = new Task("name2", Status.NEW, "description2",
                InMemoryTaskManager.generateID, TypeOfTask.TASK, Duration.ofMinutes(30), "01.01.2025,12:30");
        taskManager.createTask(task2);
        task3 = new Task("name3", Status.NEW, "description3",
                InMemoryTaskManager.generateID, TypeOfTask.TASK, Duration.ofMinutes(30), "01.01.2025,13:00");
        taskManager.createTask(task3);
    }

    @AfterEach
    public void deleteTasks() throws IOException {
        taskManager.removeAllTasks();
    }

    @Test
    public void addWithEmptyHistoryTest() {
        assertTrue(historyManager.linkedList.isEmpty(), "История не пустая");
        historyManager.add(task1);
        assertTrue(historyManager.linkedList.containsID(task1.getId()));
        assertEquals(1, historyManager.linkedList.getSize());
    }

    @Test
    public void addWithRepeatInTheMiddleTest() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        assertEquals(task3, historyManager.linkedList.tail.task);
        historyManager.add(task2);
        assertEquals(task2, historyManager.linkedList.tail.task);
        assertEquals(3, historyManager.linkedList.getSize());
    }

    @Test
    public void addWithRepeatInTheEndTest() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task2);
        assertEquals(2, historyManager.linkedList.getSize());
        assertEquals(task2, historyManager.linkedList.tail.task);
        assertEquals(task1, historyManager.linkedList.head.task);
    }

    @Test
    public void addWithSizeMoreThan10Test() {
         List<Task> tenTasks = getTenTasks();
         for(Task task: tenTasks) {
             historyManager.add(task);
         }
         AllTasks head = historyManager.linkedList.head.task;
         assertEquals(10, historyManager.linkedList.getSize());
         historyManager.add(task1);
         assertEquals(10, historyManager.linkedList.getSize());
         assertNotEquals(head, historyManager.linkedList.head.task);
         assertEquals(task1, historyManager.linkedList.tail.task);
    }

    @Test
    public void removeFirstTaskTest() {
        historyManager.add(task1);
        historyManager.add(task2);
        assertEquals(2, historyManager.linkedList.getSize());
        historyManager.remove(task1.getId());
        assertNotEquals(task1, historyManager.linkedList.head.task);
        assertFalse(historyManager.linkedList.containsID(task1.getId()));
        assertEquals(1, historyManager.linkedList.getSize());
    }

    @Test
    public void removeLastTaskTest() {
        historyManager.add(task1);
        historyManager.add(task2);
        assertEquals(2, historyManager.linkedList.getSize());
        historyManager.remove(task2.getId());
        assertNotEquals(task2, historyManager.linkedList.tail.task);
        assertFalse(historyManager.linkedList.containsID(task2.getId()));
        assertEquals(1, historyManager.linkedList.getSize());
    }

    @Test
    public void removeMiddleTaskTest() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        assertEquals(3, historyManager.linkedList.getSize());
        historyManager.remove(task2.getId());
        assertFalse(historyManager.linkedList.containsID(task2.getId()));
        assertEquals(2, historyManager.linkedList.getSize());
    }

    @Test
    public void shouldReturnEmptyLinkedListAfterCleanTheHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        assertEquals(2, historyManager.linkedList.getSize());
        historyManager.clearHistory();
        assertEquals(0, historyManager.linkedList.getSize());
        assertTrue(historyManager.linkedList.isEmpty());
        assertNull(historyManager.linkedList.head);
        assertNull(historyManager.linkedList.tail);
    }

    @Test
    public void printHistoryTestWhenHistoryIsEmpty() throws IOException {
        setUpStreams();
        assertTrue(historyManager.linkedList.isEmpty());
        assertEquals(0, historyManager.linkedList.getSize());
        taskManager.printHistory();
        assertEquals("История просмотров пуста.\n", output.toString());
        cleanUpStreams();
    }

    @Test
    public void printHistoryTestWhenHistoryIsNotEmpty() {
        historyManager.add(task1);
        historyManager.add(task2);
        assertFalse(historyManager.linkedList.isEmpty());
        assertEquals(2, historyManager.linkedList.getSize());
        assertEquals("1. Имя: name1. Статус: NEW.  Описание задачи: description1. Время старта: 01.01.2025,12:00\n" +
                        "2. Имя: name2. Статус: NEW.  Описание задачи: description2. Время старта: 01.01.2025,12:30\n",
                               historyManager.getHistory());
    }

    public List<Task> getTenTasks() {
        List<Task> tenTasks = new ArrayList<>();
        LocalDateTime time = LocalDateTime.of(2025, JANUARY, 1, 0, 0);
        for (int i = 0; i < 10; i++) {
            tenTasks.add(new Task("name", Status.NEW, "description",
                    InMemoryTaskManager.generateID, TypeOfTask.TASK, Duration.ofMinutes(30), time.format(formatter)));
            time = time.plusMinutes(30);
            InMemoryTaskManager.generateID++;
        }
        return tenTasks;
    }

    public void setUpStreams() {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    public void cleanUpStreams() throws IOException {
        output.close();
    }


}
