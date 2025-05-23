package Main;

public class Constants {

    public static final String CHOOSE_COMMAND = "Выберите команду:";
    public static final String CREATE_TASK = "1 - Создать задачу";
    public static final String PRINT_ALL_TASKS = "2 - Получить список всех задач";
    public static final String REMOVE_ALL_TASKS = "3 - Удалить все задачи";
    public static final String PRINT_TASK = "4 - Получить задачу по идентификатору";
    public static final String UPDATE_TASK = "5 - Изменить задачу";
    public static final String REMOVE_TASK = "6 - Удалить задачу";
    public static final String PRINT_SUBTASKS_FOR_EPIC = "7 - Получить списки подзадач для эпиков";
    public static final String TAKE_HISTORY = "8 - Получить историю просмотров";
    public static final String PRINT_TASKS_WITH_TIME = "9 - Получить список задач в порядке выполнения";
    public static final String EXIT = "10 - Покинуть меню";
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
    public static final String NOT_FOUND = "Не найдено. Попробуйте снова";
    public static final String PRINT_ID = "Введите идентификатор задачи";
    public static final String CHOOSE_TYPE_FOR_CHANGE = "1 - изменить задачу;" + "\n" +
            "2 - изменить подзадачу;" + "\n" + "3 - изменить эпик;" + "\n" + "Изменить статус вручную можно только " +
            "для эпика без подзадач";
    public static final String PRINT_STATUS = "Измените статус. Доступные варианты: NEW/IN_PROGRESS/DONE ";
    public static final String TASK_CHANGED = "Задача изменена";
    public static final String SUBTASK_CHANGED = "Подзадача изменена";
    public static final String EPIC_CHANGED = "Эпик изменён";
    public static final String DELETE_TASK = "1 - Удалить задачу";
    public static final String DELETE_SUBTASK = "2 - Удалить подзадачу";
    public static final String DELETE_EPIC = "3 - Удалить эпик";
    public static final String WARNING = "При удалении эпика входящие в него подзадачи будут удалены!";
    public static final String TASK_DELETED = "Задача удалена";
    public static final String SUBTASK_DELETED = "Подзадача удалена";
    public static final String EPIC_AND_SUBTASKS_DELETED = "Эпик и входящие в него подзадачи удалены";
    public static final String HISTORY = "История просмотров: ";
    public static final String HISTORY_IS_EMPTY = "История просмотров пуста.";
    public static final String ID_IS_NOT_EXIST = "Задачи с таким идентификатором не существует";
    public static final String ID_FOR_CHANGE_TASK_IS_NOT_EXIST = "Задача не найдена в списке. Попробуйте снова";
    public static final String ID_FOR_CHANGE_SUBTASK_IS_NOT_EXIST = "Подзадача не найдена в списке. Попробуйте снова";
    public static final String ID_FOR_CHANGE_EPIC_IS_NOT_EXIST = "Эпик не найден в списке. Попробуйте снова";
    public static final String MENU_FOR_MAIN = "Выберите команду: " + "\n" + "1 - Запустить программу с " +
            "автосохранением в файл " + "\n" + "2 - Запустить программу без автосохранения в файл\n"
            + "3 - Выйти из приложения";
    public static final String IO_EXCEPTION_FOR_SAVE = "Произошла ошибка при попытке сохранения в файл";
    public static final String PRINT_FILE_NAME = "Введите имя файла в формате \"имя файла\".csv";
    public static final String IO_EXCEPTION_FOR_READ_FILE = "Произошла ошибка при чтении файла";
    public static final String PRINT_DURATION = "Укажите продолжительность выполнения в минутах:";
    public static final String PRINT_START_TIME = "Укажите дату старта в формате \"dd.MM.yyyy,HH:mm\"";
    public static final String CROSSING_EXISTS = "Найдено пересечение";
}
