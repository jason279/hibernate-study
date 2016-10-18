package jason.self.fms.service.rule.expression;


public class Condition extends Expression {
	protected Condition() {
		super(Condition.class.getName(), "");
	}

	public Condition(String text) {
		super(Condition.class.getName(), text);
	}
}
