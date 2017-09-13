package com.osmancan.affinitas.matchfilter.filteringmatches.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Match {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@JsonProperty(value = "display_name")
	@Column(name = "display_name")
	private String displayName;

	@JsonProperty
	@Column(name = "age")
	private Integer age;

	@JsonProperty(value = "job_title")
	@Column(name = "job_title")
	private String jobTitle;

	@JsonProperty(value = "height_in_cm")
	@Column(name = "height_in_cm")
	private Integer heightInCm;

	@ManyToOne
	@JoinColumn(name = "city")
	private City city;
	// @JsonProperty
	// @ManyToOne
	// @JoinColumn(name = "name")
	// private City city;

	@JsonProperty(value = "main_photo")
	@Column(name = "main_photo")
	private String mainPhoto;

	@JsonProperty(value = "compatibility_score")
	@Column(name = "compatibility_score")
	private BigDecimal compatibilityScore;

	@JsonProperty(value = "contacts_exchanged")
	@Column(name = "contacts_exchanged")
	private Integer contactsExchanged;

	@JsonProperty
	@Column(name = "favourite")
	private Boolean favourite;

	@JsonProperty
	@Column(name = "religion")
	private String religion;

	public Match() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public Integer getHeightInCm() {
		return heightInCm;
	}

	public void setHeightInCm(Integer heightInCm) {
		this.heightInCm = heightInCm;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getMainPhoto() {
		return mainPhoto;
	}

	public void setMainPhoto(String mainPhoto) {
		this.mainPhoto = mainPhoto;
	}

	public BigDecimal getCompatibilityScore() {
		return compatibilityScore;
	}

	public void setCompatibilityScore(BigDecimal compatibilityScore) {
		this.compatibilityScore = compatibilityScore;
	}

	public Integer getContactsExchanged() {
		return contactsExchanged;
	}

	public void setContactsExchanged(Integer contactsExchanged) {
		this.contactsExchanged = contactsExchanged;
	}

	public Boolean getFavourite() {
		return favourite;
	}

	public void setFavourite(Boolean favourite) {
		this.favourite = favourite;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((compatibilityScore == null) ? 0 : compatibilityScore.hashCode());
		result = prime * result + ((contactsExchanged == null) ? 0 : contactsExchanged.hashCode());
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((favourite == null) ? 0 : favourite.hashCode());
		result = prime * result + ((heightInCm == null) ? 0 : heightInCm.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((jobTitle == null) ? 0 : jobTitle.hashCode());
		result = prime * result + ((mainPhoto == null) ? 0 : mainPhoto.hashCode());
		result = prime * result + ((religion == null) ? 0 : religion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Match other = (Match) obj;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (compatibilityScore == null) {
			if (other.compatibilityScore != null)
				return false;
		} else if (!compatibilityScore.equals(other.compatibilityScore))
			return false;
		if (contactsExchanged == null) {
			if (other.contactsExchanged != null)
				return false;
		} else if (!contactsExchanged.equals(other.contactsExchanged))
			return false;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (favourite == null) {
			if (other.favourite != null)
				return false;
		} else if (!favourite.equals(other.favourite))
			return false;
		if (heightInCm == null) {
			if (other.heightInCm != null)
				return false;
		} else if (!heightInCm.equals(other.heightInCm))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (jobTitle == null) {
			if (other.jobTitle != null)
				return false;
		} else if (!jobTitle.equals(other.jobTitle))
			return false;
		if (mainPhoto == null) {
			if (other.mainPhoto != null)
				return false;
		} else if (!mainPhoto.equals(other.mainPhoto))
			return false;
		if (religion == null) {
			if (other.religion != null)
				return false;
		} else if (!religion.equals(other.religion))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Match [id=" + id + ", displayName=" + displayName + ", age=" + age + ", jobTitle=" + jobTitle + ", heightInCm="
				+ heightInCm + ", city=" + city + ", mainPhoto=" + mainPhoto + ", compatibilityScore=" + compatibilityScore
				+ ", contactsExchanged=" + contactsExchanged + ", favourite=" + favourite + ", religion=" + religion + "]";
	}

}
