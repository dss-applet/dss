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
 * A link on the UI.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 322 $ - $Date: 2012-11-27 17:40:10 +0100 (Tue, 27 Nov
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class Link extends Text {

    private String url = "";

    /**
     * The default constructor for Link.
     */
    public Link() {
    }

    /**
     * The default constructor for Link.
     *
     * @param id the component id
     */
    public Link(String id) {
        super(id);
    }

    /**
     * @return the url of the Link
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url value to set
     */
    public void setUrl(String url) {
        setChanged((this.url == null && url != null) || (this.url != null && !this.url.equals(url)));
        this.url = url;
    }

    /**
     * Writes the component as a Json string
     *
     * @return the json string
     */
    @Override
    public String toJson() {
        StringBuilder s = new StringBuilder();
        s.append("\"url\":\"").append(Utils.escape(url)).append("\"");
        final String tmp = super.toJson();
        if (tmp.length() != 0) {
            s.append(",").append(tmp);
        }
        return s.toString();
    }
}
