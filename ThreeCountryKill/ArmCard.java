package ThreeCountryKill;

import java.awt.Button;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

class ArmWeaponListener implements ActionListener{//装备武器
	Player p;
	Button b;
	ArmWeaponListener(Player p,Button b){
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
		int i = p.hashMap.get(e.getActionCommand());
		Card c = p.handCard[i];
		p.desertCard(i);
		System.out.println(c.name);
		
		
		if(c.name.equals("丈八蛇矛")) {
			System.out.println("丈八蛇矛");
			p.bZBSM.setBounds(800,350,90,25);
			p.frame.add(p.bZBSM);
		}
		if(p.weapon != null) {
			p.desertWeapon();
		}
		p.weapon = c;
		
		String str = c.name+" "+c.flowerColor+Integer.toString(c.point);
		p.arm[0].setVisible(false);
		p.arm[0] = new JLabel(str);
		p.frame.add(p.arm[0]);
		p.arm[0].setBounds(900,350,80,25);
		p.arm[0].setFont(new Font("Helvetica",Font.PLAIN,10));
		p.arm[0].setVisible(true);
		
		
		p.enemy.enemyArm[0].setVisible(false);
		p.enemy.enemyArm[0] = new JLabel(str);
		p.enemy.frame.add(p.enemy.enemyArm[0]);
		p.enemy.enemyArm[0].setBounds(0,150,80,25);
		p.enemy.enemyArm[0].setFont(new Font("Helvetica",Font.PLAIN,10));
		p.enemy.enemyArm[0].setVisible(true);

	}
}

class ArmArmorListener implements ActionListener{//装备防具
	Player p;
	Button b;
	ArmArmorListener(Player p,Button b){
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
		int i = p.hashMap.get(e.getActionCommand());
		Card c = p.handCard[i];
		p.desertCard(i);
		
		if(p.armor != null) {
			p.desertArmor();
		}
		p.armor = c;
		String str = c.name+" "+c.flowerColor+Integer.toString(c.point);
		p.arm[1].setVisible(false);
		p.arm[1] = new JLabel(str);
		p.frame.add(p.arm[1]);
		p.arm[1].setBounds(900,375,80,25);
		p.arm[1].setFont(new Font("Helvetica",Font.PLAIN,10));
		p.arm[1].setVisible(true);
		
		
		p.enemy.enemyArm[1].setVisible(false);
		p.enemy.enemyArm[1] = new JLabel(str);
		p.enemy.frame.add(p.enemy.enemyArm[1]);
		p.enemy.enemyArm[1].setBounds(0,175,80,25);
		p.enemy.enemyArm[1].setFont(new Font("Helvetica",Font.PLAIN,10));
		p.enemy.enemyArm[1].setVisible(true);
	}
}

class ZBSMListener implements ActionListener{
	Player p;
	ZBSMListener(Player p){
		this.p = p;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(!p.isMyturn)
			return;
		if(p.useKillNumber >= p.upperLimitKill) {
			System.out.println("超出回合最大出杀数");
			return;
		}
		if(p.cardNumber < 2) {
			System.out.println("当前手牌数量不足以发动丈八蛇矛的主动技能");
			return;
		}
		p.useKillNumber ++;
		p.isZBSM = true;
	}
}


public class ArmCard {

}
