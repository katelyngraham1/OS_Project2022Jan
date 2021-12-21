import java.io.*;
import java.net.*;

/**
	This is the server class that handls all client connectiojj requests and creats a new 
	Thread for each incoming connection request.
 */
public class Server {
	ServerSocket serverPort;
	Socket incomingConnection;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	ClubMemberController controller;
	int portNumber;

	public Server(int port)
	{
		try 
		{
			//Step 1.
			controller = new ClubMemberController();
			serverPort = new ServerSocket(port,10);
			portNumber = port;
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void serverWorkMethod()
	{
		
		  System.out.println("Server starter and listening on port " + portNumber);
			//Step 2.
		
		  while(true)
		  {
			  try 
			  {
				  incomingConnection = serverPort.accept();
				  System.out.println("Connection recieved from client... creating new thread to handle client connection");
				  ServerThread serverT = new ServerThread(incomingConnection, controller);
				  serverT.start();
			  }
		  
			  catch (IOException e) 
			  {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
			  }
		  }
	}
	
	/**
		The command line takes one optional argument, which is the port number to listen on for new
		client connections, if it is not specified then 10000 is used.
	 */
	public static void main(String args[])
	{
		int port = 10000;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		Server myServer = new Server(port);
		myServer.serverWorkMethod();
	}
	
	
}
