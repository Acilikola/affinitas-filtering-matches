package com.osmancan.affinitas.matchfilter.filteringmatches;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.osmancan.affinitas.matchfilter.filteringmatches.config.AppConfig;

@SpringBootApplication
@Import(AppConfig.class)
public class FilteringMatchesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilteringMatchesApplication.class, args);
	}
}
