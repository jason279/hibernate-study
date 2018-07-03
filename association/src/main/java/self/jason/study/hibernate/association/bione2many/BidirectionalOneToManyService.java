package self.jason.study.hibernate.association.bione2many;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BidirectionalOneToManyService {
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void demo() {
		Department department = new Department("Foglight");
		Employee e1 = new Employee("jason");
		department.addEmployee(e1);
		Employee e2 = new Employee("kenson");
		department.addEmployee(e2);

		entityManager.persist(department);
		entityManager.flush();

		department.deleteEmployee(e1);
		entityManager.flush();
		entityManager.clear();

		entityManager.find(Department.class, 1L);
		entityManager.find(Employee.class, 2L);
	}
}
