package run;

public class checkstate {

	/**
	 * 高牌，即散牌
	 */
	final public  static int GAOPAI = 0;
	
	/**
	 * 对子，即有且只有一个对子
	 */
	final public static int DUIZI = 1;
	
	/**
	 * 两对子，即有且只有两个对子
	 */
	final public  static int TWODUIZI = 2;
	/**
	 * 三条，即有三个一样的数值的牌
	 */
	final public static int SANTIAO = 3;
	/**
	 * 顺子
	 */
	final public  static int SHUNZI = 4;
	/**
	 *同花
	 */
	final public static int TONGHUA = 5;
	/**
	 *葫芦，即有一个三条和一个对子
	 */
	final public  static int HULU = 6;
	
	/**
	 * 四条，即有四个一样数值的牌
	 */
	final public static int SITIAO = 7;
	
	/**
	 * 同花顺
	 */
	final public  static int TONGHUASHUN = 8;
	
	/**
	 * 皇家同花顺,即A开头的同花顺
	 */
	final public static int HUANJIATONGHUASHUN = 9;
	/**
	 * 准同花，四张花色一样
	 */
	final public static int ZHUNTONGHUA = -1;
	/**
	 * 准顺子，四张单连
	 */
	final public static int ZHUNSHUNZI = -2;
	
	public static int checkState(poker[] poker){
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
	
	public static int checkprestate(poker[] poker){
		if(checkZHUNTONGHUA(poker)){
			return ZHUNTONGHUA;
		}
		if(checkZHUNSHUNZI(poker)){
			return ZHUNSHUNZI;
		}
		return GAOPAI;
	}
	public static boolean checkDUIZI(poker[] poker){
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
	
	public static  boolean checkTWODUIZI(poker[] poker){
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
	
	public static boolean checkSANTIAO(poker[] poker){
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
	
	public static boolean checkSHUNZI(poker[] poker){
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
	
	public static boolean checkTONGHUA(poker[] poker){
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

	public static boolean checkHULU(poker[] poker){
		boolean isHULU = false;
		int len = poker.length;
		if(!checkDUIZI(poker) || !checkSANTIAO(poker) || len < 5){
			return false;
		}
		poker[]  temp = new poker[len-3];
		for(int i=0, j=0; i<len-2; i++){
			if(!(poker[i].getnum() == poker[i+1].getnum() && poker[i+1].getnum() == poker[i+2].getnum())){
				temp[j] = poker[i];
				j++;
			}
		}
		isHULU = checkDUIZI(temp);
		return isHULU;
	}
	
	public static boolean checkSITIAO(poker[] poker){
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
	
	public static boolean checkTONGHUASHUN(poker[] poker){
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
	
	public static boolean checkHUANJIATONGHUASHUN(poker[] poker){
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
	
	public static boolean checkZHUNTONGHUA(poker[] poker){
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
	
	public static boolean checkZHUNSHUNZI(poker[] poker){
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
