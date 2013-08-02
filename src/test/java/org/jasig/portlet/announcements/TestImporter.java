package org.jasig.portlet.announcements;

//import java.awt.List;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.jasig.portlet.announcements.model.Topic;
import org.jasig.portlet.announcements.model.TopicSubscription;
import org.jasig.portlet.announcements.service.IAnnouncementService;
import org.jasig.portlet.announcements.spring.PortletApplicationContextLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import junit.framework.TestCase;

/**
 * @author eolsson
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:context/importExportContext.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@Transactional
public class TestImporter extends TestCase {
    private static final String SESSION_FACTORY_BEAN_NAME = "sessionFactory";
    private static final String ANNOUNCEMENT_SVC_BEAN_NAME = "announcementService";
    private static final Logger log = Logger.getLogger(TestImporter.class);

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		
	}

    @Resource
    SessionFactory sessionFactory;
    
    @Resource
    IAnnouncementService announcementService;
        
    @Test
	public void testImporter() {
		String basedir = System.getProperty("basedir");
		String dataDirectory = basedir + "/src/main/data";
//		String contextPath = basedir + "/src/main/resources/context/importExportContext.xml";
//        ApplicationContext context = PortletApplicationContextLocator.getApplicationContext(contextPath);
        
//        assert context != null;
        
//        SessionFactory sessionFactory = context.getBean(SESSION_FACTORY_BEAN_NAME, SessionFactory.class);
//        IAnnouncementService announcementService = context.getBean(ANNOUNCEMENT_SVC_BEAN_NAME,IAnnouncementService.class);
        
//        // verify data before import
//        Session session = sessionFactory.getCurrentSession();
//        Transaction transaction = session.beginTransaction();
//        Query query = session.createQuery("from Announcement ");
//        List list = query.list();
//        transaction.commit();
//        assert list.size() == 0;
        
//		List<Topic> topics = announcementService.getAllTopics();
//		assertTrue("topic list should have 1 item, instead had " + topics.size(), topics.size() == 1);		
		
        Importer importer = new Importer(dataDirectory, sessionFactory, announcementService);
        importer.importData();
//        Session session = sessionFactory.getCurrentSession();
//        session.flush();
	}
    
    @AfterTransaction
    public void afterTransaction() {
        List<Topic> updatedTopics = announcementService.getAllTopics();
        assertTrue("topic list should have 2 times, instead had "+ updatedTopics.size(), updatedTopics.size() == 2);
//        System.out.println("updatedTopics.size() == " + updatedTopics.size());
//        assert updatedTopics.size() == 2;
        
        // verify data after import
        Topic addedTopic = updatedTopics.get(1);
        assert addedTopic.getTitle().equals("Academic Notices");        
    	
    }
    
}