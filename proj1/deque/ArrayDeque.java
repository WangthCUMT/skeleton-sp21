package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int arraysize;
    private int size;
    private int nextfirst;
    private int nextlast;
    private int startindex;

    public ArrayDeque() {
        arraysize = 8;
        items = (T[]) new Object[arraysize];
        startindex = 5;
        nextfirst = 4;
        nextlast = 5;
        size = 0;
    }

    private boolean isfull() {
        return size == arraysize;
    }

    public void addLast(T item) {
        if (isfull()) {
            resizeup();
        }
        size += 1;
        items[nextlast] = item;
        nextlast = (nextlast + 1) % arraysize;
    }

    public void addFirst(T item) {
        if (isfull()) {
            resizeup();
        }
        size += 1;
        items[nextfirst] = item;
        nextfirst = (nextfirst - 1 + arraysize) % arraysize;
        startindex = (nextfirst + 1) % arraysize;
    }

    // 将数组的size扩大2倍
    private void resizeup() {
        int tempsize = size();
        arraysize *= 2;
        T[] a = (T[]) new Object[arraysize];
        for (int i = 0; i < tempsize; i++) {
            a[startindex + i] = items[(startindex + i) % (arraysize / 2)];
        }
        nextlast = startindex + tempsize;
        items = a;
    }

    // 将数组的size减少2倍
    private void resizedown() {
        int tempsize = size();
        arraysize /= 2;
        T[] a = (T[]) new Object[arraysize];
        for (int i = 0; i < tempsize; i++) {
            a[1 + i] = items[(startindex + i) % (arraysize * 2)];
        }
        startindex = 1;
        nextfirst = 0;
        nextlast = (startindex + tempsize) % arraysize;
        items = a;
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        } else {
            size -= 1;
            T a = items[startindex];
            items[startindex] = null;
            nextfirst = startindex;
            startindex = (startindex + 1) % arraysize;
            if ((double) size() / arraysize <= 0.25 && size() > 8) {
                resizedown();
            }
            return a;
        }
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            nextlast = (nextlast - 1 + arraysize) % arraysize;
            T a = items[nextlast];
            items[nextlast] = null;
            size -= 1;
            if ((double) size() / arraysize <= 0.25 && size() > 8) {
                resizedown();
            }
            return a;
        }
    }

    public T get(int index) {
        if (index < 0 || index > size() - 1) {
            return null;
        } else {
            return items[(startindex + index) % arraysize];
        }
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = 0; i < size(); i++) {
            System.out.print(items[(startindex + i) % arraysize] + " ");
        }
        System.out.println();
    }

    // 创建迭代器
    public Iterator<T> iterator() {
        return new ARDequeSetIterator();
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

    private class ARDequeSetIterator implements Iterator<T> {
        private int pos;

        ARDequeSetIterator() {
            pos = 0;
        }

        @Override
        public boolean hasNext() {  //这个位置是否有值？
            return pos < size();
        }

        public T next() {  // 这个位置的值是什么？
            T returnitem = get(pos);
            pos += 1;
            return returnitem;
        }
    }
}
