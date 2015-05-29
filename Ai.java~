import java.util.*;
public class Ai {
	final public static int fold=0;
	final public static int call=1;
	final public static int raise=2;
	final public static int check=3;
	Poker[] pokes = new Poker[7];//������
	Poker[] pub_pokes = new Poker[5];//������
	//String[][] seats;//�����ݱ�
	int pid=0;//�Լ���ID
	int jetton=0,money=0,lastjetton=0;//jetton�Լ�ʣ��ĶĽ� money�Լ�ʣ���Ǯ  lastjetton�ϼҵ���ע��
	boolean needfold=false;//�費��Ҫֱ��fold
	double safety=0;//��ע��ȫֵ ϵ��
	double n_safe=0.3;//��ȫֵϵ�� �ο���С
	int safebet=0;
	int state=0;//0-9��ʾ�Ƶ����ʹ�С
	//ÿһ�ֿ�ʼִ�еĳ�ʼ��
	public void e_seat(String[][] s,int p){
		int m = s.length;
		int n = s[0].length;
		String[][] seats = new String[m][n];
		pid=p;
//		for(int i=0;i<m;i++){
//			for(int j=0;j<n;j++){
//				seats[i][j]=s[i][j];//��������ݱ�
//			}
//		}
		for(int k=0;k<m;k++){
			if(Integer.parseInt(s[k][1])==pid){
				jetton=Integer.parseInt(s[k][2]);//��ȡ�Լ�ʣ��ĶĽ�
				money=Integer.parseInt(s[k][3]);//��ȡ�Լ�ʣ���Ǯ
			}
		}
	}
	private void danger_control(int bet){//Σ�տ���
		if(jetton<=100)
			needfold=true;
		safety=bet/(double)money;
	}
	private String action(int a,int b){//��Ӧ����
		String str = "";
		if(a==0)
			str="fold\n";
		else if(a==1)
			str="call\n";
		else if(a==2)
			str="raise b\n";
		else if(a==4)
			str="check\n";
		return str;
	}
	public void e_blind(String[][] s){
		if(s.length==2){
			safebet=Integer.parseInt(s[1][1])*3;
		}
		else{
			safebet=Integer.parseInt(s[0][1])*3;
		}
	}
	public void e_hold(Poker[] p){//�õ��Լ�������
		pokes[0]=p[0];
		pokes[1]=p[1];
		sort(pokes); //��������
	}
	public String inquire1(String[][] s){
		lastjetton=Integer.parseInt(s[0][3]);//��һ�ҵ���ע��
		danger_control(lastjetton);
		String act = "";
		if(needfold==true){//Σ�տ���
			act=action(fold,0);
			return act;
		}
		if(pokes[0].getnum()==pokes[1].getnum()){//�����һ������
			if(pokes[0].getnum()<10){//����С��10
				if(safety>n_safe)
					act=action(fold,0);
				else if(lastjetton<safebet)
					act=action(call,0);
				else 
					act=action(fold,0);
			}else if(pokes[0].getnum()>10){//���Ӵ���10
				if(safety>n_safe && lastjetton<safebet)
					act=action(call,0);
				else if(safety>n_safe && lastjetton>safebet)
					act=action(fold,0);
				else if(safety<n_safe && lastjetton<safebet)
					act=action(raise,safebet/3);
				else
					act=action(call,0);
			}
			return act;
		}
		else if(Math.abs(pokes[0].getnum()-pokes[1].getnum())==1 || pokes[0].gettype()==pokes[1].gettype()){//��������ڻ���ͬ��
			if(lastjetton>safebet)
				act=action(fold,0);
			else
				act=action(call,0);
			return act;
		}
		else{//���ʲôҲû��
			if(pokes[0].getnum()>10){
				if(lastjetton<safebet)
					act=action(call,0);
				else
					act=action(fold,0);
			}
			else
				act=action(fold,0);
			return act;
		}
	}
	public void e_flop(Poker[] p){
		pokes[2]=p[0];pub_pokes[0]=p[0];
		pokes[3]=p[1];pub_pokes[1]=p[1];
		pokes[4]=p[2];pub_pokes[2]=p[2];
		sort(pokes);sort(pub_pokes);
	}
	public String inquire2(String[][] s){
		state=Checkstate.checkState(pokes);//�����������������
		int[] st=Pub_check.check(pub_pokes);//�жϹ����ƶ���������
		lastjetton=Integer.parseInt(s[0][3]);//��һ�ҵ���ע��
		danger_control(lastjetton);
		String act = "";
		if(needfold==true){//Σ�տ���
			act=action(fold,0);
			return act;
		}
		if(state>5){//������
			if(state==6){//����+����
				if(st[0]==Pub_check.santiao || st[0]==Pub_check.duizi){
					if(lastjetton<safebet){
						act=action(call,0);
					}
					else{
						act=action(fold,0);
					}
				}
			}
			else if(state==7){//ը��
				if(st[0]==Pub_check.zhadan){//���ը��ȫ�ڹ����ƶ�����
					if(lastjetton<safebet){
						act=action(call,0);
					}
					else{
						act=action(fold,0);
					}
				}
				else if(st[0]==Pub_check.santiao || st[0]==Pub_check.duizi){//���ը�������Ż��������ڹ����Ŷ�����
					if(lastjetton<jetton/2){
						act=action(raise,jetton/2);
					}
					else{
						act=action(call,0);
					}
				}
			}
			else{
				if(lastjetton<jetton/2){
					act=action(raise,jetton/2);
				}
				else{
					act=action(call,0);
				}
			}
		}
		
		
		else if(state==5){//ͬ��
			if(lastjetton<jetton/3){
				act=action(raise,jetton/3);
			}
			else{
				act=action(call,0);
			}
		}
		
		
		else if(state==4){//�����˳��
			if(pokes[0].getnum()>=12){//������ƴ���J
				if(lastjetton<jetton/3){
					act=action(raise,jetton/3);
				}
				else{
					act=action(call,0);
				}
			}
			else{
				if(lastjetton<jetton/3){
					act=action(raise,safebet);//raise��΢�ٵ�
				}
				else{
					act=action(call,0);
				}
			}
		}
		
		else if(state==3){//����
			if(st[0]==Pub_check.santiao){
				if(lastjetton<safebet){
					act=action(call,0);
				}
				else{
					act=action(fold,0);
				}
			}
			else{
				if(lastjetton<safebet){
					act=action(raise,safebet);
				}
				else{
					act=action(call,0);
				}
			}
		}
		
		
		else if(state==2 || state==1){//�����ӻ���һ����
			if(st[0]==Pub_check.duizi){
				if(lastjetton<safebet){
					act=action(call,0);
				}
				else{
					act=action(fold,0);
				}
			}
			else{
				if(lastjetton<safebet){
					act=action(raise,safebet);
				}
				else{
					act=action(call,0);
				}
			}
		}
		
		
		else if(state==0){//ʲôҲû��
			if(pub_pokes[3]==null){//����ƶ�����ֻ�������� �����е��Ƽ��������ĸ���ɫ��ͬ
				int[] test=Pub_check.check(pokes);
				if(test[0]==Pub_check.sitonghua){
					if(lastjetton<safebet){
						act=action(call,0);
					}
				}
			}
			else{
				act=action(fold,0);
			}
		}
		
		
		return act;
	}
	public void e_turn(Poker p){
		pokes[5]=p;pub_pokes[3]=p;
		sort(pokes);sort(pub_pokes);
	}
	public void e_river(Poker p){
		pokes[6]=p;pub_pokes[4]=p;
		sort(pokes);sort(pub_pokes);
	}
	public void e_showdown(){}
	
	//ÿһ�ֽ��ط����ִ�е�����
	public void e_potwin(){
		for(int i =0;i<7;i++){
			pokes[i]=null;
			pub_pokes[i]=null;
		}
		needfold=false;
	}
	private void sort(Poker[] p){//����������дӴ�С������
		//Poker[] tmp = new Poker[7];
		int count=0;
		for(int i=0;i<p.length;i++){
			if(p[6]!=null){
				count=7;
				break;
			}
			if (p[i]==null){
				count=i;
				break;
			}
			//count=i;
		}
		for(int i=count-1;i>-1;i--){
			for(int j=0;j<i;j++){
				if(p[j].getnum()<p[j+1].getnum())
					swap(p[j],p[j+1]);
				else if(p[j].getnum()==p[j+1].getnum() && p[j].gettype()<p[j+1].gettype())
					swap(p[j],p[j+1]);
			}
		}
	}
	private void swap(Poker p1,Poker p2){//����������
		Poker p3 = new Poker(p1.getnum(),p1.gettype());
		p1.setnum(p2.getnum());p1.settype(p2.gettype());
		p2.setnum(p3.getnum());p2.settype(p3.gettype());
	}
	
	public void out(){//�������  ����ʹ��
		for(int i=0;i<7;i++){
			if(pokes[i]!=null)
				System.out.print(pokes[i].getnum()+","+pokes[i].gettype()+"  ");
		}
	}
}
