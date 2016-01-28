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
package eu.europa.ejusticeportal.dss.applet.controller.ui;

import eu.europa.ejusticeportal.dss.applet.event.LoadingRefreshed;
import eu.europa.ejusticeportal.dss.applet.event.SessionExpired;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.action.SavePdfAction;
import eu.europa.ejusticeportal.dss.applet.model.service.FileChooserHome;
import eu.europa.ejusticeportal.dss.applet.model.service.PDFHome;
import eu.europa.ejusticeportal.dss.applet.view.UIFunction;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;

import java.io.File;

/**
 * 
 * Handles the event that requires the sealed PDF to be saved.
 *
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1697 $ - $Date: 2014-04-23 20:22:00 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class SaveSealedPdfHandler implements UIEventHandlerDelegate {

    /**
     * Handle the event
     * 
     * @param e the event to handle
     * @param arg the argument of the event
     */
    public void doHandle(UIEvent e, String arg) throws CodeException {
        File selectedFile = FileChooserHome.getInstance().getFileChooser().choosePdfFileSave(
                PDFHome.getInstance().getPdfName());
        if (selectedFile != null) {
            new SavePdfAction(PDFHome.getInstance().getSealedPdf(), selectedFile).exec();
        }
        Event.getInstance().fire(new LoadingRefreshed(false, false));
        if (UIFunction.redirectSessionExpired.name().equals(arg)) {
            Event.getInstance().fire(new SessionExpired(true));
        }
    }

}
