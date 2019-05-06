
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class TSP extends JFrame
    implements ActionListener
{
    private class RouteRunner
        implements Runnable
    {

        public void stop()
        {
            stop = true;
        }

        public void run()
        {
            while(!stop) 
            {
                if(route.move(temperature))
                {
                    distance.setText(dc.format(route.getBestDistance()));
                    routePanel.repaint();
                }
                String s = dc.format(temperature);
                if(!s.equals(temp.getText()))
                    temp.setText(s);
                temperature = temperature * rate;
            }
        }

        private boolean stop;
        private double temperature;
        private double rate;

        public RouteRunner(double d, double d1)
        {
            temperature = d;
            rate = d1;
            stop = false;
        }
    }

    private class RoutePanel extends JPanel
    {

        public void paint(Graphics g)
        {
            super.paint(g);
            double d = getWidth();
            double d1 = getHeight();
            route.paintBestRoute(g, d, d1);
        }

        public RoutePanel()
        {
            setBackground(Color.white);
            setBorder(new EtchedBorder());
        }
    }


    public TSP()
    {
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent windowevent)
            {
                dispose();
                System.exit(0);
            }

        });
        dc = new DecimalFormat("0.00");
        cities = new JComboBox();
        cities.addItem("20");
        cities.addItem("40");
        cities.addItem("60");
        cities.addItem("80");
        cities.addItem("100");
        cities.addActionListener(this);
        anneal = new JComboBox();
        anneal.addItem("Hill Climbing");
        anneal.addItem("Anneal Slow");
        anneal.addItem("Anneal Medium");
        anneal.addItem("Anneal Fast");
        anneal.setSelectedIndex(0);
        random = new JButton("Random");
        random.addActionListener(this);
        start = new JButton("Start");
        start.addActionListener(this);
        stop = new JButton("Stop");
        stop.setEnabled(false);
        stop.addActionListener(this);
        temp = new JTextField("");
        temp.setEditable(false);
        distance = new JTextField("");
        distance.setEditable(false);
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new GridLayout(12, 1));
        jpanel.add(new JLabel("Cities"));
        jpanel.add(cities);
        jpanel.add(new JLabel("Rate"));
        jpanel.add(anneal);
        jpanel.add(new JLabel("Temp."));
        jpanel.add(temp);
        jpanel.add(new JLabel("Distance"));
        jpanel.add(distance);
        jpanel.add(new JLabel(" "));
        jpanel.add(random);
        jpanel.add(start);
        jpanel.add(stop);
        JPanel jpanel2 = new JPanel();
        jpanel2.setLayout(new BorderLayout());
        jpanel2.add("North", jpanel);
        routePanel = new RoutePanel();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add("East", jpanel2);
        getContentPane().add("Center", routePanel);
        route = new Route(20);
        distance.setText(dc.format(route.getBestDistance()));
        routePanel.repaint();
    }

    public static void main(String args[])
    {
        System.out.println("Starting TSP...");
        TSP tsp = new TSP();
        tsp.setSize(400, 400);
        tsp.setTitle("TSP");
        tsp.setVisible(true);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(actionevent.getSource() == cities)
        {
            route = new Route(cities.getSelectedIndex() * 20 + 20);
            distance.setText(dc.format(route.getBestDistance()));
            routePanel.repaint();
        }
        if(actionevent.getSource() == start)
        {
            double d = 1.0D;
            double d1 = 1.0D;
            switch(anneal.getSelectedIndex())
            {
            case 0: // '\0'
                d = 9.9999999999999995E-007D;
                d1 = 1.0D;
                break;

            case 1: // '\001'
                d = 0.10000000000000001D;
                d1 = 0.99999899999999997D;
                break;

            case 2: // '\002'
                d = 0.10000000000000001D;
                d1 = 0.99999000000000005D;
                break;

            case 3: // '\003'
                d = 0.10000000000000001D;
                d1 = 0.99990000000000001D;
                break;
            }
            routeRunner = new RouteRunner(d, d1);
            thread = new Thread(routeRunner);
            thread.setPriority(thread.getPriority() - 1);
            thread.start();
            random.setEnabled(false);
            start.setEnabled(false);
            stop.setEnabled(true);
            cities.setEnabled(false);
            anneal.setEnabled(false);
        }
        if(actionevent.getSource() == stop)
        {
            thread.stop();
            random.setEnabled(true);
            start.setEnabled(true);
            stop.setEnabled(false);
            cities.setEnabled(true);
            anneal.setEnabled(true);
        }
        if(actionevent.getSource() == random)
        {
            route.randomize();
            distance.setText(dc.format(route.getBestDistance()));
            routePanel.repaint();
        }
    }

    private JComboBox cities;
    private JComboBox anneal;
    private JButton random;
    private JButton start;
    private JButton stop;
    private JTextField temp;
    private JTextField distance;
    private RoutePanel routePanel;
    private RouteRunner routeRunner;
    private Thread thread;
    private DecimalFormat dc;
    private Route route;





}
