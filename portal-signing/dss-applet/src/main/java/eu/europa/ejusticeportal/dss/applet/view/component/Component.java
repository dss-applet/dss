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

/**
 *
 * A component on the UI
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 322 $ - $Date: 2012-11-27 17:40:10 +0100 (Tue, 27 Nov
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class Component {

    private boolean enabled;
    private boolean visible;
    private boolean selected;
    private int changed;
    private String id;

    /**
     *
     * The default constructor for Component.
     */
    public Component() {
    }

    /**
     *
     * The default constructor for Component.
     *
     * @param id the component id
     */
    public Component(final String id) {
        this.id = id;
    }

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        setChanged(this.enabled != enabled);
        this.enabled = enabled;
    }

    /**
     * @return the changed
     */
    public boolean isChanged() {
        return changed > 0;
    }

    /**
     * @param changed the changed to set
     */
    public void setChanged(boolean changed) {
        if (changed) {
            this.changed++;
        }
    }

    /**
     * Reset the component
     */
    public void reset() {
        changed = 0;
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        setChanged(this.visible != visible);
        this.visible = visible;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        setChanged(this.selected != selected);
        this.selected = selected;
    }

    /**
     * Writes the component as a Json string
     *
     * @return the json string
     */
    public String toJson() {
        return changed > 0 ? "\"enabled\":" + enabled + ",\"selected\":" + selected + ",\"visible\":" + visible
                + ",\"id\":\"" + id + "\"" : "";
    }

    /**
     * Unset the component
     */
    public void unset() {
        changed = 1;
    }
    /**
     * Set the state of the component
     * @param s the state to set
     */
    public void setState(final ComponentState s){
        s.apply(this);
    }
    
    /**
     * Sets the changed attribute
     * @param currentValue the current value
     * @param newValue the new value
     */
    protected void setChanged(Object currentValue, Object newValue){
        setChanged((currentValue == null && newValue != null) || (currentValue != null && !currentValue.equals(newValue)));
    }
}
