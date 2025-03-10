import java.util.Map;
import java.util.LinkedHashMap;

public class CustomLinkedList <T extends AllTasks> {
    public Node<T> head;
    public Node<T> tail;
    public int size = 0;

    Map<Integer, Node<T>> nodes = new LinkedHashMap<>();

    public CustomLinkedList() {
        this.head = null;
        this.tail = null;
    }

    public void linkLast(T task) {
        Node<T> node = new Node<>(task);
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            node.prev = tail;
            tail.next = node;
            tail = node;
        }
        size++;
        nodes.put(task.getId(), node);

    }

    public void removeNode(Node<T> node) throws NullPointerException {
        try {
            if (node.prev == null) {
                deleteHead();
            } else if (node.next == null) {
                deleteTail();
            } else {
                nodes.remove(node.task.getId());
                node.prev.next = node.next;
                node.next.prev = node.prev;
                size--;
            }
        } catch (NullPointerException exception) {
           return;
        }
    }

    public void deleteHead() throws NullPointerException {
        try {
            Node<T> temp = new Node<>(null);
            if (head.equals(tail)) {
                clear();
            } else {
                temp = head;
                head = head.next;
                head.prev = null;
                temp.next = null;
            }
            if (temp.task != null) {
                nodes.remove(temp.task.getId());
                size--;
            }
        } catch (NullPointerException exception) {
            return;
        }
    }

    public void deleteTail() throws NullPointerException {
        try {
            Node<T> temp = new Node<>(null);
            if (head.equals(tail)) {
                clear();
            } else {
                temp = tail;
                tail = tail.prev;
                tail.next = null;
                temp.prev = null;
            }
            if (temp.task != null) {
                nodes.remove(temp.task.getId());
                size--;
            }
        } catch (NullPointerException exception) {
            return;
        }
    }

    public void clear() {
        this.head = null;
        this.tail = null;
        nodes.clear();
        size = 0;
    }

    public String toStringForFiles() {
        String result = "";
        for(Integer key: nodes.keySet()) {
            result = result + key + ",";
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i <= size ; i++) {
            result = nodes.values() + ",";
        }
        return result;
    }
}
