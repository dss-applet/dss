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
import java.util.Collections;
import java.util.List;

/**
 * 
 * A select in the UI.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 320 $ - $Date: 2012-11-15 17:24:52 +0100 (Thu, 15 Nov 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class Select extends Component {

    private List<Text> options = new ArrayList<Text>();

    /**
     * 
     * The default constructor for Select.
     */
    public Select() {
    }

    /**
     * 
     * The constructor for Select.
     * 
     * @param id the component identifier
     */
    public Select(final String id) {
        super(id);
    }

    /**
     * Adds an option
     * 
     * @param option the option to add
     */
    public void addOption(final Text option) {
        setChanged(true);
        options.add(option);
    }

    /**
     * Removes all options
     */
    public void removeAll() {
        setChanged(true);
        options.clear();
    }

    /**
     * Writes the component as a Json string
     * 
     * @return the json string
     */
    @Override
    public String toJson() {
        StringBuilder s = new StringBuilder();
        boolean changed = false;
        for (Text t:options){
            if (t.isChanged()){
                changed = true;
            }
        }
        if (options.size() > 0 & changed) {
            s.append("\"options\":[");
            int i = 1;
            for (Text t : options) {
                s.append("{").append(t.toJson()).append("}");
                if (i++ != options.size()){
                    s.append(",");
                }
            }
            if (s.toString().endsWith(",")){
                s.substring(0,s.toString().length()-1);
            }
            s.append("],");
        } else if (options.size() == 0){
            s.append("\"options\":[],");
        }
        return s.append(super.toJson()).toString();
    }

    /**
     * Reset the component
     */
    @Override
    public void reset() {
        super.reset();
        for (Component c : options) {
            c.reset();
        }
    }

    /**
     * Unset the component
     */
    @Override
    public void unset() {
        super.unset();
        for (Component c : options) {
            c.unset();
        }
    }

    /**
     * Gets the index of the selected option
     * 
     * @return the index
     */
    public int getIndexSelectedOption() {
        int i = 0;
        for (Component c : options) {
            if (c.isSelected()) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * Gets the options
     * 
     * @return list of options (unmodifiable)
     */
    public List<Text> getOptions() {
        return Collections.unmodifiableList(options);
    }
}
