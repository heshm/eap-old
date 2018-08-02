package org.eap.common.authority.manager;

import org.eap.framework.domain.AdminUser;

public interface UserManager {
	
	AdminUser getOneUser(String userId);

}
