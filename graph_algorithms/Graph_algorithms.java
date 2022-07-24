

/*  Summary(seek for code bellow):

  Graphs:   

    graph - orient or unorient
    |Or_graph| =
    |Unor_graph without petel| <= |V|*|V-1|/2  
    
    Graph in memory - watch Graph.java(selfwritten graph-representations) 
        G = (V, E) , E in V^2 
    1) By Matrix of "smezhnosti":  | 0 1 1 1    - O(V x V) = O(V^2)  
                                   | 1 0 1 0
                                   |   ...
                                   |
    [!!!] If Unorient graph => up and down triangels of Matrix are equal (each way - in both directions)
        => we can hold ONLY HALF OF MATRIX!!!

    2) List of "smezhnyh vershin"  - O(V + 2E) = O(V + E). V lists anyway ecxists 
        1   - (2) - (3)  
        2
        ..
        n
    ....


  Algorithms by groups:
    
  ====== Algorithms to crawl through graph(may be used as Iterator for graphs) ======

    1) DFS(Depth first search): Search in depth: => have BST(or not - see) tree as output 
        1. start node
        2. each time go to node with smaller value
        3. ...

        * Help structure:
         Visit_flag Matrix-Array. 
        visit node => it`s flag in array = true
    
        before check - watch Visit_array to reduce already watched nodes
        -each time we check neibhours of node => O(V)

        -each dot to be watched - only once
        end when all Visit_flags = true

        DFS - O(V^2) if graph stored as Matrix
            - O(V + E) if - as List os "smezhnyh" 
            E - number of callig edges during all time of work


    2) WFS: Search in width
        1.start node
        2.each time grabbing all neibhours of node


        * Help structure - Queue Q, :
        Q: add here neibhours of putten in Result[]
        -when addin in Result[] => put it`s neibhours in Queue Q


        WFS - same O():
            - O(V^2) if graph stored as Matrix
            - O(V + E) if - as List os "smezhnyh" 
                E - number of callig edges during all time of work


====== To find shortest way(by weights) ======
    3) Find shortest way - Deycstra algo - used in routing:

        !!! Here MUCH better to use Pyramid

        *Structures
        D[] - Array of "rasstoyaniy" - 
        U[] - flg array

        -each time of un_watched nodes decreases 
        each time we recount distance to each node: find D[node] - min and U[node] = false 

    4) Floyd-Uorshal - minimal(by "weight") "ostovnoe" tree - without cycles
        


====== Get minimal "ostovnoe" tree ======


    5) Prima 
        !!!! so simmular to Deycstra by code
        D[]
        Parent[]    // parent node ~ for shortest way
        Visit[]
        s - start node

        O() - same as Deycstra

        Example - how to most cheaply connect by wire or streets in city 

    6) Kruskall algo 
        Feature:
        D[i] = D[j] = C <=>  nodes i,j are belonging to same tree with root C

    */



/*  ----------------- Code structure(maby not pretty, but was suitable for me):

    -each algorithm is separeted in own subclass 
    -each main class` function "owns" subclass. It only creates obj of subclass, invokes algo-function and returns pole-result from obj

    Ex:
    
    class Algo_graph{
        Result;
        ....
        Algo_(){...}
    }
    
    Algo()
    {
        a = new Algo_graph();
        a.Algo_();
        return a.Result;}
    
*/



import java.util.ArrayList;
import java.util.List;


public class Graph_algorithms{

    // Adapter for Graph.Graph_L for algorithm DFS
    static class DFS_graph{
        Graph.Graph_L G;
        List<Integer> Result;
        List<Boolean> Visited;

        DFS_graph(){}

        DFS_graph(Graph.Graph_L graph){  
            G = graph;
            Result  = (List<Integer>) new ArrayList<Integer>();
            Visited = (List<Boolean>) new ArrayList<Boolean>();
            for (int i = 0; i < graph.get_n(); ++i)             // giving memory for each of n nodes in graph - it`s neccesury, bc we fill "Visited" in random order 
                Visited.add(false);    
        }

        public void DFS_(int x){
            Result.add(x);
            Visited.set(x, true);
            for (int sosed : G.get_list_i(x)){
                if ( !Visited.get(sosed) )                     // Check if sosed wasn`t visited
                    DFS_(sosed);    }
        }
    }


    // searching first unempty list of indents idx- start
    static int get_start(Graph.Graph_L graph){
        int start = 0;
        while(graph.get_list_i(start).isEmpty() && start <= graph.get_n())     
             start++;     
        return start;  }


    // DFS(Graph.Graph_L) - adapts _DFS(DFS_Graph) for already excisitng Graph.Graph_L type 
    static List<Integer> DFS(Graph.Graph_L graph){
        DFS_graph gr = new DFS_graph(graph);
        int start = get_start(graph);
    
        gr.DFS_(start);
        return gr.Result; }



    static class WFS_graph extends DFS_graph{
        List<Integer> Queue = (List<Integer>) new ArrayList<Integer>(); 

        WFS_graph(Graph.Graph_L graph){  super(graph);   }   // same construcor - as for DFS. Queue - is empty on start, so there`s no need to init it 

        void WFS_(int x){
            this.Queue.add(x); Visited.set(x, true);

            while(!Queue.isEmpty()){
                x = this.Queue.get(this.Queue.size() - 1);   // take last el from queue
                this.Queue.remove( this.Queue.size() - 1 );  // removve it outta Queue 
                Result.add(x);                               // add it to already watched elements
                
                for (int sosed : G.get_list_i(x)){
                    if ( !Visited.get(sosed) )                // Check if sosed wasn`t visited
                        {   this.Queue.add(sosed);  Visited.set(sosed, true); }}

            }
        }
    }
    
    static List<Integer> WFS(Graph.Graph_L graph){
        WFS_graph gr = new WFS_graph(graph);
        int start = get_start(graph);
    
        gr.WFS_(start);
        return gr.Result; }


    // !!! Works only with unOrientired graph G of type Graph.Graph_Lw !!!! 
    // !!!   - When init/add to G - use G.add_unOR();  to  keep graph Unorientired 
    static class Deycstra_graph{
         
        List<Integer> D;                                        // array of shortest ways` lengths to each node(from chosen node) of graph on each step(crawl)
        
        Graph.Graph_Lw G;
        List<Boolean> Visited;
        Integer INF = Integer.MAX_VALUE;                        // max value as Infinity

        Deycstra_graph(Graph.Graph_Lw graph){
            G = graph;
            Visited = (List<Boolean>) new ArrayList<Boolean>();
            for (int i = 0; i < graph.get_n(); ++i)             // giving memory for each of n nodes in graph - it`s neccesury, bc we fill "Visited" in random order 
              Visited.add(false);
            
            D = new ArrayList<Integer>();
            for (int i = 0; i < graph.get_n(); ++i)             // giving memory for each of n nodes in graph - it`s neccesury, bc we fill "D" in random order 
                D.add(new Integer(INF));              
        }
        

        // *** How Pyromid helps to find min?:
        //    D stores as Pyramid(min-Heap) => 0-th element is always closest(has min "w" - Pyramid`s proprty) neighbor to fixed node

        // Pyromid-sort in fact
        Graph.Graph_Lw.Quasi_Edge get_min_D(){    // taking min element from D that wasn`t chosen yet

            // Making new min-Heap of not visited nodes from where we return top(minimal node)
            Pyromid<Graph.Graph_Lw.Quasi_Edge, List<Graph.Graph_Lw.Quasi_Edge>> buff_heap = 
            new Pyromid<Graph.Graph_Lw.Quasi_Edge, List<Graph.Graph_Lw.Quasi_Edge>>();          // looks terrible - I agree. tip: left and right sides`re same and (Graph.Graph_Lw.Quasi_Edge) can be "perceived" as (Quasi_Edge)                              
            
            for (int i = 0; i < Visited.size(); ++i)
                if (!Visited.get(i) && D.get(i) != INF)  buff_heap.add(new Graph.Graph_Lw.Quasi_Edge(i, D.get(i))); 

            return buff_heap.get_i(0);
        }

        Boolean all_nodes_visited(){
            for (Boolean flg : Visited)
                if (!flg) return false;
            return true; }

        public void Deycstra_(int start){
            D.set(start, 0);                                   // way to self = 0
            List<Graph.Graph_Lw.Quasi_Edge> v_neighbors;

            Graph.Graph_Lw.Quasi_Edge v;
            Graph.Graph_Lw.Quasi_Edge edge_i;
            int sum;


            while (!all_nodes_visited()){

                v = get_min_D();
                Visited.set(v.v, true);

                v_neighbors = G.list.get(v.v); 
                for(int i=0; i < v_neighbors.size() ; ++i ){    // iterate over v`s neighbors 
                    edge_i = v_neighbors.get(i);
                    sum = v.weight + edge_i.weight;                     
                    if (D.get(edge_i.v) > sum)                  // if D[i] > D[v] + (v, i).weight
                        D.set(edge_i.v, sum); }

            }

        }
        
    }
        
    

    
// !!! Works only with unOrientired graph G of type Graph.Graph_Lw !!!! 
// !!!   - When init/add to G - use G.add_unOR();  to  keep graph Unorientired 
    public static List<Integer> Deycstra(Graph.Graph_Lw graph, int start){

        Deycstra_graph gr = new Deycstra_graph(graph);
        gr.Deycstra_(start);                            

        return gr.D;
    }



    static class Prima_graph extends Deycstra_graph{
        List<Integer> Parent;                           // parent of each node. so that we can built tree

        Prima_graph(Graph.Graph_Lw graph){
            super(graph);
        
            Parent = new ArrayList<Integer>();
            for (int i = 0; i < graph.get_n(); ++i)        
                Parent.add(null);
        }

        // SAME as Deycstra algo, but here we fill 2 arrays: D and Parent AND check if node was visited (one line in "for" cycle) 
        void Prima_(int start){
            D.set(start, 0);                                                     // way to self = 0
            List<Graph.Graph_Lw.Quasi_Edge> v_neighbors;

            Graph.Graph_Lw.Quasi_Edge v;
            Graph.Graph_Lw.Quasi_Edge edge_i;
            int sum;

            while (!all_nodes_visited()){

                v = get_min_D();
                Visited.set(v.v, true);

                v_neighbors = G.list.get(v.v); 
                for(int i=0; i < v_neighbors.size() ; ++i ){                     // iterate over v`s neighbors 
                    
                    edge_i = v_neighbors.get(i);
                    if (Visited.get(edge_i.v)) continue;                         // [NEW] skip edge if node is already watched
                                
                    if (D.get(edge_i.v) >  edge_i.weight){                       // if D[i] > (v, i).weight
                        D.set(edge_i.v,  edge_i.weight); 
                        Parent.set(edge_i.v, v.v); }                             // [NEW]        
            }  }
        }

    }



    public static List<Integer> Prima(Graph.Graph_Lw graph){
            
        // Find 1-st not null node (not all nodes can be inited in graph.     ex: G = new Graph(10); => we can chain nodes 0-9, but some nodes may stay unchained  )
        List<Graph.Graph_Lw.Quasi_Edge> list_i;
        Graph.Graph_Lw.Quasi_Edge start = null;

        for( int i = 0; i < graph.get_n(); ++i){
            list_i = graph.list.get(i);
            if (!list_i.isEmpty())
                {  start = list_i.get(0); break; } 
        }

        Prima_graph gr = new Prima_graph(graph);
        gr.Prima_(start.v);                            
        return gr.Parent;
    }



    public static class Floyd_Uorshal_graph{
        
        Graph.Graph_Mw G;
        List< List<Integer> > D;

        Floyd_Uorshal_graph(Graph.Graph_Mw graph){
            G = graph;

            // Coppy(phisically) current G`s matrix state into D
            D = (List<List<Integer>>) new ArrayList<List<Integer>>();
            for (int i = 0; i < graph.size; ++i){    
                D.add(new ArrayList<Integer>());
                for (int j = 0; j < graph.size; ++j)
                    D.get(i).add( new Integer(graph.M.get(i).get(j)) );}
        }

        void Floyd_Uorshal_(){
            int sum;
            int i_k, k_j;

            for (int k = 0; k < G.size; ++k)
                for (int i = 0; i < G.size; ++i)
                    for (int j = 0; j < G.size; ++j){
                        i_k = D.get(i).get(k);   k_j = D.get(k).get(j);
                        if ( i_k != Integer.MAX_VALUE && k_j != Integer.MAX_VALUE ){
                            sum = i_k + k_j;
                            if (D.get(i).get(j) > sum)
                                D.get(i).set(j, sum);
                        }
                    }
        }

    }


    public static List<List<Integer>> Floyd_Uorshal(Graph.Graph_Mw graph){
        Floyd_Uorshal_graph G = new Floyd_Uorshal_graph(graph);
        G.Floyd_Uorshal_();
        return G.D;
    }



    public static class Kruskall_Uoliss_graph{

        // weighted edge
        class Edge_w implements Comparable<Edge_w>{
            int u; int v; Integer w;                      // (u, v) edge with weight = "w"
            Edge_w(int U, int V, int W) {u = U; v = V; w = W; }
            
            // Compare edges due their weights
            public int compareTo(Edge_w edge)  { return w.compareTo(edge.w); }
        }

        Graph.Graph_Lw G;
        Pyromid<Edge_w, List<Edge_w>> Queue;              // min-Heap of edges => eazy to find "lowest" by weight
        List<Integer> Parent;
        List<Integer> D;                                  // array that tells to witch tree belongs each node. ex:  D[0] = D[1] = 5 <=> 0 and 1 are in same tree with root = 5 
        

        Kruskall_Uoliss_graph(Graph.Graph_Lw Gr){

            // Making pyromid(min-Heap) aka queue from all G`s edges    
            Queue = new Pyromid<Edge_w, List<Edge_w>>();        
            List<Graph.Graph_Lw.Quasi_Edge> list_i;
            for ( int i = 0; i < Gr.n; ++i){
                list_i = Gr.list.get(i);
                for ( Graph.Graph_Lw.Quasi_Edge el : list_i)
                    Queue.add(new Edge_w(i, el.v, el.weight) );
            }

            
            // Alloc memory and init by default values both arrays:
            
            D = (List<Integer>) new ArrayList<Integer>();    
            for (int i = 0; i < Gr.n; ++i)
                D.add(i);                                               // at start we have each node as separate tree

            Parent = (List<Integer>) new ArrayList<Integer>();    
            for (int i = 0; i < Gr.n; ++i)
                Parent.add(Integer.MAX_VALUE);                          // pseudo-infinity
        }
       

        
        void Kruskall_Uoliss_(){
            Edge_w min_edge;
            int u,v, old_tree;

            while (!Queue.is_empty()){
                // taking el away from queue 
                min_edge = Queue.get_min();
                Queue.remove();

                u = min_edge.u; 
                v = min_edge.v;

                if (D.get(u) != D.get(v)){              // if nodes u and v are in diffrent subtrees (subtrees excists on phase of building) 
                    Parent.set(v, u);                   // fill in the table of Parents for each node
                    old_tree = D.get(u);                // choose one of trees(D.get(u)) to add to another (D.get(v))

                    for (int i = 0; i < D.size(); ++i)
                        if (D.get(i) == old_tree)     
                            D.set(i, D.get(v));         // adding edges from old tree to another tree  
                }
            
            }
        }
    }


    public static List<Integer> Kruskall_Uoliss(Graph.Graph_Lw graph){
        Kruskall_Uoliss_graph G = new Kruskall_Uoliss_graph(graph);
        G.Kruskall_Uoliss_();
        return G.Parent;
    }

       

}
    



