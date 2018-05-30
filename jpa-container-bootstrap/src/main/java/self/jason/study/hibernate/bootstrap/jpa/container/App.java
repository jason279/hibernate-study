package self.jason.study.hibernate.bootstrap.jpa.container;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import self.jason.study.hibernate.bootstrap.jpa.container.config.SpringConfig;
import self.jason.study.hibernate.bootstrap.jpa.container.entity.Book;

public class App {
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void save() {
		Book book = new Book();
		book.setId(2L);
		book.setTitle("hibernate study - jpa container bootstrap");
		book.setAuthor("jason tian");
		entityManager.persist(book);
	}

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
		App app = context.getBean(App.class);
		app.save();
		context.close();
	}
}
