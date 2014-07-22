package extractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class DataHandler implements FileHandler {
	private File data;

	public File getExtractedData() {
		return data;
	}

	@Override
	public File extractData(File raw) {
		File newFile = new File("test");

		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(newFile)));
			writer.write("hi, this is to simulate a 'handled' file");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return newFile;
	}
}