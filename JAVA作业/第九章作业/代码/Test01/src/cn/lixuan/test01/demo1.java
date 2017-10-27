package cn.lixuan.test01;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author lx 合并1.txt和2.txt的内容放到3.txt中 ;
 */
public class demo1 {
	public static void main(String[] args) throws Exception {
		FileInputStream fs1 = new FileInputStream(new File("src/cn/lixuan/test01/1.txt"));
		FileInputStream fs2 = new FileInputStream(new File("src/cn/lixuan/test01/2.txt"));
		FileOutputStream fs3 = new FileOutputStream(new File("src/cn/lixuan/test01/3.txt"));
		byte[] arr1 = new byte[fs1.available()];
		byte[] arr2 = new byte[fs2.available()];
		fs1.read(arr1);
		fs2.read(arr2);
		String s1 = new String(arr1);
		System.out.println("文件一的内容:");
		System.out.println(s1);
		String s2 = new String(arr2);
		System.out.println("文件二的内容:");
		System.out.println(s2);
		String s3 = s1 + s2;
		byte[] arr3 = s3.getBytes();
		fs3.write(arr3);
		System.out.println("写入文件成功");

		fs1.close();
		fs2.close();
		fs3.close();
	}

}
