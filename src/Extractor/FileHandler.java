package extractor;

import java.io.File;

public interface FileHandler {
	public File extractData(File raw);
	public File getExtractedData();
}
