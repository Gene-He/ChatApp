package edu.rice.comp504.model;


import junit.framework.Assert;
import junit.framework.TestCase;
import org.eclipse.jetty.websocket.api.Session;


import static org.mockito.Mockito.mock;

public class DispatcherAdapterTest extends TestCase {

    private DispatcherAdapter dis = new DispatcherAdapter();

    /**
     * Test new session
     */
    public void testNewSession() {
        Session session = mock(Session.class);
        dis.newSession(session);
        Assert.assertNotNull("new session not null", session);
        Assert.assertTrue("id is valid larger than 0", dis.containsSession(session));
    }

    /**
     * Test add a new user from session
     */
    public void testGetUserIdFromSession()
    {
        Session session = mock(Session.class);
        dis.newSession(session);
        // check the userId not null
        int id = dis.getUserIdFromSession(session);
        Assert.assertTrue("id is valid larger than 0", id>=0);
    }

    /**
     * Determine whether the session exists.
     */
    public void testContainSession()
    {
        // check contains
        Session session = mock(Session.class);
        dis.newSession(session);
        Assert.assertTrue("id is valid larger than 0", dis.containsSession(session));

    }


}
