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
package eu.europa.ejusticeportal.dss.applet.event;

import eu.europa.ejusticeportal.dss.applet.model.service.MessageBundleHome;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;

/**
 * Event triggered when the application needs to modify the "loading/busy" status.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class LoadingRefreshed {

    private boolean loading;
    private boolean error;
    private String message1;
    private String message2;

    /**
     * Default constructor of LoadingRefreshed
     *
     * @param loading the application is loading (or performing a job)
     * @param error   an error occurred in the application.
     */
    public LoadingRefreshed(boolean loading, boolean error) {
        this(loading, error,null, null);
    }
    /**
     * Constructor
     * @param loading true to switch on
     * @param error true if it's an error
     * @param message1 first message
     * @param message2 second message
     */
    public LoadingRefreshed(boolean loading, boolean error, String message1, String message2){
        this.loading = loading;
        this.error = error;
        this.message1 = message1;
        this.message2 = message2;
    }
    /**
     * Constructor
     * @param b
     * @param c
     * @param message
     */
    public LoadingRefreshed(boolean loading, boolean error, MessagesEnum message) {
    	this(loading,error,MessageBundleHome.getInstance().getMessage(message.name()),null);
	}
	/**
     * Get the message to show on the loading page
     * @return the message to show
     */
    public String  getMessage1() {
        return message1;
    }
    /**
     * Get the second message to show on the loading page
     * @return the message to show
     */
    public String  getMessage2() {
        return message2;
    }

    /**
     * @return the loading the application is loading (or performing a job)
     */
    public boolean isLoading() {
        return loading;
    }

    /**
     * @return the error the application occurred an error.
     */
    public boolean isError() {
        return error;
    }
}
