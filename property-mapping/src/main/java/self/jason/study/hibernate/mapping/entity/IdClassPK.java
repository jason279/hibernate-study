package self.jason.study.hibernate.mapping.entity;

import java.io.Serializable;

public class IdClassPK implements Serializable {
	private static final long serialVersionUID = 415588969798906802L;
	private String subSystem;
	private String userName;

	public IdClassPK() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subSystem == null) ? 0 : subSystem.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		IdClassPK other = (IdClassPK) obj;
		if (subSystem == null) {
			if (other.subSystem != null)
				return false;
		} else if (!subSystem.equals(other.subSystem))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
