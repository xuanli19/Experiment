package cn.lixuan.test03;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

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
	Character zhanwang ;  // 展望字符 ;

	public Exp() {
		
	}

	public Exp(Character left, String right1, String right2, Character zhanwang) {
		
		this.left = left;
		this.right1 = right1;
		this.right2 = right2;
		this.zhanwang = zhanwang;
	}
}

// 每一个项目类 Item;
class Item {
	int id; // 项目编号/下标 ;
	ArrayList<Exp> expList; // 表达式列表 ;
	ArrayList<Character> fol; // 后面的字符 ;
	ArrayList<Exp> zhijie; // 直接产生式列表 ;

	public Item() {
		expList = new ArrayList<>();
		fol = new ArrayList<>();
	}
	
	@Override
	public boolean equals(Object obj) {
		//通过直接产生式来判断两个item是否重复 ;
		
		return super.equals(obj);
	}
}

// 实现思路：
public class Demo {

	public static ArrayList<Item> ItemList = new ArrayList<>();
	public static HashMap<Integer, HashMap<Character, String>> Action = new HashMap<>();
	public static HashMap<Integer, HashMap<Character, Integer>> Goto = new HashMap<>();
	public static HashSet<Character> zhongJie = new HashSet<>();
	public static HashSet<Character> feiZhongJie = new HashSet<>();
	public static HashMap<Character, ArrayList<String>> table = new HashMap<>();
	public static FileReader fr = null;
	public static Character start;
	public static LinkedList<Item> list = null;

	public static boolean isequal(Item a, Item b) {
		// 判断两个项目是否是相等的 ;

		return false;
	}
	public static ArrayList<Character> getFirst(String s ) {
		
		
		return null ;
	}
	public static void solve(Item item) {
		// 根据直接产生式 推导出整个项目,然后将项目放到ItemList中,推导出来的状态放到linkedlist中
		// 并标记好产生式关系,以及判重 ;
		HashSet<Character> set1 = new HashSet<>();
		LinkedList<Exp> li = new LinkedList<>();
		for (Exp e : item.zhijie) {
			Character left = e.left;
			String right1 = e.right1;
			String right2 = e.right2;
			Character zhanwang = e.zhanwang ; 
			if (right2.length() != 0) {
				if (right2.charAt(0) >= 'A' && right2.charAt(0) <= 'Z' && set1.contains(right2.charAt(0)) == false) {
					li.add(new Exp( left,right1,right2,zhanwang));
					set1.add(right2.charAt(0));
					item.fol.add(right2.charAt(0));
				} else if (set1.contains(right2.charAt(0)) == false) {
					item.fol.add(right2.charAt(0));
					set1.add(right2.charAt(0));
				}

			} else {
				// 移到最后一个位置了 ;

			}
		}
		//拿产生式来推导 ;
		while (!li.isEmpty()) {
			Exp top = li.peek();// li中存有S
			li.pop();
			Character left = top.left;
			String right1 = top.right1;
			String right2 = top.right2;
			Character zhanwang = top.zhanwang ; 
			ArrayList<String> list2 = table.get(right2.charAt(0));
			for (String s : list2) {
				if (s.charAt(0) >= 'A' && s.charAt(0) <= 'Z' && set1.contains(s.charAt(0)) == false) {
					if(s.length()>=2 && s.charAt(1)>='A'&& s.charAt(1)<='Z')  {
						ArrayList<Character>list3 = getFirst(""+s.substring(1, s.length())+zhanwang) ;
						for(Character c : list3)
						{
							item.expList.add(new Exp(right2.charAt(0),"",s,c));
							li.add(new Exp(right2.charAt(0),"",s,c)) ;
						}
						set1.add(s.charAt(0));
					}
					else {
						ArrayList<Character>list3 = getFirst(""+s.substring(1, s.length())+zhanwang) ;
						for(Character c : list3)
						{
							item.expList.add(new Exp(right2.charAt(0),"",s,c));
						}
						set1.add(s.charAt(0));
					}
				}else if( set1.contains(s.charAt(0)) == false) {
					
					set1.add(s.charAt(0));
				}
				
			}
			
			ItemList.add(item) ;
		}

		return;
	}

	public static void main(String[] args) {
		try {
			fr = new FileReader(new File("src/cn/lixuan/test03/1.txt"));
			BufferedReader bf = new BufferedReader(fr);
			String s;
			while ((s = bf.readLine()) != null) {
				String[] arr = s.split("->");
				Character st = arr[0].charAt(0);
				if (start == null)
					start = st;
				if (table.containsKey(st)) {
					ArrayList<String> ar = table.get(st);
					ar.add(arr[1]);
					table.put(st, ar);

				} else {
					ArrayList<String> ar = new ArrayList<>();
					ar.add(arr[1]);
					table.put(st, ar);
				}
			}
			// 先将I0直接产生式求出,然后 solve(I0) 一个个的解决 ;
			// 然后将I0的后面的字符 ;
			Item item = new Item();
			item.zhijie.add(new Exp('S', "", "" + start, '#')); // S->.E # 起始产生式 ;
			item.fol.add(start);

			list.add(item); // 放入起始产生式 ;
			while (!list.isEmpty()) {
				// list 不为空 ;
				Item it = list.peek();
				list.pop();
				solve(it);
				//该产生式拓展后，按照它的fol进行状态转换，存为item.zhijie,放进list中 ;
				//若不重复,则在map一个从前到后的映射 ;
				
				
			}

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
