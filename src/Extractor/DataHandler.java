package extractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class DataHandler implements FileHandler {
	
	private String TEST_FILE = "test.txt";

	public File getExtractedData() {
		File newFile = new File(TEST_FILE);
		return newFile;
	}

	@Override
	public File extractData(File raw) {
		File newFile = new File(TEST_FILE);

		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(newFile)));
			writer.write("hello world!");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return newFile;
	}
}