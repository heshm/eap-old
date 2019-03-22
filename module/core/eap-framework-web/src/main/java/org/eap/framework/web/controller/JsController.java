package org.eap.framework.web.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/js")
public class JsController extends BaseController{

	@GetMapping("/global.js")
	public String getConfig(HttpServletRequest request,Model model) {
		Locale local = request.getLocale();
		model.addAttribute("lang", local.toLanguageTag());
		model.addAttribute("ctx", request.getContextPath());
		return "js/global.js";
	}
	
}
