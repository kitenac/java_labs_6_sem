import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


    
/* Bellow there`s 3 realizations(as subclasses) of graph 
    -to create one of `em:    Graph.Graph_Mw = new  Graph.Graph_Mw(n) */

public class Graph{

    Graph(){}
//-------------------------------------------------------------------------------------------------------------------------------
// Graph_Mw - graph stored as n x n - Matrix of "node-node" indents

    public static class Graph_Mw{
        int size;                     // ammount of nodes
        List< List<Integer> > M;      // Matrix of weights from every i-th to j-th node - as vector of arrays(vectors) <=> Matrix   
    
    
        Graph_Mw(int n){
            M = (List< List<Integer> >) new ArrayList< List<Integer> >();  // upcast ArrayList to List with same <>-part
            for (int i = 0; i < n; ++i){         
                M.add( (List<Integer>) new ArrayList<Integer>() );         // add coloumn(vector) using upcast as above
                for (int j = 0; j < n; ++j)
                    {
                        if (i == j) M.get(i).add(0);                                                // weight of way to self = 0
                        else M.get(i).add(Integer.MAX_VALUE);                                       // fill coloumn(vector) with "infinity" - bc we have no graph yet
                    }            
            }
            size = n;}

    
        // Function to fill Src Matrix by user.   Pseudo_Ex: Src = Graph(n); Src.add(2, 2); Src.add(1, 3) ..... 
        public void add(int x, int y, int w){
            M.get(x).set(y, w);  
        }

        public void add_unOR(int x, int y, int w){
            M.get(x).set(y, w);     M.get(y).set(x, w);  
        }

        public int get_size() { return size;}
    
// -------- Just prety init/print methods bellow: -------
        // "Manager-Constructer"
        public static Graph_Mw create_G_interactive(){
            String alert = "[!!!] Format of trio: 'i,j,w' - without whitespaces or smth else. To end press 'q'" + "\n";
            Scanner sc = new Scanner(System.in);

            System.out.println("To create graph - set value of n, where n x n = size of graph`s matrix ");
            int n = sc.nextInt();
            Graph_Mw G = new Graph_Mw(n);
            
            System.out.println("Fill the matrix of graph G. \n  Enter i, j, w trio to mark path (i) --w--> (j).");
            while(true){
                String str = sc.nextLine();
                if (str.equals("q")) break;
                String trio[] = str.split(",");
                if (trio.length != 3) {System.out.println(alert + "\n");  continue; }                

                G.add(Integer.parseInt(trio[0]) , Integer.parseInt(trio[1]) , Integer.parseInt(trio[2]));         // add value in M with casting String to Integer
            }
            return G;}


        public void print(){
            System.out.println("----------- Graph as Matrix -------");
            for (List<Integer> col : M)
            {   System.out.println();
                for (Integer el : col)
                {  
                    if (el == Integer.MAX_VALUE)    System.out.print("  -");
                    else                            System.out.print("  " + el); } 
            } 
            System.out.println(); System.out.println("----------------------------------");}    
    }

    

//-------------------------------------------------------------------------------------------------------------------------------
// Graph_L - as list of nearby nodes for each node.  

     public static class Graph_L{           // this class could be not static(but for unification it`s same as Graph_Mw: when we create static class we use " = new Class.sub_class()" instead of " = new sub_class()"         
        
        List<List<Integer>> list;           // list of Integer lists
        int n;                              // ammount of nodes - not size!!!

        public Graph_L(){}

        public Graph_L(int n){
            this.n = n;
            list = (List<List<Integer>>) new ArrayList<List<Integer>> ();    
            for (int i=0; i<n; ++i) 
                list.add( (List<Integer>) new ArrayList<Integer>() );  // reserve memory for each of n nodes` list(queue - pyramid - min-Heap). so that minimal element`ll be 0-th every step  
        }


        public void add(int Node, int neighbor)    {   list.get(Node).add(neighbor);  }    // function to add neighbor to given Node`s queue(list - pyromid - min_heap)
        

        public int get_n()  { return n; }
        public List<Integer>  get_list_i(int i) { return list.get(i); }                  // return list of i-th node`s neighbors (i-th colomn of table)

        
        public void print(){
            System.out.println("--------- Graph as List --------");
            int idx = 0;
            for (List<Integer> Node : list){
                System.out.print(idx + ": ");
                idx++;
                for (int sosed : Node)
                    System.out.print(" " + sosed);  System.out.println();} System.out.println("----------------------------------"); }
    
    }


//-------------------------------------------------------------------------------------------------------------------------------
// Graph_Lw - same as Graph_L, but each neighbor now has weight "w" - stores as quasi_edge - see sublass bellow: 

    public static class Graph_Lw extends Graph_L{


        public static class Quasi_Edge implements Comparable<Quasi_Edge>{      // (v | w) - see /*...*/ above
            int v;
            Integer weight;                 // Integer has compareTo() method instead of int
            public Quasi_Edge(int V, int Weight)  { v = V; weight = Weight;}
 
            int get_v() {   return v;   }

            // Compare edges due their weights
            public int compareTo(Quasi_Edge edge)  { return weight.compareTo(edge.weight); }
 
            @Override
            public String toString() {  return "(" + v + "|" + weight + ") "; }        
         }
      
        // Redefine class pole "list" from supperclфss
        List< List<Quasi_Edge> > list;

        public Graph_Lw(int n){
            this.n = n;
            list = (List <List<Quasi_Edge>>)   new   ArrayList <List<Quasi_Edge>> ();  
            for (int i=0; i<n; ++i) 
                list.add( (List<Quasi_Edge>)   new   ArrayList<Quasi_Edge>() );  // reserve memory for each of n nodes` list  
        }

        @Override
        public void print(){
            System.out.println("--------- Graph as List --------");
            int idx = 0;
            for (List<Quasi_Edge> Node : list){
                System.out.print(idx + ": ");
                idx++;
                for (Quasi_Edge sosed : Node)
                    System.out.print(" " + sosed);  System.out.println();} System.out.println("----------------------------------"); }

        
        public void add(int u, int v, int w)      {   list.get(u).add(new Quasi_Edge(v,w));  }  
        public void add_unOR(int u, int v, int w) {   this.add(u, v, w); this.add(v, u, w);  }

    } 


}

