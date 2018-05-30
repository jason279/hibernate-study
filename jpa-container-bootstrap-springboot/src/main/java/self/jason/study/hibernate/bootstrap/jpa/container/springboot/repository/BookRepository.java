package self.jason.study.hibernate.bootstrap.jpa.container.springboot.repository;

import org.springframework.data.repository.CrudRepository;

import self.jason.study.hibernate.bootstrap.jpa.container.springboot.entity.Book;

public interface BookRepository extends CrudRepository<Book, Long>{

}
