import java.util.*;
public class Ai {
	final public static int fold=0;
	final public static int call=1;
	final public static int raise=2;
	final public static int check=3;
	Poker[] pokes = new Poker[7];//锟斤拷锟斤拷锟斤拷
	Poker[] pub_pokes = new Poker[5];//锟斤拷锟斤拷锟斤拷
	Poker[] self_pokes = new Poker[2];
	//String[][] seats;//锟斤拷锟斤拷锟捷憋拷
	int pid=0;//锟皆硷拷锟斤拷ID
	int jetton=0,money=0,lastjetton=0;//jetton锟皆硷拷剩锟斤拷亩慕锟�money锟皆硷拷剩锟斤拷锟角� lastjetton锟较家碉拷锟斤拷注锟斤拷
	boolean needfold=false;//锟借不锟斤拷要直锟斤拷fold
	double safety=0;//锟斤拷注锟斤拷全值 系锟斤拷
	double n_safe=0.15;//锟斤拷全值系锟斤拷 锟轿匡拷锟斤拷小
	int safebet=0,total=0,bigblind=0;
	int state=0;//0-9锟斤拷示锟狡碉拷锟斤拷锟酵达拷小
	public static int MIN_SAFETY;
	public static double SAFETY_FIGURE; 
	public static double MAX_RAISE;
	public static double RAISE_FIGURE;
	final public static double POOREST = 0.2;
	final public static double POOR = 0.4;
	final public static double GENERAL = 0.6;
	final public static double MIDDLERICH = 0.8;
	final public static double RICH = 1;
	
	final public  static int GAOPAI = Checkstate.GAOPAI;

	final public static int DUIZI = Checkstate.DUIZI;

	final public  static int TWODUIZI = Checkstate.TWODUIZI;

	final public static int SANTIAO = Checkstate.SANTIAO;

	final public  static int SHUNZI = Checkstate.SHUNZI;

	final public static int TONGHUA = Checkstate.TONGHUA;

	final public  static int HULU = Checkstate.HULU;
	
	final public static int SITIAO = Checkstate.SITIAO;
	
	final public  static int TONGHUASHUN = Checkstate.TONGHUASHUN;
	
	final public static int HUANJIATONGHUASHUN = Checkstate.HUANJIATONGHUASHUN;
	
	final public static int ZHUNTONGHUASHUN = Checkstate.ZHUNTONGHUASHUN;

	final public static int ZHUNTONGHUA = Checkstate.ZHUNTONGHUA;

	final public static int ZHUNSHUNZI = Checkstate.ZHUNSHUNZI;
	
	final public static int THREETONGHUA = Checkstate.THREETONGHUA;
	
	final public static int THREESHUNZI = Checkstate.THREESHUNZI;
	
	int startjetton=0,starttotal=0,count=0;
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
			if(s[k]!=null){
				if(Integer.parseInt(s[k][1])==pid){
					jetton=Integer.parseInt(s[k][2]);//锟斤拷取锟皆硷拷剩锟斤拷亩慕锟�
					money=Integer.parseInt(s[k][3]);//锟斤拷取锟皆硷拷剩锟斤拷锟角�
				}
			}
		}
		total=jetton+money;
		if(count==0){
			startjetton=jetton;
			starttotal=total;
			MAX_RAISE = startjetton;
			SAFETY_FIGURE = startjetton/28;
			RAISE_FIGURE = startjetton/14;
			count=1;
		}
	}
	private void danger_control(int bet){//危锟秸匡拷锟斤拷
		if(total<=2000)
			needfold=true;
		safety=bet/(double)jetton;
	}
	private String action(int a,int b){//锟斤拷应锟斤拷锟斤拷
		String str = "";
		if(a==0)
			str="fold\n";
		else if(a==1)
			str="call\n";
		else if(a==2)
			str="raise "+b+"\n";
		else if(a==4)
			str="check\n";
		return str;
	}
	public void e_blind(String[][] s){
		if(s[1]!=null){
			safebet=Integer.parseInt(s[1][1])*3;
			bigblind=Integer.parseInt(s[1][1]);
			
		}
		else{
			safebet=Integer.parseInt(s[0][1])*6;
			bigblind=Integer.parseInt(s[0][1])*2;
		}
		MIN_SAFETY = bigblind * 5;
	}
	public void e_hold(Poker[] p){//锟矫碉拷锟皆硷拷锟斤拷锟斤拷锟斤拷
		pokes[0]=p[0];self_pokes[0]=p[0];
		pokes[1]=p[1];self_pokes[1]=p[1];
		sort(pokes); sort(self_pokes);//锟斤拷锟斤拷锟斤拷锟斤拷
	}
	
	
	/**
	 * 得到轮到自己的时候需要下的赌金,call时需要下的赌金
	 * @param s   
	 * @return
	 */
	public int getBet(String[][] s){
		int b=0;
		for(int i=0;i<s.length;i++){
			if(s[i]!=null && s[i].length==5){
				if(b<Integer.parseInt(s[i][3])){
					b=Integer.parseInt(s[i][3]);
				}
			}
		}
		return b;
	}
	
	/**
	 * 得到公共牌堆的牌
	 * @return
	 */
	public Poker[] getPubPokes(){
		Poker[] pub_pokes1 = null;
		int count=0;
		for(int i=0;i<pub_pokes.length;i++){
			if(pub_pokes[i]==null){
				break;
			}
			else{
				count++;
			}
		}
		pub_pokes1 = new Poker[count];
		for(int j=0;j<count;j++){
			pub_pokes1[j]=pub_pokes[j];
		}
		return pub_pokes1;
	}
	
	public Poker[] getSelfPokes(){
		return self_pokes;
	}
	
	public Poker[] getPokes(){
		Poker[] pokes1 = null;
		int count=0;
		for(int i=0;i<pokes.length;i++){
			if(pokes[i]==null){
				break;
			}
			else{
				count++;
			}
		}
		pokes1 = new Poker[count];
		for (int j=0;j<count;j++){
			pokes1[j]=pokes[j];
		}
		return pokes1;
	}
	
	/**
	 * 实现安全轧值的动态更新
	 * @param state   当前状态
	 * @param max_num   最大牌的值
	 * @param second_num 第二大牌的值
	 * @param min_safety   设定安全轧值的基数
	 * @param safety_figure     可设置轧值系数
	 * @return 安全轧值
	 */
	public int getSafety(int state, Poker[] self_pokes){
		double richstate = getRichState();
		int max_num = self_pokes[0].getnum();
		int second_num = self_pokes[1].getnum();
		int minSafety = (int)(MIN_SAFETY * richstate);
		if(minSafety > jetton){
			minSafety = jetton;
		}
		int safety = 0;
		if(state == GAOPAI){
			if(max_num<10){
				return 0;
			}else if(second_num<10){
				safety = minSafety;
			}else if(second_num >= 10){
				safety = (int) (jetton*SAFETY_FIGURE*second_num);
			}
		}else if(state ==  DUIZI){
			if(max_num == 14){
				safety = jetton;
			}else if(max_num == 13){
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}else{
				safety = (int) (jetton*SAFETY_FIGURE*max_num + minSafety);
			}
			
		}
		return safety;
	}
	
	public int getSafety(Poker[] pokers, Poker[] pub_pokers, Poker[] self_pokers){
		double richstate = getRichState();
		int minSafety = (int)(MIN_SAFETY * richstate);
		if(minSafety > jetton){
			minSafety = jetton;
		}
		int safety = 0;
		int state = Checkstate.checkState(pokers);
		int pub_state = Checkstate.checkState(pub_pokers);
		int pub_prestate = Checkstate.checkprestate(pub_pokers);
		int prestate = Checkstate.checkprestate(pokers);
		switch(state){
		case HUANJIATONGHUASHUN:
			safety = jetton;
		case TONGHUASHUN:
			if(pub_state == TONGHUASHUN){
				safety = 0;
			}else if(pub_prestate != ZHUNTONGHUASHUN){
				safety = jetton;
			}else{
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}
			break;
		case SITIAO:
			if(pub_state == SITIAO){
				if(self_pokers[0].getnum() > 12){
					safety = (int) (jetton * SAFETY_FIGURE * (self_pokers[0].getnum() - 12));
				}else{
					safety = 0;
				}
			}else if(pub_state != SANTIAO){
				safety = jetton;
			}else{
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}
			break;
		case HULU:
			if(pub_state == HULU){
				safety = 0;
			}else if(pub_state != SANTIAO){
				safety = jetton;
			}else{
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}
			break;
		case TONGHUA:
			if(pub_state == TONGHUA){
				safety = 0;
			}else if(pub_prestate!=ZHUNTONGHUASHUN || pub_prestate!=ZHUNTONGHUA || pub_state != SANTIAO || pub_state != DUIZI){
				safety = jetton;
			}else if(pub_prestate == ZHUNTONGHUASHUN){
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}else if(pub_prestate == SANTIAO){
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}else if(pub_state == ZHUNTONGHUA){
				safety = jetton;
			}else if(pub_state == DUIZI){
				safety = jetton;
			}else{
				safety = jetton;
			}
			break;
		case SHUNZI:
			if(pub_state == SHUNZI){
				safety = 0;
			}else if(pub_prestate!=ZHUNTONGHUASHUN ||pub_prestate != ZHUNTONGHUA || pub_prestate != ZHUNSHUNZI || pub_state != SANTIAO || pub_state != TWODUIZI || pub_state != DUIZI){
				safety = jetton;
			}else if(pub_prestate == ZHUNTONGHUASHUN){
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}else if(pub_prestate == ZHUNTONGHUA){
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}else if(pub_state == SANTIAO){
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}else if(pub_prestate == TWODUIZI){
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}else if(pub_prestate == ZHUNSHUNZI){
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}else if(pub_state == DUIZI){
				safety = jetton;
			}else{
				safety = jetton;
			}
			break;
		case SANTIAO:
			if(pub_state == SANTIAO){
				if(self_pokers[0].getnum() == 14){
					safety = jetton;
				}else if(self_pokers[0].getnum() == 13){
					safety = jetton/2>minSafety?jetton/2:minSafety;
				}else{
					safety = 0;
				}
			}else if(pub_prestate == ZHUNTONGHUASHUN){
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}else if(pub_state == TWODUIZI){
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}else if(pub_prestate == ZHUNTONGHUA){
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}else if(pub_prestate == ZHUNSHUNZI){
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}else if(pub_state == DUIZI){
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}else{
				safety = jetton;
			}
			break;
		case TWODUIZI:
			if(pub_state == TWODUIZI){
				if(self_pokers[0].getnum() == 14){
					safety = jetton;
				}else if(self_pokers[0].getnum() == 13){
					safety = jetton/2>minSafety?jetton/2:minSafety;
				}else{
					safety = 0;
				}
			}else if(pub_prestate == ZHUNTONGHUASHUN){
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}else if(pub_prestate == ZHUNTONGHUA){
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}else if(pub_prestate == ZHUNSHUNZI){
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}else if(pub_state == DUIZI){
				safety = jetton/2>minSafety?jetton/2:minSafety;
			}else{
				safety = jetton;
			}
			break;
		case DUIZI:
			if(pub_state == DUIZI){
				safety = 0;
			}else{
				int duizi_num = Checkstate.getNumDUIZI(pokers);
				if(duizi_num > 10){
					safety = (int) (MAX_RAISE*RAISE_FIGURE*duizi_num);
					if(pub_prestate == ZHUNTONGHUASHUN){
						safety = jetton/3>minSafety?jetton/3:minSafety;
					}else if(pub_prestate == ZHUNTONGHUA){
						safety = jetton/3>minSafety?jetton/3:minSafety;
					}else if(pub_prestate == ZHUNSHUNZI){
						safety = jetton/3>minSafety?jetton/3:minSafety;
					}
				}else{
					safety = 0;
				}
			}
			break;
		case GAOPAI:
			if(pokers.length == 5){
				if(prestate == ZHUNTONGHUASHUN){
					safety = jetton/3>minSafety?jetton/3:minSafety;
				}else if(prestate == ZHUNSHUNZI || prestate == ZHUNTONGHUA){
					safety = jetton/3>minSafety?jetton/3:minSafety;
				}
			}else if(pokers.length == 6){
				if(prestate == ZHUNTONGHUASHUN){
					safety = jetton/3>minSafety?jetton/3:minSafety;
				}else if(prestate == ZHUNSHUNZI || prestate == ZHUNTONGHUA){
					safety = jetton/3>minSafety?jetton/3:minSafety;
				}
			}else{
				safety = 0;
			}			
			break;
		default:
			safety = minSafety;
	}
		return safety;
	}
	
	
	/**
	 * 实现主动下注值raise的动态更新
	 * @param state  当前状态
	 * @param max_num  最大牌的值
	 * @param second_num 第二大牌的值
	 * @param bet  下注金额
	 * @param raise_figure  设置下注系数
	 * @return  主动下注值
	 */
	public int getRaise(int state, int max_num,int second_num, int bet){	
		if(bet >= MAX_RAISE){
			return 0;
		}
		int raise_num = 0;
		double richstate = getRichState();
		int max_raise = (int)(MAX_RAISE*richstate-bet);
		
		if(state == DUIZI){
			raise_num = (int) (MAX_RAISE*RAISE_FIGURE*max_num);
		}else if(state == GAOPAI){
			if(second_num > 10){
				raise_num = (int) (MAX_RAISE * RAISE_FIGURE * (second_num - 10));
			}
		}
		if(raise_num > max_raise){
			raise_num = max_raise;
		}
		return raise_num;
	}
	
	
	
	public int getRaise(Poker[] pokers, Poker[] pub_pokers, Poker[] self_pokers, int bet){
		if(bet >= MAX_RAISE){
			return 0;
		}
		int raise_num = 0;
		double richstate = getRichState();
		int max_raise = (int)(MAX_RAISE*richstate - bet);
		if(max_raise > jetton){
			max_raise = jetton;
		}
		int state = Checkstate.checkState(pokers);
		int pub_state = Checkstate.checkState(pub_pokers);
		int pub_prestate = Checkstate.checkprestate(pub_pokers);
		switch(state){
			case HUANJIATONGHUASHUN:
				return  (int)(jetton*((pokers.length)/7));
			case TONGHUASHUN:
				if(pub_state == TONGHUASHUN){
					raise_num = 0;
				}else if(pub_prestate != ZHUNTONGHUASHUN){
					return (int)(jetton*(1/(8-pokers.length)));
				}else{
					raise_num = max_raise;
				}
				break;
			case SITIAO:
				if(pub_state == SITIAO){
					if(self_pokers[0].getnum() > 12){
						raise_num = (int) (MAX_RAISE * RAISE_FIGURE * (self_pokers[0].getnum() - 12));
					}else{
						raise_num = 0;
					}
				}else if(pub_state != SANTIAO || pub_prestate != ZHUNTONGHUASHUN){
					return (int)(jetton*(1/(8-pokers.length)));
				}else{
					raise_num = max_raise;
				}
				break;
			case HULU:
				if(pub_state == HULU){
					raise_num = 0;
				}else if(pub_state != SANTIAO || pub_prestate != ZHUNTONGHUASHUN){
					return (int)(jetton*(1/(8-pokers.length)));
				}else{
					raise_num = max_raise;
				}
				break;
			case TONGHUA:
				if(pub_state == TONGHUA){
					raise_num = 0;
				}else if(pub_prestate!=ZHUNTONGHUASHUN || pub_prestate!=ZHUNTONGHUA || pub_state != SANTIAO || pub_state != DUIZI){
					return (int)(jetton*(1/(8-pokers.length)));
				}else if(pub_prestate == ZHUNTONGHUASHUN){
					raise_num = max_raise/5;
				}else if(pub_prestate == SANTIAO){
					raise_num = max_raise/4;
				}else if(pub_state == ZHUNTONGHUA){
					raise_num = max_raise/3;
				}else if(pub_state == DUIZI){
					raise_num = max_raise/2;
				}else{
					raise_num = max_raise;
				}
				break;
			case SHUNZI:
				if(pub_state == SHUNZI){
					raise_num = 0;
				}else if(pub_prestate!=ZHUNTONGHUASHUN ||pub_prestate != ZHUNTONGHUA || pub_prestate != ZHUNSHUNZI || pub_state != SANTIAO || pub_state != TWODUIZI || pub_state != DUIZI){
					return (int)(jetton*(1/(8-pokers.length)));
				}else if(pub_prestate == ZHUNTONGHUASHUN){
					raise_num = max_raise/7;
				}else if(pub_prestate == ZHUNTONGHUA){
					raise_num = max_raise/6;
				}else if(pub_state == SANTIAO){
					raise_num = max_raise/5;
				}else if(pub_prestate == TWODUIZI){
					raise_num = max_raise/4;
				}else if(pub_prestate == ZHUNSHUNZI){
					raise_num = max_raise/3;
				}else if(pub_state == DUIZI){
					raise_num = max_raise/2;
				}else{
					raise_num = max_raise;
				}
				break;
			case SANTIAO:
				if(pub_state == SANTIAO){
					if(self_pokers[0].getnum() > 12){
						raise_num = (int) (MAX_RAISE * RAISE_FIGURE * (self_pokers[0].getnum() - 12));
					}else{
						raise_num = 0;
					}
				}else if(pub_prestate == ZHUNTONGHUASHUN){
					raise_num = max_raise/6;
				}else if(pub_state == TWODUIZI){
					raise_num = max_raise/5;
				}else if(pub_prestate == ZHUNTONGHUA){
					raise_num = max_raise/4;
				}else if(pub_prestate == ZHUNSHUNZI){
					raise_num = max_raise/3;
				}else if(pub_state == DUIZI){
					raise_num = max_raise/2;
				}else{
					raise_num = max_raise;
				}
				break;
			case TWODUIZI:
				if(pub_state == TWODUIZI){
					if(self_pokers[0].getnum() > 12){
						raise_num = (int) (MAX_RAISE * RAISE_FIGURE * (self_pokers[0].getnum() - 12));
					}else{
						raise_num = 0;
					}
				}else if(pub_prestate == ZHUNTONGHUASHUN){
					raise_num = max_raise/5;
				}else if(pub_prestate == ZHUNTONGHUA){
					raise_num = max_raise/4;
				}else if(pub_prestate == ZHUNSHUNZI){
					raise_num = max_raise/3;
				}else if(pub_state == DUIZI){
					raise_num = max_raise/2;
				}else{
					raise_num = max_raise;
				}
				break;
			case DUIZI:
				if(pub_state == DUIZI){
					if(self_pokers[0].getnum() > 12){
						raise_num = (int) (MAX_RAISE * RAISE_FIGURE * (self_pokers[0].getnum() - 12));
					}else{
						raise_num = 0;
					}
				}else{
					int duizi_num = Checkstate.getNumDUIZI(pokers);
					if(duizi_num > 10){
						raise_num = (int) (MAX_RAISE*RAISE_FIGURE*duizi_num);
						if(pub_prestate == ZHUNTONGHUASHUN){
							raise_num = raise_num/5;
						}else if(pub_prestate == ZHUNTONGHUA){
							raise_num = raise_num/4;
						}else if(pub_prestate == ZHUNSHUNZI){
							raise_num = raise_num/3;
						}
					}else{
						raise_num = 0;
					}
				}
				break;
			case GAOPAI:
				if(self_pokers[0].getnum() > 12){
					raise_num = (int) (MAX_RAISE * RAISE_FIGURE * (self_pokers[0].getnum() - 12));
				}else{
					raise_num = 0;
				}
				break;
			default:
				raise_num = 0;
		}
		if(raise_num > max_raise){
			raise_num = max_raise;
		}
		return raise_num;
	}
	/**
	 * 判断现在富有状态。分5种状态:特穷，穷，一般，中产，富有，开局默认为一般
	 * @return
	 */
	public double getRichState(){
		double richstate = GENERAL;
		int standard = startjetton/5;
		if(total < standard){
			richstate = POOREST;
		}else if(total < standard * 2 ){
			richstate = POOR;
		}else if(total < standard * 5){
			richstate = GENERAL;
		}else if(total < standard * 10){
			richstate = MIDDLERICH;
		}else{
			richstate = RICH;
		}
		return richstate;
	}
	
	public String inquire1(String[][] s){
		String act = "";
		int bet = getBet(s); 
		int state = Checkstate.checkState(self_pokes);
		int safety = getSafety(state, getSelfPokes());
		int raise_num = getRaise(state, pokes[0].getnum(), pokes[1].getnum(), bet);
		if(raise_num > 0){
			act=action(raise,raise_num);
			jetton = jetton-raise_num-bet;
		}else if(safety >= bet){
			act=action(call,0);
			jetton -= bet;
		}else{
			act=action(fold,0);
		}
		return act;
	}
	public void e_flop(Poker[] p){
		pokes[2]=p[0];pub_pokes[0]=p[0];
		pokes[3]=p[1];pub_pokes[1]=p[1];
		pokes[4]=p[2];pub_pokes[2]=p[2];
		sort(pokes);sort(pub_pokes);
	}
	public String inquire2(String[][] s){
		String act = "";
		int bet = getBet(s); 
		int safety = getSafety(getPokes(),getPubPokes(),getSelfPokes());
		int raise_num = getRaise(getPokes(),getPubPokes(),getSelfPokes(), bet);
		if(raise_num > 0){
			act=action(raise,raise_num);
			jetton = jetton-raise_num-bet;
		}else if(safety >= bet){
			act=action(call,0);
			jetton -= bet;
		}else{
			act=action(fold,0);
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
			
		}
		for(int i=0;i<5;i++){
			pub_pokes[i]=null;
		}
		self_pokes[0]=null;
		self_pokes[1]=null;
		needfold=false;
	}
	private void sort(Poker[] p){//锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷写哟锟叫★拷锟斤拷锟斤拷锟�
		//Poker[] tmp = new Poker[7];
		int count=0;
		for(int i=0;i<p.length;i++){
			if(p[p.length-1]!=null){
				count=p.length;
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
