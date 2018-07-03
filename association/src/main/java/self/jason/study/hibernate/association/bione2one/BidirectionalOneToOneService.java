package self.jason.study.hibernate.association.bione2one;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BidirectionalOneToOneService {
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void demo() {
		Person person = new Person("jason");
		IdCard idCard = new IdCard("123456789");
		person.setIdCard(idCard);

		entityManager.persist(person);
		entityManager.flush();
		entityManager.clear();

		person = entityManager.find(Person.class, 1L);

		person.deleteIdCard(idCard);
		entityManager.persist(person);
	}
}
