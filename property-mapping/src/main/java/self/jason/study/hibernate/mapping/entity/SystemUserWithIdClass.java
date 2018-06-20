package self.jason.study.hibernate.mapping.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(IdClassPK.class)
public class SystemUserWithIdClass {
	@Id
	@Column(length = 10)
	private String subSystem;
	@Id
	@Column(length = 10)
	private String userName;
	private String name;

	public String getSubSystem() {
		return subSystem;
	}

	public void setSubSystem(String subSystem) {
		this.subSystem = subSystem;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
