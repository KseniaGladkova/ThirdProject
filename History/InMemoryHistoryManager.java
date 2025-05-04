package History;

import Tasks.AllTasks;

public class InMemoryHistoryManager implements HistoryManager {

    public CustomLinkedList<AllTasks> linkedList = new CustomLinkedList<>();

    @Override
    public void add(AllTasks tasks) {
        if (linkedList.getSize() >= 10) {
            linkedList.deleteHead();
        }
        if (linkedList.nodes.containsKey(tasks.getId())) {
            linkedList.removeNode(linkedList.nodes.get(tasks.getId()));
        }
        linkedList.linkLast(tasks);
    }

    @Override
    public void remove(int id) {
        linkedList.removeNode(linkedList.nodes.get(id));
    }

    @Override
    public void clearHistory() {
        linkedList.clear();
    }

    @Override
    public String getHistory() {
        return linkedList.returnHistory();
    }

    public String toStringForFiles() {
        String result = "h;";
        for (Integer key: linkedList.nodes.keySet()) {
            result = result + key + ";";
        }
        return result;
    }
}