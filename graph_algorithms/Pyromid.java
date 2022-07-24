
//  It`s lab_2(6 sem) - just renamed from prior_queue to Pyromid 
//  AND bc in Prima/Deycsta we oughta find SHORTEST way
//    here I changed (just turned each ">" to "<" in .compareTo ) 
//    sorting from "max_Heap" - where elements were going down from greater
//    to "min-Heap" - where els goes from smalest to largest  



import java.util.ArrayList; // To make instance of List we need ArrayList \_(*_*)_/. bc List is interface and can`t be instanced by definition
import java.util.List;      // for List object
import java.lang.Math;      // for floor(), pow(), log()

// =========== Pyromid(Binary Heap) realization =============

//  1] Assume src array as "almost complite" binary tree(not BST, just BT, where each node has 2 children). Walk from down to up through all nodes that has children.
//             *
//         *       *        | each node has 2 children
//        * *     * *       | heigh = log(n)
//       ** **   ** **

//  [*] our i-th nodes are chained by rule: l_idx = 2i + 1,  r_idx = 2i + 2 
//      so "tree" is actually stores as array

//  2] Watching trio (parent, l, r): the "bigest" one of them becomes "parent", if it`s  "l" or "r" - happens switching of "parent" and one - simmilar to buble sort 
//  3] Continue 2] till root of tree



public class Pyromid <T extends Comparable <T>, Cont extends List<T>>  
{   

    Cont list;      // Cont - custom type of container that has class` List methods                   
    int n;          // Sorry for this 2-nd pole, but it`s necessory for "good read-ableness" of code  


// --------------- Sub functions
    double log2(int n)
    {
        return Math.log(n) / Math.log(2);
    }

 
    void swap(int i, int j){        // swaps by indexes   
        T buff = list.get(i);
        list.set(i, list.get(j));
        list.set(j, buff);  }


    void build(int cur){
        
        int l_idx = cur*2 + 1;  int r_idx = cur*2 + 2;
        int M_idx = cur;                                // Index of max el in trio - to be swaped with current after compairing trio
        T par;                                          // current value max-node
        
        // Watching trio (par, l, r). l and par are precounted for "readable-ness"
        if (l_idx < n) { T l = list.get(l_idx); par = list.get(M_idx); if (l.compareTo(par) < 0) M_idx = l_idx; }  
        if (r_idx < n) { T r = list.get(r_idx); par = list.get(M_idx); if (r.compareTo(par) < 0) M_idx = r_idx; }
       
         
        if (M_idx != cur){
            swap(cur, M_idx);
            build(M_idx);       // fixing left/right branch due it`s head swaped
        }

        if (cur == 0)           // deciding if to end the main reqursion 
            return;
        else
            build(cur-1);       // continue main reqursion (along nodes, that has children)
    } 


    // Same as build(), BUT: shift() assumes that we have pyromid-structed array (except it`s head)
    // so we move down(shift) while comparing shows that cur was smallest of the tripple. Then we have heap.
    // [!!!] shift() is cheaper than build() bc we don`t need to rebuild after swapping. O(log n) bc log n - is lenght of brench(which we shift down)
    void shift_down(int cur){
        
        int l_idx = cur*2 + 1;  int r_idx = cur*2 + 2;
        int M_idx = cur;                                
        T par;                                          
        
        if (l_idx < n) { T l = list.get(l_idx); par = list.get(M_idx); if (l.compareTo(par) < 0) M_idx = l_idx; }  
        if (r_idx < n) { T r = list.get(r_idx); par = list.get(M_idx); if (r.compareTo(par) < 0) M_idx = r_idx; }
        
        if (M_idx != cur){
            swap(cur, M_idx);
            
            /* Info about swapping - if need
            if (M_idx == l_idx) System.out.println(" Shifted left  | swaped: " + list.get(cur) +", "+ list.get(M_idx));
            else                System.out.println(" Shifted right | swaped: " + list.get(cur) +", "+ list.get(M_idx));
            */

            shift_down(M_idx);  
            }

        else                       // if there is no changing <=> element had "found" it`s place - we`re leaving function
            return; 
    } 


// --------------- Constructors

    public Pyromid(){
        this((Cont) new ArrayList<T>());          
        n = 0; 
    }


    public Pyromid(Cont lst){
        //list = Arrays.<T>asList(lst);  // - This way creates FIXED SIZE array, so forget about it - we need dynamic one
        this.list = lst;
        n = list.size();
        if (n > 1)
        {
            // seeking last node that has children 
            int h = (int)Math.ceil(log2(n));        // number of levels in complite binary tree(eazy to understand)
            int cur = (int)Math.pow(2, h-1) - 1;    // crawling to pre-dippest level of tree (bc deepest lvl`s nodes can`t have children). -1 bc counting from 0 in arr
            while(2*cur + 1 > n-1)                  // has no children(is leaf) => go backward. btw parent may haven`t right child, but left(2i+1) is "musthave"
                cur--;                           
            
            build(cur); 
        }
    }

    public Pyromid(Pyromid src){
        //this((Cont) new ArrayList<T>());
        list = (Cont) new ArrayList<T>( src.list );
        n = src.n;
    }

// -------------- Required Methods
    public int len()           { return n; }
    public T get_min()         { return list.get(0); }
    public boolean is_empty()  { return (n == 0); }

    public T get_i(int i)      { return list.get(i); }

    public void add(T val){
        n++;
        list.add(val);                                              

        int i = n-1;                                               // last idx in arr 
        int par_i = (int)Math.floor((i-1)/2);                      // parent index (it`s property of Pyromid)
        
        while (val.compareTo( list.get(par_i) ) < 0 && i != 0){    // comparing with parent till root (i==0)
            swap(par_i, i);
            i = par_i;                                             // crawling through branch of tree (bc each level of Pyromid is "greater" then previous)   
            par_i = (int)Math.floor((i-1)/2);                      // same
        }
    }
 
    
    public T remove(){                // remove from Queue <=> pull out root-element and rebuilt the Pyromid
        T first = get_min();
        
        // rebuild
        swap(0, n-1);
        list.remove(n-1);
        n--;                           // bc we can`t remove el from empty queue

        shift_down(0);                 // we still have pyromid structure, but the head - is wrong. So fixing it 
        return first;
    }

    public void print(){
        System.out.println("---------------------------");
        for (T el : list)
            System.out.print(el + " ");
        System.out.println("\n---------------------------");
    }


}




