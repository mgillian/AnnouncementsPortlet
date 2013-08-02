package org.jasig.portlet.announcements;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import java.util.List;
import javax.annotation.Resource;
import org.jasig.portlet.announcements.model.Topic;
import org.jasig.portlet.announcements.service.IAnnouncementService;
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
		
        Importer importer = new Importer(dataDirectory, sessionFactory, announcementService);
        importer.importData();
        
        List<Topic> updatedTopics = announcementService.getAllTopics();
        assertTrue("topic list should have 2 times, instead had "+ updatedTopics.size(), updatedTopics.size() == 2);
        
        // verify data after import
        Topic addedTopic = updatedTopics.get(1);
        assert addedTopic.getTitle().equals("Academic Notices");
	}
}
