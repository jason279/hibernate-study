package self.jason.study.hibernate.bootstrap.jpa.application;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import self.jason.study.hibernate.bootstrap.jpa.application.entity.Book;

public class App {
	public static void main(String[] args) {
		StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()//
				.configure("hibernate.cfg.xml")//
				.build();

		Metadata metadata = new MetadataSources(standardRegistry)//
				.addAnnotatedClass(Book.class)//
				.getMetadataBuilder().applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)//
				.build();

		SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.persist(getBook());
		session.getTransaction().commit();
		session.close();
		sessionFactory.close();
	}

	public static Book getBook() {
		Book book = new Book();
		book.setId(5L);
		book.setTitle("hibernate study - native bootstrap");
		book.setAuthor("jason tian");
		return book;
	}
}
