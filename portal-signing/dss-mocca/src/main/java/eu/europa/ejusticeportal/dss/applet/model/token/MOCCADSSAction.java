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
package eu.europa.ejusticeportal.dss.applet.model.token;

import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.exception.DSSException;
import eu.europa.ec.markt.dss.mocca.MOCCAPrivateKeyEntry;
import eu.europa.ec.markt.dss.mocca.MOCCASignatureTokenConnection;
import eu.europa.ec.markt.dss.signature.DSSDocument;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.token.SignatureTokenConnection;
import eu.europa.ejusticeportal.dss.applet.model.service.PasswordHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SigningContextHome;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.EnumBasedCodeException;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;
/**
 * Signature action for MOCCA.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class MOCCADSSAction extends AbstractDSSAction {

    private static final DssLogger LOG = DssLogger.getLogger(MOCCADSSAction.class.getSimpleName());

    /**
     * Create Mocca connection for the given card.
     */
    public MOCCADSSAction() {
    }

    @Override
    protected SignatureTokenConnection makeConnection() {
        return new MOCCASignatureTokenConnection(getPasswordProvider());
    }
    @Override
    public void setCardProfile(CardProfile cardProfile){
        super.setCardProfile(cardProfile);
        setChosenDigestAlgo(cardProfile.getDigestAlgo());
    }

    @Override
    public List<DSSPrivateKeyEntry> getCertificates() throws CodeException {
        if (!isConnected()) {
            connect();
        }
        sleep();
        try {
            List<DSSPrivateKeyEntry> keys= getToken().getKeys();
            PasswordHome.getInstance().reset();
            return keys;
        } catch (DSSException ex) {
            //Card has been removed
            close();
            ExceptionUtils.exception(new EnumBasedCodeException(ex, MessagesEnum.dss_applet_message_smartcard_removed),
                    LOG);
        }
        return null;
    }

    @Override
    public byte[] delegateSignature(byte[] toDigest, DSSPrivateKeyEntry keyEntry) throws CodeException {
        byte[] bytes = null;
        try {
            MOCCAPrivateKeyEntry key = (MOCCAPrivateKeyEntry)keyEntry;
            MOCCASignatureTokenConnection mocca = (MOCCASignatureTokenConnection) getToken();            
            reInitKey(key);
            bytes =  mocca.sign(toDigest, getChosenDigestAlgo(), keyEntry);
            PasswordHome.getInstance().reset();
            return bytes;
        } catch (NoSuchAlgorithmException ex) {
            //Not raised by DSS in case of MOCCASignatureTokenConnection
            ExceptionUtils.exception(new UnexpectedException("The chosen algorithm does not exist.", ex), LOG);
        } catch (DSSException ex) {
            ExceptionUtils.exception(new UnexpectedException(
                    "Can't use iText to perform sign operation or token can't sign.", ex), LOG);
        } catch (RuntimeException ex) {
            if (PasswordHome.getInstance().getPasswordInputCallback()!=null 
                    &&PasswordHome.getInstance().getPasswordInputCallback().wasCancelled()) {
                // check again if the password dialog was cancelled - this is for MOCCA
                ExceptionUtils.exception(new EnumBasedCodeException(
                        MessagesEnum.dss_applet_message_pin_entry_cancelled_mocca), LOG);
            }

            Throwable cause = ex.getCause();
            if ((cause != null) && (cause.getMessage() != null)) {
                if ((cause.getMessage().contains("does not support signature algorithm"))) {
                    //The true NoSuchAlgorithmException
                    ExceptionUtils.exception(new UnexpectedException("The chosen algorithm does not exist.", ex), LOG);
                } else if ((cause.getMessage().contains("Asked once already"))) {                    
                        //Wrong pin
                        ExceptionUtils.exception(new EnumBasedCodeException(ex, MessagesEnum.dss_applet_message_wrong_pin_mocca),
                            LOG);
                }
            } 
            throw ex;
        }
        return bytes;
    }

    /**
     * @param key
     * @throws NoSuchAlgorithmException
     */
    private void reInitKey(MOCCAPrivateKeyEntry key) throws NoSuchAlgorithmException {
                
        byte [] atr = key.getAtr();
        String s = CardProfile.atrToString(atr);
        DigestAlgorithm da = getChosenDigestAlgo();
        
        if (da == null){
            List<CardProfile> profiles = SigningContextHome.getInstance().getSigningContext().getCardProfiles();
            for (CardProfile profile:profiles){
                if (profile.getAtr()!=null && profile.getAtr().equals(s)){
                    da = (profile.getDigestAlgo());
                }
            }
            if (da == null){
                throw new IllegalStateException("Digest algorithm not known" );
            }            
        }
        setChosenDigestAlgo(da);              
    }

    /**
     * Override the method in order to figure out what the algorithms
     * should be. Note that for MOCCA the digest algorithm MUST be in
     * the card profile store - the supported algorithms are limited
     * by the MOCCA class for the card, so we know the strongest.
     * @throws NoSuchAlgorithmException 
     */
    @Override
    public DSSDocument sign(DSSDocument toBeSigned, DSSPrivateKeyEntry keyEntry) throws CodeException, NoSuchAlgorithmException {
        MOCCAPrivateKeyEntry mKeyEntry = (MOCCAPrivateKeyEntry) keyEntry;
        reInitKey(mKeyEntry);
        return super.sign(toBeSigned, keyEntry);
    }
    
    private void sleep(){
        try {
            //Sometimes it helps to take a break :-|
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
        }
    }
}
