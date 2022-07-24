import java.util.List;
import java.util.ArrayList;

class test
{
    public static void main(String[] args)
    {
        String sep = "\n-----------------------------------------------\n";
        Integer[] brr = {1, 43, 8, 3, 1337, 700, 45, 21, 435, 12, 0, -7, 32, 14};
        List<Integer> arr = new ArrayList<>(); 
        for (Integer el: brr)
            arr.add(el);


        System.out.println(sep + " -Created Queue(pyramid)" + sep);
        prior_queue<Integer, List<Integer>> Q = new prior_queue<>(arr); 
        
        for (Integer el : Q.list) 
            System.out.println(el.toString());

        System.out.println(sep + " -Adding element " + 7777 + " to Queue" + sep);
        Q.add(7777);
        for (Integer el : Q.list) 
            System.out.println(el.toString());

        System.out.println();
       
        System.out.println(sep + " -Removing top of Queue" + sep);
        Q.remove();
        for (Integer el : Q.list) 
            System.out.println(el.toString());
     
        System.out.println(sep + "Is Queue empty? \n -" + Q.is_empty() + sep);
        System.out.println(sep + "How many elements in Queue now? \n -" + Q.len() + sep);
        System.out.println(sep + "Which element has biggest priority? \n -" + Q.get_max() + sep);


}
}