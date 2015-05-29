public class Pub_check {
	final public static int nothing=0;
	final public static int duizi=1;
	final public static int santiao=2;
	final public static int zhadan=3;
	final public static int sitonghua=4;
	public static int[] check(Poker[] p){
		int[] tmp = new int[2];
		if (p.length==3){
			if(issantiao(p)){
				tmp[0]=santiao;
				tmp[1]=p[0].getnum();
			}
			else if(isduizi(p)){
				tmp[0]=duizi;
				tmp[1]=p[1].getnum();
			}
			else{
				tmp[0]=nothing;
				tmp[1]=nothing;
			}
		}
		else if (p.length==4 || p.length==5){
			if(iszhadan(p)){
				tmp[0]=zhadan;
				tmp[1]=p[1].getnum();
			}
			else if(istonghua(p)){
				tmp[0]=sitonghua;
				if(p[0].gettype()==p[1].gettype()){
					tmp[1]=p[0].gettype();
				}
				else if(p[1].gettype()==p[2].gettype()){
					tmp[1]=p[1].gettype();
				}
				else{
					tmp[1]=p[0].gettype();
				}
			}
			else if(issantiao(p)){
				tmp[0]=santiao;
				tmp[1]=p[2].getnum();
			}
			else if(isduizi(p)){
				tmp[0]=duizi;
				if(p.length==4){
					if(p[0].getnum()==p[1].getnum()){
						tmp[1]=p[0].getnum();
					}
					else if(p[1].getnum()==p[2].getnum()){
						tmp[1]=p[1].getnum();
					}
					else if(p[2].getnum()==p[3].getnum()){
						tmp[1]=p[2].getnum();
					}
				}
				else{
					if(p[0].getnum()==p[1].getnum()){
						tmp[1]=p[0].getnum();
					}
					else if(p[1].getnum()==p[2].getnum()){
						tmp[1]=p[1].getnum();
					}
					else if(p[2].getnum()==p[3].getnum()){
						tmp[1]=p[2].getnum();
					}
					else if(p[3].getnum()==p[4].getnum()){
						tmp[1]=p[3].getnum();
					}
				}
			}
			else{
				tmp[0]=nothing;
				tmp[1]=nothing;
			}
		}
		return tmp;
	}
	public static boolean isduizi(Poker[] p){
		int l = p.length;
		if(l<2) return false;
		for(int i=0;i<l-1;i++){
			if(p[i].getnum()==p[i+1].getnum())
				return true;
		}
		return false;
	}
	public static boolean issantiao(Poker[] p){
		int l = p.length;
		if (l<3) return false;
		for (int i=0;i<l-2;i++){
			if(p[i].getnum()==p[i+1].getnum() && p[i+1].getnum()==p[i+2].getnum())
				return true;
		}
		return false;
	}
	public static boolean iszhadan(Poker[] p){
		int l = p.length;
		if (l<4) return false;
		for (int i=0;i<l-3;i++){
			if(p[i].getnum()==p[i+1].getnum() && p[i+1].getnum()==p[i+2].getnum() && p[i+2].getnum()==p[i+3].getnum())
				return true;
		}
		return false;
	}
	public static boolean istonghua(Poker[] p){
		int l = p.length;
		if (l<4) return false;
		if(l==4){
			if(p[0].gettype()==p[1].gettype() && p[1].gettype()==p[2].gettype() && p[2].gettype()==p[3].gettype() )
				return true;
			else
				return false;
		}
		else if(l==5){
			int[] s = new int[4];
			for(int i=0;i<4;i++){
				s[i]=p[0].gettype()-p[i+1].gettype();
			}
			if(s[0]==s[1] && s[0]==s[2] && s[0]==s[3])
				return true;
			else{
				int count=0;
				for(int j=0;j<4;j++){
					if(s[j]==0){
						count++;
					}
				}
				if(count==3)
					return true;
				else
					return false;
			}
		}
		return false;
	}
}
