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
 * $HeadURL: http://forge.aris-lux.lan/svn/dgjustice-dss/tags/portal-v8.10.3_applet-v0.14.2-QTM3+4MaintenanceContract/portal-signing/dss-common/src/main/java/eu/europa/ejusticeportal/dss/common/KeyUsage.java $
 * $Revision: 1699 $
 * $Date: 2014-04-23 20:22:44 +0200 (Wed, 23 Apr 2014) $
 * $Author: MacFarPe $
 */
package eu.europa.ejusticeportal.dss.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the KeyUsage flags of an X509 certificate
 *  
 *     digitalSignature        (0),
 *     nonRepudiation          (1),
 *     keyEncipherment         (2),
 *     dataEncipherment        (3),
 *     keyAgreement            (4),
 *     keyCertSign             (5),
 *     cRLSign                 (6),
 *     encipherOnly            (7),
 *     decipherOnly            (8)
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1699 $ - $Date: 2014-04-23 20:22:44 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public enum KeyUsage implements Serializable{

    digitalSignature,
    nonRepudiation,
    keyEncipherment,
    dataEncipherment,
    keyAgreement,
    keyCertSign,
    cRLSign,
    encipherOnly,
    decipherOnly;

    
    /**
     * Decode the keyUsage flags
     * @param keyUsage
     * @return list of KeyUsage, never null.
     */
    public static List<KeyUsage> decode(boolean [] keyUsage){
        
        List<KeyUsage> ku = new ArrayList<KeyUsage>();
        
        if (keyUsage != null){
            for (int i=0;i<keyUsage.length && i< KeyUsage.values().length;i++){
                if (keyUsage[i]){
                    ku.add(KeyUsage.values()[i]);
                }
            }
        }
        return ku;
    }
    /**
     * Gets the key usage as String (space separated)
     * @param keyUsage the keyUsage flags
     * @return the string
     */
    public static String decodeToString(boolean[] keyUsage) {
        StringBuilder sb = new StringBuilder();
        for (KeyUsage ku:decode(keyUsage)){
            sb.append(ku.name()).append(" ");
        }
        return sb.toString();
    }
}
