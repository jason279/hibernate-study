package self.jason.study.hibernate.association.many2one;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManyToOneService {
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void demo() {
		Department department = new Department("Foglight");
		Employee employee = new Employee("jason", department);
		entityManager.persist(department);
		entityManager.persist(employee);
		
		entityManager.flush();
		entityManager.clear();
		
		entityManager.find(Department.class, 1L);
		entityManager.find(Employee.class, 2L);
	}
}
