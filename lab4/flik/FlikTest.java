package flik;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FlikTest {
    @Test
    public void num128test() {
        Integer a = 128;
        Integer b = 128;
        assertEquals(a.equals(b), a == b);
    }
}
