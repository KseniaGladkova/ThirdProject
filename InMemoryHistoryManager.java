
public class InMemoryHistoryManager implements HistoryManager {

    CustomLinkedList<AllTasks> linkedList = new CustomLinkedList<>();

    @Override
    public void add(AllTasks tasks) {
        if (linkedList.size >= 11) {
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
    public CustomLinkedList<AllTasks> getHistory() {
        return linkedList;
    }
}