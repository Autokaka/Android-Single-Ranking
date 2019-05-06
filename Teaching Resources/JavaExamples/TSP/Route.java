
import java.awt.Color;
import java.awt.Graphics;
import java.io.PrintStream;

public class Route
{

    public Route(int i)
    {
        number = i;
        distance = 0.0D;
        cities = new City[number];
        bestCities = new City[number];
        cities[0] = new City();
        for(int j = 1; j < number; j++)
        {
            cities[j] = new City();
            distance += City.getDistance(cities[j - 1], cities[j]);
        }

        distance += City.getDistance(cities[0], cities[number - 1]);
        copyBestRoute();
    }

    private void copyBestRoute()
    {
        for(int i = 0; i < number; i++)
            bestCities[i] = cities[i];

        bestDistance = distance;
    }

    private void calculateDistance()
    {
        distance = 0.0D;
        for(int i = 1; i < number; i++)
            distance += City.getDistance(cities[i - 1], cities[i]);

        distance += City.getDistance(cities[0], cities[number - 1]);
    }

    public void paintBestRoute(Graphics g, double d, double d1)
    {
        g.setColor(Color.red);
        City city = bestCities[number - 1];
        for(int i = 0; i < number; i++)
        {
            City city2 = bestCities[i];
            city.paintRoute(g, city2, d, d1);
            city = city2;
        }

        g.setColor(Color.white);
        for(int j = 0; j < number; j++)
        {
            City city1 = bestCities[j];
            city1.paint(g, d, d1);
        }

    }

    public double getBestDistance()
    {
        return bestDistance;
    }

    public void randomize()
    {
        City acity[] = new City[number];
        boolean aflag[] = new boolean[number];
        for(int i = 0; i < number; i++)
        {
            int j;
            for(j = (int)(Math.random() * (double)number); aflag[j]; j = (int)(Math.random() * (double)number));
            aflag[j] = true;
            acity[i] = cities[j];
        }

        cities = acity;
        calculateDistance();
        copyBestRoute();
    }

    public boolean move(double d)
    {
        int i = (int)(Math.random() * (double)number);
        int j = (i + 1) % number;
        int k = (j + 1 + (int)(Math.random() * (double)(number - 3))) % number;
        int l = (k + 1) % number;
        int i1 = ((k - j) + 1 + number) % number;
        if((i1 < 2) | (i1 > number - 2))
            System.out.println("Problem. Size = " + i1);
        double d1 = (City.getDistance(cities[i], cities[k]) + City.getDistance(cities[j], cities[l])) - City.getDistance(cities[i], cities[j]) - City.getDistance(cities[k], cities[l]);
        if(d1 < 0.0D || Math.random() < Math.exp(-d1 / d))
        {
            City acity[] = new City[i1];
            for(int j1 = 0; j1 < i1; j1++)
                acity[j1] = cities[(j + j1) % number];

            for(int k1 = 0; k1 < i1; k1++)
                cities[((k - k1) + number) % number] = acity[k1];

            calculateDistance();
            if(distance < bestDistance)
            {
                copyBestRoute();
                return true;
            } else
            {
                return false;
            }
        } else
        {
            return false;
        }
    }

    private City cities[];
    private City bestCities[];
    private int number;
    private double distance;
    private double bestDistance;
}
