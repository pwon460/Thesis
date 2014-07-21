package test;

import logic.UpdateChecker;

public class TestMain {

	// this is only to test the download/scheduling function
	// without needing a server
	public static void main(String[] args) {
		UpdateChecker checker = new UpdateChecker();
		checker.checkForUpdates();
		
//		while (checker.getStatus() == false) {
//			System.out.println("false");
//		}
//		System.out.println("true");
		
	}
	
}
