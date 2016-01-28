/*******************************************************************************
 * Digital Signature Applet
 * 
 *  Copyright (C) 2014 European Commission, Directorate-General for Justice (DG  JUSTICE), B-1049 Bruxelles/Brussel
 * 
 *  Developed by: ARHS Developments S.A. (rue Nicolas Bové 2B, L-1253 Luxembourg)  
 * 
 *  http://www.arhs-developments.com
 * 
 *  This file is part of the "Digital Signature Applet" project.
 * 
 *  Licensed under the EUPL, version 1.1 or – as soon they are approved by the European  Commission - subsequent versions of the EUPL (the "Licence"). 
 *  You may not use this  work except in compliance with the Licence. You may obtain a copy of the Licence at:
 * 
 *  http://ec.europa.eu/idabc/eupl.html
 * 
 *  Unless required by applicable law or agreed to in writing, software distributed under   the Licence is distributed on  
 *  an "AS IS" basis, WITHOUT WARRANTIES OR   CONDITIONS OF ANY KIND, either  express or implied. 
 * 
 *  See the Licence for the  specific language governing permissions and limitations under the Licence.
 ******************************************************************************/
package eu.europa.ejusticeportal.dss.common;

import static org.junit.Assert.assertEquals;

import eu.europa.ejusticeportal.dss.common.OS;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * 
 * Test the OS class
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1699 $ - $Date: 2014-04-23 20:22:44 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith(Parameterized.class)
public class OSTest {

    private String userAgent;
    private String expectedOS;
    private String systemProperty;

    /**
     * 
     * The constructor for OSTest.
     * @param sp the system property used to get an OS
     * @param ua the user agent used to get an OS
     * @param eo the expected OS to get
     */
    public OSTest(String sp, String ua, String eo) {
        systemProperty = sp;
        userAgent = ua;
        expectedOS = eo;
    }

    /**
     * The parameters for the test
     * @return the parameters
     */
    @Parameters
    public static List<Object[]> getParameters() {

        Object o[][] = new Object[][] {
                {
                        "MacOS X",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/537.13+ (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
                        OS.MACOS.getName() },
               {          null,
                            null,
                            OS.UNSUPPORTED.getName() },
                { "MacOS X", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:9.0) Gecko/20100101 Firefox/9.0",
                        OS.MACOS.getName() },
                { "Windows XP", "Mozilla/6.0 (Windows NT 6.2; WOW64; rv:16.0.1) Gecko/20121011 Firefox/16.0.1",                            
                        OS.WINDOWS.getName() },
                { "Windows 95", "Mozilla/6.0 (Windows 95 6.2; WOW64; rv:16.0.1) Gecko/20121011 Firefox/16.0.1",
                            OS.UNSUPPORTED.getName() },
                { "Linux", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:14.0) Gecko/20100101 Firefox/14.0.1",
                        OS.LINUX.getName() },
                {
                        "iPad",
                        "Mozilla/5.0 (iPad; CPU OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3",
                        OS.UNSUPPORTED.getName() } };
        return Arrays.asList(o);
    }

    /**
     * Test getting the OS from the user agent
     */
    @Test
    public void testFromUserAgent() {

        assertEquals("Error for user agent "+userAgent,expectedOS,OS.getFromUserAgent(userAgent).getName() );        
    }

    /**
     * Test getting the OS from the os.name system property
     */
    @Test
    public void testFromSystemProperty() {
        if (systemProperty ==null){
            return;
        }
        if (systemProperty.equals("iPad")) {
            assertEquals(OS.getFromSystemProperty(systemProperty), null);
        } else {
            assertEquals("Error for system property "+systemProperty, expectedOS,OS.getFromSystemProperty(systemProperty).getName());
        }
    }

}
