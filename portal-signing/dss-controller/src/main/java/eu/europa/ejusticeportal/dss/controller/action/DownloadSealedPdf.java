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
import eu.europa.ejusticeportal.dss.controller.PortalFacade;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Downloads a sealed PDF (classic download to the browser).
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class DownloadSealedPdf extends GetSealedPdf {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadSealedPdf.class);

    /**
     *
     * The constructor for DownloadSealedPdf.
     *
     */
    public DownloadSealedPdf() {
    }

    /**
     * Downloads the PDF to the client.
     */
    @Override
    public void elaborate(final PortalFacade portal, final HttpServletRequest request,
            final HttpServletResponse response) {

        final String pdfName = portal.getPDFDocumentName(request);
        try {
            String disclaimer = portal.getLocalisedMessages(request,null).get(MessagesEnum.dss_applet_digest_disclaimer.name());
            ByteArrayInputStream is = new ByteArrayInputStream(SealedPDFService.getInstance().sealDocument(portal.getPDFDocument(request), portal.getPDFDocumentXML(request),disclaimer, portal.getDocumentValidationConfig().getSealMethod()));
            sendPdf(is, pdfName, response);
        } catch (Exception e) {
            LOGGER.error("Error downloading the sealed PDF", e);
        }
    }

    /**
     * Send the PDF to the http response
     * @param is the stream containing the PDF
     * @param pdfName the name of the PDF
     * @param response the HttpServletResponse to which the PDF will be written
     * @throws IOException
     */
    private void sendPdf(InputStream is, String pdfName, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + pdfName + "\"");

        ServletOutputStream outs = null;
        try {
            outs = response.getOutputStream();
            int r = 0;
            byte[] chunk = new byte[8192];
            while ((r = is.read(chunk)) != -1) {
                outs.write(chunk, 0, r);
            }
            outs.flush();
        } finally {
            IOUtils.closeQuietly(outs);
        }

    }
}
