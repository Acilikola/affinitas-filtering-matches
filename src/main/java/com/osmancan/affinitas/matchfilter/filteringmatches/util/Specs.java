package com.osmancan.affinitas.matchfilter.filteringmatches.util;

import java.io.Serializable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

public class Specs<T> implements Specification<T>, Serializable {

	private Specifications<T> specs;

	private Specs() {
	}

	public static <T> Specs<T> getSpecification() {
		return new Specs();
	}

	private boolean getSpecs(Specification<T> spec) {
		if (this.specs == null) {
			this.setSpecs(Specifications.where(spec));
			return false;
		} else {
			return true;
		}
	}

	private void setSpecs(Specifications<T> newSpecs) {
		this.specs = newSpecs;
	}

	public void and(Specification<T> spec) {
		if (this.getSpecs(spec)) {
			this.setSpecs(this.specs.and(spec));
		}

	}

	public void or(Specification<T> spec) {
		if (this.getSpecs(spec)) {
			this.setSpecs(this.specs.or(spec));
		}

	}

	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		return this.specs == null ? null : this.specs.toPredicate(root, query, cb);
	}
}
