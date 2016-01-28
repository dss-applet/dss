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


import eu.europa.ec.markt.dss.exception.DSSBadPasswordException;
import eu.europa.ec.markt.dss.exception.DSSException;
import eu.europa.ec.markt.dss.parameter.SignatureParameters;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.token.PasswordInputCallback;
import eu.europa.ec.markt.dss.signature.token.Pkcs12SignatureToken;
import eu.europa.ec.markt.dss.signature.token.SignatureTokenConnection;
import eu.europa.ejusticeportal.dss.applet.model.service.PasswordHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SignatureInformationHome;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.EnumBasedCodeException;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;

import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;

/**
 * Signature action for PKCS12.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class PKCS12DSSAction extends AbstractDSSAction {

    private static final DssLogger LOG = DssLogger.getLogger(PKCS12DSSAction.class.getSimpleName());

    /**
     * Create Pkcs12 connection for the file path.
     *
     * @param filePath         the local path of the pkcs12 file
     * @param passwordProvider the provider of the password
     */
    public PKCS12DSSAction(String filePath, PasswordInputCallback passwordProvider) {
        setP12FilePath(filePath);
        setPasswordProvider(passwordProvider);
        CardProfile cp = new CardProfile();
        cp.setApi(SignatureTokenType.PKCS12.name());
        setCardProfile(cp);        
    }

    /**
     * Perform the connection to the token with PKCS12. (Try a connection without password, and if does not work, try a
     * connection with a Password provider).
     *
     * @return the SignatureTokenConnection (instance of {@link RFC3370Pkcs12SignatureToken})
     */
    @Override
    protected SignatureTokenConnection makeConnection() {
        try {
            //Attempt to get certificates without password
            Pkcs12SignatureToken attempt = new Pkcs12SignatureToken("", getP12FilePath());
            attempt.getKeys();
            return attempt;
        } catch (Exception ex) {
            LOG.log(Level.INFO,"Attempt to open the certificate file without a password failed with error {0}.", new Object[]{ex.getMessage()});
        }
        boolean success = false;
        SignatureTokenConnection c = null;
        while (!success) {
            try {
                c = new Pkcs12SignatureToken(getPasswordProvider().getPassword(), getP12FilePath());
                c.getKeys();
                success = true;
            } catch (DSSBadPasswordException e){
                success = false;
            }
        }
        return c;
    }

    /**
     * Get certificates from the PKCS12 token.
     *
     * @return A list of certificates retrieved in the PKCS12 file.
     * @throws CodeException If a wrong PKCS12 file has been provided.
     *                       If a wrong PIN has been provided.
     */
    @Override
    public List<DSSPrivateKeyEntry> getCertificates() throws CodeException {
        try {
            return getToken().getKeys();
        } catch (DSSException ex) {
            ExceptionUtils.exception(new EnumBasedCodeException(ex, MessagesEnum.dss_applet_message_wrong_pkcs12_file), LOG);
        } catch (DSSBadPasswordException ex) {
            if (PasswordHome.getInstance().getPasswordInputCallback()!=null){
                PasswordHome.getInstance().getPasswordInputCallback().setWrongPin(true);
            }
            ExceptionUtils.exception(new EnumBasedCodeException(ex, MessagesEnum.dss_applet_message_wrong_pin), LOG);
        }
        return null;
    }

    /**
     * Perform the signature for the PKCS12 token.
     *
     * @param toDigest the stream to sign
     * @param keyEntry the key entry which will sign the stream
     * @return the signature value as a byte array
     */
    @Override
    public byte[] delegateSignature(byte[] toDigest, DSSPrivateKeyEntry keyEntry) {
        try {            
            setChosenSignAlgo(keyEntry.getEncryptionAlgorithm());
            setChosenDigestAlgo(PKCS12SignatureAlgorithm.getInstance().getDigestAlgorithm(getChosenSignAlgo()));
            SignatureInformationHome.getInstance().getSignatureEvent().setDigestAlgorithm(getChosenDigestAlgo().getName());
            return getToken().sign(toDigest, getChosenDigestAlgo(), keyEntry);
        } catch (DSSException ex) {
            ExceptionUtils.exception(new UnexpectedException("DSS Exception.", ex), LOG);
        } 
        return null;
    }

    /* (non-Javadoc)
     * @see eu.europa.ejusticeportal.dss.applet.model.token.AbstractDSSAction#buildSignatureParameters(eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry)
     */
    @Override
    protected SignatureParameters buildSignatureParameters(DSSPrivateKeyEntry keyEntry)
            throws NoSuchAlgorithmException {
        setChosenSignAlgo(keyEntry.getEncryptionAlgorithm());        
        setChosenDigestAlgo(PKCS12SignatureAlgorithm.getInstance().getDigestAlgorithm(keyEntry.getEncryptionAlgorithm()));
        return super.buildSignatureParameters(keyEntry);        
    }
}
