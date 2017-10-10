package cn.leexuan.demo1;

/*
 * 写一个类A，该类创建的对象可以调用方法f输出英文字母表
 * 然后再编写一个该类的子类B必须继承A类的方法f（不允许重写），
 * 子类创建的对象不仅可以调用方法f输出英文字母表，
 * 而且可以调用子类新增的方法g输出希腊字母表。
 * */
class A {
	public void f(){
		System.out.print("英文字母表:");
		for(char c ='a' ;c <='z' ;c++)
			System.out.print(c+" ");
		System.out.println();
	}
}

class B extends A {
	void g() {
		char[] lowerGreekLetters = "αβγδεζηθικλμνξοπρστυφχψω".toCharArray();
		System.out.print("希腊字母表:");
		for (char i :lowerGreekLetters)
			System.out.print(i+" ");
		System.out.println();
	}
}
public class Main{
	public static void main(String[] args) {
		A a =new A() ;
		a.f();
		B b =new B() ;
		b.f();
		b.g();
	}
}