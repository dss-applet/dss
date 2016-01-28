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

import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.SealedPDF;
import eu.europa.ejusticeportal.dss.controller.PortalFacade;
import eu.europa.ejusticeportal.dss.controller.exception.SigningException;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * Creates the PDF file, embeds the XML, signs with the server certificate, stores PDF in session, returns Digest to the
 * applet.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc. 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class GetSealedPdf extends SigningAction {


    /**
     * 
     * The constructor for GetSealedPdf.
     */
    public GetSealedPdf() {

    }

    /**
     * Gets the sealed PDF document.
     * 
     * @param portal the PortalFacade
     * @param request the HttpServletRequest
     * @param o not used - required for the interface
     */
    @Override
    protected Object getResponseObject(final PortalFacade portal, final HttpServletRequest request, final Object o) {

        try {
            String disclaimer = portal.getLocalisedMessages(request,null).get(MessagesEnum.dss_applet_digest_disclaimer.name());
            final byte[] doc = SealedPDFService.getInstance().sealDocument(portal.getPDFDocument(request), portal.getPDFDocumentXML(request),disclaimer, portal.getDocumentValidationConfig().getSealMethod());
            SealedPDF pdf = new SealedPDF();
            pdf.setFileName(portal.getPDFDocumentName(request));
            pdf.setPdfBase64(Base64.encodeBase64String(doc));
            pdf.setSignDate(new Date());
            return pdf;
        } catch (Exception e) {
            throw new SigningException(e);
        }

    }

}
