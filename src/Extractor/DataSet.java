package Extractor;
import java.util.ArrayList;
import java.util.HashMap;


public class DataSet {
	
	public Service service;
	public ArrayList<StopPoint> stops;
	public ArrayList<RouteSection> routeSections;
	// public ArrayList<JourneyPatternSection> sections;
	public HashMap<String, JourneyPatternSection> sections;
	// public ArrayList<VehicleJourney> vehicleJourneys;
	public HashMap<String, VehicleJourney> vehicleJourneys;
	public DataSet(Service service, ArrayList<StopPoint> stops,
				   ArrayList<RouteSection> routeSections,
			       //ArrayList<JourneyPatternSection> sections,
				   HashMap<String, JourneyPatternSection> sections,
			       // ArrayList<VehicleJourney> vehicleJourneys) {
				   HashMap<String, VehicleJourney> vehicleJourneys) {
		this.service = service;
		this.stops = stops;
		this.routeSections = routeSections;
		this.sections = sections;
		this.vehicleJourneys = vehicleJourneys;
	}

}
