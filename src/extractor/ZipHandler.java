package extractor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipHandler {

	//private final String SOURCE = "/Users/cse/Thesis/transxchange2.zip";
	//private final String DESTINATION = "/Users/cse/Thesis/unzipTester";
	private ArrayList<File> filePaths;
	
	// Testing purpose
	/*
	public static void main (String[] args) {
		ZipHandler source = new ZipHandler();
		ParserHandler parserHandler = new ParserHandler();
		parserHandler.parseFiles(source.unZip(SOURCE, DESTINATION));
	}
	*/
	public boolean removeFiles (File directory) {
		if (directory != null) {
			String[] list = directory.list();
			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					File entry = new File(directory, list[i]);
					if (entry.isDirectory()) {
						if (!removeFiles(entry))
							return false;
					} else {
						if (!entry.delete())
							return false;
					}
				}
			}
			return directory.delete();
		} else {
			return false;
		}
	}

	public boolean isXML (String fileName) {
		if (fileName.lastIndexOf(".") == -1) {
			return false;
		} else {
			// get rid of backup xml files
			int count = fileName.length() - fileName.replace(".", "").length();
			if (count == 1) {
				int length = fileName.length();
				if(fileName.substring(length - 3, length).equalsIgnoreCase("xml")) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}
	}

	public ArrayList<File> unZip (String zipFile, String outputFolder) {
		System.out.println("Start unZIp");
		byte[] buffer = new byte[1024];
		this.filePaths = new ArrayList<File>();

		try {
			// Create output directory if not exist.
			// Otherwise, deletes existing directory then create it.
			//File directory = new File(this.DESTINATION);
			File directory = new File(outputFolder + "/unZipTester");
			if (!directory.exists()) {
				directory.mkdir();
			} else {
				removeFiles(directory);
				directory.mkdir();
			}
			// Get the zip file content
			ZipInputStream input = new ZipInputStream(new FileInputStream(zipFile));
			// Get the zipped file list entry
			ZipEntry entry = input.getNextEntry();
			while (entry != null) {
				String fileName = entry.getName();
				File newFile = new File(outputFolder + "/unZipTester" + File.separator+ fileName);
				// System.out.println(newFile.getAbsolutePath() + " is created");
				if (entry.isDirectory()) {
					new File(newFile.getParent()).mkdir();
				} else {
					if (this.isXML(newFile.getName())) {
////////////////////////// getAbsolulteFile()???
						filePaths.add(newFile.getAbsoluteFile());
						new File(newFile.getParent()).mkdirs();
						FileOutputStream output = new FileOutputStream(newFile);
						int i;
						while ((i = input.read(buffer)) > 0) {
							output.write(buffer, 0, i);
						}
						output.close();
					}
				}
				entry = input.getNextEntry();
			}
			input.closeEntry();
			input.close();
			// System.out.println("Finish unzip");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finish unZip");
		return filePaths;
	}

}
