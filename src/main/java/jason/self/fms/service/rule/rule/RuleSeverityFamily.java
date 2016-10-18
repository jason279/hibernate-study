package jason.self.fms.service.rule.rule;

public class RuleSeverityFamily extends Rule {
	public RuleSeverityFamily() {
	}

	public RuleSeverityFamily(String id, String name, boolean isTransient) {
		super(id, name, isTransient);
	}

	@Override
	public String getTypeName() {
		return "Severity Family";
	}

}
