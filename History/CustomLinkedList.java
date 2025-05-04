package History;

import Tasks.AllTasks;

import java.util.Map;
import java.util.LinkedHashMap;

public class CustomLinkedList<T extends AllTasks> {
    public Node<T> head;
    public Node<T> tail;
    private int size;

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

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsID(int taskID) {
        boolean contains = false;
        for (Integer key : nodes.keySet()) {
            if (key == taskID) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    public int getSize() {
        return size;
    }

    public String returnHistory() {
        int i = 1;
        String result = "";
        for (Node<T> node: nodes.values()) {
            result = result + (i) + ". " + node.task.toString() + "\n";
            i++;
        }
        return result;
    }
}
