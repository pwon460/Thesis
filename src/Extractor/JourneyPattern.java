package Extractor;

public class JourneyPattern {
	
	// private String id; // id attribute in JourneyPattern
	private String description; // route description eg) Coogee to City
	private String ref; // reference of JourneyPatternSection id
	
	public JourneyPattern() {
		// TODO Auto-generated constructor stub
	}
	/*
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	*/
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJourneyPatternSectionRef() {
		return this.ref;
	}

	public void setJourneyPatternSectionRef(String ref) {
		this.ref = ref;
	}

}
