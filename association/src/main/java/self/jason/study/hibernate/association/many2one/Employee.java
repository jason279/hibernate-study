package self.jason.study.hibernate.association.many2one;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Employee {
	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "dep_id", foreignKey = @ForeignKey(name = "DEP_ID_FK"))
	private Department department;

	public Employee() {
	}

	public Employee(String name, Department department) {
		this.name = name;
		this.department = department;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Long getId() {
		return id;
	}

}
