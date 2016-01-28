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

import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.EncryptionAlgorithm;
import eu.europa.ec.markt.dss.exception.DSSException;
import eu.europa.ec.markt.dss.parameter.SignatureParameters;
import eu.europa.ec.markt.dss.signature.DSSDocument;
import eu.europa.ec.markt.dss.signature.SignatureLevel;
import eu.europa.ec.markt.dss.signature.SignaturePackaging;
import eu.europa.ec.markt.dss.signature.pades.PAdESService;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.token.PasswordInputCallback;
import eu.europa.ec.markt.dss.signature.token.SignatureTokenConnection;
import eu.europa.ec.markt.dss.validation102853.CommonCertificateVerifier;
import eu.europa.ejusticeportal.dss.applet.model.service.FileSeeker;
import eu.europa.ejusticeportal.dss.applet.model.service.PDFHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SignatureInformationHome;
import eu.europa.ejusticeportal.dss.applet.model.service.TokenizedLibraryPath;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.Utils;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Common action for signature.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public abstract class AbstractDSSAction {

    private static final DssLogger LOG = DssLogger.getLogger(AbstractDSSAction.class.getSimpleName());
    /**
     * The password provider.
     */
    private PasswordInputCallback passwordProvider;
    /**
     * The token which handle the connection.
     */
    private SignatureTokenConnection token;
    /**
     * The cardProfile.
     */
    private CardProfile cardProfile;
    /**
     * The absolute path of the p12 file.
     */
    private String p12FilePath;
    /**
     * The absolute path of the library file.
     */
    private String libFilePath;
    /**
     * The chosen algo to perform digest.
     */
    private DigestAlgorithm chosenDigestAlgo;
    /**
     * The chosen algo to perform signature.
     */
    private EncryptionAlgorithm chosenSignAlgo;
    /**
     * The position of the certificates lists in the applet.
     */
    private int posCertificates = 0;
    /**
     * The index of the certificate selected.
     */
    private int indexCertificate = 0;
    /**
     * The index of the reader.
     */
    private int slotIndex = 0;
    /**
     * Define whether or not the token is connected.
     */
    private boolean connected = false;
    
    /**
     * Define whether or not password/PIN entry was cancelled
     */
    private boolean pinEntryCancelled;

    private boolean userProvidedLib;
    
    private SignatureTokenType signatureTokenType;

    /**
     * @see IDSSActionLifeCycle
     */
    public void connect() throws CodeException {
        setPinEntryCancelled(false);
        close();
        setToken(makeConnection());
        if (!isPinEntryCancelled()){
            connected = true;
        }
    }

    /**
     * Perform the connection to the token (smartcard/file).
     * 
     * @return the SignatureTokenConnection
     * @throws CodeException If an identified exception has been raised in the connect operation.
     */
    protected abstract SignatureTokenConnection makeConnection() throws CodeException;

    /**
     * @see IDSSAction
     * @return true if connected
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * @param connected set if the token is actually connected.
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * @see IDSSActionLifeCycle
     */
    public void close() {
        if (connected) {
            getToken().close();
            connected = false;
            pinEntryCancelled = false;
        }
    }

    /**
     * @see IDSSAction
     * @return the list of certificates
     * @throws CodeException
     */
    public abstract List<DSSPrivateKeyEntry> getCertificates() throws CodeException;

    /**
     * Delegate the signature operation to the implementation class.
     * 
     * @param toDigest the input stream to sign
     * @param keyEntry the key entry used for the signature operation
     * @return the signature value
     * @throws CodeException If an identified exception has been raised in the get certificates operation.
     */
    public abstract byte[] delegateSignature(byte[] toDigest, DSSPrivateKeyEntry keyEntry) throws CodeException;

    /**
     * @see IDSSAction
     * @param toBeSigned the Document to be signed
     * @param keyEntry the private key that will sign the document
     * @throws NoSuchAlgorithmException 
     */
    public DSSDocument sign(final DSSDocument toBeSigned, final DSSPrivateKeyEntry keyEntry) throws CodeException, NoSuchAlgorithmException {
    	DSSDocument signedDoc = null;
        try {
            PAdESService service = new PAdESService(new CommonCertificateVerifier(true)){

                @Override
                protected void assertSigningDateInCertificateValidityRange(SignatureParameters parameters) {
                    //we don't do this check here because we want to be able to sign with expired certificates
                }
                
            };
            // Input parameters
            final SignatureParameters parameters = buildSignatureParameters(keyEntry);

            byte [] toDigest = service.getDataToSign(toBeSigned, parameters);
                        
            // Sign            
            final byte[] signatureValue = delegateSignature(toDigest, keyEntry);
            
            signedDoc = service.signDocument(toBeSigned, parameters, signatureValue);
        } catch (DSSException ex) {
            ExceptionUtils.exception(new UnexpectedException("DSS Exception", ex), LOG);
        } 
        return signedDoc;
    }

    /**
     * Builds the signature parameters 
     * @param keyEntry the DssPrivateKeyEntry that we will sign with
     * @return the SignatureParamters
     * @throws NoSuchAlgorithmException
     */
    protected SignatureParameters buildSignatureParameters(final DSSPrivateKeyEntry keyEntry)
            throws NoSuchAlgorithmException {
        final SignatureParameters parameters = new SignatureParameters();
        parameters.setPrivateKeyEntry(keyEntry);
        parameters.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);
        parameters.setSignaturePackaging(SignaturePackaging.ENVELOPED);
        parameters.setSigningCertificate(keyEntry.getCertificate());
        if (keyEntry.getCertificateChain() != null) {
            parameters.setCertificateChain(keyEntry.getCertificateChain());
        }
        parameters.setDigestAlgorithm(chosenDigestAlgo);

        if (chosenDigestAlgo!=null){
            SignatureInformationHome.getInstance().getSignatureEvent().setDigestAlgorithm(chosenDigestAlgo.name());
        }
        if (keyEntry.getEncryptionAlgorithm() != null) {
            SignatureInformationHome.getInstance().getSignatureEvent()
                    .setSignatureAlgorithm(keyEntry.getEncryptionAlgorithm().name());
        } else {
            // For MOCCA, the signature algorithm in the keyEntry is null
            SignatureInformationHome.getInstance().getSignatureEvent()
                    .setSignatureAlgorithm(Utils.getSignatureAlgorithmName(keyEntry.getCertificate().getSigAlgName()));
        }
        parameters.bLevel().setSigningDate(new Date());
        return parameters;
    }

    /**
     * Search for a valid library Path in the given list. A single library path can be configured with a wild card,
     * similar to: "root/sub/filename.extension" where sub, filename and extension can contain one ore more wildcard
     * "*".
     * 
     * @param listLibraryPath the list of Library Path in which the search will be performed.
     * @return a valid library path if exist, null if not.
     */
    public String searchForValidLibrary(List<String> listLibraryPath) {
        String validLibPath = null;
        if (listLibraryPath != null && listLibraryPath.size() > 0) {
            Set<String> filePathFound = FileSeeker.getInstance().search(listLibraryPath);
            if (filePathFound != null && filePathFound.size() > 0) {
                validLibPath = (String) filePathFound.toArray()[0];
            }
        }
        return validLibPath;
    }

    /**
     * Search for a valid library in the specified directory. The directory does not contain any wildcard:
     * /this/is/an/example. The search will be performed according to the libraryName specified in the CardProfile in
     * the directory and all his sub directories.
     * 
     * @param dir The directory in which the search needs to be performed.
     * @return a valid Library file path.
     */
    public String searchForValidLibrary(File dir) {
        String validLibPath = null;
        for (String fileName : getDefaultLibraryName()) {
            Set<String> filePathFound = FileSeeker.getInstance().search(dir.getAbsolutePath() + "/*/" + fileName);
            if (filePathFound != null && filePathFound.size() > 0) {
                validLibPath = (String) filePathFound.toArray()[0];
            }
        }
        return validLibPath;
    }

    /**
     * Get the possible library names for the current token retrieved in the CardProfile.
     * 
     * @return the set of possible library names
     */
    public Set<String> getDefaultLibraryName() {
        HashSet<String> result = new HashSet<String>();
        if (getCardProfile() != null && getCardProfile().getLibraryPath() != null
                && getCardProfile().getLibraryPath().size() > 0) {
            for (String libraryPath : getCardProfile().getLibraryPath()) {
                TokenizedLibraryPath tokenLibPath = new TokenizedLibraryPath();
                tokenLibPath.tokenize(libraryPath);
                result.add(tokenLibPath.getFileName());
            }
        }
        return result;
    }

    /**
     * Get the index of the certificate that will be used for the signing operation.
     * 
     * @return the index.
     */
    public int getIndexCertificate() {
        return indexCertificate;
    }

    /**
     * Set the index of the certificate that will be used for the signing operation.
     * 
     * @param indexCertificate the index
     */
    public void setIndexCertificate(int indexCertificate) {
        this.indexCertificate = indexCertificate;
    }

    /**
     * Get the index of the reader's slot.
     * 
     * @return the index
     */
    public int getSlotIndex() {
        return slotIndex;
    }

    /**
     * Set the index of the reader's slot.
     * 
     * @param slotIndex the index.
     */
    public void setSlotIndex(int slotIndex) {
        this.slotIndex = slotIndex;
    }

    /**
     * @return the cardProfile of the token.
     */
    public CardProfile getCardProfile() {
        return cardProfile;
    }

    /**
     * @param cardProfile the cardProfile to set
     */
    public void setCardProfile(CardProfile cardProfile) {
        this.cardProfile = cardProfile;
    }

    /**
     * @return the position of the token's certificates in the list off all certificates.
     */
    public int getPosCertificates() {
        return posCertificates;
    }

    /**
     * @param posCertificates the position of the token's certificates in the list off all certificates.
     */
    public void setPosCertificates(int posCertificates) {
        this.posCertificates = posCertificates;
    }

    /**
     * Get the path of the p12 file
     * 
     * @return the absolute path of the file.
     */
    public String getP12FilePath() {
        return p12FilePath;
    }

    /**
     * Set the path of the p12 file
     * 
     * @param p12FilePath the absolute path of the file.
     */
    public void setP12FilePath(String p12FilePath) {
        this.p12FilePath = p12FilePath;
    }

    /**
     * Get the path of the library.
     * 
     * @return the absolute path
     */
    public String getLibFilePath() {
        return libFilePath;
    }

    /**
     * Set the path of the library.
     * 
     * @param libFilePath the absolute path to set
     */
    public void setLibFilePath(String libFilePath) {
        this.libFilePath = libFilePath;
    }

    /**
     * @return the digest algorithm
     */
    public DigestAlgorithm getChosenDigestAlgo() {
        return chosenDigestAlgo;
    }

    /**
     * @param chosenDigestAlgo the the digest algorithm to set
     */
    public void setChosenDigestAlgo(DigestAlgorithm chosenDigestAlgo) {
        this.chosenDigestAlgo = chosenDigestAlgo;
    }

    /**
     * @return the signing algorithm
     */
    public EncryptionAlgorithm getChosenSignAlgo() {
        return chosenSignAlgo;
    }

    /**
     * 
     * @param chosenSignAlgo the signing algorithm to set
     */
    public void setChosenSignAlgo(EncryptionAlgorithm chosenSignAlgo) {
        this.chosenSignAlgo = chosenSignAlgo;
    }





    /**
     * @return the token
     */
    public SignatureTokenConnection getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(SignatureTokenConnection token) {
        this.token = token;
    }

    /**
     * @return the passwordProvider
     */
    public PasswordInputCallback getPasswordProvider() {
        return passwordProvider;
    }

    /**
     * @param passwordProvider the passwordProvider to set
     */
    public void setPasswordProvider(PasswordInputCallback passwordProvider) {
        this.passwordProvider = passwordProvider;
    }

    /**
     * Record if the user provided the library for this action
     * 
     * @param b true if the user provided the library
     */
    public void setUserProvidedLib(boolean b) {
        userProvidedLib = b;

    }

    /**
     * 
     * @return true if the user provided the library path
     */
    public boolean getUserProvidedLib() {
        return userProvidedLib;
    }

    /**
     * @return the signatureTokenType
     */
    public SignatureTokenType getSignatureTokenType() {
        return signatureTokenType;
    }

    /**
     * @param signatureTokenType the signatureTokenType to set
     */
    public void setSignatureTokenType(SignatureTokenType signatureTokenType) {
        this.signatureTokenType = signatureTokenType;
    }

    /**
     * @return the pinEntryCancelled
     */
    public boolean isPinEntryCancelled() {
        return pinEntryCancelled;
    }

    /**
     * @param pinEntryCancelled the pinEntryCancelled to set
     */
    public void setPinEntryCancelled(boolean pinEntryCancelled) {
        this.pinEntryCancelled = pinEntryCancelled;
    }

}
