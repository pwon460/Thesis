package extractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class MockDataHandler implements FileHandler {
	
	private String testFile;

	public MockDataHandler() {
		Calendar cal = Calendar.getInstance();
		StringBuilder sb = new StringBuilder();
		sb.append(cal.get(Calendar.YEAR));
		sb.append("-");
		int month = cal.get(Calendar.MONTH);
		month++;
		sb.append(month);
		sb.append("-");
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		if (dayOfMonth < 10) {
			sb.append("0");	
		}
		sb.append(dayOfMonth);
		testFile = sb.toString();
	}
	
	@Override
	public File getExtractedData() {
		return extractData(null);
	}

	@Override
	public File extractData(File raw) {
		File newFile = new File(testFile);

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