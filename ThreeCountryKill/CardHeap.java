package ThreeCountryKill;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


class Card{//�Զ���Card�࣬����ģ���ƿ��е���
	public int ID;
	public int point;
	public String name;
	public String flowerColor;
	public String type;
	Card(int ID,int point,String name,String flowerColor,String type){
		this.ID = ID;
		this.point = point;
		this.name = name;
		this.flowerColor = flowerColor;
		this.type = type;
	}
}

class DesertHeap{//���ƶѣ������������ƶѺ�ɾ�����ƶ�ĳ�Ƶķ���
	int cardNumber;
	Card[] heap;
	DesertHeap(){
		cardNumber = 0;
		heap = new Card[0];
	}
	void insertHeap(Card card) {//���ڽ�ʹ�õ����������ƶ�
		Card[] temp = new Card[cardNumber + 1];
		for(int i = 0;i < cardNumber;i ++) {
			temp[i] = heap[i];
		}
		temp[cardNumber] = card;
		cardNumber = cardNumber + 1;
		heap = temp;
	}
	Card deleteCard(int p){//ϴ��ʱ�ĸ�������
		Card temp = heap[p];
		Card[] newHeap = new Card[cardNumber - 1];
		int num = 0;
		for(int i = 0;i < cardNumber;i ++) {
			if(i != p){
				newHeap[num++] =  heap[i];
			}
		}
		cardNumber --;
		heap = newHeap;
		return temp;
	}
}

public class CardHeap{
	Connection connection;
	static Statement statement;
	static Card[] cards = new Card[52];
	static Queue<Card> heap = new LinkedList <Card>();
	static int[] sequence;//��ʼ���ƶ�ʱ�ĸ������飬������������sequence��δʹ�ù�����
	static int tempNumber;//��ʼ��ʱ���ڼ�¼��δ�����ƶѵ��Ƶ�����
	static int cardNumber;//�ƶ����Ƶ�����
	static DesertHeap desertHeap = new DesertHeap();
	CardHeap(Connection c,Statement s) throws SQLException{
		connection = c;
		statement = s;
		cardNumber = 0;
	}
	static int[] resequence(int k) {
		int[] temp = new int[tempNumber - 1];
		int p = 0;
		for(int i = 0;i < tempNumber;i ++) {
			if(i != k)
				temp[p++] = sequence[i];
		}
		return temp;
	}
	static void insertHeap(Card card) {
		heap.add(card);
		cardNumber = cardNumber + 1;
	}
	static Card takeCard(){//���ƶѶ���һ����
		if(heap.isEmpty()) {
			reshuffle();
		}
		Card temp = heap.poll();
		cardNumber --;
		return temp;
	}
	static void main(String[] args) throws SQLException {
		Random r = new Random();
		sequence = new int[52];
		for(int i = 0;i < 52;i ++) {
			sequence[i] = i + 1;
		}
		tempNumber = 52;
		for(int i = 0;i < 52;i ++) {
			int rd = r.nextInt(tempNumber);
			String str = Integer.toString(sequence[rd]);
			sequence = resequence(rd);
			tempNumber --;
			str = "select* from card where id = " + str + ";";
			ResultSet result = statement.executeQuery(str);
			while(result.next()) {
				cards[i] = new Card(result.getInt(1),result.getInt(5),result.getString(2),result.getString(4),result.getString(3));
				insertHeap(cards[i]);
			}
		}
	}
	static void reshuffle() {
		Random r = new Random();
		Card[] temp = new Card[desertHeap.cardNumber];
		while(desertHeap.cardNumber != 0) {
			int rd =r.nextInt(desertHeap.cardNumber);
			insertHeap(desertHeap.heap[rd]);
			desertHeap.deleteCard(rd);
		}
	}
	Card judge() {
		Card temp = takeCard();
		desertHeap.insertHeap(temp);
		String str = temp.name+" "+temp.flowerColor+Integer.toString(temp.point);
		System.out.println("�ж����Ϊ:" + str);
		return temp;
	}
	void display() throws SQLException {
		for(int i = 0;i < 52;i ++) {
			Card c = heap.poll();
			System.out.println(c.ID + " "  + c.name);
		}
	}
}