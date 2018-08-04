package org.eap.framework.web.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseEndpoint {
	
	protected Logger Logger = LoggerFactory.getLogger(BaseEndpoint.class);
	
	protected final static String DELETED = "{\"status\":\"deleted\"}";
	
	protected final static String CREATED = "{\"status\":\"created\"}";
	
	protected final static String UPDATED = "{\"status\":\"updated\"}";
	

}
