package bstmap;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.HashSet;

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
        if (key == null) {
            throw new NoSuchElementException("put null pointer");
        }

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
            } else {
                r.val = value;
            }
        }
        r.size = size(r.left) + size(r.right) + 1;  // 注意插入有可能失败，所以不可以直接r.size++
        return r;
    }

    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    @Override
    public Set<KType> keySet() {
        Set<KType> res = new HashSet<>();
        keySetHelper(root, res);
        return res;
    }

    private void keySetHelper(Node<KType, VType> r, Set<KType> s) {
        if (r == null) {
            return;
        }

        s.add(r.key);
        keySetHelper(r.left, s);
        keySetHelper(r.right, s);
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public VType remove(KType key) {
        if (key == null) {
            throw new NoSuchElementException("remove null pointer");
        }

        VType res = get(key);  // 获取返回值
        root = remove(key, root);  // 删除节点
        return res;
    }

    private Node<KType, VType> remove(KType key, Node<KType, VType> r) {
        if (r == null) {  // 没有找到需要删除的元素
            return null;
        }

        int cmp = r.key.compareTo(key);
        if (cmp < 0) {
            r.right = remove(key, r.right);
        } else if (cmp > 0) {
            r.left = remove(key, r.left);
        } else {  // 要删除当前节点r
            if (r.left == null) {
                return r.right;
            } else if (r.right == null) {
                return r.left;
            } else {
                Node<KType, VType> t = r;
                r = getMin(t.right);
                r.right = removeMin(t.right);
                r.left = t.left;
            }
        }
        r.size = size(r.left) + size(r.right) + 1;
        return r;
    }

    private Node<KType, VType> getMin(Node<KType, VType> r) {
        if (r == null) {
            throw new NoSuchElementException("getMin() on empty tree");
        }

        Node<KType, VType> curr = r;
        while (curr.left != null) {
            curr = curr.left;
        }
        return curr;
    }

    private Node<KType, VType> removeMin(Node<KType, VType> r) {
        if (r == null) {
            throw new NoSuchElementException("removeMin() on empty tree");
        }

        if (r.left == null) {
            return r.right;
        }
        r.left = removeMin(r.left);
        r.size = size(r.left) + size(r.right) + 1;
        return r;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public VType remove(KType key, VType value) {
        if (key == null) {
            throw new NoSuchElementException("remove null pointer");
        }

        if (value != null && value.equals(get(key))) {
            return remove(key);
        }
        return null;
    }

    @Override
    public Iterator<KType> iterator() {
        return new BSTMapIterator();
    }

    public class BSTMapIterator implements Iterator<KType> {
        int index;
        public BSTMapIterator() {
            index = 0;
        }

        public boolean hasNext() {
            return index < size();
        }

        public KType next() {
            return getIndex(index++, root);
        }
    }

    private KType getIndex(int index, Node<KType, VType> r) {
        if (r == null) {
            throw new NoSuchElementException("getIndex on empty tree");
        } else if (index >= r.size) {
            throw new NoSuchElementException("getIndex: index should be smaller than tree size");
        } else if (index < 0) {
            throw new NoSuchElementException("getIndex: index should be larger than or equal to zero");
        }

        if (index < size(r.left)) {
            return getIndex(index, r.left);
        } else if (index > size(r.left)) {
            return getIndex(index - size(r.left) - 1, r.right);
        } else {
            return r.key;
        }
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
        for (String s: m) {
            System.out.println(s);
        }
    }
}
