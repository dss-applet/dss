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
package eu.europa.ejusticeportal.dss.applet.model.action;

import eu.europa.ejusticeportal.dss.common.AbstractPrivilegedAction;
import eu.europa.ejusticeportal.dss.applet.event.StatusRefreshed;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.common.MessageLevel;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;

/**
 * Action with elevated privileged to save the SignedPdf on the client computer.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision$ - $Date$
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class SavePdfAction extends AbstractPrivilegedAction {

    private static final DssLogger LOG = DssLogger.getLogger(SavePdfAction.class.getSimpleName());
    private final byte[] pdf;
    private final File file;

    /**
     * Default constructor for the SavePdfAction
     *
     * @param pdf the pdf data
     * @param file  the file in which the pdf will be stored
     */
    public SavePdfAction(final byte[] pdf, final File file) {
        this.pdf = pdf.clone();
        this.file = file;
    }

    /**
     * Allow the user to save the signed PDF on his computer.
     */
    @Override
    protected void doExec() {
        LOG.info("saveSignedPDF");
        if (pdf == null || pdf.length == 0) {
            throw new IllegalArgumentException("pdf == null || pdf.length == 0");
        }

        if (file != null) {
            FileOutputStream fos = null;
            try {
                LOG.log(Level.FINE, "Saving file ", file);
                LOG.log(Level.FINE, "Saving file to {0} ", file);
                fos = new FileOutputStream(file);
                fos.write(pdf);
                fos.close();
                Event.getInstance().fire(new StatusRefreshed(MessagesEnum.dss_applet_message_pdf_saved_path, MessageLevel.INFO, file.getAbsolutePath()));
            } catch (IOException ex) {
                // The user can't do anything about this
                LOG.error("Cannot save the file",ex);
                Event.getInstance().fire(new StatusRefreshed(MessagesEnum.dss_applet_message_pdf_not_saved, MessageLevel.ERROR, file.getAbsolutePath()));

            } finally {
                IOUtils.closeQuietly(fos);
            }
        }
    }
}
