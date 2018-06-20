package self.jason.study.hibernate.mapping.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {
	private static final String PREFIX = " -- comment on ";
	private String message;
	private Date date;

	public Comment(String message, Date date) {
		this.message = message;
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public static Comment parse(String comment) {
		if (comment == null) {
			return null;
		}
		String[] parts = comment.split(PREFIX);
		if (parts.length != 2) {
			throw new RuntimeException("Invalid format for comment:" + comment);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return new Comment(parts[0], sdf.parse(parts[1]));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return message + PREFIX + sdf.format(date);
	}

}
