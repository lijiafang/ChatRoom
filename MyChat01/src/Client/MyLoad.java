package Client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class MyLoad extends JFrame{
	private JButton bLoad=new JButton("µÇÂ½");
	private JButton bRegister=new JButton("×¢²á");
	boolean mainFrame=true;
	public MyLoad(){
		super("Load");
		setSize(300,200);
		setLocation(500,300);
		setVisible(true);
		setLayout(new FlowLayout());
		
		add(bLoad);
		add(bRegister);
		
		bLoad.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				MyLogin login=new MyLogin();
				
				
			}
		});
		bRegister.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				MyRegister register=new MyRegister();
				
			}
		});
		
	}
	public static void main(String[] args){
		MyLoad frame=new MyLoad();
	}
}
