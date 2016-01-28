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

import static org.junit.Assert.*;


import eu.europa.ec.markt.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.DssEvent.DssEventType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * 
 * Tests for the LogEntry
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1699 $ - $Date: 2014-04-23 20:22:44 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith(JUnit4.class)
public class DssEventTest {


    /**
     * Test the repository access log
     */
    @Test
    public void testRepositoryAccesslog(){
        Fingerprint fp = new Fingerprint();
        fp.setUserAgent("007");
        SigningContextEvent log = new SigningContextEvent("windows", "32", 7.1, fp);
        log.setAtrFound(true);
        log.setAtr("AA BB CC");
        String s = Utils.toString(log);
       
        SigningContextEvent log2 = (SigningContextEvent) Utils.fromString(s);
        
        assertEquals(log.getEventDate(),log2.getEventDate());
        assertEquals("32", log2.getArch());
        assertEquals("AA BB CC", log2.getAtr());
        assertEquals(DssEventType.SC, log2.getLogEntryType());
        assertEquals("007",log2.getFingerprint().getUserAgent());
    }
    /**
     * Test the signature log
     */
    @Test
    public void testSignatureLog(){
        
        SignatureEvent log = new SignatureEvent("windows", "64", 7.123);
        log.setDigestAlgorithm("SHA1");
        log.setSignatureAlgorithm("RSA");
        log.setNeedsUserInput(true);
        log.setSigningMethod(AppletSigningMethod.sc.name());
        log.setSigned(true);
        log.setKeyUsage(KeyUsage.decodeToString(new boolean []{true,true}));
        log.setUserSuppliedCardIssuer("Me");
        log.setUserSuppliedPkcs11Path("c:/tmp/cardos.dll");
        

        log.setApi(SignatureTokenType.PKCS11.name());
        log.setAtr("AA BB CC DD");

        String s = Utils.toString(log);
        
        SignatureEvent log2 = (SignatureEvent) Utils.fromString(s);
        
        assertEquals("64", log2.getArch());
        assertEquals(DssEventType.SG, log2.getLogEntryType());
        
        assertEquals(SignatureTokenType.PKCS11.name(), log2.getApi());
        assertTrue(log2.getKeyUsage().lastIndexOf(KeyUsage.digitalSignature.name())!=-1);
    }
}
