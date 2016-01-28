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
package eu.europa.ejusticeportal.dss.applet.model.token;

import static org.junit.Assert.*;
import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.EncryptionAlgorithm;
import eu.europa.ec.markt.dss.SignatureAlgorithm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * 
 * Test the PKCS12SignatureAlgorithm
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1697 $ - $Date: 2014-04-23 20:22:00 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith (JUnit4.class)
public class PKCS12SignatureAlgorithmTest {

    /**
     * Test the class gets the expected algorithm.
     */
    @Test
    public void test1(){
        DigestAlgorithm da = PKCS12SignatureAlgorithm.getInstance().getDigestAlgorithm(EncryptionAlgorithm.RSA);
        assertEquals(DigestAlgorithm.SHA512,da);
        da = PKCS12SignatureAlgorithm.getInstance().getDigestAlgorithm(EncryptionAlgorithm.ECDSA);
        assertEquals(DigestAlgorithm.SHA512,da);
        da = PKCS12SignatureAlgorithm.getInstance().getDigestAlgorithm(EncryptionAlgorithm.DSA);
        assertEquals(DigestAlgorithm.SHA1,da);
    }
}
