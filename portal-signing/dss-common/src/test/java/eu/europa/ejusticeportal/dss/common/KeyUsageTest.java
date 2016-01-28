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
/*
 * Project: DG Justice - DSS
 * Contractor: ARHS-Developments.
 *
 * $HeadURL: http://forge.aris-lux.lan/svn/dgjustice-dss/tags/portal-v8.10.3_applet-v0.14.2-QTM3+4MaintenanceContract/portal-signing/dss-common/src/test/java/eu/europa/ejusticeportal/dss/common/KeyUsageTest.java $
 * $Revision: 1699 $
 * $Date: 2014-04-23 20:22:44 +0200 (Wed, 23 Apr 2014) $
 * $Author: MacFarPe $
 */
package eu.europa.ejusticeportal.dss.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
/**
 * Test KeyUsage
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1699 $ - $Date: 2014-04-23 20:22:44 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith (JUnit4.class)
public class KeyUsageTest {

    /**
     * Test null flags
     */
    @Test
    public void testNull(){
        boolean [] flags = null;
        List<KeyUsage> l = KeyUsage.decode(flags);
        assertNotNull(l);
        assertEquals(0, l.size());
    }
    /**
     * Test non-repudiation
     */
    @Test
    public void testNonRepudiation(){
        boolean flags [] = new boolean []{false, true, false};
        List<KeyUsage> l = KeyUsage.decode(flags);
        assertTrue(l.contains(KeyUsage.nonRepudiation));
    }
    
    /**
     * Test non-repudiation
     */
    @Test
    public void testDigitalSignatureNonRepudiation(){
        boolean flags [] = new boolean []{true, true, false};
        List<KeyUsage> l = KeyUsage.decode(flags);
        assertTrue(l.contains(KeyUsage.nonRepudiation));
        assertTrue(l.contains(KeyUsage.digitalSignature));
    }
    
    /**
     * Test error condition (flags array is larger than the number of flags)
     */
    @Test 
    public void testErrorCondition(){
        boolean flags [] = new boolean []{true, true, false, true, true, true, true, true, true, true, true};
        List<KeyUsage> l = KeyUsage.decode(flags);
        assertTrue(l.contains(KeyUsage.digitalSignature));
    }
}
