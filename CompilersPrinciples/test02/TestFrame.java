package cn.lixuan.test02;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;
import java.awt.event.ActionEvent;

public class TestFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField Source;
	private JScrollPane scrollPane;
	private JTextArea FirstTable;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JTextField txtFirst;
	private JTextField txtFollow;
	private JTextField textField_4;
	private JTextField SourceText;
	private JButton Action2;
	private JScrollPane scrollPane_3;
	private JTextField textField_6;
	private JTextArea Result = new JTextArea();

//*************************************************************************************
	public static StringBuilder FirstText =new StringBuilder() ;
	public static StringBuilder FollowText = new StringBuilder() ;
	public static StringBuilder TableText =new StringBuilder() ;
	public static StringBuilder ResultText =new StringBuilder() ;
	public static HashSet<Character>set1 =new HashSet<>() ;
	public static HashMap<Character, ArrayList<String>> hashmap = null;
	public static HashMap<Character, Set<Character>> First = new HashMap<>();
	public static HashMap<Character, Set<Character>> Follow = new HashMap<>();
	HashMap<Character,HashMap<Character,String>>table =null ;
	public static boolean vis[] = new boolean['Z' + 1];
	public static boolean toEmpty[] = new boolean['Z' + 1];
	public static Character start  =null ; // 文法起始符号  ;
	private JTextArea Table;
	private JTextArea FollowTable;
	public static void getFirst(Character c) {
		ArrayList<String> list = hashmap.get(c);
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

	public static boolean toEmpty(Character c) { // 判断是否能变成空串 ;
		if (c == 'ε')
			return true;
		else if (c >= 'A' && c <= 'Z') {
			ArrayList<String> list = hashmap.get(c);
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
					//	System.out.println(c + " " + s.charAt(k));
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
	/**
	 * 对于字符D的follow求法描述：
	 *  1.若在产生式中未找到该字符,退出。
	 *  2.若找到了 
	 *  	1)若为最后一个字符，则将推出D的字符的follow集合加到D的follow集合中 ;
	 *  	2)若不是最后一个字符：  
	 *  	   -若后面的字符是终结符，那么将这个终结符加到D的follow集合中 ;
	 *  	   -若后面的字符是非终结符
	 *  			--若该终结符的first集合不包含空串，则将first集合加到D的follow集合中
	 *  			--若该终结符的first集合包含空串，则将该集合去掉空串加到D的follow集合中，判断后面的字符，继续循环2.
	 */
	
	public static void getFollow(Character c) {
		HashSet<Character>set =new HashSet<>() ;
		for( Entry<Character, ArrayList<String>>e  :hashmap.entrySet()){
			
			ArrayList<String> list = e.getValue();
			for(String s : list){
				for(int k =0 ;k<s.length() ;k++){
					if(s.charAt(k) == c){
						// 找到该字符 ; 
						if(k==s.length()-1){ // 它是最后一个元素 ;
							if(c!=e.getKey())
							set.addAll(Follow.get(e.getKey())) ;
						}else { // 后面有元素 ; 
							if(s.charAt(k+1)>'Z' || s.charAt(k+1)<'A'){ // 后面是终结符 ,直接加入follow集合
								set.add(s.charAt(k+1)) ; 
							}else { //后面是非终结符 
								boolean flags =false ;
								Set<Character> set2 = First.get(s.charAt(k+1)) ;
								for(Character q:set2){
									if(q=='ε')
										flags =true ;
									else 
										set.add(q) ;
								}
								if(flags){
									k++ ;
									 while(flags && k<s.length()-1 && First.get(s.charAt(k)).contains('ε')){
										 for(Character q:First.get(s.charAt(k+1))){
												if(q!='ε')
													set.add(q) ;
											}
										 k++ ;
									 }
									 flags=false ;
									 if(k==s.length()-1){
										 if(c!=e.getKey()){
											 //set.addAll(First.get(e.getKey())) ;
											 for(Character q :First.get(s.charAt(s.length()-1)) ){
												 if(q=='ε'){
													 flags =true ; 
												 }else{
													 set.add(q) ;
												 }
											 }
											 if(flags){
												 if(c!=e.getKey())
														set.addAll(Follow.get(e.getKey())) ;
											 }
										 }		 
									}
								}
							}		
						 }
					}
				}
			}
		}
		if(c==start)set.add('#') ;// 起始符号  follow 加上#  
		Follow.put(c, set) ;
	}
	public static String showStack(Stack<Character>sta){ //将栈里的元素打印出来 ;
		List<Character>list =sta ;
		StringBuilder sb =new StringBuilder() ;
		for(Character x: list){
			sb.append(x) ;
		}
		return sb.toString();
	}
	
	
	
//********************************************************************	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestFrame frame = new TestFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestFrame() {
		setTitle("LL1文法分析器");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1563, 716);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);	
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setFont(new Font("宋体", Font.PLAIN, 20));
		textField.setText("文法源文件地址：");
		textField.setBounds(54, 26, 168, 27);
		contentPane.add(textField);
		textField.setColumns(10);
		
		Source = new JTextField();
		Source.setFont(new Font("宋体", Font.PLAIN, 24));
		Source.setText("src/cn/lixuan/test02/1.txt");
		Source.setBounds(237, 26, 484, 27);
		contentPane.add(Source);
		Source.setColumns(10);
		
		JButton Action1 = new JButton("文法分析");
		Action1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FirstText =new StringBuilder() ;
				FollowText =new StringBuilder() ;
				TableText =new StringBuilder() ;
				//分析文法 ， 求first和follow集合 预测分析表 ；；
				//File file = new File("src/cn/lixuan/test02/1.txt");
				File file = new File(Source.getText());
				FileReader fr = null;
				BufferedReader br = null;
				try {
					
					fr = new FileReader(file);
					br = new BufferedReader(fr);
					set1.add('#') ;
					String line = null;
					hashmap = new HashMap<>();
					while ((line = br.readLine()) != null) {
						String[] arr = line.split("->");
						Character c = arr[0].charAt(0);
						if(start==null){
							start = c ;
						}
						String[] arr1 = arr[1].split("\\|"); // 切记：要转义
						for (String s : arr1)
							s = s.trim();
						for(String a : arr1){
							for(int k =0 ;k<a.length() ;k++)
								if(a.charAt(k)!='ε' &&  (a.charAt(k)<'A'|| a.charAt(k)>'Z') )
									set1.add(a.charAt(k) ) ;
						}
						ArrayList<String> array = new ArrayList<>();
						for (String a : arr1)
							array.add(a);
						if (!hashmap.containsKey(c))
							hashmap.put(c, array);
						else {
							ArrayList<String> ar = hashmap.get(c);
							ar.addAll(array);
							hashmap.put(c, ar);
						}
					}

					// 求First集合 :
					// 能直接计算的计算出来 不能直接计算的用拓扑排序思想（建图）
					int nodeNum = hashmap.size(); // 非终结符个数 ;
					int rudu[] = new int['Z' + 1]; // 计算每个非终结符的入度 ;
					// HashMap<Character, String> ru = new HashMap<>();
					int[][] maze = new int['Z' + 1]['Z' + 1];
					for (Entry<Character, ArrayList<String>> w : hashmap.entrySet()) {
						Set<Character> set1 = new HashSet<>();
						for (String s : w.getValue()) {
							if (s.charAt(0) <= 'Z' && s.charAt(0) >= 'A') {// 非终结符
								maze[s.charAt(0)][w.getKey()] = 1;

								for (int k = 0; k < s.length(); k++) {
									if (toEmpty(s.charAt(k)) && k + 1 < s.length()) {
										maze[s.charAt(k + 1)][w.getKey()] = 1;
										set1.add(s.charAt(k + 1));

									} else {
										break;
									}
								}

								if (!set1.contains(s.charAt(0)))
									set1.add(s.charAt(0));
							}
						}
						rudu[w.getKey()] = set1.size();
					}
					
					// 拓扑排序
					for (int k = 1; k <= nodeNum; k++) {
						for (Character c : hashmap.keySet()) {
							if (!vis[c] && rudu[c] == 0) { // 如果该字符是未被访问的,且入度为0;
								// 求c字符的first集合 ;
								getFirst(c);
								//System.out.print("First(" + c + ") : ");
								//for (Character w : First.get(c)) {
								//	System.out.print(w + "  ");
								//}
								//System.out.println();
								FirstText =FirstText.append("First(" + c + ") : ") ;
								for (Character w : First.get(c)) 
									FirstText.append(w+" ");
								FirstText.append("\r\n") ;
								// 更新入度 ;
								for (Character s = 'A'; s <= 'Z'; s++) {
									if (maze[c][s] == 1) {
										maze[c][s] = 0;
										rudu[s]--;
									}
								}
								vis[c] = true;
								break;
							}
						}
					}
					FirstTable.setText(FirstText.toString());
					// ********************************************
					// 求Follow集合 ;
					//System.out.println("*******************************************");
					for(int k=0 ;k<maze.length;k++)
						Arrays.fill(maze[k], 0);// 初始化maze数组，再次记录先后关系
					Arrays.fill(rudu, 0);
					Arrays.fill(vis, false);
					for( Entry<Character, ArrayList<String>> s   :hashmap.entrySet()){
						ArrayList<String> list = s.getValue(); 
						HashSet<Character>set =new HashSet<>() ;
						for(String s1 :list){
							int index =s1.length()-1 ; 
							while( index>=0 && s1.charAt(index)>='A'&&s1.charAt(index)<='Z'){
								if(s1.charAt(index)==s.getKey()){
									index-- ;
									continue ;
								}
								set.add(s1.charAt(index)) ;
								if(maze[s.getKey()][s1.charAt(index)]==0){
									maze[s.getKey()][s1.charAt(index)]=1 ;
									rudu[s1.charAt(index)]++ ;
								}
								if(!toEmpty(s1.charAt(index))){
									break ;
								}
								index-- ;	
							}	
						}
						//rudu[s.getKey()] = set.size() ; 
					}
					for(int k=1 ;k<=nodeNum ;k++){
						for (Character c : hashmap.keySet()) {
							if (!vis[c] && rudu[c] == 0) { // 如果该字符是未被访问的,且入度为0;
								getFollow(c);
//								System.out.print("Follow(" + c + ") : ");
//								for(Character q : Follow.get(c))
//									System.out.print(q+" ");
//								System.out.println();
								FollowText.append("Follow(" + c + ") : ") ;
								for(Character q : Follow.get(c))
									FollowText.append(q+" ") ;
								FollowText.append("\r\n") ;
								// 更新入度 ;
								for (Character s = 'A'; s <= 'Z'; s++) {
									if (maze[c][s] == 1) {
										maze[c][s] = 0;
										rudu[s]--;
									}
								}
								vis[c] = true;
								break;	
							}
						}	
					}
					FollowTable.setText(FollowText.toString());
					//  构造预测分析表 ;
					table =new HashMap<>() ;
					for( Entry<Character, ArrayList<String>> s   :hashmap.entrySet()){
						//System.out.print(s.getKey()+": ");
						ArrayList<String> list = s.getValue() ;
						for(String str : list){
							if(str.equals("ε")){
								//  将  <s.getkey , follow(s.gtekey) >放进矩阵 ;
								for(Character q : Follow.get(s.getKey())){
									HashMap<Character,String>map1 =new HashMap<>() ;
									map1.put(q, str) ; 
									//System.out.print(" "+q+" ");
									if(table.get(s.getKey())!=null)
									map1.putAll(table.get(s.getKey())) ;
									table.put(s.getKey(),map1  ) ;
								}
							}else{
								//  将<s.getkey,first(str)>放入矩阵 ;
								int index =0 ; 
								if(str.charAt(index)>'Z'||str.charAt(index)<'A'){
									HashMap<Character,String>map1 =new HashMap<>() ;
									map1.put(str.charAt(index), str) ; 
									//System.out.print(" "+str.charAt(index)+" ");
									if(table.get(s.getKey())!=null)
									map1.putAll(table.get(s.getKey())) ;
									table.put(s.getKey(),map1  ) ;
									continue ;
								}
								while(index<str.length() && str.charAt(index)<='Z' && str.charAt(index)>='A'){
									for(Character q : First.get(str.charAt(index))){
										if(q!='ε'){
										HashMap<Character,String>map1 =new HashMap<>() ;
										map1.put(q, str) ; 
										//System.out.print(" "+q+" ");
										if(table.get(s.getKey())!=null)
										map1.putAll(table.get(s.getKey())) ;
										table.put(s.getKey(),map1  ) ;
										}
									}
									if(! First.get(str.charAt(index)).contains('ε')){
										break ;
									}
									index++ ;
								}
							}
						}
					}
					// 根据预测分析表 对输入字符串进行判断 ;
					TableText.append("\t") ;
				for(Character c : set1){
					TableText.append(c+"\t") ;
				}
				TableText.append("\r\n") ;
				for(Character c : table.keySet()){
					TableText.append(c+"\t") ;
					for(Character s :set1){
						if(table.get(c).containsKey(s))
							TableText.append(table.get(c).get(s)+"\t");
						else
							TableText.append("   \t") ;
					}
					TableText.append("\r\n") ;
				}
					
					
				Table.setText(TableText.toString());
				} catch (Exception s) {
					s.printStackTrace();
				} finally {
					try {
					
					} catch (Exception s) {
						s.printStackTrace();
					}
				}
				
			}
		});
		Action1.setFont(new Font("宋体", Font.PLAIN, 21));
		Action1.setBounds(777, 25, 123, 29);
		contentPane.add(Action1);
		Result.setEditable(false);
		Result.setFont(new Font("Monospaced", Font.PLAIN, 23));
		scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 107, 322, 230);
		contentPane.add(scrollPane);
		
		FirstTable = new JTextArea();
		FirstTable.setEditable(false);
		FirstTable.setFont(new Font("Monospaced", Font.PLAIN, 23));
		scrollPane.setViewportView(FirstTable);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(370, 107, 530, 230);
		contentPane.add(scrollPane_1);
		
		FollowTable = new JTextArea();
		FollowTable.setFont(new Font("Monospaced", Font.PLAIN, 22));
		FollowTable.setEditable(false);
		scrollPane_1.setViewportView(FollowTable);
		
		
		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(15, 368, 919, 260);
		contentPane.add(scrollPane_2);
		
		Table = new JTextArea();
		Table.setEditable(false);
		Table.setFont(new Font("Monospaced", Font.PLAIN, 21));
		scrollPane_2.setViewportView(Table);
		
		txtFirst = new JTextField();
		txtFirst.setEditable(false);
		txtFirst.setFont(new Font("宋体", Font.PLAIN, 21));
		txtFirst.setText("First集合：");
		txtFirst.setBounds(15, 79, 128, 27);
		contentPane.add(txtFirst);
		txtFirst.setColumns(10);
		
		txtFollow = new JTextField();
		txtFollow.setEditable(false);
		txtFollow.setFont(new Font("宋体", Font.PLAIN, 21));
		txtFollow.setText("Follow集合：");
		txtFollow.setBounds(370, 79, 144, 27);
		contentPane.add(txtFollow);
		txtFollow.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setEditable(false);
		textField_4.setFont(new Font("宋体", Font.PLAIN, 21));
		textField_4.setText("预测分析表：");
		textField_4.setBounds(15, 339, 155, 29);
		contentPane.add(textField_4);
		textField_4.setColumns(10);
		
		SourceText = new JTextField();
		SourceText.setFont(new Font("宋体", Font.PLAIN, 21));
		SourceText.setText("i+i*i");
		SourceText.setBounds(1008, 113, 332, 27);
		contentPane.add(SourceText);
		SourceText.setColumns(10);
		
		Action2 = new JButton("句型分析");
		Action2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ResultText=new StringBuilder() ;
				// 判断一个一个表达式是不是该文法的一个句型 ;
				//思路：利用两个栈实现，一个符号栈 一个输入串栈  ;
				Stack<Character>fuhao =new Stack<>() ; 
				Stack<Character>input =new Stack<>() ; 
				fuhao.push('#') ; //将#和起始符号压入字符栈 ;
				fuhao.push(start) ; 
				//System.out.println("请输入一个表达式(如i+i*i):");
				String str  = SourceText.getText() ; 
				//Scanner sc =new Scanner(System.in) ; 
				//str =sc.next() ; 
				//str ="i+i*i" ; //用于测试 ;
				
				input.push('#') ;  //将#和表达式串倒序压入input栈 ;
				for(int k =str.length()-1 ;k>=0 ;k--){
					input.push(str.charAt(k)) ; 
				}
				ResultText.append("分析栈"+"\t"+"剩余输入串"+" 所用产生式   动 作"+"\r\n") ;
				ResultText.append(showStack(fuhao)+" \t "+new StringBuilder(showStack(input)).reverse()+
						"\t    \t    初始化"+"\r\n") ;
				//开始预测分析 ;
	 			
				while(!fuhao.empty()){
					Character top_fuhao =fuhao.peek() ; 
					Character top_input =input.peek() ;
					if(top_fuhao=='#'){
						if(top_input=='#'){
							//System.out.println("匹配成功 ");
							ResultText.append("匹配成功\r\n") ;
						}
						else{
							ResultText.append("匹配失败\r\n") ;
						}
						break ;
					}else if(top_fuhao=='ε'){
						fuhao.pop() ;
						ResultText.append(showStack(fuhao)+" \t"+new StringBuilder(showStack(input)).reverse()+
								"\t\t  POP()"+"\r\n") ;
						//System.out.println(showStack(fuhao)+"  "+new StringBuilder(showStack(input)).reverse());
					}
					else if( !(top_fuhao<='Z'&& top_fuhao>='A')  ) {
						//终结符 ;
						if(top_fuhao!=top_input){
							
							ResultText.append("匹配失败\r\n") ;
							break ; 
						}else{
						//	System.out.println(top_input+"出栈");
							fuhao.pop() ;
							input.pop() ;
							//System.out.println(showStack(fuhao)+"  "+new StringBuilder(showStack(input)).reverse());
							ResultText.append(showStack(fuhao)+" \t"+new StringBuilder(showStack(input)).reverse()+
									" \t\t  POP()"+"\r\n") ;
						}
					}else {
						//非终结符 ;
						if(table.get(top_fuhao).containsKey(top_input)){
							fuhao.pop() ;
							String s1 =table.get(top_fuhao).get(top_input) ; 
							for(int k =s1.length()-1 ;k>=0 ;k--){
								fuhao.push(s1.charAt(k)) ;
							}
							//System.out.println(showStack(fuhao)+"  "+new StringBuilder(showStack(input)).reverse());
							ResultText.append(showStack(fuhao)+" \t"+new StringBuilder(showStack(input)).reverse()+
									" \t  "+(char)top_fuhao+"->"+s1+"   POP,PUSH("+new StringBuilder(s1).reverse()+")\r\n") ;
						}else{
							ResultText.append("匹配失败\r\n") ;
							break ; 
						}
					}
				}
				Result.setText(ResultText.toString());
				
			}
		});
		Action2.setFont(new Font("宋体", Font.PLAIN, 21));
		Action2.setBounds(1380, 112, 123, 29);
		contentPane.add(Action2);
		
		scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(949, 234, 577, 394);
		contentPane.add(scrollPane_3);
		

		scrollPane_3.setViewportView(Result);
		
		textField_6 = new JTextField();
		textField_6.setEditable(false);
		textField_6.setFont(new Font("宋体", Font.PLAIN, 21));
		textField_6.setText("分析结果：");
		textField_6.setBounds(949, 205, 123, 27);
		contentPane.add(textField_6);
		textField_6.setColumns(10);
	}
}
