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
/*
 * Project: DG Justice - DSS
 * Contractor: ARHS-Developments.
 *
 * $HeadURL: http://forge.aris-lux.lan/svn/isammp/isamm-pd/trunk/app/buildtools/src/main/resources/eclipse/isamm-pd-java-code-template.xml $
 * $Revision: 6522 $
 * $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * $Author: naramsda $
 */
package eu.europa.ejusticeportal.dss.applet.model;

import eu.europa.ejusticeportal.dss.applet.event.MoccaPinEntryReady;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.service.LoggingHome;
import eu.europa.ejusticeportal.dss.applet.view.dialog.PasswordInputDialog;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;

import at.gv.egiz.smcc.CancelledException;
import at.gv.egiz.smcc.PinInfo;
import at.gv.egiz.smcc.pin.gui.PINGUI;

/**
 * Password input dialog for Mocca PinPad
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class MoccaPasswordInputDialog extends PasswordInputDialog implements PINGUI {
    
    private static final DssLogger LOG = LoggingHome.getInstance().getLogger(MoccaPasswordInputDialog.class.getSimpleName());
    private int retryCount = -1;
    /**
     * 
     * The default constructor for MoccaPasswordInputDialog.
     */
    public MoccaPasswordInputDialog(){
        
    }
    /**
     * 
     * The constructor for MoccaPasswordInputDialog.
     * @param title information to show with the dialog
     */
    public MoccaPasswordInputDialog(String title) {
        super(title);        
    }
    
    
    /* (non-Javadoc)
     * @see at.gv.egiz.smcc.pin.gui.PINProvider#providePIN(at.gv.egiz.smcc.PinInfo, int)
     */
    @Override
    public char[] providePIN(PinInfo arg0, int retries) throws CancelledException, InterruptedException {
        //user has provided the pin - this is via the GUI provided by the applet
        return super.getPassword();
    }

    /* (non-Javadoc)
     * @see at.gv.egiz.smcc.pin.gui.PINGUI#allKeysCleared()
     */
    @Override
    public void allKeysCleared() {
        //user has cleared all entered digits
        LOG.info("Mocca - All Keys Clearded");        
    }

    /* (non-Javadoc)
     * @see at.gv.egiz.smcc.pin.gui.PINGUI#correctionButtonPressed()
     */
    @Override
    public void correctionButtonPressed() {
        //user has pressed the back button
        LOG.info("Mocca - Correction Button Pressed");
        
    }

    /* (non-Javadoc)
     * @see at.gv.egiz.smcc.pin.gui.PINGUI#enterPIN(at.gv.egiz.smcc.PinInfo, int)
     */
    @Override
    public void enterPIN(PinInfo pinInfo, int retries) throws CancelledException, InterruptedException {
        //user can begin to enter the pin - not via the applet gui (e.g. pin pad)
        LOG.info("Mocca - Enter Pin");
        enterPINImp(retries);    }

    /* (non-Javadoc)
     * @see at.gv.egiz.smcc.pin.gui.PINGUI#enterPINDirect(at.gv.egiz.smcc.PinInfo, int)
     */
    @Override
    public void enterPINDirect(PinInfo pinInfo, int retries) throws CancelledException, InterruptedException {
        //user can begin to enter the pin - not via the applet gui (e.g. pin pad)
        LOG.info("Moca - Enter Pin Direct");
        enterPINImp(retries);
    }

    private void enterPINImp( int retries){
        
        if (retryCount == -1){
            //first attempt
            retryCount = retries;
            this.setWrongPin(false);
            Event.getInstance().fire(new MoccaPinEntryReady(false));
        }        
        else if (retries >0 && retries == (retryCount-1)){
            //most likely there was a bad PIN - the retry count has decremented
            retryCount = retries;
            //wrong PIN but some attempts left on the card (not clear if retries is always implemented for every card
            this.setWrongPin(true);
            Event.getInstance().fire(new MoccaPinEntryReady(true));
        } else {
            retryCount = retries;
            this.setWrongPin(false);
            Event.getInstance().fire(new MoccaPinEntryReady(false));            
        }
        //could do something to say that the card is blocked (retries = 0)
        
    }
    /* (non-Javadoc)
     * @see at.gv.egiz.smcc.pin.gui.PINGUI#validKeyPressed()
     */
    @Override
    public void validKeyPressed() {
        //user has pressed a digit on the device
        LOG.info("Mocca - Valid Key Pressed");
    }

}
