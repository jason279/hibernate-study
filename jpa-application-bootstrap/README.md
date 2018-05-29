## Overview
This sample illustrates how to bootstrap hibernate in JPA-compliant application model. 
> For compliant application-bootstrapping, rather than the container building the EntityManagerFactory for the application, the application builds the EntityManagerFactory itself using the javax.persistence.Persistence bootstrap class. The application creates an EntityManagerFactory by calling the createEntityManagerFactory method.
> -- Hibernate Document

### Step 1: Add dependencies
The sample uses hibenrate 5.2.17.Final and mysql 8.0.11, so add the following dependencies:
```
<dependency>
	<groupId>org.hibernate</groupId>
	<artifactId>hibernate-core</artifactId>
	<version>5.2.17.Final</version>
</dependency>
		
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<version>8.0.11</version>
</dependency>
```

#### Step 2: Add persistence.xml to the classpath
Add the META-INF/persistence.xml file (cannot change the folder name and file name) under classpath, since I'm using maven, so I add it to the src/main/resources folder.

The persistence.xml file contains the connection information with the database, so make sure set the right value to the following properties:
```
<property name="javax.persistence.jdbc.url"	value="jdbc:mysql://localhost:3306/study" />

<property name="javax.persistence.jdbc.user" value="root" />

<property name="javax.persistence.jdbc.password" value="root" />
```

#### Step 3: Annotated entity class
Annotated the entity class with `@Entity` annotation and mark one property as ID using `@Id` annotation.
```
@Entity
public class Book {
	@Id
    private Long id;

    private String title;

    private String author;
    
    //Getters and setters are omitted for brevity
}
```

##### Step 4: Create EntityManagerFactory, EntityManager and persist entity during Transaction
see the following code snap:
```
EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("bootstrap.jpa.application");
EntityManager entityManager = entityManagerFactory.createEntityManager();
entityManager.getTransaction().begin();
entityManager.persist(book);
entityManager.getTransaction().commit();
entityManager.close();
entityManagerFactory.close();
```

#### Note:
- Do not use hibernate latest 5.3.x version, because it will mis-report “no provider exception” when there is something else wrong, such as the jdbc connection configure not correct.  
    ```
    Exception in thread "main" javax.persistence.PersistenceException: No Persistence provider for EntityManager named bootstrap.jpa.application
    ```
- Use maven in eclipse may sometimes download the architectives uncompleted, which may cause other exception, for me, I encount the following exception (the class is exist and can be opened, sometimes for maven uncomleted donwload, the file cannot be opened with a invalid header error)
    ```
   Caused by: java.lang.ClassNotFoundException: Could not load requested class : com.mysql.cj.jdbc.Driver
    ```
    Try deleting the architectives and redownload it by maven if you see some exception which should not reported.
