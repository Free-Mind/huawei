import java.io.*;
import java.net.*;
import java.util.*;
public class Communication {
	/*
		存储各种消息的数组：
		1.座位信息：String seatMeg[8][4]
		2.手牌信息：Poker holdMeg[2]
		3.inquire信息：String inquireMeg[8][5];
		5.blind消息：String blindMeg[2][2];
		6.flop消息：Poker flopMeg[3];
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
	
	private int inquireSym;//用来标记需要使用哪一次inquire方法
	private int index;//用来标记存储消息数组的下标
	private int state=0;//用来标记开始接收某种消息；0:开始接收消息；1：正在接收消息。
	private	int choose=0;//标记消息类型
	
	Ai controller;
	
	public Communication(String args[]){
		//初始化相关参数
		controller =new Ai();
		index=0;
		remoteport=Integer.parseInt(args[1]);//服务器端口
		localport=Integer.parseInt(args[3]);//本地端口
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
		
			socket = new Socket(); 
			try {
				socket.setReuseAddress(true);
				socket.setTcpNoDelay(true);
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			try {
				socket.bind(new InetSocketAddress(InetAddress.getByAddress(localBuf),localport));
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			try {
				socket.connect(new InetSocketAddress(InetAddress.getByAddress(remoteBuf), remoteport));
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				input=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				output=new DataOutputStream(socket.getOutputStream());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//发送注册消息			
		String reg="reg: "+pid+" Bob \n";
//			System.out.println(reg);
			try {
				output.write(reg.getBytes());
				output.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//接受服务器发送回来的消息
			
				while(true){
					String temp="";
					try {
						temp = input.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						try {
							input.close();
							output.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						e.printStackTrace();
					}
//					System.out.println("服务器返回:"+temp);
					if(state==0){
						//判断接收的是哪一种类型的消息：座位信息？盲注信息？...
						choose=judgeType(temp);
					//	System.out.println("tempMeg:"+temp);
					//	System.out.println("choose:"+choose);
						if(choose==0)//游戏结束
							break;
						state=1;
					}else{
						//得到消息类型后，将该类型消息暂时存放至数组中，当本次该类型消息接收完毕，启动相应的处理程序	
						state=storeMeg(temp,choose);
					}
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
	//判断并返回消息类型
	public int judgeType(String meg){
		if(meg.equals("seat/ ")){
	//		System.out.println("服务器返回:"+meg);
			inquireSym=0;//需要使用inquire1;
			seatMeg=new String[8][];
			return 1;
		}
		else if(meg.equals("blind/ ")){
	//		System.out.println("服务器返回:"+meg);
			blindMeg=new String[2][];
			return 2;
		}
		else if(meg.equals("hold/ ")){
	//		System.out.println("服务器返回:"+meg);
			holdMeg=new Poker[2];
			return 3;
		}
		else if(meg.equals("inquire/ ")){
	//		System.out.println("服务器返回:"+meg);
			inquireMeg=new String[9][];
			return 4;
		}
		else if(meg.equals("flop/ ")){
	//		System.out.println("服务器返回:"+meg);
			flopMeg=new Poker[3];
			inquireSym=1;//需要使用inquire2
			return 5;
		}
		else if(meg.equals("turn/ ")){
	//		System.out.println("服务器返回:"+meg);
			return 6;
		}
		else if(meg.equals("river/ ")){
	//		System.out.println("服务器返回:"+meg);
			return 7;
		}
		else if(meg.equals("showdown/ ")){
	//		System.out.println("服务器返回:"+meg);
			return 8;
		}
		else if(meg.equals("pot-win/ ")){
		//	System.out.println("服务器返回:"+meg);
			return 9;
		}
		else if(meg.equals("game-over ")){//如果服务器返回gameover，表示游戏结束。
	//		System.out.println("服务器返回:"+meg);
			return 0;
		}
		return 0;
	}
	public int storeMeg(String meg,int choose){
		switch(choose){
			/*
				储存seat消息，并且执行e_seat程序；
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
			 * 接收blind信息，接收完毕之后开始处理blind信息
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
			 * 接收hold信息并且处理
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
			 * 接收inquire信息，接收完毕后调用处理inquire的方法
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
			 * 接收flop消息并且处理
			 * */
			case 5:
				if(meg.equals("/flop ")){
					controller.e_flop(flopMeg);
	//				System.out.println("flopMeg:"+meg);
					index=0;
					flopMeg=null;
					return 0;
				}else{
				//	System.out.println("flopMeg:"+meg);
					String tempMeg[]=meg.split(" ");
					Poker poke=judgeTypeAndNum(tempMeg);
					flopMeg[index]=poke;
					index++;
					return 1;
				}
			/*
			 * 	接收turn消息并且处理
			 * */
			case 6:
				if(meg.equals("/turn ")){
					return 0;
				}else{
					String temp[]=meg.split(" ");
					Poker poke=judgeTypeAndNum(temp);
				//	System.out.println("poke"+poke.getnum()+"  "+poke.gettype());
					controller.e_turn(poke);
					
					return 1;
				}
			/*
			 * 接收river消息并且处理
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
			 * 接收showdown消息：不需要进行处理
			 * */
			case 8:
				if(meg.equals("/showdown ")){
					return 0;
				}else
					return 1;
			/*
			 * 接收pot-win消息
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
		//判断花色
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
		//判断点数
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
