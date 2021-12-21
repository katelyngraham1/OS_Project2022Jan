import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
	private Socket incomingConnection;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;

	String clubName, clubId, email;
	Integer option;
	Integer suboption;
	String loginClubId, loginClubName;
	boolean loggedIn = false;
	boolean result;
	ClubMemberController controller;

	public ServerThread(Socket s, ClubMemberController contobj) {
		incomingConnection = s;
		controller = contobj;
		controller.setName(getName());
	}

	public void run() {
		
		log("New client thread started.");
		try {
			out = new ObjectOutputStream(incomingConnection.getOutputStream());
			in = new ObjectInputStream(incomingConnection.getInputStream());
		}

		catch (IOException e1) {
			e1.printStackTrace();
		}

		String resultMessage = "";
		// Step 4. The conversation with the client...
		try {
			do {
				
				sendMessage(resultMessage + "Please enter 1 to Register or 2 to Sign In");

				message = (String) in.readObject();

				if (message.equals("1")) {
					resultMessage = "";
					log("Request to register new club");
					// The client has chosen to register a new club
					sendMessage("Please enter the Club Name");
					clubName = (String) in.readObject();

					sendMessage("Please enter the Club Id");
					clubId = (String) in.readObject();

					sendMessage("Please enter the email to use for this club");
					email = (String) in.readObject();

					String resultStr = controller.addClub(clubName, clubId, email);
					if (resultStr != "") {
						resultMessage = "Sorry unable to add club, error = " + resultStr + "\n";
					} else {
						resultMessage = "The club has been added\n";
					}
				}

				else if (message.equals("2")) {
					resultMessage = "";
					log("Request to login to existing club");
					// The client is logging in to an existing club
					sendMessage("Please enter the club name");
					loginClubName = (String) in.readObject();

					sendMessage("Please enter the club id");
					loginClubId = (String) in.readObject();

					result = controller.login(loginClubId, loginClubName);

					if (result == true) {
						sendMessage("TRUE");
						loggedIn = true;
					}
					else
						sendMessage("FALSE");

				} else {
					resultMessage = "Unrecognised command entered, please choose again\n";
				}
			} while (message.equals("1") || result == false);

			// The user is logged in to
			do {
				printOptions();
				String optionStr = (String) in.readObject();
				option = Integer.parseInt(optionStr);

				if (hasSubOptions(option)) {
					printSubOptions(option);
					String suboptionStr = (String) in.readObject();
					suboption = Integer.parseInt(suboptionStr);
				}			

				switch (option) {
					case 1: 
						// add new member 
						sendMessage("Please enter the name of the member to add");
						String memberName = (String) in.readObject();

						sendMessage("Please enter the Age of the member");
						message = (String) in.readObject();
						int age = Integer.parseInt(message);

						sendMessage("Please enter the date the member last visited club, must be in the format dd/mm/yyyy");
						String dateVisited = (String) in.readObject();

						sendMessage("Please enter the membership fee that applies");
						message = (String) in.readObject();
						double fee = Double.parseDouble(message);

						sendMessage("Please enter the number that applies for the new membrship type you want to use picking from the list below\n" +
									"1 => Adult\n2 => Senior\n3 => Junior\n");
						message = (String) in.readObject();
						int membershipType = Integer.parseInt(message);

						sendMessage("Please enter the new payment status to set for this member picking from the list below\n" +
									"1 => Paid\n2 => Part Paid\n3 => Not Paid\n");
						message = (String) in.readObject();
						int paymentStatus = Integer.parseInt(message);

						String res = controller.addMember(loginClubId, memberName, age, dateVisited, fee, membershipType, paymentStatus);
						if (res == "") {
							sendMessage("New Member Added");
						} else {
							sendMessage("Unable to add member due to error: " + res);
						}
						break;

					case 2:
						// update member
						sendMessage("Please enter the membership id of the member you want to update");
						String memberId = (String) in.readObject();

						switch(suboption) {
							case 1:
							// update membership type
								sendMessage("Please enter the number that applies for the new membrship type you want to use picking from the list below\n" +
									"1 => Adult\n2 => Senior\n3 => Junior\n");
								message = (String) in.readObject();
								int newMembershipType = Integer.parseInt(message);
								res = controller.updateMembershipType(memberId, newMembershipType);
								if (res == "") {
									sendMessage("New Membership type Set");
								} else {
									sendMessage("Unable to update membership type due to error: " + res);
								}
								break;

							case 2:
								// update membership fee
								sendMessage("Please enter the new membership fee that you want to assign");
								message = (String) in.readObject();
								double newFee = Double.parseDouble(message);
								res = controller.updateMembershipFee(memberId, newFee);
								if (res == "") {
									sendMessage("New Member fee set");
								} else {
									sendMessage("Unable to set membership fee due to error: " + res);
								}
								break;

							case 3:
								// update members payment status
								sendMessage("Please enter the new payment status to set for this member picking from the list below\n" +
									"1 => Paid\n2 => Part Paid\n3 => Not Paid\n");
								message = (String) in.readObject();
								int newPaymentStatus = Integer.parseInt(message);
								res = controller.updatePaymentStatus(memberId, newPaymentStatus);
								if (res == "") {
									sendMessage("New Member payment status set");
								} else {
									sendMessage("Unable to set payment status due to error: " + res);
								}
								break;

							case 4:
								// update members visited details
								res = controller.updateMemberVisitedToday(memberId);
								if (res == "") {
									sendMessage("Member updated");
								} else {
									sendMessage("Unable to update member due to error: " + res);
								}
								break;

							case 5:
								// move member to new club
								sendMessage("Please enter the ID of the club to move this member to");
								String newClubId  = (String) in.readObject();
								res = controller.moveMember(memberId, newClubId);
								if (res == "") {
									sendMessage("Member moved to new club");
								} else {
									sendMessage("Unable to move member due to error: " + res);
								}
								break;

							default:
								sendMessage("Sorry do not recognise selected option");
						}
						break;

					case 3:
						// search club members list
						if (suboption == 1) {
							// seacrh all members visited in last 14 days 
							sendMessage(controller.getMembersLast14DaysCsv(loginClubId));
						} 
						else if (suboption == 2) {
							// list all paid members
							sendMessage(controller.getMembersPaidFeeCsv(loginClubId));
						}
						else if (suboption == 3) {
								// list all  members
								sendMessage(controller.getMembersCsv(loginClubId));
							} else {
							sendMessage("Sorry do not recognise selected option");	
						}
						break;

					case 4:
						// delete a member
						sendMessage("Please enter the membership id of the member you want to delete");
						String deleteMemberId = (String) in.readObject();
						res = controller.removeMember(deleteMemberId);
						if (res == "") {
							sendMessage("Member moved to new club");
						} else {
							sendMessage("Unable to move member due to error: " + res);
						}
						break;

					case 5:
						// Search all registered clubs
						sendMessage(controller.getClubsCsv());
						break;

					case 6:
						// logout
						loggedIn = false;		
						log("Logging out");
						break;
				}			

			} while (loggedIn);

			sendMessage("User logged out");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			try {
				in.close();
				out.close();
				incomingConnection.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void log(String message) {
		System.out.println(getName() +  ": " + message);
	}

	private boolean hasSubOptions(Integer opt) {
		return opt == 2 || opt == 3;
	}

	private void printOptions() {
		sendMessage("Please choose from the following options\n1 = Add New Member\n2 = Update Member\n" +
			"3 = Search Club Members List\n4 = Remove A Member\n5 = Search All Registered Clubs\n6 = Logout\n");
	}

	private void printSubOptions(int option) {
		switch (option) {
		case 2:
			sendMessage("1 = Update Membership Type for Member\n2 = Update Membership Fee for Member\n" +
				"3 = Update Members Payment Status\n4 = Update Member To Indicate that they have visited club today\n" +
				"5 = Move Member To New Club\n");
			break;
		case 3:
			sendMessage("1 = Search all members who visited in last 14 days\n2 = Search all members who paid their membership fee\n" 
			+ "3 = List all members in your club\n");

			break;
		default:
			break;
		}
	}

	public void sendMessage(String msg) {
		try {
			log("Sending message: " + msg);
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
