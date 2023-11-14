package books.project.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "authors")

public class Author {
	@Id
	@Column(name = "au_id", length = 20)
	@Size(min = 7, max = 20, message = "Author Id length must be 20")
	@Pattern(regexp = "^A\\d{3}$", message = "Author Id must be a String and starts with 1 Capital Letter followed by 3 digits")
	private String auId;
	
	@Size(max = 40, message = "Maximum Length is 40")
	@NotBlank
	@Column(name = "au_name", length = 40)
	private String auName;

	@Email
	@Size(max = 50, message = "Maximum Length is 50")
	@Column(name = "email", length = 50)
	private String email;

	@Pattern(regexp = "^\\d{10}$", message = "Mobile number must be a 10 digit number")
	@Column(name = "mobile", length = 12)
	private String mobile;

	@Size(max = 20, message = "Maximum Length is 20")
	@Column(name = "city", length = 20)
	private String city;

	@Size(max = 30, message = "Maximum Length is 30")
	@Column(name = "country", length = 30)
	private String country;
	
	// Title to Author
	@ManyToMany(mappedBy = "authors")
	@JsonIgnore//infinite recursion
	Set<Title> titles = new HashSet<Title>();

	public Set<Title> getTitles() {
		return titles;
	}

	public void setTitles(Set<Title> titles) {
		this.titles = titles;
	}

	public String getAuId() {
		return auId;
	}

	public void setAuId(String auId) {
		this.auId = auId;
	}

	public String getAuName() {
		return auName;
	}

	public void setAuName(String auName) {
		this.auName = auName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	public String toString() {
		return "Author [auId=" + auId + ", auName=" + auName + ", email=" + email + ", mobile=" + mobile + ", city="
				+ city + ", country=" + country + ", titles=" + titles + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(auId, auName, city, country, email, mobile, titles);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Author other = (Author) obj;
		return Objects.equals(auId, other.auId) && Objects.equals(auName, other.auName)
				&& Objects.equals(city, other.city) && Objects.equals(country, other.country)
				&& Objects.equals(email, other.email) && Objects.equals(mobile, other.mobile)
				&& Objects.equals(titles, other.titles);
	}
}
