package cz.symbiont_it.cdiqi.tests.performance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;

/**
 * 
 * @author Martin Kouba
 */
@RequestScoped
public class PopularRequestScopedService {

	/**
	 * 
	 * @return
	 */
	public String doBusiness() {
		
		List<String> data = new ArrayList<String>(1024);
		for (int i = 0; i < 1024; i++) {
			data.add(UUID.randomUUID().toString());
			Collections.sort(data);
		}
		return data.get(0);
	}
	
}
