import java.util.StringTokenizer;

/**
 * 
 */
public class Member {

	private String clubId;
	private String playerId;
	private String name;
	private int age;
	private String dateLastVisited;
	private double membershipFee;
	private int membershipType;
	private int paymentStatus;

	/**
	 * 
	 * @param cid
	 * @param pid
	 * @param n
	 * @param a
	 * @param v
	 * @param f
	 * @param mt
	 * @param ps
	 */
	public Member(String cid, String pid, String n, int a, String v, double f, int mt, int ps) {
		clubId = cid;
		playerId = pid;
		name = n;
		age = a;
		dateLastVisited = v;
		membershipFee = f;
		membershipType = mt;
		paymentStatus = ps;
	}

	/**
	 * 
	 * @param csvline
	 */
	public Member(String csvline) {
		StringTokenizer st = new StringTokenizer(csvline, ",");

		clubId = st.nextToken();
		playerId = st.nextToken();
		name = st.nextToken();
		age = Integer.parseInt(st.nextToken());
		dateLastVisited = st.nextToken();
		membershipFee = Double.parseDouble(st.nextToken());
		membershipType = Integer.parseInt(st.nextToken());
		paymentStatus = Integer.parseInt(st.nextToken());

	}

	public String getClubId() {
		return clubId;
	}

	public void setClubId(String clubId) {
		this.clubId = clubId;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getDateLastVisited() {
		return dateLastVisited;
	}

	public void setDateLastVisited(String dateLastVisited) {
		this.dateLastVisited = dateLastVisited;
	}

	public double getMembershipFee() {
		return membershipFee;
	}

	public void setMembershipFee(double membershipFee) {
		this.membershipFee = membershipFee;
	}

	public int getMembershipType() {
		return membershipType;
	}

	public void setMembershipType(int membershipType) {
		this.membershipType = membershipType;
	}

	public int getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String toCsv() {
		return getClubId() + "," + getPlayerId() + "," + getName() + "," + getAge() + "," + getDateLastVisited() + ","
				+ getMembershipFee() + "," + getMembershipType() + "," + getPaymentStatus();
	}

	public String toPrintedCsv() {
		return getClubId() + "," + getPlayerId() + "," + getName() + "," + getAge() + "," + getDateLastVisited() + ","
				+ getMembershipFee() + "," + getMembershipTypeStr(getMembershipType()) + "," + getPaymentStatusStr(getPaymentStatus());
	}

	public String getPaymentStatusStr(int paymentStatus) {
		switch (paymentStatus) {
		case 1:
			return "Paid";
		case 2:
			return "Part Paid";
		case 3:
			return "Not Paid";
		default:
			return "Unknown";
		}
	}

	public String getMembershipTypeStr(int paymentStatus) {
		switch (paymentStatus) {
		case 1:
			return "Adult";
		case 2:
			return "Senior";
		case 3:
			return "Junior";
		default:
			return "Unknown";
		}
	}
}
