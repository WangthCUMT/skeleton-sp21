package deque;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;
public class MaxArrayDequeTest {
    public static class numcomparator implements Comparator<Integer> {
        public int compare(Integer a, Integer b){
            return a-b;
        }
    }

    public static class strcomparator implements Comparator<String>{
        public int compare(String a, String b){
            return a.compareTo(b);
        }
    }
    public static class lengthcomparator implements Comparator<String>{
        public int compare(String a, String b){
            return a.length()-b.length();
        }
    }
    @Test
    public void maxtest(){
        Comparator<Integer> c = new MaxArrayDequeTest.numcomparator();
        MaxArrayDeque<Integer> a = new MaxArrayDeque<>(c);
        a.addLast(2);
        a.addLast(3);
        a.addLast(5);
        a.addFirst(9);
        assertEquals(9,(int)a.max());
    }
    @Test
    public void lengthtest(){
        Comparator<String> c = new MaxArrayDequeTest.lengthcomparator();
        MaxArrayDeque<String> a = new MaxArrayDeque<>(c);
        a.addLast("str");
        a.addLast("atrrrr");
        assertEquals("atrrrr",a.max());
    }
    @Test
    public void strtest(){
        Comparator<String> c = new MaxArrayDequeTest.lengthcomparator();
        MaxArrayDeque<String> a = new MaxArrayDeque<>(c);
        Comparator<String> d = new MaxArrayDequeTest.strcomparator();
        a.addLast("str");
        a.addLast("atrrrr");
        assertEquals("str",a.max(d));
    }
}
