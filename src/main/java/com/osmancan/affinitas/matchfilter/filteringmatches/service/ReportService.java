package com.osmancan.affinitas.matchfilter.filteringmatches.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
	
	// add filter for matches with compatibility score within specified range
	public void addCompatibilityScoreFilter(Specs<Match> specs, BigDecimal compScoreBD, String mode) {
		BigDecimal hundredBD = new BigDecimal(100);
		BigDecimal checkValBD = compScoreBD.divide(hundredBD, 2, RoundingMode.HALF_UP);
		Specification<Match> spec = null;
		if(mode.equals("min")) {
			spec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.ge(root.get("compatibilityScore"), checkValBD);
		} else if(mode.equals("max")) {
			spec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.le(root.get("compatibilityScore"), checkValBD);
		}
		if(spec != null)
			specs.and(spec);
	}
	
	// add filter for matches with age within specified range
	public void addAgeFilter(Specs<Match> specs, Integer age, String mode) {
		Specification<Match> spec = null;
		if(mode.equals("min")) {
			spec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.ge(root.get("age"), age);
		} else if(mode.equals("max")) {
			spec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.le(root.get("age"), age);
		}
		if(spec != null)
			specs.and(spec);
	}
	
	// add filter for matches with height within specified range
	public void addHeightFilter(Specs<Match> specs, Integer height, String mode) {
		Specification<Match> spec = null;
		if(mode.equals("min")) {
			spec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.ge(root.get("heightInCm"), height);
		} else if(mode.equals("max")) {
			spec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.le(root.get("heightInCm"), height);
		}
		if(spec != null)
			specs.and(spec);
	}
	
	//fetch matches based on filters or returns all when no filters are given
	public List<Match> getByFilter(String hasPhoto, String inContact, String favourite, String compScoreMin, String compScoreMax,
			String ageMin, String ageMax, String heightMin, String heightMax) {
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
		
		if(compScoreMin != null) {
			BigDecimal compScoreMinBD = new BigDecimal(compScoreMin);
			addCompatibilityScoreFilter(specs, compScoreMinBD, "min");
		}
		
		if(compScoreMax != null) {
			BigDecimal compScoreMaxBD = new BigDecimal(compScoreMax);
			addCompatibilityScoreFilter(specs, compScoreMaxBD, "max");
		}
		
		if(ageMin != null) {
			Integer ageMinInt = Integer.parseInt(ageMin);
			addAgeFilter(specs, ageMinInt, "min");
		}
		
		if(ageMax != null) {
			Integer ageMaxInt = Integer.parseInt(ageMax);
			addAgeFilter(specs, ageMaxInt, "max");
		}
		
		if(heightMin != null) {
			Integer heightMinInt = Integer.parseInt(heightMin);
			addHeightFilter(specs, heightMinInt, "min");
		}
		
		if(heightMax != null) {
			Integer heightMaxInt = Integer.parseInt(heightMax);
			addHeightFilter(specs, heightMaxInt, "max");
		}

		return matchRepository.findAll(specs);
	}
}
