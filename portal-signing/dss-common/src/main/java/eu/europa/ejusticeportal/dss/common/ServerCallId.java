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
 * Enumeration of ids used by the applet make a call to the e-Justice portal server.
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1699 $ - $Date: 2014-04-23 20:22:44 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public enum ServerCallId implements Serializable {

    /** Get the signing context */
    getSigningContext("Get the signing context", false),
    /** Get the sealed PDF document */
    getSealedPdf("Get the sealed PDF", false),
    /** Get the message bundle */
    getMessageBundle("Get the translated messages and labels", false),
    /** Upload the signed doc and get the validation */
    uploadSignedPdf("Upload the externally signed PDF", false),
    /** Log the statistics */
    logStatistics("Log signature parameters", true),
    /** Upload the applet event log */
    uploadAppletLog("Upload applet event log", false);

    private String label;

    private boolean enclosed;

    /**
     * The constructor for ServerCallId.
     * 
     * @param label text for logging
     * @param enclosed true if the server call embedded within another server call
     */
    private ServerCallId(String label, boolean enclosed) {
        this.label = label;
    }

    /**
     * Get the label for logging
     * 
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the enclosed
     */
    public boolean isEnclosed() {
        return enclosed;
    }

}
