package eu.europa.ejusticeportal;

import static org.junit.Assert.assertEquals;

import eu.europa.ejusticeportal.dss.demo.web.util.StringUtils;

import org.junit.Test;

/**
 * Test the StringUtils
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class StringUtilsTest{

    /**
     * Test of the exec method for AbstractPrivilegedExceptionAction
     */
    @Test
    public void testTruncateMessage() {
        String str = "abcdef";
        
        assertEquals("abc", StringUtils.truncateMessageInCharacters(str, 3));
        
        assertEquals("abcd", StringUtils.truncateMessageInCharacters(str, 4));
        
        assertEquals("abcde", StringUtils.truncateMessageInCharacters(str, 5));
        
        assertEquals("abcdef", StringUtils.truncateMessageInCharacters(str, 6));
        
        assertEquals("abcdef", StringUtils.truncateMessageInCharacters(str, 7));
        
        assertEquals("abcdef", StringUtils.truncateMessageInCharacters(str, 50000));
        
        str="";
        assertEquals("", StringUtils.truncateMessageInCharacters(str, 50000));
        
        str=null;
        assertEquals(null, str);
    }
    
}
