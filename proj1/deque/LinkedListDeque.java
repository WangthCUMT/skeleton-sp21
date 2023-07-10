package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    private final IntNode sentinelfirst;
    private final IntNode sentinellast;
    private int size;

    public LinkedListDeque() {
        sentinelfirst = new IntNode(null, null, null); // 前哨兵节点
        sentinellast = new IntNode(null, null, null); // 后哨兵节点
        sentinelfirst.next = sentinellast;
        sentinellast.prev = sentinelfirst;
        size = 0;
    }

    public void addFirst(T item) {
        IntNode newInt = new IntNode(sentinelfirst, item, sentinelfirst.next);
        sentinelfirst.next.prev = newInt;
        sentinelfirst.next = newInt;
        size += 1;
    }

    public void addLast(T item) {
        IntNode newInt = new IntNode(sentinellast.prev, item, sentinellast);
        sentinellast.prev.next = newInt;
        sentinellast.prev = newInt;
        size += 1;
    }

    public int size() {
        return size;
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        } else {
            size -= 1;
            T a = sentinelfirst.next.item;
            sentinelfirst.next = sentinelfirst.next.next;
            sentinelfirst.next.prev = sentinelfirst;
            return a;
        }
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            size -= 1;
            T a = sentinellast.prev.item;
            sentinellast.prev = sentinellast.prev.prev;
            sentinellast.prev.next = sentinellast;
            return a;
        }
    }

    public T get(int index) {
        if (index > size() - 1 || index < 0) {
            return null;
        } else {
            IntNode temp = sentinelfirst.next;
            while (index != 0) {
                temp = temp.next;
                index -= 1;
            }
            return temp.item;
        }
    }

    private T getRecursiveNode(IntNode p, int index) {
        if (index == 0) {
            return p.item;
        } else {
            return getRecursiveNode(p.next, index - 1);
        }
    }

    public T getRecursive(int index) {
        if (index > size() - 1 || index < 0) {
            return null;
        } else {
            return getRecursiveNode(sentinelfirst.next, index);
        }
    }

    public void printDeque() {
        IntNode temp = sentinelfirst.next;
        while (temp.next != null) {
            System.out.print(temp.item + " ");
            temp = temp.next;
        }
        System.out.println();
    }

    // 创建迭代器
    public Iterator<T> iterator() {
        return new LLDequeSetIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> other = (Deque<T>) o;
        if (this.size() != other.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            if (!(this.get(i).equals(other.get(i)))) {
                return false;
            }
        }
        return true;
    }

    private class IntNode {
        private T item;
        private IntNode next;
        private IntNode prev;

        IntNode(IntNode p, T i, IntNode n) {
            item = i;
            prev = p;
            next = n;
            // System.out.println(size);
        }
    }

    private class LLDequeSetIterator implements Iterator<T> {
        private int pos;

        LLDequeSetIterator() {
            pos = 0;
        }

        @Override
        public boolean hasNext() {  //这个位置是否有值？
            return pos < size();
        }

        @Override
        public T next() {  // 这个位置的值是什么？
            T returnitem = get(pos);
            pos += 1;
            return returnitem;
        }
    }
}
