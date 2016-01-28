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
import eu.europa.ejusticeportal.dss.controller.PortalFacade;

/**
 * Stores the SignedPDF, that is, pass it to the next step in the workflow.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 1704 $ - $Date: 2012-12-12 18:01:16 +0100 (mer., 12 déc.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class StoreSignedPdf {

    /**
     * Store the PDF in the portal
     *
     * @param portal  the portal
     * @param request the request, to get the session where pdf is temporarily
     *                stored; error also set in session attribute
     * @return true if stored.
     */
    public boolean storePdf(final PortalFacade portal, final HttpServletRequest request) {
        boolean stored = false;
        final SignedForm form = PdfTempStore.getSignedForm(request);
        if (form != null) {
            portal.storePDF(request, form);
            PdfTempStore.deleteSignedForm(request);
            stored = true;
        }
        return stored;
    }
}
