package Extractor;
/**
 * Store stop information that is under a StopPoint tag.
 * stopID: <AtcoCode> reference id of a stop
 * suburb: <Place><Suburb> suburb name where the stop is located 
 * longitude: <Place><Location><Longtitude> longitude value of the stop
 * latitude: <Place><Location><Latitude> latitude value of the stop
 * stopName: <Notes> full name of the stop
 */
public class StopPoint {
	
	private int stopID;
	private String suburb;
	private double longitude;
	private double latitude;
	private String stopName;

	public StopPoint() {
		// TODO Auto-generated constructor stub
	}
	
	public StopPoint (int stopID, String suburb, double longitude, double latitude, String stopName) {
		this.setStopID(stopID);
		this.setSuburb(suburb);
		this.setLongitude(longitude);
		this.setLatitude(latitude);
		this.setStopName(stopName);
	}

	public int getStopID() {
		return stopID;
	}

	public void setStopID(int stopID) {
		this.stopID = stopID;
	}

	public String getSuburb() {
		return suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longtitude) {
		this.longitude = longtitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getStopName() {
		return stopName;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}
}
