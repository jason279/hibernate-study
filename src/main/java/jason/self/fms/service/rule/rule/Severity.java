package jason.self.fms.service.rule.rule;

import java.util.Collections;
import java.util.Set;

import jason.self.fms.service.action.ActionHandler;

public class Severity {
	transient private Rule mRule;
	protected int mSeverityLevel;
	protected String mId;

	protected Set<ActionHandler> mFireActionHandlers = Collections.emptySet();
	protected Set<ActionHandler> mExitActionHandlers = Collections.emptySet();

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
