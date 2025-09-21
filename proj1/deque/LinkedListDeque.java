package deque;
import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T> {
    // 采用循环链表、双向链接的实现方式
    private static class Node<T> {
        public T item;
        public Node<T> next;  // 下一个节点
        public Node<T> prev;  // 上一个节点

        public Node(T i, Node<T> n, Node<T> p) {
            item = i;
            next = n;
            prev = p;
        }
    }

    private Node<T> sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node<>(null, null, null);  // 注意这时sentinel是null，所以不能在构造函数中把prev和next设为sentinel
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public LinkedListDeque(T first) {
        Node<T> firstNode = new Node<>(first, null, null);
        sentinel = new Node<>(null, firstNode, firstNode);
        firstNode.prev = firstNode.next = sentinel;
        size = 1;
    }

    public void addFirst(T item) {
        Node<T> newNode = new Node<>(item, sentinel.next, sentinel);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size++;
    }

    public void addLast(T item) {
        Node<T> newNode = new Node<>(item, sentinel, sentinel.prev);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node<T> n = sentinel.next;
        while (n != sentinel) {
            System.out.print(n.item + " ");
            n = n.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node<T> discardedNode = sentinel.next;
        sentinel.next = discardedNode.next;
        sentinel.next.prev = sentinel;
        size--;
        return discardedNode.item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node<T> discardedNode = sentinel.prev;
        sentinel.prev = discardedNode.prev;
        sentinel.prev.next = sentinel;
        size--;
        return discardedNode.item;
    }

    public T get(int index) {
        if (size <= index) {
            return null;
        }

        Node<T> n;
        if (index < size / 2) {
            // 正向查找
            n = sentinel.next;
            for (int i = 0; i < index; i++) {
                n = n.next;
            }
            return n.item;
        } else {
            n = sentinel.prev;
            for (int i = size - 1; i > index; i--) {
                n = n.prev;
            }
        }
        return n.item;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (o instanceof LinkedListDeque) {
            if (size != ((LinkedListDeque<?>) o).size) {
                return false;
            }

            Node<T> n1 = sentinel.next;
            Node<T> n2 = (Node<T>) ((LinkedListDeque<?>) o).sentinel.next;
            while (n1 != sentinel) {
                if (!n1.item.equals(n2)) {
                    return false;
                }
                n1 = n1.next;
                n2 = n2.next;
            }
            return true;
        }
        return false;
    }

    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        } else {
            return getRecHelper(index, sentinel.next);
        }
    }

    private T getRecHelper(int index, Node<T> beg) {
        if (beg == null) {
            return null;
        } else if (index == 0) {
            return beg.item;
        } else {
            return getRecHelper(index - 1, beg.next);
        }
    }
//    public Iterator<T> iterator();

    @Override
    public Iterator<T> iterator() {
        return new LLDIterator();
    }

    private class LLDIterator implements Iterator<T> {
        private Node<T> pos;
        public LLDIterator() {
            pos = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return pos != sentinel;
        }

        @Override
        public T next() {
            if (pos == sentinel) {
                return null;
            }
            T res = pos.item;
            pos = pos.next;
            return res;
        }
    }

    public static void main(String[] args) {
        LinkedListDeque<Integer> lld = new LinkedListDeque<>(37);
        lld.addFirst(210);
        lld.addLast(486);
        lld.addFirst(6);
        lld.addFirst(77);
        for (Integer x: lld) {
            System.out.println(x);
        }
    }
}
