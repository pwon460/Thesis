package Extractor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;


public class SQLiteConnection {

	// Table names.
	// Table names are decided considering dropping order of tables to avoid any conflict.
	final String STOPS = "STOPS";
	final String BUS_ROUTES_ORDERS = "BUS_ROUTES_ORDERS";
	final String BUS_SECTIONS = "BUS_SECTIONS";
	final String BUS_PATTERNS = "BUS_PATTERNS";
	final String BUS_CALENDAR = "BUS_CALENDAR";
	final String BUS_EXCEPTION = "BUS_EXCEPTION";
	final String BUS_TRIPS = "BUS_TRIPS";
	final String RAIL_STATIONS = "RAIL_STATIONS";
	final String RAIL_ROUTES_ORDERS = "RAIL_ROUTES_ORDERS";
	final String RAIL_SECTIONS = "RAIL_SECTIONS";
	final String RAIL_PATTERNS = "RAIL_PATTERNS";
	final String RAIL_CALENDAR = "RAIL_CALENDAR";
	final String RAIL_EXCEPTION = "RAIL_EXCEPTION";
	final String RAIL_TRIPS = "RAIL_TRIPS";
	final String WHARFS = "WHARFS";
	final String FERRY_ROUTES_ORDERS = "FERRY_ROUTES_ORDERS";
	final String FERRY_SECTIONS = "FERRY_SECTIONS";
	final String FERRY_PATTERNS = "FERRY_PATTERNS";
	final String FERRY_CALENDAR = "FERRY_CALENDAR";
	final String FERRY_EXCEPTION = "FERRY_EXCEPTION";
	final String FERRY_TRIPS = "FERRY_TRIPS";
	final String LIGHT_RAIL_STATIONS = "LIGHT_RAIL_STATIONS";
	final String LIGHT_RAIL_ROUTES_ORDERS = "LIGHT_RAIL_ROUTES_ORDERS";
	final String LIGHT_RAIL_SECTIONS = "LIGHT_RAIL_SECTIONS";
	final String LIGHT_RAIL_PATTERNS = "LIGHT_RAIL_PATTERNS";
	final String LIGHT_RAIL_CALENDAR = "LIGHT_RAIL_CALENDAR";
	final String LIGHT_RAIL_EXCEPTION = "LIGHT_RAIL_EXCEPTION";
	final String LIGHT_RAIL_TRIPS = "LIGHT_RAIL_TRIPS";
	final String DAYS_VARIATION = "DAYS_VARIATION";
	final String STAND = " Stand "; // Bus stand separator
	final String PLATFORM = " Station "; // Rail platform separator
	final String WHARF1 = " No."; // Circular quay wharf no separator
	final String WHARF2 = " Wharf "; // Darling harbor wharf no separator
	final String LIGHT_RAIL = " Light Rail"; // Light Rail platform separator
	final String BUS = "bus"; // mode for buses
	final String RAIL = "rail"; // mode for trains
	final String FERRY = "ferry"; // mode for ferries
	final String TRAM = "tram"; // mode for light rails

	// These counters will make a type of a atcoCode, routeId, journeyPatternSectionId and privaetCode int.
	// By doing this, database size can be saved in mobile side.
	static int STOP_COUNTER = 0;
	static int ROUTE_COUNTER = 0;
	static int JOURNEY_PATTERN_SECTION_COUNTER = 0;
	static int PRIVATE_CODE_COUNTER = 0;
	private HashMap<Integer, Integer> stopMap;
	private HashMap<String, Integer> routeMap;
	private HashMap<String, Integer> sectionMap;
	private HashMap<String, Integer> privateMap;
	// Store days variation instead of executing sql statement everytime.
	private HashMap<String, Integer> dayMap;
	
	public SQLiteConnection() {
		Connection connection = null;
		PreparedStatement stmt = null;
    	try{
    		File file = new File("/Users/cse/Thesis/server2.db");
    		file.delete();
		}catch(Exception e){
    		e.printStackTrace(); 
    	}
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:/Users/cse/Thesis/server2.db");
			connection.setAutoCommit(false);

			// Create all tables
			String query = "CREATE TABLE IF NOT EXISTS ";

			String attributes = " (atcoCode INTEGER PRIMARY KEY,"
								+ "suburb TEXT,"
								+ "longitude INTEGER,"
								+ "latitude INTEGER,"
								+ "name TEXT,"
								+ "number TEXT)"; // Stores stand No, platform No or wharf No.
			stmt = connection.prepareStatement(query + this.STOPS + attributes);
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.RAIL_STATIONS + attributes);
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.WHARFS + attributes);
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.LIGHT_RAIL_STATIONS + attributes);
			stmt.execute();
			stmt.close();

			attributes = " (routeId INT, "
					     + "seq INTEGER,"
					     + "atcoCode INTEGER)";
			stmt = connection.prepareStatement(query + this.BUS_ROUTES_ORDERS + attributes);
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.RAIL_ROUTES_ORDERS + attributes);
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.FERRY_ROUTES_ORDERS + attributes);
			stmt.execute();
			stmt.close();		
			stmt = connection.prepareStatement(query + this.LIGHT_RAIL_ROUTES_ORDERS + attributes);
			stmt.execute();
			stmt.close();

			attributes = " (lineName TEXT,"
						 + "journeyPatternSectionID INT, "
						 + "sequenceNo INT,"
						 + "origin INT,"
						 + "destination INT,"
						 + "runTime TEXT,"
						 + "PRIMARY KEY (lineName, journeyPatternSectionID, sequenceNo))";
			stmt = connection.prepareStatement(query + this.BUS_SECTIONS + attributes);
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.RAIL_SECTIONS + attributes);
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.FERRY_SECTIONS + attributes);
			stmt.execute();
			stmt.close();		
			stmt = connection.prepareStatement(query + this.LIGHT_RAIL_SECTIONS + attributes);
			stmt.execute();
			stmt.close();
			
			attributes = " (journeyPatternID TEXT, "
					 	 + "description TEXT, "
					 	 + "journeyPatternSectionRef INT, "
					 	 + "PRIMARY KEY(journeyPatternID, description, journeyPatternSectionRef), "
					 	 + "FOREIGN KEY(journeyPatternSectionRef) REFERENCES ";
			stmt = connection.prepareStatement(query + this.BUS_PATTERNS + attributes +
											   this.BUS_SECTIONS + "(journeyPatternSectionID))");
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.RAIL_PATTERNS + attributes +
											   this.RAIL_SECTIONS + "(journeyPatternSectionID))");
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.FERRY_PATTERNS + attributes +
											   this.FERRY_SECTIONS + "(journeyPatternSectionID))");
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.LIGHT_RAIL_PATTERNS + attributes +
					                           this.LIGHT_RAIL_SECTIONS + "(journeyPatternSectionID))");
			stmt.execute();
			stmt.close();

			attributes = " (dayId INTEGER PRIMARY KEY AUTOINCREMENT, "
						 + "mon INT, "
						 + "tue INT, "
						 + "wed INT, "
						 + "thu INT, "
						 + "fri INT, "
						 + "sat INT, "
						 + "sun INT)";
			stmt = connection.prepareStatement(query + this.DAYS_VARIATION + attributes);
			stmt.execute();
			stmt.close();

			attributes = " (privateCode INT, "// PRIMARY KEY
					     + "dayId INT, "
						 + "routeId INT, "
					     + "journeyPatternSectionId INT, "
						 + "departureTime INT, "
					     + "startDate INT, "
					     + "endDate INT)";
			stmt = connection.prepareStatement(query + this.BUS_CALENDAR + attributes);
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.RAIL_CALENDAR + attributes);
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.FERRY_CALENDAR + attributes);
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.LIGHT_RAIL_CALENDAR + attributes);
			stmt.execute();
			stmt.close();

			attributes = " (privateCode INT, "// PRIMARY KEY
					     + "dayID INT, "
					     + "startDate INT, "
					     + "endDate INT)";
			stmt = connection.prepareStatement(query + this.BUS_EXCEPTION + attributes);
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.RAIL_EXCEPTION + attributes);
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.FERRY_EXCEPTION + attributes);
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.LIGHT_RAIL_EXCEPTION + attributes);
			stmt.execute();
			stmt.close();

			attributes = " (journeyPatternSectionId INT, "// PRIMARY KEY
					     + "seq INT, " // PRIMARY KEY
					     + "arrival INT)";
			stmt = connection.prepareStatement(query + this.BUS_TRIPS + attributes);
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.RAIL_TRIPS + attributes);
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.FERRY_TRIPS + attributes);
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement(query + this.LIGHT_RAIL_TRIPS + attributes);
			stmt.execute();
			stmt.close();
			/*
			// add index
			stmt = connection.prepareStatement("CREATE INDEX idx1 ON " + this.BUS_PATTERNS + "(journeyPatternId)");
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement("CREATE INDEX idx2 ON " + this.RAIL_PATTERNS + "(journeyPatternId)");
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement("CREATE INDEX idx3 ON "+ this.FERRY_PATTERNS + "(journeyPatternId)");
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement("CREATE INDEX idx4 ON " + this.LIGHT_RAIL_PATTERNS + "(journeyPatternId)");
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement("CREATE INDEX idx5 ON " + this.BUS_SECTIONS + "(journeyPatternSectionId)");
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement("CREATE INDEX idx6 ON " + this.RAIL_SECTIONS + "(journeyPatternSectionId)");
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement("CREATE INDEX idx7 ON " + this.FERRY_SECTIONS + "(journeyPatternSectionId)");
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement("CREATE INDEX idx8 ON " + this.LIGHT_RAIL_SECTIONS + "(journeyPatternSectionId)");
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement("CREATE INDEX idx9 ON "+ this.BUS_PATTERNS + "(journeyPatternSectionRef)");
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement("CREATE INDEX idx10 ON " + this.RAIL_PATTERNS + "(journeyPatternSectionRef)");
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement("CREATE INDEX idx11 ON " + this.FERRY_PATTERNS + "(journeyPatternSectionRef)");
			stmt.execute();
			stmt.close();
			stmt = connection.prepareStatement("CREATE INDEX idx12 ON " + this.LIGHT_RAIL_PATTERNS + "(journeyPatternSectionRef)");
			stmt.execute();
			stmt.close();
			*/
			connection.commit();
			connection.close();
			insertDaysVariation();
			this.stopMap = new HashMap<Integer, Integer>();
			this.routeMap = new HashMap<String, Integer>();
			this.sectionMap = new HashMap<String, Integer>();
			this.privateMap = new HashMap<String, Integer>();
			// Bring the previous mappings for atcoCodes, routeIds and priateCodes
			if (this.mapFileChecker()) {
				this.bringMaps();
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Initialize database successfully");
	}

	public void insertDaysVariation() {
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:/Users/cse/Thesis/server2.db");
			connection.setAutoCommit(false);
			String query = "INSERT OR IGNORE INTO " + this.DAYS_VARIATION
					       + " (mon, tue, wed, thu, fri, sat, sun) VALUES (?, ?, ?, ?, ?, ?, ?)";
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					for (int k = 0; k < 2; k++) {
						for (int l = 0; l < 2; l++) {
							for (int m = 0; m < 2; m++) {
								for (int n = 0; n < 2; n++) {
									for (int o = 0; o < 2; o++) {
										stmt = connection.prepareStatement(query);
										stmt.setInt(1, i);
										stmt.setInt(2, j);
										stmt.setInt(3, k);
										stmt.setInt(4, l);
										stmt.setInt(5, m);
										stmt.setInt(6, n);
										stmt.setInt(7, o);
										stmt.execute();
										stmt.close();
									}
								}
							}
						}
					}
				}
			}
			// Store dayId and its related days variations to the hashmap.
			this.dayMap = new HashMap<String, Integer>();
			PreparedStatement dayStmt = connection.prepareStatement("SELECT * from " + this.DAYS_VARIATION);
			ResultSet daySet = dayStmt.executeQuery();
			while (daySet.next()) {
				String operationDays = "";
				for (int i = 2; i < 9; i++) {
					operationDays = operationDays + daySet.getString(i);
				}
				this.dayMap.put(operationDays, daySet.getInt(1));
			}
			daySet.close();
			dayStmt.close();
			connection.commit();
			connection.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);			
		}
	}

	private boolean mapFileChecker() {
		// if mapStop exists, assume the other map files also exist.
		File f = new File("/Users/cse/Thesis/data/mapStop.txt");
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public void saveMaps () {
	    FileWriter out = null;

		try {
			out = new FileWriter("/Users/cse/Thesis/data/mapStop.txt");
		    for (int i : this.stopMap.keySet()) {
		    	out.write(String.valueOf(i) + "," + String.valueOf(this.stopMap.get(i)) +"\n");
		    }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			out = new FileWriter("/Users/cse/Thesis/data/mapRoute.txt");
		    for (String i : this.routeMap.keySet()) {
		    	out.write(i + "," + String.valueOf(this.routeMap.get(i)) + "\n");     
		    }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			out = new FileWriter("/Users/cse/Thesis/data/mapSection.txt");
		    for (String i : this.sectionMap.keySet()) {
		    	out.write(i + "," + String.valueOf(this.sectionMap.get(i)) + "\n");     
		    }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			out = new FileWriter("/Users/cse/Thesis/data/mapPrivateCode.txt");
		    for (String i : this.privateMap.keySet()) {
		    	out.write(i + "," + String.valueOf(this.privateMap.get(i)) + "\n");     
		    }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
	}

	private void bringMaps () {
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		try {
			br = new BufferedReader(new FileReader("/Users/Cse/Thesis/data/mapStop.txt"));
			while((line = br.readLine()) != null) {
				String[] columns = line.split(csvSplitBy);
				this.stopMap.put(Integer.valueOf(columns[0]), Integer.valueOf(columns[1]));
			}
			br.close();
			STOP_COUNTER = stopMap.keySet().size();
		} catch (IOException e) {
			System.out.println("Failed to read mapStop.txt");
			e.printStackTrace();
		}
		try {
			br = new BufferedReader(new FileReader("/Users/Cse/Thesis/data/mapRoute.txt"));
			line = "";
			while((line = br.readLine()) != null) {
				String[] columns = line.split(csvSplitBy);
				this.routeMap.put(columns[0], Integer.valueOf(columns[1]));
			}
			br.close();
			ROUTE_COUNTER = routeMap.keySet().size();
		} catch (IOException e) {
			System.err.println("Failed to read mapRoute.txt");
			e.printStackTrace();
		}
		try {
			br = new BufferedReader(new FileReader("/Users/Cse/Thesis/data/mapSection.txt"));
			line = "";
			while((line = br.readLine()) != null) {
				String[] columns = line.split(csvSplitBy);
				this.sectionMap.put(columns[0], Integer.valueOf(columns[1]));
			}
			br.close();
			JOURNEY_PATTERN_SECTION_COUNTER = sectionMap.keySet().size();
		} catch (IOException e) {
			System.err.println("Failed to read mapSection.txt");
			e.printStackTrace();
		}
		try {
			br = new BufferedReader(new FileReader("/Users/cse/Thesis/data/mapPrivateCode.txt"));
			line = "";
			while((line = br.readLine()) != null) {
				String[] columns = line.split(csvSplitBy);
				this.privateMap.put(columns[0], Integer.valueOf(columns[1]));
			}
			br.close();
			PRIVATE_CODE_COUNTER = privateMap.keySet().size();
		} catch (IOException e) {
			System.out.println("Failed to read mapPrivateCode.txt");
			e.printStackTrace();
		}
	}

	public void insertData (Service service, ArrayList<StopPoint> stops,
							ArrayList<RouteSection> routeSections,
							HashMap<String, JourneyPatternSection> sections,
							HashMap<String, VehicleJourney> vehicleJourneys) {
		// If the id mappings don't exist, then add a new mapping.
		for (StopPoint stop : stops) {
			int id = stop.getStopID();
			if (!this.stopMap.containsKey(id)) {
				this.stopMap.put(id, STOP_COUNTER);
				STOP_COUNTER++;
			}
		}
		for (RouteSection route : routeSections) {
			String id = route.getRouteSectionId();
			if (!this.routeMap.containsKey(id)) {
				this.routeMap.put(id, ROUTE_COUNTER);
				ROUTE_COUNTER++;
			}
		}
		for (String sId : sections.keySet()) {
			if(!this.sectionMap.containsKey(sId)) {
				this.sectionMap.put(sId, JOURNEY_PATTERN_SECTION_COUNTER);
				JOURNEY_PATTERN_SECTION_COUNTER++;
			}
		}
		for (String pCode : vehicleJourneys.keySet()) {
			if (!this.privateMap.containsKey(pCode)) {
				this.privateMap.put(pCode, PRIVATE_CODE_COUNTER);
				PRIVATE_CODE_COUNTER++;
			}
		}
		//System.out.println("STOPS key " + this.stopMap.keySet());
		//System.out.println("ROUTE key " + this.routeMap.keySet());
		//System.out.println("PATTERN_SECTION key " + this.sectionMap.keySet());
		//System.out.println("PRIVATE_CODE key " + this.privateMap.keySet());
		System.out.println("Start insertData");
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:/Users/cse/Thesis/server2.db");
			connection.setAutoCommit(false);

			String mode = service.getMode();
			String query = "INSERT OR IGNORE INTO % VALUES (?, ?, ?, ?, ?, ?)";
			String queryForRoute = "INSERT OR IGNORE INTO % VALUES (?, ?, ?)";
			String queryForSection = "INSERT OR IGNORE INTO % VALUES (?, ?, ?, ?, ?, ?)";
			String queryForPattern = "INSERT OR IGNORE INTO % VALUES (?, ?, ?)";
			String queryForCalendar = "INSERT OR IGNORE INTO % VALUES (?, ?, ?, ?, ?, ?, ?)";
			String queryForException = "INSERT OR IGNORE INTO % VALUES (?, ?, ?, ?)";
			String queryForTrip = "INSERT OR IGNORE INTO % VALUES (?, ?, ?)";
			String updatedStopQuery = null;
			String updatedRouteQuery = null;
			String updatedSectionQuery = null;
			String updatedPatternQuery = null;
			String updatedCalendarQuery = null;
			String updatedExceptionQuery = null;
			String updatedTripQuery = null;
			String separator = null;
			if (!mode.equalsIgnoreCase(this.FERRY)) {
				if (mode.equalsIgnoreCase(this.BUS)) {
					separator = this.STAND;
					updatedStopQuery = query.replace("%", this.STOPS);
					updatedRouteQuery = queryForRoute.replace("%", this.BUS_ROUTES_ORDERS);
					updatedSectionQuery = queryForSection.replace("%", this.BUS_SECTIONS);
					updatedPatternQuery = queryForPattern.replace("%", this.BUS_PATTERNS);
					updatedCalendarQuery = queryForCalendar.replace("%", this.BUS_CALENDAR);
					updatedExceptionQuery = queryForException.replace("%", this.BUS_EXCEPTION);
					updatedTripQuery = queryForTrip.replace("%", this.BUS_TRIPS);
				} else if (mode.equalsIgnoreCase(this.RAIL)) {
					separator = this.PLATFORM;
					updatedStopQuery = query.replace("%", this.RAIL_STATIONS);
					updatedRouteQuery = queryForRoute.replace("%", this.RAIL_ROUTES_ORDERS);
					updatedSectionQuery = queryForSection.replace("%", this.RAIL_SECTIONS);
					updatedPatternQuery = queryForPattern.replace("%", this.RAIL_PATTERNS);
					updatedCalendarQuery = queryForCalendar.replace("%", this.RAIL_CALENDAR);
					updatedExceptionQuery = queryForException.replace("%", this.RAIL_EXCEPTION);
					updatedTripQuery = queryForTrip.replace("%", this.RAIL_TRIPS);
				} else if (mode.equalsIgnoreCase(this.TRAM)) {
					separator = this.LIGHT_RAIL;
					updatedStopQuery = query.replace("%", this.LIGHT_RAIL_STATIONS);
					updatedRouteQuery = queryForRoute.replace("%", this.LIGHT_RAIL_ROUTES_ORDERS);
					updatedSectionQuery = queryForSection.replace("%", this.LIGHT_RAIL_SECTIONS);
					updatedPatternQuery = queryForPattern.replace("%", this.LIGHT_RAIL_PATTERNS);
					updatedCalendarQuery = queryForCalendar.replace("%", this.LIGHT_RAIL_CALENDAR);
					updatedExceptionQuery = queryForException.replace("%", this.LIGHT_RAIL_EXCEPTION);
					updatedTripQuery = queryForTrip.replace("%", this.LIGHT_RAIL_TRIPS);
				} else {
					System.err.println("Wrong mode type.");
				}
				// insert stops and stations information
				for (StopPoint stop : stops) {
					// divide names into stop description and stand number
					String[] note = stop.getStopName().split(separator);
					stmt = connection.prepareStatement(updatedStopQuery);
					stmt.setInt(1, this.stopMap.get(stop.getStopID()));
					stmt.setString(2, stop.getSuburb());
					stmt.setString(3, String.valueOf(stop.getLongitude()));
					stmt.setString(4, String.valueOf(stop.getLatitude()));
					stmt.setString(5, note[0]);
					if (note.length > 1) {
						stmt.setString(6, note[1]);
					} else {
						stmt.setString(6, null);
					}
					stmt.execute();
					stmt.close();
				}
			} else {
				updatedStopQuery = query.replace("%", this.WHARFS);
				updatedRouteQuery = queryForRoute.replace("%", this.FERRY_ROUTES_ORDERS);
				updatedSectionQuery = queryForSection.replace("%", this.FERRY_SECTIONS);
				updatedPatternQuery = queryForPattern.replace("%", this.FERRY_PATTERNS);
				updatedCalendarQuery = queryForCalendar.replace("%", this.FERRY_CALENDAR);
				updatedExceptionQuery = queryForException.replace("%", this.FERRY_EXCEPTION);
				updatedTripQuery = queryForTrip.replace("%", this.FERRY_TRIPS);
				// insert wharf data
				for (StopPoint stop : stops) {
					// divide names into stop description and stand number
					String name = stop.getStopName();
					String[] note = new String[2];
					// Error from here 
					if (name.contains(this.WHARF1)) {
						separator = this.WHARF1;
						note = stop.getStopName().split(separator);
					// hardcoded because wharves including Darling in their name
					// don't follow the rule of setting wharf number
					} else if (name.contains("Darling")) {
						separator = this.WHARF2;
						note = stop.getStopName().split(separator);
					} else {
						note[0] = new String(name);
					}
					stmt = connection.prepareStatement(updatedStopQuery);
					stmt.setInt(1, this.stopMap.get(stop.getStopID()));
					stmt.setString(2, stop.getSuburb());
					stmt.setString(3, String.valueOf(stop.getLongitude()));
					stmt.setString(4, String.valueOf(stop.getLatitude()));
					stmt.setString(5, note[0]);
					if (note.length > 1) {
						stmt.setString(6, note[1]);
					} else {
						stmt.setString(6, null);
					}
					stmt.execute();
					stmt.close();
				}
			}
			/*
			String testingQuery = "SELECT * FROM STOPS";
			PreparedStatement tstmt = connection.prepareStatement(testingQuery);
			ResultSet res = tstmt.executeQuery();
			while(res.next()) {
				System.out.println(res.getString(1) + "		" + res.getString(2) + " 	" +
								   res.getString(3) + "		" + res.getString(4) + " 	" +
								   //res.getDouble(3) + " 	" + res.getDouble(4) + "	" +
								   res.getString(5) + " 	" + res.getString(6));
			}
			res.close();
			tstmt.close();
			*/
			System.out.println("inserted stops");
			String lineName = service.getLineName();
			///*
			// insert routes_orders data
			for(RouteSection section : routeSections) {
				ArrayList<RouteLink> routes = section.getRoutes();
				stmt = connection.prepareStatement(updatedRouteQuery);
				for (RouteLink link : routes) {
					String[] note = link.getRouteLinkId().split(" ");
					stmt.setInt(1, this.routeMap.get(note[0]));
					if (note[1].equals("1")) {
						stmt.setInt(2, 0);
						stmt.setInt(3, this.stopMap.get(link.getToStopId()));
					} else {
						stmt.setInt(2, Integer.valueOf(note[1]));
						stmt.setInt(3, this.stopMap.get(link.getToStopId()));
					}
					stmt.execute();
				}
				stmt.close();
			}
			System.out.println("inserted routes_orders data");
			//*/
			
			// insert section data
			for (String key : sections.keySet()) {
				JourneyPatternSection section = sections.get(key);
				stmt = connection.prepareStatement(updatedSectionQuery);
				stmt.setString(1, lineName);
				stmt.setInt(2, this.sectionMap.get(key));
				HashMap<Integer, TimingLink> sets = section.getJourneys();
				// sort keys in sequence order of paths
				List<Integer> sortedKeys = new ArrayList<Integer>(sets.keySet());
				Collections.sort(sortedKeys);
				for (int timingId : sortedKeys) {
					TimingLink link = sets.get(timingId);
					stmt.setInt(3, timingId);
					stmt.setString(4, link.getFromStopId());
					stmt.setString(5, link.getToStopId());
					stmt.setString(6, link.getRunTime());
					stmt.execute();
				}
				stmt.close();
				//temp.close();
			}
			
			for (String key : sections.keySet()) {
				JourneyPatternSection section = sections.get(key);
				HashMap<Integer, TimingLink> sets = section.getJourneys();
				/*
				List<Integer> sortedKeys = new ArrayList<Integer>(sets.keySet());
				Collections.sort(sortedKeys, new Comparator<Integer>() {
					public int compare(Integer k1, Integer k2) {
						if (k1 >= k2) {
							return 1;
						} else {
							return -1;
						}
					}
				});
				System.out.println(sortedKeys.toString());
				*/
				String time = "000000";
				for (int i = 1 ; i <= sets.keySet().size(); i++) {
					if (i == 1) { // set a departure place's order as 0 and arrivalTime to 0
						time = "000000";
						stmt = connection.prepareStatement(updatedTripQuery);
						stmt.setInt(1, this.sectionMap.get(key));
						stmt.setInt(2, 0); // seq
						stmt.setInt(3, Integer.valueOf(time));
						stmt.execute();
						stmt.close();
					}
					stmt = connection.prepareStatement(updatedTripQuery);
					stmt.setInt(1, this.sectionMap.get(key));
					stmt.setInt(2, i);
					TimingLink link = sets.get(i);
					String runTime = link.getRunTime();
					int hour = Integer.valueOf(runTime.substring(0, 2));
					int min = Integer.valueOf(runTime.substring(2, 4));
					int sec = Integer.valueOf(runTime.substring(4, 6));
					// figure out expected arrival time
					SimpleDateFormat input = new SimpleDateFormat("HHmmss");
					Calendar c = Calendar.getInstance();
					Date from = input.parse(time);
					c.setTime(from);
					c.add(Calendar.HOUR_OF_DAY, hour);
					c.add(Calendar.MINUTE, min);
					c.add(Calendar.SECOND, sec);
					time = input.format(c.getTime());
					stmt.setInt(3, Integer.valueOf(time)); // arrivalTime
					stmt.execute();
					stmt.close();
				}
			}
			
			
			/*
			testingQuery = "SELECT * FROM BUS_SECTIONS";
			tstmt = connection.prepareStatement(testingQuery);
			res = tstmt.executeQuery();
			while(res.next()) {
				System.out.println(res.getString(1) + "		" + res.getString(2) + "	" +
								   res.getInt(3) + "	" + res.getString(4) + "	" +
								   res.getString(5) + "		" + res.getString(6));
			}
			res.close();
			tstmt.close();
			*/
			System.out.println("inserted selection data");

			// insert pattern data;
			// ArrayList<JourneyPattern> standardServicesOld = service.getStandardService();
			HashMap<String, JourneyPattern> standardService = service.getStandardServce();
			// sort keys by journeyPatternId
			List<String> sortedKeys = new ArrayList<String>(standardService.keySet());
			Collections.sort(sortedKeys);
			for (String key : sortedKeys) {
				JourneyPattern pattern = standardService.get(key);
				stmt = connection.prepareStatement(updatedPatternQuery);
				stmt.setString(1, key);
				stmt.setString(2, pattern.getDescription());
				stmt.setInt(3, this.sectionMap.get(pattern.getJourneyPatternSectionRef()));
				stmt.execute();
				stmt.close();
			}
			System.out.println("inserted pattern data");
			/*
			testingQuery = "SELECT * FROM BUS_PATTERNS";
			tstmt = connection.prepareStatement(testingQuery);
			res = tstmt.executeQuery();
			while(res.next()) {
				System.out.println(res.getString(1) + "		" + res.getString(2) + "	" + res.getString(3));
			}
			*/

			// insert operation data
			// divide operation period into week 
			ArrayList<DateRange> operationWeeks = new ArrayList<DateRange>();
			try {
				DateRange range = service.getOperatingPeriod();
				SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date start;
				Date end;
				String startDateOfWeek = null;
				String endDateOfWeek = null;
				Calendar c = Calendar.getInstance();
				start = inputFormat.parse(range.getStartDate());
				end = inputFormat.parse(range.getEndDate());
				c.setTime(start);
				c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date curr = inputFormat.parse(inputFormat.format(c.getTime())); // Monday of the week that starts operation
				while (end.compareTo(curr) >= 0) {
					if (start.compareTo(curr) > 0) {
						startDateOfWeek = inputFormat.format(start);
					} else {
						startDateOfWeek = inputFormat.format(curr);
					}
					c.add(Calendar.DATE, 6); // Sunday
					curr = inputFormat.parse(inputFormat.format(c.getTime()));
					if (end.compareTo(curr) >= 0) { 
						endDateOfWeek = inputFormat.format(c.getTime());
					} else {
						endDateOfWeek = inputFormat.format(end);
					}
					operationWeeks.add(new DateRange(startDateOfWeek, endDateOfWeek));
					c.add(Calendar.DATE, 1); // next Monday
					curr = inputFormat.parse(inputFormat.format(c.getTime()));
					// System.out.println(startDateOfWeek + " : " + endDateOfWeek);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			System.out.println("calcualted weeks");

			// insert calendar data
			// sort keys by privateCode in VehicleJourney
			List<String> sortedPrivateCode = new ArrayList<String>(vehicleJourneys.keySet());
			Collections.sort(sortedPrivateCode);
			for (String key : sortedPrivateCode) {
				VehicleJourney vehicle = vehicleJourneys.get(key);
				StringBuilder dayPattern = new StringBuilder();
				dayPattern.append(vehicle.isMon() ? 1: 0);
				dayPattern.append(vehicle.isTue() ? 1: 0);
				dayPattern.append(vehicle.isWed() ? 1: 0);
				dayPattern.append(vehicle.isThu() ? 1: 0);
				dayPattern.append(vehicle.isFri() ? 1: 0);
				dayPattern.append(vehicle.isSat() ? 1: 0);
				dayPattern.append(vehicle.isSun() ? 1: 0);

				stmt = connection.prepareStatement(updatedCalendarQuery);
				stmt.setInt(1, this.privateMap.get(key));
				stmt.setInt(2, this.dayMap.get(dayPattern.toString()));
				String ref = standardService.get(vehicle.getJourneyPatternRef()).getJourneyPatternSectionRef();
				stmt.setInt(3, this.routeMap.get(sections.get(ref).getRouteLinkRef()));
				stmt.setInt(4, this.sectionMap.get(ref));
				stmt.setInt(5, Integer.valueOf(vehicle.getDepartrueTime().replaceAll(":", "")));
				stmt.setInt(6, Integer.valueOf(service.getOperatingPeriod().getStartDate().replaceAll("-", "")));
				stmt.setInt(7, Integer.valueOf(service.getOperatingPeriod().getEndDate().replaceAll("-", "")));
				stmt.execute();
				stmt.close();
				// System.out.println("add calendar data");
				// set false to non operation days
				HashMap<String, int[]> nonOperation = new HashMap<String, int[]>();
				if (vehicle.getDaysOfNonOperation() != null) {
					int mon = vehicle.isMon() ? 1 : 0;
					int tue = vehicle.isTue() ? 1 : 0;
					int wed = vehicle.isWed() ? 1 : 0;
					int thu = vehicle.isThu() ? 1 : 0;
					int fri = vehicle.isFri() ? 1 : 0;
					int sat = vehicle.isSat() ? 1 : 0;
					int sun = vehicle.isSun() ? 1 : 0;
					for (DateRange range : vehicle.getDaysOfNonOperation()) {
						Calendar c = Calendar.getInstance();
						SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
						Date start = inputFormat.parse(range.getStartDate());
						Date end = inputFormat.parse(range.getEndDate());
						c.setTime(start);
						c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
						c.set(Calendar.HOUR_OF_DAY, 0);
						c.set(Calendar.MINUTE, 0);
						c.set(Calendar.SECOND, 0);
						Date curr = start;
						while (end.compareTo(curr) >= 0) {
							c.setTime(curr);
							int i = c.get(Calendar.DAY_OF_WEEK);
							if (i == 1) { // Sunday
								sun = 0;
							} else if (i == 2) { // Monday
								mon = 0;
							} else if (i == 3) { // Tuesday
								tue = 0;
							} else if (i == 4) { // Wednesday
								wed = 0;
							} else if (i == 5) { // Thursday
								thu = 0;
							} else if (i == 6) { // Friday
								fri = 0;
							} else { // Saturday
								sat = 0;
							}
							for (DateRange dates : operationWeeks) {
								Date startD = inputFormat.parse(dates.getStartDate());
								Date endD = inputFormat.parse(dates.getEndDate());
								if (curr.compareTo(startD) >= 0 && curr.compareTo(endD) <= 0) {
									if (nonOperation.containsKey(dates.getStartDate()+dates.getEndDate())) {
										int[] days = nonOperation.get(dates.getStartDate()+dates.getEndDate());
										days[0] = days[0] | mon;
										days[1] = days[1] | tue;
										days[2] = days[2] | wed;
										days[3] = days[3] | thu;
										days[4] = days[4] | fri;
										days[5] = days[5] | sat;
										days[6] = days[6] | sun;
										nonOperation.put(dates.getStartDate()+dates.getEndDate(), days);
										break;
									} else {
										int[] days = {mon, tue, wed, thu, fri, sat, sun};
										//System.out.println(dates.getStartDate());
										//System.out.println(dates.getEndDate());
										nonOperation.put(dates.getStartDate()+dates.getEndDate(), days);
										break;
									}
								}
							}
							c.add(Calendar.DATE, 1);
							String date = inputFormat.format(c.getTime());
							try {
								curr = inputFormat.parse(date);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						//System.out.println("figure out nonOperation pattern");
						ArrayList<String> keys = new ArrayList<String>(nonOperation.keySet());
						Collections.sort(keys, new Comparator<String>() {
							public int compare(String s1, String s2) {
								if (Integer.valueOf(s1.substring(0, 11).replaceAll("-", "")) >=
								    Integer.valueOf(s2.substring(0, 11).replace("-", ""))) {
									return -1;
								} else {
									return 1;
								}
							}
						});
						//System.out.println("sorted nonOperation keys");
						for (String k : keys) {
							stmt = connection.prepareStatement(updatedExceptionQuery);
							stmt.setInt(1, this.privateMap.get(key));
							//System.out.println("key is " + key);
							int[] no = nonOperation.get(k);
							StringBuilder temp = new StringBuilder();
							for (int i = 0; i < 7; i++) {
								temp.append(no[i]);
							}
							stmt.setInt(2, dayMap.get(temp.toString()));
							//System.out.println("dayIs is " + dayMap.get(temp.toString()));
							stmt.setInt(3, Integer.valueOf(k.substring(0, 10).replaceAll("-", "")));
							//System.out.println(Integer.valueOf(k.substring(0, 10).replaceAll("-", "")));
							stmt.setInt(4, Integer.valueOf(k.substring(10).replaceAll("-", "")));
							//System.out.println(Integer.valueOf(k.substring(10).replaceAll("-", "")));
							stmt.execute();
							stmt.close();
						}
						//System.out.println("add nonOperation");
					}
				}
			}
			System.out.println("inserted exception data");
			/*
			testingQuery = "SELECT * FROM BUS_OPERATIONS";
			tstmt = connection.prepareStatement(testingQuery);
			res = tstmt.executeQuery();
			while(res.next()) {
				System.out.println(res.getString(1) + " " + res.getString(2) + " " + res.getString(3) + " " +
								   res.getString(4) + " " + res.getString(5) + " " + res.getBoolean(6) + " " +
								   res.getBoolean(7) + " " + res.getBoolean(8) + " " + res.getBoolean(9) + " " +
								   res.getBoolean(10) + " " + res.getBoolean(11) + " " + res.getBoolean(12));
			}
			res.close();
			tstmt.close();
			*/
			connection.commit();
			connection.close();
			
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Finish insertData");
	}
	
	public void makeInitialDataFile() {
		// run shell script to generates initial database files
		String command = "sh /Users/cse/Thesis/compareDatafiles.sh ";
		Process proc;
		try {
			proc = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			System.out.println("Error occurs");
			e.printStackTrace();
		}
		System.out.println("Generated initial data files.");
	}
	
	public void zipWeeklyFiles() {
		// run shell script to zip weekly files
		String command = "sh /Users/cse/Thesis/zipByDate.sh ";
		Process proc;
		try {
			proc = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			System.out.println("Error occurs");
			e.printStackTrace();
		}
		System.out.println("Zipped weekly files.");		
	}

	public void makeWeeklyChangedDataFile() {
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:/Users/cse/Thesis/server2.db");
			connection.setAutoCommit(false);
			String query = "Select distinct endDate from ";
			stmt = connection.prepareStatement(query + this.BUS_EXCEPTION + " order by endDate");
			ResultSet dates = stmt.executeQuery();
			ArrayList<Integer> dateList = new ArrayList<Integer>();
			while (dates.next()) {
				dateList.add(dates.getInt(1));
			}
			dates.close();
			stmt.close();
		    FileWriter out = null;
		    for (int i = 0; i < dateList.size(); i++) {
		    	int curr = dateList.get(i);
				File dir = new File("/Users/cse/Thesis/data/simo." + curr);
				if (!dir.exists()) {
					if (dir.mkdir()) {
						System.out.println("Directory is created!");
					} else {
						System.out.println("Failed to create directory!");
					}
				}
				try {
					out = new FileWriter(dir + "/bus_exception.txt");
					stmt = connection.prepareStatement("Select privateCode, dayId from " + this.BUS_EXCEPTION + " where endDate = ?");
					stmt.setInt(1, curr);
					ResultSet entries = stmt.executeQuery();
					while (entries.next()) {
						out.write(entries.getInt(1) + "," + entries.getInt(2) + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
		    }
			stmt = connection.prepareStatement(query + this.RAIL_EXCEPTION + " order by endDate");
			dates = stmt.executeQuery();
			dateList = new ArrayList<Integer>();
			while (dates.next()) {
				dateList.add(dates.getInt(1));
			}
			dates.close();
			stmt.close();
		    out = null;
		    for (int i = 0; i < dateList.size(); i++) {
		    	int curr = dateList.get(i);
				File dir = new File("/Users/cse/Thesis/data/simo." + curr);
				if (!dir.exists()) {
					if (dir.mkdir()) {
						System.out.println("Directory is created!");
					} else {
						System.out.println("Failed to create directory!");
					}
				}
				try {
					out = new FileWriter(dir + "/rail_exception.txt");
					stmt = connection.prepareStatement("Select privateCode, dayId from " + this.RAIL_EXCEPTION + " where endDate = ?");
					stmt.setInt(1, curr);
					ResultSet entries = stmt.executeQuery();
					while (entries.next()) {
						out.write(entries.getInt(1) + "," + entries.getInt(2) + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
		    }
			stmt = connection.prepareStatement(query + this.FERRY_EXCEPTION + " order by endDate");
			dates = stmt.executeQuery();
			dateList = new ArrayList<Integer>();
			while (dates.next()) {
				dateList.add(dates.getInt(1));
			}
			dates.close();
			stmt.close();
		    out = null;
		    for (int i = 0; i < dateList.size(); i++) {
		    	int curr = dateList.get(i);
				File dir = new File("/Users/cse/Thesis/data/simo." + curr);
				if (!dir.exists()) {
					if (dir.mkdir()) {
						System.out.println("Directory is created!");
					} else {
						System.out.println("Failed to create directory!");
					}
				}
				try {
					out = new FileWriter(dir + "/ferry_exception.txt");
					stmt = connection.prepareStatement("Select privateCode, dayId from " + this.FERRY_EXCEPTION + " where endDate = ?");
					stmt.setInt(1, curr);
					ResultSet entries = stmt.executeQuery();
					while (entries.next()) {
						out.write(entries.getInt(1) + "," + entries.getInt(2) + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
		    }
			stmt = connection.prepareStatement(query + this.LIGHT_RAIL_EXCEPTION + " order by endDate");
			dates = stmt.executeQuery();
			dateList = new ArrayList<Integer>();
			while (dates.next()) {
				dateList.add(dates.getInt(1));
			}
			dates.close();
			stmt.close();
		    out = null;
		    for (int i = 0; i < dateList.size(); i++) {
		    	int curr = dateList.get(i);
				File dir = new File("/Users/cse/Thesis/data/simo." + curr);
				if (!dir.exists()) {
					if (dir.mkdir()) {
						System.out.println("Directory is created!");
					} else {
						System.out.println("Failed to create directory!");
					}
				}
				try {
					out = new FileWriter(dir + "/light_rail_exception.txt");
					stmt = connection.prepareStatement("Select privateCode, dayId from " + this.LIGHT_RAIL_EXCEPTION + " where endDate = ?");
					stmt.setInt(1, curr);
					ResultSet entries = stmt.executeQuery();
					while (entries.next()) {
						out.write(entries.getInt(1) + "," + entries.getInt(2) + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
		    }
		    connection.commit();
			connection.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName()+ ": " + e.getMessage());
			System.exit(0);
		}
		this.zipWeeklyFiles();
		System.out.println("Generated weekly data files.");		
	}
	/*
	public static void main(String args[]) {
		SQLiteConnection setUp = new SQLiteConnection();
		System.out.println("Database connection is established successfully");
		
	}
	*/	
}

