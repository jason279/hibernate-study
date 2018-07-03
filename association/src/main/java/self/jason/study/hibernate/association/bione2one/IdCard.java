package self.jason.study.hibernate.association.bione2one;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

//@Entity
public class IdCard {
	@Id
	@GeneratedValue
	private Long id;
	private String number;

	@OneToOne(fetch = FetchType.LAZY)
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
