
import java.util.List;

public class test{

        public static void main(String[] args){
           
            String sep = "-----------------";
            System.out.println(sep + " Rabin-Karp algorithm: \n");
            List<Substring_Finder.Synt_String> Matches = Substring_Finder.Rabin__Karp("sin", "assassin - sinner", 67);

            System.out.println(" \nOccurences: ");
            for (Substring_Finder.Synt_String el : Matches)
                System.out.println(" ==== " + el);

    
            System.out.println(sep + "\n Boyer-Mur algorithm: ");
            Matches = Substring_Finder.Boyr__Mur("ATA", "HfaoakATA_ATATATA_aabdbababdbab");
            System.out.println("\n\n Matches:");
            for (Substring_Finder.Synt_String el : Matches)
                System.out.println(" ==== " + el);


            System.out.println("\n\n" + sep + "\nBoyer-Mur algorithm:\n");
            Matches = Substring_Finder.Knuth_Morris_Pratt("rnm", "losagsrnmigbstigovernment");
            System.out.println("Matches in text \"losagsrnmigbstigovernment\",  substring: \"rnm\"");
            for (Substring_Finder.Synt_String el : Matches)
                System.out.println(" ==== " + el);

    }


}