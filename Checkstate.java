package run;

public class Checkstate {

	/**
	 * 楂樼墝锛屽嵆鏁ｇ墝
	 */
	final public  static int GAOPAI = 0;
	
	/**
	 * 瀵瑰瓙锛屽嵆鏈変笖鍙湁涓�釜瀵瑰瓙
	 */
	final public static int DUIZI = 1;
	
	/**
	 * 涓ゅ瀛愶紝鍗虫湁涓斿彧鏈変袱涓瀛�
	 */
	final public  static int TWODUIZI = 2;
	/**
	 * 涓夋潯锛屽嵆鏈変笁涓竴鏍风殑鏁板�鐨勭墝
	 */
	final public static int SANTIAO = 3;
	/**
	 * 椤哄瓙
	 */
	final public  static int SHUNZI = 4;
	/**
	 *鍚岃姳
	 */
	final public static int TONGHUA = 5;
	/**
	 *钁姦锛屽嵆鏈変竴涓笁鏉″拰涓�釜瀵瑰瓙
	 */
	final public  static int HULU = 6;
	
	/**
	 * 鍥涙潯锛屽嵆鏈夊洓涓竴鏍锋暟鍊肩殑鐗�
	 */
	final public static int SITIAO = 7;
	
	/**
	 * 鍚岃姳椤�
	 */
	final public  static int TONGHUASHUN = 8;
	
	/**
	 * 鐨囧鍚岃姳椤�鍗矨寮�ご鐨勫悓鑺遍『
	 */
	final public static int HUANJIATONGHUASHUN = 9;
	/**
	 * 鍑嗗悓鑺憋紝鍥涘紶鑺辫壊涓�牱
	 */
	final public static int ZHUNTONGHUA = -1;
	/**
	 * 鍑嗛『瀛愶紝鍥涘紶鍗曡繛
	 */
	final public static int ZHUNSHUNZI = -2;
	
	public static int checkState(Poker[] poker){
		if(checkHUANJIATONGHUASHUN(poker)){
			return HUANJIATONGHUASHUN;
		}
		if( checkTONGHUASHUN(poker)){
			return TONGHUASHUN;
		}
		if(checkSITIAO(poker)){
			return SITIAO;
		}
		if(checkHULU(poker)){
			return HULU;
		}
		if(checkTONGHUA(poker)){
			return TONGHUA;
		}
		if(checkSHUNZI(poker)){
			return SHUNZI;
		}
		if(checkSANTIAO(poker)){
			return SANTIAO;
		}
		if(checkTWODUIZI(poker)){
			return TWODUIZI;
		}
		if(checkDUIZI(poker)){
			return DUIZI;
		}
		return GAOPAI;
	}
	
	public static int checkprestate(Poker[] poker){
		if(checkZHUNTONGHUA(poker)){
			return ZHUNTONGHUA;
		}
		if(checkZHUNSHUNZI(poker)){
			return ZHUNSHUNZI;
		}
		return GAOPAI;
	}
	public static boolean checkDUIZI(Poker[] poker){
		boolean isDUIZI = false; 
		int len = poker.length;
		if( len < 2){
			return false;
		}
		for(int i=0; i<len-1; i++){
			if(poker[i].getnum() == poker[i+1].getnum()){
				isDUIZI = true;
			}
		}
		return isDUIZI;
	}
	
	public static  boolean checkTWODUIZI(Poker[] poker){
		boolean isTWODUIZI = false;
		int len = poker.length;
		if(!checkDUIZI(poker) || len < 4){
			return false;
		}
		for(int i=0; i<len-1; i++){
			if(poker[i].getnum() == poker[i+1].getnum()){
				for(i=i+2;i<len-1;i++){
					if(poker[i].getnum() == poker[i+1].getnum()){
						isTWODUIZI = true;
					}
				}
			}
		}
		return isTWODUIZI;
	}
	
	public static boolean checkSANTIAO(Poker[] poker){
		boolean isSANTIAO = false;
		int len = poker.length;
		if(!checkDUIZI(poker) ||len < 3){
			return false;
		}
		for(int i=0; i<len-2; i++){
			if(poker[i].getnum() == poker[i+1].getnum() && poker[i+1].getnum() == poker[i+2].getnum()){
				isSANTIAO = true;
			}
		}
		return isSANTIAO;
	}
	
	public static boolean checkSHUNZI(Poker[] poker){
		boolean isSHUNZI = false;
		int len = poker.length;
		if(len < 5){
			return false;
		}
		for(int i=0; i<len-4; i++){
			if((poker[i].getnum() == (poker[i+1].getnum() + 1) && 
					poker[i].getnum() == (poker[i+2].getnum() + 2) && 
					poker[i].getnum() == (poker[i+3].getnum() + 3) && 
					poker[i].getnum() == (poker[i+4].getnum() + 4)) ||
					(poker[i].getnum() == 4 && 
					poker[i-1].getnum() == 3 && 
					poker[i-2].getnum() == 2 && 
					poker[len-1].getnum() == 14)){
				isSHUNZI = true;
			}
		}
		return isSHUNZI;
	}
	
	public static boolean checkTONGHUA(Poker[] poker){
		boolean isTONGHUA = false;
		int len = poker.length;
		if(len < 5){
			return false;
		} 
		for(int i=0,count=0; i<len; i++){
			for(int j=0; j<len; j++){
				if(poker[i].gettype() == poker[j].gettype()){
					count ++;
				}
			}
			if(count == 5){
				isTONGHUA = true;
				break;
			}
			count = 0;
		}
		return isTONGHUA;
	}

	public static boolean checkHULU(Poker[] poker){
		boolean isHULU = false;
		int len = poker.length;
		if(!checkDUIZI(poker) || !checkSANTIAO(poker) || len < 5){
			return false;
		}
		Poker[]  temp = new Poker[len-3];
		for(int i=0, j=0; i<len-2; i++){
			if(!(poker[i].getnum() == poker[i+1].getnum() && poker[i+1].getnum() == poker[i+2].getnum())){
				temp[j] = poker[i];
				j++;
			}
		}
		isHULU = checkDUIZI(temp);
		return isHULU;
	}
	
	public static boolean checkSITIAO(Poker[] poker){
		boolean isSITIAO = false;
		int len = poker.length;
		if(!checkDUIZI(poker) || !checkSANTIAO(poker) || len < 4){
			return false;
		}
		for(int i=0; i<len-4; i++){
			if(poker[i].getnum() == poker[i+1].getnum() && 
					poker[i].getnum() == poker[i+2].getnum() && 
					poker[i].getnum() == poker[i+3].getnum()){
				isSITIAO = true;
			}
		}
		return isSITIAO;
	}
	
	public static boolean checkTONGHUASHUN(Poker[] poker){
		boolean isTONGHUASHUN = false;
		int len = poker.length;
		if(!checkTONGHUA(poker)|| !checkSHUNZI(poker) || len < 4){
			return false;
		}
		for(int i=0; i<len-4; i++){
			if(poker[i].gettype() == poker[i+1].gettype() && 
					poker[i].gettype() == poker[i+2].gettype() && 
					poker[i].gettype() == poker[i+3].gettype() && 
					poker[i].gettype() == poker[i+4].gettype()){
				if((poker[i].getnum() == (poker[i+1].getnum() + 1) && 
						poker[i].getnum() == (poker[i+2].getnum() + 2) && 
						poker[i].getnum() == (poker[i+3].getnum() + 3) && 
						poker[i].getnum() == (poker[i+4].getnum() + 4)) ||
						(poker[i].getnum() == 4 && 
						poker[i+1].getnum() == 3 && 
						poker[i+2].getnum() == 2 && 
						poker[len-1].getnum() == 14)){
					isTONGHUASHUN = true;
				}
			}
		}
		return isTONGHUASHUN;
	}
	
	public static boolean checkHUANJIATONGHUASHUN(Poker[] poker){
		boolean isHUANJIATONGHUASHUN = false;
		int len = poker.length;
		if(!checkTONGHUA(poker)|| !checkSHUNZI(poker) || len < 4){
			return false;
		}
		for(int i=0; i<len-4; i++){
			if(poker[i].gettype() == poker[i+1].gettype() && 
					poker[i].gettype() == poker[i+2].gettype() && 
					poker[i].gettype() == poker[i+3].gettype() && 
					poker[i].gettype() == poker[i+4].gettype()){
				if(poker[i].getnum() == (poker[i+1].getnum() + 1) && 
						poker[i].getnum() == (poker[i+2].getnum() + 2) && 
						poker[i].getnum() == (poker[i+3].getnum() + 3) && 
						poker[i].getnum() == (poker[i+4].getnum() + 4)){
					if(poker[i].getnum() == 14){
						isHUANJIATONGHUASHUN = true;
					}
				}
			}
		}
		return isHUANJIATONGHUASHUN;
	}
	
	public static boolean checkZHUNTONGHUA(Poker[] poker){
		boolean isZHUNTONGHUA = false;
		int len = poker.length;
		if(len < 5 || len > 6){
			return false;
		}
		for(int i=0,count=0; i<len; i++){
			for(int j=0; j<len; j++){
				if(poker[i].gettype() == poker[j].gettype()){
					count ++;
				}
			}
			if(count == 4){
				isZHUNTONGHUA = true;
				break;
			}
			count = 0;
		}
		return isZHUNTONGHUA;
	}
	
	public static boolean checkZHUNSHUNZI(Poker[] poker){
		boolean isZHUNSHUNZI = false;
		int len = poker.length;
		if(len < 5 || len > 6){
			return false;
		}
		for(int i=0; i<len; i++){
			if((poker[i].getnum() == (poker[i+1].getnum() + 1) && 
					poker[i].getnum() == (poker[i+2].getnum() + 2) && 
					poker[i].getnum() == (poker[i+3].getnum() + 3) ) ||
					(poker[i+1].getnum() == 3 && 
					poker[i+2].getnum() == 2 && 
					poker[len-1].getnum() == 14)){
				isZHUNSHUNZI = true;
			}
		}
		return isZHUNSHUNZI;
	}
}
