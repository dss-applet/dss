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

import eu.europa.ejusticeportal.dss.applet.common.FileChooser;

/**
 * Manage the FileChooser implementation
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public final class FileChooserHome {

    /**
     * Singleton reference of FileChooserHome.
     */
    private static FileChooserHome instance;

    /**
     * Constructs singleton instance of FileChooserHome.
     */
    private FileChooserHome() {
    }

    /**
     * Provides reference to singleton getClass() of FileChooserHome.
     *
     * @return Singleton instance of FileChooserHome.
     */
    public static FileChooserHome getInstance() {
        if (instance == null) {
            instance = new FileChooserHome();
        }
        return instance;
    }

    private FileChooser fileChooser;

    /**
     * Initialise a provider of FileChooser
     *
     * @param fileChooser the fileChooser implementation
     */
    public void init(FileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    /**
     * @return the fileChooser
     */
    public FileChooser getFileChooser() {
        return fileChooser;
    }

    /**
     * @param fileChooser the fileChooser to set
     */
    public void setFileChooser(FileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }
}
