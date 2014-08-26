package Client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
public class MyChatClient extends JFrame{
	private TextArea msgShow;
    private List onlinePeople;
    private TextField msgContent;
    private Button btnPost;
    private String address = "127.0.0.1";
    public static final int PORT = 1802;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private Socket s = null;
    private String nickname;
    
    public MyChatClient(String userName) {
    	super(userName);
		setSize(500,500);
		setLocation(500,100);
		setVisible(true);
		setLayout(null);
		
		msgShow=new TextArea();
		onlinePeople=new List();
		msgContent=new TextField();
		btnPost=new Button("发送");
		
		add(msgShow);
		add(msgContent);
		add(onlinePeople);
		add(btnPost);
		
		msgShow.setBounds(0, 0, 400, 400);
		onlinePeople.setBounds(400, 0,100, 400);
		msgContent.setBounds(0, 400, 400, 50);
		btnPost.setBounds(400,400,70,50);
		
		btnPost.addActionListener(new ActionMonitor());
		msgContent.addKeyListener(new KeyMonitor());
		
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                sendMsg(nickname + " 离开");
                setVisible(false);
                //System.exit(0);
            }
        });
        
        
        this.nickname=userName;
     
		this.start();
        
    }
    class ActionMonitor implements ActionListener {

        public void actionPerformed(ActionEvent ee) {
            post();
        }
        
    }
    
    class KeyMonitor extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                post();
            }
        }
    }
    public void start() {
    	this.connect();
    	Receive r = new Receive(this);
        Thread t = new Thread(r);
        t.start();
    }
    public void post(){
    	String content=msgContent.getText().trim();
    	String postPeople=onlinePeople.getSelectedItem();
    	String postMsg=nickname+"!@~'"+postPeople+"!@~'"+content;
    	System.out.println(postMsg);
    	if(postPeople==null||"null".equals(postPeople)||"所有人".equals(postPeople)){
    		this.updateMsgShow("I("+nickname+") say to everybody:"+content);
    	}else{
    		this.updateMsgShow("I("+nickname+") chat with "+postPeople+":"+content);
    	}
    	this.sendMsg(postMsg);
    	msgContent.setText("");
    }
    public void sendMsg(String msg){
    	try {
			dos.writeUTF(msg);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    public void connect(){
    	try {
			s=new Socket(address,PORT);
			dis=new DataInputStream(s.getInputStream());
			dos=new DataOutputStream(s.getOutputStream());
			this.sendMsg(nickname+" 进入");
			//this.getOnlinePeople().add(nickname);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("connect error");
		}
    }
	public Socket getS(){
		return this.s;
	}
	public void updateMsgShow(String str){

		this.msgShow.append(str+"\n");
		//this.msgShow.setText(this.msgShow.getText()+nickname+":"+str)
		//this.getMsgShow().setText(this.getMsgShow().getText() + str + "\n");
	}
	public List getOnlinePeople(){
		return this.onlinePeople;
	}
	public TextArea getMsgShow(){
		return this.msgShow;
	}
	public static void main(String[] args) {
		MyChatClient client=new MyChatClient("lisi");
		//client.connect();
	}
}
class Receive implements Runnable{
	 private MyChatClient cc;
	 private DataInputStream dis;
	 private boolean isStart = false;
	 public Receive(MyChatClient cc) {
		this.cc= cc;
		try {
			dis=new DataInputStream(cc.getS().getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isStart=true;
	 }
	public void run(){
		String msg="";
		try{
			while(isStart){
				msg=dis.readUTF();
				String str;
				String tempMsg[]=msg.split("!@~'");
				if(tempMsg.length==3){
					String nickName=tempMsg[0];
					String content=tempMsg[2];
					if(tempMsg[1]==null||"null".equals(tempMsg[1])||"所有人".equals(tempMsg[1])){
						str=nickName+" say to everybody:"+content;
						this.cc.updateMsgShow(str);
					}else{
						str=nickName+" say to me:"+content;
						this.cc.updateMsgShow(str);
					}
				}else{
					String[] onlinePeople=msg.split("!#~'");
					if(onlinePeople.length>1){       
						this.cc.getOnlinePeople().removeAll();
						 for (int i = 0; i < onlinePeople.length; i++) {
							 System.out.println(onlinePeople[i]);    //
	                            cc.getOnlinePeople().add(onlinePeople[i]);
	                        }
					}else{
						this.cc.updateMsgShow(msg);
					}
				}
			}
		}catch(Exception e){
			 e.printStackTrace();
			 isStart = false;
		}
		
		
	}
	
}
