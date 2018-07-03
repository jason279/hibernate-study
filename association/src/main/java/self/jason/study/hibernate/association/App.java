package self.jason.study.hibernate.association;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import self.jason.study.hibernate.association.bestone2one.BestOneToOneService;
import self.jason.study.hibernate.association.bione2many.BidirectionalOneToManyService;
import self.jason.study.hibernate.association.bione2one.BidirectionalOneToOneService;
import self.jason.study.hibernate.association.many2one.ManyToOneService;
import self.jason.study.hibernate.association.unione2many.UnidirectionalOneToManyService;
import self.jason.study.hibernate.association.unione2one.UnidirectionalOneToOneService;

@SpringBootApplication
public class App {
	@Autowired
	private ManyToOneService manyToOneService;

	@Autowired
	private UnidirectionalOneToManyService unidirectionalOneToManyService;

	@Autowired
	private BidirectionalOneToManyService bidirectionalOneToManyService;

	@Autowired
	private UnidirectionalOneToOneService unidirectionalOneToOneService;

	@Autowired
	private BidirectionalOneToOneService bidirectionalOneToOneService;

	@Autowired
	private BestOneToOneService bestOneToOneService;

	@Bean
	public CommandLineRunner runner() {
		return args -> {
			manyToOneService.demo();
			// unidirectionalOneToManyService.demo();
			// bidirectionalOneToManyService.demo();
			// unidirectionalOneToOneService.demo();
			// bidirectionalOneToOneService.demo();
			// bestOneToOneService.demo();
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
