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
import java.security.Provider;
import java.security.Security;
import java.util.List;

import eu.europa.ec.markt.dss.exception.DSSException;
import eu.europa.ec.markt.dss.signature.DSSDocument;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.token.SignatureTokenConnection;
import eu.europa.ejusticeportal.dss.applet.common.JavaSixClassName;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.EnumBasedCodeException;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;


/**
 * Signature action for MS-CAPI
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class NewMSCAPIDSSAction extends AbstractDSSAction {

	private static final String DEFAULT_STORE_NAME="Windows-MY";

    @Override
	public void close() {
		keyEntries = null;
		super.close();
	}

	private static final DssLogger LOG = DssLogger.getLogger(NewMSCAPIDSSAction.class.getSimpleName());
    private int nbCerts = 0;
    private List<DSSPrivateKeyEntry> keyEntries;
    private final String keystoreName;
    /**
     * Create MSCapi connection for the given card.
     *
     * @param cp the profile of the card
     */
    public NewMSCAPIDSSAction(CardProfile cp) {
        this(cp,"Windows-MY");
        
    }

    /**
     * Create MSCAPI connection for the given card and keystore name
     * @param cp the {@link CardProfile}
     * @param keystoreName the name of the MSCAPI keystore
     */
    public NewMSCAPIDSSAction(CardProfile cp, String keystoreName) {
    	
    	this.keystoreName = keystoreName;
    	if (!DEFAULT_STORE_NAME.equals(keystoreName)){    		
    		new NewMSCAPILoadLibraryAction().doExec();
    	}
    	
        setCardProfile(cp);
        setChosenDigestAlgo(cp.getDigestAlgo());
        try {
            Security.addProvider((Provider)Class.forName(JavaSixClassName.DssMscapiProvider.getClassName()).newInstance());
        } catch (Exception e) {
            throw new RuntimeException("Problem loading the provider for the NEWMSCAPI api. Check if it's on your classpath",e);
        } 
        
    }
    /**
     * Perform the connection to the token with MS-CAPI.
     *
     * @return the SignatureTokenConnection (instance of {@link MSCAPISignatureToken})
     */
    @Override
    protected SignatureTokenConnection makeConnection() {
        return new NewMSCAPISignatureToken(keystoreName);    	
    }

    /**
     * Get certificates from the MS-CAPI token.
     *
     * @return A list of certificates retrieved in the MS-CAPI store.
     * @throws CodeException If an identified exception has been raised in the get certificates operation.
     */
    @Override
    public List<DSSPrivateKeyEntry> getCertificates() throws CodeException {
        //Card has been removed previously
        if (!isConnected()) {
            connect();
        }
        try {            
        	if (keyEntries == null) {
             keyEntries =  getToken().getKeys();
        	}
            nbCerts = keyEntries.size();
            return keyEntries;
        } catch (DSSException ex) {
            ExceptionUtils
                    .exception(new UnexpectedException("Unable to retrieve Keys from MS-CAPI Keystore.", ex), LOG);
        }
        return null;
    }

    /**
     * Encapsulation of the common signature operation ({@link AbstractDSSAction}). Detect if the smartcard has been
     * removed for MS-CAPI connection and delegate to the super operation.
     *
     * @param toBeSigned Document to sign.
     * @param keyEntry the entry key that will be used to sign
     * @return the signed {@link Document}
     * @throws CodeException If the smartcard has been removed.
     * @throws NoSuchAlgorithmException 
     */
    @Override
    public DSSDocument sign(DSSDocument toBeSigned, DSSPrivateKeyEntry keyEntry) throws CodeException, NoSuchAlgorithmException {
        final int previousNbCerts = nbCerts;
        if (previousNbCerts > getCertificates().size()) {
            //Card has been removed
            close();
            ExceptionUtils
                    .exception(new EnumBasedCodeException(MessagesEnum.dss_applet_message_smartcard_removed), LOG);
        }
        
        return super.sign(toBeSigned, keyEntry);
    }

    /**
     * Perform the signature for the MS-CAPI token.
     *
     * @param toDigest the stream to sign
     * @param keyEntry the key entry which will sign the stream
     * @return the signature value as a byte array
     */
    @Override
    public byte[] delegateSignature(byte[] toDigest, DSSPrivateKeyEntry keyEntry) {
        try {
            return getToken().sign(toDigest, getChosenDigestAlgo(), keyEntry);
        } catch (DSSException ex) {
            ExceptionUtils.exception(new UnexpectedException("DSS Exception.", ex), LOG);
        } 
        return null;
    }
}
