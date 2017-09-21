package com.osmancan.affinitas.matchfilter.filteringmatches.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.osmancan.affinitas.matchfilter.filteringmatches.model.City;
import com.osmancan.affinitas.matchfilter.filteringmatches.model.Match;
import com.osmancan.affinitas.matchfilter.filteringmatches.repository.MatchRepository;
import com.osmancan.affinitas.matchfilter.filteringmatches.util.MathUtil;
import com.osmancan.affinitas.matchfilter.filteringmatches.util.Specs;

@Service
public class ReportService {

	@Autowired
	private MatchRepository matchRepository;
	
	private static final int UPPER_BOUND_AGE = 95;
	private static final int UPPER_BOUND_HEIGHT = 210;
	private static final int LOWER_BOUND_DISTANCE = 30;
	private static final int UPPER_BOUND_DISTANCE = 300;

	// fetch all matches
	public List<Match> getAllMatches() {
		return matchRepository.findAll();
	}

	// return random login match
	public Match getRandomLoginMatch() {
		int totalMatches = (int) matchRepository.count();
		Random r = new Random();
		int randomMatchId = r.nextInt(totalMatches) + 1;
		return matchRepository.findOne(randomMatchId);
	}

	// add filter for matches with/without photos
	public void addHasPhotoFilter(Specs<Match> specs, boolean hasPhotoBoolean) {
		Specification<Match> spec = (root, criteriaQuery, criteriaBuilder) -> hasPhotoBoolean ? criteriaBuilder.isNotNull(root
				.get("mainPhoto")) : criteriaBuilder.isNull(root.get("mainPhoto"));
		specs.and(spec);
	}

	// add filter for matches in/not in contacts
	public void addInContactFilter(Specs<Match> specs, boolean inContactBoolean) {
		Specification<Match> spec = (root, criteriaQuery, criteriaBuilder) -> inContactBoolean ? criteriaBuilder.gt(
				root.get("contactsExchanged"), 0) : criteriaBuilder.le(root.get("contactsExchanged"), 0);
		specs.and(spec);
	}

	// add filter for matches favourite/not favourite
	public void addFavouriteFilter(Specs<Match> specs, boolean favouriteBoolean) {
		Specification<Match> spec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("favourite"), favouriteBoolean);
		specs.and(spec);
	}

	// add filter for matches with compatibility score within specified range
	public void addCompatibilityScoreFilter(Specs<Match> specs, BigDecimal compScoreBD, String mode) {
		BigDecimal hundredBD = new BigDecimal(100);
		BigDecimal checkValBD = compScoreBD.divide(hundredBD, 2, RoundingMode.HALF_UP);
		Specification<Match> spec = null;
		if (mode.equals("min")) {
			spec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.ge(root.get("compatibilityScore"), checkValBD);
		} else if (mode.equals("max")) {
			spec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.le(root.get("compatibilityScore"), checkValBD);
		}
		if (spec != null)
			specs.and(spec);
	}

	// add filter for matches with age within specified range
	public void addAgeFilter(Specs<Match> specs, Integer age, String mode) {
		Specification<Match> spec = null;
		if(age == 999) {
			spec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.gt(root.get("age"), UPPER_BOUND_AGE);
		} else {
			if (mode.equals("min")) {
				spec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.ge(root.get("age"), age);
			} else if (mode.equals("max")) {
				spec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.le(root.get("age"), age);
			}
		}
		if (spec != null)
			specs.and(spec);
	}

	// add filter for matches with height within specified range
	public void addHeightFilter(Specs<Match> specs, Integer height, String mode) {
		Specification<Match> spec = null;
		if(height == 999) {
			spec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.ge(root.get("heightInCm"), UPPER_BOUND_HEIGHT);
		} else {
			if (mode.equals("min")) {
				spec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.ge(root.get("heightInCm"), height);
			} else if (mode.equals("max")) {
				spec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.le(root.get("heightInCm"), height);
			}
		}
		if (spec != null)
			specs.and(spec);
	}
	
	// filter matches with distance within specified km (0 --> lower bound, -1 --> upper bound)
	public List<Match> useDistanceFilter(Match loggedInUser, List<Match> preFilterList, Integer distance) {
		City loginCity = loggedInUser.getCity();
		Predicate<Match> isCloseEnough = e -> MathUtil.calculateDistanceBetweenCitiesInKm(loginCity, e.getCity()) <= (distance == 0 ? LOWER_BOUND_DISTANCE-1 : distance);
		Predicate<Match> isFarEnough = e -> MathUtil.calculateDistanceBetweenCitiesInKm(loginCity, e.getCity()) > UPPER_BOUND_DISTANCE;
		return preFilterList.stream().filter(distance == -1 ? isFarEnough : isCloseEnough).collect(Collectors.toList());
	}

	// Optional: remove logged in match from results
	/*
	 * public void removeLoggedInMatch(Specs<Match> specs, Match loggedInMatch) { Specification<Match> spec = (root, criteriaQuery,
	 * criteriaBuilder) -> criteriaBuilder.notEqual(root.get("id"), loggedInMatch.getId()); specs.and(spec); }
	 */

	// fetch matches based on filters or returns all when no filters are given
	public List<Match> getByFilter(Match loggedInMatch, String hasPhoto, String inContact, String favourite, String compScoreMin,
			String compScoreMax, String ageMin, String ageMax, String heightMin, String heightMax, String distanceMax) {
		List<Match> retList = new ArrayList<Match>();
		Specs<Match> specs = Specs.getSpecification();

		if (hasPhoto != null && !hasPhoto.isEmpty()) {
			boolean hasPhotoBoolean = Boolean.parseBoolean(hasPhoto);
			addHasPhotoFilter(specs, hasPhotoBoolean);
		}

		if (inContact != null && !inContact.isEmpty()) {
			boolean inContactBoolean = Boolean.parseBoolean(inContact);
			addInContactFilter(specs, inContactBoolean);
		}

		if (favourite != null && !favourite.isEmpty()) {
			boolean favouriteBoolean = Boolean.parseBoolean(favourite);
			addFavouriteFilter(specs, favouriteBoolean);
		}

		if (compScoreMin != null && !compScoreMin.isEmpty()) {
			BigDecimal compScoreMinBD = new BigDecimal(compScoreMin);
			addCompatibilityScoreFilter(specs, compScoreMinBD, "min");
		}

		if (compScoreMax != null && !compScoreMax.isEmpty()) {
			BigDecimal compScoreMaxBD = new BigDecimal(compScoreMax);
			addCompatibilityScoreFilter(specs, compScoreMaxBD, "max");
		}

		if (ageMin != null && !ageMin.isEmpty()) {
			Integer ageMinInt = Integer.parseInt(ageMin);
			addAgeFilter(specs, ageMinInt, "min");
		}

		if (ageMax != null && !ageMax.isEmpty()) {
			Integer ageMaxInt = Integer.parseInt(ageMax);
			addAgeFilter(specs, ageMaxInt, "max");
		}

		if (heightMin != null && !heightMin.isEmpty()) {
			Integer heightMinInt = Integer.parseInt(heightMin);
			addHeightFilter(specs, heightMinInt, "min");
		}

		if (heightMax != null && !heightMax.isEmpty()) {
			Integer heightMaxInt = Integer.parseInt(heightMax);
			addHeightFilter(specs, heightMaxInt, "max");
		}
		
		// optional: although not requested in task, logically, the logged in user should not appear in match list. I will not implement
		// this to not mess my previous test cases in terms of record count asserts
		/*
		 * if (loggedInMatch != null) { System.out.println("Logged in as: " + loggedInMatch.toString()); removeLoggedInMatch(specs,
		 * loggedInMatch); }
		 */

		retList = matchRepository.findAll(specs);
		
		if (distanceMax != null && !distanceMax.isEmpty()) {
			Integer distanceMaxInt = Integer.parseInt(distanceMax);
			retList = useDistanceFilter(loggedInMatch, retList, distanceMaxInt);
		}
		
		return retList;
	}
}
