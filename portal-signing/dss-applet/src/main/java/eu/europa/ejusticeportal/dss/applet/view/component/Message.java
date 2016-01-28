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

import eu.europa.ejusticeportal.dss.common.MessageLevel;
import eu.europa.ejusticeportal.dss.common.Utils;

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
public class Message extends Component {

    private String title = "";
    private MessageLevel level = MessageLevel.INFO;
    private List<Text> details = new ArrayList<Text>();

    /**
     * Default constructor of Message.
     */
    public Message() {
        this(String.valueOf(System.currentTimeMillis()));
    }

    /**
     * Constructor of Message.
     * 
     * @param id the identifier
     */
    public Message(String id) {
        super(id);
    }

    /**
     * @return the list of details
     */
    public List<Text> getDetails() {
        return details;
    }

    /**
     * Set the details in the message
     * 
     * @param details the details to set
     */
    public void setDetails(List<Text> details) {
        this.details = details;
    }

    /**
     * Writes the component as a Json string
     * 
     * @return the json string
     */
    @Override
    public String toJson() {
        StringBuilder s = new StringBuilder();
        s.append("\"level\":\"").append(Utils.escape(getLevel().name())).append("\",");
        s.append("\"title\" : \"").append(getTitle()).append("\"");
        if (details.size() > 0) {
            s.append(",\"details\" : [ ");
            for (Component c : details) {
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        setChanged(this.title, title);
        this.title = title;
    }

    /**
     * @return the level
     */
    public MessageLevel getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(MessageLevel level) {
        setChanged(this.level, level);
        this.level = level;
    }

    /**
     * Adds a detail to the message
     * 
     * @param detail
     */
    public void addDetail(Text detail) {
        setChanged(true);
        this.details.add(detail);
    }
}
