package com.osmancan.affinitas.matchfilter.filteringmatches;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ReportController {
	
//	@RequestMapping(path = "/", method = RequestMethod.GET)
//	public String homePage() {
//		return "index.html";
//	}
	
	@RequestMapping(path = "/report", method = RequestMethod.GET)
	public String reportHomePage() {
		return "matchreport.html";
	}
}
