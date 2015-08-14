package com.intelligrape.core.impl.schedulers;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This service executes scheduled jobs
 *
 */
@Component
public class CQSchedulerExample {

    /** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /** The scheduler for rescheduling jobs. */
    @Reference
    private Scheduler scheduler;

    protected void activate(final ComponentContext componentContext) throws Exception {
	// case 1: with addJob() method: executes the job every minute
	final String schedulingExpression = "0 * * * * ?";
	final String jobName1 = "case1";
	final Map<String, Serializable> config1 = new HashMap<String, Serializable>();
	final boolean canRunConcurrently = true;
	final Runnable job1 = new Runnable() {
	    @Override
	    public void run() {
		log.info("Executing job1");
	    }
	};
	try {
	    this.scheduler.addJob(jobName1, job1, config1, schedulingExpression, canRunConcurrently);
	} catch (final Exception e) {
	    job1.run();
	}

	// case 2: with addPeriodicJob(): executes the job every 3 minutes
	final String jobName2 = "case2";
	final long period = 180;
	final Map<String, Serializable> config2 = new HashMap<String, Serializable>();
	final Runnable job2 = new Runnable() {
	    @Override
	    public void run() {
		log.info("Executing job2");
	    }
	};
	try {
	    this.scheduler.addPeriodicJob(jobName2, job2, config2, period, canRunConcurrently);
	} catch (final Exception e) {
	    job2.run();
	}

	// case 3: with fireJobAt(): executes the job at a specific date (date
	// of deployment + delay of 30 seconds)
	final String jobName3 = "case3";
	final long delay = 30 * 1000;
	final Date fireDate = new Date();
	fireDate.setTime(System.currentTimeMillis() + delay);
	final Map<String, Serializable> config3 = new HashMap<String, Serializable>();
	final Runnable job3 = new Runnable() {
	    @Override
	    public void run() {
		log.info("Executing job3 at date: {} with a delay of: {} seconds", fireDate, delay / 1000);
	    }
	};
	try {
	    this.scheduler.fireJobAt(jobName3, job3, config3, fireDate);
	} catch (final Exception e) {
	    job3.run();
	}
    }

    protected void deactivate(final ComponentContext componentContext) {
	log.info("Deactivated, goodbye!");
    }

}
//
// import org.apache.felix.scr.annotations.Component;
// import org.apache.felix.scr.annotations.Reference;
// import org.apache.felix.scr.annotations.Service;
// import org.apache.sling.api.resource.ResourceResolverFactory;
// import org.apache.sling.jcr.api.SlingRepository;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
//
// import com.intelligrape.core.HelloService;
//
// @Component
// @Service(value = Runnable.class)
// // @Property(name = "scheduler.expression", value = "0 0/5 * 1/1 * ? *")
// public class CQSchedulerExample implements Runnable {
//
// /** Default log. */
// protected final Logger log = LoggerFactory.getLogger(this.getClass());
//
// @Reference
// private ResourceResolverFactory resolverFactory;
//
// @Reference
// private HelloService createResourceService;
//
// @Reference
// private SlingRepository repository;
//
// @Override
// public void run() {
// // log.info("Executing a cron job (job#1) through the whiteboard pattern");
// // System.out.println(new Date() +
// // "__________*******************************________________________");
// // createNode("content/bootcamp/");
// }
// }
// import java.io.Serializable;
// import java.lang.management.ManagementFactory;
// import java.util.Date;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.Properties;
// import java.util.Set;
//
// import javax.management.MBeanServer;
// //import MBean API
// import javax.management.MBeanServerConnection;
// import javax.management.ObjectName;
//
// import org.apache.felix.scr.annotations.Component;
// import org.apache.felix.scr.annotations.Reference;
// import org.apache.felix.scr.annotations.Service;
// import org.apache.sling.commons.scheduler.Scheduler;
// import org.osgi.framework.BundleContext;
// import org.osgi.service.component.ComponentContext;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
//
// //Java Mail API
//
// /**
// * Just a simple DS Component
// */
// @Component(metatype = true)
// @Service
// public class CQSchedulerExample implements Runnable {
//
// /** Default log. */
// protected final Logger log = LoggerFactory.getLogger(this.getClass());
//
// private BundleContext bundleContext;
//
// @Reference
// private Scheduler scheduler;
//
// @Override
// public void run() {
// log.info("Running...");
// }
//
// protected void activate(final ComponentContext ctx) {
// this.bundleContext = ctx.getBundleContext();
//
// // Schedule a Sling Job to invoke an MBean operation to obtain number of
// // Stale Workflow items
// // case 3: with fireJobAt(): executes the job at a specific date (date
// // of deployment + delay of 30 seconds)
// final String jobName3 = "case3";
//
// final String schedulingExpression = "0 15 10 ? * MON-FRI"; // 10:15am
// // every
// // Monday,
// // Tuesday,
// // Wednesday,
// // Thursday
// // and Friday
//
// final Date fireDate = new Date();
//
// final Map<String, Serializable> config3 = new HashMap<String,
// Serializable>();
// final Runnable job = new Runnable() {
// @Override
// public void run() {
//
// final int staleItems = checkStaleItems();
//
// // if greater than 6 - email AEM admins
// if (staleItems > 6) {
// sendMail(staleItems);
// }
//
// }
// };
// try {
// // Add the Job
// this.scheduler.addJob("myJob", job, null, schedulingExpression, true);
//
// } catch (final Exception e) {
// job.run();
// }
//
// }
//
// protected void deactivate(final ComponentContext ctx) {
// this.bundleContext = null;
// }
//
// // Use MBean Logic to check the number of stale Workflow Items
// private int checkStaleItems() {
// try {
// // Create a MBeanServer class
// final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
//
// final ObjectName workflowMBean = getWorkflowMBean(server);
//
// // Get the number of stale workflowitems from AEM
// final Object staleWorkflowCount = server.invoke(workflowMBean,
// "countStaleWorkflows",
// new Object[] { null }, new String[] { String.class.getName() });
//
// final int mystaleCount = (Integer) staleWorkflowCount;
//
// // Return the number of stale items
// return mystaleCount;
//
// } catch (final Exception e) {
// e.printStackTrace();
// }
// return -1;
// }
//
// private static ObjectName getWorkflowMBean(final MBeanServerConnection
// server) {
// try {
// final Set<ObjectName> names = server.queryNames(new ObjectName(
// "com.adobe.granite.workflow:type=Maintenance,*"), null);
//
// if (names.isEmpty())
// return null;
//
// return names.iterator().next();
// } catch (final Exception e) {
// e.printStackTrace();
// }
// return null;
// }
//
// private void sendMail(final int count) {
// // Recipient's email ID needs to be mentioned.
// final String to = "SET THE TO ADDRESS";
//
// // Sender's email ID needs to be mentioned
// final String from = "SET THE FROM ADDRESS";
//
// // Assuming you are sending email from localhost
// final String host = "SET SMTP MAIL SERVER";
//
// // Get system properties
// final Properties properties = System.getProperties();
//
// // Setup mail server
// properties.setProperty("mail.smtp.host", host);
// properties.setProperty("mail.smtp.user", "Enter STMP USER"); // Set the
// // SMTP
// // Mail
// // user
//
// // Get the default Session object.
// // javax.mail.Authenticator authenticator = new
// // javax.mail.PasswordAuthentication("username", "password");
// // Session session = Session.getDefaultInstance(properties);
// //
// // try{
// // // Create a default MimeMessage object.
// // MimeMessage message = new MimeMessage(session);
// //
// // // Set From: header field of the header.
// // message.setFrom(new InternetAddress(from));
// //
// // // Set To: header field of the header.
// // message.addRecipient(Message.RecipientType.TO,
// // new InternetAddress(to));
// //
// // // Set Subject: header field
// // message.setSubject("Stale AEM Workflow Items");
// //
// // // Now set the actual message
// // message.setText("Please note that there are "+count
// // +" stale AEM workflows");
// //
// // // Send message
// // Transport.send(message);
// // log.info("Stale mail notification message sent message successfully....");
// // }catch (Exception ex) {
// // ex.printStackTrace();
// // }
// }
//
// }