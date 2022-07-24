
/*
Hash Map - dynamic array of linked-lists:
      __________
     |__list 1__| <------>  {k_1, d_1} --- {k_2, d_2} ----- ...
      __________
     |__list 2__| <------>  {k`_1, d`_1} --- {k`_2, d`_2} ----- ...
        ...... 
      __________
     |__list m__| <------> ...

 Vars:
    m - number of lists, n - of elements, rate koeff = m/n (averege ammount of elements in list)
    el = {key, data} 
 Theor:
    Artifficaly cut possible set of hash values: "u_hash" =  hash(el.key) mod m - this rule is in lab`s requirements and may be different. 
      -Why do we use hash? - because it`s randomness => ~uniform distribution => each list would have ~same ammount of elements = rate koeff = m/n 

    each list has uniqual "u_hash". due our realization it`s in [0;m-1], bc:
    each elemaent el - to be put in idx = hash(el.key) mod m -th list 

    m - can rise or decrease while it`s "life" AND it defines where(in which list) to put element with some hash 
        <=> ( increasing m <=> decreasing current rate = m/n)
       -So we can keep rate_koeff < max_rate (max avverege number of elements in each list)  
 How to find el in hash map:
    1) calcilate  "u_hash" =  hash(el.key) mod m = idx of el`s list
    2) seek in idx-st list element with such key = el.key 

*/




// all bellow`re  data structures allowed(and required to be from libs) in lab:
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator; // eazy way(and the only if to talk about library structure with closed poles) to crawl through data structure  - LinkedList


// {key:data} pair - content of list
class k_d_pair <K extends Comparable<K>, V> {
    K key;
    V data;

    k_d_pair() {};
    k_d_pair(K k, V dat) { key = k; data = dat; }
    k_d_pair(k_d_pair<K, V> pair) { key = pair.key; data = pair.data; }


    @Override
    public String toString() {
        return "{" + key + ":" + data + "}";
    }
}




// By this interface we can use custom hash function by @Overrite hash()
interface Hash{
    int hash(Object o);
}


// !!! U can change hash-function(@Overrite i.e. in childclass) hash() from interface Hash.     by default it`s .hashCode()
public class Hash_map<K extends Comparable<K>, V> implements Hash{
    List< LinkedList<k_d_pair<K, V> > > Table;  // hash-table.    p.s Don`t be confused about <<<>>> stuff - it`s array of linked-lists with elements typed as key-value pairs (<K, V>) 
    int m;                                      // amount of lists in table
    int n;                                      // total ammount of elements in entire table

    float max_rate = 2.0f;                      // koeff = m/n - upper border of elements per 1 averege list <=> averege number of hash collisions.  p.s. 2.0 - set by lab`s requirements, also it`s in [0.5, 2.0]

    
    public int hash(Object o){
        return o.hashCode();                    // default hash method - can be @Overrite into different one
    }

    Hash_map(){
        Table = (List< LinkedList<k_d_pair<K, V>> >) new ArrayList< LinkedList<k_d_pair<K, V>> >();     // Also don`t be confused))) - creating table: Upcast new Array_list to List with same <> part
        m = 0; n = 0;
    }

    // Create Hash map of m linked-lists <=> of m possible "u_hash" values - [0;m-1]
    Hash_map(int sz){
        Table = (List< LinkedList<k_d_pair<K, V>> >) new ArrayList< LinkedList<k_d_pair<K, V>> >();     // watch same - above
        m = sz; n = 0;
        for (int i = 0; i < m; ++i)
            Table.add(new LinkedList<k_d_pair<K, V> >() );
    }



    boolean rate_is_ok()    {  return ((float)n/(float)m <= max_rate); }
    boolean rate_will_ok()  {  return ((float)(n+1)/(float)m <= max_rate); }
    int get_idx(K key)      {  return hash(key) % m; }


    void delete()           {  Table = null; m = 0; n = 0; }

    float get_rate()        {  return (float)n/(float)m;  }
    int get_size()          {  return n;}



// --------------Subroutines for .add() method and some of required ones:
   
    // reallocating memory for n elements of array. used in rebuild()
    void resize(int new_m){
        Table = (List< LinkedList<k_d_pair<K, V>> >) new ArrayList< LinkedList<k_d_pair<K, V>> >();         // Clearing Table <=> recreate it
        m = new_m;                                                                                        // 2m+1 - required rule and n - not changes here(ammount of elements still same)
        n = 0;      // we haven`t yet add any element - it`ll be in rebuild: it uses push - which increases n
        for (int i = 0; i < m; ++i)
            Table.add(new LinkedList<k_d_pair<K, V> >() );
    }

    // Used to make new hash Table of same elements, but bigger size. Elements`re to be put in different lists(ammount of list = m grows => "idx-rule" changes)  
    public void rebuild(int new_m){
        if (new_m <= m)     {  System.out.println("Choose bigger size of Hash-table if u want to rehash Table"); return;}
        
        int old_m = m;
        List< LinkedList<k_d_pair<K, V>> > Buffer = (List< LinkedList<k_d_pair<K, V>> >) new ArrayList< LinkedList<k_d_pair<K, V>> >(Table);  // Phisical coppy of List by copy-constructer. ps "<> part" same as in constructer above
        resize(new_m);                                  // Phisicaly allocating memory up to 2*m + 1 lists. 
        
        for (int i = 0; i < old_m; ++i){                // find places for elements from m Table in new 2*m+1 Table    
            k_d_pair<K, V> pair;
            ListIterator<k_d_pair<K, V>> iter = Buffer.get(i).listIterator();

            while (iter.hasNext()){
                pair = iter.next();
                this.push(pair.key, pair.data);}
        }    
    }

    
    // brutal adding of {key:data} pair in Table
    void push(K key, V val){
        int idx = get_idx(key);
        System.out.println("Chosen " + idx + "-th list to add " + "{" + key + ":" + val + "}" );
        Table.get(idx).addLast(new k_d_pair(key, val)); 
        n++;                                            // ammout of elements incresed
        System.out.println("   Current rate_koef = " + get_rate());
    }

    // "smart" adding.              p.s. pole "n" is already "ruled" by "push()", which "add()" uses here
    void add(K key, V val){
        if (rate_will_ok()) 
            push(key, val);
        else{
            System.out.println("\n  !!! Rebuilding Hash table due rate_koef will come = " + (float)(n+1)/(float)m + "\n");
            rebuild(2*m + 1);   // recomended parameter
            add(key, val); }
    }

    

    // Method to decrease rate
    public void decrease_rate() {   rebuild(2*m + 1);  System.out.println("\n***Rate decreased down to " + get_rate()); }   // expand Table => rate decreases after rehash(it`s part of rebuilt).   


    V get_by_key(K key){
        int idx = get_idx(key);                                             // getting idx of list where must be el we search for 

        k_d_pair<K, V> pair;                                                // need in cycle
        ListIterator<k_d_pair<K, V>> iter = Table.get(idx).listIterator();  // Itterating in idx-th list 
        while (iter.hasNext()){
            pair = iter.next();
            if (key == pair.key)
                return pair.data;
        }

        System.out.println("  !!! No such element with key = " + key.toString());
        return null;
    }
    

    void remove_by_key(K key){
        boolean find = false;                                       // flg to check if there is element with given key
        int idx = get_idx(key);

        LinkedList<k_d_pair<K, V>> list_i = Table.get(idx);
        if (list_i == null) return;
        
        k_d_pair<K, V> pair = null;                                 // need in cycle
        ListIterator<k_d_pair<K, V>> iter = list_i.listIterator();  // Itterating in idx-th list 

        while (iter.hasNext()){
            pair = iter.next();
            if (key == pair.key)
                {find = true; break;}
        }

        if (find == true)
            {n--; list_i.remove(pair);}    // pair is stoped value in cycle above
        else 
            System.out.println("No element with such key = " + key);
    }    




// just pretty output 
    public void map_dump(){

        System.out.println("\n======= Hash map dump ==========\n (each line represents sequanse from same i-th sublist of Hash table, where i = hash(el) mod m) ");
        k_d_pair<K, V> pair;
        for (int i = 0; i < m; ++i){
            System.out.print("\ni:" + i + "|  ");
            ListIterator<k_d_pair<K, V>> iter = Table.get(i).listIterator();
            while (iter.hasNext()){
                pair = new k_d_pair(iter.next());
                System.out.print("---" + pair);}
        }

        System.out.println("\n\n - Current Hash_map`s rate = " + get_rate());
        System.out.println("===============================\n");
    }

}





