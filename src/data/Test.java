package data;

import java.util.ArrayList;
import java.util.HashMap;

public class Test
{

        public static void main(String [] args)
        {
                HashMap <String, HashMap <String, HashMap <String, HashMap <String, ArrayList <Double>>>>> map 
                        = new HashMap<String, HashMap <String, HashMap <String, HashMap <String, ArrayList <Double>>>>>();
                
                ArrayList<Double> list1 = new ArrayList<Double>();
                list1.add (30.0);
                list1.add (20.0);
                list1.add (25.0);
                list1.add (50.2);
                
                HashMap<String, ArrayList<Double>> leafInst = new HashMap <String, ArrayList<Double>>();
                leafInst.put ("B1", list1);
                leafInst.put ("B2", list1);
//                leafInst.put ("C1", list1);
                HashMap <String, HashMap <String, ArrayList<Double>>> leafFeat = new HashMap <String, HashMap<String, ArrayList<Double>>>();
                leafFeat.put ("B", leafInst);
                HashMap<String, HashMap <String, HashMap <String, ArrayList<Double>>>> rootInst = new HashMap<String, HashMap <String, HashMap <String, ArrayList<Double>>>>();
                rootInst.put ("A1", leafFeat);
                HashMap <String, HashMap<String, HashMap <String, HashMap <String, ArrayList<Double>>>>> rootFeat = new HashMap <String, HashMap<String, HashMap <String, HashMap <String, ArrayList<Double>>>> >();
                rootFeat.put ("A", rootInst);
                
                System.out.println (rootFeat);
                

        }

}
;