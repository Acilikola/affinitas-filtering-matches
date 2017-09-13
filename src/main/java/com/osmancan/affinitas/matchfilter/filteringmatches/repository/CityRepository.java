package com.osmancan.affinitas.matchfilter.filteringmatches.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.osmancan.affinitas.matchfilter.filteringmatches.model.City;

public interface CityRepository extends JpaRepository<City, String>, JpaSpecificationExecutor<City> {

}
