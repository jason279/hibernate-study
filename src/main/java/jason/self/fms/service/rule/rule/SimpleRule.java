package jason.self.fms.service.rule.rule;

public class SimpleRule extends Rule{
	protected SimpleRule() {
		super();
	}

	public SimpleRule(String id) {
		super(id);
	}

	@Override
	public String getTypeName() {
		return "Simple";
	}

}
