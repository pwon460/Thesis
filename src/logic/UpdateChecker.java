package logic;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import extractor.DataHandler;
import extractor.FileHandler;

@WebListener
public class UpdateChecker implements ServletContextListener {

	private static final int UPDATE_HOUR = 2; // set these two values
	private static final int UPDATE_MIN = 27;
	private static final String TIME_ZONE = "Australia/Sydney"; // can change
																// this
	private static Calendar c; // used for storing time of last update
	private static Scheduler scheduler;
	private static FileHandler fileHandler = new DataHandler();
	private static ServletContext ctx;

	// used for testing
	public void checkForUpdates() {
		CheckTDX task = new CheckTDX();
		task.setChecker(this);

		SchedulerFactory factory = new StdSchedulerFactory();
		try {
			System.out.println("creating new job");
			JobDetail job = JobBuilder.newJob(task.getClass())
					.withIdentity("checkTDX", "group1").build();
			System.out.println("done!");

			System.out.println("creating trigger for the job");
			Trigger trigger = TriggerBuilder
					.newTrigger()
					.withIdentity("trigger1", "group1")
					.withSchedule(
							CronScheduleBuilder
									.dailyAtHourAndMinute(UPDATE_HOUR, UPDATE_MIN)
									.inTimeZone(TimeZone.getTimeZone(TIME_ZONE))
									.withMisfireHandlingInstructionFireAndProceed())
					.forJob("checkTDX", "group1").build();
			System.out.println("done!");

			System.out.println("creating schedule for the job");
			// schedule the job
			scheduler = factory.getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
			System.out.println("done!");

		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

	@Override
	// used by servlet context/container
	public void contextInitialized(ServletContextEvent event) {
		ctx = event.getServletContext();
		checkForUpdates();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	// handle extraction of new file
	public void handleUpdate(String path) {
		// save timestamp of new update/version of data
		System.out.println("setting context attributes");
		c = Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE));
		Path p = Paths.get(path);
		ctx.setAttribute("mostRecentData", fileHandler.extractData(p.toFile()));
		ctx.setAttribute("timeOfRetrieval", c);
	}

}
