package deque;

import org.w3c.dom.Node;

public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int beg;  // beg表示首前
    private int end;  // end表示尾后
    private int RFACTOR = 2;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        beg = 0;
        end = 1;
    }

    public void addFirst(T item) {
        if (size == items.length) {
            T[] newArr = (T[]) new Object[RFACTOR * size];
            int pos = (beg + 1) % items.length;
            for (int i = 0; i < size; i++) {
                newArr[i] = items[pos];
                pos = (pos + 1) % items.length;
            }
            items = newArr;
            beg = items.length - 1;
            end = size;
        }
        items[beg] = item;
        beg = (beg + items.length - 1) % items.length;
        size++;
    }

    public void addLast(T item) {
        if (size == items.length) {
            T[] newArr = (T[]) new Object[RFACTOR * size];
            int pos = (beg + 1) % items.length;
            for (int i = 0; i < size; i++) {
                newArr[i] = items[pos];
                pos = (pos + 1) % items.length;
            }
            items = newArr;
            beg = items.length - 1;
            end = size;
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

    //    public Iterator<T> iterator();
}
