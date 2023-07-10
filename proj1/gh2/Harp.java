package gh2;

import deque.Deque;
import deque.LinkedListDeque;

public class Harp {
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .992; // energy decay factor

    /* Buffer for storing sound data. */
    private Deque<Double> buffer = new LinkedListDeque<>();

    /* Create a guitar string of the given frequency.  */
    public Harp(double frequency) {
        int capacity = (int) Math.round(SR / frequency) * 2;
        for (int i = 0; i < capacity; i++) {
            buffer.addLast((double) 0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        //       Make sure that your random numbers are different from each
        //       other. This does not mean that you need to check that the numbers
        //       are different from each other. It means you should repeatedly call
        //       Math.random() - 0.5 to generate new random numbers for each array index.
        Deque<Double> temp = new LinkedListDeque<>();
        for (int i = 0; i < buffer.size(); i++) {
            temp.addLast(Math.random() - 0.5);
        }
        buffer = temp;
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double removed = buffer.removeFirst();
        double enqueued = -(removed + buffer.get(0)) * DECAY * 0.5;
        buffer.addLast(enqueued);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.get(0);
    }
}