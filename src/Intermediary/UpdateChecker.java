package Intermediary;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import Extractor.DataHandler;
import Extractor.FileHandler;

public class UpdateChecker implements ServletContextListener {

	private static final int UPDATE_DAY = 7; // scheduled to update every 7 days
	private static final int UPDATE_HOUR = 0; // set this, 24hr time
	private static final int UPDATE_MINUTES = 20; // and this too 
	private static final String TIME_ZONE = "Australia/Sydney"; // can change this
	private static final int HOUR_TO_MILLI = 3600000;
	private static final int MIN_TO_MILLI = 60000;
	private static final int SEC_TO_MILLI = 1000;
	private static Calendar c;
	private static int INIT_DELAY = 0;
	private static final int PERIOD = 1;
    private static ScheduledExecutorService scheduler;
    private static FileHandler fileHandler = new DataHandler();
    private static File updatedData;


    // used for testing
	public void checkForUpdates() {
		long delay = calcDelay();
		
		if (delay < 0) {
			delay = 0;
		}
		
		// calculate delay between now and scheduled time in milliseconds		
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("thread awake now!");
		
		CheckTDX task = new CheckTDX(this);
		scheduler = Executors.newSingleThreadScheduledExecutor();
		// timeunit determines units for both initDelay and PERIOD
		scheduler.scheduleAtFixedRate(task, INIT_DELAY, PERIOD, TimeUnit.DAYS);

	}
	
	// given current time in HH:mm:ss:zzzz, calculate how much time to wait until
	// the time that this task should check with tdx server
    private long calcDelay() {
    	int delay = 0;
		Calendar tempCal = c.getInstance(TimeZone.getTimeZone(TIME_ZONE));
		int currHr = tempCal.get(Calendar.HOUR_OF_DAY);
		int currMin = tempCal.get(Calendar.MINUTE);
		int currSec = tempCal.get(Calendar.SECOND);
		int currMilli = tempCal.get(Calendar.MILLISECOND);
		
		System.out.println(currHr + ":" + currMin + ":" + currSec + ":" + currMilli); 
		if (currHr < UPDATE_HOUR) {
			delay += (UPDATE_HOUR - currHr) * HOUR_TO_MILLI;
		} else {
			delay += (currHr - UPDATE_HOUR) * HOUR_TO_MILLI;
		}
		
		System.out.println(delay);
		
		if (currMin < UPDATE_MINUTES) {
			delay += (UPDATE_MINUTES - currMin) * MIN_TO_MILLI;
		} else {
			delay += (currMin - UPDATE_MINUTES) * MIN_TO_MILLI;
		}
		System.out.println(delay);
		
		if (currSec > 0) {
			delay += currSec * SEC_TO_MILLI;
		}
		System.out.println(delay);
		
		if (currMilli > 0) {
			delay += currMilli;
		}
		System.out.println(delay);
		
		return delay;
	}

	@Override
    // used by servlet context/container
    public void contextInitialized(ServletContextEvent event) {
    	Calendar c = Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE));
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new CheckTDX(this), 0, 1, TimeUnit.DAYS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }
	
    // handle extraction of new file
	public void handleUpdate(String path) {
		// save timestamp of new update/version of data
		c = Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE));
		Path p = Paths.get(path);
		updatedData = fileHandler.extractData(p.toFile());
	}

	private void setNextUpdateDay (Calendar c) {
		c.set(Calendar.DAY_OF_WEEK, UPDATE_DAY);
	}

	private void setUpdateTime (Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, UPDATE_HOUR);
		c.set(Calendar.MINUTE, UPDATE_MINUTES);
	}

}
