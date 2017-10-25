package cn.lixuan.test03;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 
 * @author lx
 * @version 2017.10.20 编译原理实验三： 要求实现 求转换的DFA状态图; 构造相应的分析表;根据分析表分析某个表达式的总控程序;
 */

class Exp {
	// 每一个表达式 ; 比如 S->.BB,#
	Character left; // 左边的非终结符 ;
	String right1; // 右边产生式 .号左边的部分 ;
	String right2; // 右边产生式 .号右边的部分 ;
	HashSet<Character> set; // 展望字符 ;

	public Exp() {
		set = new HashSet<>();
	}
}

// 每一个项目类 Item;
class Item {
	int id; // 项目编号/下标 ;
	ArrayList<Exp> expList; // 表达式列表 ;
	ArrayList<Character> fol; // 后面的字符 ;
	ArrayList<Exp> zhijie ; //直接产生式列表 ;
	public Item() {
		expList = new ArrayList<>();
		fol = new ArrayList<>();
		
	}
	
}

// 实现思路：
public class Demo {
	public static boolean isequal(Item a ,Item b  ){
		//判断两个项目是否是相等的 ;
		
		return false ;
	}
	public static ArrayList<Item> ItemList = new ArrayList<>();
	public static HashMap<Integer, HashMap<Character, String>> Action = new HashMap<>();
	public static HashMap<Integer, HashMap<Character, Integer>> Goto = new HashMap<>();
	public static HashSet<Character>zhongJie =new HashSet<>() ; 
	public static HashSet<Character>feiZhongJie =new HashSet<>() ;
	public static HashMap<Character,ArrayList<String>>table =new HashMap<>() ;
	public static FileReader fr = null;
	//public static ArrayList<>
	public static void main(String[] args) {
		try {
			fr = new FileReader(new File("src/cn/lixuan/test03/1.txt"));
			BufferedReader bf = new BufferedReader(fr);
			String s;
			while ((s = bf.readLine()) != null) {
				String[]arr = s.split("->") ;
				Character st =arr[0].charAt(0) ;
				if(table.containsKey(st)){
					ArrayList<String>ar = table.get(st) ;
					ar.add(arr[1]) ;
					table.put(st, ar) ;
					
				}else {
					ArrayList<String>ar =new ArrayList<>() ;
					ar.add(arr[1]) ;
					table.put(st, ar) ;
				}
			}
			
			
			//先将I0求出 然后 solve(I0) 一个个的解决 ;
			

		} catch (Exception a) {
			a.printStackTrace();
			try {
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
