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
package eu.europa.ejusticeportal.dss.controller.action;

import javax.servlet.http.HttpServletRequest;

import eu.europa.ejusticeportal.dss.common.SignedForm;

/**
 * Temporary storage of the PDF between upload/validation and "permanent" store
 * Stores in the session 
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1704 $ - $Date: 2014-04-25 18:44:40 +0200 (Fri, 25 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class PdfTempStore {

    private static final String KEY_FILE = "dss_applet_pdf_temp_store_file";
   

    /**
     * 
     * The constructor for PdfTempStore.
     */
    private PdfTempStore(){
        
    }
    /**
     * Store the PDF
     * @param request the HttpServletRequest
     * @param form the SignedForm to store
     */
    public static void storeSignedForm(final HttpServletRequest request, SignedForm form) {
        request.getSession().setAttribute(KEY_FILE, form);
    }

    /**
     * Get the stored PDF from the session
     * @param request the HttpServletRequest
     * @return the stored PDF
     */
    public static SignedForm getSignedForm(final HttpServletRequest request) {
        return (SignedForm) request.getSession().getAttribute(KEY_FILE);
    }

    /**
     * Deletes the PDF and status from the session
     * @param request the HttpServletRequest
     */
    public static void deleteSignedForm(final HttpServletRequest request) {
        request.getSession().removeAttribute(KEY_FILE);
    }

}
