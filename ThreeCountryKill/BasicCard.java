package ThreeCountryKill;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;



class KillListener implements ActionListener {
	Player p;
	Button b;
	KillListener(Player p,Button b){
		this.p = p;
		this.b = b;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(p.isDesert) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.labelDesert.setVisible(false);
			p.desertNumber --;
			if(p.desertNumber == 0) {
				p.isDesert = false;
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
				p.enemy.getInjure();
				p.isGSF = false;
			}
			return;
		}
		if(p.isKill) {//如果处于响应杀的进程，则此杀不可用
			return;
		}
		if(p.useKillNumber >= p.upperLimitKill) {//规定不能超过回合最大出杀数
			if(p.weapon == null || !p.weapon.name.equals("诸葛连弩")) {
				System.out.println("超出回合最大出杀数");
				return;
			}
		}
		Card c = p.handCard[p.hashMap.get(e.getActionCommand())];
		String str = c.name + " "+c.flowerColor+Integer.toString(c.point);
		System.out.println(p.name+ "对"+p.enemy.name+"使用"+str);
		p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
		p.useKillNumber ++;
		if(p.enemy.armor == null) {
			p.enemy.UseMiss();
			return;
		}
		else {//如果对方装备有仁王盾，在我方没有青钢剑的前提下，敌方免疫黑杀
			if(p.enemy.armor.name.equals("仁王盾")&&p.weapon==null) {
				if(e.getActionCommand().contains("梅花")||e.getActionCommand().contains("黑桃")) {
					System.out.println("黑杀对仁王盾无效！");
				}
				else
					p.enemy.UseMiss();
			}
			else if(p.enemy.armor.name.equals("仁王盾")&&!p.weapon.name.equals("青钢剑")) {
				if(e.getActionCommand().contains("梅花")||e.getActionCommand().contains("黑桃")) {
					System.out.println("黑杀对仁王盾无效！");
				}
				else
					p.enemy.UseMiss();
			}
			else {
				p.enemy.UseMiss();
			}
		}
	}	
}

class MissListener implements ActionListener{
	Player p;
	Button b;
	MissListener(Player p,Button b){
		this.p = p;
		this.b = b;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(p.isDesert) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.labelDesert.setVisible(false);
			p.desertNumber --;
			if(p.desertNumber == 0) {
				p.isDesert = false;
				p.enemy.judgeStage.execute();
			}
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
		if(!p.isMiss)
			return;
		System.out.println(p.name + "使用"+e.getActionCommand());
		if(p.enemy.weapon!=null&&(p.enemy.weapon.name.equals("贯石斧")&&p.enemy.cardNumber >= 2)) {
			//触发贯石斧技能
			p.enemy.frame.add(p.enemy.labelGSF);
			p.enemy.labelGSF.setVisible(true);
			p.enemy.labelGSF.setBounds(400,200,100,25);
			p.enemy.bGSFSure.setBounds(350,250,80,25);
			p.enemy.bGSFCancel.setBounds(450,250,80,25);
			p.enemy.bGSFSure.addActionListener(new Sure());
			p.enemy.bGSFCancel.addActionListener(new Cancel());
			p.enemy.frame.add(p.enemy.bGSFSure);
			p.enemy.frame.add(p.enemy.bGSFCancel);
		}
		p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
		p.frame.remove(p.bMiss);
		p.check = true;
		p.isMiss = false;
		p.labelUseMiss.setVisible(false);
	}
	private class Sure implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			p.enemy.labelGSF.setVisible(false);
			p.enemy.frame.remove(p.enemy.bGSFSure);
			p.enemy.frame.remove(p.enemy.bGSFCancel);
			p.enemy.isGSF = true;
		}
	}
	private class Cancel implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			p.enemy.labelGSF.setVisible(false);
			p.enemy.frame.remove(p.enemy.bGSFSure);
			p.enemy.frame.remove(p.enemy.bGSFCancel);
		}
	}
}

class CancelMissListener implements ActionListener{
	Player p;
	Button bWeapon = new Button("武器");
	Button bArmor = new Button("防具");
	Button bHandCard = new Button("手牌");
	int num = 0;
	CancelMissListener(Player p){
		this.p = p;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//p.bMiss.removeActionListener(this);
		p.check = false;
		p.frame.remove(p.bMiss);
		p.isMiss = false;
		p.labelUseMiss.setVisible(false);
		if(p.enemy.weapon!=null&&p.enemy.weapon.name.equals("寒冰剑")) {
			int number = p.cardNumber;
			if(p.weapon!= null)
				number++;
			if(p.armor != null)
				number++;
			if(number < 2) {
				p.blood --;
			    return;
			}
			p.enemy.frame.add(p.enemy.labelHBJ);
			p.enemy.labelHBJ.setVisible(true);
			p.enemy.labelHBJ.setBounds(400,200,100,25);
			p.enemy.bHBJSure.setBounds(350,250,80,25);
			p.enemy.bHBJCancel.setBounds(450,250,80,25);
			p.enemy.bHBJSure.addActionListener(new Sure());
			p.enemy.bHBJCancel.addActionListener(new Cancel());
			p.enemy.frame.add(p.enemy.bHBJSure);
			p.enemy.frame.add(p.enemy.bHBJCancel);
		}
		else {
			p.getInjure();
			p.isMyturn = true;
		}
	}
	private class Sure implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			p.isMyturn = true;
			p.enemy.labelHBJ.setVisible(false);
			p.enemy.frame.remove(p.enemy.bHBJSure);
			p.enemy.frame.remove(p.enemy.bHBJCancel);
			p.enemy.isGSF = true;
			
			bWeapon.addActionListener(new Weapon());
			bWeapon.setBounds(300,200,100,25);
			if(p.weapon != null)
				p.enemy.frame.add(bWeapon);
			
			bArmor.addActionListener(new Armor());
			bArmor.setBounds(400,200,100,25);
			if(p.armor != null)
				p.enemy.frame.add(bArmor);
			
			bHandCard.addActionListener(new HandCard());
			bHandCard.setBounds(500,200,100,25);
			if(p.cardNumber != 0)
				p.enemy.frame.add(bHandCard);
		}
	}
	private class Cancel implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			p.enemy.labelHBJ.setVisible(false);
			p.enemy.frame.remove(p.enemy.bHBJSure);
			p.enemy.frame.remove(p.enemy.bHBJCancel);
			p.getInjure();
		}
	}
	private class Weapon implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			p.enemy.frame.remove(bWeapon);
			p.desertWeapon();
			num ++;
			if(num == 2) {
				p.enemy.frame.remove(bArmor);
				p.enemy.frame.remove(bHandCard);
				p.isMyturn = true;
			}
		}
	}
	private class Armor implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			p.enemy.frame.remove(bArmor);
			p.desertArmor();
			num ++;
			if(num == 2) {
				p.enemy.frame.remove(bWeapon);
				p.enemy.frame.remove(bHandCard);
				p.isMyturn = true;
			}
		}
	}
	private class HandCard implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			p.desertCard(new Random().nextInt(p.cardNumber));
			num ++;
			if(p.cardNumber == 0) {
				p.enemy.frame.remove(bHandCard);
			}
			if(num == 2) {
				p.enemy.frame.remove(bWeapon);
				p.enemy.frame.remove(bArmor);
				p.enemy.frame.remove(bHandCard);
				p.isMyturn = true;
			}
		}
	}
}

class UseKillListener implements ActionListener{
	Player p;
	Button b;
	UseKillListener(Player p,Button b){
		this.p = p;
		this.b = b;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(!p.isKill)
			return;
		System.out.println(p.name + "打出"+e.getActionCommand());
		p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
		p.frame.remove(p.bKill);
		if(p.isDuel)
			p.enemy.UseKill();
		p.check = true;
		p.isKill = false;
		p.labelUseKill.setVisible(false);
	}
}

class CancelKillListener implements ActionListener{
	Player p;
	CancelKillListener(Player p){
		this.p = p;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		p.getInjure();
		p.check = false;
		p.enemy.isDuel = p.isDuel = false;
		p.frame.remove(p.bKill);
		p.labelUseKill.setVisible(false);
	}
}

	

class PeachListener implements ActionListener{//桃
	Player p;
	Button b;
	PeachListener(Player p,Button b){
		this.p = p;
		this.b = b;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(p.isDesert) {
			p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
			p.labelDesert.setVisible(false);
			p.desertNumber --;
			if(p.desertNumber == 0) {
				p.isDesert = false;
				p.enemy.judgeStage.execute();
			}
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
		if(!p.isMyturn&&!p.isDying) {
			return;
		}
		System.out.println(p.name + "使用"+e.getActionCommand());
		if(p.blood == p.upperLimitBlood) {
			System.out.println("血量已满，不能使用桃");
			return;
		}
		p.cardHeap.desertHeap.insertHeap(p.desertCard(p.hashMap.get(e.getActionCommand())));;
		if(p.isDying) {
			p.labelUsePeach.setVisible(false);
			p.frame.remove(p.bDying);
		}
		p.blood ++;
	}
}

public class BasicCard {

}
