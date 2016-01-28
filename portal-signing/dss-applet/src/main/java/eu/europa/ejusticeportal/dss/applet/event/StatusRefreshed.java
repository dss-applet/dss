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

import eu.europa.ejusticeportal.dss.common.MessageLevel;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;

/**
 * Event triggered when the application needs to refresh the global status.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class StatusRefreshed {

    private MessageLevel level;
    private MessagesEnum titleKey;
    private Object [] params;

    /**
     * Default constructor of StatusRefreshed
     *
     * @param titleKey a title key: that can be a key from MessageEnum or a direct translated string.
     * @param level the level of the message
     * @params the parameters for the message
     */
    public StatusRefreshed(MessagesEnum titleKey, MessageLevel level, Object ... params) {
        
        if (MessagesEnum.dss_applet_message_pin_entry_cancelled.equals(titleKey) ||
            MessagesEnum.dss_applet_message_pin_entry_cancelled_mocca.equals(titleKey)){
          //it is thrown as an exception but should be downleveled
            this.level = MessageLevel.INFO;
        } else {
            this.level = level;
        }
        this.titleKey = titleKey;
        this.params = params;
    }

    /**
     * Get the title of the status message
     * @return the titleKey, that can be key from MessageEnum or a direct translated string.
     */
    public MessagesEnum getTitleKey() {
        return titleKey;
    }

    /**
     * Get the level of the status message
     * @return the level
     */
    public MessageLevel getLevel() {
        return level;
    }

    /**
     * Get the parameters for the status message
     * @return the params
     */
    public Object[] getParams() {
        return params;
    }
}
