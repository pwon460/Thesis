package test;

import Intermediary.UpdateChecker;

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UpdateChecker checker = new UpdateChecker();
		checker.checkForUpdates();
		
//		while (checker.getStatus() == false) {
//			System.out.println("false");
//		}
//		System.out.println("true");
		
	}
	
}
