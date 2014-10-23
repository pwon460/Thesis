package Extractor;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class MockDataHandler implements FileHandler {
	
	private String testFile1;
	private String testFile2;
	private String testFile3;

	public MockDataHandler() {
		Calendar cal = Calendar.getInstance();
		StringBuilder sb = new StringBuilder();
		sb.append("simo.");
		sb.append(cal.get(Calendar.YEAR));
		int month = cal.get(Calendar.MONTH);
		month++;
		sb.append(month);
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		if (dayOfMonth < 10) {
			sb.append("0");	
		}
		sb.append(dayOfMonth);
		sb.append(".zip");
		testFile1 = sb.toString();
		testFile2 = "simo.init.zip";
		testFile3 = "simo.patch.20140912.zip";
	}
	
	@Override
	public ArrayList<File> getExtractedData() {
		return extractData(null);
	}

	@Override
	public ArrayList<File> extractData(File raw) {
		ArrayList<File> fileList = new ArrayList<File>();
		File newFile = new File(testFile1);
		File newFile2 = new File(testFile2);
		File newFile3 = new File(testFile3);

		fileList.add(newFile);
		fileList.add(newFile2);
		fileList.add(newFile3);
		return fileList;
	}
	
}