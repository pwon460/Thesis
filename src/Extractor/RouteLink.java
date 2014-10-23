package Extractor;

public class RouteLink {

	private String routeLinkId;
	private int fromStopId;
	private int toStopId;
	
	public RouteLink(String id) {
		this.routeLinkId = id;
	}

	public String getRouteLinkId() {
		return this.routeLinkId;
	}

	public void setRouteLinkId(String routeLinkId) {
		this.routeLinkId = routeLinkId;
	}

	public int getFromStopId() {
		return this.fromStopId;
	}

	public void setFromStopId(int fromStopId) {
		this.fromStopId = fromStopId;
	}

	public int getToStopId() {
		return this.toStopId;
	}

	public void setToStopId(int toStopId) {
		this.toStopId = toStopId;
	}
}
