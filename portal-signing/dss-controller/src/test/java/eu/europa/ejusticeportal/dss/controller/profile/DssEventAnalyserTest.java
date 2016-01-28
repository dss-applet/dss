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
package eu.europa.ejusticeportal.dss.controller.profile;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import eu.europa.ec.markt.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.AppletSigningMethod;
import eu.europa.ejusticeportal.dss.common.Arch;
import eu.europa.ejusticeportal.dss.common.OS;
import eu.europa.ejusticeportal.dss.common.SignatureEvent;

/**
 * 
 * Test the LogEntryTransformer
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1704 $ - $Date: 2014-04-25 18:44:40 +0200 (Fri, 25 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith(JUnit4.class)
public class DssEventAnalyserTest {

    /**
     * Positive test for the transform - enough information for a new profile
     */
    @Test
    public void testTransformYes(){
        
        DssEventAnalyser transformer = new DssEventAnalyser();
        SignatureEvent log = createLogEntry();
        assertTrue(DssEventAnalyser.newProfilePossible(log));
        String s = transformer.transform(log);
        assertFalse(s.isEmpty());
    }
    
    /**
     * Test the transform for PKCS11
     */
    @Test
    public void testTransformYesPkcs11(){
        
        DssEventAnalyser transformer = new DssEventAnalyser();
        SignatureEvent log = createLogEntry();
        log.setUserSuppliedPkcs11Path("/tmp/pkcs11.so");
        log.setApi(SignatureTokenType.PKCS11.name());
        assertTrue(DssEventAnalyser.newProfilePossible(log));
        String s = transformer.transform(log);
        assertFalse(s.isEmpty());   
    }
            
    /**
     * Test the transform when there is no PKCS11 driver
     */
    @Test
    public void testTransformNoPkcs11(){
                
        SignatureEvent log = createLogEntry();
        log.setUserSuppliedPkcs11Path(null);
        log.setApi(SignatureTokenType.PKCS11.name());
        assertFalse(DssEventAnalyser.newProfilePossible(log));
    }
    
    /**
     * Test the transform when there is no ATR
     */
    @Test
    public void testTransformNoAtr(){
                
        SignatureEvent log = createLogEntry();
        log.setAtr(null);
        assertFalse(DssEventAnalyser.newProfilePossible(log));
    }
    /**
     * Test the transform when there is no digest algo
     */
    @Test 
    public void testTransformNoAlgo1(){
        SignatureEvent log = createLogEntry();
        log.setDigestAlgorithm(null);
        assertFalse(DssEventAnalyser.newProfilePossible(log));        
    }
    /**
     * Test the transform when there is no signature algo
     */
    @Test 
    public void testTransformNoAlgo2(){
        SignatureEvent log = createLogEntry();
        log.setSignatureAlgorithm(null);
        assertFalse(DssEventAnalyser.newProfilePossible(log));        
    }
    /**
     * Test the transform when there is no OS
     */
    @Test 
    public void testTransformNoOs(){
        SignatureEvent log = createLogEntry();
        log.setOs(null);
        assertFalse(DssEventAnalyser.newProfilePossible(log));        
    }
    /**
     * Test the transform when there is no arch
     */
    @Test 
    public void testTransformNoArch(){
        SignatureEvent log = createLogEntry();
        log.setArch(null);        
        assertFalse(DssEventAnalyser.newProfilePossible(log));        
    }
    /**
     * Test when user input not needed => no new profile
     */
    @Test
    public void testNoUserInput(){
        SignatureEvent log = createLogEntry();
        log.setNeedsUserInput(false);
        assertFalse(DssEventAnalyser.newProfilePossible(log));
    }
    /**
     * Test when not signed
     */
    @Test
    public void testNotSigned(){
        SignatureEvent log = createLogEntry();
        log.setSigned(false);
        assertFalse(DssEventAnalyser.newProfilePossible(log));
    }
    /**
     * Create a log entry with enough information to make a new profile
     * @return the log entry
     */
    private SignatureEvent createLogEntry(){
        SignatureEvent log = new SignatureEvent();
        log.setDigestAlgorithm("SHA1");
        log.setSignatureAlgorithm("RSA");
        log.setSigned(true);
        log.setSigningMethod(AppletSigningMethod.sc.name());
        log.setUserSuppliedCardIssuer("National Standards Institution of Luxembourg");
        log.setArch(Arch.B32.getName());
        log.setOs(OS.WINDOWS.getName());
        log.setJreVersion(7.1);
        log.setAtr("AA BB CC");
        log.setApi(SignatureTokenType.MSCAPI.name());
        log.setNeedsUserInput(true);
        return log;
        
    }
}
