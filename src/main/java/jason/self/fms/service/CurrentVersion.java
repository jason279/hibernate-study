package jason.self.fms.service;

public class CurrentVersion {
	protected String mID;
	protected String mVersionID;
	protected boolean mIsUserCreated;
	protected int mService;

	public String getVersionID() {
		return mVersionID;
	}

	public void setVersionID(String versionID) {
		mVersionID = versionID;
	}

	public String getID() {
		return mID;
	}

	public void setID(String ID) {
		mID = ID;
	}

	public boolean getIsUserCreated() {
		return mIsUserCreated;
	}

	public void setIsUserCreated(boolean isUserCreated) {
		mIsUserCreated = isUserCreated;
	}

	public int getService() {
		return mService;
	}

	public void setService(int service) {
		mService = service;
	}
}
