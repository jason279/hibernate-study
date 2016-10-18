package jason.self.fms.service.rule.rule;

import java.util.Set;

import jason.self.fms.service.action.ActionHandler;


public class UnconditionalSeverity extends Severity {
	protected UnconditionalSeverity() {
		super();
	}

	public UnconditionalSeverity(int severityLevel) {
		super(severityLevel);
	}

	public UnconditionalSeverity(int severityLevel, Set<ActionHandler> actionHandlers) {

		super(severityLevel, actionHandlers);
	}
}
