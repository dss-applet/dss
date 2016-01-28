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
package eu.europa.ejusticeportal.dss.applet.controller.cb;
import org.apache.commons.codec.binary.Base64;

import eu.europa.ejusticeportal.dss.applet.common.UIControllerHome;
import eu.europa.ejusticeportal.dss.applet.model.service.AppletInitSemaphore;
import eu.europa.ejusticeportal.dss.applet.model.service.PDFHome;
import eu.europa.ejusticeportal.dss.applet.view.UIFunction;
import eu.europa.ejusticeportal.dss.common.SealedPDF;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;

/**
 * 
 * Handle the callback of the sealed PDF - Save the Sealed Pdf for further processing.
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1697 $ - $Date: 2014-04-23 20:22:00 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class GetSealedPdfCbHandler extends AbstractCbHandler {
    
    
    /**
     * Handle the callback
     * @param the callback XML
     */
    public void doHandle(String xml) throws CodeException {
        PDFHome pdfHome = PDFHome.getInstance();
        synchronized (pdfHome) {
            SealedPDF sealedPdf = (SealedPDF) fromString(xml);
            pdfHome.setSealedPdf(Base64.decodeBase64(sealedPdf.getPdfBase64()), sealedPdf.getSignDate());
            pdfHome.setPdfName(sealedPdf.getFileName());
            pdfHome.notifyAll();
        }
        AppletInitSemaphore.getInstance().setSealedPdfReady(true);
        UIControllerHome.getInstance().getUiController().eval(UIFunction.showOpenSealedPdfPrompt);
    }
    /**
     * Get the label for the handler
     */
    public String getLabel(){
        return "Sealed PDF";
    }
    
}
