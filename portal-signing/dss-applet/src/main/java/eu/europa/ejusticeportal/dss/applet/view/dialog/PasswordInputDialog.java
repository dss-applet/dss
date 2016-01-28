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
package eu.europa.ejusticeportal.dss.applet.view.dialog;

import eu.europa.ejusticeportal.dss.applet.common.OperationCancelledException;
import eu.europa.ejusticeportal.dss.applet.common.UIControllerHome;
import eu.europa.ejusticeportal.dss.applet.model.service.AsynchronousPasswordInputCallback;
import eu.europa.ejusticeportal.dss.applet.model.service.PasswordHome;
import eu.europa.ejusticeportal.dss.applet.view.UIFunction;

/**
 * A password input dialog that implement {@link eu.europa.ec.markt.dss.signature.token.PasswordInputCallback}. This
 * class is used to provide a password to DSS.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class PasswordInputDialog implements AsynchronousPasswordInputCallback {

    private final String description;
    private char[] password;
    private boolean cancelled = false;
    private boolean wrongPin = false;

    private boolean visible = false;
    
    /**The number of times this dialog has been used. 
     * Tries>1 means a bad PIN.*/
    private int tries = 0;
    
    /**
     * The default Constructor for PasswordInputDialog
     */
    public PasswordInputDialog() {
        this.description = "";
    }

    /**
     * Constructor for PasswordInputDialog.
     * 
     * @param description descriptive text to put on the dialog
     */
    public PasswordInputDialog(String description) {
        this.description = description;
    }

    /**
     * Sets the password. This is used in the callback for asynchronous password collection.
     * 
     * @param password the password to set.
     */
    public void setPassword(char[] password) {
        synchronized (this) {
            this.password = password == null ? null : password.clone();
        }
    }

    /**
     * Provide a password.
     * 
     * @return the password as a char array.
     */
    public char[] getPassword() {
        
        PasswordHome.getInstance().setPasswordInputCallback(this);
        synchronized (this) {
            tries ++;
            password = null;
            UIControllerHome.getInstance().getUiController().eval(UIFunction.requestPassword, description,
                    Boolean.valueOf(tries>1).toString());
            visible = true;
            try {
                while (password == null && !cancelled) {
                    // wait to be notified of password or cancellation vi UIEventHandler
                    wait();
                }
            } catch (InterruptedException e) {
                setCancelled(true);
            }
            visible = false;
            if (cancelled) {
                tries = 0;
                throw new OperationCancelledException();
            }
            return password == null ? "".toCharArray() : password;
        }

    }

    /**
     * Tests if the dialog was cancelled by the user
     * 
     * @return true if cancelled
     */
    public boolean wasCancelled() {

        synchronized (this) {
            return cancelled;
        }
    }

    /**
     * Sets if the dialog was cancelled by the user
     * 
     * @param cancelled true if cancelled
     */
    public void setCancelled(boolean cancelled) {
        synchronized (this) {
            this.cancelled = cancelled;
        }
    }

    /**
     * Sets if the wrong pin was entered
     */
    public void setWrongPin(boolean b) {
        synchronized (this) {
            wrongPin = b;
        }

    }

    /**
     * Checks if the wrong PIN was entered
     */
    public boolean wasWrongPin() {
        synchronized (this) {
            return wrongPin;
        }
    }
    /**
     * @return true if the password dialog is visible.
     */
    public boolean isVisible(){
        return visible;
    }

    /**
     * Reset the flags.
     */
    public void reset(){
        visible = false;
        wrongPin = false;
        cancelled = false;
        tries = 0;
    }
    
}
