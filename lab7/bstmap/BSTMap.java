package bstmap;

import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private Node root; //创建根节点
    private int size = 0; // 树尺寸

    /**
     * Removes all of the mappings from this map.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        return containKey(root, key);
    }

    private boolean containKey(Node p, K key) {
        if (p == null) {
            return false;
        }
        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            return containKey(p.left, key);
        } else if (cmp > 0) {
            return containKey(p.right, key);
        } else {
            return true;
        }
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        return get(root, key);
    }

    private V get(Node p, K key) {
        if (p == null) {
            return null;
        }
        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            return get(p.left, key);
        } else if (cmp > 0) {
            return get(p.right, key);
        } else return p.value;
    }

    /* Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        root = put(root, key, value);
        size += 1;
    }

    private Node put(Node p, K key, V value) {
        if (p == null) {
            return new Node(key, value);
        }
        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            p.left = put(p.left, key, value);
        } else if (cmp > 0) {
            p.right = put(p.right, key, value);
        }
        return p;
    }

    public void printInOrder() {
        StringBuilder result = new StringBuilder();
        printInOrder(root, result);
        System.out.println(result.toString());
    }

    /* 按左子树、节点、右子树的顺序打印值 */
    private void printInOrder(Node p, StringBuilder s) {
        if (p == null) {
            return;
        }
        printInOrder(p.left, s);
        s.append(p.key);
        printInOrder(p.right, s);
    }

    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    public Set<K> keySet() {
        HashSet<K> hash = new HashSet<>();
        addKey(root,hash);
        return hash;
    }

    private void addKey(Node p, Set<K> set){
        if (p == null){
            return;
        }
        set.add(p.key);
        addKey(p.left,set);
        addKey(p.right,set);
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    /* 迭代器方法参考chatgpt */
    public Iterator<K> iterator() {
        return new BSTMapIter();
    }

    /* 创建嵌套类节点以创建二叉树 */
    private class Node {
        private final K key; //键
        private final V value; // 键对应的值
        private Node left, right; // 左子树和右子树

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private class BSTMapIter implements Iterator<K> {
        private Node curr;
        private final Stack<Node> stack;

        public BSTMapIter() {
            stack = new Stack<>();
            pushLeftChildren(root);
        }

        private void pushLeftChildren(Node p) {
            while (p != null) {
                stack.push(p);
                p = p.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            curr = stack.pop();
            pushLeftChildren(curr.right);
            return curr.key;
        }

    }

    public static void main(String[] args) {
        BSTMap<Integer,Integer> a =  new BSTMap<>();
        a.put(2,1);
        a.put(1,1);
        a.put(3,1);
        a.printInOrder();
    }
}