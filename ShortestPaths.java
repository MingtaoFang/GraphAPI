package graph;

/* See restrictions in Graph.java. */
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
/** The shortest paths through an edge-weighted labeled graph of type GRAPHTYPE.
 *  By overrriding methods getWeight, setWeight, getPredecessor, and
 *  setPredecessor, the client can determine how to get parameters of the
 *  search and to return results.  By overriding estimatedDistance, clients
 *  can search for paths to specific destinations using A* search.
 *  @author
 */
public abstract class ShortestPaths {

    public class AstarTraversal extends Traversal {
        protected AstarTraversal(Graph G, PriorityQueue<Integer> fringe) {//not sure if we should make the data type queue or priority queue.
            super(G, fringe); //is the implementation here ok?
        }

        @Override
        protected boolean visit(int v) {
            //need modification for visit
            if (v == getDest()) {
                return false; //breka out of the for loop so we stop seraching to save time.
            }

            // System.out.println("==================");

            // for (int vvv : _priorityfringe) {
                
            //     System.out.print("I am now with index: " + vvv + "with distance :" + (getWeight(vvv) + estimatedDistance(vvv)));
            //     System.out.println();
            // }
            // System.out.println(" ");
            // System.out.println("=========================");



            for (int houlaizhe : _G.successors(v)) {       
                if (getWeight(houlaizhe) > getWeight(v, houlaizhe) + getWeight(v)) {
                    setWeight(houlaizhe, getWeight(v, houlaizhe) + getWeight(v));
                    setPredecessor(houlaizhe, v);
                    
                    _priorityfringe.add(_priorityfringe.poll());
                
                }
                if (!_priorityfringe.isEmpty()) {
                   _priorityfringe.add(_priorityfringe.poll()); 
                }
                
            }

            if (!_priorityfringe.isEmpty()) {
                _priorityfringe.add(_priorityfringe.poll());    
            }
            


            // System.out.println("***********************");

            // for (int vvv : _priorityfringe) {
                
            //     System.out.print("I am now with index: " + vvv + "with distance :" + (getWeight(vvv) + estimatedDistance(vvv)));
            //     System.out.println();
            // }
            // System.out.println(" ");
            // System.out.println("***************************");

            // System.out.println("=========================");

            //then reorder the fringe

            //done reordering the fringe

            return true; //return true for now
        }

        @Override
        protected boolean processSuccessor(int u, int v) {
            return false; //return false for now according to the psuedocode;
        }
    }


    public class AbsoluteDistanceComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            double o1Distance = estimatedDistance(o1);
            double o2Distance = estimatedDistance(o2);
            double o1Weight = getWeight(o1);
            double o2Weight = getWeight(o2);

            if ((o1Distance + o1Weight) < (o2Distance + o2Weight)) {
                return -1;
            } else if ((o1Distance + o1Weight) > (o2Distance + o2Weight)) {
                return 1;
            } else {
                return 0;
            }
        }
    }





    /**  shortest paths in G from SOURCE. */
    public ShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public ShortestPaths(Graph G, int source, int dest) {
        _G = G;
        _source = source;
        _dest = dest;
        // FIXME
        _parent = new int[_G.maxVertex() + 2];
        _weight = new double[_G.maxVertex() + 2];

    }

    /** Initialize the shortest paths.  Must be called before using
     *  getWeight, getPredecessor, and pathTo. */
    public void setPaths() {
        // FIXME
        _comparatorIWant = new AbsoluteDistanceComparator();
        _priorityfringe = new PriorityQueue<Integer>(_G.vertexSize(), _comparatorIWant);
        _findshortest = new AstarTraversal(_G, _priorityfringe);


        for (int v : _G.vertices()) {
            _weight[v] = Double.POSITIVE_INFINITY;
            _parent[v] = -1;
        }

        _weight[getSource()] = 0;

        for (int vertex : _G.vertices()) {
            _priorityfringe.add(vertex);
            // System.out.println("I put a vertex :" + vertex);
        }
            // System.out.println("***********************");

            // for (int vvv : _priorityfringe) {
            //     System.out.println("I am checking the fringe");
            //     System.out.print(vvv);
            //     System.out.println(" ");
            // }

            // System.out.println("***********************");



    }

    /** Returns the starting vertex. */
    public int getSource() {
        // FIXME
        return _source;
    }

    /** Returns the target vertex, or 0 if there is none. */
    public int getDest() {
        // FIXME
        return _dest;
    }

    /** Returns the current weight of vertex V in the graph.  If V is
     *  not in the graph, returns positive infinity. */
    public abstract double getWeight(int v);

    /** Set getWeight(V) to W. Assumes V is in the graph. */
    protected abstract void setWeight(int v, double w);

    /** Returns the current predecessor vertex of vertex V in the graph, or 0 if
     *  V is not in the graph or has no predecessor. */
    public abstract int getPredecessor(int v);

    /** Set getPredecessor(V) to U. */
    protected abstract void setPredecessor(int v, int u);

    /** Returns an estimated heuristic weight of the shortest path from vertex
     *  V to the destination vertex (if any).  This is assumed to be less
     *  than the actual weight, and is 0 by default. */
    protected double estimatedDistance(int v) {
        return 0.0;
    }

    /** Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     *  not in the graph, returns positive infinity. */
    protected abstract double getWeight(int u, int v);

    /** Returns a list of vertices starting at _source and ending
     *  at V that represents a shortest path to V.  Invalid if there is a
     *  destination vertex other than V. */
    public List<Integer> pathTo(int v) {
        // FIXME

        ArrayList<Integer> path = new ArrayList<Integer>();

        while (getPredecessor(v) != 0) {
            path.add(v);
            v = getPredecessor(v);
        }

        path.add(getSource());


        // for (int here : _parent) {
        //     if (parent[here] == -1)
        // }

        return path;
    }

    /** Returns a list of vertices starting at the source and ending at the
     *  destination vertex. Invalid if the destination is not specified. */
    public List<Integer> pathTo() {
        return pathTo(getDest());
    }

    // FIXME

    /** The graph being searched. */
    protected final Graph _G;
    /** The starting vertex. */
    private final int _source;
    /** The target vertex. */
    private final int _dest;
    // FIXME

    private PriorityQueue<Integer> _priorityfringe;

    private AbsoluteDistanceComparator _comparatorIWant;

    public int[] _parent; // being public might not be a good choice

    //private int[] _dist;
    private AstarTraversal _findshortest;

    public double[] _weight; // being public might not be a good choice
}
