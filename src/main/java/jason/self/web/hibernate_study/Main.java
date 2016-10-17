package jason.self.web.hibernate_study;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import jason.self.web.hibernate_study.model.Person;

public class Main {
	private static SessionFactory sessionFactory;
	private static EntityManagerFactory entityManagerFactory;

	public static void main(String[] args) {
		 demoUsingNativeAPIAndXMLMapping();
		// demoUsingNativeAPIAndAnnotationMapping();
		// demoUsingJPA();
//		demoEnvers();
	}

	private static void demoEnvers() {
		try {
			// using JPA to persist a person
			entityManagerFactory = Persistence.createEntityManagerFactory("jason.self.web.hibernate.jpa");
			persistModelUsingJPA();
			updateModelUsingJPA();
		} finally {
			if (entityManagerFactory != null) {
				entityManagerFactory.close();
			}
		}

	}

	private static void updateModelUsingJPA() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(buildPerson("jason2UsingJPA", 35));
		Person p = entityManager.find(Person.class, 1);
		p.setName("jason tian");
		p.setAge(32);
		entityManager.getTransaction().commit();
		entityManager.close();

	}

	private static void demoUsingJPA() {
		try {
			entityManagerFactory = Persistence.createEntityManagerFactory("jason.self.web.hibernate.jpa");
			persistModelUsingJPA();
		} finally {
			if (entityManagerFactory != null) {
				entityManagerFactory.close();
			}
		}
	}

	private static void persistModelUsingJPA() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(buildPerson("jason1UsingJPA", 31));
		entityManager.getTransaction().commit();
	}

	private static void demoUsingNativeAPIAndAnnotationMapping() {
		try {
			bootstrapNative();
			persistModelUsingHibernateAPI();
		} finally {
			if (sessionFactory != null) {
				sessionFactory.close();
			}
		}

	}

	private static void demoUsingNativeAPIAndXMLMapping() {
		try {
			bootstrapNative();
			persistModelUsingHibernateAPI();
		} finally {
			if (sessionFactory != null) {
				sessionFactory.close();
			}
		}
	}

	private static void bootstrapNative() {
		StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
		try {
			sessionFactory = new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			System.out.println(e);
			StandardServiceRegistryBuilder.destroy(serviceRegistry);
		}
	}

	private static void persistModelUsingHibernateAPI() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(buildPerson("jasonUsingHibernateAPI", 31));
		session.getTransaction().commit();
		session.close();
	}

	private static Person buildPerson(String name, int age) {
		Person p = new Person();
		p.setName(name);
		p.setAge(age);
		return p;
	}
}
