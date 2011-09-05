package cz.symbiont_it.cdiqi.tests.basic;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Martin Kouba
 */
@RequestScoped
public class SimpleRequestScopedService {

	private static final Logger logger = LoggerFactory
			.getLogger(SimpleRequestScopedService.class);

	private String id;

	@PostConstruct
	public void create() {
		id = UUID.randomUUID().toString();
	}

	/**
	 * @param clientId
	 * @return service id
	 */
	public String doSomeBusiness(String clientId) {
		logger.info("{} is doing some business for {}", id, clientId);
		return id;
	}

}
