
import java.util.StringTokenizer;


public class Club {

	private String clubId;
	private String clubName;
	private String email;
	
	public Club(String cid, String name, String e)
	{
		clubId = cid;
		clubName = name;
		email = e;
	}

	/**
	 * 
	 * @param csvline
	 */
	public Club(String csvline) {
		StringTokenizer st = new StringTokenizer(csvline,",");
			  
		clubId = st.nextToken();
		clubName = st.nextToken();
		email = st.nextToken();
	}
	
	public String getClubName() {
		return clubName;
	}

	public String getClubId() {
		return clubId;
	}

	public void setClubId(String clubId) {
		this.clubId = clubId;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String toCsv() {
		return getClubId() + "," + getClubName() + "," + getEmail();
	}
}