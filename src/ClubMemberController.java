import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class ClubMemberController {
	List<Member> memberList;
	List<Club> clubList;
	String name;
	
	public ClubMemberController() {
		memberList = new ArrayList<Member>();
		clubList = new ArrayList<Club>();
		String line;
		
		int i;
		try 
		{
			FileReader fR = new FileReader("members.csv");
			BufferedReader bR = new BufferedReader(fR);
			
			bR.readLine();
			while((line = bR.readLine()) !=null)
			{
				memberList.add(new Member(line));
			}			
			
			fR = new FileReader("clubs.csv");
			bR = new BufferedReader(fR);
			
			bR.readLine();
			while((line = bR.readLine()) !=null)
			{
				clubList.add(new Club(line));
			}			
		} 
		
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}


	public String addClub(String clubName, String clubId, String email) {
		log("Request to add club with name:" + clubName + " club id: " + clubId + " and email: " + email);
		Club c = getClubById(clubId);
		if (c != null) {
			String error = "Cannot use that club id as it is already used";
			log(error);
			return error;
		}

		c = getClubByName(clubName);
		if (c != null) {
			String error = "Cannot use that club name as it is already used";
			log(error);
			return error;
		}

		Club newClub = new Club(clubId, clubName, email);
		clubList.add(newClub);
		addClubToFile(newClub);
        return "";
	}


	/**
	 * Go through the list of clubs and make sure that there is one that matches the
	 * clubd and name passed in. If there is return true, if not return false
	 * 
	 * @param loginClubId
	 * @param loginClubName
	 * @return
	 */
	public boolean login(String loginClubId, String loginClubName) {
		Iterator<Club> iter = clubList.iterator();

		Club c;
		
		while(iter.hasNext())
		{
			c = iter.next();	
			if(c.getClubName().equals(loginClubName) && c.getClubId().equals(loginClubId))
			{
				return true;
			}
		}
		return false;
	}

	public Club getClubByName(String clubName) {
		Iterator<Club> iter = clubList.iterator();
		Club c;
		
		while(iter.hasNext())
		{
			c = iter.next();	
			if(c.getClubName().equals(clubName))
			{
				return c;
			}
		}
		return null;
	}

	public Member getMemberById(String memberId) {
		Iterator<Member> iter = memberList.iterator();
		Member m;
		
		while(iter.hasNext())
		{
			m = iter.next();	
			if(m.getPlayerId().equals(memberId))
			{
				return m;
			}
		}
		return null;
	}

	public Club getClubById(String clubId) {
		Iterator<Club> iter = clubList.iterator();
		Club c;
		
		while(iter.hasNext())
		{
			c = iter.next();	
			if(c.getClubId().equals(clubId))
			{
				return c;
			}
		}
		return null;
	}

	public String addMember(String clubId, String memberName, int age, String dateVisited, double fee,
			int membershipType, int paymentStatus) {

		// make sure club id is valid
		Club c = getClubById(clubId);
		if (c == null) {
			return "The club id does not exist, please try again";
		}

		// make sure membership type, visited date and payment status is valid
		if (!paymentStatusIsValid(paymentStatus)) {
			return "Invalid payment status value suppplied";
		}
		if (!membershipTypeIsValid(membershipType)) {
			return "Invalid membership type value suppplied";
		}		
		if (!dateIsValid(dateVisited)) {
			return "Invalid date format provided for the date this member last visited the club, must be in the forat dd/mm/yyyy";
		}
		
		int playerId = memberList.size() + 1;
		Member newMember = new Member(clubId, ""+playerId, memberName, age, dateVisited, fee, membershipType, paymentStatus);
		memberList.add(newMember);
		addMemberToFile(newMember);
        return "";
	}


	public boolean membershipTypeIsValid(int membershipType) {
		return membershipType >= 1 && membershipType <= 3;
	}

	public boolean dateIsValid(String dateStr) {
		DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
	}

	public boolean paymentStatusIsValid(int paymentstatus) {
		return paymentstatus >= 1 && paymentstatus <= 3;
	}

	public String updateMembershipType(String memberId, int newMembershipType) {
		Member m = getMemberById(memberId);
		if (m == null) {
			return "No such member";
		}

		if (newMembershipType < 1 || newMembershipType > 3) {
			return "Invalid membership type value provided";
		}

		m.setMembershipType(newMembershipType);
		saveMembers();
		return "";
	}

	public String updateMembershipFee(String memberId, double newFee) {
		Member m = getMemberById(memberId);
		if (m == null)
			return "No such member";

		m.setMembershipFee(newFee);
		saveMembers();
		return "";
	}

	public String updatePaymentStatus(String memberId, int newPaymentStatus) {
		Member m = getMemberById(memberId);
		if (m == null)
			return "No such member";

		if (newPaymentStatus < 1 || newPaymentStatus > 3) {
			return "Invalid payment status value provided";
		}

		m.setPaymentStatus(newPaymentStatus);
		saveMembers();
		return "";
	}

	public String updateMemberVisitedToday(String memberId) {
		Member m = getMemberById(memberId);
		if (m == null)
			return "No such member";

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
		Date date = new Date();  
		m.setDateLastVisited(formatter.format(date));
		saveMembers();		
		return "";
	}

	public String moveMember(String memberId, String newClubId) {
		Member m = getMemberById(memberId);
		if (m == null)
			return "No such member";


		Club c = getClubById(newClubId);
		if (c == null) {
			return "The club id does not exist";
		}
	
		m.setClubId(newClubId);
		saveMembers();	
		return "";
	}

	public String getMembersLast14DaysCsv(String clubId) {
		Iterator<Member> iter = memberList.iterator();
		Member m;
		
		String result = "";

		while(iter.hasNext())
		{
			m = iter.next();	
			if(m.getClubId().equals(clubId) && isWithin14Days(m.getDateLastVisited()))
			{
				result += m.toPrintedCsv() + "\n";
			}
		}
		return result;
	}

	private boolean isWithin14Days(String dateLastVisited) {
		try {
			DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = sourceFormat.parse(dateLastVisited);
			Date now = new Date();
			long diff = now.getTime() - date.getTime();
			float days = (diff / (86400000));
			return days <= 14;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getMembersPaidFeeCsv(String clubId) {
		Iterator<Member> iter = memberList.iterator();
		Member m;
		
		String result = "clubid,playerid,name,age,visited,fee,type,status\n";

		while(iter.hasNext())
		{
			m = iter.next();	
			if(m.getClubId().equals(clubId) && m.getPaymentStatus() == 1)
			{
				result += m.toPrintedCsv() + "\n";
			}
		}
		return result;
	}

	public String getMembersCsv(String clubId) {
		Iterator<Member> iter = memberList.iterator();
		Member m;
		
		String result = "clubid,playerid,name,age,visited,fee,type,status\n";

		while(iter.hasNext())
		{
			m = iter.next();	
			if(m.getClubId().equals(clubId))
			{
				result += m.toPrintedCsv() + "\n";
			}
		}
		return result;
	}

	public String removeMember(String deleteMemberId) {
		Iterator<Member> iter = memberList.iterator();
		Member m;
		
		while(iter.hasNext())
		{
			m = iter.next();	
			if(m.getPlayerId().equals(deleteMemberId))
			{
				iter.remove();
			}
		}
		
		saveMembers();	
		return "";
	}

	public String getClubsCsv() {
		Iterator<Club> iter = clubList.iterator();
		Club c;
		
		String result = "clubid,clubname,email\n";
		while(iter.hasNext())
		{
			c = iter.next();	
			result += c.toCsv() + "\n";
		}
		return result;
	}


	
	private void addClubToFile(Club newClub) {
		try 
		{
			FileWriter fR = new FileWriter("clubs.csv",true);
			BufferedWriter bR = new BufferedWriter(fR);
			bR.append(newClub.toCsv()+"\n");
			bR.close();
			fR.close();
		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void addMemberToFile(Member newMember) {
		try 
		{
			FileWriter fR = new FileWriter("members.csv",true);
			BufferedWriter bR = new BufferedWriter(fR);
			bR.append(newMember.toCsv()+"\n");
			bR.close();
			fR.close();
		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public List<Member> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<Member> memberList) {
		this.memberList = memberList;
	}

	public List<Club> getClubList() {
		return clubList;
	}

	public void setClubList(List<Club> clubList) {
		this.clubList = clubList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private void log(String message) {
		System.out.println(getName() +  ": " + message);
	}


	private void saveMembers() {
		try {
			FileWriter fR = new FileWriter("members.csv");
			BufferedWriter bR = new BufferedWriter(fR);
			bR.append("clubid,playerid,name,age,visited,fee,type,status\n");
			Iterator<Member> iter = memberList.iterator();
			Member m;
			
			while(iter.hasNext())
			{
				m = iter.next();	
				bR.append(m.toCsv()+"\n");
			}
			
			bR.close();
			fR.close();
		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
