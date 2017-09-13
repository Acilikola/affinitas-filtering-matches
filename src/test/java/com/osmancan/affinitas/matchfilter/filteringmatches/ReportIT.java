package com.osmancan.affinitas.matchfilter.filteringmatches;

import org.junit.Test;

import com.jayway.restassured.RestAssured;
import static org.hamcrest.Matchers.*;


//Maven Failsafe Integration and Unit Tests for Report Page
public class ReportIT {

	//tests that the /report url is reachable
	@Test
	public void reportAliveTest() {
		RestAssured.get("/report").then().assertThat().statusCode(200);
	}
	
	//tests that the /report/matches url is reachable and that the back-end database fetches all 25 matches correctly
	@Test
	public void fetchMatchesTest() {
		RestAssured.get("/report/matches").then().assertThat().statusCode(200).and().assertThat().body("size()", is(25));
	}
	
	
}