package Server;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import JDBC.TestJdbc;
class CheckRegister implements Runnable{
	Socket clientSocket;
	CheckRegister(Socket s){
		this.clientSocket=s;
	}
	public void run(){
		String ip=clientSocket.getInetAddress().getHostAddress();
		System.out.println(ip+"..connecte");
		try{
			PrintStream out = new PrintStream(
					new BufferedOutputStream(clientSocket.getOutputStream(),1024),false);
			DataInputStream in=new DataInputStream(
						new BufferedInputStream(clientSocket.getInputStream()));
			
			String userAndPassword=null;
			userAndPassword=in.readLine();
			String str[]=userAndPassword.split("#");
			int i=TestJdbc.registerConnect(str[0], str[1]);
			out.print(i);
			out.flush();
			in.close();
			out.close();
		}catch(Exception e){
			new RuntimeException("register error");
		}
		
	}
}
public class Register_checkServer implements Runnable{
	private int PORT=1801;
	public Register_checkServer(){
		System.out.println("register_checkserver start\n");
		ServerSocket socket=null;
		try {
				socket=new ServerSocket(PORT);
			} catch (IOException e) {
				System.out.println("can not Listen port");
			}
		Socket clientSocket=null;
		while(true){
			try{
				clientSocket=socket.accept();
			}catch(IOException e){
				System.err.println("can not connection");
				System.exit(1);
			}
			new Thread(new CheckRegister(clientSocket)).start();
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	public  static void main(String[] args){
		new Register_checkServer();
	}
}


