package self.jason.study.hibernate.association.unione2many;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

//@Entity
public class Department {
	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	// @JoinColumn(name = "dep_id")
	private List<Employee> employees = new ArrayList<>();

	public Department() {
	}

	public Department(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public void addEmployee(Employee employee) {
		this.employees.add(employee);
	}

	public void deleteEmployee(Employee employee) {
		this.employees.remove(employee);
	}

}
