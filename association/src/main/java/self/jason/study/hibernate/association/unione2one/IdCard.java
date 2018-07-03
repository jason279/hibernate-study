package self.jason.study.hibernate.association.unione2one;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//@Entity
public class IdCard {
	@Id
	@GeneratedValue
	private Long id;

	private String number;

	public IdCard() {
	}

	public IdCard(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Long getId() {
		return id;
	}

}
