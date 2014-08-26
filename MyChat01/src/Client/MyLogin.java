package Client;
import javax.swing.*;

import Server.MyChatServers;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
public class MyLogin extends JFrame implements ActionListener{
	private JTextField username = new JTextField();
	private JPasswordField password = new JPasswordField();
	private JButton ok = new JButton("确定");
	private JButton cancel = new JButton("取消");
	private String address="127.0.0.1";
	private int PORT=1800;
	
	public MyLogin(){
		super("登陆");
		setSize(300, 200);
		setLocation(500,300);
		setVisible(true);
		setLayout(new BorderLayout());
		
		//top
		JPanel jpTop=new JPanel();
		add(jpTop,BorderLayout.NORTH);
		
		//content
		JPanel jpField=new JPanel();
		jpField.setLayout(null);
		JLabel jlname=new JLabel("账号");
		jlname.setBounds(50,20,50,20);
		JLabel jlpassword=new JLabel("密码");
		jlpassword.setBounds(50,60,50,20);
		username.setBounds(110,20,120,20);
		password.setBounds(110,60,120,20);
		jpField.add(jlname);
		jpField.add(jlpassword);
		jpField.add(username);
		jpField.add(password);
		add(jpField,BorderLayout.CENTER);
		
		//buttom
		JPanel jpCom=new JPanel();
		jpCom.setLayout(new FlowLayout());
		jpCom.add(ok);
		ok.addActionListener(this);
		jpCom.add(cancel);
		add(jpCom,BorderLayout.SOUTH);
		
	}
	
	public void actionPerformed(ActionEvent e){
		String username1=username.getText();
		String password1=password.getText();
		String str=username1+"#"+password1;
		login(str);
		
	}
	
	public static void main(String[] args){
		MyLogin login=new MyLogin();
	}
	
	public void login(String str){
		Socket serverSocket=null;
		PrintStream out=null;
		DataInputStream in=null;
		try{
			serverSocket=new Socket(address,PORT);
			out=new PrintStream(serverSocket.getOutputStream());
			in=new DataInputStream(serverSocket.getInputStream());
			
			
			out.println(str);
			out.flush();
		
			
			String s=in.readLine();
			if("1".equals(s)){
				
					MyChatClient frame=new MyChatClient(username.getText());
					this.dispose();
				}
			if("0".equals(s)){
				showCheckMsg("the passwd is error");
			}
			if("-1".equals(s)){
				showCheckMsg("the user not exist");
			}
			if("-2".equals(s)){
				showCheckMsg("the network is wrong");
			}
			
		}
		catch(UnknownHostException event){
			System.err.println("can not connect");
			System.exit(1);
		}
		catch (IOException e1) {
			System.err.println("can not get IO stream(do not start the Login_checkServer)");
			System.exit(1);
		}catch(Exception e){
		}
		
	}
	public void showCheckMsg(String str){
		JDialog checkDialog=new JDialog(this, "check message");
		checkDialog.show();
		checkDialog.setSize(200,150);
		checkDialog.setLayout(new FlowLayout());
		checkDialog.setLocation(400,200);
		JLabel lb=new JLabel(str);
		checkDialog.add(lb);
	}
	
}
 