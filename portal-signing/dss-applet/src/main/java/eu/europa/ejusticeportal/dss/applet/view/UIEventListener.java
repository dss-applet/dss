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


import eu.europa.ejusticeportal.dss.applet.event.ActivateCardTerminalWatcher;
import eu.europa.ejusticeportal.dss.applet.event.AppletInitialisationFailure;
import eu.europa.ejusticeportal.dss.applet.event.AppletStarted;
import eu.europa.ejusticeportal.dss.applet.event.AppletStopped;
import eu.europa.ejusticeportal.dss.applet.event.CardAdvice;
import eu.europa.ejusticeportal.dss.applet.event.CertificateSelected;
import eu.europa.ejusticeportal.dss.applet.event.CertificatesRefreshed;
import eu.europa.ejusticeportal.dss.applet.event.ClearMessages;
import eu.europa.ejusticeportal.dss.applet.event.ClosePasswordDialog;
import eu.europa.ejusticeportal.dss.applet.event.LibraryUpdated;
import eu.europa.ejusticeportal.dss.applet.event.LoadingRefreshed;
import eu.europa.ejusticeportal.dss.applet.event.MessagesReady;
import eu.europa.ejusticeportal.dss.applet.event.MoccaPinEntryReady;
import eu.europa.ejusticeportal.dss.applet.event.P12Updated;
import eu.europa.ejusticeportal.dss.applet.event.PdfSignedOk;
import eu.europa.ejusticeportal.dss.applet.event.SessionExpired;
import eu.europa.ejusticeportal.dss.applet.event.ShowMiddlewareHelp;
import eu.europa.ejusticeportal.dss.applet.event.ShowOtherCertificates;
import eu.europa.ejusticeportal.dss.applet.event.SigningError;
import eu.europa.ejusticeportal.dss.applet.event.SigningMethodChanged;
import eu.europa.ejusticeportal.dss.applet.event.SigningStatusRefreshed;
import eu.europa.ejusticeportal.dss.applet.event.StatusRefreshed;
import eu.europa.ejusticeportal.dss.applet.event.UIActivation;
import eu.europa.ejusticeportal.dss.applet.event.UploadAppletLog;
import eu.europa.ejusticeportal.dss.applet.event.UserSurvey;
import eu.europa.ejusticeportal.dss.applet.event_api.EventListener;
import eu.europa.ejusticeportal.dss.applet.model.action.ReloadApplet;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * This class manage implements the EventListener interface for Event processing.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class UIEventListener implements EventListener {

    private static final DssLogger LOG = DssLogger.getLogger(UIEventListener.class.getSimpleName());

    private static Map<Class<?>, UIEventListenerDelegate> listenerMap = new HashMap<Class<?>, UIEventListenerDelegate>();
    static {
        listenerMap.put(ClearMessages.class, new ClearMessagesHandler());
        listenerMap.put(CertificatesRefreshed.class, new CertificatesRefreshedHandler());
        listenerMap.put(CardAdvice.class, new CardAdviceHandler());
        listenerMap.put(StatusRefreshed.class, new StatusRefreshedHandler());
        listenerMap.put(UIActivation.class, new UIActivationHandler());
        listenerMap.put(LoadingRefreshed.class, new LoadingRefreshedHandler());
        listenerMap.put(AppletStarted.class, new AppletStartedHandler());
        listenerMap.put(AppletStopped.class, new AppletStoppedHandler());
        listenerMap.put(SigningStatusRefreshed.class, new SigningStatusRefreshedHandler());
        listenerMap.put(CertificateSelected.class, new CertificateSelectedHandler());
        listenerMap.put(LibraryUpdated.class, new LibraryUpdatedHandler());
        listenerMap.put(P12Updated.class, new P12UpdatedHandler());
        listenerMap.put(SessionExpired.class, new SessionExpiredHandler());
        listenerMap.put(UserSurvey.class, new UserSurveyHandler());
        listenerMap.put(MessagesReady.class, new MessagesReadyHandler());
        listenerMap.put(SigningMethodChanged.class, new SigningMethodChangedHandler());
        listenerMap.put(SigningError.class, new SigningErrorHandler());
        listenerMap.put(ReloadApplet.class, new ReloadAppletHandler());
        listenerMap.put(UploadAppletLog.class, new UploadAppletLogHandler());
        listenerMap.put(ShowMiddlewareHelp.class, new ShowMiddlewareHelpHandler());
        listenerMap.put(AppletInitialisationFailure.class, new AppletInitialisationFailureHandler());
        listenerMap.put(MoccaPinEntryReady.class, new MoccaPinEntryReadyHandler());
        listenerMap.put(ActivateCardTerminalWatcher.class, new ActivateCardTerminalWatcherHandler());
        listenerMap.put(ClosePasswordDialog.class, new ClosePasswordDialogHandler());
        listenerMap.put(ShowOtherCertificates.class, new ShowOtherCertificatesHandler());
        listenerMap.put(PdfSignedOk.class, new PdfSignedOkHandler());
    }
    /**
     * @see EventListener
     * @param event the Event to manage.
     */
    public void process(Object event) {

        UIEventListenerDelegate h = listenerMap.get(event.getClass());
        LOG.log(Level.FINE,"Processing UI event for {0}",event.getClass().getSimpleName());
        if (h==null){
            ExceptionUtils.exception(new UnexpectedException("It seems that event listener is not implemented for '"
                    + event.getClass().getSimpleName() + "'"), LOG); 
        } else {
            h.doHandle(event);
        }
    }



}
