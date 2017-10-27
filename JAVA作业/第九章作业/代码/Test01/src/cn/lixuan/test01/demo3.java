package cn.lixuan.test01;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**字符流实现：
 * 从键盘输入姓名、学号、成绩，
 * 并保存到文本文件中，重复进行，直到输入空字符串为止
 * @author lx
 *
 */
public class demo3 {
	
	public static void main(String[] args) throws Exception {
		FileOutputStream fs1 = new FileOutputStream(new File("src/cn/lixuan/test01/1.txt"));
		PrintWriter pw =new PrintWriter(fs1,true) ;
		BufferedReader reader =new BufferedReader(new InputStreamReader(System.in)) ; 
		String s =null ;
		System.out.println("请输入姓名-学号-成绩:");
		while((s=reader.readLine())!=null){	
			if(s.equals(""))
			{
				System.out.println("输入结束!");
				break ;
			}
			String[]arr =s.split(" ") ;
			System.out.println("姓名:"+arr[0]+" 学号:"+arr[1]+" 成绩:"+arr[2]);
			pw.println("姓名:"+arr[0]+" 学号:"+arr[1]+" 成绩:"+arr[2]);
			pw.flush();
			System.out.println("写入文件成功!");
			System.out.println("请输入姓名-学号-成绩:");
		} 
		reader.close();
		pw.close();
		fs1.close();
		
	}

}
