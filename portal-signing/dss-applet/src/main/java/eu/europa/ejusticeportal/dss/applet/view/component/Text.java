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

import eu.europa.ejusticeportal.dss.common.Utils;

/**
 *
 * A Text component on the UI
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class Text extends Component {

    private String text = "";
    private String altText = "";

    /**
     * Constructor of Text
     */
    public Text() {
    }

    /**
     * Constructor of Text
     *
     * @param id the id of the component
     */
    public Text(final String id) {
        super(id);
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(final String text) {
        setChanged(this.text,text);
        this.text = text;
    }

    /**
     * Get some alternative text
     * @return the altText
     */
    public String getAltText() {
        return altText;
    }

    /**
     * Set some alternative text
     * @param altText the text to set
     */
    public void setAltText(final String altText) {
        setChanged(this.altText,altText);
        this.altText = altText;
    }

    
    /**
     * Writes the component as a Json string
     *
     * @return the json string
     */
    @Override
    public String toJson() {
        StringBuilder s = new StringBuilder();
        s.append("\"text\":\"").append(text).append("\",");
        s.append("\"altText\":\"").append(altText).append("\"");
        final String tmp = super.toJson();
        if (tmp.length() != 0) {
            s.append(",").append(tmp);
        }
        return s.toString();
    }
}
