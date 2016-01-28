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

/**
 *
 * The class wraps the PDF signed by the applet
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision: 350 $ - $Date: 2012-11-30 12:14:37 +0100 (ven., 30 nov.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class SignedPDF implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
     *
     * The default constructor for SignedPDF.
     */
    public SignedPDF() {
    }

    /**
     *
     * The construct a SignedPDF from Base 64 string.
     *
     * @param b64pdf the base 64 string
     */
    public SignedPDF(String b64pdf) {
        super();
        b64PDF = b64pdf;
    }

    private String b64PDF;

    /**
     * @return the b64PDF
     */
    public String getB64PDF() {
        return b64PDF;
    }

    /**
     * @param b64pdf the b64PDF to set
     */
    public void setB64PDF(String b64pdf) {
        b64PDF = b64pdf;
    }
}
