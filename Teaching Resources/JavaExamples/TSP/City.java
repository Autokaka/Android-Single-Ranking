
import java.awt.Color;
import java.awt.Graphics;

public class City
{

    public City()
    {
        x = 0.05000000000000000 + 0.9000000000000000 * Math.random();
        y = 0.05000000000000000 + 0.9000000000000000 * Math.random();
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public static double getDistance(City city, City city1)
    {
        return Math.sqrt(Math.pow(city.x - city1.x, 2D) + Math.pow(city.y - city1.y, 2D));
    }

    public void paint(Graphics g, double d, double d1)
    {
       // byte byte0 = 16;
        g.setColor(Color.blue);
        g.fillOval((int)(x * d) - 3, (int)(y * d1) - 3, 6, 6);
    }

    public void paintRoute(Graphics g, City city, double d, double d1)
    {
        g.setColor(Color.red);
        g.drawLine((int)(x * d), (int)(y * d1), (int)(city.x * d), (int)(city.y * d1));
    }

    private double x;
    private double y;
}
