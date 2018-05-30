## Overview
This sample is similar to the jpa-container-bootstrap sample, unless this one use the latest spring boot as the start point to illustrates how to bootstrap hibernate in JPA-compliant container model (using spring as the container).

### Step 1: Generate the project architecture with spring initializr
The init work can be done via [Spring Initializr](http://start.spring.io/), select JPA and MySQL dependencies and generate the project. The pom has already included the needed dependencies, remove the useless spring-test dependency.

```
<parent>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-parent</artifactId>
	<version>2.0.2.RELEASE</version>
	<relativePath/> <!-- lookup parent from repository -->
</parent>

<dependencies>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-jpa</artifactId>
	</dependency>
	<dependency>
		<groupId>mysql</groupId>
		<artifactId>mysql-connector-java</artifactId>
		<scope>runtime</scope>
	</dependency>
</dependencies>

<build>
	<plugins>
		<plugin>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
		</plugin>
	</plugins>
</build>
```
The spring boot autoconfig will save many many work, so let the application only focus on the service logic.

#### Step 2: configure spring and hiberante
spring boot use a single configure file, application.properties, no another Beans need to be configured as spring boot takes all of these jobs.

```
spring.datasource.url=jdbc:mysql://localhost:3306/study
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=create
```

#### Step 3: Implement the Repository interface
Spring JPA implementation is very simple, only need to create an interface which extends the CRUDRepository or JPARepository.

```
public interface BookRepository extends CrudRepository<Book, Long>{
}
```

#### Step 4: Use CommandLineRunner to demo the save process
For simplify, use the CommandLineRunner to show the save process in @SpringBootApplication class:

```
@Bean
public CommandLineRunner runner(BookRepository bookRepository) {
	return args -> {
		Book book = new Book();
		book.setId(3L);
		book.setTitle("hibernate study - jpa container bootstrap, springboot");
		book.setAuthor("jason tian");

		bookRepository.save(book);
	};
}
```

#### Note:
- To clearly show the hibernate sql and binding, set the related logger level in configure file:
    ```
    logging.level.org.hibernate.SQL=debug
    logging.level.org.hibernate.type.descriptor.sql.BasicBinder=trace
    logging.level.root=warn
    ```