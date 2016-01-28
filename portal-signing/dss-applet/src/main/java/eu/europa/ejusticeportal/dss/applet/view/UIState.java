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
package eu.europa.ejusticeportal.dss.applet.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import eu.europa.ejusticeportal.dss.common.AppletSigningMethod;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;

/**
 *
 * Enumeration of the possible state of the DssApplet.
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision$ - $Date$
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public enum UIState {

    /**
     * Initial
     */
    SI____ ("Initial"),
    /**
     * Smart card signing method
     */
    SC____ ("Smart card signing method"),
    /**
     * Smart card - certificate selected
     */
    SC_CS_ ("Smart card - certificate selected"),
    /**
     * Smart card - signed
     */
    SC_SG_ ("Smart card - signed"),
    /**
     * Installed signing method
     */    
    IC____ ("Installed certificate signing method"),
    /**
     * Smart card - certificate selected
     */
    IC_CS_ ("Installed certificate - certificate selected"),
    /**
     * Smart card - signed
     */
    IC_SG_ ("Installed certificate - signed"),
    /**
     * PKCS12 signing method
     */
    P12___("PKCS12 signing method"),
    /**
     * PKCS12 - Certificate file provided
     */
    P12_FP("PKCS12 - Certificate file provided"),
    /**
     * PKCS12- certificate selected
     */
    P12_CS("PKCS12- certificate selected"),
    /**
     * PKCS12 - signed
     */
    P12_SG("PKCS12 - signed"),

    /**
     * Final
     *
     */
    SF____("Final");
    
    private String label;

    /**
     * The constructor for UIState.
     * @param label, used for logging
     */
    private UIState(String label){
        this.label = label;
    }
    private static final DssLogger LOG = DssLogger.getLogger(UIState.class.getSimpleName());
    /**
     * Current State of the DssApplet.
     */
    private static UIState currentState = UIState.SI____;

    /**
     * Make the transition from currentState to newState in the finite-state machine.
     *
     * @param newState the new State
     */
    public static void transition(final UIState newState) {
        LOG.log(Level.INFO, "UI transitions from \"{0}\" to \"{1}\"", new Object[]{currentState.getLabel(), newState.getLabel()});

        if (isLegalTransition(currentState, newState)) {
            UI.initUiFromState(newState);
            currentState = newState;

        } else {
            LOG.log(Level.SEVERE, "Illegal transition from {0} to {1}", new Object[]{currentState, newState});

        }        
    }

    public String getLabel() {
        return label;
    }

    /**
     * Reset the state machine.
     */
    protected static void reset() {
        currentState = UIState.SI____;
    }

    /**
     * Transition the UI to ready to sign (i.e. certificate selected or external method)
     */
    public static void transitionReadyToSign() {
        switch (currentState) {
        //case SC_LIB:
        case SC____:
        case SC_CS_:
        case SC_SG_:
            transition(SC_CS_);
            break;
        case IC____:
        case IC_CS_:
        case IC_SG_:
            transition(IC_CS_);
            break;            
        case P12_FP:
        case P12_CS:
        case P12_SG:
        case P12___:
            transition(P12_CS);
            break;
        default:
            LOG.log(Level.SEVERE, "Unexpectedly ready to sign from state {0}", new Object[]{currentState});
        }
    }

    /**
     * Transition the UI to signed state (doc signed online or uploaded)
     */
    public static void transitionSigned() {
        switch (currentState) {
        case SC_CS_:
            transition(SC_SG_);
            break;
        case IC_CS_:
            transition(IC_SG_);
            break;
        case P12_CS:
            transition(P12_SG);
            break;
        default:
            LOG.log(Level.SEVERE, "Unexpectedly signed from state {0}", new Object[]{currentState});
        }
    }

    /**
     * Test if the current UI state is ready to sign
     * @return
     */
    public static boolean isReadyToSign(){
    	return (SC_CS_==currentState||IC_CS_==currentState||P12_CS==currentState);
    }
    /**
     * @return the current state
     */
    public static UIState getCurrentState() {
        return currentState;
    }
    private static Map<UIState, List<UIState>> transitions = new HashMap<UIState, List<UIState>>();

    // GENERATED STATIC BLOCK
    static {
        transitions.put(SI____,Arrays.asList(SC____,IC____,P12___,SF____,SC_CS_));
        transitions.put(SC____,Arrays.asList(SF____,SC_CS_));        
        transitions.put(SC_CS_,Arrays.asList(SF____,SC_SG_,SC____));
        transitions.put(SC_SG_,Arrays.asList(SF____,SC_CS_,SC____));
        transitions.put(IC____,Arrays.asList(SF____,IC_CS_));        
        transitions.put(IC_CS_,Arrays.asList(SF____,IC_SG_,IC____));
        transitions.put(IC_SG_,Arrays.asList(SF____,IC_CS_,IC____));        
        transitions.put(P12___,Arrays.asList(SF____,P12_FP,P12_CS));
        transitions.put(P12_FP,Arrays.asList(SF____,P12_CS,P12___));
        transitions.put(P12_CS,Arrays.asList(SF____,P12___,P12_SG,P12_FP));
        transitions.put(P12_SG,Arrays.asList(SF____,P12_CS,P12_FP,P12___));
        transitions.put(SF____,new ArrayList<UIState>());
        }

    /**
     * Tests if the state transition is legal
     *
     * @param from from state
     * @param to   to state
     * @return true if legal
     */
    private static boolean isLegalTransition(final UIState from, final UIState to) {
        if (from.equals(to)) {
            return true;
        }
        return transitions.get(from) == null ? false : transitions.get(from).contains(to);
    }

    /**
     * Transition to the default state for the signing method
     * @param signingMethod the method
     */
    public static void transition(AppletSigningMethod signingMethod) {
        switch (signingMethod) {
        case sc:
            transition(UIState.SC____);
            break;
        case installed_cert:
            transition(UIState.IC____);
            break;            
        case p12:
            transition(UIState.P12___);
            break;
        default:
            ExceptionUtils.exception(new UnexpectedException("Unknown signing method: " + signingMethod.name()), LOG);
        }        
    }
}
