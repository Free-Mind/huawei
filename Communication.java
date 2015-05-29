package run;
import java.io.*;
import java.net.*;
import java.util.*;
public class Communication {
	/*
		瀛樺偍鍚勭娑堟伅鐨勬暟缁勶細
		1.搴т綅淇℃伅锛歋tring seatMeg[8][4]
		2.鎵嬬墝淇℃伅锛歅oker holdMeg[2]
		3.inquire淇℃伅锛歋tring inquireMeg[8][5];
		5.blind娑堟伅锛歋tring blindMeg[2][2];
		6.flop娑堟伅锛歅oker flopMeg[3];
	*/
	private String seatMeg[][];
	private Poker holdMeg[];
	private String inquireMeg[][];
	private String blindMeg[][];
	private Poker flopMeg[];
	private int pid;
	
	private Socket socket;
	private BufferedReader input;
	private DataOutputStream output;
	private int localport;
	private int remoteport;
	private byte[] remoteBuf;
	private byte[] localBuf;
	
	private int inquireSym;//鐢ㄦ潵鏍囪闇�浣跨敤鍝竴娆nquire鏂规硶
	private int index;//鐢ㄦ潵鏍囪瀛樺偍娑堟伅鏁扮粍鐨勪笅鏍�
	private int state=0;//鐢ㄦ潵鏍囪寮�鎺ユ敹鏌愮娑堟伅锛�:寮�鎺ユ敹娑堟伅锛�锛氭鍦ㄦ帴鏀舵秷鎭�
	private	int choose=0;//鏍囪娑堟伅绫诲瀷
	
	Ai controller;
	
	public Communication(String args[]){
		//鍒濆鍖栫浉鍏冲弬鏁�
		controller =new Ai();
		index=0;
		remoteport=Integer.parseInt(args[1]);//鏈嶅姟鍣ㄧ鍙�
		localport=Integer.parseInt(args[3]);//鏈湴绔彛
		String[] remoteStr = args[0].split("\\.");
		String[] localStr = args[2].split("\\.");
		remoteBuf = new byte[4];
		localBuf = new byte[4];
		pid=Integer.parseInt(args[4]);
		
		for(int i = 0; i < 4; i++){
		    remoteBuf[i] = (byte)(Integer.parseInt(remoteStr[i])&0xff);
		}
		
		for(int i = 0; i < 4; i++){
		    localBuf[i] = (byte)(Integer.parseInt(localStr[i])&0xff);
		}
	}
	public void connect(){
		try{
			socket = new Socket(); 
			socket.setReuseAddress(true); 
			socket.bind(new InetSocketAddress(InetAddress.getByAddress(localBuf),localport)); 
			socket.connect(new InetSocketAddress(InetAddress.getByAddress(remoteBuf), remoteport));
			
			input=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output=new DataOutputStream(socket.getOutputStream());
			//鍙戦�娉ㄥ唽娑堟伅			
			String reg="reg: "+pid+" Bob \n";
			System.out.println(reg);
			output.write(reg.getBytes());
			output.flush();
			//鎺ュ彈鏈嶅姟鍣ㄥ彂閫佸洖鏉ョ殑娑堟伅
			try{
				while(true){
					String temp=input.readLine();
				//	System.out.println("鏈嶅姟鍣ㄨ繑鍥�"+temp);
					if(state==0){
						//鍒ゆ柇鎺ユ敹鐨勬槸鍝竴绉嶇被鍨嬬殑娑堟伅锛氬骇浣嶄俊鎭紵鐩叉敞淇℃伅锛�..
						choose=judgeType(temp);
						System.out.println("choose:"+choose);
						if(choose==0)//娓告垙缁撴潫
							break;
						state=1;
					}else{
						//寰楀埌娑堟伅绫诲瀷鍚庯紝灏嗚绫诲瀷娑堟伅鏆傛椂瀛樻斁鑷虫暟缁勪腑锛屽綋鏈璇ョ被鍨嬫秷鎭帴鏀跺畬姣曪紝鍚姩鐩稿簲鐨勫鐞嗙▼搴�
						state=storeMeg(temp,choose);
					}
				}
				input.close();
				output.close();
			}
			//鍏抽棴杈撳叆杈撳嚭娴�
			catch(Exception e){
				input.close();
				output.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		if(socket!=null){
			try{
				socket.close();
			}
			catch(IOException e){
				socket=null;
				e.printStackTrace();
			}
		}
	}
	//鍒ゆ柇骞惰繑鍥炴秷鎭被鍨�
	public int judgeType(String meg){
		if(meg.equals("seat/ ")){
			System.out.println("鏈嶅姟鍣ㄨ繑鍥�"+meg);
			inquireSym=0;//闇�浣跨敤inquire1;
			seatMeg=new String[8][];
			return 1;
		}
		else if(meg.equals("blind/ ")){
			System.out.println("鏈嶅姟鍣ㄨ繑鍥�"+meg);
			blindMeg=new String[2][];
			return 2;
		}
		else if(meg.equals("hold/ ")){
			System.out.println("鏈嶅姟鍣ㄨ繑鍥�"+meg);
			holdMeg=new Poker[2];
			return 3;
		}
		else if(meg.equals("inquire/ ")){
			System.out.println("鏈嶅姟鍣ㄨ繑鍥�"+meg);
			inquireMeg=new String[8][];
			return 4;
		}
		else if(meg.equals("flop/ ")){
			System.out.println("鏈嶅姟鍣ㄨ繑鍥�"+meg);
			flopMeg=new Poker[3];
			inquireSym=1;//闇�浣跨敤inquire2
			return 5;
		}
		else if(meg.equals("turn/ ")){
			System.out.println("鏈嶅姟鍣ㄨ繑鍥�"+meg);
			return 6;
		}
		else if(meg.equals("river/ ")){
			System.out.println("鏈嶅姟鍣ㄨ繑鍥�"+meg);
			return 7;
		}
		else if(meg.equals("showdown/ ")){
			System.out.println("鏈嶅姟鍣ㄨ繑鍥�"+meg);
			return 8;
		}
		else if(meg.equals("pot-win/ ")){
			System.out.println("鏈嶅姟鍣ㄨ繑鍥�"+meg);
			return 9;
		}
		else if(meg.equals("game-over ")){//濡傛灉鏈嶅姟鍣ㄨ繑鍥瀏ameover锛岃〃绀烘父鎴忕粨鏉熴�
			System.out.println("鏈嶅姟鍣ㄨ繑鍥�"+meg);
			return 0;
		}
		return 0;
	}
	public int storeMeg(String meg,int choose){
		switch(choose){
			/*
				鍌ㄥ瓨seat娑堟伅锛屽苟涓旀墽琛宔_seat绋嬪簭锛�
			*/
			case 1:
				if(meg.equals("/seat ")){
					controller.e_seat(seatMeg,pid);
					seatMeg=null;
					index=0;
					return 0;
				}
				else{
					String temp[]=new String[4];
					String tempMeg[]=meg.split(" ");
					int length=tempMeg.length;
					if(length==3){
						temp[0]="0";
						for(int i=0;i<length;i++){
							temp[i+1]=tempMeg[i];
						}	
						seatMeg[index]=temp;
						index++;
					}else if(length==5){
						temp[0]="0";
						for(int i=0;i<3;i++){
							temp[i+1]=tempMeg[i+2];
						}	
						seatMeg[index]=temp;
						index++;
					}
					else{
						seatMeg[index]=tempMeg;
						index++;
					}
					return 1;
				}
			/*
			 * 鎺ユ敹blind淇℃伅锛屾帴鏀跺畬姣曚箣鍚庡紑濮嬪鐞哹lind淇℃伅
			 * */
			case 2:
				if(meg.equals("/blind ")){
					controller.e_blind(blindMeg);
					blindMeg=null;
					index=0;
					return 0;
				}else{
					String tempMeg[]=meg.split(" ");
					blindMeg[index]=tempMeg;
					index++;
					return 1;
				}
			/*
			 * 鎺ユ敹hold淇℃伅骞朵笖澶勭悊
			 * */
			case 3:
				if(meg.equals("/hold ")){
					controller.e_hold(holdMeg);
					holdMeg=null;
					index=0;
					return 0;
				}else{
						int num=2;
						int type=0;
						String tempMeg[]=meg.split(" ");
						Poker poke=judgeTypeAndNum(tempMeg);
						holdMeg[index]=poke;
						index++;
						return 1;
				}
			/*
			 * 鎺ユ敹inquire淇℃伅锛屾帴鏀跺畬姣曞悗璋冪敤澶勭悊inquire鐨勬柟娉�
			 * */
			case 4:
				if(meg.equals("/inquire ")){
					String message;
					if(inquireSym==0){
						message=controller.inquire1(inquireMeg);
					}
					else{
						message=controller.inquire2(inquireMeg);
					}
					System.out.println("action:"+message);
					try{
						output.write(message.getBytes());
						output.flush();
					}
					catch(Exception e){
						e.printStackTrace();
					}			
					inquireMeg=null;
					index=0;
					return 0;
				}else{
					String tempMeg[]=meg.split(" ");
					inquireMeg[index]=tempMeg;
					index++;
					return 1;
				}		
			/*
			 * 鎺ユ敹flop娑堟伅骞朵笖澶勭悊
			 * */
			case 5:
				if(meg.equals("/flop ")){
					controller.e_flop(flopMeg);
					index=0;
					flopMeg=null;
					return 0;
				}else{
					String tempMeg[]=meg.split(" ");
					Poker poke=judgeTypeAndNum(tempMeg);
					flopMeg[index]=poke;
					index++;
					return 1;
				}
			/*
			 * 	鎺ユ敹turn娑堟伅骞朵笖澶勭悊
			 * */
			case 6:
				if(meg.equals("/turn ")){
					return 0;
				}else{
					String temp[]=meg.split(" ");
					Poker poke=judgeTypeAndNum(temp);
					controller.e_turn(poke);
					return 1;
				}
			/*
			 * 鎺ユ敹river娑堟伅骞朵笖澶勭悊
			 * */
			case 7:
				if(meg.equals("/river ")){
					return 0;
				}else{
					String temp[]=meg.split(" ");
					Poker poke=judgeTypeAndNum(temp);
					controller.e_river(poke);
					return 1;
				}
			/*
			 * 鎺ユ敹showdown娑堟伅锛氫笉闇�杩涜澶勭悊
			 * */
			case 8:
				if(meg.equals("/showdown ")){
					return 0;
				}else
					return 1;
			/*
			 * 鎺ユ敹pot-win娑堟伅
			 * */
			case 9:
				if(meg.equals("/pot-win ")){
					controller.e_potwin();
					return 0;
				}else{
					return 1;
				}
		}
		return 0;
	}	
	public Poker judgeTypeAndNum(String temp[]){
		//鍒ゆ柇鑺辫壊
		int type=0;
		int num=2;
		if(temp[0].equals("SPADES"))
			type=0;
		else if(temp[0].equals("CLUBS"))
			type=1;
		else if(temp[0].equals("DIAMONDS"))
			type=2;
		else if(temp[0].equals("HEARTS"))
			type=3;
		//鍒ゆ柇鐐规暟
		if(temp[1].equals("J"))
			num=11;
		else if(temp[1].equals("Q"))
			num=12;
		else if(temp[1].equals("K"))
			num=13;
		else if(temp[1].equals("A"))
			num=14;
		else
			num=Integer.parseInt(temp[1]);
		Poker poke=new Poker(num,type);
		return poke;
	}
}
