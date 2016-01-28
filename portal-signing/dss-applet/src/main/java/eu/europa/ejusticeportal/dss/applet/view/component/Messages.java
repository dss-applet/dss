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
package eu.europa.ejusticeportal.dss.applet.view.component;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A list of messages on the UI.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class Messages extends Component {

    private List<Message> messages = new ArrayList<Message>();
    private boolean clear;

    /**
     * Default constructor of Messages.
     */
    public Messages() {
    }

    /**
     * Default constructor of Messages.
     * 
     * @param id
     */
    public Messages(String id) {
        super(id);
    }

    /**
     * @return the list of messages
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Add a message
     * 
     * @param message the message to add
     */
    public void addMessage(Message message) {
        clear = false;
        messages.add(message);
        setChanged(true);
    }

    /**
     * Clear previous messages.
     */
    public void clear() {
        messages.clear();
        clear = true;
        setChanged(true);
    }

    /**
     * Writes the component as a Json string
     * 
     * @return the json string
     */
    @Override
    public String toJson() {
        StringBuilder s = new StringBuilder();
        s.append("\"clear\":").append(clear);
        if (messages.size() > 0) {
            s.append(",\"messages\" : [ ");
            for (Component c : messages) {
                s.append("{").append(c.toJson()).append("}").append(",");
            }
            s.deleteCharAt(s.length() - 1);
            s.append("]");
        }
        final String tmp = super.toJson();
        if (tmp.length() != 0) {
            s.append(",").append(tmp);
        }
        return s.toString();
    }

    /**
     * @return the clear
     */
    public boolean isClear() {
        return clear;
    }

    /**
     * @param clear the clear to set
     */
    public void setClear(boolean clear) {
        this.clear = clear;
    }
}
