package cn.leexuan.demo1;

public class Fan {
	private int speed=1 ; 
	private boolean open=false ; 
	private double radius =5;
	private String color  ="blue";
	public int getSpeed() {
		return speed;
	}
	public boolean isOpen() {
		return open;
	}
	public double getRadius() {
		return radius;
	}
	public String getColor() {
		return color;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public void setColor(String color) {
		this.color = color;
	}
	Fan(){
		
	}
	@Override
	public String toString() {
		if (this.open)
			return ""+speed+" "+color+" "+radius;
		else 
			return ""+color+" "+radius+" fan is off" ;
	}
	public static void main(String[] args) {
		Fan fan1 =new Fan() ;
		fan1.setSpeed(3);
		fan1.setRadius(10);
		fan1.setColor("yellow");
		fan1.setOpen(true);
		System.out.println("第一个风扇:");
		System.out.println(fan1);
		
		Fan fan2 =new Fan() ;
		fan1.setSpeed(2);
		System.out.println("第二个风扇:");
		System.out.println(fan2);
	}
	
	
}
