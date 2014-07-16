package Intermediary;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

	private static final int UPDATE_HOUR = 4; // set this, 24hr time
	private static final int UPDATE_MINUTES = 44; // and this too 
	private static final String TIME_ZONE = "Australia/Sydney"; // can change this
	private static Calendar c; // used for storing time of last update
	private static int INIT_DELAY = 0;
	private static final int PERIOD = 1;
    private static ScheduledExecutorService scheduler;
    private static FileHandler fileHandler = new DataHandler();
    private static File updatedData;


    // used for testing
	public void checkForUpdates() {
		CheckTDX task = new CheckTDX(this);
		scheduler = Executors.newSingleThreadScheduledExecutor();
		exitWhenTimeToUpdate();
		scheduler.scheduleAtFixedRate(task, INIT_DELAY, PERIOD, TimeUnit.DAYS);
	}
	
	@Override
    // used by servlet context/container
    public void contextInitialized(ServletContextEvent event) {
    	checkForUpdates();
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

	// TODO maybe turn this into wait/notify... not sure how to do
	// this was simplest fix until then
	private void exitWhenTimeToUpdate () {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE));
		
		// sits in here until hour is correct
		while (c.get(Calendar.HOUR_OF_DAY) != UPDATE_HOUR) {
			c = Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE));
		}
		
		// sits in here until minutes are correct
		while (c.get(Calendar.MINUTE) != UPDATE_MINUTES) {
			c = Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE));
		}
	}

}
