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

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import eu.europa.ejusticeportal.dss.common.Fingerprint;
import eu.europa.ejusticeportal.dss.common.SigningContext;
import eu.europa.ejusticeportal.dss.controller.PortalFacade;

/**
 * A repository of smart card profiles
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public interface CardProfileRepository {

    /**
     * Gets the card profiles from the repository based on the information on
     * the fingerprint,
     * and sets the information in the signing context
     * @param request the request
     * @param portal the PortalFacade (for logging)
     * @param fp the Fingerprint
     * @param sc the SigningContext, in which the card profiles informations
     *           will be set
     * @throws NoSuchAlgorithmException 
     */
    void findCardProfiles(HttpServletRequest request, PortalFacade portal, Fingerprint fp, SigningContext sc) throws NoSuchAlgorithmException;
    
    
}
