package self.jason.study.hibernate.bootstrap.jpa.application;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import self.jason.study.hibernate.bootstrap.jpa.application.entity.Book;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("bootstrap.jpa.application");
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Book book = new Book();
		book.setId(1L);
		book.setTitle("hibernate study");
		book.setAuthor("jason tian");
		entityManager.getTransaction().begin();
		entityManager.persist(book);
		entityManager.getTransaction().commit();
		entityManager.close();
		entityManagerFactory.close();
	}
}
