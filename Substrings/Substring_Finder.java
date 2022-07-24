

/*

    Formaly we seek for str of length = l in Text => Text[s+l] = str

    !!!!!!!!!! 
        Пересечения - нужно учитывать, как вхождение: текст aaa - даст два вхождения "а"
    !!!!!!!!!!!!!!!


    Rabin-Karp:
        using SPECIAL hash-function that can be recount from previous step(substring of text) hash:
            have text abagf watch str = ag
        
        in lec 10 - as base - bc we had only digets(0-9) => watch explonation after this (общий случай)

    
    Finate automat:
        can be represent as graph of possible input/output as edge and dots as states of automat

        each symbol of text(not earch str) assumes as one of m levels => we have 0 - none,1 - text[0], 2 - text[0-1],,..., m - text 

        Representation - see suffix function

    
    Boyer-Mour:
        
    First 
        Stop-symbol
        table of stop places(idx in text) for each symbol of str except last(anyway compare it)

    Better:   - Смотри примеры, особенно последние
        Stop-suffix
        table of ---||--- 

    So table defines how many symbols right we must move str ("move" - relate with text)


*/


import java.util.ArrayList;
import java.util.List;
import java.lang.Math;


public class Substring_Finder{

    // string and it`s start`s position in text
    public static class Synt_String{
        int possition;
        String str; 
        Synt_String(String s, int pos)      {str = s; possition = pos;}
        
        @Override
        public String toString(){
            return "[pos: " + possition + "; str: " + str + "]";}
    }



    public static class Rabin_Karp{
        String T;  String P;      // T - text, P - searched substring 
        int n;     int m;         // Size of T and P
        int d;     int d_m;       // d - size of text`s alpabet, d_m = d^(m-1) mod q
        int q;                    // q - module of Z/q
        int h;                    // h = special_hash(P)
        int pos;                  // possition(idx) in text 

        Boolean is_visited(String ch, List<String> lst){
            for (String el : lst)
                if (el.equals(ch)) 
                    return true;
            return false;    }


        int get_alphabet_size(){
            List<String> Visited = (List<String>) new ArrayList<String>();
            for (int i = 0; i < T.length(); ++i)
                if (!is_visited( Character.toString(T.charAt(i)), Visited))    // don`t be confused by  Character.toString(T.charAt(i)) - just making string from char, which is i-th simol in T  
                    Visited.add(Character.toString(T.charAt(i)));          
            
            return Visited.size();  }

        // first hash 
        int requrent_hash(String str){
            int h_ = 0;
            
            for (int i = str.length() - 1; i >= 0; --i)
                {h_ += (int)Math.pow(d, i) * str.charAt(str.length() - 1 - i); }
            

            return h_%q;}   // we`re Z/q   

        // fixing Java`s rule for "%" - it can return negative vals
        int fix_mod(int mod){return mod<0 ? q + mod : mod;}

        // each next hash after first. 
        int next_h(int h_){
            return fix_mod(  (d * (h_ - d_m*T.charAt(pos-1)) + T.charAt(pos + m - 1)) % q  );   // watch lec #1 about substrings ; pos - idx in text after we had rotated P
        }               

        // rotate right => start and end simbols will be
        String next_P(String P_){
            pos++;  // moving cursor right
            String n_P = P_.substring(1, P.length()) + Character.toString(T.charAt(pos + P.length() - 1));     // moving P through T right by 1 simbol = P_
            return n_P; 
        }

        public Rabin_Karp(String P, String T, int q){
            this.P = P; this.T = T; this.q = q;
            d = get_alphabet_size();
            m = P.length(); n = T.length();
            d_m = (int)Math.pow(d, m-1) % q;
            h = requrent_hash(P);
            pos = 0;   }

            List<Synt_String> Rabin_Karp_(){

            List<Synt_String> Matches = (List<Synt_String>) new ArrayList<Synt_String>();

            String P_i = T.substring(0, P.length());     // start of text of P`s length 
            int h_i = requrent_hash(P_i);
            if (h_i == h)
                if (P_i.equals(P))
                    Matches.add(new Synt_String(P, 0));    
            System.out.println("req_hash(" + P + ") = " + h + "\n   " + P_i + "  pos = " + pos + "  hash_i = " + h_i);

            for (int i = 1; i <= n - m; ++i){  // all possible rottations
                P_i = next_P(P_i);
                h_i = next_h(h_i);
                System.out.println("   " + P_i + "  pos = " + pos + "  hash_i = " + h_i);
                if (h_i == h)
                    if (P_i.equals(P))
                        Matches.add(new Synt_String(P, pos));  }

            return Matches;
        }
        
    }
    

    public static List<Synt_String> Rabin__Karp(String P, String T, int q){
        Rabin_Karp Algo = new Rabin_Karp(P, T, q);
        System.out.println("searching substring = " + Algo.P +"\ntext = " + Algo.T + "\n(text`s alphabet length = " + Algo.d + ")\n");
        return Algo.Rabin_Karp_();
    }






    public static class Boyr_Mur{
        String T; String P;
        List<Synt_String> Stop_tbl; List<Synt_String> Suff_tbl;


        Boolean is_visited(String ch, List<String> lst){
            for (String el : lst)
                if (el.equals(ch)) 
                    return true;
            return false;    }
        
        List<String> get_alphabet(){
            List<String> Visited = (List<String>) new ArrayList<String>();
            for (int i = 0; i < T.length(); ++i)
                if (!is_visited( Character.toString(T.charAt(i)), Visited))    // don`t be confused by  Character.toString(T.charAt(i)) - just making string from char, which is i-th simol in T  
                    Visited.add(Character.toString(T.charAt(i)));          
            
        return Visited;  }
     
        void fill_stop_tbl(){
            for (int j = 0; j <= P.length() - 2; ++j)       // idk why P.length() - 2, but that`s the rule
                for (Synt_String el : Stop_tbl)
                    if (el.str.equals( Character.toString(P.charAt(j)) ))
                            el.possition = j;   }

        // there must be prefix_function(), but i forget about it and spend realy much time with realization bellow.  ps prefix-function is done at the end(Knutt_Morris_Pratt) 
        void fill_suff_tbl(){
            String sub_suf; String watch;
            int j = 0;                                                              // j = 1 - first step of for. bc suff[0] = "" - no need to fill in 
            for(Synt_String suff : Suff_tbl.subList(1, P.length() + 1)){            // taking all Suff_tbl`s elements except 0-th = "" 
                j++;    
                for (int i = j + suff.str.length(); i < Suff_tbl.size(); ++i){      // i - idx in array of subsuffixes

                    sub_suf = Suff_tbl.get(i).str;  
                    watch = sub_suf.substring(0, suff.str.length());                // cutten sub_suffix to size of our suff

                    System.out.println("   suff: " + suff.str + "    i = " + i + "  sub_suf:" + sub_suf + "  cutten sub_suf:"+ watch);

                    if (suff.str.equals(watch))
                        {
                            System.out.println("subsufix and sufix: " + sub_suf + " | " + suff.str);    
                            suff.possition = sub_suf.length() - suff.str.length();  // look at p.7 in lecture and you`ll understand(ammount of "stars - * " is equal to ammount of symbols that`re overroted right)
                            break;
                        }
                }
            }
        }

        Boyr_Mur(String P, String T){
            this.T = T; this.P = P;

            List<String> alphabet = get_alphabet();

            Stop_tbl = (List<Synt_String>) new ArrayList<Synt_String>();
            Suff_tbl = (List<Synt_String>) new ArrayList<Synt_String>();

            for (int i = 0; i < alphabet.size(); ++i)
                Stop_tbl.add( new Synt_String(alphabet.get(i), -1));


            Suff_tbl.add(new Synt_String("", 1));               // empty sting <=> no such suffix in table => move 1 simbol right
            String suff = ""; 
        
            for (int i = P.length() - 1; i >= 0; --i){          // iversed order
                suff = Character.toString(P.charAt(i)) + suff;
                Suff_tbl.add(new Synt_String(suff, P.length()));
            }
            fill_stop_tbl();
            fill_suff_tbl();
        }

        int max(int a, int b)   { return  a>b ? a : b;}
        
        int get_idx(String A, List<Synt_String> alphabet){
            for (Synt_String el: alphabet)
                if (el.str.equals(A)) 
                    return el.possition;
            return 1;             // if no such suffix occured - just move forward.     ps  if alphabet == Stop_tbl there`s no way to A(literal-string) not to be in Stop_table => this root is only for suffixes
        }



        List<Synt_String> Boyr_Mur_(){

            int delta_stop;     // step by stop-table 
            int delta_suff;     // .... by suff-table
            int i, j, t_i;
            List<Synt_String> Resault = (List<Synt_String>) new ArrayList<Synt_String>();
            
            System.out.println("\n            Starting Boyer-Mur algorithm(each step we choose one of euristics - that one gives biggest shift right)\n             where pos = i - possition in Text\n             The text:" + "\"" + T + "\"\n");

            for (i = 0;i < T.length() - P.length() + 1; ){  // while i can be start of suffix
                
                System.out.println("   pos = " + i + ": " + T.charAt(i));
                
                for (j = P.length() - 1; j > 0; --j)       // basicaly look on P and T_i while they are same
                    if (!(P.charAt(j) == T.charAt(i+j)))
                        break;
                
                if (j == 0){ // "for" above worked all times <=> P and T_i are same <=> we`ve found match
                    Resault.add( new Synt_String(P, i));
                    delta_stop = 1;
                }

                else{
                    t_i = get_idx( Character.toString(T.charAt(i+j)), Stop_tbl);      // idx of T[i+j] in Stop_tbl
                    delta_stop = j - t_i;}

                
                
                delta_suff = get_idx(T.substring(i, i + (P.length() - j)) , Suff_tbl);  // idx of suffix T[i;i+j] in Suff_tbl
               
                System.out.println(" delta_stop, delta_suff = "+ delta_stop + ", "+ delta_suff + "   suffix_i: " + T.substring(i, i + (P.length() - j)));
                i += max(delta_stop, delta_suff);           // move as far as we can(due to stop-symbol or suffix euristics)
              
            }
            return Resault;
        }

    }


    public static List<Synt_String> Boyr__Mur(String P, String T){

        System.out.println("\n\n            Building Suffix-table: ");
        Boyr_Mur Algo = new Boyr_Mur(P, T);

        System.out.println("\n\n Stop-table: ");
        for (Synt_String el: Algo.Stop_tbl)
            System.out.print(el + " ");        

        System.out.println("\n\n Suffix-table:");
        for (Synt_String el: Algo.Suff_tbl)
            System.out.print(el + " ");
        System.out.println();

        return Algo.Boyr_Mur_();
    }


    // Returns table "pi": prefix function`s value(int) for each S[0:i]
    // prefix function(i): returns length of max prefix that is also excists as suffix in P[0:i] 
   public static List<Integer> prefix_function(String S, List<Integer> pi){
        pi.set(0, 0);
        int k = 0;                                       // ammount of matched symbols in a row (of prefix and suffix)
        for (int i = 1; i < S.length(); ++i){            // i = 1 bc we need some rot, otherwise(i=0) matching doesn`t counts  
            while (k>0  &&  S.charAt(k) != S.charAt(i)) 
                k = pi.get(k-1); 
            if (S.charAt(k) == S.charAt(i)) 
                k++;

            pi.set(i, k);                                // current ammount of matched simbols
        }
        return pi;
    }


    public static List<Synt_String> Knuth_Morris_Pratt(String P, String T){
        List<Synt_String> Results = (List<Synt_String>) new ArrayList<Synt_String>();
        List<Integer> pi = (List<Integer>) new ArrayList<Integer>();    for(int i = 0; i < P.length(); ++i)     pi.add(0);   // Table of prefix function for each P[0:i]
        
        pi = prefix_function(P, pi);
        int k = 0;                                          // ammount of matched symbols in a row (of P and substring from T)

        for (int i = 0; i < T.length(); ++i){               // watching all Text
            while (k > 0 && P.charAt(k) != T.charAt(i))     // try to find matching simbols. if k <= 0 - then there`s no idx = k-1 - watch bellow:   
                k = pi.get(k-1);                            
            
            if (P.charAt(k) == T.charAt(i))  k++;           // P[k] == T[i] => move forward <=> k++; i++(in next cycle)
            if (k == P.length()){                           // all simbols matched => found occurance
                Results.add( new Synt_String(P, i - P.length() + 1));   // add idx of P`s start(in T)
                k = pi.get(k-1);
            }
        }
        return Results;
    }

}
















