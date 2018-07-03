package self.jason.study.hibernate.association.unione2one;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

//@Entity
public class Person {
	@Id
	@GeneratedValue
	private Long id;
	private String name;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
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
		this.idCard = idCard;
	}

	public Long getId() {
		return id;
	}

}
