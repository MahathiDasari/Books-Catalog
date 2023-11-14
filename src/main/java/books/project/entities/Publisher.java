package books.project.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "publishers")

public class Publisher {
	@Id
	@Column(name = "pub_id", length = 4)
	@Pattern(regexp = "^P\\d{3}$", message = "Publisher Id must be a String and starts with 1 Capital Letters followed by 3 digits")
	private String pubId;
	@Column(name = "pub_name", length = 40)
	@Size(max = 40, message = "Maximum Length is 40")
	@NotBlank
	private String pubName;
	@Column(name = "email", length = 50)
	@Email
	@Size(max = 50, message = "Maximum Length is 50")
	private String email;
	@Column(name = "city", length = 20)
	@Size(max = 20, message = "Maximum Length is 20")
	private String city;
	@Column(name = "country", length = 30)
	@Size(max = 30, message = "Maximum Length is 30")
	private String country;

	// Publisher to title
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "publisher")
	@JsonIgnore
	private List<Title> titles = new ArrayList<Title>();

	public List<Title> getTitles() {
		return titles;
	}

	public void setTitles(List<Title> titles) {
		this.titles = titles;
	}

	public String getPubId() {
		return pubId;
	}

	public void setPubId(String pubId) {
		this.pubId = pubId;
	}

	public String getPubName() {
		return pubName;
	}

	public void setPubName(String pubName) {
		this.pubName = pubName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public int hashCode() {
		return Objects.hash(city, country, email, pubId, pubName, titles);
	}

	@Override
	public String toString() {
		return "Publisher [pubId=" + pubId + ", pubName=" + pubName + ", email=" + email + ", city=" + city
				+ ", country=" + country + ", titles=" + titles + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Publisher other = (Publisher) obj;
		return Objects.equals(city, other.city) && Objects.equals(country, other.country)
				&& Objects.equals(email, other.email) && Objects.equals(pubId, other.pubId)
				&& Objects.equals(pubName, other.pubName) && Objects.equals(titles, other.titles);
	}

}
