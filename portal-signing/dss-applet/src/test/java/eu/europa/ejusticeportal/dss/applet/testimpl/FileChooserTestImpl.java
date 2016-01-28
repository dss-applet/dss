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
package eu.europa.ejusticeportal.dss.applet.testimpl;

import eu.europa.ejusticeportal.dss.applet.common.FileChooser;

import java.io.File;

/**
 * Test implementation of FileChooser
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class FileChooserTestImpl implements FileChooser {

    private File fPKCS11;
    private File fPKCS12;
    private File fPDFOpen;
    private File fPDFSave;

    /**
     * Default constructor of FileChooserTestImpl
     *
     * @param fPKCS11
     * @param fPKCS12
     * @param fPDFOpen
     * @param fPDFSave
     */
    public FileChooserTestImpl(File fPKCS11, File fPKCS12, File fPDFOpen, File fPDFSave) {
        this.fPKCS11 = fPKCS11;
        this.fPKCS12 = fPKCS12;
        this.fPDFOpen = fPDFOpen;
        this.fPDFSave = fPDFSave;
    }

    /**
     * @return the selected PKCS11 file
     */
    public File choosePKCS11FileOpen() {
        return getfPKCS11();
    }

    /**
     *
     * @return the selected PKCS12 file
     */
    public File choosePKCS12FileOpen() {
        return getfPKCS12();
    }

    /**
     * @return the selected PDF file
     */
    public File choosePdfFileOpen() {
        return getfPDFOpen();
    }

    /**
     * @param defaultName (not use in test implementation)
     * @return the selected PDF file
     */
    public File choosePdfFileSave(String defaultName) {
        return getfPDFSave();
    }

    /**
     * @return the fPKCS11
     */
    public File getfPKCS11() {
        return fPKCS11;
    }

    /**
     * @param fPKCS11 the fPKCS11 to set
     */
    public void setfPKCS11(File fPKCS11) {
        this.fPKCS11 = fPKCS11;
    }

    /**
     * @return the fPKCS12
     */
    public File getfPKCS12() {
        return fPKCS12;
    }

    /**
     * @param fPKCS12 the fPKCS12 to set
     */
    public void setfPKCS12(File fPKCS12) {
        this.fPKCS12 = fPKCS12;
    }

    /**
     * @return the fPDFOpen
     */
    public File getfPDFOpen() {
        return fPDFOpen;
    }

    /**
     * @param fPDFOpen the fPDFOpen to set
     */
    public void setfPDFOpen(File fPDFOpen) {
        this.fPDFOpen = fPDFOpen;
    }

    /**
     * @return the fPDFSave
     */
    public File getfPDFSave() {
        return fPDFSave;
    }

    /**
     * @param fPDFSave the fPDFSave to set
     */
    public void setfPDFSave(File fPDFSave) {
        this.fPDFSave = fPDFSave;
    }
}
