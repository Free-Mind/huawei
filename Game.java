
public class Game{
	public static void main(String[] args){
//		String test[]={"127.0.0.1","6000","127.0.0.8","6008","8888"};
		Communication con=new Communication(args);
		con.connect();
	}
}
