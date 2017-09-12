package com.osmancan.affinitas.matchfilter.filteringmatches;

import org.junit.Test;

import com.jayway.restassured.RestAssured;

//Maven Failsafe Integration Test for Report Page
public class ReportIT {

	@Test
	public void reportAliveTest() {
		RestAssured.get("/report").then().assertThat().statusCode(200);
	}
}