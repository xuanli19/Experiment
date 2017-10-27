package cn.lixuan.test01;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * 在屏幕上显示：输入姓名，然后将用户输入的姓名保存到文本文件中
 * 重复进行，直到用户输入空字符串为止。
 * @author lx
 */
public class demo2 {

	public static void main(String[] args) throws Exception {
		FileOutputStream fs1 = new FileOutputStream(new File("src/cn/lixuan/test01/1.txt"));
		PrintWriter pw =new PrintWriter(fs1,true) ;
		BufferedReader reader =new BufferedReader(new InputStreamReader(System.in)) ; 
		String s =null ; 
		while((s=reader.readLine())!=null){
			if(s.equals(""))
				break ;
			System.out.println("读取内容:"+s);
			pw.println(s);
			pw.flush();
			System.out.println("写入文件成功!");
		}
		reader.close();
		pw.close();
		fs1.close();
		
	}

}
