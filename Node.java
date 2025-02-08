import java.util.Objects;

public class Node<T extends AllTasks> {

    public T task;
    public Node<T> prev;
    public Node<T> next;

    public Node(T task) {
        this.task = task;
        this.prev = null;
        this.next = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<T> node = (Node<T>) o;
        return Objects.equals(task, node.task) && Objects.equals(prev, node.prev) && Objects.equals(next, node.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, prev, next);
    }

    @Override
    public String toString() {
        return task.toString();
    }
}
