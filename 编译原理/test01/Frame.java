package cn.lixuan.test01;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

class Word {
	String val; // 单词的值 ;
	int type; // 单词种别 ;
	int x; // 单词的位置横坐标
	int y; // 单词的位置纵坐标

	Word(String val, int type, int x, int y) {
		this.val = val;
		this.type = type;
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
//		return "    " + String.format("%3s   ",val ) + String.format(" (%2d,%3s)",type,val )+String.format(" %s  ", Main.types[type] )
//		+String.format("   (%3d,%3d)",x,y );
		//return String.format("%3s  (%2d,%3s)  %10s   (%3d,%3d) ",val,type,val,Main.types[type],x,y) ;
		return "    " + val + "   \t(" + type + "," + val + ")\t" + Main.types[type] + "\t(" + x + "," + y + ")";
	}
}

public class Frame extends JFrame {

	private JPanel source;
	private JTextField path;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame();
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
	public Frame() {
		setTitle("词法分析器");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		setBounds(100, 100, 1107, 514);
		source = new JPanel();
		source.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(source);
		source.setLayout(null);

		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 19));
		textArea.setText("源文件路径：");
		textArea.setBounds(56, 15, 106, 33);
		source.add(textArea);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(450, 82, 620, 282);
		source.add(scrollPane_1);

		JTextArea result = new JTextArea();
		result.setEditable(false);
		result.setLineWrap(true);
		result.setFont(new Font("Monospaced", Font.PLAIN, 18));
		scrollPane_1.setViewportView(result);

		path = new JTextField();
		path.setFont(new Font("宋体", Font.PLAIN, 22));
		path.setText( "src/cn/lixuan/test01/1.txt");
		path.setBounds(177, 14, 423, 34);
		source.add(path);
		path.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(66, 77, 356, 287);
		source.add(scrollPane);

		JTextArea source_code = new JTextArea();
		source_code.setFont(new Font("Monospaced", Font.PLAIN, 19));
		scrollPane.setViewportView(source_code);
		source_code.setLineWrap(true);
		JButton button3 = new JButton("程序词法分析");
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					// fileName = "src/cn/lixuan/test01/1.txt"; // 使用相对路径 ;
					String des = source_code.getText();
					// System.out.println(des);
					int index = 0;
					list.clear();
					lineNum = 1;
					rowNum = 1;
					while (index < des.length()) {
						index = getWords(index, des);
					}
					result.setText("   单词   \t二元序列\t类型    \t位置\r\n");
					for (Word word : list) {
						result.setText(result.getText() + word.toString() + "\r\n");
					}
				//	System.out.println("读取完成");
				} catch (Exception s) {
					s.printStackTrace();
				}

			}
		});

		JButton button1 = new JButton("词法分析");
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					// fileName = "src/cn/lixuan/test01/1.txt"; // 使用相对路径 ;
					fileName = path.getText();
					file = new File(fileName);
					fsin = new FileInputStream(file);
					byte[] arr = new byte[fsin.available()];
					fsin.read(arr);
					String des = new String(arr);
					source_code.setText(des);
					int index = 0;
					list.clear();
					lineNum = 1;
					rowNum = 1;
					while (index < des.length()) {
						index = getWords(index, des);
					}
					
					result.setText("   单词   \t二元序列\t类型    \t位置\r\n");
					for (Word word : list) {
						result.setText(result.getText() + word.toString() + "\r\n");
					}

				} catch (Exception s) {
					s.printStackTrace();
				}
			}
		});
		button1.setBounds(628, 19, 123, 29);
		source.add(button1);

		button3.setBounds(152, 379, 153, 33);
		source.add(button3);

	}

	public static String[] types = { "错误", "关键字", "分界符", "运算符", "关系运算符", "常数", "标识符",
									// 0		 1 		2 			3 			4 			5 		6
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
			put("int", 5);
			put("then", 6);
			
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
			put("\"", 7);
			put("{", 8);
			put("}", 9);

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
			put("%", 10);
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
			put("==", 7);
		}
	}; // 关系运算符

	public static int getWords(int index, String str) {
		// 读取一个字符串

		while ((str.charAt(index) == ' ' || str.charAt(index) == '\t')) {
			index++;
			if (index >= str.length())
				return str.length();
			return index;
		}
		// 因为JAVA中的换行是 "\r\n" 所以要判断两个字符 ;
		if (str.charAt(index) == '\r' && str.charAt(index + 1) == '\n') {
			// 换行 ;
			rowNum = 1;
			lineNum++;
			index += 2;
			return index;
		}else // 识别文本框中输入的回车'\n'
			if(str.charAt(index)=='\n'){
			rowNum=1 ;
			lineNum++ ;
			index++ ;
			return index ;
		}
		StringBuilder sb = new StringBuilder();
		if ((str.charAt(index) <= 'z' && str.charAt(index) >= 'a')) {
			// 首字符是字母 ;(关键字 标识符)
			// 拼出字符串 ;
			while ((str.charAt(index) <= 'z' && str.charAt(index) >= 'a')
					|| (str.charAt(index) <= '9' && str.charAt(index) >= '0'
					 )) {
				sb.append(str.charAt(index));
				index++;
				if (index == str.length())
					break;
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
					|| (str.charAt(index) <= '9' && str.charAt(index) >= '0'|| str.charAt(index)=='.')) {
				sb.append(str.charAt(index));
				index++;
				if(index>=str.length())
					break ;
			}
			String str1 = sb.toString();
			boolean error = false;
			int num =0 ; 
			for (int k = 0; k < str1.length(); k++) {
				if (str1.charAt(k) <= 'z' && str1.charAt(k) >= 'a' ) {
					
					error = true;
					break;
				}else if(str1.charAt(k)=='.'){
					num++ ;
				}
			}
			
			if (error || num>=2) {
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
			if (yunSuan.containsKey(str.charAt(index) + "")) {

				String s = "" + str.charAt(index);
				index++;
				
				if(index<str.length()){
					s += str.charAt(index);
					if (yunSuan.containsKey(s)) {
						//System.out.println(s);
						index++;
						list.add(new Word(s, 3, lineNum, rowNum));
					} else {
						list.add(new Word(s.substring(0, s.length() - 1), 3, lineNum, rowNum));
					}
				}
				else {
					list.add(new Word(s, 3, lineNum, rowNum));
				}
				rowNum++;
				return index;
			} else // 关系运算符 4
			if (guanXiYunSuan.containsKey(str.charAt(index) + "")) {
				String s = "" + str.charAt(index);
				index++;
				if(index<str.length()){
					s += str.charAt(index);
					
					if (guanXiYunSuan.containsKey(s)) {
						index++;
						
						list.add(new Word(s, 4, lineNum, rowNum));
					} else {
						list.add(new Word(s.substring(0, s.length() - 1), 4, lineNum, rowNum));
					}
				}
				else {
					list.add(new Word(s, 4, lineNum, rowNum));
				}
				rowNum++;
				return index;
			} else// 分界符 2
			if (fenJie.containsKey(str.charAt(index) + "")) {

				list.add(new Word("" + str.charAt(index), 2, lineNum, rowNum));
				index++;
				rowNum++;
				return index;
			} else {
				// error 0
				// System.out.println("str.charAt:"+str.charAt(index));

				list.add(new Word("" + str.charAt(index), 0, lineNum, rowNum));
				index++;
				rowNum++;
				return index;
			}

		}
	}
}
