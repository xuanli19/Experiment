package cn.lixuan.test01;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

//class Word {
//	String val; // 单词的值 ;
//	int type; // 单词种别 ;
//	int x; // 单词的位置横坐标
//	int y; // 单词的位置纵坐标
//
//	Word(String val, int type, int x, int y) {
//		this.val = val;
//		this.type = type;
//		this.x = x;
//		this.y = y;
//	}
//	
//	@Override
//	public String toString() {
//		return "    "+val + "   \t   \t(" + type+","+val+")    \t\t" + Main.types[type] +
//				"           \t\t("+ x + "," + y+")";
//	}
//}

/**
 * 利用java实现c语言的词法分析
 * 
 * @author lx
 * @version 2017.10.9
 */
public class Main {

	public static String[] types = { "错误", "关键字", "分界符", "运算符", "关系运算符", "常数", "标识符",
							        // 0		 1 		2 		3 			4 			5 		6
	};
	public static Scanner sc = null;
	public static String fileName = null;
	public static File file = null;
	public static FileInputStream fsin = null;
	public static int lineNum = 1; // 行号 ;
	public static int rowNum = 1; // 列号 ;
	public static ArrayList<Word> list = new ArrayList<>();
	public static HashMap<String, Integer> changShu = new HashMap<String, Integer>();
	public static HashMap<String, Integer> keyWords = new HashMap<String, Integer>() {
		{
			put("if", 1);
			put("for", 2);
			put("while", 3);
			put("printf", 4);
		}
	}; // 关键字列表
	public static HashMap<String, Integer> biaoZhi = new HashMap<String, Integer>();
	// 标识符列表 (包括 变量名,数组名,函数名)
	public static HashMap<String, Integer> fenJie = new HashMap<String, Integer>() {
		{
			put(";", 1);
			put(",", 2);
			put("(", 3);
			put(")", 4);
			put("[", 5);
			put("]", 6);
		}
	}; // 分界符列表
	public static HashMap<String, Integer> yunSuan = new HashMap<String, Integer>() {
		{
			put("+", 1);
			put("-", 2);
			put("*", 3);
			put("/", 4);
			put("+=", 5);
			put("-=", 6);
			put("/=", 7);
			put("*=", 8);
			put("++", 9);
			put("--", 10);
		}
	};// 运算符列表
	public static HashMap<String, Integer> guanXiYunSuan = new HashMap<String, Integer>() {
		{
			put("<", 1);
			put(">", 2);
			put("<=", 3);
			put(">=", 4);
			put("!", 5);
			put("!=", 6);
			put("=", 7);
		}
	}; // 关系运算符

	public static int getWords(int index, String str) {
		// 读取一个字符串

		while ((str.charAt(index) == ' ' || str.charAt(index) == '\t')) {
			index++;
			if (index >= str.length())
				return str.length();
			return index ;
		}
		// 因为JAVA中的换行是 "\r\n" 所以要判断两个字符 ;
		if (str.charAt(index) == '\r' && str.charAt(index + 1) == '\n') {
			// 换行 ;
			rowNum = 1;
			lineNum++;
			index += 2;
			return index;
		}
		StringBuilder sb = new StringBuilder();
		if ((str.charAt(index) <= 'z' && str.charAt(index) >= 'a')) {
			// 首字符是字母 ;(关键字 标识符)
			// 拼出字符串 ;
			while ((str.charAt(index) <= 'z' && str.charAt(index) >= 'a')
					|| (str.charAt(index) <= '9' && str.charAt(index) >= '0')) {
				sb.append(str.charAt(index));
				index++;
				if(index==str.length())break ;
			}

			String str1 = sb.toString();
			if (keyWords.containsKey(str1)) {
				// 是关键字 ;
				list.add(new Word(str1, 1, lineNum, rowNum));
			} else {
				// 是标识符
				if (biaoZhi.containsKey(str1)) {
					list.add(new Word(str1, 6, lineNum, rowNum));
				} else {
					biaoZhi.put(str1, biaoZhi.size() + 1);
					list.add(new Word(str1, 6, lineNum, rowNum));
				}
			}
			rowNum++;
			return index;
		} else if (str.charAt(index) <= '9' && str.charAt(index) >= '0') {
			// 首字符是数字 ;(常数表)
			while ((str.charAt(index) <= 'z' && str.charAt(index) >= 'a')
					|| (str.charAt(index) <= '9' && str.charAt(index) >= '0')) {
				sb.append(str.charAt(index));
				index++;
			}
			String str1 = sb.toString();
			boolean error = false;
			for (int k = 0; k < str1.length(); k++) {
				if (str1.charAt(k) <= 'z' && str1.charAt(k) >= 'a') {
					error = true;
					break;
				}
			}
			if (error) {
				// 数字串中含字母
				list.add(new Word(str1, 0, lineNum, rowNum));
			} else {
				// 纯数字串 ;
				if (changShu.containsKey(str1)) {
					// 已在常数列表中 ;
					list.add(new Word(str1, 5, lineNum, rowNum));
				} else {
					// 不在常数列表中，将它加入
					changShu.put(str1, changShu.size() + 1);
					list.add(new Word(str1, 5, lineNum, rowNum));
				}
			}
			rowNum++;
			return index;
		} else {
			// 运算符 + - * / += ++ 等
			if (yunSuan.containsKey(str.charAt(index)+"" )) {
				
				String s = ""+str.charAt(index) ;
				index++ ;
				s+=str.charAt(index) ;
				if(yunSuan.containsKey(s)){
					index++ ;
					list.add(new Word(s,3,lineNum,rowNum)) ;
				}else {
					list.add(new Word(s.substring(0,s.length()-1),3,lineNum,rowNum )) ;
				}
				rowNum++ ;
				return index;
			} else // 关系运算符 4
			if (guanXiYunSuan.containsKey(str.charAt(index)+"")) {
				String s = ""+str.charAt(index) ;
				index++ ;
				s+=str.charAt(index) ;
				if(guanXiYunSuan.containsKey(s)){
					index++ ;
					list.add(new Word(s,4,lineNum,rowNum)) ;
				}else {
					list.add(new Word(s.substring(0,s.length()-1),4,lineNum,rowNum )) ;
				}
				rowNum++ ;
				return index;
			} else// 分界符 2
			if (fenJie.containsKey(str.charAt(index)+"")) {
				
				list.add(new Word(""+str.charAt(index),2,lineNum,rowNum)) ;
				index++ ;
				rowNum++ ;
				return index ;
			}
			else {
				// error   0
				//System.out.println("str.charAt:"+str.charAt(index));
				index++ ;
				list.add(new Word(""+str.charAt(index),0,lineNum,rowNum)) ;
				rowNum++ ;
				return index ;
			}

		}
	}

	public static void main(String[] args) {
		System.out.println("请输入c语言源程序文件名:");

		try {
			sc = new Scanner(System.in);
			fileName = "src/cn/lixuan/test01/1.txt"; // 使用相对路径 ;
			file = new File(fileName);
			fsin = new FileInputStream(file);
			byte[] arr = new byte[fsin.available()];
			fsin.read(arr);
			String des = new String(arr);
			int index = 0;
			while (index < des.length()) {
				index = getWords(index, des);
			}
			System.out.println("       单词   \t\t二元序列     \t\t类型          \t\t位置");
			for(Word word  : list){
				System.out.println(word);
			}
			System.out.println("读取完成");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {

				fsin.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
