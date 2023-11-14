package books.project.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;

@Embeddable
public class Salecpk implements Serializable {
	@Pattern(regexp = "^S\\d{3}$", message = "Store Id must be a String and starts with 1 Capital Letter followed by 3 digits")
	@Column(name = "store_id", length = 4)
	private String storeId;
	
	@Pattern(regexp = "^T\\d{3}$", message = "Title Id must be a String and starts with 1 Capital Letter followed by 3 digits")
	@Column(name = "title_id", length = 20)
	private String titleId;
	

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getTitleId() {
		return titleId;
	}

	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(storeId, titleId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Salecpk))
			return false;
		Salecpk other = (Salecpk) obj;
		return Objects.equals(storeId, other.storeId) && Objects.equals(titleId, other.titleId);
	}

	@Override
	public String toString() {
		return "SalePk [storeId=" + storeId + ", titleId=" + titleId + "]";
	}

}
