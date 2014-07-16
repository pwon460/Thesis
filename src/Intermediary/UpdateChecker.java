package Intermediary;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class UpdateChecker implements ServletContextListener {

	private static boolean hasUpdate = false;
	private static final int UPDATE_DAY = 1;
	private static final int UPDATE_HOUR = 0; // set this
	private static final int UPDATE_MINUTES = 0; // and this too 
	private static final String TIME_ZONE = "Australia/Sydney"; // can change this
	private static Calendar c = Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE));
	private static final int INIT_DELAY = 0;
	private static final int NUM_UNIT = 1;
    private ScheduledExecutorService scheduler;

    @Override
    // ignore this for now
    public void contextInitialized(ServletContextEvent event) {
    	Calendar c = Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE));
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new CheckTDX(this), 0, 1, TimeUnit.DAYS);
    }

    // used for testing
	public void checkForUpdates() {
		CheckTDX task = new CheckTDX(this);
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(task, INIT_DELAY, NUM_UNIT, TimeUnit.DAYS);
	}

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }
	
	public void setUpdate(boolean value) {
		hasUpdate = value;
	}

	public boolean getStatus() {
		return hasUpdate;
	}
		
	private void setNextUpdateDay (Calendar c) {
		c.set(Calendar.DAY_OF_WEEK, UPDATE_DAY);
	}

	private void setUpdateTime (Calendar c) {
		c.set(Calendar.HOUR, UPDATE_HOUR);
		c.set(Calendar.MINUTE, UPDATE_MINUTES);
	}

}
