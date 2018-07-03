package self.jason.study.hibernate.association.bione2one;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

//@Entity
public class Person {
	@Id
	@GeneratedValue
	private Long id;
	private String name;

	@OneToOne(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private IdCard idCard;

	public Person() {
	}

	public Person(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IdCard getIdCard() {
		return idCard;
	}

	public void setIdCard(IdCard idCard) {
		idCard.setPerson(this);
		this.idCard = idCard;
	}

	public void deleteIdCard(IdCard idCard) {
		idCard.setPerson(null);
		this.idCard = null;
	}

	public Long getId() {
		return id;
	}

}
