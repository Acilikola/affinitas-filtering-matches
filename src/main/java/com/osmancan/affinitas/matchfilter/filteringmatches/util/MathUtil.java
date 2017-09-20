package com.osmancan.affinitas.matchfilter.filteringmatches.util;

import com.osmancan.affinitas.matchfilter.filteringmatches.model.City;

public class MathUtil {

	public final static double RADIUS_OF_EARTH_IN_KM = 6371;

	public static int calculateDistanceBetweenCitiesInKm(City city1, City city2) {
		int ret = 0;
		double latDiff = Math.toRadians((city2.getLat().subtract(city1.getLat()).doubleValue()));
		double lonDiff = Math.toRadians((city2.getLon().subtract(city1.getLon()).doubleValue()));
		double diff = 0.5 - Math.cos(latDiff)/2 + 
				Math.cos(Math.toRadians(city1.getLat().doubleValue())) * 
				Math.cos(Math.toRadians(city2.getLat().doubleValue())) * (1 - Math.cos(lonDiff)) / 2;
		ret = (int) (2 *  RADIUS_OF_EARTH_IN_KM * Math.asin(Math.sqrt(diff)));
		return ret;
	}
}
