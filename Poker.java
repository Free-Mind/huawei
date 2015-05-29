public class Poker {
	final public static int hearts=3;
	final public static int spades=0;
	final public static int clubs=1;
	final public static int diamonds=2;
	private int num;
	private int type;
	public Poker(int num,int type){
		this.num=num;
		this.type=type;
	}
	public int getnum(){
		return num;
	}
	public void setnum(int no){
		num=no;
	}
	public int gettype(){
		return type;
	}
	public void settype(int no){
		type=no;
	}
}
