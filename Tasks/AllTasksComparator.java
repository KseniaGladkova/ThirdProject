package Tasks;

import java.util.Comparator;

public class AllTasksComparator implements Comparator<AllTasks> {

    @Override
    public int compare(AllTasks task1, AllTasks task2) {
        int result = 0;
        if (task1.getStartTime().isBefore(task2.getStartTime())) {
            result = -1;
        } else if (task1.getStartTime().isAfter(task2.getStartTime())) {
            result = 1;
        }
        return result;
    }

}
