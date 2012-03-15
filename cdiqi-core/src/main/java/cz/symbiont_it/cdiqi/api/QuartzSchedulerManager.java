/*
 * Copyright 2012, Symbiont v.o.s
 *	
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.symbiont_it.cdiqi.api;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

/**
 * Quartz scheduler manager.
 * 
 * @author Martin Kouba
 */
public interface QuartzSchedulerManager {

	public static final String QUARTZ_PROPERTY_FILE_NAME = "cdiqi-quartz.properties";
	
	/**
	 * Perform init.
	 * 
	 * @throws SchedulerException
	 */
	public void init() throws SchedulerException;

	/**
	 * @return <code>true</code> if initialized, <code>false</code> otherwise
	 */
	public boolean isInitialized();

	/**
	 * Start underlying quartz scheduler. If not initialized perform init first.
	 * 
	 * @throws SchedulerException
	 */
	public void start() throws SchedulerException;

	/**
	 * Pause underlying quartz scheduler.
	 * 
	 * @throws SchedulerException
	 */
	public void standby() throws SchedulerException;

	/**
	 * Deletes all scheduling data.
	 * 
	 * @throws SchedulerException
	 */
	public void clear() throws SchedulerException;

	/**
	 * Schedule new job.
	 * 
	 * @param jobDetail
	 * @param trigger
	 * @throws SchedulerException
	 */
	public void schedule(JobDetail jobDetail, Trigger trigger)
			throws SchedulerException;

}
