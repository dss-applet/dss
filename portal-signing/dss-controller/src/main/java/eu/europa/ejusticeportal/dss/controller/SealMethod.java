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
package eu.europa.ejusticeportal.dss.controller;

import eu.europa.ejusticeportal.dss.controller.exception.DssInitialisationException;

/**
 * 
 * Represents a seal method to be used.
 * 
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1704 $ - $Date: 2014-04-25 18:44:40 +0200 (Fri, 25 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public enum SealMethod {
    /**
     * The PADES seal method.
     */
    SEAL("S"),
    /**
     * The custom seal method.
     */
    SEAL_CUSTOM("C"),
    /**
     * The no method.
     */
    NO_SEAL("N"),
    ;
    private String code;

    SealMethod(String c) {
        code = c;
    }

    /**
     * Get the code for the sealMethod
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Get the seal method from a code
     * @param c the code
     * @return the SealMethod
     */
    public static SealMethod fromCode(String c) {
        for (SealMethod vl : SealMethod.values()) {
            if (vl.getCode().equals(c)) {
                return vl;
            }
        }
        throw new DssInitialisationException("Seal Method for code "+c +" is not defined!");
    }

}
