package jason.self.fms.service.rule.rule;

import java.util.Set;

import jason.self.fms.service.action.ActionHandler;
import jason.self.fms.service.rule.expression.Condition;
import jason.self.fms.service.rule.expression.Message;

public class ConditionalSeverity extends Severity {
	protected Condition mCondition = new EmptyCondition();

	public ConditionalSeverity(int severityLevel, Set<ActionHandler> actionHandlers, String condition, String message) {

		super(severityLevel, actionHandlers);
		mMessage = new Message("SeverityMessage", message);

		mCondition = new Condition(condition);
	}

	public ConditionalSeverity(int severityLevel) {
		super(severityLevel);
	}

	protected ConditionalSeverity() {
		super();
	}

	private static class EmptyCondition extends Condition {
	}
}
