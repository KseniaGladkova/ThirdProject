package Tests;
import TasksManagers.*;
import History.*;
import Tasks.*;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicStatusTest {
    static TaskManager taskManager;
    Epic epic;

    @BeforeAll
    public static void createObject() {
        taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
    }

    @BeforeEach
    public void createEpic() throws IOException {
        epic = new Epic("name", Status.NEW, "description", new ArrayList<AllTasks>(),
                InMemoryTaskManager.generateID, TypeOfTask.EPIC, Duration.ofMinutes(0), "01.01.2025,12:00");
        taskManager.createEpic(epic);
    }

    public void createTwoSubtasks(Status status1, Status status2) throws IOException {
        Subtask subtask1 = new Subtask("name", status1, "description", epic.getId(),
                InMemoryTaskManager.generateID, TypeOfTask.SUBTASK, Duration.ofMinutes(30), "01.01.2025,12:00");
        taskManager.createSubtask(subtask1,epic.getId());
        Subtask subtask2 = new Subtask("name", status2, "description", epic.getId(),
                InMemoryTaskManager.generateID, TypeOfTask.SUBTASK, Duration.ofMinutes(30), "01.01.2025,12:30");
        taskManager.createSubtask(subtask2,epic.getId());
    }

    @AfterEach
    public void delete() throws IOException {
        taskManager.removeAllTasks();
    }

    @Test
    public void shouldReturnStatusNewWhenEpicHasNoSubtasks() throws IOException {
        taskManager.changeEpicStatus(epic);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void shouldReturnStatusNewWhenAllSubtasksHaveStatusNew() throws IOException {
        createTwoSubtasks(Status.NEW, Status.NEW);
        taskManager.changeEpicStatus(epic);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void shouldReturnStatusDoneWhenAllSubtasksHaveStatusDone() throws IOException {
        createTwoSubtasks(Status.DONE, Status.DONE);
        taskManager.changeEpicStatus(epic);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void shouldReturnStatusInProgressWhenAllSubtasksHaveStatusInProgress() throws IOException {
        createTwoSubtasks(Status.IN_PROGRESS, Status.IN_PROGRESS);
        taskManager.changeEpicStatus(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldReturnStatusInProgressWhenSubtasksStatusAreNewAndDone() throws IOException {
        createTwoSubtasks(Status.NEW, Status.DONE);
        taskManager.changeEpicStatus(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldReturnStatusInProgressWhenSubtasksStatusAreNewAndInProgress() throws IOException {
        createTwoSubtasks(Status.NEW, Status.IN_PROGRESS);
        taskManager.changeEpicStatus(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldReturnStatusInProgressWhenSubtasksStatusAreDoneAndInProgress() throws IOException {
        createTwoSubtasks(Status.DONE, Status.IN_PROGRESS);
        taskManager.changeEpicStatus(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}
