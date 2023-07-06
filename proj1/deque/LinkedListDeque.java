package deque;

public class LinkedListDeque {
<<<<<<< HEAD

=======
    private static class IntNode {
        public int item;
        public IntNode next;
        public IntNode prev;

        public IntNode(int i, IntNode p,IntNode n) {
            item = i;
            prev = p;
            next = n;
            // System.out.println(size);
        }
    }
    private IntNode sentinelfirst;
    private IntNode sentinellast;
    private int size;
    public LinkedListDeque(int i){
        sentinelfirst = new IntNode(63,null,null);
        sentinellast = new IntNode(63,null,null);
        sentinelfirst.next = new IntNode(i,sentinelfirst,sentinellast);
    }

    public static void main(String[] args) {
        LinkedListDeque a = new LinkedListDeque(2);

    }
>>>>>>> aad606d3c2ef8416c6819195874eba9b93cf248e
}
