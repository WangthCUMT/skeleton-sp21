package lab11.graphs;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s; //起始坐标
    private int t; //终点坐标
    private Maze maze;
    // 广度优先搜索的步骤：
    // 1、初始化队列，将起始顶点s添加进队列，并且将对应marked设置为true
    //2、重复以下步骤：
    //将当前顶点v从队列中移除
    //对于未被标记的v的顶点n：标记n，并将n添加进队列
    //设置edgeto[n] = v，distTo[n] = disTo[v] + 1

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
        Queue<Integer> q = new ArrayDeque<>();
        q.add(s); //初始队列
        marked[s] = true;
        while (!q.isEmpty()){
            int v = q.remove(); //将当前顶点v从队列中移除
            for (int n : maze.adj(v)){
                if (!marked[n]){//对于未被标记的v的顶点n
                    marked[n] = true; //标记n
                    edgeTo[n] = v;
                    distTo[n] = distTo[v] + 1;
                    q.add(n);// 将n添加进队列
                    announce();
                    if (n == t){ // 如果邻近节点找到目标，直接停止
                        return;
                    }
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs();
    }
}

