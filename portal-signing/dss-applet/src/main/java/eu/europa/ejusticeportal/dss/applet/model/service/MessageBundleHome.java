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

import eu.europa.ejusticeportal.dss.applet.event.AppletInitialisationFailure;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.common.MessageBundle;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;

/**
 * Manage the instance of the MessageBundle.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class MessageBundleHome {

    private static final DssLogger LOG = DssLogger.getLogger(MessageBundleHome.class.getSimpleName());
    private static volatile MessageBundleHome instance;

    private MessageBundleHome() {
    }

    /**
     * Get the single instance of MessageBundleHome
     * 
     * @return the instance
     */
    public static MessageBundleHome getInstance() {
        if (null == instance) {
            instance = new MessageBundleHome();
        }
        return instance;
    }

    private MessageBundle messageBundle;

    /**
     * Initialise the MessageBundleHome with a MessageBundle
     * 
     * @param messageBundle
     */
    public void init(MessageBundle messageBundle) {
        this.messageBundle = messageBundle;
    }

    /**
     * Get the MessageBundle object.
     * 
     * @return the messageBundle
     */
    public MessageBundle getMessageBundle() {
        if (messageBundle == null) {
            ExceptionUtils.exception(new UnexpectedException("MessageBundle not available for now"), LOG);
        }
        return messageBundle;
    }

    /**
     * Get the specified message from a key.
     * 
     * @param key the key of the message to get.
     * @return the localised message or, if there is no message, the key
     */
    public String getMessage(final String key) {
        final String m = getMessageBundle().getMessages().get(key);
        return m == null ? key : m;
    }
    /**
     * Waits the current thread until the messages have been initialised.
     */
    public static void waitForMessages() {
        MessageBundleHome mbh = MessageBundleHome.getInstance(); 
        synchronized(mbh){            
            try {
                try {
                    //messages might not be ready
                    mbh.getMessageBundle();
                } catch (UnexpectedException ue){
                    //If the messages are not ready after 1 minute it should be an error. 
                    mbh.wait(60000);
                    try {
                        mbh.getMessageBundle();
                    } catch (UnexpectedException e){
                        Event.getInstance().fire(new AppletInitialisationFailure());    
                    }                    
                }
            } catch (InterruptedException e1) {
                //not important
                LOG.debug("interrupted",e1);
            }
            
        }
    }
}
