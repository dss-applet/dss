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

import eu.europa.ejusticeportal.dss.applet.controller.EventHandler;
import eu.europa.ejusticeportal.dss.applet.event.ClearMessages;
import eu.europa.ejusticeportal.dss.applet.event.LoadingRefreshed;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.service.MessageBundleHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SessionKeepAliveHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SigningContextHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SigningMethodHome;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Handle the callback of UI (browser side). More of an event dispatcher than handler.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class UIEventHandler extends EventHandler<UIEvent> {

    private static final DssLogger LOG = DssLogger.getLogger(UIEventHandler.class.getSimpleName());
    private String arg;

    private static Map<UIEvent, UIEventHandlerDelegate> eventHandlerMap = new HashMap<UIEvent, UIEventHandlerDelegate>();

    static {
        eventHandlerMap.put(UIEvent.selectPKCS11File, new SelectPkcs11FileHandler());
        eventHandlerMap.put(UIEvent.selectPKCS12File, new SelectPkcs12FileHandler());
        eventHandlerMap.put(UIEvent.signDocument, new SignDocumentHandler());
        eventHandlerMap.put(UIEvent.refreshSigningContext, RefreshSigningContextHandler.getInstance());
        eventHandlerMap.put(UIEvent.saveSignedPdf, new SaveSignedPdfHandler());
        eventHandlerMap.put(UIEvent.openSealedPdf, new ViewPdfHandler());
        eventHandlerMap.put(UIEvent.viewPdfBeforeSigning, new ViewPdfHandler());
        eventHandlerMap.put(UIEvent.prepareToSignWithOtherCertificate, new ChangeCertificateHandler());
        eventHandlerMap.put(UIEvent.saveSealedPdf, new SaveSealedPdfHandler());
        eventHandlerMap.put(UIEvent.agreeLogSigningInformation, LogSigningInformationHandler.getInstance());
        eventHandlerMap.put(UIEvent.refuseLogSigningInformation, LogSigningInformationHandler.getInstance());
        eventHandlerMap.put(UIEvent.providePassword, PasswordProvidedHandler.getInstance());
        eventHandlerMap.put(UIEvent.refusePassword, PasswordProvidedHandler.getInstance());
        eventHandlerMap.put(UIEvent.showMiddlewareHelp, new ShowMiddlewareHelpHandler());
        eventHandlerMap.put(UIEvent.agreeSendErrorReport, ErrorReportHandler.getInstance());
        eventHandlerMap.put(UIEvent.refuseSendErrorReport, ErrorReportHandler.getInstance());
        eventHandlerMap.put(UIEvent.uploadAppletLog, UploadAppletLogHandler.getInstance());
        eventHandlerMap.put(UIEvent.signOtherCertificate, new SignOtherCertificateHandler());
        eventHandlerMap.put(UIEvent.showOtherCertificates, new ShowOtherCertificatesHandler());
        
    }

    /**
     * Default constructor of UIEventHandler.
     * 
     * @param arg argument of the UIEvent.
     */
    public UIEventHandler(String arg) {
        this.arg = arg;
    }

    /**
     * Execute the handler for the specified UIEvent.
     * 
     * @param uiEvent the UIEvent
     */
    @Override
    public void doHandle(UIEvent uiEvent) throws CodeException {
        LOG.log(Level.INFO, "UI Event: {0}", new Object[] { uiEvent.getLabel()});
        if (uiEvent.isLocal()) {
            SessionKeepAliveHome.getInstance().keepAlive();
        }
        if (!uiEvent.isBackground()){
            String message1 = null;
            String message2 = null;
            if (uiEvent.isLong()) {
                message1 = MessageBundleHome.getInstance().getMessage(MessagesEnum.dss_applet_message_long_operation.name());
            }
            if (uiEvent.equals(UIEvent.signDocument) && SigningMethodHome.getInstance().getSigningMethod().isWarnAboutPassword()) {
            	message2 = MessageBundleHome.getInstance().getMessage(MessagesEnum.dss_applet_message_password_dialog_hidden.name());
            }
            Event.getInstance().fire(new LoadingRefreshed(true, false, message1, message2));
        }
        if (!uiEvent.isAsync()) {
            // clear the messages from the UI, but only if the event is synchronous with a user interaction
            Event.getInstance().fire(new ClearMessages());
        }
        // pass the event through the filters
        boolean propagate = filter(uiEvent, arg);
        // Dispatch the event to its handler
        if (propagate) {
            UIEventHandlerDelegate h = eventHandlerMap.get(uiEvent);
            if (h == null) {
                ExceptionUtils.exception(new UnexpectedException(
                        "It seems that event handler is not implemented for '" + uiEvent.name() + "'"), LOG);
            } else {
                h.doHandle(uiEvent, arg);
            }
        }
    }

    /**
     * Filter the event
     * 
     * @param uiEvent the event
     * @param a the event argument
     * @throws CodeException for managed exception from the filter
     */
    private boolean filter(final UIEvent uiEvent, final String a) throws CodeException {
        if (SigningContextHome.getInstance().getSigningContext()!=null && SigningContextHome.getInstance().getSigningContext().getCardProfiles()!=null){
            for (CardProfile cp : SigningContextHome.getInstance().getSigningContext().getCardProfiles()) {
                if (cp.getEventFilters() != null) {
                    for (String s : cp.getEventFilters()) {
                        try {
                            final UIEventFilter ef = (UIEventFilter) Class.forName(s).newInstance();
                            LOG.log(Level.FINE, "Applying filter {0} to event {1}.", new Object[] { s, uiEvent.name() });
                            if (!ef.apply(uiEvent, a)) {
                                return false;
                            }
                        } catch (InstantiationException e) {
                            LOG.warn(e.getMessage(), e);
                        } catch (IllegalAccessException e) {
                            LOG.warn(e.getMessage(), e);
                        } catch (ClassNotFoundException e) {
                            LOG.warn( e.getMessage(), e);
                        }
                    }
                }
            }
        }
        return true;
    }
}
