package jason.self.fms.service.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jason.self.fms.service.CurrentVersion;
import jason.self.fms.service.Versionable;
import jason.self.fms.service.rule.rule.Rule;
import jason.self.fms.service.rule.rule.RuleSeverityFamily;
import jason.self.fms.service.rule.rule.SimpleRule;
import jason.self.fms.service.rule.rule.TopologyQuery;
import jason.self.fms.service.sl.Service;
import jason.self.fms.service.util.HibernateHelper.OperationVersionable;

public class TestHibernateHelper {
	private SessionFactory sessionFactory;

	@Before
	public void setUp() throws Exception {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
//		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
//		} catch (Exception e) {
//			StandardServiceRegistryBuilder.destroy(registry);
//			System.out.println("set up failed:" + e);
//		}
	}

	@After
	public void tearDown() throws Exception {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}

	@Test
	public void testSaveCurrentVersion() throws Exception {
		Versionable rule = new SimpleRule("877e64db-3193-4ea5-8150-22ead239ee9d");
		rule.setId(UUID.randomUUID().toString());
		rule.setVersionID(UUID.randomUUID().toString());
		rule.setIsUserCreated(true);
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		HibernateHelper.updateCurrentVersion(null, rule, session, Service.RULE);
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testGetCurrentVersion() {
		Versionable rule = new SimpleRule("877e64db-3193-4ea5-8150-22ead239ee9d");
		rule.setId(null);
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		CurrentVersion cv = HibernateHelper.getCurrentVersion(rule, session);
		assertNull(cv);
		rule.setId("1256ab60-eade-4ba5-8b24-a4bfeedae926");
		cv = HibernateHelper.getCurrentVersion(rule, session);
		assertEquals("119f5e81-94db-4c64-985d-5724bd613255", cv.getVersionID());
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testPersistNewRule() {
		Rule emailReportSimpleRule = new SimpleRule("877e64db-3193-4ea5-8150-22ead239ee9d");
		emailReportSimpleRule.setCreatedTimestamp(new Timestamp(new Date().getTime()));
		emailReportSimpleRule.setName("Email Reports Sample");
		emailReportSimpleRule.setComments("Emails reports as a PDF attachment. Edit rule condition to filter on report name. Do not forget to set mail.* registry variables.");
		emailReportSimpleRule.setIsExternal(true);
		emailReportSimpleRule.setIsUserCreated(false);
		emailReportSimpleRule.setTriggerWithoutData(false);
		emailReportSimpleRule.setEventDriven("ReportGeneratedEvent");
		emailReportSimpleRule.setCartridgeName("Core-ReportNotification");
		emailReportSimpleRule.setCartridgeVersion("5.7.5.5");
		HibernateHelper.persist(OperationVersionable.SAVE_FORCE_MAKE_CURRENT, sessionFactory, emailReportSimpleRule, null, false,
				Service.RULE);
		
		Rule freeDatabaseSpaceCheckingSeverityRule = new RuleSeverityFamily();
		freeDatabaseSpaceCheckingSeverityRule.setId("712ee1de-d002-4e7c-af94-d7cb715dc43c");
		freeDatabaseSpaceCheckingSeverityRule.setCreatedTimestamp(new Timestamp(new Date().getTime()));
		freeDatabaseSpaceCheckingSeverityRule.setName("Catalyst Free Database Space Checking");
		freeDatabaseSpaceCheckingSeverityRule.setComments("Rule to check whether the Oracle tablespaces or SQL Server database still have/has enough free space.");
		freeDatabaseSpaceCheckingSeverityRule.setIsExternal(true);
		freeDatabaseSpaceCheckingSeverityRule.setIsUserCreated(false);
		freeDatabaseSpaceCheckingSeverityRule.setTriggerWithoutData(true);
		freeDatabaseSpaceCheckingSeverityRule.setDataDriven();
		TopologyQuery domainQuery = new TopologyQuery("CatalystTablespace");
		freeDatabaseSpaceCheckingSeverityRule.setDomainQuery(domainQuery);
		freeDatabaseSpaceCheckingSeverityRule.setCartridgeName("Core-MonitoringPolicy");
		freeDatabaseSpaceCheckingSeverityRule.setCartridgeVersion("5.7.5.5");
		HibernateHelper.persist(OperationVersionable.SAVE_FORCE_MAKE_CURRENT, sessionFactory, freeDatabaseSpaceCheckingSeverityRule, null, true,
				Service.RULE);
	}

	@Test
	public void testPersistExistRule() {
		Rule rule = new SimpleRule("877e64db-3193-4ea5-8150-22ead239ee9d");
		rule.setName("test rule");
		HibernateHelper.persist(OperationVersionable.SAVE_FORCE_MAKE_CURRENT, sessionFactory, rule, null, false,
				Service.RULE);
	}
}
