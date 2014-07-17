package Intermediary;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import Extractor.DataHandler;
import Extractor.FileHandler;

@WebListener
public class UpdateChecker implements ServletContextListener {

	private static final int UPDATE_HOUR = 5; // set this, 24hr time
	private static final int UPDATE_MINUTES = 27; // and this too
	private static final String TIME_ZONE = "Australia/Sydney"; // can change
																// this
	private static Calendar c; // used for storing time of last update
	private static int INIT_DELAY = 0;
	private static final int PERIOD = 5; // change this
	private static ScheduledExecutorService scheduler;
	private static FileHandler fileHandler = new DataHandler();
	private static final int MILLIS_IN_30_SECS = 30000;
	private static ServletContext ctx;

	// used for testing
	public void checkForUpdates() {
		CheckTDX task = new CheckTDX(this);
		scheduler = Executors.newSingleThreadScheduledExecutor();
		exitWhenTimeToUpdate();
		// change the time units to hours if need be, left it as
		// minutes for testing purposes
		scheduler.scheduleAtFixedRate(task, INIT_DELAY, PERIOD,
				TimeUnit.MINUTES);
	}

	@Override
	// used by servlet context/container
	public void contextInitialized(ServletContextEvent event) {
		ctx = event.getServletContext();
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
		ctx.setAttribute("mostRecentData", fileHandler.extractData(p.toFile()));
		ctx.setAttribute("timeOfRetrieval", c);
	}

	// TODO maybe turn this into wait/notify... not sure how to do
	// this is the simplest fix until then
	private void exitWhenTimeToUpdate() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE));

		// sits in here until hour and minutes are correct
		while (c.get(Calendar.HOUR_OF_DAY) != UPDATE_HOUR
				&& c.get(Calendar.MINUTE) != UPDATE_MINUTES) {
			c = Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE));
			// sleep for half a minute to not waste resources
			try {
				Thread.sleep(MILLIS_IN_30_SECS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
