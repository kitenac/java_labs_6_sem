

//map_data - data type:  pair (key:data) - used for AVL_tree<map_data> bellow
class map_data<K extends Comparable<K>, T > implements Comparable<map_data<K, T>>{  //  implements Comparable - to make map_data type fit wit T type from AVL_tree<T extends Compareable<T>>
    K key;          // Must be uniqual!!!
    T data;
    
    map_data(K keY, T datA)
    {   key = keY; data = datA; }
    
    map_data(map_data<K, T> src)
    {   key = src.key; data = src.data; }

    // compare map's nodes - same as comparing their keys 
    // comparing keys => BST will provide search by key value. if we used data => BST - would provide search by .data
    public int compareTo(map_data<K, T> B)
    {   return (this.key).compareTo(B.key); }

    // !!! Good & Simple Feature !!! 
    public String toString(){   // Provides informative printing to object of type "map_data". when calling append and Balance`s functions from AVL_tree - there is much printig of objects type T, we`ve got T = map_data here  
        return "{" + this.key.toString() + ":" + this.data.toString() + "}";
    }
}


// !!! method .get_by_key() throws Exception - so catch it or just write "main() throws Exception"
public class _map<K extends Comparable<K>, T> extends AVL_tree<map_data<K,T>>{   // K - type of key. bc we search/organize elemments by key`s value

    _map(){}

    _map(K key, T data){
        map_data Data = new map_data(key, data);
        AVL_tree<map_data<K, T>> Tree = new AVL_tree(Data);
        this.root = Tree.root;  this.n = Tree.n;
    }
    
    //----------Required methods:

    // Coppy(phisical) constructer 
    _map(_map<K, T> src){
        AVL_tree<map_data<K, T>> copy = new AVL_tree(src);       
        this.root = copy.root;  this.n = src.n;
    }
    
    // [***]same as constructer above, BUT it`s method, so it can be used without new(). 
    // ALSO the OBJECT _map(hash string in reality) will REMAIN SAME only it's data will change
    public void copy_map(_map<K, T> src){
        _map<K, T> copy = new _map(src);
        this.root = copy.root;  this.n = src.n;
    }
    
    

    public void append(K key, T data){
        this.append(new map_data<K, T>(key, data));   // self-written method from parent class 
    }
    
    public boolean is_empty(){ return (root == null); }
    
    public void vipe(){ root = null; n = 0;}         // the rest work - will do garbadge collector :D
    

    public T get_by_key(K KEY) throws Exception{                      // get data of pair (key:data) by key 
        
        map_data<K, T> serach = new map_data(KEY, null);            // preparing input for find(Node<...> , map_data<K, T> KEY). Because our class _map has "map_data<K, T>" type of tree-node, not just K 

        Node<map_data<K, T>> indent = this.find(this.root, serach);

        try{
            return indent.data.data;}    // return data of node with needed key

        catch(Exception e){ System.out.println("\n\n       [Alert!!!] Can`t find node with such key: " + KEY.toString() + "\n"); throw e;}
    }

    public boolean is_excists(K KEY) {
        try     
            {get_by_key(KEY); return true;}
        catch(Exception e)   {return false;}
    }

    public static void map_dump(_map MAP){
        For_test_2.map_dump(MAP);
    }
}






// ------------------END--------------------- 
//  Separate class for testing. It`s in this file - to have access to map_data class(i didn`t want to make one more file where this class might be public) 



// same as For_test from AVL_tree, but specificated for map
class For_test_2{
    

    static <K extends Comparable<K>,T> String tP(Node<map_data<K, T>> node){
        if(node != null){
            map_data<K, T> pair = node.data;
            if(pair != null) return pair.toString();
        }
        return "{null}";
    }



    // print tree by 2 branches in line. -So make terminal-screen as wide as u can
    static <K extends Comparable<K>,T> void print_tree(Node<map_data<K, T>> node, int count){   
        
        if (count/2 == 0)
            System.out.println("\n");

        if (node != null)
        {
            count++;
            System.out.print(node.data.key + ":" + node.data.data + "(bal:" + node.balance + "|l-" + tP(node.l) + "|r-" + tP(node.r) + "|par-" + tP(node.par) + ") ");
            print_tree(node.l, count);

            if (count%2 == 0)                           // spacing between node couples (left, right) 
                for(int i = 0; i<count; ++i) System.out.print(" "); 

            print_tree(node.r, count);        
        }    
    }

    public static void map_dump(AVL_tree Tree){
        String sep = "---------------------------------------------------------------------------------------------------";
        System.out.println(sep + "\n    Map dump (1 line - root, 2 - left branch, 3 - right branch; node-pairs - {key:data}):");
        print_tree(Tree.root , 0);
        System.out.println("\n" + sep);
    }
}