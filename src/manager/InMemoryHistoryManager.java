package manager;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> receivedTasks = new HashMap<>();
    private Node head;
    private Node tail;

    private static class Node {
        private Task task;
        private Node prev;
        private Node next;

        public Node(Node prev, Task task, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

    private void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if(oldTail == null) {
            head = newNode;
            receivedTasks.put(task.getId(), newNode);
        } else {
            oldTail.next = newNode;
            receivedTasks.put(task.getId(), newNode);
        }
    }

    private void removeNode(Node node) {
        if (node != null) {
            final Node next = node.next;
            final Node prev = node.prev;
            node.task = null;
            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (head == node && tail !=node) {
                head = next;
                head.prev = null;
            } else if (head != node && tail == node) {
                tail = prev;
                tail.next = null;
            } else {
                prev.next = next;
                next.prev = prev;
            }
        }
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node currentNode = head;
        while (currentNode != null) {
            tasks.add(currentNode.task);
            currentNode = currentNode.next;
        }
        return tasks;
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            linkLast((task));
        }
    }
    
    @Override
    public void remove(int id) {
        removeNode(receivedTasks.get(id));
    }
    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

}
