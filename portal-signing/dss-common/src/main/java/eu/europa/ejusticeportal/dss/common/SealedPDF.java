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
package eu.europa.ejusticeportal.dss.common;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * The class represents the PDF document that will be signed by tha applet.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 262 $ - $Date: 2012-11-15 17:27:42 +0100 (jeu., 15 nov.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class SealedPDF implements Serializable {


	private static final long serialVersionUID = 1L;
	public String fileName;
	public String pdfBase64;
	public Date signDate;

    public SealedPDF () {
        
    }
    
   /**
    * Get the date/time the sealed PDF was signed
    * @return the date/time
    */
    public Date getSignDate() {
        return signDate;
    }

    /**
     * Set the date/time the sealed PDF was signed
     * @param signDate the date/time
     */
    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    /**
     * Get the name of the file corresponding to the PDF.
     *
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Set the name of the file
     *
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Get the PDF as base64 string
     *
     * @return the pdfBase64
     */
    public String getPdfBase64() {
        return pdfBase64;
    }

    /**
     * Set the PDF as base64 string.
     *
     * @param pdfBase64 the pdfBase64 to set
     */
    public void setPdfBase64(String pdfBase64) {
        this.pdfBase64 = pdfBase64;
    }
}
