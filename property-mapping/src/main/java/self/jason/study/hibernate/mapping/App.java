package self.jason.study.hibernate.mapping;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import self.jason.study.hibernate.mapping.entity.Author;
import self.jason.study.hibernate.mapping.entity.Book;
import self.jason.study.hibernate.mapping.entity.Category;
import self.jason.study.hibernate.mapping.entity.Comment;
import self.jason.study.hibernate.mapping.entity.Kind;
import self.jason.study.hibernate.mapping.entity.EmbeddedIdPK;
import self.jason.study.hibernate.mapping.entity.SystemUserWithEmbeddedId;
import self.jason.study.hibernate.mapping.entity.SystemUserWithIdClass;
import self.jason.study.hibernate.mapping.service.BookService;
import self.jason.study.hibernate.mapping.service.SystemUserService;

@SpringBootApplication
public class App {

	@Bean
	public CommandLineRunner runner(BookService bookService, SystemUserService systemUserService) {
		return args -> {
			bookService.add(getBook());
			bookService.add(new Book("book2", true));
			bookService.add(new Book("book3", false));
			
			bookService.addMultipleBooks(); 

			assert bookService.getFreeBookNumber() == 2;

			systemUserService.add(getEmbeddedIdObj(), getIdClassObj());
		};
	}

	private SystemUserWithIdClass getIdClassObj() {
		SystemUserWithIdClass idClassObj = new SystemUserWithIdClass();
		idClassObj.setSubSystem("idClass");
		idClassObj.setUserName("user1");
		idClassObj.setName("name1");
		return idClassObj;
	}

	private SystemUserWithEmbeddedId getEmbeddedIdObj() {
		SystemUserWithEmbeddedId embeddedIdObj = new SystemUserWithEmbeddedId();
		embeddedIdObj.setPk(new EmbeddedIdPK("embeddedId", "user2"));
		embeddedIdObj.setName("name2");
		return embeddedIdObj;
	}

	private Book getBook() {
		Book book = new Book();
		book.setMainAuthor(new Author("jason tian", "jason@self.email"));
		book.setSecondAuthor(new Author("tom wu", "tom.wu@people.email"));
		book.setTitle("hibernate study - property mapping");
		book.setCategory(Category.JAVA);
		book.setKind(Kind.PUBLIC);
		book.setFree(true);
		book.setComment(new Comment("show how to map basic property", new Date()));
		book.setOption("option description.");
		return book;
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
