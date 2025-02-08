import java.util.Map;
import java.util.HashMap;

public class CustomLinkedList <T extends AllTasks> {
    public Node<T> head;
    public Node<T> tail;
    public int size = 0;

    Map<Integer, Node<T>> nodes = new HashMap<>();

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

    public void removeNode(Node<T> node) {
        if (node.equals(head)) {
            deleteHead();
        } else if (node.equals(tail)) {
            deleteTail();
        } else {
            nodes.remove(node.task.getId());
            node.prev.next = node.next;
            node.next.prev = node.prev;
            node.prev = null;
            node.next = null;
            size--;
        }
    }

    public void deleteHead() {
        nodes.remove(head.task.getId());
        if (head.equals(tail)) {
            head = null;
            tail = null;
        } else {
            Node<T> newHead = head.next;
            head.next = null;
            newHead.prev = null;
            head = newHead;
        }
        size--;
    }

    public void deleteTail() {
        nodes.remove(tail.task.getId());
        if (head.equals(tail)) {
            head = null;
            tail = null;
        } else {
            Node<T> newTail = tail.prev;
            tail.prev = null;
            newTail.next = null;
            tail = newTail;
        }
        size--;
    }

    public void clear() {
        head.next = null;
        tail.prev = null;
        this.head = null;
        this.tail = null;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i <= size ; i++) {
            result = nodes.values() + ", ";
        }
        return result;
    }
}
