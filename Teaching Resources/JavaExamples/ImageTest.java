import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImageTest extends JFrame {

    MyPanel m = null;

    public ImageTest() {

        m = new MyPanel();
        this.add(m);
        this.setSize(400, 300);
        this.setTitle("画笔学习");
        this.setLocation(400, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        ImageTest t = new ImageTest();

    }

}

// 定义一个mypanel,绘制图片
class MyPanel extends JPanel {

    // 重写paint方法
    // Graphics是绘图的重要类，你可以把它理解成一只画笔
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.green);

        // 画直线
        g.drawLine(10, 0, 10, 40);

        // 画圆
        g.drawOval(60, 10, 30, 30);

        // 画椭圆
        g.drawOval(20, 10, 30, 60);

        // 画矩形边框
        g.drawRect(40, 40, 40, 60);

        // 填充矩形
        g.setColor(Color.black);
        g.fillRect(100, 100, 60, 50);

        // 填充椭圆
        g.setColor(Color.yellow);
        g.fillOval(100, 200, 60, 50);

        

        // 画字符串,后面的参数是字出现的位置
        g.setColor(Color.blue);
        g.setFont(new Font("华文彩云", Font.BOLD, 80));
        g.drawString("中国梦", 120, 80);

    }
}