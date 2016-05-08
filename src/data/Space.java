/**
 * @author changchun
 */
package data;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import data.Instance;

public class Space
{
        public double Rmax = 0;
        public double size = 0;
        int row_list = 0;                                                                        //网格行列个数
        
        /**
         * 读取文件中的数据
         * @param filePath      文件的目录
         * @return
         */
        public HashMap<String, Instance> readTxt (String filePath)
        {
                String[]  txtArr = new String [5];
                HashMap<String, Instance> map = new HashMap<String, Instance>();
                
                try
                {
                        String encoding = "utf8";
                        File file = new File (filePath);
                        if (file.isFile() && file.exists())
                        {
                                InputStreamReader read = new InputStreamReader (new FileInputStream (file), encoding);
                                BufferedReader bufferedReader = new BufferedReader(read);
                                String lineTxt = null;
                                if ((lineTxt  = bufferedReader.readLine()) != null)
                                        this.size = Double.parseDouble(lineTxt);
                               // System.out.println(this.size);
                                while ((lineTxt  = bufferedReader.readLine()) != null)
                                {
                                        txtArr = lineTxt.split("\\s+");                         // 空格分割
                                        if (Double.parseDouble(txtArr[4]) > this.Rmax)          // 比较rmax
                                        {
                                                this.Rmax  = Double.parseDouble(txtArr[4]);          // rmax
                                        }
                                        map.put(txtArr[1]+txtArr[0],                    // 每个点生成一个实例对象，并且存储到hashmap中，eg: key: A1, val:instance
                                                        new Instance (txtArr[1]+txtArr[0],                      // 实例名称
                                                                        Double.parseDouble(txtArr[2]),          // x坐标
                                                                        Double.parseDouble(txtArr[3]),          // y 坐标
                                                                        Double.parseDouble(txtArr[4]),          // r
                                                                           txtArr[1]                                                    // 特征
                                        ));
                                        
                                }
                                read.close ();
                        }
                        else
                        {
                                System.out.println ("can not find file");
                        }
                }
                catch (Exception e)
                {
                        System.out.println ("read file error");
                        e.printStackTrace ();
                }
                return map;
        }
        
        /**
         * 将文件中的数据，初始化到map中
         * @param dst
         */
        public HashMap<String, Instance> init (String dst)
        {
                HashMap <String, Instance> map;
                map = readTxt(dst);

                return map;
        }
        
        /**
         * 初始化棋盘
         * @param map <hashmap>
         * @return
         */
        public HashMap<String, ArrayList<String>>  initGrid ( HashMap<String, Instance> map )
        {
                double d = 2 * this.Rmax;                                                     //网格粒度
                //System.out.println(d);
                HashMap<String, ArrayList<String>> grid = new HashMap<String, ArrayList<String>>();                                        //网格列表
               
                if ( this.size%d == 0 )
                {
                        Double d_s = new Double( this.size/d );
                        this.row_list = d_s.intValue() ;
                }
                else
                {
                        Double d_s = new Double( this.size/d + 1 );
                        this.row_list = d_s.intValue();
                }
                for (int i = 0; i <this. row_list; i++)                        //初始化grid           
                        for( int j = 0; j < this.row_list; j++)                        
                                grid.put("["+i+","+j+"]", new ArrayList<String>());  
                //System.out.println(grid);
                                                     
                String temp = null;
                Iterator iter = map.keySet().iterator();  
                while(iter.hasNext()) {                         
                        String key = (String)iter.next();                     
                        Instance ins = (Instance)map.get(key);
                        temp = null;
                        Double d_s = new Double( ins.getX()/d );
                        int j = d_s.intValue();
                        d_s = new Double( ins.getY()/d );
                        int i = d_s.intValue();
                        temp =  "[" + i + "," + j + "]";                
                       grid.get(temp).add(key);                                       
                } 
             //System.out.println(grid); 
                return grid;
        }
        
        /**
         * 生成邻居集
         * @param grid  棋盘
         * @param map   
         * @return
         */
        public HashMap <String, HashMap <String, HashMap <String, HashMap <String, ArrayList <Double>>>>> initStarNeighbor(
                        HashMap <String, ArrayList <String>> grid, HashMap <String, Instance> map)
        {
                HashMap <String, HashMap <String, HashMap <String, HashMap <String, ArrayList <Double>>>>> starNeighbor = new HashMap <String, HashMap <String, HashMap <String, HashMap <String, ArrayList <Double>>>>>();
                String temp;
                ArrayList <String> itself;
                ArrayList <String> neighbor;
                ArrayList <Double> crossPoint;

                for (int i = 0; i < this.row_list; i++)
                {
                        for (int j = 0; j < this.row_list - 1; j++)
                        {
                                temp = "[" + i + "," + j + "]";
                                itself = (ArrayList <String>) grid.get(temp);
                                neighbor = getNeighbor(i, j, grid);

                                for (int m = 0; m < itself.size(); m++)
                                {
                                        for (int n = 0; n < itself.size(); n++)
                                        {
                                                if (map.get(itself.get(m)).getf()
                                                                .compareTo(map.get(itself.get(n)).getf()) < 0) // 字典序，比较
                                                {
                                                        crossPoint = pre_cross(map.get(itself.get(m)),
                                                                        map.get(itself.get(n))); //
                                                        if (!crossPoint.isEmpty()) // 如果两个实例相交
                                                        {
                                                                System.out.println(crossPoint);
                                                                if (!starNeighbor.containsKey(
                                                                                map.get(itself.get(m)).getf()))
                                                                {
                                                                        HashMap <String, ArrayList <Double>> hm1 = new HashMap <String, ArrayList <Double>>();
                                                                        hm1.put(itself.get(n), crossPoint); // <B2,x1x2y1y2>
                                                                        HashMap <String, HashMap <String, ArrayList <Double>>> hm2 = new HashMap <String, HashMap <String, ArrayList <Double>>>();
                                                                        hm2.put(map.get(itself.get(n)).getf(), hm1); // <B,<>>
                                                                        HashMap <String, HashMap <String, HashMap <String, ArrayList <Double>>>> hm3 = new HashMap <String, HashMap <String, HashMap <String, ArrayList <Double>>>>();
                                                                        hm3.put(itself.get(m), hm2); // <A3,<>>
                                                                        starNeighbor.put(map.get(itself.get(m)).getf(),
                                                                                        hm3); // <A,<>>
                                                                }
                                                                else if (!starNeighbor
                                                                                .get(map.get(itself.get(m)).getf())
                                                                                .containsKey(itself.get(m)))
                                                                {
                                                                        HashMap <String, ArrayList <Double>> hm1 = new HashMap <String, ArrayList <Double>>();
                                                                        hm1.put(itself.get(n), crossPoint); // <B2,x1x2y1y2>
                                                                        HashMap <String, HashMap <String, ArrayList <Double>>> hm2 = new HashMap <String, HashMap <String, ArrayList <Double>>>();
                                                                        hm2.put(map.get(itself.get(n)).getf(), hm1); // <B,<>>
                                                                        starNeighbor.get(map.get(itself.get(m)).getf())
                                                                                        .put(itself.get(m), hm2);
                                                                }
                                                                else if (!starNeighbor
                                                                                .get(map.get(itself.get(m)).getf())
                                                                                .get(itself.get(m))
                                                                                .containsKey(map.get(itself.get(n))
                                                                                                .getf())) // A.A1.?
                                                                {
                                                                        HashMap <String, ArrayList <Double>> hm1 = new HashMap <String, ArrayList <Double>>();
                                                                        hm1.put(itself.get(n), crossPoint); // <B2,x1x2y1y2>
                                                                        starNeighbor.get(map.get(itself.get(m)).getf())
                                                                                        .get(itself.get(m)).put(
                                                                                                        map.get(itself.get(
                                                                                                                        n))
                                                                                                                        .getf(),
                                                                                                        hm1);
                                                                }
                                                                else // A.A1.B.?
                                                                {
                                                                        starNeighbor.get(map.get(itself.get(m)).getf())
                                                                                        .get(itself.get(m))
                                                                                        .get(map.get(itself.get(n))
                                                                                                        .getf())
                                                                                        .put(itself.get(n), crossPoint);
                                                                }
                                                        }
                                                }
                                        }
                                } // itself
                                for (int m = 0; m < itself.size(); m++)
                                {
                                        for (int n = 0; n < neighbor.size(); n++)
                                        {
                                                if (map.get(itself.get(m)).getf()
                                                                .compareTo(map.get(neighbor.get(n)).getf()) < 0) // 字典序，比较
                                                {
                                                        crossPoint = pre_cross(map.get(itself.get(m)),
                                                                        map.get(neighbor.get(n))); //
                                                        if (!crossPoint.isEmpty()) // 如果两个实例相交
                                                        {
                                                                System.out.println(crossPoint);
                                                                if (!starNeighbor.containsKey(
                                                                                map.get(itself.get(m)).getf()))
                                                                {
                                                                        HashMap <String, ArrayList <Double>> hm1 = new HashMap <String, ArrayList <Double>>();
                                                                        hm1.put(neighbor.get(n), crossPoint); // <B2,x1x2y1y2>
                                                                        HashMap <String, HashMap <String, ArrayList <Double>>> hm2 = new HashMap <String, HashMap <String, ArrayList <Double>>>();
                                                                        hm2.put(map.get(neighbor.get(n)).getf(), hm1); // <B,<>>
                                                                        HashMap <String, HashMap <String, HashMap <String, ArrayList <Double>>>> hm3 = new HashMap <String, HashMap <String, HashMap <String, ArrayList <Double>>>>();
                                                                        hm3.put(itself.get(m), hm2); // <A3,<>>
                                                                        starNeighbor.put(map.get(itself.get(m)).getf(),
                                                                                        hm3); // <A,<>>
                                                                }
                                                                else if (!starNeighbor
                                                                                .get(map.get(itself.get(m)).getf())
                                                                                .containsKey(itself.get(m)))
                                                                {
                                                                        HashMap <String, ArrayList <Double>> hm1 = new HashMap <String, ArrayList <Double>>();
                                                                        hm1.put(neighbor.get(n), crossPoint); // <B2,x1x2y1y2>
                                                                        HashMap <String, HashMap <String, ArrayList <Double>>> hm2 = new HashMap <String, HashMap <String, ArrayList <Double>>>();
                                                                        hm2.put(map.get(neighbor.get(n)).getf(), hm1); // <B,<>>
                                                                        starNeighbor.get(map.get(itself.get(m)).getf())
                                                                                        .put(itself.get(m), hm2);
                                                                }
                                                                else if (!starNeighbor
                                                                                .get(map.get(itself.get(m)).getf())
                                                                                .get(itself.get(m))
                                                                                .containsKey(map.get(neighbor.get(n))
                                                                                                .getf())) // A.A1.?
                                                                {
                                                                        HashMap <String, ArrayList <Double>> hm1 = new HashMap <String, ArrayList <Double>>();
                                                                        hm1.put(neighbor.get(n), crossPoint); // <B2,x1x2y1y2>
                                                                        starNeighbor.get(map.get(itself.get(m)).getf())
                                                                                        .get(itself.get(m)).put(
                                                                                                        map.get(neighbor.get(
                                                                                                                        n))
                                                                                                                        .getf(),
                                                                                                        hm1);
                                                                }
                                                                else // A.A1.B.?
                                                                {
                                                                        starNeighbor.get(map.get(itself.get(m)).getf())
                                                                                        .get(itself.get(m))
                                                                                        .get(map.get(neighbor.get(n))
                                                                                                        .getf())
                                                                                        .put(neighbor.get(n),
                                                                                                        crossPoint);
                                                                }
                                                        }
                                                }
                                        }
                                } // neighbor
                        }

                }
                System.out.println(starNeighbor);
                return starNeighbor;
        }
        
        /**
         * 获取棋盘中某个单元的邻居
         * @param i     棋盘的横坐标
         * @param j     棋盘的纵坐标
         * @param grid  棋盘
         * @return
         */
        private ArrayList <String> getNeighbor(int i, int j, HashMap<String, ArrayList<String>> grid)
        {
                ArrayList <String> list = new ArrayList<String>();
                
                if (i == 0)             // 最下面的一行
                {
                        if (j == 0)     // 最左侧一列
                        {
                            list.addAll (grid.get("["+i+","+(j+1)+"]"));
                            list.addAll (grid.get("["+(i+1)+","+j+"]"));
                            list.addAll (grid.get("["+(i+1)+","+(j+1)+"]"));
                        }
                        else if(j < this.row_list -1 )  // 中间列
                        {
                            list.addAll (grid.get("["+i+","+(j-1)+"]"));
                            list.addAll (grid.get("["+i+","+(j+1)+"]"));
                            list.addAll (grid.get("["+(i+1)+","+(j-1)+"]"));
                            list.addAll (grid.get("["+(i+1)+","+j+"]"));
                            list.addAll (grid.get("["+(i+1)+","+(j+1)+"]")); 
                        }
                        else    // 最右侧的一列
                        {
                                list.addAll (grid.get("["+i+","+(j-1)+"]")); 
                                list.addAll (grid.get("["+(i+1)+","+(j-1)+"]")); 
                                list.addAll (grid.get("["+(i+1)+","+j+"]")); 
                        }
                }
                else if (i < this.row_list - 1) // 中间的
                {
                        if (j == 0)     // 最左侧一列
                    {
                        list.addAll (grid.get("["+i+","+(j+1)+"]"));
                        list.addAll (grid.get("["+(i+1)+","+j+"]"));
                        list.addAll (grid.get("["+(i+1)+","+(j+1)+"]"));
                        list.addAll (grid.get("["+(i-1)+","+j+"]"));
                        list.addAll (grid.get("["+(i-1)+","+(j+1)+"]"));
                    }
                    else if(j < this.row_list -1 )      // 中间列
                    {
                        list.addAll (grid.get("["+i+","+(j-1)+"]"));
                        list.addAll (grid.get("["+i+","+(j+1)+"]"));
                        list.addAll (grid.get("["+(i+1)+","+(j-1)+"]"));
                        list.addAll (grid.get("["+(i+1)+","+j+"]"));
                        list.addAll (grid.get("["+(i+1)+","+(j+1)+"]"));
                        list.addAll (grid.get("["+(i-1)+","+(j-1)+"]"));
                        list.addAll (grid.get("["+(i-1)+","+j+"]"));
                        list.addAll (grid.get("["+(i-1)+","+(j+1)+"]")); 
                    }
                    else        // 最右侧的一列
                    {
                        list.addAll (grid.get("["+i+","+(j-1)+"]")); 
                        list.addAll (grid.get("["+(i+1)+","+(j-1)+"]")); 
                        list.addAll (grid.get("["+(i+1)+","+j+"]"));
                        list.addAll (grid.get("["+(i-1)+","+(j-1)+"]")); 
                        list.addAll (grid.get("["+(i-1)+","+j+"]")); 
                    }
                }
                else    // 最上面的一行
                {
                        if (j == 0)     // 最左侧一列
                    {
                            list.addAll (grid.get("["+i+","+(j+1)+"]"));
                            list.addAll (grid.get("["+(i-1)+","+j+"]"));
                            list.addAll (grid.get("["+(i-1)+","+(j+1)+"]"));
                    }
                    else if(j < this.row_list -1 )      // 中间列
                    {
                            list.addAll (grid.get("["+i+","+(j-1)+"]"));
                            list.addAll (grid.get("["+i+","+(j+1)+"]"));
                            list.addAll (grid.get("["+(i-1)+","+(j-1)+"]"));
                            list.addAll (grid.get("["+(i-1)+","+j+"]"));
                            list.addAll (grid.get("["+(i-1)+","+(j+1)+"]")); 
                    }
                    else        // 最右侧的一列
                    {
                        list.addAll (grid.get("["+i+","+(j-1)+"]")); 
                        list.addAll (grid.get("["+(i-1)+","+(j-1)+"]")); 
                        list.addAll (grid.get("["+(i-1)+","+j+"]")); 
                    }
                }
                //System.out.println(list);
                return list;
        }
        /**
         * 实例预处理
         * @param inst1
         * @param inst2
         * @return
         */
        private ArrayList <Double> pre_cross (Instance inst1, Instance inst2)
        {
                ArrayList <Double> list;
                double X1 = inst1.getX() - inst1.getR();                // 实例1的左横坐标1
                double Y1 = inst1.getY() - inst1.getR();
                double X2 = inst1.getX() + inst1.getR();                // 实例1的右横坐标2
                double Y2 = inst1.getY() + inst1.getR();
                
                double X3 = inst2.getX() - inst2.getR();                // 实例1的左横坐标1
                double Y3 = inst2.getY() - inst2.getR();
                double X4 = inst2.getX() + inst2.getR();                // 实例1的右横坐标2
                double Y4 = inst2.getY() + inst2.getR();
               
                list = is_cross (X1, Y1, X2, Y2, X3, Y3, X4, Y4);
                
                return list;
        }
        
        
        /**
         *      判断两个实例是否相交
         * 
         * @param X1
         * @param Y1
         * @param X2
         * @param Y2
         * @param X3
         * @param Y3
         * @param X4
         * @param Y4
         * @return
         */
        private ArrayList<Double> is_cross (double X1, double Y1, double X2, double Y2, double X3, double Y3, double X4, double Y4)
        {
                ArrayList <Double> list = new ArrayList <Double> ();
                double [] tmp_point;
                boolean x_is_crossed = false;
                boolean y_is_crossed = false; 
                if ((X1 >= X3 && X1 < X4) ||  (X2>X3 && X2<=X4) || (X1>X3 && X2<X4) || (X2>X4 && X1<X3))    // 
                {
                        x_is_crossed = true;
                        if ((Y1>=Y3 && Y1<Y4) || (Y2>Y3&&Y2<=Y4) || (Y1>Y4 && Y2<Y3) || (Y2>Y4&&Y1<Y3))
                       {
                             y_is_crossed = true;
                        }
                }
                
                if ( x_is_crossed == true && y_is_crossed == true)
                {
                        tmp_point = get_cross_point (X1, X2, X3, X4);
                        list.add (tmp_point[0]);                // x坐标
                        list.add (tmp_point[1]);                // x 坐标
                        tmp_point = get_cross_point(Y1, Y2, Y3, Y4);
                        list.add (tmp_point[0]);                // y坐标
                        list.add (tmp_point[1]);                // y 坐标
                }
                return list;

        }
        
        /**
         *      获取实例相交的坐标点
         * @param pm1
         * @param pm2
         * @param pm3
         * @param pm4
         * @return
         */
        private double [] get_cross_point(double pm1, double pm2, double pm3, double pm4)
        {
                double [] tmp = {pm1, pm2, pm3, pm4};
                double [] cross_point = new double [2];
                Arrays.sort (tmp);
                cross_point [0] = tmp [1];
                cross_point [1] = tmp [2];
                return cross_point;
        }

        
        
        
        
        
        
        
        
        
        
        
        
        
        


        
}
