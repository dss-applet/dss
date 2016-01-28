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
package eu.europa.ejusticeportal.dss.applet.view;
import eu.europa.ejusticeportal.dss.applet.event.UIActivation;
import eu.europa.ejusticeportal.dss.applet.model.service.FingerprintHome;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;


import javax.swing.SwingUtilities;
import javax.swing.UIManager;
/**
 * 
 * Handles the UI event activating the UI for the first time.
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1697 $ - $Date: 2014-04-23 20:22:00 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class UIActivationHandler implements UIEventListenerDelegate{
    private static final DssLogger LOG = DssLogger.getLogger(UIActivationHandler.class.getSimpleName());
    //the minimum JRE version that the system look and feel works without issue.
    private static final double MIN_JRE_VERSION_FOR_SYS_LF = 1.6;
    /**
     * Handle the event
     * @param event the event to handle
     */
    public void doHandle(Object event) {
        UIState.transition(UIState.SI____);
        UI.updateUI();

        // Trying to set system look and feel (For input dialog and file dialog)
        try {
            if (FingerprintHome.getInstance().getJreVersion()>=MIN_JRE_VERSION_FOR_SYS_LF) {
                // Set system Java L&F - for JRE5 although the L&F loads without error, there are
                // issues with the file browser.
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());                
                SwingUtilities.updateComponentTreeUI(((UIActivation)event).getDssApplet());
            }
        } catch (Exception ex) {
            // Just accept the default L&F (Metal)
            ExceptionUtils.log(ex, LOG);
        }   
    }

}
