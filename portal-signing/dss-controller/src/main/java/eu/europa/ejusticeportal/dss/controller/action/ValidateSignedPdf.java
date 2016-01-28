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

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ejusticeportal.dss.common.SignatureStatus;
import eu.europa.ejusticeportal.dss.common.SignedForm;
import eu.europa.ejusticeportal.dss.common.SignedPDF;
import eu.europa.ejusticeportal.dss.controller.PortalFacade;
import eu.europa.ejusticeportal.dss.controller.exception.SigningBusinessException;
import eu.europa.ejusticeportal.dss.controller.exception.SigningException;

/**
 *
 * Receives the signed digest from the applet and joins it to the PDF.
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc. 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class ValidateSignedPdf extends SigningAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateSignedPdf.class);
    

    /**
     * The constructor for ValidateSignedPdf.
     */
    public ValidateSignedPdf() {
    }

    /**
     * Get the signature status
     * @param portal the PortalFacade
     * @param request the HttpServletRequest
     * @param o expected to be a SignedPDF
     * @return the SignatureStatus
     */
    @Override
    protected Object getResponseObject(final PortalFacade portal, final HttpServletRequest request, final Object o) {

        final SignedPDF sPdf = (SignedPDF) o;
        final byte[] signedPdf = Base64.decodeBase64(sPdf.getB64PDF());
        final SignatureStatus signatureStatus = new SignatureStatus();
        try {
            DocumentValidationService.getInstance().validatePdf(portal, request, signedPdf,null, signatureStatus);
            // Temporarily store the PDF for the next step in the process
            PdfTempStore.storeSignedForm(request, new SignedForm(signatureStatus, signedPdf));           
            return signatureStatus;
        } catch (SigningBusinessException e) {
            throw e;
        } catch (SigningException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error signing the PDF", e);
            throw new SigningException(e);
        }
    }
    
}
