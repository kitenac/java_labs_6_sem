
class test{
    public static void main(String[] args){
        
        String sep = "\n-----------------------------------------------------\n";

        Hash_map <Integer, String> idk = new Hash_map(2);
        System.out.println(sep + "Created Hash_map of 2 lists");
        System.out.println(sep + "Let`s add 3 elements into it Hash_map\n");

        idk.add(14, "Pizza");
        idk.add(25, "Pel`meny");
        idk.add(3, "Borsch");

        idk.map_dump();

        System.out.println(sep + "Let`s add 4 more elements into it(and see rebuild)\n");
        idk.add(11, "Pie");
        idk.add(10, "Potato");
        idk.add(9, "Cheese");
        idk.add(8, "Bread");


        System.out.println(sep +"Let`s see Hash_Map after expanding and rehashing");
        idk.map_dump();

        System.out.println(sep + "Who has key 14? -" + idk.get_by_key(14));
        
        System.out.println(sep +"Let`s delete element with key = 3");
        idk.remove_by_key(3);
        idk.map_dump();

        System.out.println(sep + "Let`s change rate:");
        idk.decrease_rate();        

        System.out.println(sep + "How many elements do we have? " + idk.get_size());

        System.out.println(sep + "Let`s rehash elemnts from hash map:\n");
        idk.decrease_rate();
        idk.map_dump();

        System.out.println(sep + "Let`s delete all elements:");
        idk.delete();
        idk.map_dump();

    }

}