public class Checkstate {
	/**
	 * ���ƣ���ɢ��
	 */
	final public  static int GAOPAI = 0;
	/**
	 * ���ӣ�������ֻ��һ������
	 */
	final public static int DUIZI = 1;
	/**
	 * �����ӣ�������ֻ����������
	 */
	//�����ӣ�������ֻ����������
	final public  static int TWODUIZI = 2;
	/**
	 * �������������һ�����ֵ����
	 */
	final public static int SANTIAO = 3;
	/**
	 * ˳��
	 */
	final public  static int SHUNZI = 4;
	/**
	 *ͬ��
	 */
	final public static int TONGHUA = 5;
	/**
	 *��«������һ��������һ������
	 */
	final public  static int HULU = 6;
	/**
	 * �����������ĸ�һ����ֵ����
	 */
	final public static int SITIAO = 7;
	/**
	 * ͬ��˳
	 */
	final public  static int TONGHUASHUN = 8;
	/**
	 * �ʼ�ͬ��˳,��A��ͷ��ͬ��˳
	 */
	final public static int HUANJIATONGHUASHUN = 9;
	public static int checkState(Poker[] Poker){
		if(checkHUANJIATONGHUASHUN(Poker)){
			return HUANJIATONGHUASHUN;
		}
		if( checkTONGHUASHUN(Poker)){
			return TONGHUASHUN;
		}
		if(checkSITIAO(Poker)){
			return SITIAO;
		}
		if(checkHULU(Poker)){
			return HULU;
		}
		if(checkTONGHUA(Poker)){
			return TONGHUA;
		}
		if(checkSHUNZI(Poker)){
			return SHUNZI;
		}
		if(checkSANTIAO(Poker)){
			return SANTIAO;
		}
		if(checkTWODUIZI(Poker)){
			return TWODUIZI;
		}
		if(checkDUIZI(Poker)){
			return DUIZI;
		}
		return GAOPAI;
	}
	
	public static boolean checkDUIZI(Poker[] Poker){
		boolean isDUIZI = false; 
		if( Poker.length < 2){
			return false;
		}
		for(int i=0; i<Poker.length-1; i++){
			if(Poker[i].getnum() == Poker[i+1].getnum()){
				isDUIZI = true;
			}
		}
		return isDUIZI;
	}
	
	public static boolean checkTWODUIZI(Poker[] Poker){
		boolean isTWODUIZI = false;
		if(!checkDUIZI(Poker) || Poker.length < 4){
			return false;
		}
		for(int i=0; i<Poker.length-1; i++){
			if(Poker[i].getnum() == Poker[i+1].getnum()){
				for(i=i+2;i<Poker.length-1;i++){
					if(Poker[i].getnum() == Poker[i+1].getnum()){
						isTWODUIZI = true;
					}
				}
			}
		}
		return isTWODUIZI;
	}
	
	public static boolean checkSANTIAO(Poker[] Poker){
		boolean isSANTIAO = false;
		if(!checkDUIZI(Poker) ||Poker.length < 3){
			return false;
		}
		for(int i=0; i<Poker.length-2; i++){
			if(Poker[i].getnum() == Poker[i+1].getnum() && Poker[i+1].getnum() == Poker[i+2].getnum()){
				isSANTIAO = true;
			}
		}
		return isSANTIAO;
	}
	
	public static boolean checkSHUNZI(Poker[] Poker){
		boolean isSHUNZI = false;
		if(Poker.length < 5){
			return false;
		}
		for(int i=0; i<Poker.length-4; i++){
			if(Poker[i].getnum() == (Poker[i+1].getnum() + 1) && 
					Poker[i].getnum() == (Poker[i+2].getnum() + 2) && 
					Poker[i].getnum() == (Poker[i+3].getnum() + 3) && 
					Poker[i].getnum() == (Poker[i+4].getnum() + 4)){
				isSHUNZI = true;
			}
		}
		return isSHUNZI;
	}
	
	public  static boolean checkTONGHUA(Poker[] Poker){
		boolean isTONGHUA = false;
		if(Poker.length < 5){
			return false;
		}
		for(int i=0; i<Poker.length-4; i++){
			if(Poker[i].gettype() == Poker[i+1].gettype() && 
					Poker[i].gettype() == Poker[i+2].gettype() && 
					Poker[i].gettype() == Poker[i+3].gettype() && 
					Poker[i].gettype() == Poker[i+4].gettype()){
				isTONGHUA = true;
			}
		}
		return isTONGHUA;
	}

	public  static boolean checkHULU(Poker[] Poker){
		boolean isHULU = false;
		if(!checkDUIZI(Poker) || !checkSANTIAO(Poker) || Poker.length < 5){
			return false;
		}
		Poker[]  temp = new Poker[Poker.length-3];
		for(int i=0, j=0; i<Poker.length-2; i++){
			if(!(Poker[i].getnum() == Poker[i+1].getnum() && Poker[i+1].getnum() == Poker[i+2].getnum())){
				temp[j] = Poker[i];
				j++;
			}
		}
		isHULU = checkDUIZI(temp);
		return isHULU;
	}
	
	public static boolean checkSITIAO(Poker[] Poker){
		boolean isSITIAO = false;
		if(!checkDUIZI(Poker) || !checkSANTIAO(Poker) || Poker.length < 4){
			return false;
		}
		for(int i=0; i<Poker.length-4; i++){
			if(Poker[i].getnum() == Poker[i+1].getnum() && 
					Poker[i].getnum() == Poker[i+2].getnum() && 
					Poker[i].getnum() == Poker[i+3].getnum()){
				isSITIAO = true;
			}
		}
		return isSITIAO;
	}
	
	public static boolean checkTONGHUASHUN(Poker[] Poker){
		boolean isTONGHUASHUN = false;
		if(!checkTONGHUA(Poker)|| !checkSHUNZI(Poker) || Poker.length < 4){
			return false;
		}
		for(int i=0; i<Poker.length-4; i++){
			if(Poker[i].gettype() == Poker[i+1].gettype() && 
					Poker[i].gettype() == Poker[i+2].gettype() && 
					Poker[i].gettype() == Poker[i+3].gettype() && 
					Poker[i].gettype() == Poker[i+4].gettype()){
				if(Poker[i].getnum() == (Poker[i+1].getnum() + 1) && 
						Poker[i].getnum() == (Poker[i+2].getnum() + 2) && 
						Poker[i].getnum() == (Poker[i+3].getnum() + 3) && 
						Poker[i].getnum() == (Poker[i+4].getnum() + 4)){
					isTONGHUASHUN = true;
				}
			}
		}
		return isTONGHUASHUN;
	}
	
	public  static boolean checkHUANJIATONGHUASHUN(Poker[] Poker){
		boolean isHUANJIATONGHUASHUN = false;
		if(!checkTONGHUA(Poker)|| !checkSHUNZI(Poker) || Poker.length < 4){
			return false;
		}
		for(int i=0; i<Poker.length-4; i++){
			if(Poker[i].gettype() == Poker[i+1].gettype() && 
					Poker[i].gettype() == Poker[i+2].gettype() && 
					Poker[i].gettype() == Poker[i+3].gettype() && 
					Poker[i].gettype() == Poker[i+4].gettype()){
				if(Poker[i].getnum() == (Poker[i+1].getnum() + 1) && 
						Poker[i].getnum() == (Poker[i+2].getnum() + 2) && 
						Poker[i].getnum() == (Poker[i+3].getnum() + 3) && 
						Poker[i].getnum() == (Poker[i+4].getnum() + 4)){
					if(Poker[i].getnum() == 1){
						isHUANJIATONGHUASHUN = true;
					}
				}
			}
		}
		return isHUANJIATONGHUASHUN;
	}
}
