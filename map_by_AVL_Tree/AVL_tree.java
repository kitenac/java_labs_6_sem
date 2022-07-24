/*
    AVL tree - BST which forms as close as possible to Complite tree(so there wouldn`t be branches that are much(>1) longer then others) 
    [*] So, AVL tree combines profit from BST and binary Heap(pyramid)


    BST(binary search tree) may cause  bastard tree (if elements are monotone up or down: 999, 845, 256, ..., 2, 1)
    
    AVL_tree - can resolove it. USSR algo - !!!!!!!!! watch Mathnet.tree - just 2-4 pages  !!!!!!!
    Requirments:
    * BST - vrode
    * each node`s subtree has branches that differs by 1 or equal   
    * property of this tree: h of tree is limited - h <= 1/log[2](phi).... - wat
    * [!!!] Building-reqursive: h+1 tree = new root + h-1_tree + h-2_tree. h =1,2,3,4 - tree is trivial 
     => m_h+2 = m_h+1 + m_h + 1 ~ Phibonachi => ~ Phi=Const - aproximation(algebra)  m_h ~ Phi^h   

    Structure:
        * pointer to next node
        * "balans" pole - difference between subtees of current node - may be (-1, 0, 1)
           => can see if balance ruins and fix
    
    Balancing:
         Helpful operations in balancing:
        * Want to swap elements by places and safe structure? 
            - "Left/Right rotate" - just googlize some .gif   - O(1)
            -  "Big Left/Right rotate" - by two smaller rots  - O(1)

        Add - to end, then check AVL property through ONLY ONE branch 
            and STOP after 1 balancing bc imbalanceness had been found ! 
        
       Delete - like in BST:
            - serch min/max el in right/left branch => switch and delete(eazy bc we`re close to end) 
            - balance

      !!! Note:  possible bad situation, which is doged in this realization:

                     N              N
                      N   ------>  N N
                       N
                    (BST)       (AVL, but not BST(N.l = N what isn`t possible in BST))
            
                    -But in this realization there wouldn`t be such situation - each node is pair: (key, val), 
                        where "key" defines comparsion(=>searching) and it`s UNIQUAL 

            */

import java.lang.Math;      // for floor(), pow(), log()




class Node <T extends Comparable<T>>{
    T data;
    Node par;            // link to parrent`s node   
    Node l;
    Node r;
    int balance;         // difference between right and left branches length - must be in [-1, 1]

    Node() {}

    Node(T el)      {  this.data = el;  balance = 0; }

    Node(Node src)   { data = (T)src.data; balance = src.balance;}

    // node-chain constructer: adds child for "parent" with "src`s" data(data + balance) containing
    Node(Node src, Node parent)  { data = (T)src.data; balance = src.balance; par = parent; }  // l and r to be handeled by user

// create
    Node(T el, Node parent)
    {
        this.data = el;
        this.par = parent;
        this.balance = 0;   }



    // ------ Methods


    // updating child on parent side after it changed(swap/...)
    // A.par = B.par - isn`t enough - we also need to affect the parent: par.l/r = A;
    // !!! "src.par = null" is skipped, bc src.par to be changed after swapping outta this function
    void give_parent(Node dst){
        dst.par = par;            // change at child side
        if (par == null) return;  // if this.par = null - it`s root <=> no parent => exit

        if (this == par.l)      // change at parrent side         
            par.l = dst; 
        else 
            par.r = dst;
        
    }
    


    // !!!! Untested: 
    public static <T extends Comparable<T>>  void insert(T el, Node parent) {
       Node ins = new Node();
       ins.data = (T)el;
       ins.par = parent;
       ins.balance = 0;

       Node old_subtree;

       if (el.compareTo((T)parent.data) >= 0){
           old_subtree = parent.r;
           parent.r = ins; }
       else{
           old_subtree = parent.l;
           parent.l = ins;  }


       if (old_subtree.data.compareTo(el) >= 0)
            ins.r = old_subtree;
       else 
            ins.l = old_subtree;

        // smth with ins.get_parent(...);                            // left and right nodes now has new parent - so changing their "par" pole
    }



}





// For better read-ableness putting all stuff with balance - into separate class
class Balance
{

    public static boolean is_valid(Node node)
    {   return (node.balance > -2 && node.balance < 2); }


// [!!!] this function assumes that tree WAS ALREADY BALANCED before we added "node"!!!       
    public static void update_branch_balance(Node node){                // NOTE: to fully understand algo - take a look at pic(p.8 in pesentation) of avl-tree with balances and imagine u add new node to end - how must be changed balances?

        if (node.par == null)       {return;}   // root => end     
                             
        if (node.equals(node.par.l))            // correcting parent`s balance,  where parent is node.par 
            node.par.balance--;                 // node - left child
        else        
            node.par.balance++;                 // node - right child
        

        if (node.par.balance == 0 || is_valid(node) == false)  {return;}   // subtree is fully(0) balnced? => all the rest tree is balanced => leave ||  
                                                                           // || if balnce isn`t valid(local imbalnce to be handeled with spectial funtion)

        update_branch_balance(node.par);        // Crawling up the branch till root
    }




//  [ALERT!] all _rot()s are related to balance() so don`t use `em separately!  Because the part(...) that`s upper then top of this subtree - to be rebalanced in balance() method 

// ------------------------------------ Moving nodes(rotating) in BST tree without loosing BST-properties                     
/*         ...                       ...
            |                         |
            A        l_rot()          B
          L   B     -----(@)-->     A   R            - helps much to understand l_rot()/r_rot()
       (@`)  C R    <--(@`)----    L C   (@)
              (@)    r_rot()     (@`)

   * @, @` - added element  - fixes by l/r_rots(), they aren`t exist in tree at SAME time - only one. 
     @, @' may be l/r child of  R(in l_rot)  or  L(in r_rot), otherwise we don`t need/can`t use l/r_rots - !!! helps understand rebalancing in l/r_rot() functions 
   * R and L are compulsory in l/r_rot - respectively.    C - optional in both rots   
   * so we see that l_rot and r_rot - are oposite operations  
            for l_rot:
                difference:  A - became LEFT children     
                const:       L C R - still leafs in the same order (not RLC or CLR ...) */

    static void l_rot(Node A){
        Node B = A.r;

        // buffering
        Node C = B.l;   
        // swap
        B.l = A;        // LEFT "rotation"
        A.r = C;        // in past B was right child. now it`s buffer - C

        // renew parents for top(B) and branch(A) on node`s side and parent`s side
        if (C != null) C.par = A;
        A.give_parent(B);           // A.par.l/r = B. due we don`t know if "l" or "r" - use .give_parent()
        A.par = B;
  
        // renew blances after rotation 
        B.balance = 0;
        
        if (A.l != null) A.balance = -1;
        else             A.balance =  0;

        if   (C != null) A.balance += 1;
        // the part upper then our subtree - to be balanced in balance() - as [Alert!] said
    }
    

    // same as l_rot, but A and B are swaped and C = right branch
    static void r_rot(Node B){
        Node A = B.l;
        Node C = A.r;   
        A.r = B;        // RIGHT "rotation"
        B.l = C;       

        if (C != null) C.par = B;
        B.give_parent(A);
        B.par = A;

        // renew blances after rotation 
        A.balance = 0;
        if (B.r != null) B.balance = 1;
        else             B.balance = 0;

        if   (C != null) B.balance -= 1;
        // ... - as [Alert!] said
    }


// just see in action: p.12 or google 
    static void Big_l_rot(Node A){
        Node B = A.r;
        
        r_rot(B);
        l_rot(A);
        // ... - as [Alert!] said
    }

// same, but l and r rots`re swaped
    static void Big_r_rot(Node A){
        Node B = A.l;
        
        l_rot(B);
        r_rot(A);
        // ... - as [Alert!] said
    }


// simple subroutins
    static void branch_inc(Node leaf){
        leaf.balance++;
        if (leaf.par != null) branch_inc(leaf.par);}

    static void branch_dec(Node leaf){
        leaf.balance--;
        if (leaf.par != null) branch_dec(leaf.par);}

// renew balance for branch that`s upper than top - assuming heigh of our subtree decreased by 1 after balancing by some of "_rot"s - see balance() method
    static void renew_aftr_bal(Node top){       // "top" - node that`s on top of rebalanced subtree
        if (top.par != null)
        {
            if (top == top.par.l) 
                branch_inc(top.par);
            else  
                branch_dec(top.par);
        }
    }



// --Сhoise between big/small left/right "rotations" to correct balance ~ manager 
    public static void balance(Node imba){                               // node "added" causes imbalance in "imba" node
//     |  we have 2 "dimensions" of rotations:                          |
//     |       1) side(l/r) - depends on sign(-/+) of .balance pole,    |
//     |       2) type(simple/big) - if we can`t do simple => do big    |

       
        if (imba.balance < 0){          // left is heavy => rot side - right
            if(imba.l.balance == -1)    // try to make rot type - simple | Ask yourself - when can`t i make rot and must use Big_rot? - see pictures at p.10-11 
                r_rot(imba);    
            else                        // "forced" to use type - big
                Big_r_rot(imba); }
        else{                           // right is heavy => rot side - left
            if(imba.r.balance == 1)     // same
                l_rot(imba);                 
            else
                Big_l_rot(imba); }
        
        renew_aftr_bal(imba.par);       // after balancing  imbalanced(in past) node "imba" becomes child of new top. But top - not always root, so we must update balance for branch, which contains rebalanced subtree. Assuming that subtree`s heigh decreased by 1
    }                           



}






public class AVL_tree<T extends Comparable<T>> {
    Node root;
    int n;          // ammount of elements in tree


    // --------Various constructors
    AVL_tree()
    { n = 0; root = null; }


    AVL_tree(T el){
        root = new Node(el);
        n = 1;    }



    // subroutines for copy constructer
    void CP(Node src, Node dst){
        // copping data-related(not links) poles - this step can`t be done into reqursion, so do it here as start
        dst.data = src.data;
        dst.balance = src.balance;

        req_CP(src, dst);           // reqursive copy(physically - not just by link)
    }
    void req_CP(Node src, Node dst){    
        if (src.l != null)  { Node add = new Node(src.l, dst); dst.l = add; req_CP(src.l, add); }
        if (src.r != null)  { Node add = new Node(src.r, dst); dst.r = add; req_CP(src.r, add); }
    }

    // copy-constructer
    AVL_tree(AVL_tree src) 
    {
        n = src.n;
        root = new Node();
        CP(src.root, root);     // phisically copping all nodes and their chaining

    }




    // this func is needed to start balance(). it finds node with invalid balance after we added new node  
    Node find_invalid(Node buble){  // at start of reqursion "buble"(goes up branch likewise) - is node we have appended/inserted, so it could ruin balance
        
        if (buble.par == null)
            return null;

        else if (!Balance.is_valid(buble.par))   
            return buble.par;
        else 
            return find_invalid(buble.par);
    }
    


    // Appending new element to tree without rebuilding tree(in case element haven`t occured yet)
    // This func is much related with get_parent()!!! - it returns build point
    Node get_parent(T el, Node node_i)      // To fully understand this algo - look attentively on some picture of tree and ask yourself - "how to add new node?"  
    {

        if (el.compareTo((T)node_i.data) >= 0)          // Try to look right subtree
        {
            if (node_i.r == null)                       // If can`t go, but need to move => we found place to add new one
                return node_i;
            else
                return get_parent(el, node_i.r);        // Here we go forward until can
        }
         
        else                                            // Try to look left subtree
        {
           if (node_i.l == null)                        // If can`t go, but need to move => we found place to add new one
              return node_i;
          
           else
               return get_parent(el, node_i.l);
        }

        
    }



// [!!!] this function assumes that tree WAS ALREADY BALANCED before we added new node!!!                     
    public void append(T el)
    {       
        n++;                                                            // bc we add element size increments
        if (n == 1) {root = new Node(el); return;}                      // if our tree was empty before appending elemnet

        // getting parrent for new node due BST logic
        Node parent_node = get_parent(el, root);                    

        Node add_node = new Node(el, parent_node);                      // creating node with known parent 

        // "telling" parent if our new node is left or right 
        if (el.compareTo((T)parent_node.data) >= 0)                       
            parent_node.r = add_node;                     
    
        else 
            parent_node.l = add_node;                        

        
        Balance.update_branch_balance(add_node);                       // correcting balance after adding node

       
        Node invalid = find_invalid(add_node);
        if (invalid != null){
            System.out.println(" " + add_node.data.toString() + " -causes invalid balance for " + invalid.data.toString() + "(balance = " + invalid.balance + ")");
            Balance.balance(invalid);   // balance() must be called EACH TIME we appended 1 new el - to keep AVL properties
            System.out.println("    [*]Balance fixed for " + invalid.data.toString() + " (balance = " + invalid.balance + ")");
            System.out.println("    [*]for el " + invalid.par.data.toString() + " children now: " +invalid.par.l.data.toString()+ "(balance = " + invalid.par.l.balance +"), " + invalid.par.r.data.toString() + "(balance = " + invalid.par.r.balance + ")");

            while (root.par != null) root = root.par;  // after balancing root might be rotated, so in fact it changed   
        }
        
        else
            System.out.println(" " + add_node.data.toString() + " -OK");

    }


    public Node<T> find(Node<T> node_i, T el)         // Returning node of tree element which .data == el or null if there`s no such node 
    {
        if (node_i == null)
            {return node_i;}
        
        if (el.compareTo(node_i.data) == 0)
            {return node_i;}
        

        if (el.compareTo(node_i.data) < 0)
            return find(node_i.l, el);
        else
            return find(node_i.r, el);
    }


    public static void tree_dump(AVL_tree Tree){
        For_test.Tree_dump(Tree);
    }



    // n = 1 by start!!!
    Integer sub_n(Node subroot, Integer n){
        if(subroot.l != null)
          { n++; n = sub_n(subroot.l, n);}

        if(subroot.r != null)
          { n++; n = sub_n(subroot.r, n);}

        return n;
    }

    public Integer get_n(){
        return sub_n(root, 1);
    }



}

// --------------------------------- END of logical part ---------------------------------------------------------------------

// Part for tests
class For_test{


    static String tP(Node node) // tP - try print
    {
        if(node != null) return (node.data).toString();
        else return "null";}


    // print tree by 2 branches in line. -So make terminal-screen as wide as u can
    static void print_tree(Node node, int count){   
        
        if (count/2 == 0)
            System.out.println("\n");

        if (node != null)
        {
            count++;
            System.out.print(node.data + "(bal:" + node.balance + "|l:" + tP(node.l) + "|r:" + tP(node.r) + "|par:" + tP(node.par) + ") ");
            print_tree(node.l, count);

            if (count%2 == 0)                           // spacing between node couples (left, right) 
                for(int i = 0; i<count; ++i) System.out.print(" "); 

            print_tree(node.r, count);        
        }    
    }

    static void Tree_dump(AVL_tree Tree){
        String sep = "------------------------------------------------------------------------------";
        System.out.println(sep + "\n    Tree dump (1 line - root, 2 - left branch, 3 - right branch):");
        print_tree(Tree.root , 0);
        System.out.println("\n" + sep);
    }
}









