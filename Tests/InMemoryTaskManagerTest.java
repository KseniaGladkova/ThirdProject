package Tests;

import TasksManagers.*;
import History.*;
import Tasks.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class InMemoryTaskManagerTest {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy,HH:mm");
    private static ByteArrayOutputStream output;
    Scanner scanner;
    InMemoryTaskManager taskManager;
    Task newTask;
    Epic newEpic;
    Subtask newSubtask;

    @BeforeEach
    public void createObject() throws IOException {
        taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
        newTask = new Task("name", Status.NEW, "description",
                InMemoryTaskManager.generateID, TypeOfTask.TASK, Duration.ofMinutes(30), "01.01.2025,12:00");
        taskManager.createTask(newTask);
        newEpic = new Epic("name", Status.NEW, "description", new ArrayList<AllTasks>(),
                InMemoryTaskManager.generateID, TypeOfTask.EPIC, Duration.ofMinutes(0),
                LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).format(formatter));
        taskManager.createEpic(newEpic);
        newSubtask = new Subtask("name", Status.NEW, "description", newEpic.getId(),
                InMemoryTaskManager.generateID, TypeOfTask.SUBTASK, Duration.ofMinutes(30), "01.01.2025,13:00");
        taskManager.createSubtask(newSubtask, newEpic.getId());
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    public void removeTasks() throws IOException {
        output.close();
        taskManager.removeAllTasks();
    }

    @Test
    public void generateIDShouldBeEqual4WhenAllTypeOfTasksCreated() {
        assertEquals(InMemoryTaskManager.generateID, 4);
    }

    @Test
    public void shouldCreateTaskAndAddToTasksMap() {
        assertNotNull(newTask, "Задача не создана");
        assertTrue(taskManager.tasks.containsKey(newTask.getId()), "Задачи нет в таблице tasks");
        assertTrue(taskManager.allTasksList.contains(newTask), "Задачи нет в списке allTasksList");
        assertTrue(taskManager.periods.containsKey(newTask.getStartTime()));
    }

    @Test
    public void shouldCreateEpicAndAddToEpicsMap() {
        assertNotNull(newEpic, "Эпик не создан");
        assertTrue(taskManager.epics.containsKey(newEpic.getId()), "Эпика нет в таблице epics");
        assertTrue(taskManager.allTasksList.contains(taskManager.epics.get(newEpic.getId())),
                "Эпика нет в списке allTasksList");
    }

    @Test
    public void shouldCreateSubtaskAndAddToSubtasksMap() {
        assertNotNull(newSubtask, "Подзадача не создана");
        assertTrue(taskManager.subtasks.containsKey(newSubtask.getId()), "Подзадачи нет в таблице subtasks");
        assertTrue(taskManager.allTasksList.contains(newSubtask), "Подзадачи нет в списке allTasksList");
        assertTrue(taskManager.periods.containsKey(newSubtask.getStartTime()));
        assertEquals(newEpic.getId(), newSubtask.getEpicID(), "Подзадача не соответствует эпику");
        assertEquals(newSubtask.getId(), taskManager.epics.get(newEpic.getId()).getSubtasks().getFirst().getId(),
                "Эпик не связан с подзадачей");
    }

    @Test
    public void CreateSubtaskIfEpicIDDoesNotExist() throws IOException {
        int notEpicID = 5;
        assertFalse(taskManager.epics.containsKey(notEpicID));
        taskManager.createSubtask(newSubtask, notEpicID);
        assertEquals("Эпик с таким идентификатором не найден\n", output.toString());
    }

    @Test
    public void toCreateWithNotNumberTest() {
        scanner = new Scanner("notNumber\n notNumber\n notNumber\n notNumber\n");
        taskManager.toCreate(scanner);
        String[] lines = output.toString().split("\n");
        assertEquals("Введите целое число", lines[lines.length - 1]);
        scanner.close();
    }

    @Test
    public void toCreateWithNoCommandTest() {
        scanner = new Scanner("name\n description\n4\n id");
        taskManager.toCreate(scanner);
        String[] lines = output.toString().split("\n");
        assertEquals("Такой команды нет", lines[lines.length - 1]);
        scanner.close();
    }


    @Test
    public void shouldReturnEmptyMapsAndAllTasksListAfterRemoveAllTasks() throws IOException {
        assertFalse(taskManager.tasks.isEmpty());
        assertFalse(taskManager.epics.isEmpty());
        assertFalse(taskManager.subtasks.isEmpty());
        assertFalse(taskManager.periods.isEmpty());
        assertEquals(3, taskManager.allTasksList.size());
        taskManager.removeAllTasks();
        assertEquals("Все задачи удалены\n", output.toString());
        assertTrue(taskManager.tasks.isEmpty());
        assertTrue(taskManager.epics.isEmpty());
        assertTrue(taskManager.subtasks.isEmpty());
        assertTrue(taskManager.periods.isEmpty());
        assertEquals(0, taskManager.allTasksList.size());
    }

    @Test
    public void deleteTaskTestIfIDExists() throws IOException {
        assertTrue(taskManager.tasks.containsKey(newTask.getId()));
        taskManager.deleteTaskByID(newTask.getId());
        assertFalse(taskManager.tasks.containsKey(newTask.getId()));
        assertFalse(taskManager.allTasksList.contains(newTask));
        assertFalse(taskManager.periods.containsKey(newTask.getStartTime()));
        assertEquals("Задача удалена\n", output.toString());
    }

    @Test
    public void deleteTaskTestIfIDDoesNotExist() throws IOException {
        int notID = 5;
        assertFalse(taskManager.tasks.containsKey(notID));
        taskManager.deleteTaskByID(notID);
        assertEquals("Не найдено. Попробуйте снова\n", output.toString());
    }

    @Test
    public void deleteTaskTestIfTasksIsEmpty() throws IOException {
        taskManager.tasks.clear();
        taskManager.deleteTaskByID(newTask.getId());
        assertEquals("Список задач пуст.\n", output.toString());
    }

    @Test
    public void deleteSubtaskTestIfIDExists() throws IOException {
        assertTrue(taskManager.subtasks.containsKey(newSubtask.getId()));
        taskManager.deleteSubtaskByID(newSubtask.getId());
        assertFalse(taskManager.subtasks.containsKey(newSubtask.getId()));
        assertFalse(taskManager.allTasksList.contains(newSubtask));
        assertFalse(taskManager.periods.containsKey(newSubtask.getStartTime()));
        String[] lines = output.toString().split("\n");
        assertEquals("Подзадача удалена", lines[lines.length - 1]);
    }

    @Test
    public void deleteSubtaskTestIfIDDoesNotExist() throws IOException {
        int notID = 5;
        assertFalse(taskManager.subtasks.containsKey(notID));
        taskManager.deleteSubtaskByID(notID);
        assertEquals("Не найдено. Попробуйте снова\n", output.toString());
    }

    @Test
    public void deleteSubtaskTestIfSubtasksIsEmpty() throws IOException {
        taskManager.subtasks.clear();
        taskManager.deleteSubtaskByID(newSubtask.getId());
        String[] lines = output.toString().split("\n");
        assertEquals("Список подзадач пуст.",lines[lines.length -1 ]);
    }

    @Test
    public void deleteEpicAndIncomingSubtasksTestIfIDExists() throws IOException {
        assertTrue(taskManager.epics.containsKey(newEpic.getId()));
        taskManager.deleteEpicByID(newEpic.getId());
        assertEquals("Эпик и входящие в него подзадачи удалены\n", output.toString());
        assertFalse(taskManager.epics.containsKey(newEpic.getId()));
        assertFalse(taskManager.subtasks.containsKey(newSubtask.getId()));
    }

    @Test
    public void deleteEpicAndIncomingSubtasksTestIfIDDoesNotExist() throws IOException {
        int notID = 5;
        assertFalse(taskManager.epics.containsKey(notID));
        taskManager.deleteEpicByID(notID);
        assertEquals("Не найдено. Попробуйте снова\n", output.toString());
    }

    @Test
    public void deleteEpicAndIncomingSubtasksTestIfEpicsIsEmpty() throws IOException {
        taskManager.epics.clear();
        taskManager.deleteEpicByID(newEpic.getId());
        assertEquals("Список эпиков пуст.\n", output.toString());
    }

    @Test
    public void toDeleteWithNoCommandTest() throws IOException {
        scanner = new Scanner(newTask.getId() + "\n4");
        taskManager.toDeleteTask(scanner);
        String[] lines = output.toString().split("\n");
        assertEquals("Такой команды нет", lines[lines.length - 1]);
        scanner.close();
    }

    @Test
    public void toDeleteWithNotNumberTest() throws IOException {
        scanner = new Scanner(newTask.getId() + "\nnotNumber");
        taskManager.toDeleteTask(scanner);
        String[] lines = output.toString().split("\n");
        assertEquals("Введите целое число", lines[lines.length - 1]);
        scanner.close();
    }

    @Test
    public void shouldPrintAllTasksWhenTasksMapIsNotEmpty() throws IOException {
        taskManager.printAllTasks();
        assertEquals("Список задач: \n" +
                newTask.getId() + ". " + newTask.toString() + "\n", output.toString());
    }

    @Test
    public void printAllTasksTestWhenTasksMapIsEmpty() throws IOException {
        taskManager.tasks.clear();
        taskManager.printAllTasks();
        assertEquals("Список задач пуст.\n", output.toString());
    }

    @Test
    public void shouldPrintAllEpicsWhenEpicsMapIsNotEmpty() throws IOException {
        taskManager.deleteSubtaskByID(newSubtask.getId());
        taskManager.printAllEpics();
        String[] lines = output.toString().split("\n");
        assertEquals("Список эпиков: \n" +
                newEpic.getId() + ". " + newEpic.toString(), lines[lines.length - 2] +
                "\n" + lines[lines.length - 1]);
    }

    @Test
    public void printAllEpicsTestWhenEpicsMapIsEmpty() throws IOException {
        taskManager.epics.clear();
        taskManager.printAllEpics();
        assertEquals("Список эпиков пуст.\n", output.toString());
    }

    @Test
    public void shouldPrintAllSubtasksWhenSubtasksMapIsNotEmpty() throws IOException {
        taskManager.printAllSubtasks();
        assertEquals("Список подзадач: \n" +
                newSubtask.getId() + ". " + newSubtask.toString() + "\n", output.toString());
    }

    @Test
    public void printAllSubtasksTestWhenSubtasksMapIsEmpty() throws IOException {
        taskManager.subtasks.clear();
        taskManager.printAllSubtasks();
        assertEquals("Список подзадач пуст.\n", output.toString());
    }

    @Test
    public void printSubtasksForEpicsTest() {
        taskManager.printSubtasksForEpic();
        assertEquals("Эпику " + newEpic.getName() +
                " принадлежат следующие подзадачи: \n" +
                newSubtask.toString() + "\n", output.toString());
    }

    @Test
    public void getPrioritizedTasksTest() {
        taskManager.getPrioritizedTasks();
        assertEquals("1. Имя: name. Статус: NEW.  Описание задачи: description. " +
                "Время старта: 01.01.2025,12:00\n2. Имя: name. Статус: NEW.  Описание задачи: description. " +
                "Время старта: 01.01.2025,13:00\n3. Имя: name. Статус: NEW.  Описание задачи: description. " +
                "Время старта: 01.01.2025,13:00\n", output.toString());
    }

    @Test
    public void printMenuTest() {
        taskManager.printMenu();
        assertEquals("Выберите команду:\n" +
                "1 - Создать задачу\n" +
                "2 - Получить список всех задач\n" +
                "3 - Удалить все задачи\n" +
                "4 - Получить задачу по идентификатору\n" +
                "5 - Изменить задачу\n" +
                "6 - Удалить задачу\n" +
                "7 - Получить списки подзадач для эпиков\n" +
                "8 - Получить историю просмотров\n" +
                "9 - Получить список задач в порядке выполнения\n" +
                "10 - Покинуть меню\n", output.toString());
    }

    @Test
    public void menuForDeleteTest() {
        taskManager.menuForDelete();
        assertEquals("1 - Удалить задачу\n" +
                "2 - Удалить подзадачу\n" +
                "3 - Удалить эпик\n" +
                "При удалении эпика входящие в него подзадачи будут удалены!\n", output.toString());
    }

    @Test
    public void shouldReturnTaskToStringAfterGetTaskByID() throws IOException {
        taskManager.getTaskByID(newTask.getId());
        assertEquals("Имя: " + newTask.getName() + ". Статус: " + newTask.getStatus() +
                ".  Описание задачи: " + newTask.getDescription() + ". Время старта: "
                + newTask.getStartTime().format(formatter) + "\n", output.toString());
    }

    @Test
    public void getTaskByIDTestWhenTasksIsEmpty() throws IOException {
        taskManager.tasks.clear();
        assertFalse(taskManager.tasks.containsKey(newTask.getId()));
        taskManager.getTaskByID(newTask.getId());
        assertEquals("Список задач пуст.\n", output.toString());
    }

    @Test
    public void getTaskByIDWhenIDIsNotExists() throws IOException {
        int notID = 5;
        assertFalse(taskManager.tasks.containsKey(notID));
        taskManager.getTaskByID(notID);
        assertEquals("Задачи с таким идентификатором не существует\n", output.toString());
    }

    @Test
    public void shouldReturnEpicToStringAfterGetEpicByID() throws IOException {
        taskManager.getEpicByID(newEpic.getId());
        assertEquals("Имя: " + newEpic.getName() + ". Статус: " + newEpic.getStatus() +
                ".  Описание задачи: " + newEpic.getDescription() + ". Время старта: "
                + "01.01.2025,13:00\n", output.toString());
    }

    @Test
    public void getEpicByIDTestWhenEpicsIsEmpty() throws IOException {
        taskManager.epics.clear();
        assertFalse(taskManager.epics.containsKey(newEpic.getId()));
        taskManager.getEpicByID(newEpic.getId());
        assertEquals("Список эпиков пуст.\n", output.toString());
    }

    @Test
    public void getEpicByIDWhenIDIsNotExists() throws IOException {
        int notID = 5;
        assertFalse(taskManager.epics.containsKey(notID));
        taskManager.getEpicByID(notID);
        assertEquals("Задачи с таким идентификатором не существует\n", output.toString());
    }

    @Test
    public void shouldReturnSubtaskToStringAfterGetSubtaskByID() throws IOException {
        taskManager.getSubtaskByID(newSubtask.getId());
        assertEquals("Имя: " + newSubtask.getName() + ". Статус: " + newSubtask.getStatus() +
                ".  Описание задачи: " + newSubtask.getDescription() + ". Время старта: "
                + newSubtask.getStartTime().format(formatter) + "\n", output.toString());
    }

    @Test
    public void getSubtaskByIDTestWhenSubtasksIsEmpty() throws IOException {
        taskManager.subtasks.clear();
        assertFalse(taskManager.subtasks.containsKey(newSubtask.getId()));
        taskManager.getSubtaskByID(newSubtask.getId());
        assertEquals("Список подзадач пуст.\n", output.toString());
    }

    @Test
    public void getSubtaskByIDWhenIDDoesNotExist() throws IOException {
        int notID = 5;
        assertFalse(taskManager.subtasks.containsKey(notID));
        taskManager.getSubtaskByID(notID);
        assertEquals("Задачи с таким идентификатором не существует\n", output.toString());
    }

    @Test
    public void toGetTaskTestWhenCommandDoesNotExist() throws IOException {
        scanner = new Scanner("1\n4\n");
        taskManager.toGetTask(scanner);
        String[] lines = output.toString().split("\n");
        assertEquals("Такой команды нет", lines[lines.length - 1]);
        scanner.close();
    }

    @Test
    public void toGetTaskTestWhenCommandIsNotNumber() throws IOException {
        scanner = new Scanner("notNumber\n4\n");
        taskManager.toGetTask(scanner);
        String[] lines = output.toString().split("\n");
        assertEquals("Введите целое число", lines[lines.length - 1]);
        scanner.close();
    }

    @Test
    public void changeTaskTest() throws IOException {
        String newName = "taskNewName";
        Status newStatus = Status.IN_PROGRESS;
        String newDescription = "taskNewDescription";
        taskManager.changeTask(newTask.getId(), newName, newStatus, newDescription,
                newTask.getDuration(), newTask.getStartTime().format(formatter));
        assertEquals(newName, taskManager.tasks.get(newTask.getId()).getName());
        assertEquals(newStatus, taskManager.tasks.get(newTask.getId()).getStatus());
        assertEquals(newDescription, taskManager.tasks.get(newTask.getId()).getDescription());
        assertEquals("Задача изменена\n", output.toString());
    }

    @Test
    public void changeTaskTestWhenIDDoesNotExist() throws IOException {
        String newName = "taskNewName";
        Status newStatus = Status.IN_PROGRESS;
        String newDescription = "taskNewDescription";
        int notID = 5;
        assertFalse(taskManager.tasks.containsKey(notID));
        taskManager.changeTask(notID, newName, newStatus, newDescription,
                newTask.getDuration(), newTask.getStartTime().format(formatter));
        assertEquals("Задача не найдена в списке. Попробуйте снова\n", output.toString());
    }

    @Test
    public void changeTaskTestWhenTasksMapIsEmpty() throws IOException {
        taskManager.tasks.clear();
        String newName = "taskNewName";
        Status newStatus = Status.IN_PROGRESS;
        String newDescription = "taskNewDescription";
        assertFalse(taskManager.tasks.containsKey(newTask.getId()));
        taskManager.changeTask(newTask.getId(), newName, newStatus, newDescription,
                newTask.getDuration(), newTask.getStartTime().format(formatter));
        assertEquals("Список задач пуст.\n", output.toString());
    }

    @Test
    public void changeSubtaskTest() throws IOException {
        String newName = "subtaskNewName";
        Status newStatus = Status.IN_PROGRESS;
        String newDescription = "subtaskNewDescription";
        taskManager.changeSubtask(newSubtask.getId(), newName, newStatus, newDescription, newEpic.getId(),
                Duration.ofMinutes(30), newSubtask.getStartTime().format(formatter));
        assertEquals(newName, taskManager.subtasks.get(newSubtask.getId()).getName());
        assertEquals(newStatus, taskManager.subtasks.get(newSubtask.getId()).getStatus());
        assertEquals(newDescription, taskManager.subtasks.get(newSubtask.getId()).getDescription());
        assertFalse(taskManager.epics.get(newEpic.getId()).getSubtasks().contains(newSubtask));
        String[] lines = output.toString().split("\n");
        assertEquals("Подзадача изменена", lines[lines.length - 1]);
    }

    @Test
    public void changeSubtaskTestWhenIDDoesNotExist() throws IOException {
        int notID = 5;
        String newName = "subtaskNewName";
        Status newStatus = Status.IN_PROGRESS;
        String newDescription = "subtaskNewDescription";
        taskManager.changeSubtask(notID, newName, newStatus, newDescription, newEpic.getId(), newSubtask.getDuration(),
                newSubtask.getStartTime().format(formatter));
        assertFalse(taskManager.subtasks.containsKey(notID));
        assertEquals("Подзадача не найдена в списке. Попробуйте снова\n", output.toString());
    }

    @Test
    public void changeSubtaskTestWhenSubtasksMapIsEmpty() throws IOException {
        taskManager.subtasks.clear();
        String newName = "subtaskNewName";
        Status newStatus = Status.IN_PROGRESS;
        String newDescription = "subtaskNewDescription";
        assertFalse(taskManager.subtasks.containsKey(newSubtask.getId()));
        taskManager.changeSubtask(newSubtask.getId(), newName, newStatus, newDescription, newEpic.getId(), newSubtask.getDuration(),
                newSubtask.getStartTime().format(formatter));
        assertEquals("Список подзадач пуст.\n", output.toString());
    }

    @Test
    public void  changeSubtaskTestWhenEpicDoesNotExist() throws IOException {
        String newName = "subtaskNewName";
        Status newStatus = Status.IN_PROGRESS;
        String newDescription = "subtaskNewDescription";
        int notEpicID = 5;
        assertFalse(taskManager.epics.containsKey(notEpicID));
        taskManager.changeSubtask(newSubtask.getId(), newName, newStatus, newDescription, notEpicID,
                newSubtask.getDuration(), newSubtask.getStartTime().format(formatter));
        assertEquals("Эпик с таким идентификатором не найден\n", output.toString());
    }

    @Test
    public void changeEpicTest() throws IOException {
        String newName = "epicNewName";
        Status newStatus = Status.IN_PROGRESS;
        String newDescription = "epicNewDescription";
        taskManager.epics.get(newEpic.getId()).getSubtasks().clear();
        ArrayList<AllTasks> newSubtaskID = taskManager.epics.get(newEpic.getId()).getSubtasks();
        taskManager.changeEpic(newName, newStatus, newDescription, newSubtaskID, newEpic.getId(),
                newEpic.getDuration(), newEpic.getStartTime().format(formatter));
        assertEquals(newName, taskManager.epics.get(newEpic.getId()).getName());
        assertEquals(newStatus, taskManager.epics.get(newEpic.getId()).getStatus());
        assertEquals(newDescription, taskManager.epics.get(newEpic.getId()).getDescription());
        assertEquals(Status.IN_PROGRESS, taskManager.epics.get(newEpic.getId()).getStatus());
        assertEquals("Эпик изменён\n", output.toString());
    }

    @Test
    public void changeEpicTestWhenIDDoesNotExist() throws IOException {
        int notID = 5;
        String newName = "epicNewName";
        Status newStatus = Status.IN_PROGRESS;
        String newDescription = "epicNewDescription";
        taskManager.changeEpic(newName, newStatus, newDescription, newEpic.getSubtasks(), notID, newEpic.getDuration(),
                newEpic.getStartTime().format(formatter));
        assertFalse(taskManager.epics.containsKey(notID));
        assertEquals("Эпик не найден в списке. Попробуйте снова\n", output.toString());
    }

    @Test
    public void changeEpicTestWhenEpicsMapIsEmpty() throws IOException {
        taskManager.epics.clear();
        String newName = "epicNewName";
        Status newStatus = Status.IN_PROGRESS;
        String newDescription = "epicNewDescription";
        taskManager.changeEpic(newName, newStatus, newDescription, newEpic.getSubtasks(), newEpic.getId(),
                newEpic.getDuration(), newEpic.getStartTime().format(formatter));
        assertFalse(taskManager.epics.containsKey(newEpic.getId()));
        assertEquals("Список эпиков пуст.\n", output.toString());
    }

    @Test
    public void toChangeTaskTestWhenCommandIsNotNumber() {
        scanner = new Scanner("notNumber\n" + newTask.getId() + "\nname\ndescription");
        taskManager.toChangeTask(scanner);
        String[] lines = output.toString().split("\n");
        assertEquals("Введите целое число", lines[lines.length - 1]);
        scanner.close();
    }

    @Test
    public void toChangeTaskTestWhenCommandDoesNotExist() throws IOException {
        scanner = new Scanner("4\n" + newTask.getId() + "\nname\ndescription");
        taskManager.toChangeTask(scanner);
        String[] lines = output.toString().split("\n");
        assertEquals("Такой команды нет", lines[lines.length - 1]);
        scanner.close();
    }

    @Test
    public void shouldReturnTrueWhenReplayBeforeTaskTimeExists() throws IOException {
        Task task1 = new Task("name", Status.NEW, "description", InMemoryTaskManager.generateID,
                TypeOfTask.TASK, Duration.ofMinutes(60), "01.01.2025,15:00");
        taskManager.createTask(task1);
        Task task2 = new Task("name", Status.NEW, "description", InMemoryTaskManager.generateID,
                TypeOfTask.TASK, Duration.ofMinutes(60), "01.01.2025,14:30");
        assertTrue(taskManager.isReplayExists(task2.getStartTime(), task2.getDuration()));
        taskManager.removeAllTasks();
    }

    @Test
    public void shouldReturnTrueWhenReplayDuringTaskTimeExists() throws IOException {
        Task task1 = new Task("name", Status.NEW, "description", InMemoryTaskManager.generateID,
                TypeOfTask.TASK, Duration.ofMinutes(60), "01.01.2025,15:00");
        taskManager.createTask(task1);
        Task task2 = new Task("name", Status.NEW, "description", InMemoryTaskManager.generateID,
                TypeOfTask.TASK, Duration.ofMinutes(10), "01.01.2025,15:30");
        assertTrue(taskManager.isReplayExists(task2.getStartTime(), task2.getDuration()));
        taskManager.removeAllTasks();
    }

    @Test
    public void shouldReturnTrueWhenReplayAfterTaskTimeExists() throws IOException {
        Task task1 = new Task("name", Status.NEW, "description", InMemoryTaskManager.generateID,
                TypeOfTask.TASK, Duration.ofMinutes(60), "01.01.2025,15:00");
        taskManager.createTask(task1);
        Task task2 = new Task("name", Status.NEW, "description", InMemoryTaskManager.generateID,
                TypeOfTask.TASK, Duration.ofMinutes(60), "01.01.2025,15:30");
        assertTrue(taskManager.isReplayExists(task2.getStartTime(), task2.getDuration()));
        taskManager.removeAllTasks();
    }

    @Test
    public void shouldReturnFalseWhenReplayDoesNotExist() throws IOException {
        Task task1 = new Task("name", Status.NEW, "description", InMemoryTaskManager.generateID,
                TypeOfTask.TASK, Duration.ofMinutes(60), "01.01.2025,15:00");
        taskManager.createTask(task1);
        Task task2 = new Task("name", Status.NEW, "description", InMemoryTaskManager.generateID,
                TypeOfTask.TASK, Duration.ofMinutes(60), "01.01.2025,16:00");
        assertFalse(taskManager.isReplayExists(task2.getStartTime(), task2.getDuration()));
        taskManager.removeAllTasks();
    }

    @Test
    public void shouldReturn0WhenGetSubtaskIndexIfSizeIs1() {
        int expectedIndex = 0;
        int actualIndex = taskManager.getSubtaskIndex(newEpic, newSubtask.getId());
        assertEquals(1, taskManager.epics.get(newEpic.getId()).getSubtasks().size());
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void shouldReturn1And2WhenGetSubtaskIndexIfSizeIs3() throws IOException {
        Subtask subtask1 = new Subtask("name", Status.NEW, "description", newEpic.getId(),
                InMemoryTaskManager.generateID, TypeOfTask.SUBTASK, Duration.ofMinutes(15), "01.01.2025,14:00");
        taskManager.createSubtask(subtask1, newEpic.getId());
        Subtask subtask2 = new Subtask("name", Status.NEW, "description", newEpic.getId(),
                InMemoryTaskManager.generateID, TypeOfTask.SUBTASK, Duration.ofMinutes(15), "01.01.2025,14:15");
        taskManager.createSubtask(subtask2, newEpic.getId());
        int expectedIndex1 = 1;
        int expectedIndex2 = 2;
        assertEquals(3, taskManager.epics.get(newEpic.getId()).getSubtasks().size());
        assertEquals(expectedIndex1, taskManager.getSubtaskIndex(newEpic, subtask1.getId()));
        assertEquals(expectedIndex2, taskManager.getSubtaskIndex(newEpic, subtask2.getId()));
    }

    @Test
    public void statusShouldBeNewWhenGetNewStatusWithCommandNew() {
        scanner = new Scanner("NEW");
        taskManager.getNewStatus(scanner);
        assertEquals(Status.NEW, InMemoryTaskManager.newStatus);
        scanner.close();
    }

    @Test
    public void statusShouldBeInProgressWhenGetNewStatusWithCommandInProgress() {
        scanner = new Scanner("IN_PROGRESS");
        taskManager.getNewStatus(scanner);
        assertEquals(Status.IN_PROGRESS, InMemoryTaskManager.newStatus);
        scanner.close();
    }

    @Test
    public void statusShouldBeDoneWhenGetNewStatusWithCommandDone() {
        scanner = new Scanner("DONE");
        taskManager.getNewStatus(scanner);
        assertEquals(Status.DONE, InMemoryTaskManager.newStatus);
        scanner.close();
    }

    @Test
    public void shouldReturnStringWhenGetNewStatusIfCommandDoesNotExist() throws IOException {
        scanner = new Scanner("DOES_NOT_EXIST");
        taskManager.getNewStatus(scanner);
        String[] lines = output.toString().split("\n");
        assertEquals("Не найдено. Попробуйте снова", lines[lines.length - 1]);
        scanner.close();
    }
}
