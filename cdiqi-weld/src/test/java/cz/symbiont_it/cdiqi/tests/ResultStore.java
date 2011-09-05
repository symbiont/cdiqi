package cz.symbiont_it.cdiqi.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Martin Kouba
 */
public abstract class ResultStore {
	
	/**
	 * Maps clientId to list of results
	 */
	private Map<String, List<String>> resultMap = new HashMap<String, List<String>>();

	/**
	 * 
	 */
	public synchronized void storeResult(String clientId, String result) {

		if (!resultMap.containsKey(clientId))
			resultMap.put(clientId, new ArrayList<String>());

		resultMap.get(clientId).add(result);
	}

	/**
	 * 
	 * @param clientId
	 * @return
	 */
	public int getResultsCount(String clientId) {
		return resultMap.get(clientId).size();
	}

	/**
	 * 
	 * @param clientId
	 * @return
	 */
	public String getResult(String clientId, int idx) {
		return resultMap.get(clientId).get(idx);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasUniqueResults() {

		List<String> allResults = new ArrayList<String>();
		for (Map.Entry<String, List<String>> entry : resultMap.entrySet()) {
			allResults.addAll(entry.getValue());
		}
		Set<String> allResultsSet = new HashSet<String>(allResults);

		return allResults.size() == allResultsSet.size();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResultStore [resultMap=").append(resultMap).append("]");
		return builder.toString();
	}
	
	/**
	 * 
	 */
	public synchronized void clear() {
		resultMap.clear();
	}

}
