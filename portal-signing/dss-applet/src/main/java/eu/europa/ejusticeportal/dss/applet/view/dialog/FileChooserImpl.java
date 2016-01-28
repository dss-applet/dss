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
package eu.europa.ejusticeportal.dss.applet.view.dialog;

import eu.europa.ejusticeportal.dss.applet.common.FileChooser;
import eu.europa.ejusticeportal.dss.applet.view.dialog.filter.PDFFileFilter;
import eu.europa.ejusticeportal.dss.applet.view.dialog.filter.PKCS11FileFilter;
import eu.europa.ejusticeportal.dss.applet.view.dialog.filter.PKCS12FileFilter;

import java.io.File;

/**
 * This class implements the FileChooser interface and provides File input using JFileChooser objects.
 * (JFileChooser is encapsulated in *Action because of the privileged needed)
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class FileChooserImpl implements FileChooser {

    /**
     * @see FileChooser
     * @return the chosen PKCS11 file
     */
    public File choosePKCS11FileOpen() {
        OpenSelectFileAction a = new OpenSelectFileAction(new PKCS11FileFilter());
        a.exec();
        return a.getFile();
    }

    /**
     * @see FileChooser
     * @return the chosen PKCS12 file
     */
    public File choosePKCS12FileOpen() {
        OpenSelectFileAction a = new OpenSelectFileAction(new PKCS12FileFilter());
        a.exec();
        return a.getFile();
    }

    /**
     * @see FileChooser
     * @return the chosen PDF file to save
     */
    public File choosePdfFileSave(String defaultName) {
        SaveSelectFileAction a = new SaveSelectFileAction(new PDFFileFilter(), defaultName);
        a.exec();
        return a.getFile();
    }
}
