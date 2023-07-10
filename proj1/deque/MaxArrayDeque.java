package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private final Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }

    /**
     * returns the maximum element in the deque as governed by the previously given Comparator.
     * If the MaxArrayDeque is empty, simply return null.
     */
    public T max() {
        if (size() == 0) {
            return null;
        }
        T max = get(0);
        for (T cur : this) {
            if (comparator.compare(cur, max) > 0) {
                max = cur;
            }
        }
        return max;
    }

    /**
     * returns the maximum element in the deque as governed by the parameter Comparator c.
     * If the MaxArrayDeque is empty, simply return null.
     */
    public T max(Comparator<T> c) {
        if (size() == 0) {
            return null;
        }
        T max = get(0);
        for (T cur : this) {
            if (c.compare(cur, max) > 0) {
                max = cur;
            }
        }
        return max;
    }
}
