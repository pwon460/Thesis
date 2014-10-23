package Extractor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parse downloaded XML files from tdx server.
 *
 */
public class ParserHandler extends DefaultHandler {
	
	// constants - tags
	final String STOP_POINT = "StopPoint";
	final String STOP_ID = "AtcoCode";
	final String SUBURB = "Suburb";
	final String LONGITUDE = "Longitude";
	final String LATITUDE = "Latitude";
	final String NOTES = "Notes";
	final String ROUTE_SECTIONS = "RouteSections";
	final String ROUTE_SECTION = "RouteSection";
	final String ROUTE_LINK = "RouteLink";
	final String JOURNEY_PATTERN_SECTION = "JourneyPatternSection";
	final String JOURNEY_PATTERN_SECTIONS = "JourneyPatternSectinos";
	final String ID = "id";
	final String FROM = "From";
	final String TO = "To";
	final String SEQUENCE_NO = "SequenceNumber";
	final String ROUTE_LINK_REF = "RouteLinkRef";
	final String STOP_POINT_REF = "StopPointRef"; // same as STOP_ID
	final String RUN_TIME = "RunTime";
	final String PASSING_TIME = "PT";
	final String HOUR = "H";
	final String MINUTE = "M";
	final String SECOND = "S";
	final String LINE_NAME = "LineName"; // Route number like bus #
	final String OPERATING_PERIOD = "OperatingPeriod";
	final String MODE = "Mode"; // Transportation type
	final String JOURNEY_PATTERN = "JourneyPattern";
	final String DESCRIPTION = "Description";
	final String JOURNEY_PATTERN_SECTION_REFS = "JourneyPatternSectionRefs";
	final String STANDARD_SERVICE = "StandardService";
	final String VEHICLE_TYPE = "VehicleType";
	final String VEHICLE_JOURNEY = "VehicleJourney";
	final String DAYS_OF_WEEK = "DaysOfWeek";
	final String MONDAY = "Monday";
	final String TUESDAY = "Tuesday";
	final String WEDNESDAY = "Wednesday";
	final String THURSDAY = "Thursday";
	final String FRIDAY = "Friday";
	final String SATURDAY = "Saturday";
	final String SUNDAY = "Sunday";
	final String MONDAY_TO_FRIDAY = "MondayToFriday";
	final String MONDAY_TO_SATURDAY = "MondayToSaturday";
	final String MONDAY_TO_SUNDAY = "MondayToSunday";
	final String NOT_SATURDAY = "NotSaturday";
	final String WEEKEND = "Weekend";
	final String DAYS_OF_NON_OPERATION = "DaysOfNonOperation";
	final String DATE_RANGE = "DateRange";
	final String START_DATE = "StartDate";
	final String END_DATE = "EndDate";
	final String JOURNEY_PATTERN_REF = "JourneyPatternRef";
	final String DEPARTURE_TIME = "DepartureTime";
	final String PRIVATE_CODE = "PrivateCode";

	// class variables
	private SAXParserFactory parserFact;
	private SAXParser parser;
	private String fileName;
	private String startTagName;
	private StopPoint stop;
	private ArrayList<StopPoint> stops;
	private String current; // current tag name
	private String prev; // name of a tag that is one before the current tag
	private ArrayList<RouteSection> routeSections;
	private RouteSection routeSection;
	private RouteLink routeLink;
	private String journeyPatternSectionId;
	private JourneyPatternSection section;
	// private ArrayList<JourneyPatternSection> sectionsOld;
	private HashMap<String, JourneyPatternSection> sections;
	private Integer timingLinkId;
	private TimingLink journey;
	private HashMap<Integer, TimingLink> journeys;
	// private ArrayList<TimingLink> journeysOld;
	private String boundary; // 2nd tag's name
	private Service service;
	// private ArrayList<JourneyPattern> standardServiceOld;
	private HashMap<String, JourneyPattern> standardService;
	private String  journeyPatternId;
	private JourneyPattern pattern;
	private String boundary2; // 3rd tag's name
	private String vehicleJourneyId;
	private VehicleJourney vehicleJourney;
	// private ArrayList<VehicleJourney> vehicleJourneysOld;
	private HashMap<String, VehicleJourney> vehicleJourneys;
	private String boundary3; //4th tag's name
	private ArrayList<DateRange> daysOfNonOperation;
	private DateRange dateRange;
	// private HashMap<String, String> mapSectionIds;
	private static int counterBus;
	private static int counterRail;
	private static int counterLight;
	private static int counterFerry;
	
	public ParserHandler() {
		super();
		this.boundary = "Default";
		this.boundary2 = "Default";
		this.boundary3 = "Default";
		try {
			this.parserFact = SAXParserFactory.newInstance();
			this.parser = parserFact.newSAXParser();
			this.boundary = new String();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	public ParserHandler(String fileName) {
		super();
		this.boundary = "Default";
		this.boundary2 = "Default";
		this.boundary3 = "Default";
		try {
			parserFact = SAXParserFactory.newInstance();
			parser = parserFact.newSAXParser();
			this.boundary = new String();
		} catch (Exception e) {
			System.err.print(e.getMessage());
			e.printStackTrace();
		}
		this.fileName = fileName;
	}
	
	public void parse() {
		try {
			parser.parse(this.fileName, this);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void parse(String fileName) {
		try {
			parser.parse(fileName, this);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void parseFiles (ArrayList<File> filePaths) {
		System.out.println("Start parseFiles");
		SQLiteConnection connection = new SQLiteConnection();
		counterBus = 0;
		counterRail = 0;
		counterLight = 0;
		counterFerry = 0;
		for (File file: filePaths) {
			ParserHandler parser = new ParserHandler(file.getAbsolutePath());
			try{
				System.out.println("parsing starts for " + file.getAbsolutePath());
				parser.parse();
				DataSet set = parser.displayInfo();
				// System.out.println(set.service == null);
				System.out.println("parsing finished");
				// fix later, to pass just DataSet set itself not separatively as params
				connection.insertData(set.service, set.stops, set.routeSections, set.sections, set.vehicleJourneys);
				// connection.insertData(this.service, this.stops, this.sections, this.vehicleJourneys);
			} catch (Exception e) {
				e.printStackTrace();
				e.getCause();
				System.out.println("parser doesn't work why");
				System.exit(0);
			}
		}
		// save maps
		connection.saveMaps();
		connection.makeInitialDataFile();
		connection.makeWeeklyChangedDataFile();
		System.out.println(counterBus + ":" + counterRail + ":" + counterFerry + ":" + counterLight);
		System.out.println("Finish parseFiles");
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		this.startTagName = qName;
		if (this.startTagName.equalsIgnoreCase(this.STOP_POINT)) {
			this.stop = new StopPoint();
			if (this.stops == null) {
				this.stops = new ArrayList<StopPoint>();
				this.boundary = this.STOP_POINT;
			}
		} else if (this.startTagName.equalsIgnoreCase(this.STOP_ID)) {
			this.current = this.STOP_ID;
		} else if (this.startTagName.equalsIgnoreCase(this.SUBURB)) {
			this.current = this.SUBURB;
		} else if (this.startTagName.equalsIgnoreCase(this.LONGITUDE)) {
			this.current = this.LONGITUDE;
		} else if (this.startTagName.equalsIgnoreCase(this.LATITUDE)) {
			this.current = this.LATITUDE;
		} else if (this.boundary.equalsIgnoreCase(this.STOP_POINT) && 
				   this.startTagName.equalsIgnoreCase(this.NOTES)) {
			this.current = this.NOTES;
		} else if (this.startTagName.equalsIgnoreCase(this.ROUTE_SECTIONS)) {
			this.routeSections = new ArrayList<RouteSection>();
			this.current = this.ROUTE_SECTIONS;
		} else if (this.startTagName.equalsIgnoreCase(this.ROUTE_SECTION)) {
			this.routeSection = new RouteSection();
			// trim a reference to decrease the length
			StringBuilder temp = new StringBuilder(attributes.getValue(this.ID).trim());
			int i = temp.indexOf("-");
			temp.delete(0, i+1);
			String id = temp.toString();
			id = id.replaceAll("sj2|-|_", "");
			this.routeSection.setRouteSectionId(id);
			this.current = this.ROUTE_SECTION;
		} else if (this.startTagName.equalsIgnoreCase(this.ROUTE_LINK)) {
			this.boundary = this.ROUTE_LINK;
			// trim a reference to decrease the length
			StringBuilder temp = new StringBuilder(attributes.getValue(this.ID).trim());
			int i = temp.indexOf("-");
			temp.delete(0, i+1);
			i = temp.lastIndexOf("-");
			temp.replace(i, i+1, " ");
			String id = temp.toString();
			id = id.replaceAll("sj2|-|_", "");
			this.routeLink = new RouteLink(id);	
			this.current = this.ROUTE_LINK;
		} else if (this.boundary.equalsIgnoreCase(this.ROUTE_LINK) &&
				   this.startTagName.equalsIgnoreCase(this.FROM)) {
			this.current = null;
			this.prev = this.FROM;
		} else if (this.boundary.equalsIgnoreCase(this.ROUTE_LINK) &&
				   this.startTagName.equalsIgnoreCase(this.TO)) {
			this.current = null;
			this.prev = this.TO;
		} else if (this.startTagName.equalsIgnoreCase(this.JOURNEY_PATTERN_SECTION)) {
			this.section = new JourneyPatternSection();
			// trim an id to decrease the id length
			StringBuilder temp = new StringBuilder(attributes.getValue(this.ID));
			int i = temp.indexOf("-");
			temp.delete(0, i+1);
			String id = temp.toString();
			id = id.replaceAll("sj2|-|_", "");
			// this.section.setId(id);
			this.journeyPatternSectionId = id;
			// if (this.sectionsOld == null) {
			if (this.sections == null) {
				this.boundary = this.JOURNEY_PATTERN_SECTION;
				// this.sectionsOld = new ArrayList<JourneyPatternSection>();
				this.sections = new HashMap<String, JourneyPatternSection>();
				// this.mapSectionIds = new HashMap<String, String>();
			}
		} else if (this.boundary.equalsIgnoreCase(this.JOURNEY_PATTERN_SECTION) && 
				   this.startTagName.equalsIgnoreCase(this.FROM)) {
			this.journey = new TimingLink();
			// if (this.journeysOld == null) {
			if (this.journeys == null) {
				// this.journeysOld = new ArrayList<TimingLink>(); 
				this.journeys = new HashMap<Integer, TimingLink>();
			}
			String no = attributes.getValue(this.SEQUENCE_NO);
			if (!no.isEmpty()) {
				this.timingLinkId = Integer.valueOf(no);
				// this.journey.setSequenceNo(Integer.valueOf(no).intValue());
			}
			this.prev = this.FROM;
		} else if (this.boundary.equalsIgnoreCase(this.JOURNEY_PATTERN_SECTION) &&
				   this.startTagName.equalsIgnoreCase(this.TO)) {
			this.prev = this.TO;
		} else if (this.startTagName.equalsIgnoreCase(this.STOP_POINT_REF)) {
			this.current = this.STOP_POINT_REF;
		} else if (this.startTagName.equalsIgnoreCase(this.ROUTE_LINK_REF)) {
			this.current = this.ROUTE_LINK_REF;
		} else if (this.startTagName.equalsIgnoreCase(this.RUN_TIME)) {
			this.current = this.RUN_TIME;
		} else if (this.startTagName.equalsIgnoreCase(this.LINE_NAME)) {
			this.current = this.LINE_NAME;
		} else if (this.startTagName.equalsIgnoreCase(this.OPERATING_PERIOD)) {
			this.boundary = this.OPERATING_PERIOD;
		} else if (this.boundary.equalsIgnoreCase(this.OPERATING_PERIOD)) {
			if (this.startTagName.equalsIgnoreCase(this.START_DATE)) {
				this.dateRange = new DateRange();
				this.current = this.START_DATE;
			} else if (this.startTagName.equalsIgnoreCase(this.END_DATE)) {
				this.current = this.END_DATE;
			}
		} else if (this.startTagName.equalsIgnoreCase(this.MODE)) {
			this.current = this.MODE;
		} else if (this.startTagName.equalsIgnoreCase(this.JOURNEY_PATTERN)) {
			this.pattern = new JourneyPattern();
			// trim id to decrease the length
			StringBuilder temp = new StringBuilder(attributes.getValue(this.ID));
			int i = temp.indexOf("-");
			temp.delete(0, i+1);
			String patternID = temp.toString();
			patternID = patternID.replaceAll("sj2|-|_", "");
			// this.pattern.setId(patternID);
			this.journeyPatternId = patternID;
			// if (this.standardServiceOld == null) {
			if (this.standardService == null) {	
				// this.standardServiceOld = new ArrayList<JourneyPattern>();
				this.standardService = new HashMap<String, JourneyPattern>();
				this.boundary = this.JOURNEY_PATTERN;
			}
		} else if (this.startTagName.equalsIgnoreCase(this.VEHICLE_TYPE)) {
			if (this.boundary == this.JOURNEY_PATTERN) {
				this.boundary2 = this.VEHICLE_TYPE;
			}
		} else if (this.boundary.equalsIgnoreCase(this.JOURNEY_PATTERN) &&
				   this.startTagName.equalsIgnoreCase(this.DESCRIPTION)) {
			if (!this.boundary2.equalsIgnoreCase(this.VEHICLE_TYPE)) {
				this.current = this.DESCRIPTION;
			}	
		} else if (this.boundary.equalsIgnoreCase(this.JOURNEY_PATTERN) &&
				   this.startTagName.equalsIgnoreCase(this.JOURNEY_PATTERN_SECTION_REFS)) {
			this.current = this.JOURNEY_PATTERN_SECTION_REFS;
		} else if (this.startTagName.equalsIgnoreCase(this.VEHICLE_JOURNEY)) {
			this.vehicleJourney = new VehicleJourney();
			// if (this.vehicleJourneysOld == null) {
			if (this.vehicleJourneys == null) {
				// this.vehicleJourneysOld = new ArrayList<VehicleJourney>();
				this.vehicleJourneys = new HashMap<String, VehicleJourney>();
				this.boundary = this.VEHICLE_JOURNEY;
			}
		} else if (this.boundary.equalsIgnoreCase(this.VEHICLE_JOURNEY) &&
				   this.startTagName.equalsIgnoreCase(this.PRIVATE_CODE)) {
			this.current = this.PRIVATE_CODE;
		} else if (this.boundary.equalsIgnoreCase(this.VEHICLE_JOURNEY) &&
				   this.startTagName.equalsIgnoreCase(this.DAYS_OF_WEEK)) {
			this.boundary2 = this.DAYS_OF_WEEK;
		} else if (this.boundary.equalsIgnoreCase(this.VEHICLE_JOURNEY) &&
				   this.boundary2.equalsIgnoreCase(this.DAYS_OF_WEEK)) {
			if (this.startTagName.equalsIgnoreCase(this.MONDAY)) {
				this.vehicleJourney.setMon(true);
			} else if (this.startTagName.equalsIgnoreCase(this.TUESDAY)) {
				this.vehicleJourney.setTue(true);
			} else if (this.startTagName.equalsIgnoreCase(this.WEDNESDAY)) {
				this.vehicleJourney.setWed(true);
			} else if (this.startTagName.equalsIgnoreCase(this.THURSDAY)) {
				this.vehicleJourney.setThu(true);
			} else if (this.startTagName.equalsIgnoreCase(this.FRIDAY)) {
				this.vehicleJourney.setFri(true);
			} else if (this.startTagName.equalsIgnoreCase(this.SATURDAY)) {
				this.vehicleJourney.setSat(true);
			} else if (this.startTagName.equalsIgnoreCase(this.SUNDAY)) {
				this.vehicleJourney.setSun(true);
			} else if (this.startTagName.equalsIgnoreCase(this.MONDAY_TO_FRIDAY)) {
				this.vehicleJourney.setMon(true);
				this.vehicleJourney.setTue(true);
				this.vehicleJourney.setWed(true);
				this.vehicleJourney.setThu(true);
				this.vehicleJourney.setFri(true);
			} else if (this.startTagName.equalsIgnoreCase(this.MONDAY_TO_SATURDAY)) {
				this.vehicleJourney.setMon(true);
				this.vehicleJourney.setTue(true);
				this.vehicleJourney.setWed(true);
				this.vehicleJourney.setThu(true);
				this.vehicleJourney.setFri(true);
				this.vehicleJourney.setSat(true);
			} else if (this.startTagName.equalsIgnoreCase(this.MONDAY_TO_SUNDAY)) {
				this.vehicleJourney.setMon(true);
				this.vehicleJourney.setTue(true);
				this.vehicleJourney.setWed(true);
				this.vehicleJourney.setThu(true);
				this.vehicleJourney.setFri(true);
				this.vehicleJourney.setSat(true);
				this.vehicleJourney.setSun(true);
			} else if (this.startTagName.equalsIgnoreCase(this.NOT_SATURDAY)) {
				this.vehicleJourney.setMon(true);
				this.vehicleJourney.setTue(true);
				this.vehicleJourney.setWed(true);
				this.vehicleJourney.setThu(true);
				this.vehicleJourney.setFri(true);
				this.vehicleJourney.setSun(true);				
			} else if (this.startTagName.equalsIgnoreCase(this.WEEKEND)) {
				this.vehicleJourney.setSat(true);
				this.vehicleJourney.setSun(true);				
			}	
		} else if (this.boundary.equalsIgnoreCase(this.VEHICLE_JOURNEY) &&
				   this.startTagName.equalsIgnoreCase(this.DAYS_OF_NON_OPERATION)) {
			this.boundary2 = this.DAYS_OF_NON_OPERATION;
			this.daysOfNonOperation = new ArrayList<DateRange>();
		} else if (this.boundary2.equalsIgnoreCase(this.DAYS_OF_NON_OPERATION) &&
				   this.startTagName.equalsIgnoreCase(this.DATE_RANGE)) {
			this.boundary3 = this.DATE_RANGE;
		} else if (this.boundary3.equalsIgnoreCase(this.DATE_RANGE)) {
			if (this.startTagName.equalsIgnoreCase(this.START_DATE)) {
				this.current = this.START_DATE;
			} else if (this.startTagName.equalsIgnoreCase(this.END_DATE)) {
				this.current = this.END_DATE;
			}
		} else if (this.startTagName.equalsIgnoreCase(this.JOURNEY_PATTERN_REF)) {
			this.current = this.JOURNEY_PATTERN_REF;
		} else if (this.startTagName.equalsIgnoreCase(this.DEPARTURE_TIME)) {
			this.current = this.DEPARTURE_TIME;
		}
	}

	@Override
	public void characters (char ch[], int start, int length) throws SAXException {
		if (this.STOP_ID.equalsIgnoreCase(this.current)) {
			this.stop.setStopID(Integer.valueOf(new String(ch, start, length)));
			this.current = null;
		} else if (this.SUBURB.equalsIgnoreCase(this.current)) {
			this.stop.setSuburb(new String(ch, start, length));
			this.current = null;
		} else if (this.LONGITUDE.equalsIgnoreCase(this.current)) {
			String temp = new String(ch, start, length).trim();
			if (!temp.isEmpty()) {
				this.stop.setLongitude(Double.valueOf(temp).doubleValue());
			}
			this.current = null;
		} else if (this.LATITUDE.equalsIgnoreCase(this.current)) {
			String temp = new String(ch, start, length).trim();
			if (!temp.isEmpty()) {
				this.stop.setLatitude(Double.valueOf(temp).doubleValue());
			}
			this.current = null;
		} else if (this.NOTES.equalsIgnoreCase(this.current)) {
			this.stop.setStopName(new String(ch, start, length).trim());
			this.current = null;
		} else if (this.boundary.equalsIgnoreCase(this.ROUTE_LINK) &&
				   this.STOP_POINT_REF.equalsIgnoreCase(this.current)) {
			if (this.prev.equalsIgnoreCase(this.FROM)) {
				this.routeLink.setFromStopId(Integer.valueOf(new String(ch, start, length).trim()));
				this.prev = null;
				//this.current = null;
			} else if (this.prev.equalsIgnoreCase(this.TO)) {
				this.routeLink.setToStopId(Integer.valueOf(new String(ch, start, length).trim()));
				this.prev = null;
				//this.current = null;
			}
			this.current = null;	
		} else if (this.boundary.equalsIgnoreCase(this.JOURNEY_PATTERN_SECTION) &&
				   this.STOP_POINT_REF.equalsIgnoreCase(this.current)) {
			if (this.prev != null) {
				if (this.prev.equalsIgnoreCase(this.FROM)) {
					this.journey.setFromStopId(new String(ch, start, length).trim());
					this.prev = null;
					//this.current = null;
				} else if (this.prev.equalsIgnoreCase(this.TO)) {
					this.journey.setToStopId(new String(ch, start, length).trim());
					this.prev = null;
					//this.current = null;
				}
			}
			this.current = null;
		} else if (this.ROUTE_LINK_REF.equalsIgnoreCase(this.current)) {
			// trim a reference to decrease the length
			StringBuilder temp = new StringBuilder(new String(ch, start, length).trim());
			int i = temp.indexOf("-");
			temp.delete(0, i+1);
			i = temp.lastIndexOf("-");
			temp.delete(i, temp.length());
			String ref = temp.toString();
			ref = ref.replaceAll("sj2|-|_", "");
			this.section.setRouteLinkRef(ref);
			this.current = null;
			//System.out.println(this.section.getRouteLinkRef());
		} else if (this.RUN_TIME.equalsIgnoreCase(this.current)) {
			// Format runTime to hhmmss
			String runTime = new String(ch, start, length).trim();
			StringBuilder formattedTime = new StringBuilder();
			runTime = runTime.replaceAll(this.PASSING_TIME, "");
			StringBuilder temp = new StringBuilder(runTime);
			if (runTime.contains(this.HOUR)) {
				int h = runTime.indexOf(this.HOUR);
				int duration = runTime.substring(0, h).length();
				if (duration < 10) {
					formattedTime.append("0" + runTime.substring(0, h));	
				} else {
					formattedTime.append(runTime.substring(0, h));
				}
				runTime = temp.delete(0, h+1).toString();
			} else {
				formattedTime.append("00");
			}
			if (runTime.contains(this.MINUTE)) {
				int m = runTime.indexOf(this.MINUTE);
				int duration = runTime.substring(0,m).length();
				if (duration < 10) { 
					formattedTime.append("0" + runTime.substring(0, m));
				} else {
					formattedTime.append(runTime.substring(0, m));
				}
				runTime = temp.delete(0, m+1).toString();
			} else {
				formattedTime.append("00");
			}
			if (runTime.contains(this.SECOND)) {
				int s = runTime.indexOf(this.SECOND);
				int duration = runTime.substring(0,s).length();
				if (duration < 10) {
					formattedTime.append("0" + runTime.substring(0, s));	
				} else {
					formattedTime.append(runTime.substring(0, s));
				}
			} else {
				formattedTime.append("00");
			}
			this.journey.setRunTime(formattedTime.toString());
			this.current = null;
		} else if (this.LINE_NAME.equalsIgnoreCase(this.current)) {
			this.service = new Service();
			this.service.setLineName(new String(ch, start, length));
			this.current = null;
		} else if (this.boundary.equalsIgnoreCase(this.OPERATING_PERIOD) &&
				   this.START_DATE.equalsIgnoreCase(this.current)) {
			this.dateRange.setStartDate(new String(ch, start, length));
			this.current = null;
		} else if (this.boundary.equalsIgnoreCase(this.OPERATING_PERIOD) &&
				   this.END_DATE.equalsIgnoreCase(this.current)) {
			this.dateRange.setEndDate(new String(ch, start, length));
			this.service.setOperatingPeriod(this.dateRange);
			this.current = null;
		} else if (this.MODE.equalsIgnoreCase(this.current)) {
			this.service.setMode(new String(ch, start, length));
			this.current = null;
		} else if (//this.boundary.equalsIgnoreCase(this.JOURNEY_PATTERN) && 
				   this.DESCRIPTION.equalsIgnoreCase(this.current)) {
			this.pattern.setDescription(new String(ch, start, length).trim());
			this.current = null;
		} else if (//this.boundary.equalsIgnoreCase(this.JOURNEY_PATTERN) &&
				   this.JOURNEY_PATTERN_SECTION_REFS.equalsIgnoreCase(this.current)) {
			// trim reference to decrease the length
			StringBuilder temp = new StringBuilder(new String(ch, start, length));
			int i = temp.indexOf("-");
			temp.delete(0, i+1);
			String ref = temp.toString();
			ref = ref.replaceAll("sj2|-|_", "");
			this.pattern.setJourneyPatternSectionRef(ref);
			this.current = null;
		} else if (this.PRIVATE_CODE.equalsIgnoreCase(this.current)) {
			// trim private code to decrease the length
			if (this.service.getMode().equalsIgnoreCase("Bus")) {
				counterBus++;
			} else if (this.service.getMode().equalsIgnoreCase("rail")) {
				counterRail++;
			} else if (this.service.getMode().equalsIgnoreCase("ferry")) {
				counterFerry++;
			} else if (this.service.getMode().equalsIgnoreCase("tram")) {
				counterLight++;
			}
			StringBuilder temp = new StringBuilder(new String(ch, start, length));
			int i = temp.indexOf("-");
			temp.delete(0, i+1);
			i = temp.indexOf("-");
			temp.delete(0, i+1);
			String pCode = temp.toString();
			pCode = pCode.replaceAll("sj2|-|_", "");
			// this.vehicleJourney.setPrivateCode(pCode);
			this.vehicleJourneyId = pCode;
			// System.out.println(pCode);
			this.current = null;
		} else if (this.boundary3.equalsIgnoreCase(this.DATE_RANGE) &&
				   this.START_DATE.equalsIgnoreCase(this.current)) {
			this.dateRange = new DateRange();
			this.dateRange.setStartDate(new String(ch, start, length));
			this.current = null;
		} else if (this.boundary3.equalsIgnoreCase(this.DATE_RANGE) &&
				   this.END_DATE.equalsIgnoreCase(this.current)) {
			if (dateRange == null) {
				System.out.println("Start date is not specifed.");
			} else {
				this.dateRange.setEndDate(new String(ch, start, length));
			}
			//this.boundary3 = "Out of dateRange"; // change comment later
			this.current = null;
		} else if (this.JOURNEY_PATTERN_REF.equalsIgnoreCase(this.current)) {
			// trim a reference to decrease the length
			StringBuilder temp = new StringBuilder(new String(ch, start, length));
			int i = temp.indexOf("-");
			temp.delete(0, i+1);
			String ref = temp.toString();
			ref = ref.replaceAll("sj2|-|_", "");
			this.vehicleJourney.setJourneyPatternRef(ref);
			this.current = null;
		} else if (this.DEPARTURE_TIME.equalsIgnoreCase(this.current)) {
			this.vehicleJourney.setDepartrueTime(new String(ch, start, length));
			this.current = null;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase(this.STOP_POINT)) {
			this.stops.add(this.stop);
		} else if (qName.equalsIgnoreCase(this.ROUTE_LINK)) {
			this.boundary = "Out of RouteLink boundary"; // change comment later
			this.routeSection.getRoutes().add(this.routeLink);
			this.routeLink = null;
		} else if (qName.equalsIgnoreCase(this.ROUTE_SECTION)) {
			this.routeSections.add(this.routeSection);
			this.routeSection = null;
		} else if (qName.equalsIgnoreCase(this.RUN_TIME)) {
			// this.journeysOld.add(journey);
			this.journeys.put(this.timingLinkId, this.journey);
		} else if (qName.equalsIgnoreCase(this.JOURNEY_PATTERN_SECTION)) {
			// this.section.setJourneysOld(this.journeysOld);
			this.section.setJourneys(this.journeys);
			// this.journeysOld = null;
			this.journeys = null;
			/*
			// to prevent duplication in journeys, 
			// check whether there's a same journeys already in the sections
			for (int i = 0; i < sections.size(); i++) {
				if (sections.get(i).equalsJourney(this.section.getJourneys())) {
					// no need to add in sections
					// but should record the section id to replace later
					this.mapSectionIds.put(this.section.getId(), this.sections.get(i).getId());
					this.section = null;
					break;
				}
			}
			*/
			// if (this.section != null) {
				// this.sectionsOld.add(this.section);
				this.sections.put(this.journeyPatternSectionId, this.section);
			// }	
		} else if (qName.equalsIgnoreCase(this.OPERATING_PERIOD)) {
			this.dateRange = null;
			this.boundary = "Out of Operating Type"; // change comment later
		} else if (qName.equalsIgnoreCase(this.JOURNEY_PATTERN)) {
			/*
			// check if there is existing journeyPatternSectionRef
			// if so, replace with existing journeyPatternSectionRef
			String key = this.pattern.getId();
			if (this.mapSectionIds.containsKey(key)) {
				this.pattern.setId(this.mapSectionIds.get(key));
			}
			*/
			// this.standardServiceOld.add(this.pattern);
			this.standardService.put(this.journeyPatternId, this.pattern);
			this.journeyPatternId = null;
			this.pattern = null;
		} else if (qName.equalsIgnoreCase(this.STANDARD_SERVICE)) {
			// this.service.setStandardService(this.standardServiceOld);
			this.service.setStandardServce(this.standardService);
		} else if (qName.equalsIgnoreCase(this.VEHICLE_TYPE)) {
			this.boundary2 = "Out of Vehicle Type"; // change comment later
		} else if (this.boundary.equalsIgnoreCase(this.VEHICLE_JOURNEY) &&
				   qName.equalsIgnoreCase(this.DAYS_OF_WEEK)) {
			this.boundary2 = "out of Days of Week"; // change comment later
		} else if (qName.equalsIgnoreCase(this.DATE_RANGE) &&
				   this.boundary3.equalsIgnoreCase(this.DATE_RANGE)) {
			this.daysOfNonOperation.add(this.dateRange);
			this.dateRange = null;
			this.boundary3 = "Out of Date Range";
		} else if (qName.equalsIgnoreCase(this.DAYS_OF_NON_OPERATION) &&
				   this.boundary2.equalsIgnoreCase(this.DAYS_OF_NON_OPERATION)) {
			this.vehicleJourney.setDaysOfNonOperation(this.daysOfNonOperation);
			this.boundary2 = "Out of Days Of Non Operation"; // change comment later
			this.daysOfNonOperation = null;
		} else if (qName.equalsIgnoreCase(this.VEHICLE_JOURNEY)) {
			// this.vehicleJourneysOld.add(this.vehicleJourney);
			this.vehicleJourneys.put(this.vehicleJourneyId, this.vehicleJourney);
			this.vehicleJourneyId = null;
			this.vehicleJourney = null;
		}
	}
	// Testing purpose
	// public void displayInfo() {
	public DataSet displayInfo() {
		/*
		System.out.println("Line Name: " + this.service.getLineName());
		System.out.println("Mode: " + this.service.getMode());
		System.out.println("Operating Period: ");
		System.out.println("	Start Date: " + this.service.getOperatingPeriod().getStartDate());
		System.out.println("	End Date: " + this.service.getOperatingPeriod().getEndDate());
		
		for(int i = 0; i < this.service.getStandardService().size(); i++) {
			JourneyPattern curr = this.service.getStandardService().get(i);
			System.out.println("No. " + i);
			System.out.println("	Pattern ID: " + curr.getId());
			System.out.println("	Description: " + curr.getDescription());
			System.out.println("	Section Ref: " + curr.getJourneyPatternSectionRef());
		}
		/*
		for(int i = 0; i < this.stops.size(); i++) {
			StopPoint curr = this.stops.get(i);
			System.out.println("No." + i);
			System.out.println("	Stop ID: " + curr.getStopID());
			System.out.println("	Suburb: " + curr.getSuburb());
			System.out.println("	Longitude: " + curr.getLongitude());
			System.out.println("	Latitude: " + curr.getLatitude());
			System.out.println("	Stop Name: " + curr.getStopName());
		}
		/*
		for(int i = 0; i < this.routeSections.size(); i++) {
			RouteSection curr = this.routeSections.get(i);
			System.out.println("Section ID: " + curr.getRouteSectionId());
			ArrayList<RouteLink> links = curr.getRoutes();
			for (int j = 0; j < links.size(); j++) {
				System.out.println("	Route Link ID: " + links.get(j).getRouteLinkId());
				System.out.println("	from: " + links.get(j).getFromStopId());
				System.out.println("	stop: " + links.get(j).getToStopId());
			}
		}
		/*
		for(int i = 0; i < 1; i++) { //this.sections.size(); i++) {
			JourneyPatternSection section = this.sections.get(i);
			System.out.println("JourneyPatternSection id: " + section.getId());
			for(int j = 0; j < section.getJourneys().size(); j++) {
				TimingLink journey = section.getJourneys().get(j);
				System.out.println("	Sequence No: " + journey.getSequenceNo());
				System.out.println("	RouteLink ref: " + journey.getRouteLinkRef());
				System.out.println("	Origin: " + journey.getFromStopId());
				System.out.println("	Destination: " + journey.getToStopId());
				System.out.println("	Run Time: " + journey.getRunTime());
			}
		}
		/*
		for(int i = 0; i < this.vehicleJourneys.size(); i++) {
			VehicleJourney curr = this.vehicleJourneys.get(i);
			System.out.println("No : " + i);
			System.out.println("	Private Code: " + curr.getPrivateCode());
			System.out.println("	JourneyPatternRef: " + curr.getJourneyPatternRef());
			System.out.println("	Departure Time: " + curr.getDepartrueTime());
			System.out.println("	Monday: " + curr.isMon());
			System.out.println("	Tuesday: " + curr.isTue());
			System.out.println("	Wednesday: " + curr.isWed());
			System.out.println("	Thursday: " + curr.isThu());
			System.out.println("	Friday: " + curr.isFri());
			System.out.println("	Saturday: " + curr.isSat());
			System.out.println("	Sunday: " + curr.isSun());
			if (curr.getDaysOfNonOperation() != null) {
				for (int j = 0; j < curr.getDaysOfNonOperation().size(); j++) {
					DateRange date = curr.getDaysOfNonOperation().get(j);
					System.out.println("	Start Date: " + date.getStartDate() + "	End Date: " + date.getEndDate());
				}
			}
		}
		*/
		
		// DataSet sets = new DataSet(this.service, this.stops, this.routeSections, this.sections, this.vehicleJourneysOld);
		DataSet sets = new DataSet(this.service, this.stops, this.routeSections, this.sections, this.vehicleJourneys);
		/*
		System.out.println(sets.service == null);
		System.out.println(sets.stops == null);
		System.out.println(sets.sections == null);
		System.out.println(sets.vehicleJourneys == null);
		*/
		return sets;
	}

	/* Testing Purpose
	public static void main(String[] argv) {
		ParserHandler parser = new ParserHandler("/Users/cse/Thesis/transxchange/90/nsw_90-373_-1-sj2.xml");
		try{
			System.out.println("parsing starts");
			parser.parse();
			System.out.println("parsing finished");
			parser.displayInfo();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	*/
}
