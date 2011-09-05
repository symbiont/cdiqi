cdiqi - CDI Quartz Scheduler integration
=========================

__cdiqi__ provides simple integration of [Quartz Scheduler](http://www.quartz-scheduler.org/) into CDI application; currently only [Weld](http://seamframework.org/Weld) implementation is provided.
It's useful for developers who are familiar with Quartz API (Job, Trigger, etc.).
In fact it is not a real CDI extension and doesn't try to fit quartz classes into CDI programming model. 
Basically __cdiqi__  just forces Quartz to use CDI beans as its jobs (via org.quartz.spi.JobFactory) and bounds request context to every job execution so that it's possible to use @RequestScoped, @Dependent and @ApplicationScoped jobs and beans. 
@ApplicationScoped jobs must of course solve thread safety.
__cdiqi__ also provides very basic support for asynchronous execution (see Examples).

A different way of Quartz Scheduler integration is provided by [Seam 3 Cron module](http://seamframework.org/Seam3/CronModule) - it has its own API and may use Quartz as implementation.

FAQ
---
1. Why is Weld impl needed?  
_To provide request scope functionality. CDI spec does not define any convenient context related stuff and we do not want to reinvent the wheel. So we use context related stuff from weld-api. __cdiqi__ is not pure CDI extension._

2. What versions of Weld are supported?  
_Tested on 1.1.2 (JBoss AS7 module)._

3. How about logging?  
___cdiqi__ uses slf4j for logging._

4. How is __cdiqi__ tested?  
___cdiqi__ is tested with Arquillian on JBoss AS7 (see test sources). We only test managed beans - not session beans (EJB spec)._

5. What version of Quartz scheduler is used?  
___cdiqi__ uses 2.0.2 right now._

6. Where to get it?  
_At the moment the only way is to build it from source (mvn package -Dmaven.test.skip=true :-)). __cdiqi__ is not hosted on any maven repo._ 

Known issues
------------
BoundRequestJobListener uses JobExecutionContext volatile data map for request context storage. Its not obvious whether serialization of execution context is used somewhere in Quartz. 
If running into similiar problems make all CDI jobs serializable to prevent serialization problems.  

Examples
--------

### Scheduling job
1. Write your jobs as regular CDI beans (@Dependant, @ApplicationScoped, @RequestScoped beans are supported)
2. Inject QuartzSchedulerManager
3. Schedule jobs / start scheduler (or vice versa; the best place would be application start)

	@Inject  
	private QuartzSchedulerManager quartzManager;
	
	...  
	 JobDetail job01 = newJob(SimpleJob.class).withIdentity("testJob1", "testGroup1").build();  
     Trigger trigger01 = newTrigger().withIdentity("trigger1", "testGroup1")  
		 .startNow().withSchedule(repeatSecondlyForTotalCount(10, 2))  
		 .build();  
	 quartzManager.schedule(job01, trigger01);  
		 
     quartzManager.start();	
     ...
	
### Execute CDI bean business method asynchronously

1. Write your asynchronous service - CDI bean (@Dependant, @ApplicationScoped or @RequestScoped) implementing cz.symbiont_it.cdiqi.api.Asynchronous
2. Start scheduler
3. Execute asynchronously

	public class SimpleAsyncService implements Asynchronous {
	
		public void execute(AsyncInvocationContext ctx) {
			// Do something asynchronously
		}
	}

	@Inject  
	private QuartzSchedulerManager quartzManager;
	
	...  
	quartzManager.start();  
	quartzManager.executeAsynchronously(SimpleAsyncService.class);  
	...