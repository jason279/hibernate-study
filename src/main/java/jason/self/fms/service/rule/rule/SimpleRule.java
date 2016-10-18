package jason.self.fms.service.rule.rule;

import java.util.Set;

import jason.self.fms.service.action.ActionHandler;


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
	
	public SimpleRule(String id, String name, boolean isTransient,
			String condition,
			Set<ActionHandler> actionHandlers,
			Set<ActionHandler> undefinedActionHandlers
			) {

		super(id, name, isTransient);

		ConditionalSeverity conditionalSeverity =
				new ConditionalSeverity(Severity.SEVERITY_FIRE,
						actionHandlers,
						condition, "Rule Fired");

		addSeverity(conditionalSeverity);

		UnconditionalSeverity unconditionalSeverity =
				new UnconditionalSeverity(Severity.SEVERITY_UNDEFINED,
						undefinedActionHandlers);

		addSeverity(unconditionalSeverity);


	}

}
