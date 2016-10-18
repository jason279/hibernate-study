package jason.self.fms.service.rule.rule;

public class HitRateFiringStrategy extends AbstractFiringStrategy {
	private int mMonitorWindowSize;
	private int mMonitoredHitThreshold;

	// derived field
	private transient boolean[] mMonitorWindow;
	private transient int mMonitorWindowPointer;
	private transient int mMonitoredHitCount;

	protected HitRateFiringStrategy() {
	}

	public HitRateFiringStrategy(int monitoredHitThreshold, int monitorWindowSize) {
		this(null, monitoredHitThreshold, monitorWindowSize);
	}

	protected HitRateFiringStrategy(String id, int monitoredHitThreshold, int monitorWindowSize) {
		super(id);

		assert monitorWindowSize > 0 : "Must have a monitored window size greater than 0";
		assert monitorWindowSize >= monitoredHitThreshold : "Hit threshold should be less than the monitored window size";

		mMonitoredHitThreshold = monitoredHitThreshold;
		mMonitorWindowSize = monitorWindowSize;
		mMonitorWindow = new boolean[mMonitorWindowSize];
		clearState();
	}
	
	public synchronized int getMonitorWindowSize() {
		return mMonitorWindowSize;
	}

	public synchronized int getMonitoredHitThreshold() {
		return mMonitoredHitThreshold;
	}


	public synchronized void clearState() {
		for (int i = 0; i < mMonitorWindow.length; i++) {
			mMonitorWindow[i] = false;
		}
		mMonitorWindowPointer = 0;
		mMonitoredHitCount = 0;
	}
}
