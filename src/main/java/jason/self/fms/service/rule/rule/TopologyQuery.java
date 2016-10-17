package jason.self.fms.service.rule.rule;

public class TopologyQuery {
	protected String mQueryText;

	protected TopologyQuery() {
	}

	public TopologyQuery(String queryText) {
		this.mQueryText = queryText;
	}

	public String getQueryText() {
		return mQueryText;
	}
}
