import java.util.Map;
import java.util.HashMap;

public class CustomLinkedList <T extends AllTasks> {
    public Node<AllTasks> head;
    public Node<AllTasks> tail;
    public Node<AllTasks> task;
    public int size = 0;

    Map<Integer, Node<AllTasks>> nodes = new HashMap<>();

    public CustomLinkedList() {
        this.head = null;
        this.tail = null;
    }

    public void linkLast(AllTasks task) {
        Node<AllTasks> node = new Node<>(task);
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

    public void removeNode(Node<AllTasks> node) {
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
            Node<AllTasks> newHead = head.next;
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
            Node<AllTasks> newTail = tail.prev;
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
        return nodes.values() + "";
    }
}
