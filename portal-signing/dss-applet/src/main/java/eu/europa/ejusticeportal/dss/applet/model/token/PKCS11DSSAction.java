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

import eu.europa.ec.markt.dss.exception.DSSException;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.token.PasswordInputCallback;
import eu.europa.ec.markt.dss.signature.token.Pkcs11SignatureToken;
import eu.europa.ec.markt.dss.signature.token.SignatureTokenConnection;
import eu.europa.ejusticeportal.dss.applet.common.OperationCancelledException;
import eu.europa.ejusticeportal.dss.applet.model.service.PasswordHome;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.EnumBasedCodeException;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import sun.security.pkcs11.wrapper.PKCS11Exception;

/**
 * Signature action for PKCS11.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class PKCS11DSSAction extends AbstractDSSAction {

    private static final DssLogger LOG = DssLogger.getLogger(PKCS11DSSAction.class.getSimpleName());

    /**
     * Create Pkcs11 connection for the given card. If no library information is
     * provided in the CardProfile, wait for an input before the connection.
     *
     * @param cp               the profile of the card
     * @param passwordProvider the provider of the password
     */
    public PKCS11DSSAction(CardProfile cp, PasswordInputCallback passwordProvider) {
        setCardProfile(cp);
        setPasswordProvider(passwordProvider);
        setChosenDigestAlgo(cp.getDigestAlgo());
        
    }

    /**
     * Perform the connection to the token with PKCS11.
     *
     * @return the SignatureTokenConnection (instance of {@link RFC3370Pkcs11SignatureToken})
     */
    @Override
    protected SignatureTokenConnection makeConnection() {
        final File libFile = new File(getLibFilePath());
        if (!libFile.exists()) {
            ExceptionUtils
                    .exception(
                            new UnexpectedException(
                                    "The PKCS11 lib must exist on the makeConnection call. The user has removed the file during processing ?"),
                            LOG);
        }
                
        return new Pkcs11SignatureToken(getLibFilePath(), getPasswordProvider(), getSlotIndex());
    }

    /**
     * Get certificates from the PKCS11 token.
     *
     * @return A list of certificates retrieved with the PKCS11 connection.
     * @throws CodeException If a wrong PKCS11 library has been set.
     *                       If a wrong PIN has been provided.
     *                       If the smartcard has been removed.
     */
    @Override
    public List<DSSPrivateKeyEntry> getCertificates() throws CodeException {
        
        if (isPinEntryCancelled()){
            return null;
        }
        //Handle communication failure without refresh
        if (!isConnected()) {
            connect();
        }

        try {
            return getToken().getKeys();
        } catch (DSSException ex) {

            analyseException(ex);
        }
        catch (Exception ex){

            analyseException(ex);
        }
        return null;
    }

    /**
     * Analyses the KeyStoreException
     * @param ex the exception to analyse
     * @throws EnumBasedCodeException representing the analysis of the exception
     */
    private void analyseException(Exception ex) throws EnumBasedCodeException {
        if (ExceptionUtils.getRootCause(ex) instanceof IOException) {
            //this is for when the pre-selected library in card profile was wrong.
            ExceptionUtils.exception(new EnumBasedCodeException(ex,
                    MessagesEnum.dss_applet_message_wrong_pkcs11_lib, getLibFilePath()), LOG);
        } else if (ExceptionUtils.getRootCause(ex) instanceof PKCS11Exception) {
            if (PasswordHome.getInstance().getPasswordInputCallback()!=null 
                    &&PasswordHome.getInstance().getPasswordInputCallback().wasCancelled()){
                LOG.info("PIN entry cancelled - paswordInputCallback.wasCancelled");
                setPinEntryCancelled(true);                
                setConnected(false);
                return;
            } else {
                PKCS11Exception e = (PKCS11Exception)ExceptionUtils.getRootCause(ex);
                if (e.getMessage()!=null && (e.getMessage().contains(CKRCodes.CKR_DEVICE_REMOVED.name()))){
                    ExceptionUtils.exception(new EnumBasedCodeException(ex, MessagesEnum.dss_applet_message_smartcard_removed),
                            LOG);                   
                } else if (e.getMessage()!=null && e.getMessage().contains(CKRCodes.CKR_FUNCTION_CANCELED.name())){
                    LOG.info("PIN entry cancelled - "+CKRCodes.CKR_FUNCTION_CANCELED);
                    setPinEntryCancelled(true);
                    setConnected(false);
                    return;
                }
                else if (e.getMessage()!=null && e.getMessage().contains(CKRCodes.CKR_PIN_INCORRECT.name())){                    
                    setConnected(false);
                    PasswordHome.getInstance().getPasswordInputCallback().setWrongPin(true);
                    return;
                }
            }

        } else if (ExceptionUtils.getRootCause(ex) instanceof OperationCancelledException &&
                PasswordHome.getInstance().wasCancelled()){
                LOG.info("Pin Entry Cancelled - OperationCancelledException");
                setPinEntryCancelled(true);
                setConnected(false);
                return;
        } 
        ExceptionUtils.exception(new EnumBasedCodeException(ex,
                MessagesEnum.dss_applet_message_technical_failure, ex.getMessage()), LOG);
    }

    /**
     * Perform the signature for the PKCS11 token.
     *
     * @param toDigest the stream to sign
     * @param keyEntry the key entry which will sign the stream
     * @return the signature value as a byte array
     * @throws EnumBasedCodeException 
     */
    @Override
    public byte[] delegateSignature(byte[] toDigest, DSSPrivateKeyEntry keyEntry) throws EnumBasedCodeException {
        try {
            return getToken().sign(toDigest, getChosenDigestAlgo(), keyEntry);
        } catch (DSSException ex){
            analyseException(ex);
        }
        return null;
    }
}
