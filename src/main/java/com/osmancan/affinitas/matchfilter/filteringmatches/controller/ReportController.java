package com.osmancan.affinitas.matchfilter.filteringmatches.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.osmancan.affinitas.matchfilter.filteringmatches.model.Match;
import com.osmancan.affinitas.matchfilter.filteringmatches.service.ReportService;

@Controller
public class ReportController {

	@Autowired
	private ReportService reportService;

	@RequestMapping(path = "/report", method = RequestMethod.GET)
	public String reportHomePage() {
		return "matchreport.html";
	}

	//obsolete --> when no params passed to getByFilter, returns same result with getAllMatches
	/*
	@ResponseBody
	@RequestMapping(path = "/report/matches", method = RequestMethod.GET)
	public List<Match> getAllMatches() {
		 return reportService.getAllMatches();
	}
	*/
	
	//no params => returns all matches
	//with params => applies specified filters (allows multiple)
	@ResponseBody
	@RequestMapping(path = "/report/matches", method = RequestMethod.GET)
	public List<Match> getByFilter(@RequestParam Map<String, String> params) {
		String hasPhoto = params.get("hasPhoto");
		String inContact = params.get("inContact");
		String favourite = params.get("favourite");
		return reportService.getByFilter(hasPhoto, inContact, favourite);
	}
}
