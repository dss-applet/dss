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

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

/**
 * 
 * A dialog to select a file.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class FileDialog {

    /**
     * Singleton reference of FileDialogHome.
     */
    private static FileDialog instance;

    /**
     * Constructs singleton instance of FileDialogHome.
     */
    private FileDialog() {
    }

    /**
     * Provides reference to singleton getClass() of FileDialogHome.
     * 
     * @return Singleton instance of FileDialogHome.
     */
    public static FileDialog getInstance() {
        if (instance == null) {
            instance = new FileDialog();
        }
        return instance;
    }

    /**
     * Load file dialog, required to load the [signed] pdf, PKCS11 library or PKCS12 file.
     */
    private JFileChooser fileLoadDialog;

    /**
     * @return the fileLoadDialog
     */
    public JFileChooser getFileLoadDialog() {
        // note that we don't want do new JFileChooser() because on jre 1.6-81 that will just hang.
        if (fileLoadDialog == null) {
            String f = System.getProperty("user.home");
            if (f == null) {
                f = System.getProperty("user.dir");
            }
            if (f != null) {
                fileLoadDialog = new JFileChooser(new File(f), new FileSystemView() {

                    @Override
                    public File createNewFolder(File containingDir) throws IOException {

                        File file = new File(containingDir, "/dss4ejustice");
                        return file;
                    }
                });
            } else {
                //hope it's not jre 1.6-81
                fileLoadDialog = new JFileChooser();
            }
        }
        return fileLoadDialog;
    }
}
