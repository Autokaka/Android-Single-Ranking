// 
import java.awt.*;
import java.awt.event.*;
class app16_1 extends Frame implements ActionListener
{
   static app16_1 frm=new app16_1(); 
   static Button btn=new Button("Click Me");
   
   public static void main(String args[])
   {  
      btn.addActionListener(frm);     // 
      frm.setLayout(new FlowLayout());
      frm.setTitle("Action Event");
      frm.setSize(200,150);
      frm.add(btn);
      frm.setVisible(true);
   }
   
   public void actionPerformed(ActionEvent e)  // 
   {
      frm.setBackground(Color.yellow);
   }   
}
