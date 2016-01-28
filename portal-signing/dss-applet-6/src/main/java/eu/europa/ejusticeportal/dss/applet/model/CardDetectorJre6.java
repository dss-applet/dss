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

import eu.europa.ejusticeportal.dss.applet.common.JavaSixClassName;
import eu.europa.ejusticeportal.dss.applet.model.service.FingerprintHome;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.EnumBasedCodeException;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;


/**
 *
 * Detects smartcard device.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 364 $ - $Date: 2012-10-22 09:12:06 +0200 (lun., 22 oct.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class CardDetectorJre6 implements CardDetector, PrivilegedExceptionAction<List<CardProfile>> {

    private static final DssLogger LOG = DssLogger.getLogger(CardDetectorJre6.class.getSimpleName());
    private static CardSupportCheck moccaCardSupportCheck;
    private boolean init = false;
    static 
    {
        try {
            moccaCardSupportCheck = (CardSupportCheck)Class.forName(JavaSixClassName.MoccaCardSupport.getClassName()).newInstance();
        } catch (Exception e) {
            moccaCardSupportCheck = null;
            LOG.info("MOCCA is not available",e);
        }
    }
    @Override
    public List<CardProfile> detectCard() throws CodeException {
        List<CardProfile> listCardDetect = null;
        try {
            listCardDetect = AccessController.doPrivileged(this);
        } catch (PrivilegedActionException ex) {
            ExceptionUtils.exception(new EnumBasedCodeException(ex, MessagesEnum.dss_applet_message_technical_failure),
                    LOG);
        }
        return listCardDetect;
    }

    @Override
    public List<CardProfile> run() throws CardException {
        try {           
            String os = FingerprintHome.getInstance().getFingerprint().getOs();            
            os = os == null?"":os.toLowerCase();           
            if (!init && os.contains("linux") && System.getProperty("sun.security.smartcardio.library") == null) {
                LOG.log(Level.INFO, "Looking for pcsc on Linux");
                File lib = LinuxLibraryFinder.getLibraryPath("pcsclite",FingerprintHome.getInstance().getFingerprint().getArch().contains("64"));                
                System.setProperty("sun.security.smartcardio.library", lib.getAbsolutePath());
                LOG.log(Level.INFO, "Set sun.security.smartcardio.library to "+System.getProperty("sun.security.smartcardio.library"));
            } else {
                LOG.log(Level.INFO, "Not setting sun.security.smartcardio.library");
            }
        } catch (Exception e) {
            LOG.error("PCSC is not available - try setting sun.security.smartcardio.library",e); 
        } finally {
            init = true;
        }
        final List<CardProfile> listCardDetect = new ArrayList<CardProfile>();
        final TerminalFactory terminalFactory = getTerminalFactory();
        
        final List<CardTerminal> listCardTerminal;
        try {
            listCardTerminal = terminalFactory.terminals().list();
        } catch (Exception e){
            //on MacOS and Linux there is an exception when there are no terminals connected
            //but it is OK to continue with the intitalisation - user can connect + refresh 
            //or use PKCS12 or MSCAPI
            LOG.debug("Error listing the terminals",e);
            LOG.info("No terminals found.");
            return listCardDetect;
        }
        int terminalIndex = 0;
        for (CardTerminal cardTerminal : listCardTerminal) {
            //cardTerminal.isCardPresent() always returns false on MacOS, so catch the CardException instead
            try {
                final CardProfile cardProfile = new CardProfile();
                final Card card = cardTerminal.connect("*");
                final ATR atr = card.getATR();
                cardProfile.setAtr(CardProfile.atrToString(atr.getBytes()));
                cardProfile.setTerminalIndex(terminalIndex);
                if (moccaCardSupportCheck!=null) {
                    cardProfile.setApi(moccaCardSupportCheck.isCardSupported(card, cardTerminal)?SignatureTokenType.MOCCA.name():null);
                }
                listCardDetect.add(cardProfile);
                LOG.log(Level.INFO,"Found card in terminal {0} with ATR {1}.", new Object[]{terminalIndex,cardProfile.getAtr()});                             
            } catch (CardException e) {
                //Card not present or unreadable
                LOG.log(Level.INFO, "No card present in terminal {0}, or not readable.", Integer.toString(terminalIndex));
            }
            terminalIndex++;
        }
        
        return listCardDetect;
    }
    public static TerminalFactory getTerminalFactory() {
            return TerminalFactory.getDefault();
    }
    
}
