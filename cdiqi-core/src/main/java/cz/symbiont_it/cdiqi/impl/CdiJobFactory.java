/*
 * Copyright 2011, Symbiont v.o.s
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
package cz.symbiont_it.cdiqi.impl;

import javax.inject.Inject;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

/**
 * In order to avoid memory leaks with <tt>@Dependent</tt> jobs (see <a
 * href="https://issues.jboss.org/browse/WELD-920">WELD-920</a>) this factory
 * does not "produce" jobs directly.
 * 
 * It produces request scoped delegate. In fact it does not produce the real
 * instance but uninitialized proxy instead (see <a
 * href="https://issues.jboss.org/browse/CDI-125">CDI-125</a>). Unless CDI
 * implementations work this way this factory will not work correctly.
 * 
 * @author Martin Kouba
 * @see JobFactory
 * @see JobExecutionDelegate
 */
public class CdiJobFactory implements JobFactory {

	@Inject
	private JobExecutionDelegate jobExecutionDelegate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.spi.JobFactory#newJob(org.quartz.spi.TriggerFiredBundle,
	 * org.quartz.Scheduler)
	 */
	public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler)
			throws SchedulerException {
		return jobExecutionDelegate;
	}

}
