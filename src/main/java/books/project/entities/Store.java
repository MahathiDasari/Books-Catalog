package books.project.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "stores")

public class Store {

	@Id
	@Column(name = "store_id")
	@Pattern(regexp = "^S\\d{3}$", message = "Store Id must be Greater Than 1000")
	private String storeId;

	@Column(name = "location", length = 20)
	@NotBlank
	@Size(max = 20, message = "Maximum Length is 20")
	private String location;

	@Column(name = "city", length = 20)
	@NotBlank
	@Size(max = 20, message = "Maximum Length is 20")
	private String city;

	@Column(name = "country", length = 10)
	@Size(max = 10, message = "Maximum Length is 10")
	private String country;

	// Store to Sale
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "store")
	@JsonIgnore
	List<Sale> sales = new ArrayList<>();

	// Store to title
	@ManyToMany
	@JoinTable(name = "sales", joinColumns = @JoinColumn(name = "store_id"), inverseJoinColumns = @JoinColumn(name = "title_id"))
	@JsonIgnore
	Set<Title> titles = new HashSet<Title>();

	public Set<Title> getTitles() {
		return titles;
	}

	public void setTitles(Set<Title> titles) {
		this.titles = titles;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public List<Sale> getSales() {
		return sales;
	}

	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}

	@Override
	public String toString() {
		return "Store [storeId=" + storeId + ", location=" + location + ", city=" + city + ", country=" + country
				+ ", titles=" + titles + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(city, country, location, storeId, titles);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Store other = (Store) obj;
		return Objects.equals(city, other.city) && Objects.equals(country, other.country)
				&& Objects.equals(location, other.location) && Objects.equals(storeId, other.storeId)
				&& Objects.equals(titles, other.titles);
	}
}