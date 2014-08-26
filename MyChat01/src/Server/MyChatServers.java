package Server;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
public class MyChatServers extends JFrame implements Runnable{
	private TextArea msgShow;
    private java.awt.List onlinePeople;
    private ServerSocket ss = null;
    public static final int PORT = 1802;
    private List<Client> clients = new ArrayList<Client>();
    
    public MyChatServers(){
    	
    	super("聊天服务器运行中");
    	//System.out.println("my~~~~");
    	setSize(500,500);
		setLocation(500,100);
		setVisible(true);
		setLayout(null);
		
		msgShow=new TextArea();
		onlinePeople=new java.awt.List();
		
		add(msgShow);
		add(onlinePeople);
		
		msgShow.setBounds(0, 0, 400, 500);
		onlinePeople.setBounds(400, 0, 100, 500);
		
		this.start();
    }
    public void start(){
    	try{
    		ss=new ServerSocket(PORT);
    		while(true){
    			Socket s=ss.accept();
    			Client c=new Client(this,s);  //给连进来的客户端创建线程
    			Thread t=new Thread(c);
    			t.start();
    			clients.add(c);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		if(ss!=null){
    			try {
					ss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    		ss=null;
    	}
    }

    public List<Client> getClients(){
    	return clients;
    }
    public void updateMsgShow(String str){

		this.msgShow.append(str+"\n");
	}
    public java.awt.List getOnlinePeople(){
    	return onlinePeople;
    }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args)throws IOException{
		new MyChatServers();
	}
   
}
class Client implements Runnable{
	private MyChatServers cs;
	private Socket s;
	private DataInputStream dis;
	private DataOutputStream dos;
	private boolean isStart=false;
	private String nickname;
	
	public Client(MyChatServers cs,Socket s){
		this.cs=cs;
		this.s=s;
		try {
			dis=new DataInputStream(s.getInputStream());
			isStart=true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run(){
		String msg="";
		try{
			while(isStart){
				msg=dis.readUTF();
				String tempMsg[]=msg.split("!@~'");
				if(tempMsg.length==3){
					String nickname=tempMsg[0];
					String content=tempMsg[2];
					if(tempMsg[1]==null||"null".equals(tempMsg[1])||"所有人".equals(tempMsg[1])){
						for(int i=0;i<cs.getClients().size();i++){
							Client c=cs.getClients().get(i);
							if(this.s!=c.getS()){
								this.send(c.getS(),msg);
							}
						}
						cs.updateMsgShow(msg);
					}else{
						for(int i=0;i<cs.getClients().size();i++){
							Client c=cs.getClients().get(i);
							if(tempMsg[1].equals(c.getNickname())){
								this.send(c.getS(), msg);
							}
						}
						cs.updateMsgShow(msg);
					}
				}else{
					String sysMsg[]=msg.split(" ");
					if(sysMsg.length==2){
						if("进入".equals(sysMsg[1])){
							cs.getOnlinePeople().add(sysMsg[0]);
							this.nickname=sysMsg[0];
							String onlinePeople="所有人";
							for(int i=0;i<cs.getOnlinePeople().getItemCount();i++){
								onlinePeople+="!#~'"+cs.getOnlinePeople().getItem(i);
							}
							for(int i=0;i<cs.getClients().size();i++){
								Client c=cs.getClients().get(i);
								this.send(c.getS(), onlinePeople);
							}
						}else if("离开".equals(sysMsg[1])){
							cs.getOnlinePeople().remove(sysMsg[0]);
							String onlinePeople="所有人";
							for(int i=0;i<cs.getOnlinePeople().getItemCount();i++){
								onlinePeople+="!#~'"+cs.getOnlinePeople().getItem(i);
							}
							for(int i=0;i<cs.getClients().size();i++){
								Client c=cs.getClients().get(i);
								this.send(c.getS(), onlinePeople);
							}
						}
						msg+="聊天室";
						cs.updateMsgShow(msg);
					}
					for(int i=0;i<cs.getClients().size();i++){
						Client c=cs.getClients().get(i);
						this.send(c.getS(), msg);
					}
				}
				
			}
		}catch(Exception e){
			 isStart = false;
	            cs.getClients().remove(this);
		}
	}
	public Socket getS(){
		return s;
	}
	public void send(Socket s,String msg){
		try {
			dos=new DataOutputStream(s.getOutputStream());
			dos.writeUTF(msg);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	public String getNickname(){
		return nickname;
	}
}