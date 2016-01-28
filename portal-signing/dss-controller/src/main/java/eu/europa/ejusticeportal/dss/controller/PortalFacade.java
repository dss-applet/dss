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

import eu.europa.ec.markt.dss.validation102853.crl.CRLSource;
import eu.europa.ejusticeportal.dss.common.DssEvent;
import eu.europa.ejusticeportal.dss.common.SignedForm;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * An interface intended to separate the signing service from the rest of the
 * e-justice portal services.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public interface PortalFacade {

    /**
     * Get the user country. This is the country that shall be used to identify
     * the Member State signature policy.
     *
     * @param request the request
     * @return the country code, corresponding to a country code used in the MS
     *         policy data store
     */
    String getUserCountry(HttpServletRequest request);

    /**
     * Gets the PDF document that must be sealed and given to the user for
     * signature.
     *
     * @param request the request.
     * @return the PDF document.
     */
    byte[] getPDFDocument(HttpServletRequest request);

    /**
     * Gets the XML associated with the PDF document
     *
     * @param request the request
     * @return the XML
     */
    String getPDFDocumentXML(HttpServletRequest request);

    /**
     * Gets the name of the PDF document
     *
     * @param request the request
     * @return the PDF name
     */
    String getPDFDocumentName(HttpServletRequest request);

    /**
     * Gets the localised messages needed by the signing service
     *
     * @param request the request.
     * @param codes   the list of message codes.
     * @return the localised messages
     */
    Map<String, String> getLocalisedMessages(HttpServletRequest request, List<String> codes);

    /**
     * Stores the pdf document. The method tells the portal that the signing
     * service has signed the PDF. The portal should
     * take the necessary actions to continue the workflow
     *
     * @param request the request
     * @param signedForm the container of PDF, signature status, and possible detached signature
     */
    void storePDF(HttpServletRequest request, SignedForm signedForm );

    /**
     * Log the given event. This function will be used for
     * statistical purposes. (Which card failed, which unknown card has
     * successfully signed the form...). 
     * The implementation could add some information about the user, for example the country.
     * @param request the request
     * @param event the event that must be logged
     */
    void log(HttpServletRequest request, DssEvent event);

    /**
     * Get the XML containing the smart card profiles supported by the signing service.
     *
     * @return the XML
     */
    String getCardProfileXML();

    /**
     * Get the configuration of the document validation
     * @return the configuration
     */
    DocumentValidationConfig getDocumentValidationConfig();

    /**
     * Get a {@link CRLSource}
     * @return the {@link CRLSource}
     */
	CRLSource getCrlSource();
}
