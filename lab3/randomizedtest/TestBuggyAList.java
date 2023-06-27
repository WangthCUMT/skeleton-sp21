package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
  @Test
  public void randomizedTest(){
      AListNoResizing<Integer> L = new AListNoResizing<>();
      BuggyAList<Integer> broken = new BuggyAList<>();

      int N = 5000;
      for (int i = 0; i < N; i += 1) {
          int operationNumber = StdRandom.uniform(0, 4);
          if (operationNumber == 0) {
              // addLast
              int randVal = StdRandom.uniform(0, 100);
              L.addLast(randVal);
              broken.addLast(randVal);
          } else if (operationNumber == 1) {
              // size
              int size = L.size();
              int brokensize = broken.size();
              assertEquals(size,brokensize);
          } else if (operationNumber == 2 && L.size() > 0) {
              // get last
              int last = L.getLast();
              int brokenlast = broken.getLast();
              assertEquals(last,brokenlast);
          } else if (operationNumber == 3 && L.size()>0) {
              // remove last
              int removedlast = L.removeLast();
              int brokenremovelast = broken.removeLast();
          }
      }
  }
}
