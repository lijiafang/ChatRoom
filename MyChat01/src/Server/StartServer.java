package Server;

public class StartServer {
	public static void main(String[] args) {
		new Thread(new MyChatServers()).start();
		new Thread(new Login_checkServer()).start();
		//new Thread(new Register_checkServer()).start();
		
	}

}
