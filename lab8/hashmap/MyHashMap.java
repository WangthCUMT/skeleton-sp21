package hashmap;

import java.util.*;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 * <p>
 * Assumes null keys will never be inserted, and does not resize down upon remove().
 *
 * @author Tonghui Wang
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
    // You should probably define some more!
    private final int Default_size = 16;
    private final double Default_loadFactor = 0.75;
    private final double MaxloadFactor;
    private int size;


    /**
     * Constructors
     */
    public MyHashMap() {
        this(16, 0.75);
        size = 0;
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75);
        size = 0;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad     maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        if (initialSize < 1 || maxLoad <= 0) {
            throw new IllegalArgumentException();
        }
        buckets = createTable(initialSize);
        MaxloadFactor = maxLoad;
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
        return new LinkedList<>();
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
        Collection<Node>[] table = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    public void clear() {
        buckets = createTable(Default_size);
        size = 0;
    }

    /**
     * Returns true if this map contains a mapping for the specified key.
     */
    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        if (getNode(key) == null) {
            return null;
        } else {
            return getNode(key).value;
        }
    }

    /**
     * Returns the number of key-value mappings in this map.
     */
    public int size() {
        return size;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    public void put(K key, V value) {
        Node node = getNode(key);
        Node add_node = createNode(key, value);
        int buckerindex = getIndex(key);
        if (node == null) {
            buckets[buckerindex].add(add_node);
            size += 1;
        } else {
            node.value = value;
        }
        if (reachedMaxLoad()) {
            resize(buckets.length * 2);
        }
    }

    private void resize(int capacity) {
        Collection<Node>[] tempBuckets = createTable(capacity);
        for (Collection<Node> bucket : buckets) {
            for (Node node : bucket) {
                int newIndex = getIndex(node.key, tempBuckets);
                tempBuckets[newIndex].add(node);
            }
        }
        buckets = tempBuckets;
    }

    /**
     * Returns the node that contains the input key, if no such node, return null
     */
    private Node getNode(K key) {
        int bucketindex = getIndex(key);
        return getNode(key, bucketindex);
    }

    private Node getNode(K key, int bucketindex) {
        for (Node node : buckets[bucketindex]) {
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Returns a Set view of the keys contained in this map.
     */
    public Set<K> keySet() {
        HashSet<K> hash = new HashSet<>();
        for (Collection<Node> bucket : buckets) {
            for (Node node : bucket) {
                hash.add(node.key);
            }
        }
        return hash;
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    public V remove(K key) {
        Node node = getNode(key);
        if (node == null){
            return null;
        }else {
            V tempvalue = node.value;
            int bucketIndex = getIndex(key);
            buckets[bucketIndex].remove(node);
            return tempvalue;
        }
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    public V remove(K key, V value) {
        Node node = getNode(key);
        if (node == null){
            return null;
        }else {
            if (value.equals(node.value)){
                int bucketIndex = getIndex(key);
                buckets[bucketIndex].remove(node);
                return value;
            }else {
                return null;
            }
        }
    }

    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    /**
     * Calculate the hashcode of given key
     */
    private int getIndex(K key) {
        return getIndex(key, buckets);
    }

    private int getIndex(K key, Collection<Node>[] hashtable) {
        int h = key.hashCode();
        return Math.floorMod(h, hashtable.length);
    }

    private boolean reachedMaxLoad() {
        return (double) (size / buckets.length) > MaxloadFactor;
    }
}
