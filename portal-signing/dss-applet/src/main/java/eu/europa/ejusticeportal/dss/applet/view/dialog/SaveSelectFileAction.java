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

import eu.europa.ejusticeportal.dss.common.AbstractPrivilegedAction;
import eu.europa.ejusticeportal.dss.applet.model.service.MessageBundleHome;
import eu.europa.ejusticeportal.dss.applet.view.dialog.filter.AbstractFileFilter;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Privilege action to select a file on File System for saving purpose.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class SaveSelectFileAction extends AbstractPrivilegedAction {

    private AbstractFileFilter filter;
    private File file;
    private String defaultFileName;

    /**
     *  Default constructor of SaveSelectFileAction.
     *
     * @param filter Filter for the view of the File Dialog.
     * @param defaultFileName Default file name.
     */
    public SaveSelectFileAction(final AbstractFileFilter filter, final String defaultFileName) {
        this.filter = filter;
        this.defaultFileName = defaultFileName;
    }

    @Override
    protected void doExec() {
        getFileDialog().setFileSelectionMode(filter.getFileSelectionMode());
        getFileDialog().setSelectedFile(new File(defaultFileName));
        getFileDialog().setFileFilter(filter);
        getFileDialog().setVisible(true);
        while (file == null) {
            int returnVal = getFileDialog().showSaveDialog(JFrame.getFrames().length>0?JFrame.getFrames()[0]:new JFrame());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = getFileDialog().getSelectedFile();
                if (file.exists()){
                	int option = JOptionPane.showConfirmDialog(getFileDialog(), 
                			MessageBundleHome.getInstance().getMessage(MessagesEnum.dss_applet_overwrite_file.name()),
                			MessageBundleHome.getInstance().getMessage(MessagesEnum.dss_applet_overwrite_file_title.name()),
                			JOptionPane.YES_NO_OPTION);
                	if (option == JOptionPane.NO_OPTION){
                		file = null;
                	}
                }
            } else {
                file = null;
                break;
            }
        }
        
    }

    /**
     * @return the file if the user clicked on "save", null if he
     *         cancelled.
     */
    public File getFile() {
        return file;
    }

    /**
     * @return the instance of the File Dialog.
     */
    protected JFileChooser getFileDialog() {
        return FileDialog.getInstance().getFileLoadDialog();
    }
}
