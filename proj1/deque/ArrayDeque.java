package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T> {
    private T[] items;
    private int size;
    private int beg;  // beg表示首前
    private int end;  // end表示尾后
    private int RFACTOR = 2;

    private void resize(int capacity) {
        if (capacity < size) {
            return;
        }
        T[] newArr = (T[]) new Object[capacity];
        int pos = (beg + 1) % items.length;
        for (int i = 0; i < size; i++) {
            newArr[i] = items[pos];
            pos = (pos + 1) % items.length;
        }
        items = newArr;
        beg = capacity - 1;
        end = size;
    }

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        beg = 0;
        end = 1;
    }

    public void addFirst(T item) {
        if (size == items.length) {
            resize(RFACTOR * size);
        }
        items[beg] = item;
        beg = (beg + items.length - 1) % items.length;
        size++;
    }

    public void addLast(T item) {
        if (size == items.length) {
            resize(RFACTOR * size);
        }
        items[end] = item;
        end = (end + 1) % items.length;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int pos = (beg + 1) % items.length;
        for (int i = 0; i < size; i++) {
            System.out.print(items[pos] + " ");
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        beg = (beg + 1) % items.length;
        T res = items[beg];
        items[beg] = null;
        size--;
        if (size <= items.length / 4 && items.length >= 8) {
            resize(items.length / 2);
        }
        return res;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        end = (end + items.length - 1) % items.length;
        T res = items[end];
        items[end] = null;
        size--;
        return res;
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        }
        return items[(beg + index + 1) % items.length];
    }

    public Iterator<T> iterator() {
        return new ADIterator();
    }

    private class ADIterator implements Iterator<T> {
        private int pos;
        public ADIterator() {
            pos = (beg + 1) % items.length;  // 指向第一个元素
        }

        public boolean hasNext() {
            return pos != end;
        }

        public T next() {
            if (pos == end) {
                return null;
            }
            T res = items[pos];
            pos = (pos + 1) % items.length;
            return res;
        }
    }

    public static void main(String[] args) {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.addFirst(10);
        ad.addLast(50);
        ad.addLast(100);
        ad.addFirst(0);
        for (Integer x: ad) {
            System.out.println(x);
        }
    }
}
