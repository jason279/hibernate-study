package self.jason.study.hibernate.mapping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import self.jason.study.hibernate.mapping.entity.Book;
import self.jason.study.hibernate.mapping.repository.BookRepository;

@Service
public class BookService {
	@Autowired
	private BookRepository bookRepository;

	@Transactional
	public void add(Book book) {
		book = bookRepository.save(book);
		book.setTitle("rename-" + book.getTitle());

	}

	public int getFreeBookNumber() {
		// since the Book entity is annotated with @Where, can directly invoke the count() method.
		return (int) bookRepository.count();
	}

	@Transactional
	public void addMultipleBooks() {
		for (int i = 10; i < 20; i++) {
			bookRepository.save(new Book("book" + i, true));
		}
	}

}
