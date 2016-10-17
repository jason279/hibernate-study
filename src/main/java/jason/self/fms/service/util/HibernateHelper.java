package jason.self.fms.service.util;

import java.util.concurrent.atomic.AtomicLong;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jboss.logging.Logger;

import jason.self.fms.service.CurrentVersion;
import jason.self.fms.service.NitrogenBroadcastingService;
import jason.self.fms.service.Versionable;
import jason.self.fms.service.sl.Service;

public class HibernateHelper {
	private static final Logger CAT = Logger.getLogger(HibernateHelper.class);
	public static final String CHANGE_NOTIFICATION = "config.object.modified";

	private static AtomicLong mNotificationCounter = new AtomicLong();

	public static long getNextNotificationSeq() {
		return mNotificationCounter.getAndIncrement();
	}

	public enum OperationVersionable {
		SAVE_MAKE_CURRENT, SAVE_KEEP_CURRENT, SAVE_FORCE_MAKE_CURRENT, DELETE_CURRENT, DELETE_CURRENT_FORCE, UPDATE_ONLY, MAKE_CURRENT
	}

	public static Object persist(OperationVersionable operation, SessionFactory sessionFactory, Versionable object,
			NitrogenBroadcastingService mbean, boolean isUserCreated, Service service) {
		try {
			return persistObject(operation, sessionFactory, object, mbean, isUserCreated, service);
		} catch (InconsistentVersionException e) {
			// Should not get in here - no objects are passed for version
			// verification
			// in fms: CAT.ignoreException("Only applies to methods with a list
			// of
			// versionable objects to check", e);
			CAT.error("Only applies to methods with a list of versionable objects to check", e);
			return null;
		}
	}

	protected static Object persistObject(OperationVersionable operation, SessionFactory sessionFactory,
			Versionable object, NitrogenBroadcastingService mbean, boolean isUserCreated, Service service)
			throws InconsistentVersionException {

		// Attempt to obtain the session associated with the current transaction
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			CAT.debug("No enclosing JTA transaction.", e);
		}

		if (session != null) {

			// Perform the operation within the existing JTA transaction
			Transaction transaction = session.getTransaction();

			// verify objects versions
			// in fms, the relatedObjects are null, so nothing will happen in
			// the method, ignore it.
			// checkCurrentVersions(relatedObjects, session);

			performOperationVersionable(operation, session, transaction, object, mbean, isUserCreated, service);
		} else {

			// No enclosing JTA transaction. Run in a new transaction.
			session = sessionFactory.openSession();
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();

				// verify objects versions
				// checkCurrentVersions(relatedObjects, session);

				performOperationVersionable(operation, session, transaction, object, mbean, isUserCreated, service);
				transaction.commit();
				transaction = null;
			} finally {
				if (transaction != null) {

					// Transaction was not committed - perform a rollback
					try {
						transaction.rollback();
					} catch (Throwable t) {
						CAT.error("Error rolling back transaction.", t);
					}
				}
			}
		}
		return object;
	}

	private static void performOperationVersionable(OperationVersionable operation,
			Session session,
			Transaction transaction, Versionable versionableObject,
			NitrogenBroadcastingService mbean, boolean isUserCreated,
			Service service) {

		String notificationType = "";
		CurrentVersion currentVersion = getCurrentVersion(versionableObject,
				session);

		String curVersionID = null;
		if (currentVersion != null) {
			curVersionID = currentVersion.getVersionID();
		}


		switch (operation) {

//			case SAVE_MAKE_CURRENT:
//				prepareVersionable(versionableObject, isUserCreated);
//				session.save(versionableObject);
//
//				if (!isCurrentVersionUserCreated(currentVersion)) {
//					updateCurrentVersion(currentVersion, versionableObject,
//							session, service);
//					notificationType = CHANGE_NOTIFICATION;
//				} else {
//					CAT.debug("Current version of " + versionableObject +
//							" is created by user and not overwritten by cartridge version.");
//				}
//
//				// update the security service
//
//				break;
//			case SAVE_KEEP_CURRENT:
//				prepareVersionable(versionableObject, isUserCreated);
//				session.save(versionableObject);
//
//				AuditVersionInfoThreadLocal.setVersionInfo(versionableObject.getId(),
//						curVersionID,
//						versionableObject.getVersionID(),
//						service.value());
//
//				break;
			case SAVE_FORCE_MAKE_CURRENT:
//				prepareVersionable(versionableObject, isUserCreated);
				session.save(versionableObject);
				updateCurrentVersion(currentVersion, versionableObject, session, service);

				notificationType = CHANGE_NOTIFICATION;
				break;
//			case MAKE_CURRENT:
//				updateCurrentVersion(currentVersion, versionableObject, session, service);
//				notificationType = CHANGE_NOTIFICATION;
//				break;
//			case DELETE_CURRENT:
//				if (!isCurrentVersionUserCreated(currentVersion)) {
//					deleteCurrentVersion(currentVersion, versionableObject,
//							session, service);
//					notificationType = DELETE_NOTIFICATION;
//				}
//				break;
//			case DELETE_CURRENT_FORCE:
//				deleteCurrentVersion(currentVersion, versionableObject,
//						session, service);
//				notificationType = DELETE_NOTIFICATION;
//				break;
//			case UPDATE_ONLY:
//				session.update(versionableObject);
//				AuditVersionInfoThreadLocal.setVersionInfo(versionableObject.getId(),
//						curVersionID,
//						versionableObject.getVersionID(),
//						service.value());
//
//				break;
		}

//		if (!notificationType.equals("")) {
//			transaction.registerSynchronization(TransactionalMemoryUtils.adjustSynchronizationOrder(
//					new SynchronizationEventGenerator(
//					versionableObject, mbean, notificationType,
//					getNextNotificationSeq())));
//		}

	}

	public static CurrentVersion getCurrentVersion(Versionable versionableObject, Session session) {
		if (versionableObject.getId() == null) {
			// There cannot be a current version with a new object
			return null;
		}

		return (CurrentVersion) session.get(CurrentVersion.class, versionableObject.getId());

	}

	public static void updateCurrentVersion(CurrentVersion currentVersion, Versionable versionable, Session session,
			Service service) {

		if (currentVersion == null) {
			currentVersion = new CurrentVersion();
		}
		currentVersion.setID(versionable.getId());
		currentVersion.setVersionID(versionable.getVersionID());
		currentVersion.setIsUserCreated(versionable.getIsUserCreated());
		currentVersion.setService(service.value());
		session.saveOrUpdate(currentVersion);
	}

}
