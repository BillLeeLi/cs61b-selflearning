package deque;

import java.lang.reflect.Array;

public class SimpleDeque<T> {
    private T[] items;
    private int size;
    private int RFACTOR = 10;

    public SimpleDeque() {
        items = (T[]) new Object[10];
        size = 0;
    }

    public SimpleDeque(T x) {
        items = (T[]) new Object[10];
        items[0] = x;
        size = 1;
    }

    public void addFirst(T item) {
        if (size == items.length) {
            T[] newArr = (T[]) new Object[RFACTOR * size];
            System.arraycopy(items, 0, newArr, 0, size);
            items = newArr;
        }
        System.arraycopy(items, 0, items, 1, size);
        items[0] = item;
        size++;
    }

    public void addLast(T item) {
        if (size == items.length) {
            T[] newArr = (T[]) new Object[RFACTOR * size];
            System.arraycopy(items, 0, newArr, 0, size);
            items = newArr;
        }
        items[size++] = item;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T res = items[0];
        System.arraycopy(items, 1, items, 0, size - 1);
        items[size - 1] = null;
        size--;
        return res;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T res = items[size - 1];
        items[size - 1] = null;
        size--;
        return res;
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        }
        return items[index];
    }
}
