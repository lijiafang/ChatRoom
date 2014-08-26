package Client;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
public class MyRegister extends JFrame implements ActionListener{
	private JTextField username=new JTextField();
	private JPasswordField password=new JPasswordField();
	private JPasswordField repassword=new JPasswordField();
	private JButton ok=new JButton("»∑∂®");
	private JButton cancel=new JButton("»°œ˚");
	private String address="127.0.0.1";
	private int PORT=1801;
	
	public MyRegister(){
		super("◊¢≤·’À∫≈");
		setSize(300,300);
		setLocation(500,300);
		setVisible(true);
		setLayout(new BorderLayout());
		
		//top
		JPanel jpTop=new JPanel();
		add(jpTop,BorderLayout.NORTH);
		
		//center
		JPanel jpCenter=new JPanel();
		jpCenter.setLayout(null);
		JLabel jlusername=new JLabel("Í«≥∆");
		jlusername.setBounds(50,20,50,20);
		JLabel jlpassword=new JLabel("√‹¬Î");
		jlpassword.setBounds(50,60,50,20);
		JLabel jlrepassword=new JLabel("»∑»œ√‹¬Î");
		jlrepassword.setBounds(50,100,50,20);
		username.setBounds(110,20,120,20);
		password.setBounds(110,60,120,20);
		repassword.setBounds(110,100,120,20);
		jpCenter.add(jlusername);
		jpCenter.add(jlpassword);
		jpCenter.add(jlrepassword);
		jpCenter.add(username);
		jpCenter.add(password);
		jpCenter.add(repassword);
		add(jpCenter,BorderLayout.CENTER);
		
		//buttom
		JPanel jpButtom=new JPanel();
		jpButtom.setLayout(new FlowLayout());
		jpButtom.add(ok);
		jpButtom.add(cancel);
		add(jpButtom,BorderLayout.SOUTH);
		
		ok.addActionListener(this);
			
	}
	public static void main(String[] args){
		MyRegister register=new MyRegister();
	}
	public void actionPerformed(ActionEvent e){
		String username1=username.getText();
		String password1=password.getText();
		String password2=password.getText();
		while(!(password1.equals(password2))){
			showCheckMsg("the password is not consistents");
		}
		String str=username1+"#"+password1;
		register(str);
		
	}
	public void register(String str){
		Socket serverSocket=null;
		PrintStream out=null;
		DataInputStream in=null;
		try{
			serverSocket=new Socket(address,PORT);
			out=new PrintStream(serverSocket.getOutputStream());
			in=new DataInputStream(serverSocket.getInputStream());
			
			out.println(str);
			out.flush();
			//System.out.println("aaaaa");
			
			String s=in.readLine();
			
			if("0".equals(s)){
				showCheckMsg("register successful");
				//this.dispose();
			}
			if("1".equals(s)){
				showCheckMsg("the user has existed");
			}
			
		}
		catch(UnknownHostException event){
			System.err.println("can not connect");
			System.exit(1);
		}
		catch (IOException e1) {
			System.err.println("can not get IO stream");
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
