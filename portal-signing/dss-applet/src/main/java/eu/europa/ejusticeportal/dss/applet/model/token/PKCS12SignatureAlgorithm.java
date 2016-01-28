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

import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.EncryptionAlgorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * A class to manage PKCS12 signature algorithms
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1697 $ - $Date: 2014-04-23 20:22:00 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class PKCS12SignatureAlgorithm {

    private static final Map<EncryptionAlgorithm, DigestAlgorithm> ALGOS = new HashMap<EncryptionAlgorithm, DigestAlgorithm>();
    private static final PKCS12SignatureAlgorithm INSTANCE = new PKCS12SignatureAlgorithm();

    static {
        ALGOS.put(EncryptionAlgorithm.RSA, DigestAlgorithm.SHA512);
        ALGOS.put(EncryptionAlgorithm.ECDSA, DigestAlgorithm.SHA512);
        ALGOS.put(EncryptionAlgorithm.DSA, DigestAlgorithm.SHA1);
    }
    /**
     * 
     * The default constructor for PKCS12SignatureAlgorithm.
     */
    private PKCS12SignatureAlgorithm() {
    }

    /**
     * Get the instance
     * @return the instance
     */
    public static PKCS12SignatureAlgorithm getInstance() {
        return INSTANCE;
    }

    /**
     * Gets the strongest DigestAlgorithm that will work with the given SignatureAlgorithm.
     * This is useful for PKCS12 where we are not limited by capabilities of a smartcard.
     * @param sa the SignatureAlgorithm
     * @return the strongest DigestAlgorithm we can use with the given SignatureAlgorithm.
     */
    public DigestAlgorithm getDigestAlgorithm(EncryptionAlgorithm sa){
        return ALGOS.get(sa);
    }
}
