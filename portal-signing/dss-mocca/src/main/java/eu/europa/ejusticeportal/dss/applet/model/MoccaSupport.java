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
package eu.europa.ejusticeportal.dss.applet.model;

import javax.smartcardio.Card;
import javax.smartcardio.CardTerminal;

import at.gv.egiz.smcc.CardNotSupportedException;
import at.gv.egiz.smcc.SignatureCard;
import at.gv.egiz.smcc.SignatureCardFactory;
/**
 * Test if a card is supported by Mocca
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1071 $ - $Date: 2013-03-04 11:57:27 +0100 (Mon, 04 Mar 2013) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class MoccaSupport implements CardSupportCheck {
    /**
     * Check if the given card is supported by MOCCA
     * @param card the card
     * @param terminal the terminal
     * @return true if supported by MOCCA
     */
    @Override
    public boolean isCardSupported(Card card, CardTerminal terminal){
     
        boolean mocca = false;
        SignatureCard sc = null;
        try {
            sc = SignatureCardFactory.getInstance().createSignatureCard(card, terminal);
            
            sc.disconnect(true);
            mocca = true;
        } catch (CardNotSupportedException e){
            mocca = false;
        } finally {
            if (sc!=null){
                //disconnect and reset
                sc.disconnect(true);
            }
        }
        return mocca;
    }
    
}
