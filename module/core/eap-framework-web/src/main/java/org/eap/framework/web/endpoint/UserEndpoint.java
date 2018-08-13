package org.eap.framework.web.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.eap.framework.domain.AdminUser;
import org.eap.framework.service.EapSecService;
import org.eap.framework.web.util.AuthenticationUtils;

@RestController
public class UserEndpoint extends BaseEndpoint{
	
	@Autowired
	private EapSecService eapSecService;
	
	@GetMapping
	public String index() {
		return "{\"success\":true}";
	}
	
	@GetMapping("/readLoginUserInfo")
	public AdminUser readLoginUserInfo() {
		UserDetails userDetails = AuthenticationUtils.getPrincipal();
		return eapSecService.readAdminUserByLoginName(userDetails.getUsername());
	}

}
