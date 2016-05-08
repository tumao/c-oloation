package data;

public class Instance
{
        double x;
        double y;
        double r;                      
        String name;            // 实例名称
        String f;               // 特征   
        
        public Instance (String name, double  x, double y, double r, String f)
        {
                this.name = name;
                this.x = x;
                this .y = y;
                this.r = r;
                this.f = f;
        }
        
        /**
         * 
         * @return
         */
        public double getR ()
        {
                return this.r;
        }
        
        /**
         * 
         * @return
         */
        public double getX ()
        {
                return this.x;
        }
        
        /**
         * 
         * @return
         */
        public double getY ()
        {
                return this.y;
        }
        
        /**
         * 
         * @param x
         */
        public void setX (double x)
        {
                this.x = x;
        }
        
        /**
         * 
         * @param y
         */
        public void setY (double y)
        {
                this.y = y;
        }
        
        /**
         * 
         * @param r
         */
        public void setR (double r)
        {
                this.r = r;
        }
        
        public String getName ()
        {
                return this.name;
        }
        
        public void setf (String f)
        {
                this.f = f;
        }
        
        public String getf ()
        {
                return this.f;
        }
}
