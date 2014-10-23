package extractor;
import java.util.HashMap;


public class Service {
	
	private String lineName; // route name, Eg) 373(train), Blue Mountains Line(Train) ...
	private String mode; // store a transportation type, Eg) Bus, Train, Ferry and Railway
	// private ArrayList<JourneyPattern> standardServiceOld;
	private HashMap<String, JourneyPattern> standardServce;
	private DateRange operatingPeriod;
	//Take out below values from services because they are specified in vehicle journeys
	//private String daysOfWeek; // store operating days of a week, Eg) MondayToSunday
	
	public Service() {
		// TODO Auto-generated constructor stub
	}

	public String getLineName() {
		return this.lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getMode() {
		return this.mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public DateRange getOperatingPeriod() {
		return operatingPeriod;
	}

	public void setOperatingPeriod(DateRange period) {
		this.operatingPeriod = period;
	}

	public HashMap<String, JourneyPattern> getStandardServce() {
		return standardServce;
	}

	public void setStandardServce(HashMap<String, JourneyPattern> standardServce) {
		this.standardServce = standardServce;
	}

}
