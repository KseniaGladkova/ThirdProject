import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class Manager {
    static Scanner scanner = new Scanner(System.in);
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    public int generateIDForTask = 1;
    public int generateIDForEpic = 1;
    public int generateIDForSubtask = 1;

    public static final String CHOOSE_COMMAND = "Выберите команду: ";
    public static final String CREATE_TASK = "1 - Создать задачу";
    public static final String PRINT_ALL_TASKS = "2 - Получить список всех задач";
    public static final String REMOVE_ALL_TASKS = "3 - Удалить все задачи";
    public static final String PRINT_TASK = "4 - Получить задачу по идентификатору";
    public static final String UPDATE_TASK = "5 - Изменить задачу";
    public static final String REMOVE_TASK = "6 - Удалить задачу";
    public static final String PRINT_SUBTASKS_FOR_EPIC = "7 - Получить списки подзадач для эпиков";
    public static final String EXIT = "8 - Выйти из приложения";
    public static final String NOT_COMMAND = "Такой команды нет";
    public static final String PRINT_NUMBER = "Введите целое число";
    public static final String PRINT_NAME = "Введите имя задачи: ";
    public static final String PRINT_DESCRIPTION = "Описание задачи: ";
    public static final String PRINT_TYPE_OF_TASK = "Выберите тип задачи: 1 - задача; 2 - эпик; 3 - подзадача;";
    public static final String TASK_CREATED = "Задача создана, идентификатор: ";
    public static final String CHOOSE_EPIC = "Введите идентификатор эпика, к которому относится подзадача: ";
    public static final String EPIC_IS_NOT_EXIST = "Эпик с таким идентификатором не найден";
    public static final String LIST_IS_EMPTY = "Все задачи удалены";
    public static final String TASKS = "Список задач: ";
    public static final String EPICS = "Список эпиков: ";
    public static final String SUBTASKS = "Список подзадач: ";
    public static final String TASK_LIST_IS_EMPTY = "Список задач пуст.";
    public static final String EPIC_LIST_IS_EMPTY = "Список эпиков пуст.";
    public static final String SUBTASK_LIST_IS_EMPTY = "Список подзадач пуст.";
    public static final String NOT_FOUND = "Не найдено, попробуйте снова";
    public static final String PRINT_ID = "Введите идентификатор задачи";
    public static final String CHOOSE_TYPE_FOR_CHANGE = "1 - изменить задачу; 2 - изменить подзадачу; ";
    public static final String PRINT_STATUS = "Измените статус. Доступные варианты: IN_PROGRESS/DONE ";
    public static final String TASK_CHANGED = "Задача изменена";
    public static final String SUBTASK_CHANGED = "Подзадача изменена";
    public static final String DELETE_TASK = "1 - Удалить задачу";
    public static final String DELETE_SUBTASK = "2 - Удалить подзадачу";
    public static final String DELETE_EPIC = "3 - Удалить эпик";
    public static final String WARNING = "При удалении эпика входящие в него подзадачи будут удалены!";
    public static final String TASK_DELETED = "Задача удалена";
    public static final String SUBTASK_DELETED = "Подзадача удалена";
    public static final String EPIC_AND_SUBTASKS_DELETED = "Эпик и входящие в него подзадачи удалены";

    public void runApp() {
        while (true) {
            printMenu();
            String command = scanner.nextLine();
            if (isNumeric(command)) {
                int comm = Integer.parseInt(command);
                switch (comm) {
                    case 1:
                        System.out.println(PRINT_NAME);
                        String name = scanner.nextLine();
                        System.out.println(PRINT_DESCRIPTION);
                        String description = scanner.nextLine();
                        System.out.println(PRINT_TYPE_OF_TASK);
                        String type = scanner.nextLine();
                        if (!isNumeric(type)) {
                            System.out.println(PRINT_NUMBER);
                            continue;
                        } else {
                            int typeOfTask = Integer.parseInt(type);
                            switch (typeOfTask) {
                                case 1:
                                    Task task = new Task(name, "NEW", description);
                                    createTask(task);
                                    break;
                                case 2:
                                    Epic epic = new Epic(name, "NEW", description, new ArrayList<>());
                                    createEpic(epic);
                                    break;
                                case 3:
                                    System.out.println(CHOOSE_EPIC);
                                    String numberOdEpic = scanner.nextLine();
                                    if (!isNumeric(numberOdEpic)) {
                                        System.out.println(PRINT_NUMBER);
                                    } else {
                                        int epicID = Integer.parseInt(numberOdEpic);
                                        if (!epics.containsKey(epicID)) {
                                            System.out.println(EPIC_IS_NOT_EXIST);
                                        } else {
                                            Subtask subtask = new Subtask(name, "NEW", description, epicID);
                                            createSubtask(subtask, epicID);
                                        }
                                    }
                                    break;
                                default:
                                    System.out.println(NOT_COMMAND);
                                    break;
                            }
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
                        System.out.println(PRINT_TYPE_OF_TASK);
                        String typeOfTask = scanner.nextLine();
                        if (!isNumeric(typeOfTask)) {
                            System.out.println(PRINT_NUMBER);
                        } else {
                            int typeOf = Integer.parseInt(typeOfTask);
                            System.out.println(PRINT_ID);
                            String taskID = scanner.nextLine();
                            if (!isNumeric(taskID)) {
                                System.out.println(PRINT_NUMBER);
                            } else {
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
                                        System.out.println(NOT_COMMAND);
                                        break;
                                }
                            }
                        }
                        break;
                    case 5:
                        System.out.println(CHOOSE_TYPE_FOR_CHANGE);
                        String var = scanner.nextLine();
                        if (!isNumeric(var)) {
                            System.out.println(PRINT_NUMBER);
                        } else {
                            int variant = Integer.parseInt(var);
                            System.out.println(PRINT_ID);
                            String uniqID = scanner.nextLine();
                            if (!isNumeric(uniqID)) {
                                System.out.println(PRINT_NUMBER);
                            } else {
                                int uniqieID = Integer.parseInt(uniqID);
                                System.out.println(PRINT_NAME);
                                String newName = scanner.nextLine();
                                System.out.println(PRINT_DESCRIPTION);
                                String newDescription = scanner.nextLine();
                                System.out.println(PRINT_STATUS);
                                String newStatus = scanner.nextLine();
                                if ((!newStatus.equals("DONE")) && (!newStatus.equals("IN_PROGRESS"))) {
                                    System.out.println(NOT_FOUND);
                                } else {
                                    switch (variant) {
                                        case 1:
                                            changeTask(uniqieID, newName, newStatus, newDescription);
                                            break;
                                        case 2:
                                            System.out.println(CHOOSE_EPIC);
                                            String newEpic = scanner.nextLine();
                                            if (!isNumeric(newEpic)) {
                                                System.out.println(PRINT_NUMBER);
                                            } else {
                                                int newEpicID = Integer.parseInt(newEpic);
                                                changeSubtask(uniqieID, newName, newStatus, newDescription, newEpicID);
                                                break;
                                            }
                                            break;
                                        default:
                                            System.out.println(NOT_COMMAND);
                                            break;
                                    }
                                }
                            }
                        }
                        break;
                    case 6:
                        menuForDelete();
                        String point = scanner.nextLine();
                        if (!isNumeric(point)) {
                            System.out.println(PRINT_NUMBER);
                        } else {
                            int pointOfMenu = Integer.parseInt(point);
                            System.out.println(PRINT_ID);
                            String deleteID = scanner.nextLine();
                            if (!isNumeric(deleteID)) {
                                System.out.println(PRINT_NUMBER);
                            } else {
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
                                        System.out.println(NOT_COMMAND);
                                        break;
                                }
                            }
                        }
                        break;
                    case 7:
                        printSubtasksForEpic();
                        break;
                    case 8:
                        return;
                    default:
                        System.out.println(NOT_COMMAND);
                        break;
                }
            } else {
                System.out.println(PRINT_NUMBER);
            }
        }
    }

    public void createTask(Task task) {
        tasks.put(generateIDForTask, task);
        System.out.println(TASK_CREATED + generateIDForTask);
        ++generateIDForTask;
    }

    public void createEpic(Epic epic) {
        epics.put(generateIDForEpic, epic);
        System.out.println(TASK_CREATED + generateIDForEpic);
        ++generateIDForEpic;
    }

    public void createSubtask(Subtask subtask, int epicID) {
        subtasks.put(generateIDForSubtask, subtask);
        for (Integer key : epics.keySet()) {
            if (key == epicID) {
                epics.get(epicID).getSubtaskID().add(generateIDForSubtask);
            }
        }
        System.out.println(TASK_CREATED + generateIDForSubtask);
        ++generateIDForSubtask;
    }

    public void RemoveAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        System.out.println(LIST_IS_EMPTY);
    }

    public void printAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println(TASK_LIST_IS_EMPTY);
        } else {
            System.out.println(TASKS);
            for (Integer k : tasks.keySet()) {
                System.out.print(k + ". ");
                System.out.println(tasks.get(k).toString());
            }
        }
    }

    public void printAllEpics() {
        if (epics.isEmpty()) {
            System.out.println(EPIC_LIST_IS_EMPTY);
        } else {
            System.out.println(EPICS);
            for (Integer k : epics.keySet()) {
                System.out.print(k + ". ");
                System.out.println(epics.get(k).toString());
            }
        }
    }

    public void printAllSubtasks() {
        if (subtasks.isEmpty()) {
            System.out.println(SUBTASK_LIST_IS_EMPTY);
        } else {
            System.out.println(SUBTASKS);
            for (Integer k : subtasks.keySet()) {
                System.out.print(k + ". ");
                System.out.println(subtasks.get(k).toString());
            }
        }
    }

    public void printSubtasksForEpic() {
        for (Epic ep : epics.values()) {
            System.out.println("Эпику " + ep.getName() + " принадлежат следующие подзадачи: ");
            for (Integer sudID : ep.getSubtaskID()) {
                for (Integer subKey : subtasks.keySet()) {
                    if (Objects.equals(sudID, subKey)) {
                        System.out.print(subKey + ". ");
                        System.out.println(subtasks.get(subKey).toString());
                    }
                }
            }
        }
    }

    public void takeTask(int uniqueNumber) {
        if (!tasks.containsKey(uniqueNumber)) {
            System.out.println(NOT_FOUND);
        } else {
            for (Integer k : tasks.keySet()) {
                if (Objects.equals(k, uniqueNumber)) {
                    System.out.println(tasks.get(k).toString());
                }
            }
        }
    }

    public void takeEpic(int uniqueNumber) {
        if (!epics.containsKey(uniqueNumber)) {
            System.out.println(NOT_FOUND);
        } else {
            for (Integer k : epics.keySet()) {
                if (Objects.equals(k, uniqueNumber)) {
                    System.out.println(epics.get(k).toString());
                }
            }
        }
    }

    public void takeSubtask(int uniqueNumber) {
        if (!subtasks.containsKey(uniqueNumber)) {
            System.out.println(NOT_FOUND);
        } else {
            for (Integer k : subtasks.keySet()) {
                if (Objects.equals(k, uniqueNumber)) {
                    System.out.println(subtasks.get(k).toString());
                }
            }
        }
    }

    public void changeTask(int uniqieID, String newName, String newStatus, String newDescription) {
        if (!tasks.containsKey(uniqieID)) {
            System.out.println(NOT_FOUND);
        } else {
            Task newTask = new Task(newName, newStatus, newDescription);
            tasks.put(uniqieID, newTask);
            System.out.println(TASK_CHANGED);
        }
    }

    public void changeSubtask(int uniqieID, String newName, String newStatus, String newDescription, int newEpicID) {
        if (!subtasks.containsKey(uniqieID)) {
            System.out.println(NOT_FOUND);
        } else {
            Subtask newSubtask = new Subtask(newName, newStatus, newDescription, newEpicID);
            subtasks.put(uniqieID, newSubtask);
            for (Subtask sub : subtasks.values()) {
                for (Integer k : epics.keySet()) {
                    if (Objects.equals(sub.getEpicID(), k)) {
                        changeEpic(epics.get(k));
                    }
                }
            }
        }
        System.out.println(SUBTASK_CHANGED);
    }

    public void changeEpic(Epic epic) {
        int statusNew = 0;
        int done = 0;
        for (Integer k : subtasks.keySet()) {
            if (epic.getSubtaskID().contains(k)) {
                if (subtasks.get(k).getStatus().equals("DONE")) {
                    ++done;
                } else if (subtasks.get(k).getStatus().equals("NEW")) {
                    ++statusNew;
                }
            }
        }
        if ((statusNew == epic.getSubtaskID().size()) || (epic.getSubtaskID().isEmpty())) {
            epic.setStatus("NEW");
        } else if (done == epic.getSubtaskID().size()) {
            epic.setStatus("DONE");
        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manager manager = (Manager) o;
        return generateIDForTask == manager.generateIDForTask && generateIDForEpic == manager.generateIDForEpic
                && generateIDForSubtask == manager.generateIDForSubtask && Objects.equals(tasks, manager.tasks)
                && Objects.equals(subtasks, manager.subtasks) && Objects.equals(epics, manager.epics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tasks, subtasks, epics, generateIDForTask, generateIDForEpic, generateIDForSubtask);
    }

    public void menuForDelete() {
        System.out.println(DELETE_TASK);
        System.out.println(DELETE_SUBTASK);
        System.out.println(DELETE_EPIC);
        System.out.println(WARNING);
    }

    public void deleteTask(int uniqueID) {
        tasks.remove(uniqueID);
        System.out.println(TASK_DELETED);
    }

    public void deleteSubtask(int uniqueID) {
        subtasks.remove(uniqueID);
        System.out.println(SUBTASK_DELETED);
    }

    public void deleteEpic(int uniqueID) {
        for (Integer k : epics.get(uniqueID).getSubtaskID()) {
            subtasks.remove(k);
        }
        epics.remove(uniqueID);
        System.out.println(EPIC_AND_SUBTASKS_DELETED);
    }

    public void printMenu() {
        System.out.println(CHOOSE_COMMAND);
        System.out.println(CREATE_TASK);
        System.out.println(PRINT_ALL_TASKS);
        System.out.println(REMOVE_ALL_TASKS);
        System.out.println(PRINT_TASK);
        System.out.println(UPDATE_TASK);
        System.out.println(REMOVE_TASK);
        System.out.println(PRINT_SUBTASKS_FOR_EPIC);
        System.out.println(EXIT);
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

