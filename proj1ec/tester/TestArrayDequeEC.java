package tester;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

import static org.junit.Assert.assertEquals;

public class TestArrayDequeEC {
    @Test
    public void randomizedTest() {
        ArrayDequeSolution<Integer> L = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> broken = new StudentArrayDeque<>();
        StringBuilder Errormessage = new StringBuilder();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 5);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                broken.addLast(randVal);
                Errormessage.append("addLast(" + randVal + ") \n");
            } else if (operationNumber == 2 && L.size() > 0 && broken.size() > 0) {
                // removeFirst
                Integer first = L.removeFirst();
                Integer brokenfirst = broken.removeFirst();
                Errormessage.append("removeFirst() \n");
                assertEquals(Errormessage.toString(),first, brokenfirst);
            } else if (operationNumber == 3 && L.size() > 0 && broken.size() > 0) {
                // removeLast
                Integer removedlast = L.removeLast();
                Integer brokenremovelast = broken.removeLast();
                Errormessage.append("removeLast() \n");
                assertEquals(Errormessage.toString(), removedlast, brokenremovelast);
            } else if (operationNumber == 4) {
                // addFirst
                int randVal = StdRandom.uniform(0, 100);
                L.addFirst(randVal);
                broken.addFirst(randVal);
                Errormessage.append("addFirst(" + randVal + ") \n");
            }
        }
    }
}
