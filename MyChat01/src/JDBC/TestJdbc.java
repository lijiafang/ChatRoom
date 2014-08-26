package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestJdbc {
	public static String DRIVER="com.mysql.jdbc.Driver";
	public static String URL="jdbc:mysql://localhost:3306/lijiafang";
	public static String USER="root";
	public static String PASSWD="lijiafang";
	public static Statement state=null;
	static{
		try {
			Class.forName(DRIVER);
			Connection conn=DriverManager.getConnection(URL, USER, PASSWD);
			state=conn.createStatement();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static int loginConnect(String id,String passwd){
		try {
			String str="select name from user where id='"+id+"'";
			//state.execute("insert into user value('111','xwk')");
			ResultSet result=state.executeQuery(str);
			if(!result.next()){
				System.out.println("the user not exist");
				return -1;
			}else if(!result.getString(1).equals(passwd)){
				System.out.println("the passwd is error");
				return 0;
			}else{
				System.out.println("all right");
				return 1;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return -2;
	}
	public static int registerConnect(String id,String passwd){
		int i=loginConnect(id,"");
		if(i==-1){
			String str="insert into user value('"+id+"',"+"'"+passwd+"')" ;
			try {
				int resulte=state.executeUpdate(str);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return 0;
		}
		else{
			return 1;
		}
	}
}
