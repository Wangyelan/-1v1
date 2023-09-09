package ThreeCountryKill;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.security.auth.Subject;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.mysql.cj.x.protobuf.MysqlxNotice.Frame;


import java.util.EventListener;

class Player implements EventListener{

	Socket socket;
	Card weapon;
	Card armor;
	Card[] judge;//judge[0]乐不思蜀,judge[1]兵粮寸断，由于1v1牌堆没有闪电，所以不设置闪电判定区
	Card[] handCard;
	CardHeap cardHeap;

	Player enemy;
	
	ExecutorService pool = Executors.newCachedThreadPool();//线程池
	
	JTextField bloodShow = new JTextField();
	JTextField upperShow = new JTextField();
	JTextField EbloodShow = new JTextField();
	JTextField EupperShow = new JTextField();
	//显示生命值的线程所用标签
	
	JTextField LBSSShow = new JTextField();
	JTextField BLCDShow = new JTextField();
	JTextField ELBSSShow = new JTextField();
	JTextField EBLCDShow = new JTextField();
	//显示判定区的线程所用的标签
	
	boolean check = true;//用于判断有没有出闪或者出杀
	//cardHeap.attributeAdded(ServletContextAttributeEvent scae);
	
	HashMap<String,Integer> hashMap = new HashMap<>();
	int blood;
	int upperLimitBlood;//体力上限
	int upperLimitKill;//回合最大出杀数
	int useKillNumber;//回合出杀数
	int cardNumber;//手牌数
	int place;
	int myPlaceX;//记录下一张我方手牌在JFrame中的位置
	int enemyX;//记录下一张敌方手牌在JFrame中的位置
	int WXKJNumber = 0;//无懈可击数量
	
	boolean isBLCD = false;
	boolean isLBSS = false;
	boolean isMyturn = false;//用于判断是否是自己的回合
	boolean isDying = false;//用于判断是否处于濒死阶段
	boolean isMiss = false;//用于判断是否能出闪
	boolean isKill = false;//用于判断是否能响应杀（南蛮入侵、决斗出杀）
	boolean isDuel = false;;//用于判断是否处于决斗结算阶段
	boolean isZBSM = false;//用于判断是否处于丈八蛇矛选牌阶段
	boolean isGSF = false;//用于判断是否处于贯石斧结算阶段
	boolean isHBJ = false;//用于判断是否处于寒冰剑结算阶段
	boolean isDesert = false;//用于判断是否处于弃牌阶段
	boolean isWXKJ = false;
	
	
	int ZBSMNumber = 0;//用于计算是否处于丈八蛇矛已选牌数
	int GSFNumber = 0;//用于计算是否处于贯石斧已选牌数
	int desertNumber = 0;//用于计算是否处于弃牌阶段已选牌数
	
	Stack<Button> buttonStack = new Stack<Button>();//敌人的手牌
	Button[] buttonArray = new Button[0];//自己的手牌
	Button bKill = new Button("取消");//杀的取消按钮
	Button bMiss = new Button("取消");//闪的取消按钮
	Button bDying = new Button("取消");//濒死状态的的取消按钮
	Button bWXKJ = new Button("取消");//无懈可击的取消按钮
	Button bZBSM = new Button("丈八蛇矛");
	Button bHBJSure = new Button("确定");
	Button bHBJCancel= new Button("取消");
	Button bGSFSure = new Button("确定");
	Button bGSFCancel = new Button("取消");
	
	JLabel[] arm = new JLabel[2];//0是武器，1是防具
	JLabel[] enemyArm = new JLabel[2];
	JLabel labelDesert = new JLabel("弃牌阶段，请弃牌");
	JLabel labelUseKill = new JLabel("请出杀");
	JLabel labelUseMiss = new JLabel("请出闪");
	JLabel labelUsePeach = new JLabel("您已处于濒死状态，请使用一张桃");
	JLabel labelUseWXKJ = new JLabel("请打出一张无懈可击");
	JLabel labelHBJ = new JLabel("是否发动寒冰剑");
	JLabel labelGSF = new JLabel("是否发动贯石斧");	
	
	JFrame frame;
	Container contentPane;
	String name;
	
	JudgeStage judgeStage;
	TakeCardStage takeCardStage;
	OutCardStage outCardStage;
	DesertCardStage desertCardStage;
	/*
	 * 这四个变量是判定阶段、摸牌阶段、出牌阶段、弃牌阶段
	 * */
	
	Player(CardHeap cardHeap,int p,String name) throws IOException{
		blood = name.equals("玩家一")?5:4;
		upperLimitBlood = name.equals("玩家一")?5:4;
		
		this.name = name;
		myPlaceX = 0;
		frame = new JFrame();
		frame.addComponentListener(null);
		contentPane = frame.getContentPane();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane.setLayout(null);
		frame.setSize(1000,600);
		frame.setVisible(true);
		bDying.addActionListener(new DyingCancel());
		bZBSM.addActionListener(new ZBSMListener(this));
		
		Image img1;
		if(name.equals("玩家一"))
			img1= ImageIO.read(getClass().getResource("上兵伐谋.jpg"));
		else
			img1 = ImageIO.read(getClass().getResource("骆统.jpg"));

		img1.getScaledInstance(80, 150, Image.SCALE_SMOOTH);
		
		JButton jName = new JButton(name);
		
		jName.setIcon(new ImageIcon(img1));
		
		
		contentPane.add(jName);
		jName.setBounds(900, 400, 80, 150);
		arm[0] = new JLabel("武器区");
		arm[0].setBounds(900,350,80,25);
		arm[1] = new JLabel("防具区");
		arm[1].setBounds(900,375,80,25);
		frame.add(arm[0]);
		arm[0].setFont(new Font("Helvetica",Font.PLAIN,10));
		arm[1].setFont(new Font("Helvetica",Font.PLAIN,10));
		frame.add(arm[1]);
		bMiss.addActionListener(new CancelMissListener(this));
		bKill.addActionListener(new CancelKillListener(this));
		
		enemyArm[0] = new JLabel("武器区");
		enemyArm[0].setBounds(0,150,80,25);
		enemyArm[1] = new JLabel("防具区");
		enemyArm[1].setBounds(0,175,80,25);
		frame.add(enemyArm[0]);
		enemyArm[0].setFont(new Font("Helvetica",Font.PLAIN,10));
		enemyArm[1].setFont(new Font("Helvetica",Font.PLAIN,10));
		frame.add(enemyArm[1]);
		
		JButton jNameEnemy = new JButton((name.equals("玩家一")?"玩家二":"玩家一"));
		Image img2;
		if(name.equals("玩家一"))
			img2 = ImageIO.read(getClass().getResource("骆统.jpg"));
		else
			img2 = ImageIO.read(getClass().getResource("上兵伐谋.jpg"));
		img2.getScaledInstance(80, 150, Image.SCALE_SMOOTH);
		jNameEnemy.setIcon(new ImageIcon(img2));
		
		jName.setIcon(new ImageIcon(img1));
		contentPane.add(jNameEnemy);
		jNameEnemy.setBounds(0, 0, 80, 150);
		enemyX = 900;
		
		
		labelUseKill.setBounds(450, 200, 200, 50);
		labelUseMiss.setBounds(450, 200, 200, 50);
		labelUseWXKJ.setBounds(450, 200, 200, 50);
		//设置三个提示标签的位置
		
		
		place = p;
		weapon =  null;
		armor = null;
		judge = new Card[2];
		handCard = new Card[0];
		upperLimitKill = 1;
		useKillNumber = 0;
		cardNumber = 0;
		judgeStage = new JudgeStage(this,cardHeap,false);
		takeCardStage = new TakeCardStage(this,cardHeap,false);
		outCardStage = new OutCardStage(this,cardHeap,false);
		desertCardStage = new DesertCardStage(this,cardHeap,false);
		this.cardHeap = cardHeap;
				
		bloodShow.setText("当前生命值:" + Integer.toString(blood));
		upperShow.setText("生命上限:" + Integer.toString(upperLimitBlood));
		EbloodShow.setText("当前生命值:" + Integer.toString(blood));
		EupperShow.setText("生命上限:" + Integer.toString(upperLimitBlood));
		bloodShow.setEditable(false);
		upperShow.setEditable(false);
		EbloodShow.setEditable(false);
		EupperShow.setEditable(false);
		
		LBSSShow.setEditable(false);
		BLCDShow.setEditable(false);
		ELBSSShow.setEditable(false);
		EBLCDShow.setEditable(false);
		
		frame.add(bloodShow);
		frame.add(upperShow);
		frame.add(EbloodShow);
		frame.add(EupperShow);
		frame.add(LBSSShow);
		frame.add(BLCDShow);
		frame.add(ELBSSShow);
		frame.add(EBLCDShow);

		
		for(int i = 0; i < 4;i ++) {
			Card[] tempCard = new Card[cardNumber + 1];
			for(int j = 0;j < cardNumber;j ++) {
				tempCard[j] = handCard[j];
			}
			Card temp = cardHeap.takeCard();
			tempCard[cardNumber] = temp;
			handCard = tempCard;
			String str = temp.name+" "+temp.flowerColor+Integer.toString(temp.point);
			if(str.contains("无懈可击"))
				WXKJNumber ++;
			hashMap.put(str, cardNumber);
			Button b = new Button(str);
			new ButtonListener(this,b).addListener();
			frame.add(b);	
			addButton(b);
			b.setFont(new Font("Helvetica",Font.PLAIN,10));
			b.setBounds(myPlaceX,400,80,150);
			
			Button Enemyb = new Button();
			buttonStack.add(Enemyb);
			frame.add(Enemyb);
			Enemyb.setBounds(enemyX, 0, 80, 150);
			
			myPlaceX += 80;
			enemyX -= 80;
			cardNumber = cardNumber + 1;
		}
		/*
		 * 初始化时给每名玩家分配4张手牌，这个循环是摸牌操作
		 * 至于为什么没有用后文所写的TakeCard方法，
		 * 因为初始化的时候enemy为null
		 * */
	}
	void getInjure() {//受到一点伤害
		System.out.println(name + "受到一点伤害");
		blood --;
		if(blood == 0) 
			Dying();
	}
	void Dying() {//濒死状态
		isDying = true;
		labelUsePeach.setVisible(true);
		labelUsePeach.setBounds(400,225,200,25);
		bDying.setBounds(450,325,80,25);
		frame.add(bDying);
		frame.add(labelUsePeach);
	}
	void execute() {//p1专用，用于开始第一个判定阶段
		judgeStage.execute();
	}
	void getEnemy(Player enemy) {//找到对手
		this.enemy = enemy;
		pool.submit(new BloodThread());
		pool.submit(new JudgeThread());
	}
	
	void takeCard() {//摸牌
		/*
		 * 摸牌过程中不仅需要更新Button数组，
		 * 还要对Button的栈进行压栈操作并把对应String，int存入哈希表
		 * */
		Card[] tempCard = new Card[cardNumber + 1];
		for(int i = 0;i < cardNumber;i ++) {
			tempCard[i] = handCard[i];
		}
		
		Card temp = cardHeap.takeCard();
		tempCard[cardNumber] = temp;
		handCard = tempCard;
		String str = temp.name+" "+temp.flowerColor+Integer.toString(temp.point);
		if(str.contains("无懈可击")) {
			WXKJNumber ++;
		}
		hashMap.put(str, cardNumber);
		Button b = new Button(str);
		new ButtonListener(this,b).addListener();
		b.setFont(new Font("Helvetica",Font.PLAIN,10));
		b.setBounds(myPlaceX,400,80,150);
		addButton(b);
		frame.add(b);
		
		Button Enemyb = new Button();
		enemy.buttonStack.add(Enemyb);
		enemy.frame.add(Enemyb);
		Enemyb.setBounds(enemy.enemyX, 0, 80, 150);
		
		myPlaceX += 80;
		enemy.enemyX -= 80;
		cardNumber = cardNumber + 1;
	}
	Card desertCard(int p) {//弃置一张手牌
		/*
		 * 弃置手牌的同时需要更新哈希表，
		 * 并将frame中的Button重新排序
		 * */
		Card[] temp = new Card[cardNumber - 1];
		int num = 0;
		for(int i = 0;i < cardNumber;i ++) {
			if(p != i)
				temp[num++] = handCard[i];
		}
		
		Card card = handCard[p];		
		handCard = temp;
		HashMap<String,Integer> hm = new HashMap<>();
		for(int i = 0;i < cardNumber - 1;i ++) {
			String str = handCard[i].name+" "+handCard[i].flowerColor+Integer.toString(handCard[i].point);
			hm.put(str, i);
		}
		hashMap = hm;
		
		enemy.frame.remove(enemy.buttonStack.pop());
		enemy.enemyX += 80;
		
		
		for(int i = 0;i < cardNumber;i ++) {
			frame.remove(buttonArray[i]);
		}
		
		deleteButton(p);
		cardNumber = cardNumber - 1;
		
		myPlaceX = 0;
		for(int i = 0;i < cardNumber;i ++) {
			buttonArray[i].setBounds(myPlaceX,400,80,150);
			myPlaceX += 80;
			frame.add(buttonArray[i]);
			//buttonArray.
		}
		return card;
	}
	
	void addButton(Button b) {//用于button数组的修改（增加操作）
		Button[] temp = new Button[cardNumber +  1];
		for(int i = 0;i < cardNumber;i ++) {
			temp[i] = buttonArray[i];
		}
		temp[cardNumber] = b;
		buttonArray = temp;
	}
    void deleteButton(int p) {//用于button数组的修改（删除操作）
    	Button b = buttonArray[p];
    	Button[] temp = new Button[cardNumber - 1];
    	int num = 0;
		for(int i = 0;i < cardNumber - 1;i ++) {
			Card c = handCard[i];
			String str = c.name + " "+ c.flowerColor+ Integer.toString(c.point);
			temp[num] = new Button(str); 
			temp[num].setFont(new Font("Helvetica",Font.PLAIN,10));
			new ButtonListener(this,temp[num++]).addListener();
		}
		buttonArray = temp;
	}
    
    boolean UseMiss(){
		if(armor != null) {//打出闪或使用闪
			if(armor.name.equals("八卦阵")) {
				if(enemy.weapon==null||!enemy.weapon.name.equals("青钢剑")) {
					Card c = cardHeap.judge();
					String str = c.name+" "+c.flowerColor+Integer.toString(c.point);
					System.out.println("判定结果是: "+ str);
					if(c.flowerColor.equals("红桃")||c.flowerColor.equals("方片")) {
						return true;
					}
				}
			}		
		}
		check = false;
		isMiss = true;
		labelUseMiss = new JLabel("请出闪!");
		labelUseMiss.setBounds(450, 200, 100, 50);
		bMiss.setBounds(450, 250, 50, 50);
		frame.add(bMiss);
		frame.add(labelUseMiss);
		return check;
	}
    
    boolean UseKill() {//打出杀
		check = false;
		isKill = true;
		labelUseKill = new JLabel("请出杀!");
		labelUseKill.setBounds(450, 200, 100, 50);
		bKill.setBounds(450, 250, 50, 50);
		frame.add(bKill);
		frame.add(labelUseKill);
		return check;
	}

	void desertWeapon() {//弃置武器的方法
		this.arm[0].setVisible(false);
		this.arm[0] = new JLabel("武器区");
		frame.add(arm[0]);
		arm[0].setBounds(900,350,80,25);
		arm[0].setFont(new Font("Helvetica",Font.PLAIN,10));
		this.arm[0].setVisible(true);
		if(weapon.name.equals("丈八蛇矛")) {
			frame.remove(bZBSM);
		}
		this.weapon = null;
		
		enemy.enemyArm[0].setVisible(false);
		enemy.enemyArm[0] = new JLabel("武器区");
		enemy.frame.add(enemy.enemyArm[0]);
		enemy.enemyArm[0].setBounds(0,150,80,25);
		enemy.enemyArm[0].setFont(new Font("Helvetica",Font.PLAIN,10));
		enemy.enemyArm[0].setVisible(true);
		

	}
	void desertArmor() {//弃置防具的方法
		this.arm[1].setVisible(false);
		this.arm[1] = new JLabel("防具区");
		frame.add(arm[1]);
		arm[1].setBounds(900,375,80,25);
		arm[1].setFont(new Font("Helvetica",Font.PLAIN,10));
		this.arm[1].setVisible(true);
		this.armor = null;
		
		enemy.enemyArm[1].setVisible(false);
		enemy.enemyArm[1] = new JLabel("防具区");
		enemy.frame.add(enemy.enemyArm[1]);
		enemy.enemyArm[1].setBounds(0,175,80,25);
		enemy.enemyArm[1].setFont(new Font("Helvetica",Font.PLAIN,10));
		enemy.enemyArm[1].setVisible(true);
		

	}
	
	class BloodThread extends Thread{//显示生命值的线程
		BloodThread(){this.setDaemon(true);}
		public void run() {
			while(true) {
				bloodShow.setVisible(false);
				upperShow.setVisible(false);
				bloodShow.setText("当前生命值:" + Integer.toString(blood));
				upperShow.setText("生命上限:" + Integer.toString(upperLimitBlood));
				
				EbloodShow.setVisible(false);
				EupperShow.setVisible(false);
				EbloodShow.setText("当前生命值:" + Integer.toString(enemy.blood));
				EupperShow.setText("生命上限:" + Integer.toString(enemy.upperLimitBlood));
				
				bloodShow.setBounds(900,300,100,25);
				upperShow.setBounds(900,325,100,25);
				EbloodShow.setBounds(0,200,100,25);
				EupperShow.setBounds(0,225,100,25);
				
				bloodShow.setVisible(true);
				upperShow.setVisible(true);
				EbloodShow.setVisible(true);
				EupperShow.setVisible(true);
				
				try {
					sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	class JudgeThread extends Thread{//显示判定区的线程
		JudgeThread(){this.setDaemon(true);}
		public void run() {
			while(true) {
				LBSSShow.setVisible(false);
				BLCDShow.setVisible(false);
				ELBSSShow.setVisible(false);
				EBLCDShow.setVisible(false);
				String str1,str2,estr1,estr2;
				if(judge[0] == null)
					str1 = "乐不思蜀判定区为空";
				else {
					str1 = judge[0].name+" "+ judge[0].flowerColor + Integer.toString(judge[0].point);   
				}
				if(judge[1] == null)
					str2 = "兵粮寸断判定区为空";
				else {
					str2 = judge[0].name+" "+ judge[0].flowerColor + Integer.toString(judge[0].point);   
				}
				if(enemy.judge[0] == null)
					estr1 = "乐不思蜀判定区为空";
				else {
					estr1 = enemy.judge[0].name+" "+ enemy.judge[0].flowerColor + Integer.toString(enemy.judge[0].point);   
				}
				if(enemy.judge[1] == null)
					estr2 = "兵粮寸断判定区为空";
				else {
					estr2 = enemy.judge[0].name+" "+ enemy.judge[0].flowerColor + Integer.toString(enemy.judge[0].point);   
				}	
				LBSSShow.setText(str1);
				BLCDShow.setText(str2);
				
				LBSSShow.setBounds(0,350,200,25);
				BLCDShow.setBounds(0,375,200,25);
				
				LBSSShow.setVisible(true);
				BLCDShow.setVisible(true);
				
				
				ELBSSShow.setText(estr1);
				EBLCDShow.setText(estr2);
				
				ELBSSShow.setBounds(800,150,200,25);
				EBLCDShow.setBounds(800,175,200,25);
				
				ELBSSShow.setVisible(true);
				EBLCDShow.setVisible(true);
				try {
					sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	class DyingCancel implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			System.out.println(name + "阵亡，"+ enemy.name + "获胜");
			frame.dispose();
			enemy.frame.dispose();
		}
		
	}
	
	
}


abstract class Stage{
	Player player;boolean isPass;CardHeap cardHeap;
	Stage(Player player,CardHeap cardHeap,Boolean b){
		this.player = player;
		this.cardHeap = cardHeap;
		isPass = b;
	}
}
class JudgeStage extends Stage{
	Button LBSSCancel = new Button("取消");
	Button BLCDCancel = new Button("取消");
	JudgeStage(Player player,CardHeap cardHeap,Boolean b){
		super(player,cardHeap,b);
	}
	void execute() {
		System.out.println("判定阶段");
		System.out.println();
		if(isPass||(player.judge[0] == null && player.judge[1] == null)) {
			System.out.println("跳过判定阶段");
			player.takeCardStage = new TakeCardStage(player,cardHeap,false);
			player.outCardStage = new OutCardStage(player,cardHeap,false);
			player.takeCardStage.execute();
			return;
		}
		
		else {
			if(player.judge[0] != null){//判定区有乐不思蜀
				if(player.WXKJNumber > 0) {
					player.isLBSS = true;
					player.isWXKJ = true;
					player.labelUseWXKJ = new JLabel("请打出一张无懈可击");
					player.frame.add(player.labelUseWXKJ);
					player.frame.add(player.bWXKJ);
					player.labelUseWXKJ.setBounds(400, 200, 200, 50);
					player.bWXKJ.setBounds(400, 250, 50, 50);
					
					player.bWXKJ.addActionListener(new CancelListener(true));
				}
				else {
					LBSS();
					player.takeCardStage.execute();
				}
			}
			else{
				player.outCardStage = new OutCardStage(player,cardHeap,false); 
			}
			if(player.judge[1] != null) {//判定区有兵粮寸断
				if(player.WXKJNumber > 0) {
					player.isBLCD = true;
					player.isWXKJ = true;
					player.labelUseWXKJ = new JLabel("请打出一张无懈可击");
					player.frame.add(player.labelUseWXKJ);
					player.frame.add(player.bWXKJ);
					player.labelUseWXKJ.setBounds(400, 200, 200, 50);
					player.bWXKJ.setBounds(400, 250, 50, 50);
					
					player.bWXKJ.addActionListener(new CancelListener(false));
				}
				else {
					BLCD();
					player.takeCardStage.execute();
				}
					
			}
			else
				player.takeCardStage = new TakeCardStage(player,cardHeap,false);
		}
	}
	void LBSS() {		
		if(!cardHeap.judge().flowerColor.equals("红桃")) {
			player.outCardStage = new OutCardStage(player,cardHeap,true);
			System.out.println(player.name+"跳过出牌阶段");
		}
		else
			player.outCardStage = new OutCardStage(player,cardHeap,false);
		player.cardHeap.desertHeap.insertHeap(player.judge[0]);
		player.judge[0] = null;
		
	}
	void BLCD() {
		if(!cardHeap.judge().flowerColor.equals("梅花")) {
			player.takeCardStage = new TakeCardStage(player,cardHeap,true);
			System.out.println(player.name+"跳过摸牌阶段");
		}
		else
			player.takeCardStage = new TakeCardStage(player,cardHeap,false);
		player.cardHeap.desertHeap.insertHeap(player.judge[1]);
		player.judge[1] = null;
	}
	class CancelListener implements ActionListener{
		boolean bool;
		CancelListener(boolean bool){this.bool = bool;}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(bool){//true为乐不思蜀，false为兵粮寸断
				player.frame.remove(player.bWXKJ);
				player.labelUseWXKJ.setVisible(false);
				player.bWXKJ.removeActionListener(this);
				LBSS();
				player.isWXKJ = false;
				
			}
			else {
				player.frame.remove(player.bWXKJ);
				player.labelUseWXKJ.setVisible(false);
				player.bWXKJ.removeActionListener(this);
				BLCD();
				player.isWXKJ = false;
				
			}
		}
	}
}
class TakeCardStage extends Stage{
	TakeCardStage(Player player,CardHeap cardHeap,Boolean b){
		super(player,cardHeap,b);
	}
	void execute() {
		if(isPass) {
			System.out.println(player.name + "跳过摸牌阶段");
		}
		else{
			System.out.println("摸牌阶段");
			for(int i = 0;i < 2;i ++)
				player.takeCard();
			if(player.name.equals("玩家二"))
			player.takeCard();
		}
		player.outCardStage.execute();
	}
}
class OutCardStage extends Stage{
	OutCardStage(Player player,CardHeap cardHeap,Boolean b){
		super(player,cardHeap,b);
	}
	public void execute() {
		if(isPass) {
			System.out.println(player.name + "跳过出牌阶段");
			player.desertCardStage.execute();
			return;
		}
		Button finishOutCard = new Button("回合结束");
		finishOutCard.addActionListener(new FinishOutCardListener(player,finishOutCard));
		finishOutCard.setBounds(800,375,90,25);
		player.frame.add(finishOutCard);
		player.isMyturn = true;
	}
}
class DesertCardStage extends Stage{
	DesertCardStage(Player player,CardHeap cardHeap,Boolean b){
		super(player,cardHeap,b);
	}
	void execute() {
		player.useKillNumber = 0;
		player.desertNumber =  player.cardNumber - player.blood;
		
		if(player.desertNumber <= 0) {
			player.desertNumber = 0;
			player.enemy.execute();
			return;
		}
		player.frame.add(player.labelDesert);
		
		player.labelDesert.setBounds(400,250,100,25);
		player.labelDesert.setVisible(true);
		
		player.isMyturn = false;
		player.isDesert = true;
	}
}


class ButtonEnter implements ActionListener{
	JFrame j;Socket socket;CardHeap cardHeap;
	ButtonEnter(JFrame j,Socket socket,CardHeap cardHeap){
		this.j = j;
		this.socket = socket;
		this.cardHeap = cardHeap;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("成功载入！");
		j.setVisible(false);
		Player p1;
		try {
			p1 = new Player(cardHeap,1,"玩家一");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		Player p2;
		try {
			p2 = new Player(cardHeap,2,"玩家二");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		p1.getEnemy(p2);
		p2.getEnemy(p1);
		p1.execute();
	}
	
}
public class game {
	public static void main(String[] args) throws SQLException, IOException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("载入成功");
		} catch (ClassNotFoundException e) {
			System.out.println("载入失败");
			e.printStackTrace();
		}
		String url = "jdbc:mysql://127.0.0.1:3306/threecountieskill";
		String user = "root";
		String password = "Royalwwl9801";
		Connection connection;
		Statement statement;
		try {
			connection = DriverManager.getConnection(url, user, password);
			statement = connection.createStatement();
			statement.execute("SET SQL_SAFE_UPDATES = 0;");
			statement.execute("delete from card_heap where true;");
			statement.execute("delete from desert_card where true;");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		CardHeap cardHeap = new CardHeap(connection, statement);
		cardHeap.main(args);
		/*
		 * 连接数据库
		 * */
		
		ServerSocket serverSocket = new ServerSocket(2572);
		JFrame jFrame = new JFrame();
		Container contentPane= jFrame.getContentPane();
		jFrame.setLayout(new BorderLayout());
		Button b = new Button("开始游戏！");
		b.addActionListener(new ButtonEnter(jFrame,new Socket("localhost",2572),cardHeap));
		jFrame.add("Center",b);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
		jFrame.setSize(200, 200);	
		serverSocket.close();
	}
}
