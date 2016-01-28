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
package eu.europa.ejusticeportal.dss.applet.controller;

import eu.europa.ejusticeportal.dss.applet.event.LoadingRefreshed;
import eu.europa.ejusticeportal.dss.applet.event.StatusRefreshed;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.service.AppletInitSemaphore;
import eu.europa.ejusticeportal.dss.common.MessageLevel;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;


/**
 * Abstraction of the Event Handler for the applet.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @param <T> the Event type.
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public abstract class EventHandler<T> {

    private static final DssLogger LOG = DssLogger.getLogger(EventHandler.class.getSimpleName());

    /**
     * Execute the doHandle function in a new Thread, managing raised exceptions.
     * @param event the event
     */
    public void handle(final T event) {
        new Thread() {
            @Override
            public void run() {
                try {
                    doHandle(event);
                } catch (CodeException ex) {
                    ExceptionUtils.log(ex, LOG);
                    if (!AppletInitSemaphore.getInstance().isShuttingDown()){
                        //The page unloads and the JavaScript communication is unreliable, so we get an error. Avoid briefly
                        //showing it on the screen
                        Event.getInstance().fire(new StatusRefreshed(MessagesEnum.valueOf(ex.getCode()), MessageLevel.ERROR, ex.getObjects()));
                    }
                    Event.getInstance().fire(new LoadingRefreshed(false, true));
                } catch (UnexpectedException ex) {
                    ExceptionUtils.log(ex, LOG);
                    if (!AppletInitSemaphore.getInstance().isShuttingDown()){
                        Event.getInstance().fire(
                                new StatusRefreshed(MessagesEnum.dss_applet_message_technical_failure, MessageLevel.ERROR));
                    }
                    Event.getInstance().fire(new LoadingRefreshed(false, true));
                } catch (Exception ex) {
                    ExceptionUtils.log(ex, LOG);
                    if (!AppletInitSemaphore.getInstance().isShuttingDown()){
                        Event.getInstance().fire(new StatusRefreshed(MessagesEnum.dss_applet_message_technical_failure,
                                MessageLevel.ERROR));
                    }
                    Event.getInstance().fire(new LoadingRefreshed(false, true));
                }
            }
        }.start();
    }

    /**
     * Execute the handler for the specified event.
     *
     * @param event
     * @throws CodeException
     */
    protected abstract void doHandle(T event) throws CodeException;
}
