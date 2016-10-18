package jason.self.fms.service.rule.rule;

public class InstantiationDelayFiringStrategy extends AbstractFiringStrategy {
	private long mDelayPeriod;

	protected InstantiationDelayFiringStrategy() {
	}

	public InstantiationDelayFiringStrategy(long delayPeriod) {
		this(null, delayPeriod);
	}

	protected InstantiationDelayFiringStrategy(String id, long delayPeriod) {
		super(id);

		assert (delayPeriod >= 0);

		mDelayPeriod = delayPeriod;
	}

	public synchronized long getDelayPeriod() {
		return mDelayPeriod;
	}

}
