package com.osmancan.affinitas.matchfilter.filteringmatches.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@ResponseBody
	@RequestMapping(path = "/report/matches", method = RequestMethod.GET)
	public List<Match> getAllMatches() {
		 return reportService.getAllMatches();
	}
}
