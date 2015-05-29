package run;
import java.util.*;
public class Ai {
	final public static int fold=0;
	final public static int call=1;
	final public static int raise=2;
	final public static int check=3;
	Poker[] pokes = new Poker[7];//锟斤拷锟斤拷锟斤拷
	Poker[] pub_pokes = new Poker[5];//锟斤拷锟斤拷锟斤拷
	//String[][] seats;//锟斤拷锟斤拷锟捷憋拷
	int pid=0;//锟皆硷拷锟斤拷ID
	int jetton=0,money=0,lastjetton=0;//jetton锟皆硷拷剩锟斤拷亩慕锟�money锟皆硷拷剩锟斤拷锟角� lastjetton锟较家碉拷锟斤拷注锟斤拷
	boolean needfold=false;//锟借不锟斤拷要直锟斤拷fold
	double safety=0;//锟斤拷注锟斤拷全值 系锟斤拷
	double n_safe=0.3;//锟斤拷全值系锟斤拷 锟轿匡拷锟斤拷小
	int safebet=0;
	int state=0;//0-9锟斤拷示锟狡碉拷锟斤拷锟酵达拷小
	//每一锟街匡拷始执锟叫的筹拷始锟斤拷
	public void e_seat(String[][] s,int p){
		int m = s.length;
		int n = s[0].length;
		String[][] seats = new String[m][n];
		pid=p;
//		for(int i=0;i<m;i++){
//			for(int j=0;j<n;j++){
//				seats[i][j]=s[i][j];//锟斤拷锟斤拷锟斤拷锟斤拷荼锟�
//			}
//		}
		for(int k=0;k<m;k++){
			if(Integer.parseInt(s[k][1])==pid){
				jetton=Integer.parseInt(s[k][2]);//锟斤拷取锟皆硷拷剩锟斤拷亩慕锟�
				money=Integer.parseInt(s[k][3]);//锟斤拷取锟皆硷拷剩锟斤拷锟角�
			}
		}
	}
	private void danger_control(int bet){//危锟秸匡拷锟斤拷
		if(jetton<=100)
			needfold=true;
		safety=bet/(double)money;
	}
	private String action(int a,int b){//锟斤拷应锟斤拷锟斤拷
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
	public void e_hold(Poker[] p){//锟矫碉拷锟皆硷拷锟斤拷锟斤拷锟斤拷
		pokes[0]=p[0];
		pokes[1]=p[1];
		sort(pokes); //锟斤拷锟斤拷锟斤拷锟斤拷
	}
	public String inquire1(String[][] s){
		lastjetton=Integer.parseInt(s[0][3]);//锟斤拷一锟揭碉拷锟斤拷注锟斤拷
		danger_control(lastjetton);
		String act = "";
		if(needfold==true){//危锟秸匡拷锟斤拷
			act=action(fold,0);
			return act;
		}
		if(pokes[0].getnum()==pokes[1].getnum()){//锟斤拷锟斤拷锟揭伙拷锟斤拷锟斤拷锟�
			if(pokes[0].getnum()<10){//锟斤拷锟斤拷小锟斤拷10
				if(safety>n_safe)
					act=action(fold,0);
				else if(lastjetton<safebet)
					act=action(call,0);
				else 
					act=action(fold,0);
			}else if(pokes[0].getnum()>10){//锟斤拷锟接达拷锟斤拷10
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
		else if(Math.abs(pokes[0].getnum()-pokes[1].getnum())==1 || pokes[0].gettype()==pokes[1].gettype()){//锟斤拷锟斤拷锟斤拷锟斤拷诨锟斤拷锟酵拷锟�
			if(lastjetton>safebet)
				act=action(fold,0);
			else
				act=action(call,0);
			return act;
		}
		else{//锟斤拷锟绞裁匆裁伙拷锟�
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
		state=Checkstate.checkState(pokes);//锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
		int[] st=Pub_check.check(pub_pokes);//锟叫断癸拷锟斤拷锟狡讹拷锟斤拷锟斤拷锟斤拷锟斤拷
		lastjetton=Integer.parseInt(s[0][3]);//锟斤拷一锟揭碉拷锟斤拷注锟斤拷
		danger_control(lastjetton);
		String act = "";
		if(needfold==true){//危锟秸匡拷锟斤拷
			act=action(fold,0);
			return act;
		}
		if(state>5){//锟斤拷锟斤拷锟斤拷
			if(state==6){//锟斤拷锟斤拷+锟斤拷锟斤拷
				if(st[0]==Pub_check.santiao || st[0]==Pub_check.duizi){
					if(lastjetton<safebet){
						act=action(call,0);
					}
					else{
						act=action(fold,0);
					}
				}
			}
			else if(state==7){//炸锟斤拷
				if(st[0]==Pub_check.zhadan){//锟斤拷锟秸拷锟饺拷诠锟斤拷锟斤拷贫锟斤拷锟斤拷锟�
					if(lastjetton<safebet){
						act=action(call,0);
					}
					else{
						act=action(fold,0);
					}
				}
				else if(st[0]==Pub_check.santiao || st[0]==Pub_check.duizi){//锟斤拷锟秸拷锟斤拷锟斤拷锟斤拷呕锟斤拷锟斤拷锟斤拷锟斤拷诠锟斤拷锟斤拷哦锟斤拷锟斤拷锟�
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
		
		
		else if(state==5){//同锟斤拷
			if(lastjetton<jetton/3){
				act=action(raise,jetton/3);
			}
			else{
				act=action(call,0);
			}
		}
		
		
		else if(state==4){//锟斤拷锟斤拷锟剿筹拷锟�
			if(pokes[0].getnum()>=12){//锟斤拷锟斤拷锟斤拷拼锟斤拷锟絁
				if(lastjetton<jetton/3){
					act=action(raise,jetton/3);
				}
				else{
					act=action(call,0);
				}
			}
			else{
				if(lastjetton<jetton/3){
					act=action(raise,safebet);//raise锟斤拷微锟劫碉拷
				}
				else{
					act=action(call,0);
				}
			}
		}
		
		else if(state==3){//锟斤拷锟斤拷
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
		
		
		else if(state==2 || state==1){//锟斤拷锟斤拷锟接伙拷锟斤拷一锟斤拷锟斤拷
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
		
		
		else if(state==0){//什么也没锟斤拷
			if(pub_pokes[3]==null){//锟斤拷锟斤拷贫锟斤拷锟斤拷锟街伙拷锟斤拷锟斤拷锟斤拷锟�锟斤拷锟斤拷锟叫碉拷锟狡硷拷锟斤拷锟斤拷锟斤拷锟侥革拷锟斤拷色锟斤拷同
				if(Checkstate.checkprestate(pokes)==Checkstate.ZHUNTONGHUA || Checkstate.checkprestate(pokes)==Checkstate.ZHUNSHUNZI){
					act=action(call,0);
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
	
	//每一锟街斤拷锟截凤拷锟斤拷锟街达拷械锟斤拷锟斤拷锟�
	public void e_potwin(){
		for(int i =0;i<7;i++){
			pokes[i]=null;
			pub_pokes[i]=null;
		}
		needfold=false;
	}
	private void sort(Poker[] p){//锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷写哟锟叫★拷锟斤拷锟斤拷锟�
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
	private void swap(Poker p1,Poker p2){//锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
		Poker p3 = new Poker(p1.getnum(),p1.gettype());
		p1.setnum(p2.getnum());p1.settype(p2.gettype());
		p2.setnum(p3.getnum());p2.settype(p3.gettype());
	}
	
	public void out(){//锟斤拷锟斤拷锟斤拷锟� 锟斤拷锟斤拷使锟斤拷
		for(int i=0;i<7;i++){
			if(pokes[i]!=null)
				System.out.print(pokes[i].getnum()+","+pokes[i].gettype()+"  ");
		}
	}
}
