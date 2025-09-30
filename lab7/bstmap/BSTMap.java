package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<KType extends Comparable<KType>, VType> implements Map61B<KType, VType> {
    private Node<KType, VType> root;

    public BSTMap() {
        root = null;
    }

    /* Removes all the mappings from this map. */
    @Override
    public void clear() {
        root = null;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(KType key) {
        Node<KType, VType> curr = root;
        while (curr != null) {
            int cmp = curr.key.compareTo(key);
            if (cmp == 0) {
                return true;
            } else if (cmp < 0) {
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        return false;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public VType get(KType key) {
        return get(key, root);
    }

    private VType get(KType key, Node<KType, VType> r) {
        if (r == null) {
            return null;
        }

        int cmp = r.key.compareTo(key);
        if (cmp == 0) {
            return r.val;
        } else if (cmp < 0) {
            return get(key, r.right);
        } else {
            return get(key, r.left);
        }
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size(root);
    }

    private int size(Node<KType, VType> r) {
        if (r == null) {
            return 0;
        }
        return r.size;
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(KType key, VType value) {
        root = put(key, value, root);
    }

    private Node<KType, VType> put(KType key, VType value, Node<KType, VType> r) {
        if (r == null) {
            r = new Node<>(key, value);
        } else {
            int cmp = r.key.compareTo(key);
            if (cmp < 0) {
                r.right = put(key, value, r.right);
            } else if (cmp > 0) {
                r.left = put(key, value, r.left);
            }
        }
        r.size = size(r.left) + size(r.right) + 1;  // 注意插入有可能失败，所以不可以直接r.size++
        return r;
    }

    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    @Override
    public Set<KType> keySet() {
        throw new UnsupportedOperationException("keySet() not implemented");
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public VType remove(KType key) {
        throw new UnsupportedOperationException("keySet() not implemented");
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public VType remove(KType key, VType value) {
        throw new UnsupportedOperationException("keySet() not implemented");
    }

    @Override
    public Iterator<KType> iterator() {
        throw new UnsupportedOperationException("keySet() not implemented");
    }

    public void printInOrder() {
        printInOrder(root);
        System.out.println();
    }

    private void printInOrder(Node<KType, VType> r) {
        if (r == null) {
            return;
        }
        printInOrder(r.left);
        System.out.print("("+ r.key + ", " + r.val + ") ");
        printInOrder(r.right);
    }

    private static class Node<KType, VType> {
        KType key;
        VType val;
        Node<KType, VType> left;
        Node<KType, VType> right;
        int size;

        public Node(KType k, VType v) {
            key = k;
            val = v;
            left = null;
            right = null;
            size = 1;
        }
    }

    public static void main(String[] args) {
        BSTMap<String, Integer> m = new BSTMap<>();
        m.put("hello", 5);
        m.put("world", 10);
        m.put("apple", 30);
        m.put("watermelon", 15);
        m.printInOrder();
    }
}
