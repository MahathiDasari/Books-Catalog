package books.project.entities;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sales")
public class Sale {

	@EmbeddedId
	private Salecpk key;

	@Column(name = "qty_sold")
	private Integer qtySold;

	//many sales one store
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "store_id", insertable = false, updatable = false)
	private Store store;

	//many sales one title  
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "title_id", insertable = false, updatable = false)
	private Title title;

	public Salecpk getKey() {
		return key;
	}

	public void setKey(Salecpk key) {
		this.key = key;
	}

	public Integer getQtySold() {
		return qtySold;
	}

	public void setQtySold(Integer qtySold) {
		this.qtySold = qtySold;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Sale [key=" + key + ", qtySold=" + qtySold + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, qtySold);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sale other = (Sale) obj;
		return Objects.equals(key, other.key) && Objects.equals(qtySold, other.qtySold);
	}
}
