package jason.self.fms.service.rule.expression;

public class RuleVariable {
	protected String mText = "";
	protected String mName;

	public RuleVariable() {
	};

	public RuleVariable(String name, String text) {
		mName = name;
		mText = text;
	}

	public String getText() {
		return mText != null ? mText : "";
	}

	public void setText(String text) {
		mText = text;
	}

	public String getName() {
		return mName != null ? mName : "";
	}

	public void setName(String name) {
		mName = name;
	}
}
