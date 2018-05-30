## Overview
This sample illustrates how to bootstrap hibernate in JPA-compliant container model (using spring as the container). 
> For compliant container-bootstrapping, the container will build an EntityManagerFactory for each persistent-unit defined in the META-INF/persistence.xml configuration file and make that available to the application for injection via the javax.persistence.PersistenceUnit annotation or via JNDI lookup.
> -- Hibernate Document

### Step 1: Add dependencies
The sample uses hibenrate 5.2.17.Final, spring 5.0.6.RELEASE and mysql 8.0.11, so add the following dependencies:
```
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-context</artifactId>
	<version>${spring.version}</version>
</dependency>

<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-orm</artifactId>
	<version>${spring.version}</version>
</dependency>
		
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

#### Step 2: Spring Annotation configure the JPA related Beans
Use Spring AnnotationConfigApplicationContext, can configure DataSource, EntityMangerFacotryBean and JpaTransactionManger, so that spring container can manage the EntityManager and Transaction for the application.

```
@Bean
public DataSource dataSource() {
	SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
	dataSource.setDriverClass(Driver.class);
	dataSource.setUrl("jdbc:mysql://localhost:3306/study");
	dataSource.setUsername("root");
	dataSource.setPassword("root");
	return dataSource;
}

@Bean
public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
	LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
	entityManagerFactoryBean.setDataSource(dataSource());
	entityManagerFactoryBean.setPackagesToScan("self.jason.study.hibernate.bootstrap.jpa.container.entity");
	entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
	entityManagerFactoryBean.setJpaProperties(hibernateProperties());
	return entityManagerFactoryBean;
}

private Properties hibernateProperties() {
	Properties properties = new Properties();
	properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
	properties.setProperty("hibernate.show_sql", "true");
	properties.setProperty("hibernate.hbm2ddl.auto", "update");
	return properties;
}

@Bean
public JpaTransactionManager transactionManager() {
	System.out.println(entityManagerFactory().getObject());
	return new JpaTransactionManager(entityManagerFactory().getObject());
}
```

#### Step 3: Autowired the EntityManager and @Transactional the save() method
In the container bootstrap model, the container, here spring, will manager the EntityManager and Transaction, so let spring inject them:
```
@PersistenceContext
private EntityManager entityManager;

@Transactional
public void save() {
    ...
    entityManager.persist(book);
}
```
The advantidage is the application doesn't need to care the transaction anymore, no begin(), commit(), close() method here.

#### Note:
- The config class should be annotated by @Configuration and @EnableTransactionManagement so that the Transaction is enabled and managed by spring.

- The entityManagerFactory() bean shoud return LocalContainerEntityManagerFactoryBean type and could not return EntityManagerFactory via LocalContainerEntityManagerFactoryBean.getObject(), because the entityManagerFactory is created in the AbstractEntityManagerFactoryBean.afterPropertiesSet() method, which means it is created out the @Bean method.

- By default the LocalContainerEntityManagerFactoryBean will try to load the persistence configuration via the JPA stardanr META-INF/persistence.xml file, to avoid this, the setPackagesToScan() method should be invoked.
    >>> Set whether to use Spring-based scanning for entity classes in the classpath instead of using JPA's standard scanning of jar files with persistence.xml markers in them. In case of Spring-based scanning, no persistence.xml is necessary; all you need to do is to specify base packages to search here. 
    >>> -- setPackagesToScan() method document
