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

import eu.europa.ejusticeportal.dss.applet.view.dialog.filter.AbstractFileFilter;
import eu.europa.ejusticeportal.dss.common.AbstractPrivilegedAction;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Select a file with a File Dialog in "Open" mode.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class OpenSelectFileAction extends AbstractPrivilegedAction {

    private static final DssLogger LOG = DssLogger.getLogger(OpenSelectFileAction.class.getSimpleName());
    private AbstractFileFilter filter;
    private File file;

    /**
     * The default constructor of OpenSelectFileAction
     *
     * @param filter the filter to apply to the File Dialog view
     */
    public OpenSelectFileAction(final AbstractFileFilter filter) {
        this.filter = filter;
    }

    @Override
    protected void doExec() {
        try {
            //invokeAndWait avoids blocked UI in MacOS 
            SwingUtilities.invokeAndWait(new Opener());
        } catch (InterruptedException e) {
            LOG.debug("interrupted",e);
        } catch (InvocationTargetException e) {
            LOG.debug("invocation target",e);
        }
    }

    /**
     * Open the file dialog
     */
    private class Opener implements Runnable{

        /**
         * Open the file dialog
         */
        public void run() {
            getFileDialog().resetChoosableFileFilters();
            getFileDialog().setFileSelectionMode(filter.getFileSelectionMode());
            getFileDialog().setFileFilter(filter);
            getFileDialog().setVisible(true);
            int returnVal = getFileDialog().showOpenDialog(JFrame.getFrames()[0]);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = getFileDialog().getSelectedFile();
            } else {
                file = null;
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
     * @return the File Dialog object
     */
    protected JFileChooser getFileDialog() {
        return FileDialog.getInstance().getFileLoadDialog();
    }
}
