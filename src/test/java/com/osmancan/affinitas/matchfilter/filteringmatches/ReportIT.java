package com.osmancan.affinitas.matchfilter.filteringmatches;

import org.junit.Test;

import com.jayway.restassured.RestAssured;
import static org.hamcrest.Matchers.*;


//Maven Failsafe Integration Test for Report Page
public class ReportIT {

	@Test
	public void reportAliveTest() {
		RestAssured.get("/report").then().assertThat().statusCode(200);
	}
	
	@Test
	public void fetchMatchesTest() {
		RestAssured.get("/report/matches").then().assertThat().statusCode(200).and().assertThat().body("size()", greaterThan(0));
	}
}