package self.jason.study.hibernate.association.bestone2one;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

//@Entity
public class IdCard {
	@Id
	private Long id;
	private String number;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	private Person person;

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

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}
