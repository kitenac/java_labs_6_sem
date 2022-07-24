
import java.util.ArrayList;
import java.util.List;


public class test{
    

    public static void pretty_print(List<Integer> lst, String line_1, String line_2){
        
        int el;

        System.out.print(line_1);
        for (int i = 0; i < lst.size(); ++i)
            System.out.print(i + "   ");
        System.out.println();
      
        System.out.print(line_2);
        for (int i = 0; i < lst.size(); ++i){
            try{
                el = lst.get(i);
                if (el != Integer.MAX_VALUE)
                    System.out.print(el + "   "); 
                else System.out.print("INF ");
            }

            catch (NullPointerException e)  {   System.out.print("-   ");}    
        }
        System.out.println(); 
    }

    public static void pretty_print_2D(List<List<Integer>> M){
            System.out.println();
            for (List<Integer> col : M)
            {   System.out.println();
                for (Integer el : col)
                {  
                    if (el == Integer.MAX_VALUE)    System.out.print("  -");
                    else                            System.out.print("  " + el); } 
            } 
            System.out.println(); System.out.println(); }    
 



    public static void main(String[] args) {
        
        System.out.println("======= Crawling algorithms:");

        Graph.Graph_L G_2 = new Graph.Graph_L(7);
        G_2.add(1, 2); G_2.add(1, 4); G_2.add(2, 6); G_2.add(2, 5); G_2.add(3, 4);  G_2.add(6, 3); G_2.add(5, 4);
        G_2.print();

        System.out.println("DFS-crawling result: " + Graph_algorithms.DFS(G_2) );        
        System.out.println("WFS-crawling result: " + Graph_algorithms.WFS(G_2) );   
       
        System.out.println("\n======= Shortest ways between nodes algorithms:");
        Graph.Graph_Lw G_4 = new Graph.Graph_Lw(10);
        G_4.add_unOR(1, 2, 8); G_4.add_unOR(1, 4, 5); G_4.add_unOR(2, 6, 2); G_4.add_unOR(2, 5, 3); G_4.add_unOR(3, 4, 1);  G_4.add_unOR(6, 3, 9); G_4.add_unOR(5, 4, 3);
        System.out.println("\nLet`s take different graph for next algorithms:");
        G_4.print();

        System.out.println("\nDeycstra result(let 4 be start node): ");
        pretty_print(Graph_algorithms.Deycstra(G_4, 4), "Node:     ", "Distance: ");
        

        Graph.Graph_Mw G_5 = new Graph.Graph_Mw(7);
        G_5.add_unOR(1, 2, 8); G_5.add_unOR(1, 4, 5); G_5.add_unOR(2, 6, 2); G_5.add_unOR(2, 5, 3); G_5.add_unOR(3, 4, 1);  G_5.add_unOR(6, 3, 9); G_5.add_unOR(5, 4, 3);
        System.out.println("\nOur graph as matrix: ");
        G_5.print();

        System.out.println("\nFloyd-Uorshal result(node to node shortest ways` weights): \n -As we can see 5-th line(node 4) is same as Deycstra result(starting from node 4) ");
        pretty_print_2D(Graph_algorithms.Floyd_Uorshal(G_5) );

        System.out.println("\n======= Поиск минимального остовного дерева:");
        System.out.println("\nPrima result: ");
        pretty_print(Graph_algorithms.Prima(G_4),       "Node:     ", "Parent:   ");
        
        System.out.println("\nKruscal-Uoliss result: ");
        pretty_print(Graph_algorithms.Kruskall_Uoliss(G_4), "Node:     ", "Parent:   ");

        System.out.println("\n\n -Prima and Kruscal-Uoliss gave same graph (representation is little bit different, but if u draw graph - u`ll see)\n");

    }


}