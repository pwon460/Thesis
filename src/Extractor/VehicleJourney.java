package Extractor;
import java.util.ArrayList;


public class VehicleJourney {

	//private String privateCode;
	private boolean mon; // If true, operates. Otherwise, not operate.
	private boolean tue;
	private boolean wed;
	private boolean thu;
	private boolean fri;
	private boolean sat;
	private boolean sun;
	private ArrayList<DateRange> daysOfNonOperation;
	private String journeyPatternRef;
	private String departrueTime; // store in string form. when it needs to be compare, compare using SimpleDateFormat
	// private ArrayList<DateRange> operationWeeks;
	
	public VehicleJourney() {
		this.mon = false;
		this.tue = false;
		this.wed = false;
		this.thu = false;
		this.fri = false;
		this.sat = false;
		this.sun = false;
	}
/*
	public String getPrivateCode() {
		return privateCode;
	}

	public void setPrivateCode(String privateCode) {
		this.privateCode = privateCode;
	}
*/
	public boolean isMon() {
		return mon;
	}

	public void setMon(boolean mon) {
		this.mon = mon;
	}

	public boolean isTue() {
		return tue;
	}

	public void setTue(boolean tue) {
		this.tue = tue;
	}

	public boolean isWed() {
		return wed;
	}

	public void setWed(boolean wed) {
		this.wed = wed;
	}

	public boolean isThu() {
		return thu;
	}

	public void setThu(boolean thu) {
		this.thu = thu;
	}

	public boolean isFri() {
		return fri;
	}

	public void setFri(boolean fri) {
		this.fri = fri;
	}

	public boolean isSat() {
		return sat;
	}

	public void setSat(boolean sat) {
		this.sat = sat;
	}

	public boolean isSun() {
		return sun;
	}

	public void setSun(boolean sun) {
		this.sun = sun;
	}

	public ArrayList<DateRange> getDaysOfNonOperation() {
		return daysOfNonOperation;
	}

	public void setDaysOfNonOperation(ArrayList<DateRange> daysOfNonOperation) {
		this.daysOfNonOperation = daysOfNonOperation;
	}

	public String getJourneyPatternRef() {
		return journeyPatternRef;
	}

	public void setJourneyPatternRef(String journeyPatternRef) {
		this.journeyPatternRef = journeyPatternRef;
	}

	public String getDepartrueTime() {
		return departrueTime;
	}

	public void setDepartrueTime(String departrueTime) {
		this.departrueTime = departrueTime;
	}
	/*
	public ArrayList<DateRange> getOperationWeeks() {
		return operationWeeks;
	}

	public void setOperationWeeks(ArrayList<DateRange> operationWeeks) {
		this.operationWeeks = operationWeeks;
	}
	*/
}
