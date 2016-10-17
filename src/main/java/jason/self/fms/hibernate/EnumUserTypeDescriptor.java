package jason.self.fms.hibernate;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;

import jason.self.fms.service.rule.rule.Rule.TriggerType;

public class EnumUserTypeDescriptor extends AbstractTypeDescriptor<TriggerType> {
	public static final EnumUserTypeDescriptor INSTANCE = new EnumUserTypeDescriptor();

	protected EnumUserTypeDescriptor() {
		super(TriggerType.class);
	}

	public String toString(TriggerType value) {
		return value.getClass().getName() + " " + value.toString();
	}

	public TriggerType fromString(String string) {
		String[] parts = string.split(" ");
		return TriggerType.valueOf(parts[1]);
	}

	public <X> X unwrap(TriggerType value, Class<X> type, WrapperOptions options) {
		if (value == null) {
			return null;
		}
		if (TriggerType.class.isAssignableFrom(type)) {
			return (X) value;
		}
		if (String.class.isAssignableFrom(type)) {
			return (X) toString(value);
		}
		throw unknownUnwrap(type);
	}

	public <X> TriggerType wrap(X value, WrapperOptions options) {
		if (value == null) {
			return null;
		}
		if (String.class.isInstance(value)) {
			return fromString((String) value);
		}
		if (TriggerType.class.isInstance(value)) {
			return (TriggerType) value;
		}
		throw unknownWrap(value.getClass());
	}

}
