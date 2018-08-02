package org.eap.common.param.controller;

import org.eap.framework.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonDemoController extends BaseController {
	
	
	@GetMapping("demo")
	public String index(){

		return "demo";
	}
	

}
