public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = Managers.getInMemoryTaskManager(Managers.getDefaultHistory());
        manager.runApp();
    }
}