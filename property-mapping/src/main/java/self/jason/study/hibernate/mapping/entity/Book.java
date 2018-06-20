package self.jason.study.hibernate.mapping.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import self.jason.study.hibernate.mapping.converter.CommentConverter;

@Entity
@Table(name = "jason_book")
@SecondaryTable(name = "book_option", pkJoinColumns = @PrimaryKeyJoinColumn(name = "book_id"))
@Where(clause = "free=true")
@AttributeOverrides({ @AttributeOverride(name = "mainAuthor.name", column = @Column(name = "main_author_name")),
		@AttributeOverride(name = "mainAuthor.email", column = @Column(name = "main_author_email")),
		@AttributeOverride(name = "secondAuthor.name", column = @Column(name = "second_author_name")),
		@AttributeOverride(name = "secondAuthor.email", column = @Column(name = "second_author_email")) })
@DynamicUpdate
public class Book {
	@Id
	@Basic // @Basic annotation can be omit
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_sequence_generator")
	@SequenceGenerator(name = "book_sequence_generator", sequenceName = "book_sequence", initialValue = 10, allocationSize = 3)
//	@GenericGenerator(name = "book_sequence_generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
//			@Parameter(name = "sequence_name", value = "book_sequence"),
//			@Parameter(name = "initial_value", value = "10"),
//			 @Parameter(name = "increment_size", value = "3"),
//			 @Parameter(name = "optimizer", value = "pooled-lo")
//			})
	private Long id;

	private String title;

	@Embedded // can be ignored
	private Author mainAuthor;

	private Author secondAuthor;

	// default mapping for enumeration property is the ordinal
	private Category category;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private Kind kind;

	@Convert(converter = CommentConverter.class)
	private Comment comment;

	@Type(type = "yes_no")
	private boolean free;

	@Column(table = "book_option", name = "`option`")
	private String option;

	@Transient
	private String noMappingProp;

	public Book() {
	}

	public Book(String title, boolean free) {
		this.title = title;
		this.free = free;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Author getMainAuthor() {
		return mainAuthor;
	}

	public void setMainAuthor(Author mainAuthor) {
		this.mainAuthor = mainAuthor;
	}

	public Author getSecondAuthor() {
		return secondAuthor;
	}

	public void setSecondAuthor(Author secondAuthor) {
		this.secondAuthor = secondAuthor;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Kind getKind() {
		return kind;
	}

	public void setKind(Kind kind) {
		this.kind = kind;
	}

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

}