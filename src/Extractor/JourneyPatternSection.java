package Extractor;
import java.util.HashMap;


public class JourneyPatternSection {
	// private String id;
	// private ArrayList<TimingLink> journeysOld;
	private HashMap<Integer, TimingLink> journeys;
	private String routeLinkRef;
	
	public JourneyPatternSection() {
		// TODO Auto-generated constructor stub
	}
	/*
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	*/
	/*
	public ArrayList<TimingLink> getJourneysOld() {
		return journeysOld;
	}

	public void setJourneysOld(ArrayList<TimingLink> journeysOld) {
		this.journeysOld = journeysOld;
	}
	*/
	public HashMap<Integer, TimingLink> getJourneys() {
		return journeys;
	}

	public void setJourneys(HashMap<Integer, TimingLink> journeys) {
		this.journeys = journeys;
	}

	public String getRouteLinkRef() {
		return routeLinkRef;
	}

	public void setRouteLinkRef(String routeLinkRef) {
		this.routeLinkRef = routeLinkRef;
	}
	
	/*
	public boolean equalsJourney (ArrayList<TimingLink> other) {
		int size = this.journeysOld.size();
		if (size != other.size()) {
			return false;
		}
		for (int i = 0; i < this.journeysOld.size(); i++) {
			if (!this.journeysOld.get(i).equals(other.get(i))) {
				return false;
			}
		}
		return true;
	}
	*/
}