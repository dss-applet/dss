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
package eu.europa.ejusticeportal.dss.controller.signature;

import eu.europa.ejusticeportal.dss.controller.DocumentValidationConfig;
import eu.europa.ejusticeportal.dss.controller.HttpProxyConfig;
import eu.europa.ejusticeportal.dss.controller.ValidationLevel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;



@RunWith(JUnit4.class)
public class RefreshingTrustedListCertificateSourceTest {

    /**
     * Tests the source
     * @throws Exception 
     */
    @Test
    public void testSource() throws Exception {
    	HttpProxyConfig hc = Mockito.mock(HttpProxyConfig.class);
        Mockito.when(hc.isHttpEnabled()).thenReturn(Boolean.FALSE);
        Mockito.when(hc.isHttpsEnabled()).thenReturn(Boolean.FALSE);
        DocumentValidationConfig config = Mockito.mock(DocumentValidationConfig.class);
        Mockito.when(config.getOriginValidationLevel()).thenReturn(ValidationLevel.DISABLED);
        Mockito.when(config.getTamperedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getRevokedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getSignedValidationLevel()).thenReturn(ValidationLevel.EXCEPTION);
        Mockito.when(config.getTrustedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getWorkflowValidationLevel()).thenReturn(ValidationLevel.DISABLED);
        Mockito.when(config.getLotlUrl()).thenReturn("https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl-mp.xml");
        Mockito.when(config.getRefreshPeriod()).thenReturn(3600);
        RefreshingTrustedListsCertificateSource.init(hc, config);
    	final RefreshingTrustedListsCertificateSource s1 = RefreshingTrustedListsCertificateSource.getInstance();
        s1.refresh();

        Thread t1 = new Thread() {
            public void run() {
                s1.refresh();
            }
        };
        t1.setDaemon(true);
        t1.start();
        //TODO fix this test
//        assertTrue(t1.isAlive());
//        assertFalse(s1.getCertificates().isEmpty());
//
//        CertificateAndContext c1 = s1.getCertificateList().get(0);
//        CertificateAndContext c2 = s1.getCertificateList().get(0);
//        assertFalse(s1.getDiagnosticInfo().isEmpty());
//        assertTrue(c1==c2);
//        while (t1.isAlive()) {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                throw new Exception("The sleep was interrupted");
//            }
//        }
//        
//        c2 = s1.getCertificateList().get(0);
        //assertFalse(c1==c2);
    }
}
