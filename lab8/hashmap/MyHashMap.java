package hashmap;

import java.util.*;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 * <p>
 * Assumes null keys will never be inserted, and does not resize down upon remove().
 *
 * @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private HashSet<K> keys;
    private double maxLoad = 0.75;
    private int capacity = 16;  // 容量
    private int FACTOR = 2;

    /**
     * Constructors
     */
    public MyHashMap() {
        buckets = new Collection[capacity];
        keys = new HashSet<>();
    }

    public MyHashMap(int initialSize) {
        capacity = initialSize;
        buckets = new Collection[capacity];
        keys = new HashSet<>();
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad     maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        capacity = initialSize;
        this.maxLoad = maxLoad;
        buckets = new Collection[capacity];
        keys = new HashSet<>();
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`remove` method)
     * 3. Iterate through items (`iterator` method)
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();  // 这个方法不应该被直接使用，而是应该使用重载版本。默认版本使用ArrayList
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    /**
     * Removes all the mappings from this map.
     */
    @Override
    public void clear() {
        buckets = createTable(capacity);  // 创建一个新的表
        keys = new HashSet<>();  // 创建新的键集合(初始为空)
    }

    /**
     * Returns true if this map contains a mapping for the specified key.
     */
    @Override
    public boolean containsKey(K key) {
        return keys.contains(key);
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (!containsKey(key)) {  // 对不存在的键直接返回null
            return null;
        }

        int index = Math.floorMod(key.hashCode(), capacity);
        for (Node x : buckets[index]) {
            if (x.key.equals(key)) {
                return x.value;
            }
        }
        return null;
    }

    /**
     * Returns the number of key-value mappings in this map.
     */
    @Override
    public int size() {
        return keys.size();
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    @Override
    public void put(K key, V value) {
        int index = Math.floorMod(key.hashCode(), capacity);
        if (buckets[index] == null) {
            buckets[index] = createBucket();
        }

        if (!containsKey(key)) {  // 键之前不存在
            buckets[index].add(createNode(key, value));
            keys.add(key);
        } else {
            for (Node x : buckets[index]) {
                if (x.key.equals(key)) {
                    x.value = value;
                    break;
                }
            }

        }

        if ((double) keys.size() / capacity > maxLoad) {
            resize(FACTOR * capacity);
        }
    }

    /**
     * Returns a Set view of the keys contained in this map.
     */
    @Override
    public Set<K> keySet() {
        return keys;
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public V remove(K key) {
        throw new NoSuchElementException("remove(K key) not implemented");
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    @Override
    public V remove(K key, V value) {
        throw new NoSuchElementException("remove(K key, V value) not implemented");
    }

    @Override
    public Iterator<K> iterator() {
        return keys.iterator();
    }

    private void resize(int newCapacity) {
        capacity = newCapacity;
        keys = new HashSet<>();
        Collection<Node>[] oldTable = buckets;  // 旧表
        buckets = createTable(newCapacity);     // 创建一个新表
        for (Collection<Node> bucket: oldTable) {
            if (bucket != null) {
                for (Node x: bucket) {
                    put(x.key, x.value);
                }
            }
        }
    }
}
