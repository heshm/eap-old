package org.eap.framework.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController extends BaseController {
	
	@GetMapping("/index")
	public String login(){
		return "index";
	}

}
