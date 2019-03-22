package org.eap.framework.web.controller;

import org.eap.framework.web.dto.CommonDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController extends BaseController {
	
	private final static String loginView = "login";
	
	@GetMapping("/login")
	public String login(){
		return loginView;
	}
	
	@GetMapping("/loginSuccess")
	public @ResponseBody CommonDTO<String> loginSuccess(){
		CommonDTO<String> result = new CommonDTO<>();
		result.setResult(true);
		result.setMessage("Login success!");
		return result;
	}

}
