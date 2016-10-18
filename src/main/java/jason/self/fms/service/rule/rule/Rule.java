package jason.self.fms.service.rule.rule;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jboss.logging.Logger;
import org.jibx.runtime.JiBXException;

import jason.self.fms.model.topology.TopologyType;
import jason.self.fms.service.Versionable;
import jason.self.fms.service.cartridge.api.DomainNameUtil;
import jason.self.fms.service.rule.expression.Expression;
import jason.self.fms.service.rule.expression.Message;
import jason.self.fms.service.schedule.Schedule;
import jason.self.fms.service.schedule.service.ScheduleServiceMBean;
import jason.self.fms.util.JibxHelper;

public abstract class Rule implements Versionable {
	private static final Logger CAT = Logger.getLogger(Rule.class);
	protected String mVersionID;
	private String mId;
	protected Timestamp mCreatedTimestamp;
	protected String mName;
	protected String mComments;
	protected boolean mIsExternal = true;
	protected boolean mIsUserCreated;
	protected boolean mTriggerWithoutData = true;
	protected TriggerType mTriggerType = TriggerType.DATA_DRIVEN;
	protected long mFiringInterval;
	protected TopologyQuery mDomainQuery;
	protected transient TopologyType mScopingTopologyObjectType;
	protected String mCartridgeName;
	protected String mCartridgeVersion;
	private String mDomainName = null;
	protected Date mActionsSuspendedTill = new Date(0);
	protected Date mAlarmsSuspendedTill = new Date(0);
	protected String mHelp;
	protected String mTriggeringEventName;
	private Schedule mTriggeringSchedule;
	protected boolean mSuspended;

	protected Map<Integer, Severity> mSeverities = new HashMap<Integer, Severity>(4);
	protected Set<FiringStrategy> mFiringStrategies = Collections.emptySet();
	protected Set<Expression> mExpressions = Collections.emptySet();
	protected Set<Message> mMessages = Collections.emptySet();

	private ReentrantReadWriteLock mEffectiveScheduleLock = new ReentrantReadWriteLock();
	private HashSet<String> mEffectiveScheduleIds = new HashSet<String>(4);

	private ReentrantReadWriteLock mBlackOutScheduleLock = new ReentrantReadWriteLock();
	private HashSet<String> mBlackOutScheduleIds = new HashSet<String>(4);

	// not used in hbm.xml
	protected boolean mIsTransient;
	protected transient ScheduleServiceMBean mScheduleService;
	private ReentrantReadWriteLock mClearingLock = new ReentrantReadWriteLock();
	protected boolean mEditable = true;

	protected Rule() {
	}

	protected Rule(String id) {
		mId = id;
	}

	protected Rule(String id, String name, boolean isTransient) {

		if (id == null && isTransient) {
			mId = UUID.randomUUID().toString();
		}

		mName = name;
		mIsTransient = isTransient;
		mEditable = true;
	}

	// abstract method
	public abstract String getTypeName();

	public Set<String> getBlackOutScheduleIds() {
		mBlackOutScheduleLock.readLock().lock();
		try {
			return Collections.unmodifiableSet(mBlackOutScheduleIds);
		} finally {
			mBlackOutScheduleLock.readLock().unlock();
		}
	}

	public void setBlackOutScheduleIds(Set<String> blackOutScheduleIds) {
		mBlackOutScheduleLock.writeLock().lock();
		try {
			mBlackOutScheduleIds = new HashSet<String>(1);
			if (null != blackOutScheduleIds) {
				mBlackOutScheduleIds.addAll(blackOutScheduleIds);
			}
		} finally {
			mBlackOutScheduleLock.writeLock().unlock();
		}
	}

	public Set<String> getEffectiveScheduleIds() {
		mEffectiveScheduleLock.readLock().lock();
		try {
			return Collections.unmodifiableSet(mEffectiveScheduleIds);
		} finally {
			mEffectiveScheduleLock.readLock().unlock();
		}
	}

	public void setEffectiveScheduleIds(Set<String> effectiveScheduleIds) {
		mEffectiveScheduleLock.writeLock().lock();
		try {
			mEffectiveScheduleIds = new HashSet<String>(1);
			if (null != effectiveScheduleIds) {
				mEffectiveScheduleIds.addAll(effectiveScheduleIds);
			}
		} finally {
			mEffectiveScheduleLock.writeLock().unlock();
		}
	}

	public long getFiringInterval() {
		return mFiringInterval;
	}

	public Set<Message> getMessages() {
		if (mMessages == null) {
			mMessages = Collections.emptySet();
		}
		return mMessages;
	}

	public void setMessages(Set<Message> messages) {
		mMessages = Collections.emptySet();
		if (null != messages) {
			mMessages.addAll(messages);
		}
	}

	public Set<Expression> getExpressions() {
		return mExpressions;
	}

	public void setExpressions(Set<Expression> expressions) {
		mExpressions = Collections.emptySet();
		if (null != expressions) {
			mExpressions.addAll(expressions);
		}
	}

	public Set<FiringStrategy> getFiringStrategies() {
		return mFiringStrategies;
	}

	public void setFiringStrategies(Set<FiringStrategy> firingStrategies) {
		mFiringStrategies = Collections.emptySet();
		if (null != firingStrategies) {
			mFiringStrategies = new HashSet<FiringStrategy>(firingStrategies);
		}
	}

	public Map<Integer, Severity> getSeverities() {
		return mSeverities;
	}

	public void setSeverities(Map<Integer, Severity> severities) {
		mSeverities = new HashMap<Integer, Severity>(severities.size());
		for (Severity severity : severities.values()) {
			addSeverity(severity);
		}
	}

	public void addSeverity(Severity severity) {
		severity.setRule(this);
		mSeverities.put(severity.getSeverityLevel(), severity);
	}

	public boolean isSuspended() {
		return mSuspended;
	}

	public void setSuspended(boolean suspended) {
		mSuspended = suspended;
	}

	public void setAlarmsSuspendedTill(Date suspendedTill) {
		if (suspendedTill == null) {
			// backward compatibility
			suspendedTill = new Date(0);
		}
		mAlarmsSuspendedTill = suspendedTill;
	}

	public Date getAlarmsSuspendedTill() {
		if (mAlarmsSuspendedTill == null)
			return null;
		return (Date) mAlarmsSuspendedTill.clone();
	}

	public void setActionsSuspendedTill(Date suspendedTill) {
		if (suspendedTill == null) {
			// backward compatibility
			suspendedTill = new Date(0);
		}
		mActionsSuspendedTill = suspendedTill;
	}

	public Date getActionsSuspendedTill() {
		if (mActionsSuspendedTill == null)
			return null;
		return (Date) mActionsSuspendedTill.clone();
	}

	public String getDomainName() {
		return DomainNameUtil.isEmptyDomain(mDomainName) ? inferDomainName() : mDomainName;
	}

	private String inferDomainName() {
		return getScopingTopologyObjectType() == null ? null : getScopingTopologyObjectType().getDomainName();
	}

	public void setDomainName(String domainName) throws IllegalArgumentException {
		if (!DomainNameUtil.isValidDomainName(domainName)) {
			throw new IllegalArgumentException("Invalid domain name: " + domainName);
		}
		mDomainName = domainName;
	}

	public TopologyType getScopingTopologyObjectType() {
		// if (mScopingTopologyObjectType == null && mDomainQuery != null) {
		// IQueryService queryService =
		// ServiceLocatorFactory.getLocator().getQueryService();
		// try {
		// mScopingTopologyObjectType =
		// queryService.getScopingTopologyObjectType(mDomainQuery.getQueryText());
		// } catch (QueryVerificationException e) {
		// CAT.debug("Ignore query parsing issue, return null", e);
		// }
		// }
		// TODO not finished
		return mScopingTopologyObjectType;
	}

	public String getCartridgeName() {
		return mCartridgeName;
	}

	public void setCartridgeName(String name) {
		mCartridgeName = name;
	}

	public String getCartridgeVersion() {
		return mCartridgeVersion;
	}

	public void setCartridgeVersion(String version) {
		mCartridgeVersion = version;
	}

	public TopologyQuery getDomainQuery() {
		return mDomainQuery;
	}

	public void setDomainQuery(TopologyQuery domainQuery) {
		mDomainQuery = domainQuery;
		mScopingTopologyObjectType = null;
	}

	public TriggerType getTriggerType() {
		return mTriggerType;
	}

	public void setDataDriven() {

		mTriggerType = TriggerType.DATA_DRIVEN;
		mTriggeringEventName = null;
		mTriggeringSchedule = null;
		mFiringInterval = 0L;
	}

	public void setTimeDriven(long firingInterval) {

		mTriggerType = TriggerType.TIME_DRIVEN;
		mFiringInterval = firingInterval;
		mTriggeringEventName = null;
		mTriggeringSchedule = null;
	}

	public void setEventDriven(String eventName) {
		mTriggerType = TriggerType.SYSTEM_EVENT;
		mTriggeringEventName = eventName;
		mFiringInterval = 0L;
		mTriggeringSchedule = null;
	}

	private void setScheduleDriven(Schedule schedule) {
		mTriggerType = TriggerType.SCHEDULE_DRIVEN;
		mTriggeringSchedule = schedule;
		mTriggeringEventName = null;
		mFiringInterval = 0L;
	}

	public void setScheduleDriven(String scheduleId) {
		// setScheduleDriven(ScheduleFactory.getInstance().createNamedScheduleReference(scheduleId));
		// TODO not finish
	}

	public boolean isTriggerWithoutData() {
		return mTriggerWithoutData;
	}

	public void setTriggerWithoutData(boolean triggerWithoutData) {
		mTriggerWithoutData = triggerWithoutData;
	}

	public boolean getIsUserCreated() {
		return mIsUserCreated;
	}

	public void setIsUserCreated(boolean isUserCreated) {
		mIsUserCreated = isUserCreated;
	}

	public boolean getIsExternal() {
		return mIsExternal;
	}

	public void setIsExternal(boolean external) {
		mIsExternal = external;
	}

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		mId = id;
	}

	public String getComments() {
		return mComments;
	}

	public void setComments(String mComments) {

		this.mComments = mComments;
	}

	public String getVersionID() {
		return mVersionID;
	}

	public void setVersionID(String versionID) {
		mVersionID = versionID;
	}

	public Timestamp getCreatedTimestamp() {
		return mCreatedTimestamp;
	}

	public void setCreatedTimestamp(Timestamp createdTimestamp) {
		mCreatedTimestamp = createdTimestamp;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {

		this.mName = mName;
	}

	protected String getTriggeringScheduleXML() throws JiBXException {
		if (mTriggerType == TriggerType.SCHEDULE_DRIVEN && (mTriggeringSchedule != null)) {
			return JibxHelper.toXML(mTriggeringSchedule);
		} else {
			return null;
		}
	}

	protected void setTriggeringScheduleXML(String xml) throws JiBXException {
		mTriggeringSchedule = (xml == null) ? null : (Schedule) JibxHelper.unmarshalXML(xml, Schedule.class);
	}

	public enum TriggerType {
		TIME_DRIVEN, DATA_DRIVEN, SCHEDULE_DRIVEN, SYSTEM_EVENT
	}

	// method not used in hbm file
	public boolean isIsTransient() {
		return mIsTransient;
	}

	public void setIsTransient(boolean isTransient) {

		mIsTransient = isTransient;
	}

	public void setScheduleService(ScheduleServiceMBean scheduleService) {
		if (scheduleService == null) {
			throw new IllegalArgumentException("scheduleService is null");
		}

		if (mScheduleService != null) {
			CAT.debug("overwriting rule's previous schedule service");
		}

		mScheduleService = scheduleService;
	}

	public void makeEditable() {
		mEditable = true;
	}

	public void makeUnEditable() {
		mEditable = false;
	}

	// other methods in Versionable
	public void clearVersioningInfo() {
		setVersionID(null);

		mClearingLock.writeLock().lock();

		try {
			for (Severity severity : getSeverities().values()) {
				severity.clearVersionInfo();
			}
			for (FiringStrategy strategy : getFiringStrategies()) {
				strategy.clearVersionInfo();
			}
		} finally {
			mClearingLock.writeLock().unlock();
		}

	}
}
