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
package cz.symbiont_it.cdiqi.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The way to pass parameters to asynchronous business method call.
 * 
 * @author Martin Kouba
 */
public class AsyncInvocationContext implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String, Object> dataMap = new HashMap<String, Object>();

	/**
	 * 
	 * @param <T>
	 * @param key
	 * @param value
	 * @return
	 */
	public <T extends Object> AsyncInvocationContext put(String key,
			T value) {
		dataMap.put(key, value);
		return this;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return dataMap.get(key);
	}

}
