package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        int[] N = new int[]{1000,2000,4000,8000,16000,32000,64000,128000};
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> out = new AList<>();
        AList<Integer> ops = new AList<>();
        for (int i = 0; i < N.length; i++){
            SLList<Integer> Si = create(N[i]);
            out.addLast(Si.size());
            Stopwatch sw = new Stopwatch();
            getNtimes(Si,10000);
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
            ops.addLast(10000);
        }
        printTimingTable(out,times,ops);
    }
    // 创建一个有N大小的SLList
    private static SLList<Integer> create(int N){
        SLList<Integer> Si = new SLList<>();
        for(int i = 0; i < N; i++){
            Si.addFirst(i);
        }
        return Si;
    }
    private static void getNtimes(SLList<Integer> Sin,int N){
        for (int i = 0; i < N; i++){
            Sin.getLast();
        }
    }
}
