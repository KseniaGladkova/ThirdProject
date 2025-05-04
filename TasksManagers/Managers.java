package TasksManagers;
import History.*;
import Main.Constants;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;

public class Managers {
    static Scanner scanner = new Scanner(System.in);

    public static InMemoryTaskManager getInMemoryTaskManager(HistoryManager historyManager) {
        return new InMemoryTaskManager(historyManager);
    }

    public static FileBackedTasksManager getFileBackedTasksManager() throws IOException {
        System.out.println(Constants.PRINT_FILE_NAME);
        String name = scanner.nextLine();
        String filePath = "resources/" + name;
        File file = new File(filePath);
        return FileBackedTasksManager.loadFromFile(file);
    }
    
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

