package jason.self.fms.service;

import java.sql.Timestamp;

public interface Versionable {
	String getId();

	void setId(String id);

	String getVersionID();

	void setVersionID(String versionID);

	Timestamp getCreatedTimestamp();

	void setCreatedTimestamp(Timestamp createdTimestamp);

	void clearVersioningInfo();

	boolean getIsUserCreated();

	void setIsUserCreated(boolean isUserCreated);
}
