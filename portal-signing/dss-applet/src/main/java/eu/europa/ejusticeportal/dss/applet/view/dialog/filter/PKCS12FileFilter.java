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
package eu.europa.ejusticeportal.dss.applet.view.dialog.filter;

import java.io.File;

import javax.swing.JFileChooser;

/**
 * File filter for PKCS12 files.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class PKCS12FileFilter extends AbstractFileFilter {

    /**
     * @param f the file
     * @return Whether the given file is accepted by this filter (P12 filter).
     */
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = FileFilterUtils.getExtension(f);
        return (extension != null && extension.equals(FileFilterUtils.P12));
    }

    /**
     * @return The description of this filter.
     * @see FileView#getName
     */
    @Override
    public String getDescription() {
        return "(*.p12)";
    }

    /**
     * @return the File selection mode that will be used in the FileChooser view
     * @see JFileChooser
     */
    public int getFileSelectionMode() {
        return JFileChooser.FILES_ONLY;
    }
}
