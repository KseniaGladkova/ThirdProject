import java.util.Scanner;

public class Manager {
    static Scanner scanner = new Scanner(System.in);
    public static final String CHOOSE_COMMAND = "Выберите команду: ";
    public static final String CREATE_TASK = "1 - Создать задачу";
    public static final String PRINT_ALL_TASKS = "2 - Получить список всех задач";
    public static final String REMOVE_ALL_TASKS = "3 - Удалить все задачи";
    public static final String PRINT_TASK = "4 - Получить задачу по идентификатору";
    public static final String UPDATE_TASK = "5 - Изменить задачу";
    public static final String REMOVE_TASK = "6 - Удалить задачу";
    public static final String EXIT = "7 - Выйти из приложения";
    public static final String NOT_COMMAND = "Такой команды нет";
    public static final String PRINT_NUMBER = "Введите целое число";
    public static final String PRINT_NAME = "Введите имя задачи: ";
    public static final String PRINT_DESCRIPTION = "Описание задачи: ";
    public static final String PRINT_TYPE_OF_TASK = "Выберите тип задачи: 1 - задача; 2 - эпик; 3 - подзадача;";

    public void runApp() {
        while (true) {
            printMenu();
            String command = scanner.nextLine();
            boolean isNum = isNumeric(command);
            if (isNum) {
                int comm = Integer.parseInt(command);
                if (comm == 1) {
                    System.out.println(PRINT_NAME);
                    String name = scanner.nextLine();
                    System.out.println(PRINT_DESCRIPTION);
                    String description = scanner.nextLine();
                    System.out.println(PRINT_TYPE_OF_TASK);
                    String type = scanner.nextLine();
                    boolean isNumb = isNumeric(type);
                    if (!isNumb){
                        System.out.println(PRINT_NUMBER);
                        continue;
                    } else {
                        int typeOfTask = Integer.parseInt(type);
                        switch (typeOfTask){
                            case 1:
                                Task task = new Task(name, "New", description);
                                task.createTask(task);
                                break;
                            case 2:
                                System.out.println("2");
                                break;
                            case 3:
                                System.out.println("3");
                                break;
                            default:
                                System.out.println(NOT_COMMAND);
                                break;
                        }
                        continue;
                    }
                }
                if (comm == 2) {
                    System.out.println("2");
                    continue;
                }
                if (comm == 3) {
                    System.out.println("3");
                    continue;
                }
                if (comm == 4) {
                    System.out.println("4");
                    continue;
                }
                if (comm == 5) {
                    System.out.println("5");
                    continue;
                }
                if (comm == 6) {
                    System.out.println("6");
                    continue;
                }
                if (comm == 7) {
                    System.out.println("7");
                    break;
                }
                System.out.println(NOT_COMMAND);
            } else {
                System.out.println(PRINT_NUMBER);
            }
        }
    }

    public void printMenu() {
        System.out.println(CHOOSE_COMMAND);
        System.out.println(CREATE_TASK);
        System.out.println(PRINT_ALL_TASKS);
        System.out.println(REMOVE_ALL_TASKS);
        System.out.println(PRINT_TASK);
        System.out.println(UPDATE_TASK);
        System.out.println(REMOVE_TASK);
        System.out.println(EXIT);
    }

    public static boolean isNumeric (String command) {
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
