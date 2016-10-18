package jason.self.fms.service.rule.rule;

import java.util.Collections;
import java.util.Set;

import jason.self.fms.service.action.ActionHandler;
import jason.self.fms.service.rule.expression.Message;

public class Severity {
	protected static final String DEFAULT_SEVERITY_MESSAGE_NAME = "default.severity.message";

	public static final int SEVERITY_FIRE = 1;
	public static final int SEVERITY_UNDEFINED = -1;

	transient private Rule mRule;
	protected int mSeverityLevel;
	protected String mId;
	protected boolean mDisabled;

	protected Set<ActionHandler> mFireActionHandlers = Collections.emptySet();
	protected Set<ActionHandler> mExitActionHandlers = Collections.emptySet();

	protected Message mMessage = new Message(DEFAULT_SEVERITY_MESSAGE_NAME,
			"Rule @" + RuleConstants.PREDEFINED_VARIABLE_RULE_NAME + " fired with @"
					+ RuleConstants.PREDEFINED_VARIABLE_SEVERITY_LEVEL_NAME + " severity.");

	protected Severity() {
	}

	protected Severity(int severityLevel) {
		this();
		setSeverityLevel(severityLevel);
	}

	protected Severity(int severityLevel, Set<ActionHandler> actionHandlers) {
		this(severityLevel);
		mFireActionHandlers.addAll(actionHandlers);

	}

	public Message getMessage() {
		return mMessage;
	}

	public void setMessage(Message message) {
		mMessage = message;
	}

	protected void setRule(Rule rule) {
		mRule = rule;
	}

	public Rule getRule() {
		return mRule;
	}

	public int getSeverityLevel() {
		return mSeverityLevel;
	}

	private void setSeverityLevel(int severityLevel) {
		mSeverityLevel = severityLevel;
	}

	public boolean isDisabled() {
		return mDisabled;
	}

	public void setDisabled(boolean disabled) {
		mDisabled = disabled;
	}

	public void clearVersionInfo() {
		mId = null;

		for (ActionHandler handler : mFireActionHandlers) {
			handler.clearVersionInfo();
		}
		for (ActionHandler handler : mExitActionHandlers) {
			handler.clearVersionInfo();
		}
	}

}
