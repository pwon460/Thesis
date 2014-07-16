package test;

import Intermediary.UpdateChecker;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UpdateChecker checker = new UpdateChecker();
		checker.checkForUpdates();
		
		while (checker.getStatus() == false) {
//			System.out.println("false");
		}
		System.out.println("true");
		
	}
	
}
