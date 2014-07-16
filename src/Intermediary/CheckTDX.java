package Intermediary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import static java.nio.file.StandardCopyOption.*;

public class CheckTDX implements Runnable {

	private static boolean hasUpdate = false;
	private UpdateChecker checker = null;
	private static String current = System.getProperty("user.dir");
	private static String path1 = ""; // variable for file that app users will receive
	private static String path2 = ""; // newly downloaded file is assigned here

	public CheckTDX(UpdateChecker updateChecker) {
		this.checker = updateChecker;
	}

	@Override
	public void run() {
		System.out.println("downloading file from tdx");
		downloadZip(); // download file from TDX and save it as temp.zip
		
		while (!path2.equals("") && isZipMalformed(path2)) {
			System.out.println("zip 2 malformed, redownloading");
			downloadZip();
		}
		
		// if path1 (ie. file1) doesn't exist, this means the file downloaded is
		// the first one for the server or the server was just turned on
		if (path2.equals("")) {
			checker.handleUpdate(path1);
		} else { // there exists an old file and a new one has just been
					// downloaded
			try {
				ZipFile zip1 = new ZipFile(new File(path1));
				ZipFile zip2 = new ZipFile(new File(path2));

				compareSize(zip1, zip2);
				if (!hasUpdate) {
					compareEntries(zip1, zip2);
				}

				zip1.close();
				zip2.close();
				
			} catch (ZipException e) { // zip malformed - eg. server died while
										// dl'ing
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("cleaning up extra zip");
			// clean up files such that only most recent version remains
			try {
				handleZip2();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// pass it forward to data extraction part
			if (hasUpdate) {
				checker.handleUpdate(path1);
			}
			
		}

	}

	// check if malformed or incompletely downloaded
	private boolean isZipMalformed(String path) {
		boolean isValid = false;

		try {
			ZipFile zip = new ZipFile(new File(path));
			zip.close();
		} catch (ZipException e) {
			isValid = true;
		} catch (IOException e) {
			isValid = true;
		}

		return isValid;
	}

	private void downloadZip() {
		try {
			Authenticator.setDefault(new CustomAuthenticator());
			URL url = new URL(
					"https://tdx.transportnsw.info/download/files/transxchange.zip");
			InputStream input = url.openStream();
			File file = new File(current, "temp.zip");
			// if temp exists, save as temp2
			// set path1 to point to existing temp
			if (file.exists() && !file.isDirectory()) {
				path1 = file.getCanonicalPath();
				// if zip1 is malformed, it will be overwritten
				if (!isZipMalformed(path1)) { 
					file = new File(current, "temp2.zip");
					path2 = file.getCanonicalPath();
					System.out.println("saving to " + path2);
				} else {
					System.out.println("malformed zip detected, overwriting");
					System.out.println("saving to " + path1);
				}
			} else { // otherwise save as temp
				path1 = file.getCanonicalPath();
			}

			OutputStream output = new FileOutputStream(file);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = input.read(bytes)) != -1) {
				output.write(bytes, 0, read);
			}

			output.close();
			input.close();
			System.out.println("done!");
			
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleZip2() throws IOException {
		Path p1 = Paths.get(path1);
		Path p2 = Paths.get(path2);

		if (hasUpdate) { // old zip superceeded by new zip, ie. replace old copy
			// TODO: THERE IS A CHANCE THAT THIS FILE WILL BE IN USE
			// BY THE SERVLET OR EXTRACTOR
			// NEED TO PUT A LOCK ON IT OR SOMETHING
			// BEFORE IT CAN BE REPLACED BY NEWER VERSION
			System.out.println("new file is different to old one");
			System.out.println("replacing old file with new one");
			Files.move(p2, p1, REPLACE_EXISTING);
		} else { // new zip is same as old zip, ie. it's useless
			System.out.println("newly downloaded file is same as old one");
			System.out.println("removing duplicated version");
			removeZip(path2);
		}
	}
	
	private void removeZip (String path) {
		System.out.println(path);
		Path p = Paths.get(path);
		try {
			Files.deleteIfExists(p);
		} catch (IOException e) {
			System.err.println("Unable to delete zip!");
			e.printStackTrace();
		}
	}

	private void compareSize(ZipFile zip1, ZipFile zip2) {
		if (zip1.size() != zip2.size()) {
			hasUpdate = true;
		}
	}

	private void compareEntries(ZipFile zip1, ZipFile zip2) {
		HashMap<String, Long> map1 = null;
		HashMap<String, Long> map2 = null;

		map1 = makeZipMap(zip1);
		map2 = makeZipMap(zip2);

		if (!map1.equals(map2)) {
			hasUpdate = true;
		}

	}

	private HashMap<String, Long> makeZipMap(ZipFile zip) {
		HashMap<String, Long> map = new HashMap<String, Long>();

		for (Enumeration e = zip.entries(); e.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) e.nextElement();
			String filename = entry.getName();
			Long timestamp = entry.getTime();

			if (!map.containsKey(filename)) {
				map.put(filename, timestamp);
			}
		}

		return map;
	}

}
