package cn.lixuan.test01;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**字符流实现：
 * 从文件中读取各学生的成绩，
 * 并计算所有学生成绩的平均值、最大值和最小值
 * @author lx
 */
public class demo4 {

	public static void main(String[] args) throws Exception {
		//从刚刚写入的成绩文件中读入成绩,进行处理 ;
		BufferedReader bf  =new BufferedReader(new FileReader(new File("src/cn/lixuan/test01/1.txt"))) ;
		String s =null ;
		int max =0,min=100,sum=0,count=0, score ;
		while((s=bf.readLine())!=null){
			String[]arr = s.split(":");
			String s1 =arr[arr.length-1] ;
			System.out.println("第"+ ++count+"个成绩是"+s1);
			score = Integer.parseInt(s1) ;
			sum+=score ;
			max =Math.max(max, score) ;
			min =Math.min(min, score) ;
		}
		System.out.println("读取完成!");
		System.out.println("最高成绩:"+max+" 最低成绩:"+min+" 平均成绩:"+sum*1.0/count);
		
		
	}

}
