package ThreeCountryKill;

import java.awt.AWTEvent;
import java.awt.Button;
import java.awt.Container;
import java.awt.Event;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.net.ServerSocket;
import java.util.EventListener;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.mysql.cj.x.protobuf.MysqlxNotice.Frame;


class ButtonListener{//���ڸ����õ���������Ӷ�Ӧ�ļ���������֤��������ʹ��
	Player p;
	Button b;
	ButtonListener(Player p,Button b){
		this.p = p;
		this.b = b;
	}
	void addListener() {
		String str = b.getActionCommand();
		if(str.contains("����")) {b.addActionListener(new DuelListener(p,b));}
		else if(str.contains("����뷢")) {b.addActionListener(new WJQFListener(p,b));}
		else if(str.contains("�������")) {b.addActionListener(new ArmWeaponListener(p,b));}
		else if(str.contains("������")) {b.addActionListener(new ArmArmorListener(p,b));}
		else if(str.contains("ɱ")) {
			b.addActionListener(new KillListener(p,b));
			b.addActionListener(new UseKillListener(p,b));
		}
		else if(str.contains("������")) {b.addActionListener(new ArmArmorListener(p,b));}
		else if(str.contains("��")) {b.addActionListener(new MissListener(p,b));}
		else if(str.contains("���Ӳ���")) {b.addActionListener(new GHCQListener(p,b));}
		else if(str.contains("˳��ǣ��")) {b.addActionListener(new SSQYListener(p,b));}
		else if(str.contains("��ʯ��")) {b.addActionListener(new ArmWeaponListener(p,b));}
		else if(str.contains("��ֽ�")) {b.addActionListener(new ArmWeaponListener(p,b));}
		else if(str.contains("�ֲ�˼��")) {b.addActionListener(new LBSSListener(p,b));}
		else if(str.contains("ˮ���߾�")) {b.addActionListener(new SYQJListener(p,b));}
		else if(str.contains("��������")) {b.addActionListener(new WZSYListener(p,b));}
		else if(str.contains("������")) {b.addActionListener(new ArmWeaponListener(p,b));}
		else if(str.contains("�ɰ���ì")) {b.addActionListener(new ArmWeaponListener(p,b));}
		else if(str.contains("�������")) {b.addActionListener(new BLCDListener(p,b));}
		else if(str.contains("��������")) {b.addActionListener(new NMRQListener(p,b));}
		else if(str.contains("��и�ɻ�")) {b.addActionListener(new WXKJListener(p,b));}
		else if(str.contains("��"))
		{b.addActionListener(new PeachListener(p,b));}
	}
}


class DuelListener implements ActionListener{//����
	Player p;
	Button b;
	DuelListener(Player p,Button b){
		this.p = p;
		this.b = b;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		/*
		 * �����ж��Ƿ������ƽ���
		 * Ȼ���ж��Ƿ����Լ��غ�
		 * �������ж��Ƿ����ɰ���ì�͹�ʯ����ѡ�ƽ׶�
		 * */
		if(p.isDesert) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.desertNumber --;
			if(p.desertNumber == 0) {
				p.isDesert = false;
				p.labelDesert.setVisible(false);
				p.enemy.judgeStage.execute();
			}
		}
		if(!p.isMyturn) {
			return;
		}
		if(p.isZBSM) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.ZBSMNumber ++;
			if(p.ZBSMNumber == 2) {
				p.ZBSMNumber = 0;
				p.enemy.UseMiss();
				p.isZBSM = false;
			}
			return;
		}
		if(p.isGSF) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.GSFNumber ++;
			if(p.ZBSMNumber == 2) {
				p.GSFNumber = 0;
				p.enemy.blood --;
				p.isGSF = false;
			}
			return;
		}
		System.out.println(p.name + "ʹ��"+e.getActionCommand());
		p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
		if(p.enemy.WXKJNumber == 0) {
			execute();
			return;
		}
		//��и�ɻ�����
		/*
		 * ��ǰ���ж��Ƿ����ɰ���ì�͹�ʯ��ѡ�ƽ׶�������
		 * ����������ж���и�ɻ���������и�ɻ�����ģ�ÿ�������ƶ�����������̣���и�ɻ����⣩
		 * */
		p.enemy.isWXKJ = true;
		p.enemy.labelUseWXKJ = new JLabel("����һ����и�ɻ�");
		p.enemy.frame.add(p.enemy.labelUseWXKJ);
		p.enemy.frame.add(p.enemy.bWXKJ);
		p.enemy.labelUseWXKJ.setBounds(400, 200, 200, 50);
		p.enemy.bWXKJ.setBounds(400, 250, 50, 50);
		
		p.enemy.bWXKJ.addActionListener(new CancelListener());
	}
	private class CancelListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			p.enemy.frame.remove(p.enemy.bWXKJ);
			p.enemy.labelUseWXKJ.setVisible(false);
			p.enemy.bWXKJ.removeActionListener(this);
			execute();
			p.enemy.isWXKJ = false;
		}
	}
	void execute() {
		p.enemy.UseKill();
		p.isDuel = true;
		p.enemy.isDuel = true;
		p.isMyturn = true;
	}
}


class WJQFListener implements ActionListener{//����뷢
	Player p;
	Button b;
	WJQFListener(Player p,Button b){
		this.p = p;
		this.b = b;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(p.isDesert) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.desertNumber --;
			if(p.desertNumber == 0) {
				p.isDesert = false;
				p.labelDesert.setVisible(false);
				p.enemy.judgeStage.execute();
			}
		}
		if(!p.isMyturn) {
			return;
		}
		if(p.isZBSM) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.ZBSMNumber ++;
			if(p.ZBSMNumber == 2) {
				p.ZBSMNumber = 0;
				p.enemy.UseMiss();
				p.isZBSM = false;
			}
			return;
		}
		if(p.isGSF) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.GSFNumber ++;
			if(p.ZBSMNumber == 2) {
				p.GSFNumber = 0;
				p.enemy.blood --;
				p.isGSF = false;
			}
			return;
		}
		System.out.println(p.name + "ʹ��"+e.getActionCommand());
		p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
		if(p.enemy.WXKJNumber == 0) {
			p.enemy.UseMiss();
			return;
		}
		p.enemy.isWXKJ = true;
		p.enemy.labelUseWXKJ = new JLabel("����һ����и�ɻ�");
		p.enemy.frame.add(p.enemy.labelUseWXKJ);
		p.enemy.frame.add(p.enemy.bWXKJ);
		p.enemy.labelUseWXKJ.setBounds(400, 200, 200, 50);
		p.enemy.bWXKJ.setBounds(400, 250, 50, 50);
		
		p.enemy.bWXKJ.addActionListener(new CancelListener());
	}
	private class CancelListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			p.enemy.frame.remove(p.enemy.bWXKJ);
			p.enemy.labelUseWXKJ.setVisible(false);
			p.enemy.bWXKJ.removeActionListener(this);
			p.enemy.UseMiss();
			p.enemy.isWXKJ = false;
		}
	}
}

class GHCQListener implements ActionListener{//���Ӳ���
	Player p;
	Button b;
	Button b1,b2,b3;
	GHCQListener(Player p,Button b){
		this.p = p;
		this.b = b;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(p.isDesert) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;;
			p.desertNumber --;
			if(p.desertNumber == 0) {
				p.isDesert = false;
				p.labelDesert.setVisible(false);
				p.enemy.judgeStage.execute();
			}
		}
		if(!p.isMyturn) {
			return;
		}
		if(p.isZBSM) {
			System.out.println(e.getActionCommand());
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.ZBSMNumber ++;
			if(p.ZBSMNumber == 2) {
				p.ZBSMNumber = 0;
				p.enemy.UseMiss();
			}
			return;
		}
		
		if(p.isGSF) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.GSFNumber ++;
			if(p.ZBSMNumber == 2) {
				p.GSFNumber = 0;
				p.enemy.UseMiss();
			}
			return;
		}
		System.out.println(p.name + "ʹ��"+e.getActionCommand());
		p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
		if(p.enemy.WXKJNumber == 0) {
			execute();
			return;
		}
		p.enemy.isWXKJ = true;
		p.enemy.labelUseWXKJ = new JLabel("����һ����и�ɻ�");
		p.enemy.frame.add(p.enemy.labelUseWXKJ);
		p.enemy.frame.add(p.enemy.bWXKJ);
		p.enemy.labelUseWXKJ.setBounds(400, 200, 200, 50);
		p.enemy.bWXKJ.setBounds(400, 250, 50, 50);
		
		p.enemy.bWXKJ.addActionListener(new CancelListener());
	}
	private class CancelListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			p.enemy.frame.remove(p.enemy.bWXKJ);
			p.enemy.labelUseWXKJ.setVisible(false);
			p.enemy.bWXKJ.removeActionListener(this);
			execute();
			p.enemy.isWXKJ = false;
			p.isMyturn = true;
		}
	}
	void execute() {
		b3 = new Button("����");
		b3.setBounds(500,200,80,25);
		b3.addActionListener(new DismantleListener());
		p.frame.add(b3);
		if(p.enemy.weapon == null && p.enemy.armor == null) {}
		else {
			b1 = new Button("����");
			b1.setBounds(300,200,80,25);
			b1.addActionListener(new WeaponListener());
			b2 = new Button("����");
			b2.setBounds(400,200,80,25);
			b2.addActionListener(new ArmorListener());
			if(p.enemy.weapon != null) {
				p.frame.add(b1);
			}
			if(p.enemy.armor != null) {
				p.frame.add(b2);
			}
		}
	}
	private class WeaponListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(b1 != null)
				p.frame.remove(b1);
			if(b2 != null)
				p.frame.remove(b2);
			p.frame.remove(b3);
			Card c = p.enemy.weapon;
			String str = c.name + " " + c.flowerColor + Integer.toString(c.point);
			System.out.println(p.name+"���"+" " + str);
			p.enemy.desertWeapon();
			p.isMyturn = true;
		}
		
	}
	private class ArmorListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(b1 != null)
				p.frame.remove(b1);
			if(b2 != null)
				p.frame.remove(b2);
			p.frame.remove(b3);
			Card c = p.enemy.armor;
			String str = c.name + " " + c.flowerColor + Integer.toString(c.point);
			System.out.println(p.name+"���"+" " + str);
			p.enemy.desertArmor();
			p.isMyturn = true;
		}	
	}
	private class DismantleListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(b1 != null)
				p.frame.remove(b1);
			if(b2 != null)
				p.frame.remove(b2);
			p.frame.remove(b3);
			Random r = new Random();
			int chooseNumber = r.nextInt(p.enemy.cardNumber);
			String str = p.enemy.handCard[chooseNumber].name+" "+p.enemy.handCard[chooseNumber].flowerColor+Integer.toString(p.enemy.handCard[chooseNumber].point);
			System.out.println(p.name+"���"+" " + str);
			p.enemy.cardHeap.desertHeap.insertHeap(p.enemy.desertCard(chooseNumber));;
		}
	}
}

class SSQYListener implements ActionListener{//˳��ǣ��
	Player p;
	Button b;
	Button b1,b2,b3;
	SSQYListener(Player p,Button b){
		this.p = p;
		this.b = b;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(p.isDesert) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.desertNumber --;
			if(p.desertNumber == 0) {
				p.isDesert = false;
				p.labelDesert.setVisible(false);
				p.enemy.judgeStage.execute();
			}
		}
		if(!p.isMyturn) {
			return;
		}
		if(p.isZBSM) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.ZBSMNumber ++;
			if(p.ZBSMNumber == 2) {
				p.ZBSMNumber = 0;
				p.enemy.UseMiss();
				p.isZBSM = false;
			}
			return;
		}
		if(p.isGSF) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.GSFNumber ++;
			if(p.ZBSMNumber == 2) {
				p.GSFNumber = 0;
				p.enemy.blood --;
				p.isGSF = false;
			}
			return;
		}
		System.out.println(p.name + "ʹ��"+e.getActionCommand());
		int place = p.hashMap.get(e.getActionCommand());
		p.desertCard(place);
		if(p.enemy.WXKJNumber == 0) {
			execute();		
			return;
		}
		p.enemy.isWXKJ = true;
		p.enemy.labelUseWXKJ = new JLabel("����һ����и�ɻ�");
		p.enemy.frame.add(p.enemy.labelUseWXKJ);
		p.enemy.frame.add(p.enemy.bWXKJ);
		p.enemy.labelUseWXKJ.setBounds(400, 200, 200, 50);
		p.enemy.bWXKJ.setBounds(400, 250, 50, 50);
		
		p.enemy.bWXKJ.addActionListener(new CancelListener());
		
	}
	private class CancelListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			p.enemy.frame.remove(p.enemy.bWXKJ);
			p.enemy.labelUseWXKJ.setVisible(false);
			p.enemy.bWXKJ.removeActionListener(this);
			execute();
			p.enemy.isWXKJ = false;
			p.isMyturn = true;
		}
	}
	void execute(){
		Button Enemyb = new Button();
		p.enemy.buttonStack.add(Enemyb);
		p.enemy.frame.add(Enemyb);
		Enemyb.setBounds(p.enemy.enemyX, 0, 80, 150);
		p.enemy.enemyX -= 80;
		
		b3 = new Button("����");
		b3.setBounds(500,200,80,25);
		b3.addActionListener(new TakeListener());
		p.frame.add(b3);
		p.frame.remove(b);	
		if(p.enemy.weapon == null && p.enemy.armor == null) {}
		else {
			b1 = new Button("����");
			b1.setBounds(300,200,80,25);
			b1.addActionListener(new WeaponListener());
			b2 = new Button("����");
			b2.setBounds(400,200,80,25);
			b2.addActionListener(new ArmorListener());
			if(p.enemy.weapon != null) {
				p.frame.add(b1);
			}
			if(p.enemy.armor != null) {
				p.frame.add(b2);
			}
		}
	}
	private class WeaponListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(b1 != null)
				p.frame.remove(b1);
			if(b2 != null)
				p.frame.remove(b2);
			p.frame.remove(b3);
			Card[] tempCard = new Card[p.cardNumber + 1];
			for(int i = 0;i < p.cardNumber;i ++) {
				tempCard[i] = p.handCard[i];
			}
			
			Card c = p.enemy.weapon;
			tempCard[p.cardNumber] = c;
			
			p.handCard = tempCard;
			p.enemy.desertWeapon();
			
			String str = c.name + " " + c.flowerColor + Integer.toString(c.point);
			Button b = new Button(str);
			p.hashMap.put(str, p.cardNumber);
			
			b.setFont(new Font("Helvetica",Font.PLAIN,10));
			b.setBounds(p.myPlaceX,400,80,150);
			p.addButton(b);
			
			p.frame.add(b);
			new ButtonListener(p,b).addListener();
			System.out.println(p.name + "����"+" " + str);
			
			p.myPlaceX += 80;
			p.cardNumber ++;
			p.isMyturn = true;
		}
		
	}
	private class ArmorListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(b1 != null)
				p.frame.remove(b1);
			if(b2 != null)
				p.frame.remove(b2);
			p.frame.remove(b3);
			
			Card[] tempCard = new Card[p.cardNumber + 1];
			for(int i = 0;i < p.cardNumber;i ++) {
				tempCard[i] = p.handCard[i];
			}
			
			Card c = p.enemy.armor;
			tempCard[p.cardNumber] = c;
			
			p.handCard = tempCard;
			p.enemy.desertArmor();
			
			String str = c.name + " " + c.flowerColor + Integer.toString(c.point);
			Button b = new Button(str);
			p.hashMap.put(str, p.cardNumber);
			new ButtonListener(p,b).addListener();
			b.setFont(new Font("Helvetica",Font.PLAIN,10));
			b.setBounds(p.myPlaceX,400,80,150);
			p.addButton(b);
			p.frame.add(b);
			System.out.println(p.name + "����"+" " + str);
			p.myPlaceX += 80;
			p.cardNumber ++;
			p.isMyturn = true;
		}	
	}
	private class TakeListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(b1 != null)
				p.frame.remove(b1);
			if(b2 != null)
				p.frame.remove(b2);
			p.frame.remove(b3);
			Random r = new Random();
			int chooseNumber = r.nextInt(p.enemy.cardNumber);
			Card c = p.enemy.handCard[chooseNumber];
			
			String str = c.name+" "+c.flowerColor+Integer.toString(c.point);
			System.out.println(p.name+"����"+" " + str);
			Card[] tempCard = new Card[p.cardNumber + 1];
			for(int i = 0;i < p.cardNumber;i ++) {
				tempCard[i] = p.handCard[i];
			}
			tempCard[p.cardNumber] = c;
			p.handCard = tempCard;
			Button button = new Button(str);
			p.hashMap.put(str, p.cardNumber);
			new ButtonListener(p,button).addListener();
			button.setFont(new Font("Helvetica",Font.PLAIN,10));
			button.setBounds(p.myPlaceX,400,80,150);
			p.addButton(button);
			p.frame.add(button);
			p.myPlaceX += 80;
			p.cardNumber ++;
			p.enemy.desertCard(chooseNumber);
			p.isMyturn = true;
		}
	}
}

class LBSSListener implements ActionListener{//�ֲ�˼��
	Player p;
	Button b;
	LBSSListener(Player p,Button b){
		this.p = p;
		this.b = b;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(p.isDesert) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.desertNumber --;
			if(p.desertNumber == 0) {
				p.isDesert = false;
				p.labelDesert.setVisible(false);
				p.enemy.judgeStage.execute();
			}
		}
		if(!p.isMyturn) {
			return;
		}
		if(p.isZBSM) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.ZBSMNumber ++;
			if(p.ZBSMNumber == 2) {
				p.ZBSMNumber = 0;
				p.enemy.UseMiss();
				p.isZBSM = false;
			}
			return;
		}
		if(p.isGSF) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.GSFNumber ++;
			if(p.ZBSMNumber == 2) {
				p.GSFNumber = 0;
				p.enemy.blood --;
				p.isGSF = false;
			}
			return;
		}
		if(p.enemy.judge[0] != null) {
			System.out.println("�Է��ж��������ֲ�˼��!");
			return;
		}
		System.out.println(p.name + "ʹ��"+e.getActionCommand());
		int chooseNumber = p.hashMap.get(e.getActionCommand());
		p.enemy.judge[0] = p.handCard[chooseNumber];
		p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
	}
}

class SYQJListener implements ActionListener{//ˮ���߾�
	Player p;
	Button b,b1,b2;
	SYQJListener(Player p,Button b){
		this.p = p;
		this.b = b;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(p.isDesert) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.desertNumber --;
			if(p.desertNumber == 0) {
				p.isDesert = false;
				p.labelDesert.setVisible(false);
				p.enemy.judgeStage.execute();
			}
		}
		if(!p.isMyturn) {
			return;
		}
		if(p.isZBSM) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.ZBSMNumber ++;
			if(p.ZBSMNumber == 2) {
				p.ZBSMNumber = 0;
				p.enemy.UseMiss();
				p.isZBSM = false;
			}
			return;
		}
		if(p.isGSF) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.GSFNumber ++;
			if(p.ZBSMNumber == 2) {
				p.GSFNumber = 0;
				p.enemy.blood --;
				p.isGSF = false;
			}
			return;
		}
		//p.frame.remove(b);
		System.out.println(p.name + "ʹ��"+e.getActionCommand());
		p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
		if(p.enemy.WXKJNumber == 0) {
			execute();
			return;
		}
		p.enemy.isWXKJ = true;
		p.enemy.labelUseWXKJ = new JLabel("����һ����и�ɻ�");
		p.enemy.frame.add(p.enemy.labelUseWXKJ);
		p.enemy.frame.add(p.enemy.bWXKJ);
		p.enemy.labelUseWXKJ.setBounds(400, 200, 200, 50);
		p.enemy.bWXKJ.setBounds(400, 250, 50, 50);
		
		p.enemy.bWXKJ.addActionListener(new CancelListener());	
	}
	private class CancelListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			p.enemy.frame.remove(p.enemy.bWXKJ);
			p.enemy.labelUseWXKJ.setVisible(false);
			p.enemy.bWXKJ.removeActionListener(this);
			execute();
			p.enemy.isWXKJ = false;
		}
	}
	void execute() {
		if(p.enemy.weapon == null && p.enemy.armor == null) {
			p.enemy.getInjure();
			return;
		}
		b1 = new Button("����װ����");
		b2 = new Button("�ܵ��˺�");
		b1.setBounds(400,225,80,25);
		b2.setBounds(500,225,80,25);
		b1.addActionListener(new ArmListener());
		b2.addActionListener(new HurtListener());
		p.enemy.frame.add(b1);
		p.enemy.frame.add(b2);
	}
	private class ArmListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			p.enemy.frame.remove(b1);
			p.enemy.frame.remove(b2);
			p.enemy.desertWeapon();
			p.enemy.desertArmor();
			p.isMyturn = true;
		}
	}
	private class HurtListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			p.enemy.frame.remove(b1);
			p.enemy.frame.remove(b2);
			p.enemy.getInjure();
			p.isMyturn = true;
		}
		
	}
}

class WZSYListener implements ActionListener{//��������
	Player p;
	Button b;
	WZSYListener(Player p,Button b){
		this.p = p;
		this.b = b;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(p.isDesert) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.desertNumber --;
			if(p.desertNumber == 0) {
				p.isDesert = false;
				p.labelDesert.setVisible(false);
				p.enemy.judgeStage.execute();
			}
		}
		if(!p.isMyturn) {
			return;
		}
		if(p.isZBSM) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.ZBSMNumber ++;
			if(p.ZBSMNumber == 2) {
				p.ZBSMNumber = 0;
				p.enemy.UseMiss();
				p.isZBSM = false;
			}
			return;
		}
		if(p.isGSF) {
			p.desertCard(p.hashMap.get(b.getLabel()));
			p.GSFNumber ++;
			if(p.ZBSMNumber == 2) {
				p.GSFNumber = 0;
				p.enemy.blood --;
				p.isGSF = false;
			}
			return;
		}
		System.out.println(p.name + "ʹ��"+e.getActionCommand());
		p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
		if(p.enemy.WXKJNumber == 0) {
			execute();
			return;
		}
		p.enemy.isWXKJ = true;
		p.enemy.labelUseWXKJ = new JLabel("����һ����и�ɻ�");
		p.enemy.frame.add(p.enemy.labelUseWXKJ);
		p.enemy.frame.add(p.enemy.bWXKJ);
		p.enemy.labelUseWXKJ.setBounds(400, 200, 200, 50);
		p.enemy.bWXKJ.setBounds(400, 250, 50, 50);
		
		p.enemy.bWXKJ.addActionListener(new CancelListener());	
	}
	private class CancelListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			p.enemy.frame.remove(p.enemy.bWXKJ);
			p.enemy.labelUseWXKJ.setVisible(false);
			p.enemy.bWXKJ.removeActionListener(this);
			execute();
			p.enemy.isWXKJ = false;
		}
	}
	void execute() {
		for(int i = 0; i < 2;i ++) {
			p.takeCard();
		}
		p.isMyturn = true;
	}
}

class BLCDListener implements ActionListener{//�������
	Player p;
	Button b;
	BLCDListener(Player p,Button b){
		this.p = p;
		this.b = b;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(p.isDesert) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.desertNumber --;
			if(p.desertNumber == 0) {
				p.isDesert = false;
				p.labelDesert.setVisible(false);
				p.enemy.judgeStage.execute();
			}
		}
		if(!p.isMyturn) {
			return;
		}
		if(p.isZBSM) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.ZBSMNumber ++;
			if(p.ZBSMNumber == 2) {
				p.ZBSMNumber = 0;
				p.enemy.UseMiss();
				p.isZBSM = false;
			}
			return;
		}
		if(p.isGSF) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.GSFNumber ++;
			if(p.ZBSMNumber == 2) {
				p.GSFNumber = 0;
				p.enemy.blood --;
				p.isGSF = false;
			}
			return;
		}
		if(p.enemy.judge[1] != null) {
			System.out.println("�Է��ж������б������!");
			return;
		}
		
		int place = p.hashMap.get(b.getLabel());
		Card c = p.handCard[place];
		System.out.println(p.name + "ʹ��"+e.getActionCommand());
		p.desertCard(p.hashMap.get(b.getLabel()));
		p.enemy.judge[1] = c;
	}
}

class NMRQListener implements ActionListener{//��������
	Player p;
	Button b;
	NMRQListener(Player p,Button b){
		this.p = p;
		this.b = b;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(p.isDesert) {
			p.desertCard(p.hashMap.get(e.getActionCommand()));
			p.desertNumber --;
			if(p.desertNumber == 0) {
				p.isDesert = false;
				p.labelDesert.setVisible(false);
				p.enemy.judgeStage.execute();
			}
		}
		if(!p.isMyturn) {
			return;
		}
		if(p.isZBSM) {
			p.desertCard(p.hashMap.get(b.getLabel()));
			p.ZBSMNumber ++;
			if(p.ZBSMNumber == 2) {
				p.ZBSMNumber = 0;
				p.enemy.UseMiss();
				p.isZBSM = false;
			}
			return;
		}
		if(p.isGSF) {
			p.desertCard(p.hashMap.get(b.getLabel()));
			p.GSFNumber ++;
			if(p.ZBSMNumber == 2) {
				p.GSFNumber = 0;
				p.enemy.blood --;
				p.isGSF = false;
			}
			return;
		}
		System.out.println(p.name + "ʹ��"+e.getActionCommand());
		p.desertCard(p.hashMap.get(b.getLabel()));
		if(p.enemy.WXKJNumber == 0) {
			execute();
			return;
		}
		p.enemy.isWXKJ = true;
		p.enemy.labelUseWXKJ = new JLabel("����һ����и�ɻ�");
		p.enemy.frame.add(p.enemy.labelUseWXKJ);
		p.enemy.frame.add(p.enemy.bWXKJ);
		p.enemy.labelUseWXKJ.setBounds(400, 200, 200, 50);
		p.enemy.bWXKJ.setBounds(400, 250, 50, 50);
		
		p.enemy.bWXKJ.addActionListener(new CancelListener());
	}
	private class CancelListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			p.enemy.frame.remove(p.enemy.bWXKJ);
			p.enemy.labelUseWXKJ.setVisible(false);
			p.enemy.bWXKJ.removeActionListener(this);
			execute();
			p.enemy.isWXKJ = false;
		}
	}
	void execute() {
		p.enemy.UseKill();
		
	}
}

class WXKJListener implements ActionListener{//��и�ɻ�
	Player p;
	Button b;
	WXKJListener(Player p,Button b){
		this.p = p;
		this.b = b;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(p.isDesert) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.desertNumber --;
			p.WXKJNumber --;
			if(p.desertNumber == 0) {
				p.isDesert = false;
				p.labelDesert.setVisible(false);
				p.enemy.judgeStage.execute();
			}
		}
		if(p.isZBSM) {
			p.desertCard(p.hashMap.get(b.getLabel()));
			p.ZBSMNumber ++;
			if(p.ZBSMNumber == 2) {
				p.ZBSMNumber = 0;
				p.enemy.UseMiss();
				p.isZBSM = false;
			}
			return;
		}
		if(p.isGSF) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.GSFNumber ++;
			if(p.GSFNumber == 2) {
				p.GSFNumber = 0;
				p.enemy.blood --;
				p.isGSF = false;
			}
			return;
		}
		if(!p.isWXKJ)
			return;
		System.out.println(p.name + "ʹ��"+e.getActionCommand());
		if(p.isLBSS) {
			p.outCardStage.execute();
			p.isLBSS= false;
		}
		if(p.isBLCD) {
			p.takeCardStage.execute();
			p.isBLCD = false;
		}
		p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
		p.frame.remove(p.bWXKJ);
		p.labelUseWXKJ.setVisible(false);
		
		execute();
	}
	void execute() {
		p.WXKJNumber --;
		p.isWXKJ = false;
	}
	
}


class FinishOutCardListener implements ActionListener{
	Player p;
	Button b;
	FinishOutCardListener(Player p,Button b){
		this.p = p;
		this.b = b;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		b.setVisible(false);
		p.isMyturn = false;
		p.frame.remove(b);
		p.desertCardStage.execute();
	}
}


public class KitCard {

}
