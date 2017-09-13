package com.osmancan.affinitas.matchfilter.filteringmatches.config;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.osmancan.affinitas.matchfilter.filteringmatches.model.City;
import com.osmancan.affinitas.matchfilter.filteringmatches.model.Match;
import com.osmancan.affinitas.matchfilter.filteringmatches.repository.CityRepository;
import com.osmancan.affinitas.matchfilter.filteringmatches.repository.MatchRepository;

@Configuration
@ComponentScan("com.osmancan.affinitas.matchfilter.filteringmatches.*")
@EnableJpaRepositories("com.osmancan.affinitas.matchfilter.filteringmatches.repository")
@EntityScan("com.osmancan.affinitas.matchfilter.filteringmatches.model")
@EnableAutoConfiguration
public class AppConfig {

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private CityRepository cityRepository;

	//parses the provided json and creates database tables and fills them using mapper
	@PostConstruct
	public void init() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		JsonNode array = mapper.readTree(getClass().getClassLoader().getResource("matches.json")).get("matches");
		
		List<Match> matches = mapper.readValue(array.toString(), new TypeReference<List<Match>>() {});
		List<City> cities = matches.stream().map(Match::getCity).collect(Collectors.toList());
		
		cityRepository.save(cities);
		matchRepository.save(matches);
	}
}
