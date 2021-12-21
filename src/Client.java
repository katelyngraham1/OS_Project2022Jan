import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Client {

	Socket outgoingConnection;
	ObjectInputStream in;
	ObjectOutputStream out;
	String message;
	String response;
	String option;
	Scanner input;
	int port;
	String host = "127.0.0.1";

	public Client(int p)
	{
		input = new Scanner(System.in);
		port = p;
	}
	
	public void clientWorkMethod()
	{
		try 
		{
			String result;
			// connect to server
			outgoingConnection = new Socket(host,port);
			
			System.out.println("Connection made to " + host + "  Port " + port);
			
			// Assign input and output streams
			in = new ObjectInputStream(outgoingConnection.getInputStream());
			out = new ObjectOutputStream(outgoingConnection.getOutputStream());
			
			
			try 
			{
				do
				{
					message = (String)in.readObject();
					System.out.println(message);
					response = input.nextLine();
					sendMessage(response);
					
					
					option = response;
					
					if(response.equals("1"))
					{
						getResponses(3);
						message = (String)in.readObject();
						System.out.println(message);
					} 
					else if(response.equals("2"))
					{
						getResponses(2);
						message = (String)in.readObject();
						System.out.println(message);
					} else {
						message = "FALSE";
					}
					
				} while(message.equals("FALSE")||option.equals("1"));
				
				
				// At this point the person has logged in, so now they can access extra options
				do
				{
					boolean isValid = false;
					message = (String)in.readObject();					
					System.out.print(message);

					while(!isValid) {

						response = input.nextLine();
						if (isInteger(response)) {
							int ioption = Integer.parseInt(response);
							if (ioption >0 && ioption < 7)
								isValid = true;
						}	
					}
					sendMessage(response);
					option = response;
					switch (option) {
						case "1":
							getResponses(6);
							break;

						case "2":
							// get the sub option for update member
							message = (String)in.readObject();
							System.out.print(message);
							isValid = false;
							int suboption = -1;
							while (!isValid) {
								response = input.nextLine();
								if(isInteger(response)) {
									suboption = Integer.parseInt(response);
									isValid = suboption > 0 && suboption < 6;
								}
							}
							sendMessage(response);
														
							switch(suboption) {
								case 1:
								case 2:
								case 3:
								case 5:
									getResponses(2);
									break;									
								case 4:
									getResponses(1);
									break;
							}
							break;
						case "3":
							message = (String)in.readObject();
							System.out.print(message);
							response = input.nextLine();
							sendMessage(response);
							break;
						case "4":
							getResponses(1);
							break;
						case "5":
						case "6":
							break;
						default:
							break;
					}

					message = (String)in.readObject();
					System.out.println(message);	

				} while(!option.equals("6"));
			} 
			
			
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} 
		
		catch (UnknownHostException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void log(String message) {
		// System.out.println("Client: " + message);
	}

	public static void main(String args[])
	{
		int tport = 10000;
		if (args.length > 0) {
			tport = Integer.parseInt(args[0]);
		}

		Client myClient = new Client(tport);
		myClient.clientWorkMethod();
	}
	
	public void getResponses(int steps) throws IOException, ClassNotFoundException {
		for (int i = 0; i < steps; i++) {
			message = (String)in.readObject();
			System.out.println(message);
			response = input.nextLine();
			sendMessage(response);	
		}	
	}

	public void sendMessage(String msg)
	{
		try 
		{
			log("Sending message: " + msg);
			out.writeObject(msg);
			out.flush();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public boolean isInteger(String s) {
		try { 
			Integer.parseInt(s); 
		} catch(NumberFormatException e) { 
			return false; 
		} catch(NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

}
