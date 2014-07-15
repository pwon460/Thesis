package Intermediary;

import java.util.Timer;


public class UpdateChecker {
	
	private static boolean hasUpdate = false;
	
	public void checkForUpdates () {
		CheckTDX task = new CheckTDX(this);
		Timer t = new Timer();
	
		t.schedule(task, 0, 1000000);
	}
	
	public void setUpdate (boolean value) {
		hasUpdate = value;
	}
	
	public boolean getStatus () {
		return hasUpdate;
	}
	
}
