package self.jason.study.hibernate.bootstrap.jpa.application;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import self.jason.study.hibernate.bootstrap.jpa.application.entity.Book;

public class App {
	public static void main(String[] args) {
		Configuration cfg = new Configuration().addAnnotatedClass(Book.class)
				.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect")//
				.setProperty("hibernate.show_sql", "true")//
				.setProperty("hibernate.hbm2ddl.auto", "create")//
				.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
				.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/study")
				.setProperty("hibernate.connection.username", "root")
				.setProperty("hibernate.connection.password", "root");
		
		Book book = new Book();
		book.setId(1L);
		book.setTitle("hibernate study - native legacy bootstrap");
		book.setAuthor("jason tian");

		SessionFactory sessionFactory = cfg.buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(book);
		session.getTransaction().commit();
		session.close();
		sessionFactory.close();
	}
}
