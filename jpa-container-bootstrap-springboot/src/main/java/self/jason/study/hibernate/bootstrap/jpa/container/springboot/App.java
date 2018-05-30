package self.jason.study.hibernate.bootstrap.jpa.container.springboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import self.jason.study.hibernate.bootstrap.jpa.container.springboot.entity.Book;
import self.jason.study.hibernate.bootstrap.jpa.container.springboot.repository.BookRepository;

@SpringBootApplication
public class App {

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

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
