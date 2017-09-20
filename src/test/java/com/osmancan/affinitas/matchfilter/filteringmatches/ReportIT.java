package com.osmancan.affinitas.matchfilter.filteringmatches;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.osmancan.affinitas.matchfilter.filteringmatches.model.City;
import com.osmancan.affinitas.matchfilter.filteringmatches.model.Match;
import com.osmancan.affinitas.matchfilter.filteringmatches.util.MathUtil;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

//Maven Failsafe Integration and Unit Tests for Report Page
public class ReportIT {

	// tests that the /report url is reachable
	@Test
	public void reportAliveTest() {
		RestAssured.get("/report").then().assertThat().statusCode(200);
	}

	// tests that the /report/matches url is reachable and that the back-end database fetches all 25 matches correctly
	@Test
	public void fetchMatchesTest() {
		RestAssured.get("/report/matches").then().assertThat().statusCode(200).and().assertThat().body("size()", is(25));
	}

	// tests that the hasPhoto filter is working properly (with photo = 22 matches, without = 3)
	@Test
	public void hasPhotoFilterTest() {
		RestAssured.given().parameters("hasPhoto", true).then().expect().body(containsString("main_photo")).and().body("size()", is(22))
				.when().get("/report/matches");
		RestAssured.given().parameters("hasPhoto", false).then().expect().body(not(containsString("main_photo"))).and()
				.body("size()", is(3)).when().get("/report/matches");
	}
	
	// tests that the inContact filter is working properly (incontact = 12 matches, not incontact = 13)
	@Test
	public void inContactFilterTest() {
		RestAssured.given().parameters("inContact", true).then().expect().body("findAll{it.contacts_exchanged > 0}.size()", is(12))
		.when().get("/report/matches");
		RestAssured.given().parameters("inContact", false).then().expect().body("findAll{it.contacts_exchanged == 0}.size()", is(13))
		.when().get("/report/matches");
	}
	
	// tests that the favourite filter is working properly (favourite = 6 matches, not favourite = 19)
	@Test
	public void favouriteFilterTest() {
		RestAssured.given().parameters("favourite", true).then().expect().body("findAll{it.favourite}.size()", is(6))
		.when().get("/report/matches");
		RestAssured.given().parameters("favourite", false).then().expect().body("findAll{!it.favourite}.size()", is(19))
		.when().get("/report/matches");
	}
	
	// tests that the compatibilityScore filter is working properly
	@Test
	public void compatibilityScoreFilterTest() {
		RestAssured.given().parameters("compScoreMin", 87).then().expect().body("findAll{it.compatibility_score >= 0.87}.size()", is(18))
		.when().get("/report/matches");
		RestAssured.given().parameters("compScoreMax", 80).then().expect().body("findAll{it.compatibility_score <= 0.8}.size()", is(5))
		.when().get("/report/matches");
		RestAssured.given().parameters("compScoreMin", 30, "compScoreMax", 55).then().expect()
		.body("compatibility_score", everyItem(allOf(greaterThanOrEqualTo(0.3f),lessThanOrEqualTo(0.55f)))).when().get("/report/matches");
	}
	
	// tests that the age filter is working properly
	@Test
	public void ageFilterTest() {
		RestAssured.given().parameters("ageMin", 40).then().expect().body("findAll{it.age >= 40}.size()", is(14))
		.when().get("/report/matches");
		RestAssured.given().parameters("ageMax", 42).then().expect().body("findAll{it.age <= 42}.size()", is(17))
		.when().get("/report/matches");
		RestAssured.given().parameters("ageMin", 18, "ageMax", 37).then().expect()
		.body("age", everyItem(allOf(greaterThanOrEqualTo(18),lessThanOrEqualTo(37)))).when().get("/report/matches");
	}
	
	// tests that the height filter is working properly
	@Test
	public void heightFilterTest() {
		RestAssured.given().parameters("heightMin", 170).then().expect().body("findAll{it.height_in_cm >= 170}.size()", is(5))
		.when().get("/report/matches");
		RestAssured.given().parameters("heightMax", 150).then().expect().body("findAll{it.height_in_cm <= 150}.size()", is(7))
		.when().get("/report/matches");
		RestAssured.given().parameters("heightMin", 160, "heightMax", 172).then().expect()
		.body("height_in_cm", everyItem(allOf(greaterThanOrEqualTo(160),lessThanOrEqualTo(172)))).when().get("/report/matches");
	}
	
	// tests that the distance filter is working properly
	// (dummy loginCity = Leeds, cities with distance less than 251km: "Leeds", "Solihull", "Swindon", "Oxford", "Harlow")
	// (dummy loginCity2 = Leeds, cities with distance <30: "Leeds")
	// (dummy loginCity3 = Leeds, cities with distance >300: "Eastbourne", "Salisbury", "Weymouth", "Bournemouth", "Plymouth", "Inverness",
	// "Aberdeen", "Londonderry")
	@Test
	public void distanceFilterTest() {
		City city1 = new City();
		city1.setName("Leeds");
		city1.setLat(new BigDecimal(53.801277));
		city1.setLon(new BigDecimal(-1.548567));
		Integer distance = 251;
		
		Predicate<Match> isCloseEnough = e -> MathUtil.calculateDistanceBetweenCitiesInKm(city1, e.getCity()) <= (distance == 0 ? 29 : distance);
		Response response = RestAssured.get("/report/matches");
		List<Match> returnedMatches = Arrays.asList(response.getBody().as(Match[].class));
		Set<String> matchingCities = returnedMatches.stream().filter(isCloseEnough).map(Match::getCity).map(City::getName).collect(Collectors.toSet());
		assertTrue(matchingCities.containsAll(Arrays.asList("Leeds", "Solihull", "Swindon", "Oxford", "Harlow"))
				&& !matchingCities.contains("London"));
		
		Integer distance2 = 0;
		isCloseEnough = e -> MathUtil.calculateDistanceBetweenCitiesInKm(city1, e.getCity()) <= (distance2 == 0 ? 29 : distance2);
		response = RestAssured.get("/report/matches");
		returnedMatches = Arrays.asList(response.getBody().as(Match[].class));
		matchingCities = returnedMatches.stream().filter(isCloseEnough).map(Match::getCity).map(City::getName).collect(Collectors.toSet());
		assertTrue(matchingCities.size() == 1 && matchingCities.contains("Leeds"));
		
		Integer distance3 = -1;
		Predicate<Match> isFarEnough = e -> MathUtil.calculateDistanceBetweenCitiesInKm(city1, e.getCity()) > 300;
		response = RestAssured.get("/report/matches");
		returnedMatches = Arrays.asList(response.getBody().as(Match[].class));
		matchingCities = returnedMatches.stream().filter(distance3 == -1 ? isFarEnough : isCloseEnough).map(Match::getCity).map(City::getName).collect(Collectors.toSet());
		assertTrue(matchingCities.containsAll(Arrays.asList("Eastbourne", "Salisbury", "Weymouth", "Bournemouth", "Plymouth", "Inverness",
				"Aberdeen", "Londonderry"))	&& !matchingCities.contains("Leeds"));
	}
	
}