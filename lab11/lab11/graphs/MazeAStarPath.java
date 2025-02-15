package lab11.graphs;
import edu.princeton.cs.algs4.IndexMinPQ;
/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    private IndexMinPQ<Integer> pq;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        distTo = new int[m.V()];
        edgeTo = new int[m.V()];
        for (int v = 0; v < m.V(); v++){
            distTo[v] = 10000;
        }
    }

    /** Estimate of the distance from v to the target. Math.abs(sourceX - targetX) + Math.abs(sourceY - targetY);*/
    private int h(int v) {
        int sourceX = maze.toX(v);
        int sourceY = maze.toY(v);
        int targetX = maze.toX(t);
        int targetY = maze.toY(t);
        return Math.abs(sourceX - targetX) + Math.abs(sourceY - targetY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        // A star算法步骤：
        // 将初始顶点插入pq并标记
        // 弹出当前队列顶点v，比较所有邻近顶点w，为d(source,v) + h(v,goal)的顺序储存在pq中
        // 重复弹出并标记
        pq = new IndexMinPQ<>(maze.V());
        distTo[s] = 0;
        pq.insert(s,distTo[s]);
        marked[s] = true;
        while (!pq.isEmpty()){
            int v = pq.delMin();
            for (int w : maze.adj(v)){
                if (!marked[w]){
                    marked[w] = true;
                    relax(w,v);
                    if (w == t){
                        return;
                    }
                }
            }
        }
    }

    private void relax(int w, int v) {
        if (distTo[w] > (distTo[v] + 1 + h(v))){
            distTo[w] = distTo[v] + 1 + h(v);
            edgeTo[w] = v;
            if (pq.contains(w)) pq.decreaseKey(w,(distTo[w] + h(w)));
            else pq.insert(w,(distTo[w] + h(w)));
            announce();
        }
    }


    @Override
    public void solve() {
        astar(s);
    }

}

