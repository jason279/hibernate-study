package jason.self.fms.service.sl;

public enum Service {
	RULE(8);
	Service(int value) {
		this.value = value;
	}

	private final int value;

	public int value() {
		return value;
	}
}
