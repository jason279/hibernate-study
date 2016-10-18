package jason.self.fms.service.rule.rule;

public class AbstractFiringStrategy implements FiringStrategy {
	private String mId;

	protected AbstractFiringStrategy() {
		mId = null;
	}

	public void clearVersionInfo() {
		mId = null;
	}

	protected AbstractFiringStrategy(String id) {
		mId = id;
	}

	public String getId() {
		return mId;
	}
}
