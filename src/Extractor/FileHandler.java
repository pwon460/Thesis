package extractor;

import java.io.File;
import java.util.ArrayList;

public interface FileHandler {
	public ArrayList<File> extractData(File raw);
	public ArrayList<File> getExtractedData();
}
