package deque;

public class ArrayDeque {
    private int[] items;
    private int arraysize;
    private int size;
    private int nextfirst;
    private int nextlast;
    private int startindex;
    public ArrayDeque(){
        arraysize = 8;
        items = new int[arraysize];
        startindex = 5;
        nextfirst = 4;
        nextlast = 5;
        size = 0;
    }
    public ArrayDeque(int i){
        size = 1;
        arraysize = 8;
        items = new int[arraysize];
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
    public void addLast(int i){
        size += 1;
        items[nextlast] = i;
        nextlast = (nextlast + 1) % arraysize;
    }
    public void addFirst(int i){
        size += 1;
        items[nextfirst] = i;
        nextfirst = (nextfirst - 1 + arraysize)% arraysize;
        startindex = (nextfirst + 1) % arraysize;
    }
    public void resize(int cap){
        int[] a = new int[cap];

    }
    public int size(){
        return size;
    }
    public void printDeque(){
        for (int i = 0; i < size();i++){
            System.out.println(items[(startindex+i) % arraysize]);
        }
    }
    public static void main(String[] args) {
        ArrayDeque a = new ArrayDeque(2);
        a.addLast(2);
        a.addLast(3);
        a.addLast(4);
        a.addLast(5);
        a.addLast(6);
        a.addFirst(1);
        a.printDeque();
    }
}
