package self.jason.study.hibernate.association.unimany2many;

import java.util.List;

public class Student {
	private Long id;
	private String name;
	
	List<Course> courses;
}
