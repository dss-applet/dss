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
package eu.europa.ejusticeportal.dss.applet.model.service;

import eu.europa.ejusticeportal.dss.common.Fingerprint;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * Test the EnvironmentInitAction
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class EnvironmentInitActionTest {

    /**
     * Test of doExec method, of class EnvironmentInitAction.
     */
    @Test
    public void testDoExec() {

        final Properties mock = Mockito.mock(Properties.class);
        Mockito.when(mock.getProperty("os.arch")).thenReturn("32 bits");
        Mockito.when(mock.getProperty("os.name")).thenReturn("Windows");
        Mockito.when(mock.getProperty("os.version")).thenReturn("7");
        Mockito.when(mock.getProperty("java.vendor")).thenReturn("Oracle");
        Mockito.when(mock.getProperty("java.version")).thenReturn("1.7_07");

        Fingerprint fp = new Fingerprint();
        EnvironmentInitAction instance = new EnvironmentInitAction(fp) {
            @Override
            protected Properties getProperties() {
                return mock;
            }
        };
        instance.doExec();

        Assert.assertEquals("32 bits", fp.getArch());
        Assert.assertEquals("windows", fp.getOs());
        Assert.assertEquals("7", fp.getOsVersion());
        Assert.assertEquals("Oracle", fp.getJreVendor());
        Assert.assertEquals("1.7_07", fp.getJreVersion());
    }
}
