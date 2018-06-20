package self.jason.study.hibernate.mapping.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class SystemUserWithEmbeddedId {
	@EmbeddedId
	private EmbeddedIdPK pk;
	private String name;

	public EmbeddedIdPK getPk() {
		return pk;
	}

	public void setPk(EmbeddedIdPK pk) {
		this.pk = pk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
