package self.jason.study.hibernate.mapping.repository;

import org.springframework.data.repository.CrudRepository;

import self.jason.study.hibernate.mapping.entity.Book;

public interface BookRepository extends CrudRepository<Book, Long> {

}
