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

import java.lang.annotation.Annotation;

/**
 * Asynchronous event wrapper. Target event of this wrapper is fired
 * asynchronously in separate thread.
 * 
 * @author Martin Kouba
 */
public final class AsyncEvent {

	private final Object targetEvent;

	private final Annotation[] qualifiers;

	public AsyncEvent(Object targetEvent, Annotation... qualifiers) {
		super();
		
		if(targetEvent == null)
			throw new IllegalArgumentException("Target event must not be null");
		
		this.targetEvent = targetEvent;
		this.qualifiers = qualifiers;
	}

	public Object getTargetEvent() {
		return targetEvent;
	}

	public Annotation[] getQualifiers() {
		return qualifiers;
	}
	
}
