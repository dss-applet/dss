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
package eu.europa.ejusticeportal.dss.applet.event_api;


/**
 * This class manage the EventListener of the application.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class Event {

    /**
     * Singleton reference of Event.
     */
    private static Event instance;

    /**
     * Constructs singleton instance of Event.
     */
    private Event() {
    }

    /**
     * Provides reference to singleton getClass() of Event.
     * 
     * @return Singleton instance of Event.
     */
    public static Event getInstance() {
        if (instance == null) {
            instance = new Event();
        }
        return instance;
    }

    private EventListener listener;

    /**
     * Register a EventListener for the application.
     * 
     * @param listener the EventListener to register
     */
    public void registerListener(EventListener listener) {
        this.listener = listener;
    }

    /**
     * Fire a new Event.
     * 
     * @param event the event to fire
     */
    public void fire(Object event) {
        listener.process(event);
    }
}
