package deque;

public class ArrayDeque<T> {
    private T[] items;
    private int arraysize;
    private int size;
    private int nextfirst;
    private int nextlast;
    private int startindex;
    public ArrayDeque(){
        arraysize = 8;
        items = (T[]) new Object[arraysize];
        startindex = 5;
        nextfirst = 4;
        nextlast = 5;
        size = 0;
    }
    public ArrayDeque(T i){
        size = 1;
        arraysize = 8;
        items = (T[]) new Object[arraysize];
        startindex = 5;
        nextfirst = 4;
        nextlast = 5;
        items[nextlast] = i;
        nextlast += 1;
    }
    private boolean isfull(){
        return size == arraysize;
    }
    public boolean isEmpty(){
        return size == 0;
    }
    public void addLast(T i){
        if (isfull()){
            resizeup();
        }
        size += 1;
        items[nextlast] = i;
        nextlast = (nextlast + 1) % arraysize;
    }
    public void addFirst(T i){
        if (isfull()){
            resizeup();
        }
        size += 1;
        items[nextfirst] = i;
        nextfirst = (nextfirst - 1 + arraysize)% arraysize;
        startindex = (nextfirst + 1) % arraysize;
    }
    // 将数组的size扩大2倍
    private void resizeup(){
        int tempsize = size();
        arraysize *= 2;
        T[] a = (T[]) new Object[arraysize];
        for (int i = 0; i < tempsize;i++){
            a[startindex + i] = items[(startindex+i)%(arraysize/2)];
        }
        nextlast = startindex + tempsize;
        items = a;
    }
    // 将数组的size减少2倍
    private void resizedown(){
        int tempsize = size();
        arraysize /= 2;
        T[] a = (T[]) new Object[arraysize];
        for (int i = 0;i < tempsize;i++){
            a[1+i] = items[(startindex+i)%(arraysize*2)];
        }
        startindex = 1;
        nextfirst = 0;
        nextlast = (startindex + tempsize)% arraysize;
        items = a;
    }
    public T removeFirst(){
        if (isEmpty()){
            return null;
        }else {
            size -= 1;
            T a = items[startindex];
            items[startindex] = null;
            nextfirst = startindex;
            startindex = (startindex+1) % arraysize;
            if ((double) size() /arraysize<=0.25&& size() >8){
                resizedown();
            }
            return a;
        }
    }
    public T removeLast(){
        if (isEmpty()){
            return null;
        }else {
            T a = items[(startindex - 1 + size())%arraysize];
            items[(startindex - 1 + size())%arraysize] = null;
            nextlast = (nextlast - 1 + arraysize) % arraysize;
            size -= 1;
            if ((double) size() /arraysize<=0.25 && size() >8){
                resizedown();
            }
            return a;
        }
    }
    public T get(int index){
        if (index < 0 || index > size() - 1){
            return null;
        }else {
            return items[(startindex+index)%arraysize];
        }
    }
    public int size(){
        return size;
    }
    public void printDeque(){
        for (int i = 0; i < size();i++){
            System.out.print(items[(startindex+i) % arraysize]+ " ");
        }
        System.out.println();
    }
    public static void main(String[] args) {
        ArrayDeque<Integer> a = new ArrayDeque<>(2);
        a.addLast(3);
        System.out.println(a.get(2));
    }
}
