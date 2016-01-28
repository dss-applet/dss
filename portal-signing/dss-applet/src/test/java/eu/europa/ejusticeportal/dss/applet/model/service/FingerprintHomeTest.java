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

import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test of class FingerprintHome
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class FingerprintHomeTest {

    /**
     * Test of getInstance method, of class FingerprintHome.
     */
    @Test
    public void testFingerprintHome() {
        FingerprintHome instance = FingerprintHome.getInstance();
        instance.init("testNavPlatform", "testUserAgent");
        try {
            instance.refresh();
        } catch (CodeException ex) {
            assertEquals(ex.getCode(), MessagesEnum.dss_applet_message_card_detection_unavailable.name());
        }
    }
}
