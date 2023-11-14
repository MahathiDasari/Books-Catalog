package books.project.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "titles")

public class Title {

	@Id
	@Column(name = "title_id", length = 20)
	@Pattern(regexp = "^T\\d{3}$", message = "Title Id must be a String and starts with 1 Capital Letters followed by 3 digits")
	private String titleId;
	@Column(name = "title")
	@NotNull(message = "Title cannot be null")
	@Size(max = 80, message = "Title cannot exceed 80 characters")
	private String title;
	@Column(name = "price")
	@Positive(message = "Price must be a positive value")
	private Double price;
	@Column(name = "pub_id", length = 4)
	@Pattern(regexp = "^P\\d{3}$", message = "Publisher Id must be a String and starts with 1 Capital Letters followed by 3 digits")
	private String pubId;
	@Column(name = "ytd_sales")
	@PositiveOrZero(message = "YTD sales must be a non-negative value")
	private Integer ytdSales;
	@Column(name = "release_year")
	private LocalDate releaseYear;

	// many titles one publisher
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "pub_id", updatable = false, insertable = false)
	private Publisher publisher;

	//many titles many authors
	@ManyToMany
	@JsonIgnore
	@JoinTable(name = "Titleauthors", joinColumns = @JoinColumn(name = "title_id"), inverseJoinColumns = @JoinColumn(name = "au_id"))
	Set<Author> authors = new HashSet<Author>();

	//many titles many stores
	@ManyToMany(mappedBy = "titles")
	@JsonIgnore
	Set<Store> stores = new HashSet<Store>();

	//one title many sales
	@OneToMany(mappedBy = "title", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Sale> sales = new ArrayList<>();

	public Set<Store> getStores() {
		return stores;
	}

	public void setStores(Set<Store> stores) {
		this.stores = stores;
	}

	public List<Sale> getSales() {
		return sales;
	}

	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}

	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public String getTitleId() {
		return titleId;
	}

	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPubId() {
		return pubId;
	}

	public void setPubId(String pubId) {
		this.pubId = pubId;
	}

	public Integer getYtdSales() {
		return ytdSales;
	}

	public void setYtdSales(Integer ytdSales) {
		this.ytdSales = ytdSales;
	}

	public LocalDate getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(LocalDate releaseYear) {
		this.releaseYear = releaseYear;
	}

	@Override
	public String toString() {
		return "Title [titleId=" + titleId + ", title=" + title + ", price=" + price + ", pubId=" + pubId
				+ ", ytdSales=" + ytdSales + ", releaseYear=" + releaseYear + ", publisher=" + publisher + ", authors="
				+ authors + ", stores=" + stores + ", sales=" + sales + "]";
	}
}
