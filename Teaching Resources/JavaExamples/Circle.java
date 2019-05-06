
abstract class Shape {
	protected int x,y;
	protected double radius;
	
	public int getX() {return x;}
	
	public int getY() {return y;}
	
	public void setX(int x) {this.x=x;}
	
	public void setY(int y) {this.y=y;}

	public abstract void setRadius(double radius);
}
	
interface Measureable {
	double getArea();
}

class MeasureUtil {
	public static double maxArea(Measureable m1, Measureable m2) {
		return Math.max(m1.getArea(), m2.getArea());
	}
}

public class Circle extends Shape implements Measureable {
	private double radius;
	
	public Circle(int x, int y, double radius) {
		setX(x);
		setY(y);
		setRadius(radius);
	}
	
	public double getRadius() {
		return radius;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public double getArea() {
		return Math.PI*radius*radius;
	}
	
	public static void main(String[] args) {
		Circle c1 = new Circle(10,10,5);
		Circle c2 = new Circle(10,10,15);
		MeasureUtil m = new MeasureUtil();
		System.out.println("The max area of the two circles is: "+ 
						   m.maxArea(c1,c2));
	}
}


	
	
