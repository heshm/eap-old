package org.eap.framework.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController extends BaseController {
	
	@Autowired
    private SessionRegistry sessionRegistry;
	
	@GetMapping("/index")
	public String index(){
		return "index";
	}
	
	@GetMapping("/online/count")
	public @ResponseBody Integer getOnlineCount() {
		List<Object> principals = sessionRegistry.getAllPrincipals()
				.stream()
		        .filter(principal -> principal instanceof UserDetails)
		        .map(UserDetails.class::cast)
		        .collect(Collectors.toList());
		return principals.size();
	}
	
	@GetMapping("/dashboard")
	public String dashboard() {
		return "dashboard";
	}

}
