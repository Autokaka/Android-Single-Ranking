// app14_1
class Ctest 
{
   private String id;
   public Ctest(String str){   // 
      id=str;
   }
   
   public void run(){    // run() method
      for(int i=0;i<4;i++){
         for(int j=0;j<100000000;j++);  // 
         System.out.println(id+" is running..");
      } 
   }
}
 
public class app14_1
{
   public static void main(String args[])
   {  
      Ctest dog=new Ctest("doggy");
      Ctest cat=new Ctest("kitty"); 
      dog.run();
      cat.run();
   }
}
