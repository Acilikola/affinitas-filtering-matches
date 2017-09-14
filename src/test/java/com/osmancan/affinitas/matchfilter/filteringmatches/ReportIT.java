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
}