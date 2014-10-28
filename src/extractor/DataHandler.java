package extractor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class DataHandler {

	//private static final String DESTINATION = "/Thesis/Downloads";
	private static String servletPath;
	
	public DataHandler() {
		// TODO Auto-generated constructor stub
	}

	public static String getServletPath() {
		return servletPath;
	}

	public static void setServletPath(String servletPath) {
		DataHandler.servletPath = servletPath;
	}

	public static ArrayList<File> getExtractedData() {
		ArrayList<File> files = new ArrayList<File>();
		//File folder = new File(DESTINATION);
		File folder = new File(servletPath + "/Downloads");
		System.out.println("folder path is " + servletPath + "/Downloads");
		File[] listOfFiles = folder.listFiles();
		File init = null;
		File patch = null;
		File weekly = null;
		String currSunday = getCurrentSunday().trim();
	    for (int i = 0; i < listOfFiles.length; i++) {
	    	File curr = listOfFiles[i];
	      if (curr.isFile() &&  curr.getName().equals("simo.init.zip")) {
	    	  init = curr;
	      } else if (curr.isFile() && curr.getName().matches("simo\\.patch\\.\\d{8}\\.zip")) {
	    	  patch = curr;
	      } else if (curr.isFile() && curr.getName().matches("simo\\." + currSunday + "\\.zip")) {
	    	  weekly = curr;
	      }
	    }
	    files.add(weekly);
	    files.add(init);
	    files.add(patch);
	    System.out.println("Extrated Files in getExtractedData() ");
	    //System.out.println(weekly == null);
	    //System.out.println(init == null);
	    //System.out.println(patch == null);
	    return files;
	}
	
	public static ArrayList<File> extractData (File source) {
		System.out.println("Start ExtractData");
		File zipFile = source;
		ZipHandler zipHandler = new ZipHandler();
		//ArrayList<File> filePaths = zipHandler.unZip(zipFile.getAbsolutePath(), DESTINATION);
		ArrayList<File> filePaths = zipHandler.unZip(zipFile.getAbsolutePath(), zipFile.getParentFile().getPath());
		ParserHandler parserHandler = new ParserHandler();
		parserHandler.parseFiles(filePaths);
		System.out.println("Database is generated");
		return getExtractedData();
	}

	private static String getCurrentSunday() {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return new SimpleDateFormat("yyyyMMdd").format(c.getTime());
	}
	// Below stuffs are for testing purpose
	/*
	private File data;
	public File getExtractedDataTest() {
		return data;
	}
	
	public static void extractDataTest (File source) {
		System.out.println("Start ExtractData");
		File zipFile = source;
		ZipHandler zipHandler = new ZipHandler();
		ArrayList<File> filePaths = zipHandler.unZip(zipFile.getAbsolutePath(), DESTINATION);
		ParserHandler parserHandler = new ParserHandler();
		parserHandler.parseFiles(filePaths);
		System.out.println("Database is generated");
	}

	public static void main (String args[]) {
		File source = new File("/Users/cse/Thesis/transxchange2.zip");
		extractDataTest(source);
	}
	*/
}
