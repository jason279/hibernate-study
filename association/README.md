## Overview
This sample illustrates the different associations type for multiple entities. In detail, there are seven different types of association: ManyToOne, uni/bi-directional OneToMany, uni/bi-directinal OneToOne and uni/bi-directional ManyToMany.

NOTE: Only annotated the entity class in one package when running the application to avoid the table name confliction.

## one-to-many relationship
Consider that Department and Employee entities, they are one-to-many relationship. And can be defined in ManyToOne association, unidirectional OneToMany association or the full bidirectional OneToMany association.
In database side, the ManyToOne association and bidirectional OneToMany association is the same, using foreign key on the child table. While the Unidirectional OneToMany is implemented based on a third linked table.
In java code side (or JPA/Hibernate side), they use different annotations, see the following for detail.

### 1. ManyToOne
It can be thought as the other part of a bidirectonal OneToMany association, compared with unidirectional OneToMany, as the associated property is one the child side.
code side:

```
@Entity
public class Department {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	
	//Getters and setters are omitted for brevity
}

@Entity
public class Employee {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	
    @ManyToOne(fetch = FetchType.LAZY)
	private Department department;
	
	//Getters and setters are omitted for brevity
}

    // DAO method
    @Transactional
	public void demo() {
		Employee employee = new Employee("jason", department);
		Department department = new Department("Foglight");

		entityManager.persist(employee);
		entityManager.persist(department);
		
		entityManager.flush();
		entityManager.clear();
		
		entityManager.find(Department.class, 1L);
		entityManager.find(Employee.class, 2L);
	}
``` 

db side
```
create table department (id bigint not null, name varchar(255), primary key (id)) engine=MyISAM
create table employee (id bigint not null, name varchar(255), department_id bigint, primary key (id)) engine=MyISAM
alter table employee add constraint FKbejtwvg9bxus2mffsm3swj3u9 foreign key (department_id) references department (id)

insert into department (name, id) values (?, ?)
binding parameter [1] as [VARCHAR] - [Foglight]
binding parameter [2] as [BIGINT] - [1]
insert into employee (department_id, name, id) values (?, ?, ?)
binding parameter [1] as [BIGINT] - [1]
binding parameter [2] as [VARCHAR] - [jason]
binding parameter [3] as [BIGINT] - [2]

select department0_.id as id1_0_0_, department0_.name as name2_0_0_ from department department0_ where department0_.id=?
binding parameter [1] as [BIGINT] - [1]
select employee0_.id as id1_1_0_, employee0_.department_id as departme3_1_0_, employee0_.name as name2_1_0_ from employee employee0_ where employee0_.id=?
binding parameter [1] as [BIGINT] - [2]
```
#### NOTE:
1. The default foreign key is the ParentEntityName_PK (department_id in the example), can be manually defined by @JoinColumn#name, also can define the FK name in database side, using its foreignKey property, e.g. ```	@JoinColumn(name = "dep_id", foreignKey = @ForeignKey(name = "DEP_ID_FK")) ```.
2. Persist the parent entity first otherwise, additional update sql statement will be generated to update the FK column of child table.
3. The default fetch type for @ManyToOne is EAGER, so we'd better set it to lazy for performance issue.

### 2. Unidirectional OneToMany
It is implemented in db side via an additional linked table, and not the best chooice, because the modification for the collection field will generate delete sql statements for all the parent id rows and re-insert the latest items which result in that a single remove will generate a lot of sql statements.

code side:
```
@Entity
public class Department {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Employee> employees = new ArrayList<>();
	
	//Getters and setters are omitted for brevity
}

@Entity
public class Employee {
	@Id
	@GeneratedValue
	private Long id;
	private String name;

	//Getters and setters are omitted for brevity
}

    // DAO method
    @Transactional
	public void demo() {
		Department department = new Department("Foglight");
		Employee e1 = new Employee("jason");
		department.addEmployee(e1);
		Employee e2 = new Employee("kenson");
		department.addEmployee(e2);

		entityManager.persist(department);
		entityManager.flush();

		department.deleteEmployee(e1);
		entityManager.flush();
	}
``` 

db side
```
create table department (id bigint not null, name varchar(255), primary key (id)) engine=MyISAM
create table department_employees (department_id bigint not null, employees_id bigint not null) engine=MyISAM
create table employee (id bigint not null, name varchar(255), primary key (id)) engine=MyISAM
alter table department_employees add constraint UK_glhfs8s9v6j1up1hmnu5u1roo unique (employees_id)
alter table department_employees add constraint FKnoa5axqogt38rt5mr2q5sabme foreign key (employees_id) references employee (id)
alter table department_employees add constraint FKb66y4tjmeyj55r61lv12ms3kw foreign key (department_id) references department (id)

insert into department (name, id) values (?, ?)
binding parameter [1] as [VARCHAR] - [Foglight]
binding parameter [2] as [BIGINT] - [1]
insert into employee (name, id) values (?, ?)
binding parameter [1] as [VARCHAR] - [jason]
binding parameter [2] as [BIGINT] - [2]
insert into employee (name, id) values (?, ?)
binding parameter [1] as [VARCHAR] - [kenson]
binding parameter [2] as [BIGINT] - [3]
insert into department_employees (department_id, employees_id) values (?, ?)
binding parameter [1] as [BIGINT] - [1]
binding parameter [2] as [BIGINT] - [2]
insert into department_employees (department_id, employees_id) values (?, ?)
binding parameter [1] as [BIGINT] - [1]
binding parameter [2] as [BIGINT] - [3]

// deleteEmployee operation related sql statements:
delete from department_employees where department_id=?
binding parameter [1] as [BIGINT] - [1]
insert into department_employees (department_id, employees_id) values (?, ?)
binding parameter [1] as [BIGINT] - [1]
binding parameter [2] as [BIGINT] - [3]
delete from employee where id=?
binding parameter [1] as [BIGINT] - [2]

// load parent in lazy mode (default strategy)
select department0_.id as id1_0_0_, department0_.name as name2_0_0_ from department department0_ where department0_.id=?
binding parameter [1] as [BIGINT] - [1]

// load parent in earge mode 
select department0_.id as id1_0_0_, department0_.name as name2_0_0_, employees1_.department_id as departme1_1_1_, employee2_.id as employee2_1_1_, employee2_.id as id1_2_2_, employee2_.name as name2_2_2_ from department department0_ left outer join department_employees employees1_ on department0_.id=employees1_.department_id left outer join employee employee2_ on employees1_.employees_id=employee2_.id where department0_.id=?
binding parameter [1] as [BIGINT] - [1]
```

#### NOTE:
1. A linked table is used to maintain the relationship of the association, and the child id has a unique constraint.
2. The sql statements for modification child collection is not efficient.
3. The cascade can be used on the parent side to transmit the operation  performed on the parent entity to the child side, and if orphanRemoval is set to be true, then the orphan child row will be deleted.
4. You can add @JoinColumn(name="dep_id") to avoid the linked table, and set the FK on Employee table, but the cascaded persist operation for child entity is still not efficient, first insert then update the FK. That's because the Hibernate flush order: the persist action is executed before the collection elements are handled. This way, Hibernate inserts the child records first without the Foreign Key since the child entity does not store this information. During collection handling phase, the Foreign Key column is updated accordingly. The related sql are:
    ```
    // collection property on Deparment entity
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "dep_id") // this annotation will avoid the linked table issue
	private List<Employee> employees = new ArrayList<>();
	
	// persist(department) related sql statements, first insert child then update its FK
	insert into department (name, id) values (?, ?)
    binding parameter [1] as [VARCHAR] - [Foglight]
    binding parameter [2] as [BIGINT] - [1]
    insert into employee (name, id) values (?, ?)
    binding parameter [1] as [VARCHAR] - [jason]
    binding parameter [2] as [BIGINT] - [2]
    insert into employee (name, id) values (?, ?)
    binding parameter [1] as [VARCHAR] - [kenson]
    binding parameter [2] as [BIGINT] - [3]
    update employee set dep_id=? where id=?
    binding parameter [1] as [BIGINT] - [1]
    binding parameter [2] as [BIGINT] - [2]
    update employee set dep_id=? where id=?
    binding parameter [1] as [BIGINT] - [1]
    binding parameter [2] as [BIGINT] - [3]
    
    // department.deleteEmployee(e1) related sql statements, first unsent then delete...
    // the parent entity state change is executed first, which triggers the child entity update. 
    // Afterward, when the collection is processed, the orphan removal action will execute the child row delete statement.
    update employee set dep_id=null where dep_id=? and id=?
    binding parameter [1] as [BIGINT] - [1]
    binding parameter [2] as [BIGINT] - [2]
    delete from employee where id=?
    binding parameter [1] as [BIGINT] - [2]
    ```

### 3. Bidirectional OneToMany (also requires ManyToOne on child side)
On db side, it is the same as ManyToOne association, only a FK on the child table. 

code side
```
@Entity
public class Department {
	@Id
	@GeneratedValue
	private Long id;
	private String name;

	@OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Employee> employees = new ArrayList<>();
	
	//Getters and setters are omitted for brevity
}

@Entity
public class Employee {
	@Id
	@GeneratedValue
	private Long id;
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	private Department department;

	//Getters and setters are omitted for brevity
}

    // DAO method is the same as unidirectional OneToMany association
``` 

db side
```
create table department (id bigint not null, name varchar(255), primary key (id)) engine=MyISAM
create table employee (id bigint not null, name varchar(255), department_id bigint, primary key (id)) engine=MyISAM
alter table employee add constraint FKbejtwvg9bxus2mffsm3swj3u9 foreign key (department_id) references department (id)

insert into department (name, id) values (?, ?)
binding parameter [1] as [VARCHAR] - [Foglight]
binding parameter [2] as [BIGINT] - [1]
insert into employee (department_id, name, id) values (?, ?, ?)
binding parameter [1] as [BIGINT] - [1]
binding parameter [2] as [VARCHAR] - [jason]
binding parameter [3] as [BIGINT] - [2]
insert into employee (department_id, name, id) values (?, ?, ?)
binding parameter [1] as [BIGINT] - [1]
binding parameter [2] as [VARCHAR] - [kenson]
binding parameter [3] as [BIGINT] - [3]

// deleteEmployee operation related sql statements (orphanRemoval=true)
delete from employee where id=?
binding parameter [1] as [BIGINT] - [2]

// deleteEmployee operation related sql statements (default orphanRemoval=false)
update employee set department_id=?, name=? where id=?
binding parameter [1] as [BIGINT] - [null]
binding parameter [2] as [VARCHAR] - [jason]
binding parameter [3] as [BIGINT] - [2]

// load parent in lazy mode (default strategy)
select department0_.id as id1_0_0_, department0_.name as name2_0_0_ from department department0_ where department0_.id=?
binding parameter [1] as [BIGINT] - [1]
```

#### NOTE:
1. This is the best way to map a one-to-many database relationship when we really need the collection on the parent side of the association.
2. Just because you have the option of using the @OneToMany annotation, it does not mean this should be the default option for every one-to-many database relationship. The problem with collections is that we can only use them when the number of child records is rather limited.
Therefore, most of the time, the @ManyToOne annotation on the child side is everything you need. See https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/ for detail.
3. Whenever a bidirectional association is formed, the application developer must make sure both sides are in-sync at all times. See the addEmployee() and deleteEmployee() method for detail.
4. The mappedBy property should be set properly otherwise the linked table will created again (just like the unidirectional case). And every bidirectional association must have one owning side only (the child side), the other one being referred to as the inverse (or the mappedBy) side.
5. The defalut fetch type for @ManyToOne is EAGER, set it to LAZY mannually to get better performance.

## one-to-one relationship
Consider that Person and IdCard entities, they are one-to-one relationship. For one-to-one, it can also be unidirectional or bidirectional.

### 4. Unidirectional OneToOne
It is the same as the ManyToOne asscociation, that is a FK is generated on the client side. The only anti-human is that it treats the Person as the client side in db perspective. To move the FK to IdCard table, the bidirectional OneToOne should be used.

code side
```
@Entity
public class Person {
	@Id
	@GeneratedValue
	private Long id;
	private String name;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private IdCard idCard;

	//Getters and setters are omitted for brevity
}

@Entity
public class IdCard {
	@Id
	@GeneratedValue
	private Long id;

	private String number;

	//Getters and setters are omitted for brevity
}

    // DAO method 
    @Transactional
	public void demo() {
		Person person = new Person("jason");
		IdCard idCard = new IdCard("123456789");
		person.setIdCard(idCard);

		entityManager.persist(person);
		entityManager.flush();

		person.setIdCard(null);
		entityManager.persist(person);
	}
``` 

db side
```
create table id_card (id bigint not null, number varchar(255), primary key (id)) engine=MyISAM
create table person (id bigint not null, name varchar(255), id_card_id bigint, primary key (id)) engine=MyISAM
alter table person add constraint FKnrr1ss74s6qjl4bedn4aa3op3 foreign key (id_card_id) references id_card (id)

insert into id_card (number, id) values (?, ?)
binding parameter [1] as [VARCHAR] - [123456789]
binding parameter [2] as [BIGINT] - [2]
insert into person (id_card_id, name, id) values (?, ?, ?)
binding parameter [1] as [BIGINT] - [2]
binding parameter [2] as [VARCHAR] - [jason]
binding parameter [3] as [BIGINT] - [1]

// setIdCard(null) operation related sql statements (orphanRemoval=true)
update person set id_card_id=?, name=? where id=?
binding parameter [1] as [BIGINT] - [null]
binding parameter [2] as [VARCHAR] - [jason]
binding parameter [3] as [BIGINT] - [1]
delete from id_card where id=?
binding parameter [1] as [BIGINT] - [2]
```

#### NOTE:
1. Because of the FK on Person table, you can not delete the idCard directly via ```entityManger.remove(idCard)``` method. First should invoke the person.setIdCard(null) to remove the FK constraint. In orphanRemoval=true mode, the idCard will be deleted automatically, or you can invoke entityManager.remove() method to delete it.

### 5. Bidirectional OneToOne
Similar to the bidirectional OneToMany Association, mappedBy property, FK in IdCard side and etc... Only one issue is the identity property on the child class, IdCard. Since they are one-to-one relationship, the best practice is that the IdCard can share the id with Person. See the Best Practice for OneToOne Association.

code side
```
@Entity
public class Person {
	@Id
	@GeneratedValue
	private Long id;
	private String name;

    @OneToOne(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private IdCard idCard;

	//Getters and setters are omitted for brevity
}

@Entity
public class IdCard {
	@Id
	@GeneratedValue
	private Long id;
	private String number;

	@OneToOne(fetch = FetchType.LAZY)
	private Person person;

	//Getters and setters are omitted for brevity
}

    // DAO method 
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
``` 

db side
```
create table id_card (id bigint not null, number varchar(255), person_id bigint, primary key (id)) engine=MyISAM
create table person (id bigint not null, name varchar(255), primary key (id)) engine=MyISAM
alter table id_card add constraint FK57ne6hgr21ornhvjxpnmfrqxx foreign key (person_id) references person (id)
insert into person (name, id) values (?, ?)
binding parameter [1] as [VARCHAR] - [jason]
binding parameter [2] as [BIGINT] - [1]
insert into id_card (number, person_id, id) values (?, ?, ?)
binding parameter [1] as [VARCHAR] - [123456789]
binding parameter [2] as [BIGINT] - [1]
binding parameter [3] as [BIGINT] - [2]

// lazy fetch still causes a secondary query
select person0_.id as id1_1_0_, person0_.name as name2_1_0_ from person person0_ where person0_.id=?
binding parameter [1] as [BIGINT] - [1]
select idcard0_.id as id1_0_0_, idcard0_.number as number2_0_0_, idcard0_.person_id as person_i3_0_0_ from id_card idcard0_ where idcard0_.person_id=?
binding parameter [1] as [BIGINT] - [1]

// person.deleteIdCard(idCard) operation related sql statements (orphanRemoval=true)
delete from id_card where id=?
binding parameter [1] as [BIGINT] - [2]
```

#### NOTE:
1. Like other bidirectional relationship, make sure maintain the synchoronise of parent and child side, that's the Person#setIdCard() and Person#deleteIdCard() util methods used to.
2. Although you might annotate the parent-side association to be fetched lazily (the default fetchType for @OneToOne is eager), Hibernate cannot honor this request since it cannot know whether the association is null or not.
The only way to figure out whether there is an associated record on the child side is to fetch the child association using a secondary query. Because this can lead to N+1 query issues, it’s much more efficient to use unidirectional @OneToOne associations with the @MapsId annotation in place.

### Best way to mapper a one-to-one relationship
As metioned above, the efficient way to mapper a one-to-one relationship is using a unidirectional mapping with a @MapsId annotation. 
Because in bidirectional mode, the child table has its own PK and FK, which are most often indexed, so sharing the PK can reduce the index footprint by half, which is desirable since you want to store all your indexes into memory to speed up index scanning.

code side
```
@Entity
public class Person {
	@Id
	@GeneratedValue
	private Long id;
	private String name;

	//Getters and setters are omitted for brevity
}

@Entity
public class IdCard {
	@Id
	private Long id;
	private String number;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	private Person person;

	//Getters and setters are omitted for brevity
}

    // DAO method 
    @Transactional
	public void demo() {
		Person person = new Person("jason");
		IdCard idCard = new IdCard("123456789");
		idCard.setPerson(person);

		entityManager.persist(idCard);
		entityManager.persist(person);
		entityManager.flush();
		entityManager.clear();

		person = entityManager.find(Person.class, 1L);
		idCard = entityManager.find(IdCard.class, person.getId());
	}
``` 

db side
```
create table id_card (number varchar(255), person_id bigint not null, primary key (person_id)) engine=MyISAM
create table person (id bigint not null, name varchar(255), primary key (id)) engine=MyISAM
alter table id_card add constraint FK57ne6hgr21ornhvjxpnmfrqxx foreign key (person_id) references person (id)
insert into person (name, id) values (?, ?)
binding parameter [1] as [VARCHAR] - [jason]
binding parameter [2] as [BIGINT] - [1]
insert into id_card (number, person_id) values (?, ?)
binding parameter [1] as [VARCHAR] - [123456789]
binding parameter [2] as [BIGINT] - [1]

select person0_.id as id1_1_0_, person0_.name as name2_1_0_ from person person0_ where person0_.id=?
binding parameter [1] as [BIGINT] - [1]
select idcard0_.person_id as person_i2_0_0_, idcard0_.number as number1_0_0_ from id_card idcard0_ where idcard0_.person_id=?
binding parameter [1] as [BIGINT] - [1]
```

#### NOTE:
1. The IdCard#id column serves as both Primary Key and FK. the @Id column no longer uses a @GeneratedValue annotation since the identifier is populated with the identifier of the parent Person entity.
2. Since no cascade exist, you should persist the child entity, IdCard, manually.

## many-to-many relationship
Consider that Student and Course entities, they are many-to-many relationship. For many-to-many, it can also be unidirectional or bidirectional.

### 6. Unidirectional ManyToMany
### 7. Bidirectional ManyToMany


