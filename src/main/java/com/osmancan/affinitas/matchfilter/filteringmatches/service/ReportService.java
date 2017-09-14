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
	
	// add filter for matches with/without photos
	public void addHasPhotoFilter(Specs<Match> specs, boolean hasPhotoBoolean) {
		Specification<Match> spec = (root, criteriaQuery, criteriaBuilder) -> hasPhotoBoolean ? 
				criteriaBuilder.isNotNull(root.get("mainPhoto")) : criteriaBuilder.isNull(root.get("mainPhoto"));
		specs.and(spec);
	}

	// add filter for matches in/not in contacts
	public void addInContactFilter(Specs<Match> specs, boolean inContactBoolean) {
		Specification<Match> spec = (root, criteriaQuery, criteriaBuilder) -> inContactBoolean ? 
				criteriaBuilder.gt(root.get("contactsExchanged"), 0) : criteriaBuilder.le(root.get("contactsExchanged"), 0);
		specs.and(spec);
	}
	
	// add filter for matches favourite/not favourite
	public void addFavouriteFilter(Specs<Match> specs, boolean favouriteBoolean) {
		Specification<Match> spec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("favourite"), favouriteBoolean);
		specs.and(spec);
	}
	
	//fetch matches based on filters or returns all when no filters are given
	public List<Match> getByFilter(String hasPhoto, String inContact, String favourite) {
		Specs<Match> specs = Specs.getSpecification();
		
		if (hasPhoto != null) {
			boolean hasPhotoBoolean = Boolean.parseBoolean(hasPhoto);
			addHasPhotoFilter(specs, hasPhotoBoolean);
		}
		
		if(inContact != null) {
			boolean inContactBoolean = Boolean.parseBoolean(inContact);
			addInContactFilter(specs, inContactBoolean);
		}
		
		if(favourite != null) {
			boolean favouriteBoolean = Boolean.parseBoolean(favourite);
			addFavouriteFilter(specs, favouriteBoolean);
		}

		return matchRepository.findAll(specs);
	}
}
