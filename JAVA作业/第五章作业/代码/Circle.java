package cn.leexuan.demo1;

public class Circle {

	double height;
	double r;

	Circle(double height, double r) {
		this.height = height;
		this.r = r;
	}

	public double tiji() {
		return height * Math.PI * r * r;
	}

	public static void main(String[] args) {
		Circle cycle = new Circle(10, 2);
		System.out.println(cycle.tiji());
	}

}
