package eu.europa.ejusticeportal.dss.warsigning;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilsTest {
    
    @Test
    public void testGetFileNameWithoutExtension(){
        String filename = "server.p12";
        assertEquals("server", Utils.getFileNameWithoutExtension(filename));
        
        filename = "server.p1";
        assertEquals("server", Utils.getFileNameWithoutExtension(filename));
        
        filename = "server.p";
        assertEquals("server", Utils.getFileNameWithoutExtension(filename));
        
        filename = "server.";
        assertEquals("server.", Utils.getFileNameWithoutExtension(filename));
        
        filename = "server";
        assertEquals("server", Utils.getFileNameWithoutExtension(filename));

        filename = "my.server.p12";
        assertEquals("my.server", Utils.getFileNameWithoutExtension(filename));
        
    }

}
