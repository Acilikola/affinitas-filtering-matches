package com.osmancan.affinitas.matchfilter.filteringmatches.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.osmancan.affinitas.matchfilter.filteringmatches.model.Match;
import com.osmancan.affinitas.matchfilter.filteringmatches.repository.MatchRepository;

@Service
public class ReportService {

	@Autowired
	private MatchRepository matchRepository;

	//fetch all matches
	public List<Match> getAllMatches() {
		return matchRepository.findAll();
	}
}
