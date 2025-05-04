package Main;

import TasksManagers.*;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws NumberFormatException, NullPointerException {
        while (true) {
            try {
                System.out.println(Constants.MENU_FOR_MAIN);
                int point = Integer.parseInt(scanner.nextLine());
                switch (point) {
                    case 1:
                    Managers.getFileBackedTasksManager().runApp();
                        break;
                    case 2:
                        Managers.getInMemoryTaskManager(Managers.getDefaultHistory()).runApp();
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println(Constants.NOT_COMMAND);
                        break;
                }
            } catch (NumberFormatException exception) {
                System.out.println(Constants.PRINT_NUMBER);
                continue;
            } catch (NullPointerException exception ) {
                System.out.println(exception.getMessage());
                continue;
            } catch (IOException exception) {
                System.out.println(Constants.IO_EXCEPTION_FOR_READ_FILE);
                continue;
            }
        }
    }
}
