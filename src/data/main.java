package data;

import java.util.ArrayList;
import java.util.HashMap;

public class main
{

        public static void main(String [] args)
        {
                HashMap <String, Instance>map;
                Space it = new Space ();
                map = it.init("/workspace/mining.txt");
               // System.out.println(map.get("A1").getY());
//                System.out.println(map.get("A1"));
               // System.out.println(map);
                HashMap<String, ArrayList<String>> grid = it. initGrid (  map );
                HashMap<String, HashMap<String, HashMap<String, HashMap<String, ArrayList<Double>>>>> starNeighbor;  //星型邻居表
                starNeighbor = it.initStarNeighbor(grid, map);
                ArrayList <ArrayList<String>> frequent = new ArrayList <ArrayList<String>> ();      //频繁模式
                
                
                
        }

}
