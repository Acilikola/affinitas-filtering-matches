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

	public Match loggedInMatch = null;

	@RequestMapping(path = "/report", method = RequestMethod.GET)
	public String reportHomePage() {
		if (loggedInMatch == null) {
			loggedInMatch = reportService.getRandomLoginMatch();
		}
		return "matchreport.html";
	}

	// obsolete --> when no params passed to getByFilter, returns same result with getAllMatches
	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping(path = "/report/matches", method = RequestMethod.GET) public List<Match> getAllMatches() { return
	 * reportService.getAllMatches(); }
	 */

	// no params => returns all matches
	// with params => applies specified filters (allows multiple)
	@ResponseBody
	@RequestMapping(path = "/report/matches", method = RequestMethod.GET)
	public List<Match> getByFilter(@RequestParam Map<String, String> params) {
		String hasPhoto = params.get("hasPhoto");
		String inContact = params.get("inContact");
		String favourite = params.get("favourite");
		String compScoreMin = params.get("compScoreMin");
		String compScoreMax = params.get("compScoreMax");
		String ageMin = params.get("ageMin");
		String ageMax = params.get("ageMax");
		String heightMin = params.get("heightMin");
		String heightMax = params.get("heightMax");
		String distanceMax = params.get("distanceMax");
		return reportService.getByFilter(loggedInMatch, hasPhoto, inContact, favourite, compScoreMin, compScoreMax, ageMin, ageMax,
				heightMin, heightMax, distanceMax);
	}
	
	@ResponseBody
	@RequestMapping(path = "/report/loggedin", method = RequestMethod.GET)
	public Match getLoggedInUser() {
		if (loggedInMatch == null) {
			loggedInMatch = reportService.getRandomLoginMatch();
		}
		return loggedInMatch;
	}
}
