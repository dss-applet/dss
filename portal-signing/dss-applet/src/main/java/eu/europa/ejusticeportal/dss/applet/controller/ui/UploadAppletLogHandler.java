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

import eu.europa.ejusticeportal.dss.applet.event.UploadAppletLog;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.service.AppletInitSemaphore;
import eu.europa.ejusticeportal.dss.applet.model.service.LoggingHome;
import eu.europa.ejusticeportal.dss.applet.model.service.MessageBundleHome;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.InMemoryHandler;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
 * 
 * Handles the event that requires the details of how the user signed to be logged.
 * 
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1697 $ - $Date: 2014-04-23 20:22:00 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class UploadAppletLogHandler implements UIEventHandlerDelegate {

    private static final UploadAppletLogHandler INSTANCE = new UploadAppletLogHandler();

    private static final DssLogger LOG = LoggingHome.getInstance().getLogger(UploadAppletLogHandler.class.getSimpleName());
    /**
     * 
     * The default constructor for LogSigningInformationHandler.
     */
    private UploadAppletLogHandler() {

    }

    /**
     * Handle the event.
     * Construct a dialog to ask
     * 
     * @param event the event to handle
     * @param arg the argument of the event
     */
    public void doHandle(UIEvent event, String arg) throws CodeException {
        Color defaultColor = UIManager.getColor("Panel.background");
        Font f = new JLabel().getFont();
        JTextArea intro = new JTextArea(MessageBundleHome.getInstance().getMessage(MessagesEnum.dss_applet_intro_upload_log.name()));
        intro.setRows(2);
        intro.setColumns(10);
        intro.setEditable(false);
        intro.setBackground(defaultColor);
        intro.setFont(f);

        final JTextArea details = new JTextArea(InMemoryHandler.getInstance().getLog());
        details.setEditable(false);
        details.setBackground(defaultColor);

        final Box vb = Box.createVerticalBox();
        JTextArea agree = new JTextArea(MessageBundleHome.getInstance().getMessage(MessagesEnum.dss_applet_ask_upload_log.name()));
        agree.setBackground(defaultColor);
        agree.setEditable(false);
        agree.setFont(f);
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        agree.setRows(2);
        agree.setColumns(10);

        vb.add(Box.createVerticalGlue());
        vb.add(intro);
        vb.add(Box.createVerticalGlue());
        vb.add(Box.createVerticalGlue());

        vb.add(Box.createVerticalGlue());
        JPanel pnlDetails = new JPanel();
        pnlDetails.add(details);
        JScrollPane scroller = new JScrollPane(pnlDetails);
        scroller.setPreferredSize(new Dimension(300, (int) screenSize.getHeight() / 3));
        vb.add(scroller);
        vb.add(Box.createVerticalGlue());
        vb.add(agree);

        String title = MessageBundleHome.getInstance().getMessage(MessagesEnum.dss_applet_title_upload_log.name());
        String yes = MessageBundleHome.getInstance().getMessage(MessagesEnum.dss_applet_message_yes.name());
        String no = MessageBundleHome.getInstance().getMessage(MessagesEnum.dss_applet_message_no.name());
        String [] options = new String[]{no,yes};
        int option = JOptionPane.showOptionDialog(null, vb, title,JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (option == 1) {            
                try {
                    AppletInitSemaphore.getInstance().setShuttingDown();
                    Event.getInstance().fire(new UploadAppletLog()); 
                    //sleep to allow time for log to be uploaded
                    Thread.sleep(2000L);
                    InMemoryHandler.getInstance().clear();
                }    catch (InterruptedException e) {
                    LOG.error("Upload applet log interrupted", e);
                }
        }        
    }

    /**
     * Gets the Singleton instance of LogSigningInformationHandler
     * 
     * @return the instance
     */
    public static UploadAppletLogHandler getInstance() {
        return INSTANCE;
    }

}
