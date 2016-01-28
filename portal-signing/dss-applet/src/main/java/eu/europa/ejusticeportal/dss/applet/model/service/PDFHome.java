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
package eu.europa.ejusticeportal.dss.applet.model.service;

import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;


import java.util.Date;

/**
 * Manage the PDFs objects.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class PDFHome {

    private static final DssLogger LOG = DssLogger.getLogger(PDFHome.class.getSimpleName());
    private static PDFHome instance;

    private PDFHome() {
    }

    /**
     * Get the single instance of PDFHome
     * 
     * @return the instance
     */
    public static PDFHome getInstance() {
        if (null == instance) {
            instance = new PDFHome();
        }
        return instance;
    }

    /**
     * PDF signed with the server certificate.
     */
    private byte[] sealedPdf;
    /**
     * PDF signed by both the server and the user.
     */
    private byte[] signedPdf;
    /**
     * Name of the Sealed Pdf
     */
    private String pdfName;

    /**
     * The date the sealed PDF was signed
     */
    private Date signDate;
    /**
     * Get the sealed Pdf. If the sealedPdf has not been received by the server yet, the Thread will wait for a certain
     * TimeOut.
     * 
     * @return sealedPdf
     */
    public byte[] getSealedPdf() {
        if (sealedPdf == null) {
            ExceptionUtils.exception(new UnexpectedException("sealedPdf not available for now"), LOG);
        }
        return sealedPdf;

    }

    /**
     * Gets the date of the signature
     * 
     * @return the Date
     * @throws IOException for error reading the PDF
     */
    public Date getSealedPdfSignDate() {
        return signDate;
    }

    /**
     * @return the sealed Pdf has been received
     */
    public boolean sealedPdfReceived() {
        return sealedPdf != null;
    }

    /**
     * Set the sealed Pdf
     * 
     * @param sealedPdf the sealedPdf to set
     * @param signDate the sign date
     */
    public void setSealedPdf(byte[] sealedPdf, Date signDate) {
        this.sealedPdf = sealedPdf == null ? null : sealedPdf.clone();
        this.signDate = signDate;
    }

    /**
     * Get the signed Pdf.
     * 
     * @return the signedPdf
     */
    public byte[] getSignedPdf() {
        return signedPdf;
    }

    /**
     * Set the signed Pdf.
     * 
     * @param signedPdf the signedPdf to set
     */
    public void setSignedPdf(byte[] signedPdf) {
        this.signedPdf = signedPdf == null ? null : signedPdf.clone();
    }

    /**
     * Get the Sealed Pdf name given by the server.
     * 
     * @return the name
     */
    public String getPdfName() {
        return pdfName;
    }

    /**
     * Set the Sealed Pdf name.
     * 
     * @param pdfName the pdfName to set
     */
    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }
}
