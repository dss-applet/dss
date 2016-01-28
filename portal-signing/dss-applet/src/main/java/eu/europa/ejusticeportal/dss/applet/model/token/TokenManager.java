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

import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ejusticeportal.dss.applet.common.JavaSixClassName;
import eu.europa.ejusticeportal.dss.applet.common.OperationCancelledException;
import eu.europa.ejusticeportal.dss.applet.common.PasswordInputFactory;
import eu.europa.ejusticeportal.dss.applet.event.LibraryUpdated;
import eu.europa.ejusticeportal.dss.applet.event.StatusRefreshed;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.service.FingerprintHome;
import eu.europa.ejusticeportal.dss.applet.model.service.MessageBundleHome;
import eu.europa.ejusticeportal.dss.applet.model.service.PasswordHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SigningContextHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SigningMethodHome;
import eu.europa.ejusticeportal.dss.common.AppletSigningMethod;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.CertificateDisplayDetails;
import eu.europa.ejusticeportal.dss.common.Fingerprint;
import eu.europa.ejusticeportal.dss.common.MessageLevel;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.SigningContext;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.EnumBasedCodeException;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;
import eu.europa.ejusticeportal.dss.mscapi.DssMscapiProvider;

import java.io.File;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Manage the tokens.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision$ - $Date$
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class TokenManager {

    private static final DssLogger LOG = DssLogger.getLogger(TokenManager.class.getSimpleName());

    private static volatile TokenManager instance;

    
    //This map links a certificate to its AbstractDSSAction
    private static Map<String,AbstractDSSAction> certActionMap = new HashMap<String,AbstractDSSAction>();
    private static List<CertificateDisplayDetails> certDisplayDetails = new ArrayList<CertificateDisplayDetails>();

    //Some error messages that indicate PIN entry was cancelled
    private static final List<String> CANCEL_MSGS = Arrays.asList("the action was cancelled by the user", 
                                                                "access was denied because of a security violation", 
                                                                "the operation was canceled by the user");
    //Some Exception class names that indicate PIN entry was cancelled
    private static final List<String> CANCEL_CLASS = Arrays.asList("at.gv.egiz.smcc.CancelledException",
                                                                   "at.gv.egiz.smcc.PINOperationAbortedException");
    
    
    /**
     * 
     * The default constructor for TokenManager.
     */
    private TokenManager() {
    }

    /**
     * Get the single instance of TokenManager
     * 
     * @return the instance
     */
    public static TokenManager getInstance() {
        if (null == instance) {
            instance = new TokenManager();
        }
        return instance;
    }

    private PasswordInputFactory passwordInputFactory;

    /**
     * Should be called at the initialisation of the application to define the PasswordInputFactory
     * 
     * @param passwordInputFactory the passwordInput factory
     */
    public void init(PasswordInputFactory passwordInputFactory) {
        this.passwordInputFactory = passwordInputFactory;
    }

    /**
     * Current connections to Smart Cards or PKCS12.
     */
    private List<AbstractDSSAction> tokens = Collections.synchronizedList(new ArrayList<AbstractDSSAction>());


    /**
     * Get the list of the current tokens.
     * 
     * @return
     */
    public List<AbstractDSSAction> getTokens() {
        synchronized(tokens){
            return Collections.unmodifiableList(tokens);
        }
    }

    /**
     * Called by the applet to refresh the token connections according to the signing context.
     * 
     * @throws CodeException
     */
    public void refreshTokens() throws CodeException {
        certActionMap.clear();
        certDisplayDetails.clear();
        closeTokenConnections();        
        AppletSigningMethod m = SigningMethodHome.getInstance().getSigningMethod();
        if (m.equals(AppletSigningMethod.sc)||m.equals(AppletSigningMethod.installed_cert)) {
            setTokenConnections(SigningContextHome.getInstance().getSigningContext());
        }
    }

    /**
     * Close the connection for each token.
     */
    public void closeTokenConnections() {

        synchronized (tokens){
        for (AbstractDSSAction ada : tokens) {
            if (ada.isConnected()) {
                LOG.log(Level.FINE, "Closing token connection for {0}", ada.getCardProfile().getCardDescription());
                ada.close();
            }
        }
        tokens.clear();
        }
    }

    /**
     * Set the token connection along with the given Signing Context.
     * 
     * @param sc the signing context
     * @throws CodeException if an error occurred when trying to connect to a token.
     */
    private void setTokenConnections(SigningContext sc) throws CodeException {
        LOG.log(Level.FINE,"Setting token connections");
        synchronized (tokens){
        final Fingerprint fingerPrint = FingerprintHome.getInstance().getFingerprint();
        boolean alreadyHasMscapi = false;
        boolean alreadyHasMocca = false;
        for (CardProfile cp : sc.getCardProfiles()) {
            if (cp.getApi() == null) {
                continue;
            }
            LOG.log(Level.FINE, "Open connection for {0}\nAPI : {1}",
                    new Object[] { cp.getCardDescription(), cp.getApi() });
            AbstractDSSAction ada = null;
            switch (cp.getApi()) {
            case PKCS11:                
                ada = setTokenConnectionPkcs11(cp);
                break;
            case MSCAPI:
                ada = new MSCAPIDSSAction(cp);
                ada.connect();
                break;
            case NEWMSCAPI:
                if (!alreadyHasMscapi) {
                    alreadyHasMscapi = true;
                    
                    ada = new NewMSCAPIDSSAction(cp,DssMscapiProvider.KEYSTORE_ID);
                    ada.connect();
                    break;
                } else {
                    // It's enough to have only one MSCAPI connection - it gets all certs from all cards
                    break;
                }
            case MOCCA:
                if (Fingerprint.getJreVersion(fingerPrint) < 1.6) {
                    ExceptionUtils.shouldNotBeInvoked();
                    break;
                }
                if (!alreadyHasMocca && 
                		AppletSigningMethod.sc.equals(SigningMethodHome.getInstance().getSigningMethod())){
                    alreadyHasMocca = true;
                    ada = setTokenConnectionMocca(cp);
                } else {
                    break;
                }
            default:
                break;
            }
            if (ada != null) {
                ada.setSignatureTokenType(cp.getApi());
                tokens.add(ada);
                LOG.log(Level.FINE, "Connection opened successfully for {0}\nAPI : {1}\nSlot: {2}", new Object[] {
                        cp.getCardDescription(), cp.getApi(), ada.getSlotIndex() });
            }
        }
        for (AbstractDSSAction ada:tokens){
            if (ada.isPinEntryCancelled()){
                Event.getInstance().fire(new StatusRefreshed(MessagesEnum.dss_applet_message_pin_entry_cancelled, MessageLevel.INFO));
                break;
            }
        }
        }
    }
    /**
     * Makes the token connection for MOCCA
     * @param cp the CardProfile
     * @return the AbstractDSSAction connection for the Card or null if not supported
     * @throws CodeException for managed exceptions
     */
    private AbstractDSSAction setTokenConnectionMocca(CardProfile cp) {
        AbstractDSSAction ada = null;
        try {        
            ada = (AbstractDSSAction) Class.forName(JavaSixClassName.MOCCADSSAction.getClassName())
                    .newInstance();
            ada.setPasswordProvider(passwordInputFactory.getPasswordInput(SignatureTokenType.MOCCA,MessageBundleHome.getInstance()
                    .getMessage(MessagesEnum.dss_applet_message_why_password_card_mocca.name())));
            ada.setCardProfile(cp);
            ada.connect();
        } catch (Exception ex) {
            // The user can't do anything about this
            ExceptionUtils.exception(new UnexpectedException(
                    "Can't load or instantiate classes related to MOCCA connection.", ex,
                    JavaSixClassName.MOCCADSSAction.getClassName()), LOG);
        }
        return ada;
    }
    /**
     * Makes the token connection for PKCS11
     * @param cp the CardProfile
     * @return the AbstractDSSAction connection for the Card
     * @throws CodeException for managed exceptions
     */
    private AbstractDSSAction setTokenConnectionPkcs11(CardProfile cp) throws CodeException {
        AbstractDSSAction ada;
        ada = new PKCS11DSSAction(cp, passwordInputFactory.getPasswordInput(SignatureTokenType.PKCS11,MessageBundleHome.getInstance()
                .getMessage(MessagesEnum.dss_applet_message_why_password_card.name())));
        String validLibPath = ada.searchForValidLibrary(cp.getLibraryPath());
        if (validLibPath != null) {            
             ada.setSlotIndex(cp.getTerminalIndex());
             ada.setLibFilePath(validLibPath);
             ada.connect();
        }
        return ada;
    }

    /**
     * Refresh the PKCS12 token with the specified filepath.
     * 
     * @param filePath
     * @throws CodeException
     */
    public void refreshPKCS12Token(final String filePath) throws CodeException {
        synchronized (tokens){
        tokens.clear();
        AbstractDSSAction ada = new PKCS12DSSAction(filePath,
                passwordInputFactory.getPasswordInput(SignatureTokenType.PKCS12,MessageBundleHome.getInstance().getMessage(
                        MessagesEnum.dss_applet_message_why_password_pkcs12.name())));
        ada.setSignatureTokenType(SignatureTokenType.PKCS12);
        try {
            ada.connect();
            tokens.add(ada);
            Event.getInstance().fire(new StatusRefreshed(MessagesEnum.dss_applet_message_pkcs12_path, MessageLevel.INFO,filePath));
        } catch (OperationCancelledException e){
            LOG.info("PIN entry cancelled - refreshPKCS12Token");
            Event.getInstance().fire(new StatusRefreshed(MessagesEnum.dss_applet_message_pin_entry_cancelled,MessageLevel.INFO));
        }
        }
    }

    /**
     * Refresh all the PKCS11 instance with specified filepath.
     * 
     * @param file
     * @throws CodeException
     */
    public void refreshPKCS11Tokens(final File file) throws CodeException {
        CodeException exceptionToRaise = null;
        boolean success = false;
        synchronized (tokens){
        for (AbstractDSSAction ada : tokens) {
            if (ada.getCardProfile().getApi() == SignatureTokenType.PKCS11) {
                /*
                 * When several tokens are waiting for a pkcs11, only one can be satisfied. So if it works for one
                 * PKCS11, there is no "visible" exception. If it failed for all of them, the most accurate exception is
                 * raised.
                 */
                if (file.isDirectory()) {
                    String validLibFilePath = ada.searchForValidLibrary(file);
                    if (validLibFilePath != null) {
                        ada.setLibFilePath(validLibFilePath);
                    } else {
                        ExceptionUtils.exception(new EnumBasedCodeException(
                                MessagesEnum.dss_applet_message_wrong_pkcs11_lib), LOG);
                    }
                } else {
                    ada.setLibFilePath(file.getAbsolutePath());
                }
                
                ada.connect();
                if (!ada.isConnected()){
                    continue;
                }
                
                try {
                    if (ada.getCertificates() == null && PasswordHome.getInstance().getPasswordInputCallback().wasCancelled()) {
                        LOG.info("PIN entry cancelled - refreshPKCS11Tokens");
                        ExceptionUtils.exception(new EnumBasedCodeException(new Exception("PIN Entry Cancelled"),
                                MessagesEnum.dss_applet_message_pin_entry_cancelled), LOG);
                    }
                    success = true;
                    ada.setUserProvidedLib(true);
                    Event.getInstance().fire(new LibraryUpdated(ada.getLibFilePath()));
                    break;
                } catch (CodeException ex) {
                    ada.setUserProvidedLib(false);
                    /*
                     * Wrong pin is the most accurate Exception considered here, because this means that the lib file is
                     * correct for the given token.
                     */
                    if (exceptionToRaise == null
                            || !exceptionToRaise.getCode().equals(MessagesEnum.dss_applet_message_wrong_pin.name())) {
                        exceptionToRaise = ex;
                    }
                }
            }
        }
        if ((!success) && (exceptionToRaise != null)) {
            ExceptionUtils.exception(exceptionToRaise, LOG);
        }
        }        
    }

    
    /**
     * Get the display names of all the certificates of all the connected tokens.
     * The method filters the certificates in order not to return certificates detected by MSCAPI 
     * if the same certificate was detected by other means.
     * @return the list of names.
     * @throws CodeException
     */
     public List<CertificateDisplayDetails> getCertificateNames() throws CodeException {        
        int index = 0;
        AppletSigningMethod m = SigningMethodHome.getInstance().getSigningMethod();
        synchronized (tokens) {
            
        
        for (AbstractDSSAction ada : tokens) {
            ada.setPosCertificates(index);
            if (!ada.isConnected()||ada.isPinEntryCancelled()) {                
                continue;
            } else if (m.equals(AppletSigningMethod.p12) && !(ada instanceof PKCS12DSSAction)) {
                continue;
            }
            if (ada.getCertificates() == null 
            	&& PasswordHome.getInstance().getPasswordInputCallback()!=null
            	&& PasswordHome.getInstance().getPasswordInputCallback().wasCancelled()) {
                LOG.info("PIN entry cancelled - getCertificateNames");
            }
            if (ada.getCertificates() != null) {
                for (DSSPrivateKeyEntry keyEntry : ada.getCertificates()) {
                        index++;
                        CertificateDisplayDetails n = CertificateDisplayUtils.getDisplayDetails(keyEntry,ada.getCardProfile());
                        AbstractDSSAction a = certActionMap.get(n.getCertificateHash());
                        if (a==null) {
                            certDisplayDetails.add(n);
                        }
                        if (a!=null && a.getSignatureTokenType().equals(SignatureTokenType.MSCAPI) 
                                    && ada.getSignatureTokenType().equals(SignatureTokenType.PKCS11)){
                            //MSCAPI will be replaced with p11 because either it was defined in the card store
                            //or the user has chosen the library.
                            certActionMap.put(n.getCertificateHash(), ada);           
                        } else if (a == null){
                            certActionMap.put(n.getCertificateHash(), ada);
                        } else if (a.getSignatureTokenType().equals(SignatureTokenType.MOCCA)){
                            certActionMap.put(n.getCertificateHash(), ada);
                            //Could maybe instead add list of ada to the map and try all? MOCCA sometimes is more reliable.
                            //But probably too much user interaction (failed signature, multiple prompts for PW in different styles...) 
                        } 
                       //don't replace P11 with MSCAPI - see if clause above for reason.

                }
            }
        }
        }

        CertificateDisplayUtils.prepare(certDisplayDetails, SigningMethodHome.getInstance().getSigningMethod());

        return certDisplayDetails;
    }
     
    
    /**
     * Gets the {@link CertificateDisplayDetails} for a certificate identified by its hash
     * @param certHash the hash
     * @return the CertificateDisplayDetails
     * @throws EnumBasedCodeException if the card was removed.
     */
	public CertificateDisplayDetails getCertificateDisplayName(String certHash) throws EnumBasedCodeException{
        for (CertificateDisplayDetails cdd:certDisplayDetails){
            if (cdd.getCertificateHash().equals(certHash)){
                return cdd;
            }
        }
        ExceptionUtils.throwException(new EnumBasedCodeException(MessagesEnum.dss_applet_message_smartcard_removed), LOG);
        return null;
    }


    
  
	/**
     * Get the AbstractDSSAction instance corresponding to the hash of the selected certificate.
     * Pre-condition - getCertificateNames must be called to set up the the certificates/actions 
     * @param lada the list of AbstractDSSAction
     * @param certHash the hash of the selected certificate
     * @return the AbstractDSSAction instance
     * @throws EnumBasedCodeException 
     */
    public static AbstractDSSAction getInstanceFromCertificate(List<AbstractDSSAction> lada, String certHash) throws CodeException {
        AbstractDSSAction a = certActionMap.get(certHash);
        if (a==null){
            ExceptionUtils.throwException(new EnumBasedCodeException(MessagesEnum.dss_applet_message_smartcard_removed), LOG);
        }
        return a;
    }

    

     /**
     * Analyse a signature exception and decide if the exception was because the user cancelled
     * @param throwable the exception to analyse
     * @return true if we think it was a cancellation; note that if we are wrong, it's OK - the document
     * was not processed.
     */
    public static boolean wasCancelled(Throwable throwable) {
        boolean cancelled = false;
        final Throwable root = ExceptionUtils.getRootCause(throwable);
        if (root instanceof OperationCancelledException||throwable instanceof OperationCancelledException) {            
            cancelled = true;
        }
        else if (root instanceof SignatureException){
            String msg = throwable.getCause().getMessage()!=null?throwable.getCause().getMessage():throwable.getCause().getLocalizedMessage();        
            if (msg!=null){
                msg = msg.toLowerCase();                 
                for (String s:CANCEL_MSGS){
                    if (msg.contains(s)){
                        cancelled = true;
                    }
                }
            }
        }  else {
            String className = root.getClass().getName();
            for (String s:CANCEL_CLASS){
                if (className.equals(s)){
                    cancelled = true;
                }
            }               
        }
        return cancelled;
    }
}
