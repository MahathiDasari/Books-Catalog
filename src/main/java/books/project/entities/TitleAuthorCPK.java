package books.project.entities;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable

public class TitleAuthorCPK implements Serializable {

	@Column(name = "title_id")
	private String titleid;

	@Column(name = "au_id")
	private String auid;

	public String getTitleid() {
		return titleid;
	}

	public void setTitleid(String titleid) {
		this.titleid = titleid;
	}

	public String getAuid() {
		return auid;
	}

	public void setAuid(String auid) {
		this.auid = auid;
	}

	@Override
	public String toString() {
		return "TitleAuthorCPK [titleid=" + titleid + ", auid=" + auid + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(auid, titleid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TitleAuthorCPK other = (TitleAuthorCPK) obj;
		return Objects.equals(auid, other.auid) && Objects.equals(titleid, other.titleid);
	}
}
