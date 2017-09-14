package com.osmancan.affinitas.matchfilter.filteringmatches.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.osmancan.affinitas.matchfilter.filteringmatches.model.Match;
import com.osmancan.affinitas.matchfilter.filteringmatches.repository.MatchRepository;
import com.osmancan.affinitas.matchfilter.filteringmatches.util.Specs;

@Service
public class ReportService {

	@Autowired
	private MatchRepository matchRepository;

	//fetch all matches
	public List<Match> getAllMatches() {
		return matchRepository.findAll();
	}
	
	//add filter for matches with/without photos
	public void addHasPhotoFilter(Specs<Match> specs, boolean hasPhotoBoolean) {
		Specification<Match> spec = (root, criteriaQuery, criteriaBuilder)
				-> hasPhotoBoolean ? criteriaBuilder.isNotNull(root.get("mainPhoto")) : criteriaBuilder.isNull(root.get("mainPhoto"));
		specs.and(spec);
	}
	
	//fetch matches based on filters or returns all when no filters are given
	public List<Match> getByFilter(String hasPhoto) {

		Specs<Match> specs = Specs.getSpecification();
		if (hasPhoto != null) {
			boolean hasPhotoBoolean = Boolean.parseBoolean(hasPhoto);
			addHasPhotoFilter(specs, hasPhotoBoolean);
		}

		return matchRepository.findAll(specs);
	}
}
