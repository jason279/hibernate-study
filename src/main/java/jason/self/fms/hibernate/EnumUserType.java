package jason.self.fms.hibernate;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

import jason.self.fms.service.rule.rule.Rule.TriggerType;

public class EnumUserType extends AbstractSingleColumnStandardBasicType<TriggerType>{
	public static final EnumUserType INSTANCE = new EnumUserType();

	public EnumUserType() {
		super(VarcharTypeDescriptor.INSTANCE, EnumUserTypeDescriptor.INSTANCE);
	}

	public String getName() {
		return "triggerType";
	}

}
