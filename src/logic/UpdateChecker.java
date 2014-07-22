package logic;

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

@WebListener
public class UpdateChecker implements ServletContextListener {

	// change the timezone as appropriate
	private static final String TIME_ZONE = "Australia/Sydney";
	private static Calendar c = Calendar.getInstance(TimeZone
			.getTimeZone(TIME_ZONE)); // used for storing time of last update
	// change these two values for implementation
	// these preset values were used for testing purposes
	private static final int UPDATE_HOUR = c.get(Calendar.HOUR_OF_DAY); 
	private static final int UPDATE_MIN = (c.get(Calendar.MINUTE) + 1) % 60;

	private static Scheduler scheduler;
	private static ServletContext ctx;

	// used for testing
	public void checkForUpdates() {
		CheckTDX task = new CheckTDX();

		SchedulerFactory factory = new StdSchedulerFactory();
		try {
			System.out.println("creating new job");
			JobDetail job = JobBuilder.newJob(task.getClass())
					.withIdentity("checkTDX", "group1").build();
			job.getJobDataMap().put("context", ctx);
			job.getJobDataMap().put("timezone", TIME_ZONE);
			System.out.println("done!");

			System.out.println("creating trigger for the job");
			System.out.println("task set to be run at " + UPDATE_HOUR + ":"
					+ UPDATE_MIN + " every day");
			Trigger trigger = TriggerBuilder
					.newTrigger()
					.withIdentity("trigger1", "group1")
					.withSchedule(
							CronScheduleBuilder
									.dailyAtHourAndMinute(UPDATE_HOUR,
											UPDATE_MIN)
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

}
