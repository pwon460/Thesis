package extractor;
import java.io.File;
import java.util.ArrayList;


public class DataHandler {

	private static final String DESTINATION = "/Users/cse/Thesis/unzipTester";
	private ArrayList<File> dataFiles;

	public DataHandler() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<File> getExtractedData() {
		return dataFiles;
	}
	
	public static ArrayList<File> extractData (File source) {
		System.out.println("Start ExtractData");
		File zipFile = source;
		ArrayList<File> files = new ArrayList<File>();
		ZipHandler zipHandler = new ZipHandler();
		ArrayList<File> filePaths = zipHandler.unZip(zipFile.getAbsolutePath(), DESTINATION);
		ParserHandler parserHandler = new ParserHandler();
		parserHandler.parseFiles(filePaths);
		System.out.println("Database is generated");
		File folder = new File("/Users/cse/Thesis/data");
		File[] listOfFiles = folder.listFiles();
	    for (int i = 0; i < listOfFiles.length; i++) {
	    	File curr = listOfFiles[i];
	      if (curr.isFile() &&  curr.getName().endsWith(".zip")) {
	    	  files.add(curr);
	      }
	    }
		return files;
	}

	// Below stuffs are for testing purpose
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
		File source = new File("/Users/cse/Thesis/transxchange-373.zip");
		extractDataTest(source);
	}
}
