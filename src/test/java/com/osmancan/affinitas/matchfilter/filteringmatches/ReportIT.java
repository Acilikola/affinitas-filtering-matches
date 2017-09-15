package com.osmancan.affinitas.matchfilter.filteringmatches;

import org.junit.Test;

import com.jayway.restassured.RestAssured;

import static org.hamcrest.Matchers.*;

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
	
}