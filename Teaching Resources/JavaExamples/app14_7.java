// app14_7, 
class CBank
{
   private static int sum=0;
   //public  static synchronized void add(int n){
   public  static void add(int n){
      int tmp=sum;
      tmp=tmp+n;   // 
      try{
         Thread.sleep((int)(1000*Math.random()));  // 
      }
      catch(InterruptedException e){} 
      sum=tmp;
      System.out.println("sum= "+sum); 
  }
}

class CCustomer extends Thread // CCustomer
{
   public void run(){    // run() method
      for(int i=1;i<=3;i++)
         CBank.add(100);  // 
   }
}

public class app14_7
{
   public static void main(String args[])
   {  
      CCustomer c1=new CCustomer();
      CCustomer c2=new CCustomer();
      c1.start();
      c2.start();
   }
}


