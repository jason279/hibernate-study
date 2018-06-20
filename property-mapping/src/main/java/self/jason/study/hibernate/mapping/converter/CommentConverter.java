package self.jason.study.hibernate.mapping.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import self.jason.study.hibernate.mapping.entity.Comment;

@Converter
public class CommentConverter implements AttributeConverter<Comment, String> {
	@Override
	public String convertToDatabaseColumn(Comment comment) {
		return comment == null ? null : comment.toString();
	}

	@Override
	public Comment convertToEntityAttribute(String dbData) {
		return Comment.parse(dbData);
	}

}
