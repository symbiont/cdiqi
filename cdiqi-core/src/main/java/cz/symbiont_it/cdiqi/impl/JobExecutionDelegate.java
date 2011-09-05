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

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Delegates job execution to injected job.
 * 
 * @author Martin Kouba
 * @see CdiJobFactory
 */
@RequestScoped
public class JobExecutionDelegate implements Job {

	@Inject
	private Instance<Job> jobInstance;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		Instance<? extends Job> child = jobInstance.select(context
				.getJobDetail().getJobClass());

		if (child.isAmbiguous())
			throw new IllegalStateException(
					"Cannot produce job: ambiguous instance");

		if (child.isUnsatisfied())
			throw new IllegalStateException(
					"Cannot produce job: unsatisfied instance");

		child.get().execute(context);
	}

}
