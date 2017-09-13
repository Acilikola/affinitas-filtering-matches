package com.osmancan.affinitas.matchfilter.filteringmatches.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.osmancan.affinitas.matchfilter.filteringmatches.model.Match;

public interface MatchRepository extends JpaRepository<Match, Integer>, JpaSpecificationExecutor<Match> {

}
