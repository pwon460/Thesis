package Extractor;
import java.util.ArrayList;


public class RouteSection {
	
	private String routeSectionId;
	private ArrayList<RouteLink> routes;

	public RouteSection() {
		this.routes = new ArrayList<RouteLink>();
	}

	public String getRouteSectionId() {
		return this.routeSectionId;
	}

	public void setRouteSectionId(String routeSectionId) {
		this.routeSectionId = routeSectionId;
	}

	public ArrayList<RouteLink> getRoutes() {
		return this.routes;
	}

	public void setRoutes(ArrayList<RouteLink> routes) {
		this.routes = routes;
	}

}
