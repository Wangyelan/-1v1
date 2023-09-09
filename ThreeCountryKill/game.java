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
	Card[] judge;//judge[0]�ֲ�˼��,judge[1]������ϣ�����1v1�ƶ�û�����磬���Բ����������ж���
	Card[] handCard;
	CardHeap cardHeap;

	Player enemy;
	
	ExecutorService pool = Executors.newCachedThreadPool();//�̳߳�
	
	JTextField bloodShow = new JTextField();
	JTextField upperShow = new JTextField();
	JTextField EbloodShow = new JTextField();
	JTextField EupperShow = new JTextField();
	//��ʾ����ֵ���߳����ñ�ǩ
	
	JTextField LBSSShow = new JTextField();
	JTextField BLCDShow = new JTextField();
	JTextField ELBSSShow = new JTextField();
	JTextField EBLCDShow = new JTextField();
	//��ʾ�ж������߳����õı�ǩ
	
	boolean check = true;//�����ж���û�г������߳�ɱ
	//cardHeap.attributeAdded(ServletContextAttributeEvent scae);
	
	HashMap<String,Integer> hashMap = new HashMap<>();
	int blood;
	int upperLimitBlood;//��������
	int upperLimitKill;//�غ�����ɱ��
	int useKillNumber;//�غϳ�ɱ��
	int cardNumber;//������
	int place;
	int myPlaceX;//��¼��һ���ҷ�������JFrame�е�λ��
	int enemyX;//��¼��һ�ŵз�������JFrame�е�λ��
	int WXKJNumber = 0;//��и�ɻ�����
	
	boolean isBLCD = false;
	boolean isLBSS = false;
	boolean isMyturn = false;//�����ж��Ƿ����Լ��Ļغ�
	boolean isDying = false;//�����ж��Ƿ��ڱ����׶�
	boolean isMiss = false;//�����ж��Ƿ��ܳ���
	boolean isKill = false;//�����ж��Ƿ�����Ӧɱ���������֡�������ɱ��
	boolean isDuel = false;;//�����ж��Ƿ��ھ�������׶�
	boolean isZBSM = false;//�����ж��Ƿ����ɰ���ìѡ�ƽ׶�
	boolean isGSF = false;//�����ж��Ƿ��ڹ�ʯ������׶�
	boolean isHBJ = false;//�����ж��Ƿ��ں���������׶�
	boolean isDesert = false;//�����ж��Ƿ������ƽ׶�
	boolean isWXKJ = false;
	
	
	int ZBSMNumber = 0;//���ڼ����Ƿ����ɰ���ì��ѡ����
	int GSFNumber = 0;//���ڼ����Ƿ��ڹ�ʯ����ѡ����
	int desertNumber = 0;//���ڼ����Ƿ������ƽ׶���ѡ����
	
	Stack<Button> buttonStack = new Stack<Button>();//���˵�����
	Button[] buttonArray = new Button[0];//�Լ�������
	Button bKill = new Button("ȡ��");//ɱ��ȡ����ť
	Button bMiss = new Button("ȡ��");//����ȡ����ť
	Button bDying = new Button("ȡ��");//����״̬�ĵ�ȡ����ť
	Button bWXKJ = new Button("ȡ��");//��и�ɻ���ȡ����ť
	Button bZBSM = new Button("�ɰ���ì");
	Button bHBJSure = new Button("ȷ��");
	Button bHBJCancel= new Button("ȡ��");
	Button bGSFSure = new Button("ȷ��");
	Button bGSFCancel = new Button("ȡ��");
	
	JLabel[] arm = new JLabel[2];//0��������1�Ƿ���
	JLabel[] enemyArm = new JLabel[2];
	JLabel labelDesert = new JLabel("���ƽ׶Σ�������");
	JLabel labelUseKill = new JLabel("���ɱ");
	JLabel labelUseMiss = new JLabel("�����");
	JLabel labelUsePeach = new JLabel("���Ѵ��ڱ���״̬����ʹ��һ����");
	JLabel labelUseWXKJ = new JLabel("����һ����и�ɻ�");
	JLabel labelHBJ = new JLabel("�Ƿ񷢶�������");
	JLabel labelGSF = new JLabel("�Ƿ񷢶���ʯ��");	
	
	JFrame frame;
	Container contentPane;
	String name;
	
	JudgeStage judgeStage;
	TakeCardStage takeCardStage;
	OutCardStage outCardStage;
	DesertCardStage desertCardStage;
	/*
	 * ���ĸ��������ж��׶Ρ����ƽ׶Ρ����ƽ׶Ρ����ƽ׶�
	 * */
	
	Player(CardHeap cardHeap,int p,String name) throws IOException{
		blood = name.equals("���һ")?5:4;
		upperLimitBlood = name.equals("���һ")?5:4;
		
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
		if(name.equals("���һ"))
			img1= ImageIO.read(getClass().getResource("�ϱ���ı.jpg"));
		else
			img1 = ImageIO.read(getClass().getResource("��ͳ.jpg"));

		img1.getScaledInstance(80, 150, Image.SCALE_SMOOTH);
		
		JButton jName = new JButton(name);
		
		jName.setIcon(new ImageIcon(img1));
		
		
		contentPane.add(jName);
		jName.setBounds(900, 400, 80, 150);
		arm[0] = new JLabel("������");
		arm[0].setBounds(900,350,80,25);
		arm[1] = new JLabel("������");
		arm[1].setBounds(900,375,80,25);
		frame.add(arm[0]);
		arm[0].setFont(new Font("Helvetica",Font.PLAIN,10));
		arm[1].setFont(new Font("Helvetica",Font.PLAIN,10));
		frame.add(arm[1]);
		bMiss.addActionListener(new CancelMissListener(this));
		bKill.addActionListener(new CancelKillListener(this));
		
		enemyArm[0] = new JLabel("������");
		enemyArm[0].setBounds(0,150,80,25);
		enemyArm[1] = new JLabel("������");
		enemyArm[1].setBounds(0,175,80,25);
		frame.add(enemyArm[0]);
		enemyArm[0].setFont(new Font("Helvetica",Font.PLAIN,10));
		enemyArm[1].setFont(new Font("Helvetica",Font.PLAIN,10));
		frame.add(enemyArm[1]);
		
		JButton jNameEnemy = new JButton((name.equals("���һ")?"��Ҷ�":"���һ"));
		Image img2;
		if(name.equals("���һ"))
			img2 = ImageIO.read(getClass().getResource("��ͳ.jpg"));
		else
			img2 = ImageIO.read(getClass().getResource("�ϱ���ı.jpg"));
		img2.getScaledInstance(80, 150, Image.SCALE_SMOOTH);
		jNameEnemy.setIcon(new ImageIcon(img2));
		
		jName.setIcon(new ImageIcon(img1));
		contentPane.add(jNameEnemy);
		jNameEnemy.setBounds(0, 0, 80, 150);
		enemyX = 900;
		
		
		labelUseKill.setBounds(450, 200, 200, 50);
		labelUseMiss.setBounds(450, 200, 200, 50);
		labelUseWXKJ.setBounds(450, 200, 200, 50);
		//����������ʾ��ǩ��λ��
		
		
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
				
		bloodShow.setText("��ǰ����ֵ:" + Integer.toString(blood));
		upperShow.setText("��������:" + Integer.toString(upperLimitBlood));
		EbloodShow.setText("��ǰ����ֵ:" + Integer.toString(blood));
		EupperShow.setText("��������:" + Integer.toString(upperLimitBlood));
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
			if(str.contains("��и�ɻ�"))
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
		 * ��ʼ��ʱ��ÿ����ҷ���4�����ƣ����ѭ�������Ʋ���
		 * ����Ϊʲôû���ú�����д��TakeCard������
		 * ��Ϊ��ʼ����ʱ��enemyΪnull
		 * */
	}
	void getInjure() {//�ܵ�һ���˺�
		System.out.println(name + "�ܵ�һ���˺�");
		blood --;
		if(blood == 0) 
			Dying();
	}
	void Dying() {//����״̬
		isDying = true;
		labelUsePeach.setVisible(true);
		labelUsePeach.setBounds(400,225,200,25);
		bDying.setBounds(450,325,80,25);
		frame.add(bDying);
		frame.add(labelUsePeach);
	}
	void execute() {//p1ר�ã����ڿ�ʼ��һ���ж��׶�
		judgeStage.execute();
	}
	void getEnemy(Player enemy) {//�ҵ�����
		this.enemy = enemy;
		pool.submit(new BloodThread());
		pool.submit(new JudgeThread());
	}
	
	void takeCard() {//����
		/*
		 * ���ƹ����в�����Ҫ����Button���飬
		 * ��Ҫ��Button��ջ����ѹջ�������Ѷ�ӦString��int�����ϣ��
		 * */
		Card[] tempCard = new Card[cardNumber + 1];
		for(int i = 0;i < cardNumber;i ++) {
			tempCard[i] = handCard[i];
		}
		
		Card temp = cardHeap.takeCard();
		tempCard[cardNumber] = temp;
		handCard = tempCard;
		String str = temp.name+" "+temp.flowerColor+Integer.toString(temp.point);
		if(str.contains("��и�ɻ�")) {
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
	Card desertCard(int p) {//����һ������
		/*
		 * �������Ƶ�ͬʱ��Ҫ���¹�ϣ��
		 * ����frame�е�Button��������
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
	
	void addButton(Button b) {//����button������޸ģ����Ӳ�����
		Button[] temp = new Button[cardNumber +  1];
		for(int i = 0;i < cardNumber;i ++) {
			temp[i] = buttonArray[i];
		}
		temp[cardNumber] = b;
		buttonArray = temp;
	}
    void deleteButton(int p) {//����button������޸ģ�ɾ��������
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
		if(armor != null) {//�������ʹ����
			if(armor.name.equals("������")) {
				if(enemy.weapon==null||!enemy.weapon.name.equals("��ֽ�")) {
					Card c = cardHeap.judge();
					String str = c.name+" "+c.flowerColor+Integer.toString(c.point);
					System.out.println("�ж������: "+ str);
					if(c.flowerColor.equals("����")||c.flowerColor.equals("��Ƭ")) {
						return true;
					}
				}
			}		
		}
		check = false;
		isMiss = true;
		labelUseMiss = new JLabel("�����!");
		labelUseMiss.setBounds(450, 200, 100, 50);
		bMiss.setBounds(450, 250, 50, 50);
		frame.add(bMiss);
		frame.add(labelUseMiss);
		return check;
	}
    
    boolean UseKill() {//���ɱ
		check = false;
		isKill = true;
		labelUseKill = new JLabel("���ɱ!");
		labelUseKill.setBounds(450, 200, 100, 50);
		bKill.setBounds(450, 250, 50, 50);
		frame.add(bKill);
		frame.add(labelUseKill);
		return check;
	}

	void desertWeapon() {//���������ķ���
		this.arm[0].setVisible(false);
		this.arm[0] = new JLabel("������");
		frame.add(arm[0]);
		arm[0].setBounds(900,350,80,25);
		arm[0].setFont(new Font("Helvetica",Font.PLAIN,10));
		this.arm[0].setVisible(true);
		if(weapon.name.equals("�ɰ���ì")) {
			frame.remove(bZBSM);
		}
		this.weapon = null;
		
		enemy.enemyArm[0].setVisible(false);
		enemy.enemyArm[0] = new JLabel("������");
		enemy.frame.add(enemy.enemyArm[0]);
		enemy.enemyArm[0].setBounds(0,150,80,25);
		enemy.enemyArm[0].setFont(new Font("Helvetica",Font.PLAIN,10));
		enemy.enemyArm[0].setVisible(true);
		

	}
	void desertArmor() {//���÷��ߵķ���
		this.arm[1].setVisible(false);
		this.arm[1] = new JLabel("������");
		frame.add(arm[1]);
		arm[1].setBounds(900,375,80,25);
		arm[1].setFont(new Font("Helvetica",Font.PLAIN,10));
		this.arm[1].setVisible(true);
		this.armor = null;
		
		enemy.enemyArm[1].setVisible(false);
		enemy.enemyArm[1] = new JLabel("������");
		enemy.frame.add(enemy.enemyArm[1]);
		enemy.enemyArm[1].setBounds(0,175,80,25);
		enemy.enemyArm[1].setFont(new Font("Helvetica",Font.PLAIN,10));
		enemy.enemyArm[1].setVisible(true);
		

	}
	
	class BloodThread extends Thread{//��ʾ����ֵ���߳�
		BloodThread(){this.setDaemon(true);}
		public void run() {
			while(true) {
				bloodShow.setVisible(false);
				upperShow.setVisible(false);
				bloodShow.setText("��ǰ����ֵ:" + Integer.toString(blood));
				upperShow.setText("��������:" + Integer.toString(upperLimitBlood));
				
				EbloodShow.setVisible(false);
				EupperShow.setVisible(false);
				EbloodShow.setText("��ǰ����ֵ:" + Integer.toString(enemy.blood));
				EupperShow.setText("��������:" + Integer.toString(enemy.upperLimitBlood));
				
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
	class JudgeThread extends Thread{//��ʾ�ж������߳�
		JudgeThread(){this.setDaemon(true);}
		public void run() {
			while(true) {
				LBSSShow.setVisible(false);
				BLCDShow.setVisible(false);
				ELBSSShow.setVisible(false);
				EBLCDShow.setVisible(false);
				String str1,str2,estr1,estr2;
				if(judge[0] == null)
					str1 = "�ֲ�˼���ж���Ϊ��";
				else {
					str1 = judge[0].name+" "+ judge[0].flowerColor + Integer.toString(judge[0].point);   
				}
				if(judge[1] == null)
					str2 = "��������ж���Ϊ��";
				else {
					str2 = judge[0].name+" "+ judge[0].flowerColor + Integer.toString(judge[0].point);   
				}
				if(enemy.judge[0] == null)
					estr1 = "�ֲ�˼���ж���Ϊ��";
				else {
					estr1 = enemy.judge[0].name+" "+ enemy.judge[0].flowerColor + Integer.toString(enemy.judge[0].point);   
				}
				if(enemy.judge[1] == null)
					estr2 = "��������ж���Ϊ��";
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
			System.out.println(name + "������"+ enemy.name + "��ʤ");
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
	Button LBSSCancel = new Button("ȡ��");
	Button BLCDCancel = new Button("ȡ��");
	JudgeStage(Player player,CardHeap cardHeap,Boolean b){
		super(player,cardHeap,b);
	}
	void execute() {
		System.out.println("�ж��׶�");
		System.out.println();
		if(isPass||(player.judge[0] == null && player.judge[1] == null)) {
			System.out.println("�����ж��׶�");
			player.takeCardStage = new TakeCardStage(player,cardHeap,false);
			player.outCardStage = new OutCardStage(player,cardHeap,false);
			player.takeCardStage.execute();
			return;
		}
		
		else {
			if(player.judge[0] != null){//�ж������ֲ�˼��
				if(player.WXKJNumber > 0) {
					player.isLBSS = true;
					player.isWXKJ = true;
					player.labelUseWXKJ = new JLabel("����һ����и�ɻ�");
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
			if(player.judge[1] != null) {//�ж����б������
				if(player.WXKJNumber > 0) {
					player.isBLCD = true;
					player.isWXKJ = true;
					player.labelUseWXKJ = new JLabel("����һ����и�ɻ�");
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
		if(!cardHeap.judge().flowerColor.equals("����")) {
			player.outCardStage = new OutCardStage(player,cardHeap,true);
			System.out.println(player.name+"�������ƽ׶�");
		}
		else
			player.outCardStage = new OutCardStage(player,cardHeap,false);
		player.cardHeap.desertHeap.insertHeap(player.judge[0]);
		player.judge[0] = null;
		
	}
	void BLCD() {
		if(!cardHeap.judge().flowerColor.equals("÷��")) {
			player.takeCardStage = new TakeCardStage(player,cardHeap,true);
			System.out.println(player.name+"�������ƽ׶�");
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
			if(bool){//trueΪ�ֲ�˼��falseΪ�������
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
			System.out.println(player.name + "�������ƽ׶�");
		}
		else{
			System.out.println("���ƽ׶�");
			for(int i = 0;i < 2;i ++)
				player.takeCard();
			if(player.name.equals("��Ҷ�"))
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
			System.out.println(player.name + "�������ƽ׶�");
			player.desertCardStage.execute();
			return;
		}
		Button finishOutCard = new Button("�غϽ���");
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
		System.out.println("�ɹ����룡");
		j.setVisible(false);
		Player p1;
		try {
			p1 = new Player(cardHeap,1,"���һ");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		Player p2;
		try {
			p2 = new Player(cardHeap,2,"��Ҷ�");
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
			System.out.println("����ɹ�");
		} catch (ClassNotFoundException e) {
			System.out.println("����ʧ��");
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
		 * �������ݿ�
		 * */
		
		ServerSocket serverSocket = new ServerSocket(2572);
		JFrame jFrame = new JFrame();
		Container contentPane= jFrame.getContentPane();
		jFrame.setLayout(new BorderLayout());
		Button b = new Button("��ʼ��Ϸ��");
		b.addActionListener(new ButtonEnter(jFrame,new Socket("localhost",2572),cardHeap));
		jFrame.add("Center",b);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
		jFrame.setSize(200, 200);	
		serverSocket.close();
	}
}
