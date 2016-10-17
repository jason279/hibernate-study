package jason.self.fms.service.rule.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;

import jason.self.fms.service.NitrogenBroadcastingService;
import jason.self.fms.service.rule.rule.Rule;
import jason.self.fms.service.schedule.service.ScheduleServiceMBean;
import jason.self.fms.service.sl.Service;
import jason.self.fms.service.util.HibernateHelper;

public class RulePersistenceService implements NitrogenBroadcastingService{
	private static final Logger CAT = Logger.getLogger(RulePersistenceService.class);
	static final String LATEST_VERSION_BY_ID = "FROM Rule AS rule WHERE rule.mId = :id"
			+ " ORDER BY rule.mCreatedTimestamp DESC";

	private static final HibernateHelper.OperationVersionable SAVE = HibernateHelper.OperationVersionable.SAVE_FORCE_MAKE_CURRENT;

	private final SessionFactory mSessionFactory;
	private final ScheduleServiceMBean mScheduleService;
	private final Map<String, Rule> mTransientRules = new ConcurrentHashMap<String, Rule>(1, 0.75f, 1);
	// in fms, it uses TxMapAccessTemplate
	private final Map<String, Rule> mDatabaseRules = new ConcurrentHashMap<String, Rule>();

	private CloningStrategy mCloningStrategy = new DeepCopyingCloningStrategy();

	public RulePersistenceService(SessionFactory sessionFactory, ScheduleServiceMBean scheduleService) {
		if (sessionFactory == null) {
			throw new IllegalArgumentException("sessionFactory cannot be null");
		}
		if (scheduleService == null) {
			throw new IllegalArgumentException("scheduleService cannot be null");
		}

		mSessionFactory = sessionFactory;
		mScheduleService = scheduleService;
	}

	public Rule save(Rule rule) {

		// brand new rules that come in from the wild might not have an ID
		// associated with them, so
		// generate one now. databases are terrible at rolling UUIDs.
		String id = rule.getId();
		if (id == null) {
			UUID newId = UUID.randomUUID();
			id = newId.toString();
			rule.setId(id);

			CAT.debug("rolled a new ID for " + rule);
		}

		if (rule.isIsTransient()) {
			CAT.debug("adding " + rule + " to the transient collection");

			mTransientRules.put(id, rule);
			sendNotification(HibernateHelper.CHANGE_NOTIFICATION, rule);
		} else {
			Session session = mSessionFactory.openSession();
			try {
				removeFromDbCache(id);

				CAT.debug("adding " + rule + " to the persisted database");

				// the logic used to determine the latest version of a rule uses
				// the rule's creation
				// time, which is stored as a datetime. some databases (mysql
				// and sqlsrv) only have one
				// second time resolution, which means if save is invoked twice
				// quickly for the same
				// rule they'll end up with the same timestamp and bugger up the
				// latest version logic.
				//
				// so just spin in a loop until there's a 1 second difference
				// between the times. yuck.
				//
				// it would be nice to switch that field to a long, or just have
				// an auto-increment index
				// or something more fine-grained that 1 second.
				Map<String, Object> params = Collections.singletonMap("id", (Object) id);
				Rule latestVersion = runUniqueQuery(LATEST_VERSION_BY_ID, params);

				if (latestVersion != null) {
					Timestamp latestVersionTimestamp = latestVersion.getCreatedTimestamp();
					long latestVersionTime = latestVersionTimestamp.getTime();

					try {
						long diff = Math.abs(System.currentTimeMillis() - latestVersionTime);
						while (diff < 1000) {
							CAT.debug("sleeping " + diff + "ms to keep rule versions 1s apar");
							Thread.sleep(diff);
							diff = Math.abs(System.currentTimeMillis() - latestVersionTime);
						}
					} catch (InterruptedException e) {
						CAT.debug("interrupted before time was up; saving immediately", e);

						// reset the thread interrupted flag. something has
						// grown impatient with this
						// thread and would like it to exit ASAP. future
						// blocking operations should
						// fail, too
						Thread.currentThread().interrupt();
					}
				}

				// saving the rule should generate a unique created time
				boolean isUserCreated = rule.getIsUserCreated();
				rule = (Rule) HibernateHelper.persist(SAVE, mSessionFactory, rule, this, isUserCreated, Service.RULE);
				initServices(Collections.singleton(rule));

				cache(Arrays.asList(rule));
			} finally {
				session.close();
			}
		}

		return rule;
	}

	private void sendNotification(String type, Rule rule) {
		assert rule.isIsTransient() : "only transient rules require manual notification emission";
		assert type != null && !type.equals("") : "bad notification type";

		Session session = mSessionFactory.getCurrentSession();
		Transaction transaction = session.getTransaction();
		assert transaction != null : "method called outside of a transaction";

		long seq = HibernateHelper.getNextNotificationSeq();
		// TODO not finish
		CAT.info("send notification " + type + ",seq=" + seq);
		// Synchronization sync = new SynchronizationEventGenerator(rule, this,
		// type, seq);
		// sync = TransactionalMemoryUtils.adjustSynchronizationOrder(sync);
		// transaction.registerSynchronization(sync);
	}

	private void removeFromDbCache(String id) {
		Map<String, Rule> databaseRules = getDatabaseRules();
		Rule removed = databaseRules.remove(id);
		if (removed != null) {
			System.out.println("debug2 - uncached " + removed);
		}
	}

	private synchronized Map<String, Rule> getDatabaseRules() {
		return mDatabaseRules;
	}

	private Rule runUniqueQuery(String query, Map<String, Object> params) {
		Rule found = null;

		Session session = mSessionFactory.openSession();
		try {
			Query<Rule> q = session.createQuery(query, Rule.class);
			assignParameters(q, params);
			q.setMaxResults(1);

			// session doesn't need to be closed - it is managed by the
			// surrounding transaction
			// in fms: found = q.uniqueResult();
			found = q.uniqueResultOptional().get();
		} catch (HibernateException e) {
			System.out.println("error - hibernate failure" + e);
		} finally {
			session.close();
		}

		if (found != null) {
			initServices(Collections.singleton(found));
		}

		return found;
	}

	private static void assignParameters(Query query, Map<String, Object> params) {
		if (params == null) {
			params = Collections.emptyMap();
		}

		for (Map.Entry<String, Object> entry : params.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			if (value instanceof Collection<?>) {
				query.setParameterList(key, (Collection<?>) value);
			} else {
				query.setParameter(key, value);
			}
		}
	}

	private void initServices(Collection<Rule> rules) {
		for (Rule rule : rules) {
			// TODO: this can probably be expanded in the future to basically
			// all the other services
			// that are included in a rule's start method
			rule.setScheduleService(mScheduleService);
		}
	}

	private synchronized void cache(Collection<Rule> rules) {
		Map<String, Rule> dbRules = getDatabaseRules();

		int size = rules.size();
		HashMap<String, Rule> added = new HashMap<String, Rule>(size);
		for (Rule rule : rules) {
			String id = rule.getId();

			// save a cloned copy so that the cached version cannot be modified
			// by clients
			Rule cloned = mCloningStrategy.clone(rule);

			CAT.debug("cached " + cloned);
			added.put(id, cloned);
		}

		dbRules.putAll(added);
	}

	public static interface CloningStrategy {
		/**
		 * Clone an {@code object}
		 * 
		 * @param object
		 *            The object to clone
		 * @return A cloned version of {@code object}
		 */
		<T> T clone(T object);
	}

	public static class DeepCopyingCloningStrategy implements CloningStrategy {
		/**
		 * Return a deep copy of {@code object} by writing it to a temporary
		 * buffer then reading the copy back in.
		 */
		public <T> T clone(T object) {
			// in fms: return (T) ObjectCloner.deepCopy( object );
			return (T) object;
		}

		@Override
		public String toString() {
			return "deep copying cloning strategy";
		}
	}
}
