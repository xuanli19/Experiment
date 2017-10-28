package cn.lixuan.test03;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

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
	Character zhanwang; // 展望字符 ;

	public Exp() {

	}

	public Exp(Character left, String right1, String right2, Character zhanwang) {

		this.left = left;
		this.right1 = right1;
		this.right2 = right2;
		this.zhanwang = zhanwang;
	}

	@Override
	public String toString() {
		return left + "->" + right1 + "." + right2 + "," + zhanwang;
	}

	@Override
	public boolean equals(Object obj) {
		Exp obj1 = (Exp) obj;
		if (obj1.left.equals(this.left) && obj1.right1.equals(this.right1) && obj1.right2.equals(this.right2)
				&& obj1.zhanwang.equals(this.zhanwang)) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() { // 让它们hash地址相同，便于equals函数判重 ;
		return 1;
	}
}

// 每一个项目类 Item;
class Item {
	int id; // 项目编号/下标 ;
	ArrayList<Exp> expList = new ArrayList<>(); // 表达式列表 ;
	ArrayList<Character> fol = new ArrayList<>(); // 后面的字符 ;
	ArrayList<Exp> zhijie = new ArrayList<>(); // 直接产生式列表 ;

	public Item() {

	}

	@Override
	public boolean equals(Object obj) {
		// 通过直接产生式来判断两个item是否重复 ;
		Item obj1 = (Item) obj;
		if (obj1.zhijie.size() != this.zhijie.size())
			return false;
		for (int k = 0; k < obj1.zhijie.size(); k++) {
			boolean flag = false;
			for (int s = 0; s < this.zhijie.size(); s++) {
				if (this.zhijie.get(s).equals(obj1.zhijie.get(k))) {
					flag = true;
				}
			}
			if (!flag)
				return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return 2;
	}
}

// 实现思路：
public class Demo {

	public static ArrayList<Item> ItemList = new ArrayList<>();
	public static HashMap<Integer, HashMap<Character, String>> Action = new HashMap<>(); // 对终结符
																							// ;
	public static HashMap<Integer, HashMap<Character, Integer>> Goto = new HashMap<>(); // 对非终结符
																						// ;
	public static HashSet<Character> zhongJie = new HashSet<>();
	public static HashSet<Character> feiZhongJie = new HashSet<>();
	public static HashMap<Character, ArrayList<String>> table = new HashMap<>();
	public static FileReader fr = null;
	public static Character start;
	public static LinkedList<Item> list = new LinkedList<>();
	public static HashMap<Character, Set<Character>> First = new HashMap<>();
	public static boolean vis[] = new boolean['Z' + 1];
	public static HashMap<Item, Integer> ItemToInt = new HashMap<>();
	public static HashMap<String, HashMap<Character, Integer>> guiYue = new HashMap<>();
	public static HashSet<Item> set2 = new HashSet<>(ItemList);

	public static boolean toEmpty(Character c) { // 判断是否能变成空串 ;
		if (c == 'ε')
			return true;
		else if (c >= 'A' && c <= 'Z') {
			ArrayList<String> list = table.get(c);
			boolean flag = true;
			for (String s : list) {
				// 每一个字符c的产生式 ;
				int k = 0;
				for (; k < s.length(); k++) {
					if (s.length() == 1 && s.charAt(0) == 'ε')
						return true;
					if (!(s.charAt(k) >= 'A' && s.charAt(k) <= 'Z'))
						break;
					if (s.charAt(k) >= 'A' && s.charAt(k) <= 'Z') {
						// System.out.println(c + " " + s.charAt(k));
						flag = flag && toEmpty(s.charAt(k));
					}
					if (!flag)
						break;
				}
				if (flag && k == s.length())
					return true;
			}
		}
		return false;
	}

	public static void getFirst(Character c) {
		ArrayList<String> list = table.get(c);
		Set<Character> ch = new HashSet<>();
		for (String s : list) {
			if (s.charAt(0) == 'ε') {
				ch.add('ε');
			} else if (s.charAt(0) >= 'A' && s.charAt(0) <= 'Z') {
				// 将该字符的first集合添加到ch中 ;
				for (Character x : First.get(s.charAt(0))) {
					if (x != 'ε')
						ch.add(x);
				}
				// 判断该字符是否能推导出空串 ;
				int k = 0;
				for (; k < s.length(); k++) {
					if (toEmpty(s.charAt(k))) {
						if (k == s.length() - 1) {
							ch.add('ε');
							break;
						} else if (s.charAt(k + 1) >= 'A' && s.charAt(k + 1) <= 'Z') {
							for (Character q : First.get(s.charAt(k + 1))) {
								if (q != 'ε')
									ch.add(q);
							}
						} else
							break;
					} else {
						break;
					}
				}
			} else {
				// 直接将该字符添加到first集合中 ;
				ch.add(s.charAt(0));
			}
		}
		First.put(c, ch);
	}

	public static ArrayList<Character> getFirst(String s) {
		ArrayList<Character> arr = new ArrayList<>();
		if (!(s.charAt(0) >= 'A' && s.charAt(0) <= 'Z')) {
			arr.add(s.charAt(0));
			return arr;
		} else {
			Set<Character> set = First.get(s.charAt(0));
			for (Character d : set) {
				arr.add(d);
			}
			return arr;
		}
		// return null ;
	}

	private static Item solve1(Item it, Character q) {
		// 将it中所有得到q可直接到达的表达式放到 zhijie列表中 ;
		Item newitem = new Item();
		ArrayList<Exp> list1 = new ArrayList<>();
		ArrayList<Exp> list2 = it.expList;
		for (Exp exp : list2) {
			Character left = exp.left;
			String right1 = exp.right1;
			String right2 = exp.right2;
			Character zhanwang = exp.zhanwang;
			if (right2.length() >= 1 && q.equals(right2.charAt(0))) {
				list1.add(new Exp(left, right1 + q, right2.substring(1, right2.length()), zhanwang));
			}
			else if( right2.length()==0 && zhanwang.equals(q) ) {
				
			}
			//TODO
			
		}
		newitem.zhijie = list1;
		return newitem;
	}

	public static void solve(Item item) {
		// 根据直接产生式 推导出整个项目,然后将项目放到ItemList中,推导出来的状态放到linkedlist中
		// 并标记好产生式关系,以及判重 ;
		HashSet<Character> set1 = new HashSet<>();
		LinkedList<Exp> li = new LinkedList<>();
		HashSet<Exp> hashset = new HashSet<>();
		HashSet<Character> hashset1 = new HashSet<>();
		for (Exp e : item.zhijie) {
			// item.expList.add(e) ;
			hashset.add(e);
			Character left = e.left;
			String right1 = e.right1;
			String right2 = e.right2;
			Character zhanwang = e.zhanwang;
			if (right2.length() != 0) {
				if (right2.charAt(0) >= 'A' && right2.charAt(0) <= 'Z') {
					li.add(new Exp(left, right1, right2, zhanwang));
					set1.add(right2.charAt(0));
					hashset1.add(right2.charAt(0));
				} else {
					// li.add(new Exp(left, right1, right2, zhanwang));
					hashset1.add(right2.charAt(0));
					set1.add(right2.charAt(0));
				}

			} else {
				// 移到最后一个位置了 ;

			}
		}
		// 拿产生式来推导 ;
		while (!li.isEmpty()) {
			Exp top = li.peek();
			li.pop();
		//	Character left = top.left;
			//String right1 = top.right1;
			String right2 = top.right2;
			Character zhanwang = top.zhanwang;

			ArrayList<String> list2 = table.get(right2.charAt(0));// right2第一个元素是非终结符
																	// ;
			for (String s : list2) {
				if (s.charAt(0) >= 'A' && s.charAt(0) <= 'Z') {
					hashset1.add(s.charAt(0));
					ArrayList<Character> list3 = getFirst("" + right2.substring(1, right2.length()) + zhanwang);
					for (Character c : list3) {
						// item.expList.add(new Exp(right2.charAt(0),"",s,c));
						hashset.add(new Exp(right2.charAt(0), "", s, c));
						li.add(new Exp(right2.charAt(0), "", s, c));
						// System.out.println(new Exp(right2.charAt(0),"",s,c));
					}
					set1.add(s.charAt(0));
				} else if (!(s.charAt(0) >= 'A' && s.charAt(0) <= 'Z')) {
					hashset1.add(s.charAt(0));
					ArrayList<Character> list3 = getFirst("" + right2.substring(1, right2.length()) + zhanwang);
					for (Character c : list3) {
						// item.expList.add(new Exp(right2.charAt(0),"",s,c));
						hashset.add(new Exp(right2.charAt(0), "", s, c));
						// li.add(new Exp(right2.charAt(0), "", s, c));
					}
					set1.add(s.charAt(0));
				}

			}

		}
		for (Exp s : hashset)
			item.expList.add(s);
		for (Character d : hashset1)
			item.fol.add(d);
		// ItemList.add(item);
		set2.add(item);
		return;
	}

	public static void main(String[] args) {
		try {
			fr = new FileReader(new File("src/cn/lixuan/test03/2.txt"));
			BufferedReader bf = new BufferedReader(fr);
			String s;
			while ((s = bf.readLine()) != null) {

				String[] arr = s.split("->");
				Character st = arr[0].charAt(0);
				feiZhongJie.add(st);
				for (int r = 0; r < arr[1].length(); r++) {
					if (!(arr[1].charAt(r) >= 'A' && arr[1].charAt(r) <= 'Z'))
						zhongJie.add(arr[1].charAt(r));
				}
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
			int num = 0;
			for (Entry<Character, ArrayList<String>> g : table.entrySet()) {
				ArrayList<String> list2 = g.getValue();
				for (String s1 : list2) {
					HashMap<Character, Integer> map1 = new HashMap<>();
					map1.put(g.getKey(), ++num);
					guiYue.put(s1, map1);
				}
			}

			for (Entry<String, HashMap<Character, Integer>> e1 : guiYue.entrySet()) {

				for (Entry<Character, Integer> e2 : e1.getValue().entrySet()) {
					System.out.println("规约表达式" + e2.getValue() + ": " + e2.getKey() + "->" + e1.getKey());
				}
			}

			ArrayList<String> ac = new ArrayList<>();
			ac.add(start + "");
			table.put('S', ac);
			// 求First集合 :
			// 能直接计算的计算出来 不能直接计算的用拓扑排序思想（建图）
			int nodeNum = table.size(); // 非终结符个数 ;

			int rudu[] = new int['Z' + 1]; // 计算每个非终结符的入度 ;
			// HashMap<Character, String> ru = new HashMap<>();
			int[][] maze = new int['Z' + 1]['Z' + 1];
			for (Entry<Character, ArrayList<String>> e : table.entrySet()) {
				Set<Character> set1 = new HashSet<>();
				for (String s1 : e.getValue()) {
					if (s1.charAt(0) <= 'Z' && s1.charAt(0) >= 'A') {// 非终结符
						maze[s1.charAt(0)][e.getKey()] = 1;

						for (int k = 0; k < s1.length(); k++) {
							if (toEmpty(s1.charAt(k)) && k + 1 < s1.length()) {
								maze[s1.charAt(k + 1)][e.getKey()] = 1;
								set1.add(s1.charAt(k + 1));

							} else {
								break;
							}
						}

						if (!set1.contains(s1.charAt(0)))
							set1.add(s1.charAt(0));
					}
				}
				rudu[e.getKey()] = set1.size();
			}

			// 拓扑排序
			for (int k = 1; k <= nodeNum; k++) {
				for (Character c : table.keySet()) {
					if (!vis[c] && rudu[c] == 0) { // 如果该字符是未被访问的,且入度为0;
						// 求c字符的first集合 ;
						getFirst(c);
						for (Character s1 = 'A'; s1 <= 'Z'; s1++) {
							if (maze[c][s1] == 1) {
								maze[c][s1] = 0;
								rudu[s1]--;
							}
						}
						vis[c] = true;
						break;
					}
				}
			}

			// 先将I0直接产生式求出,然后 solve(I0) 一个个的解决 ;
			// 然后将I0的后面的字符 ;
			Item item = new Item();
			item.zhijie.add(new Exp('S', "", "" + start, '#')); // S->.E # 起始产生式
			list.add(item); // 放入起始产生式 ;
			ItemToInt.put(item, 1);

			int count = 1;
			while (!list.isEmpty()) {
				// list 不为空 ;
				Item it = list.peek();
				int count1 = ItemToInt.get(it);
				list.pop();
				solve(it);
				// 该产生式拓展后，按照它的fol进行状态转换，存为item.zhijie,放进list中 ;
				// 若不重复,则在map一个从前到后的映射 ;
				ArrayList<Character> fol = it.fol;
				abc: for (Character q : fol) {
					Item newitem = solve1(it, q); // 产生它的直接产生式 ;
					for (Item it1 : set2) { // 进行判重 ，若直接产生式不同再进行拓展 ;
						if (it1.equals(newitem)) {
							continue abc;
						}
					}
					if (list.contains(newitem))
						continue abc;
					list.add(newitem);
					count++;
					if (q >= 'A' && q <= 'Z') {
						HashMap<Character, Integer> map = Goto.get(count1) ;
						if(map==null)
							 map = new HashMap<>();
						map.put(q, count) ;
						Goto.put(count1, map);
					} else {
						//TODO 
						HashMap<Character, String> map1 = new HashMap<>();
						// s移进 r规约 ;
						for (Exp f : it.expList) {
//							System.out.println(f + " hello");
							if (f.right2.equals("") && f.zhanwang.equals(q)) {
								// 规约 ; 判断规约的产生式 ;
								int st = guiYue.get(f.right1).get(f.left);
								map1.put(q, "r" + st);
								System.out.println("规约:"+f);
								//Action.put(count1, map1);
								HashMap<Character, String> map = Action.get(count1) ;
								if(map==null)
									map =new HashMap<>() ;
								map.put(q, "r"+st) ;
								Action.put(count1, map) ;
								//System.out.println("hello1");
							} else if (f.right2.length() >= 1 && q.equals((f.right2.charAt(0)))) {
								// 移进 ;
								//System.out.println("hello2");
								System.out.println("移进:"+f);
								
								HashMap<Character, String> map = Action.get(count1) ;
								if(map==null)
									map =new HashMap<>() ;
								map.put(q, "s" + count) ;
								Action.put(count1, map) ;
							} else {
								System.out.println(f +" "+q);
							}
						}
						System.out.println(count1);
						
						
						
					}
					
					ItemToInt.put(newitem, count);
				}
			}
			System.out.println("Action:" + Action.size());

			System.out.println("项目集族共有" + set2.size() + "种状态");
			for (Item w : set2) {
				System.out.println("项目"+ItemToInt.get(w));
				for (Exp r : w.expList) {
					System.out.println(r);
				}
			}
			int nodenum = set2.size();
			// 分析表 ;
			// feiZhongJie.add('S') ;
			zhongJie.add('#');
			System.out.print("\t");
			for (Character c : zhongJie) {
				System.out.print(c + "\t");
			}
			for (Character c : feiZhongJie) {
				System.out.print(c + "\t");
			}
			System.out.println();
			
			for (int w = 1; w <= nodenum; w++) {
				System.out.print("" + w + "\t");
				for (Character c : zhongJie) {
					if (Action.get(w) != null && Action.get(w).get(c) != null) {
						System.out.print(Action.get(w).get(c) + "\t");
					}else {
						System.out.print("\t");
					}
				}
				for (Character c : feiZhongJie) {
					if (Goto.get(w) != null && Goto.get(w).get(c) != null) {
						System.out.print(Goto.get(w).get(c) + "\t");
					}else {
						System.out.print("  \t");
					}
				}
				
				System.out.println();
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
