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
package eu.europa.ejusticeportal.dss.applet.model.action;

import eu.europa.ec.markt.dss.signature.DSSDocument;
import eu.europa.ec.markt.dss.signature.InMemoryDocument;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ejusticeportal.dss.common.AbstractPrivilegedExceptionAction;
import eu.europa.ejusticeportal.dss.applet.event.ActivateCardTerminalWatcher;
import eu.europa.ejusticeportal.dss.applet.event.LoadingRefreshed;
import eu.europa.ejusticeportal.dss.applet.event.SigningError;
import eu.europa.ejusticeportal.dss.applet.event.StatusRefreshed;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.service.AsynchronousCallerHome;
import eu.europa.ejusticeportal.dss.applet.model.service.PDFHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SignatureInformationHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SigningContextHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SigningMethodHome;
import eu.europa.ejusticeportal.dss.applet.model.token.AbstractDSSAction;
import eu.europa.ejusticeportal.dss.applet.model.token.CertificateDisplayUtils;
import eu.europa.ejusticeportal.dss.applet.model.token.TokenManager;
import eu.europa.ejusticeportal.dss.applet.model.token.UnknownTokenSignature;
import eu.europa.ejusticeportal.dss.applet.server.AsynchronousServerCall;
import eu.europa.ejusticeportal.dss.applet.server.ServerCall;
import eu.europa.ejusticeportal.dss.common.AppletSigningMethod;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.CertificateDisplayDetails;
import eu.europa.ejusticeportal.dss.common.KeyUsage;
import eu.europa.ejusticeportal.dss.common.MessageLevel;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.ServerCallId;
import eu.europa.ejusticeportal.dss.common.SignatureEvent;
import eu.europa.ejusticeportal.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.EnumBasedCodeException;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.factory.SignedPDFFactory;

import java.util.List;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;

/**
 *
 * Action with elevated privileged to sign the SealedPdf on the client computer.
 * It also automatically sends the Signed Pdf to the server.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision$ - $Date$
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class SigningAction extends AbstractPrivilegedExceptionAction {

    private static final DssLogger LOG = DssLogger.getLogger(SigningAction.class.getSimpleName());
    private String certHash;

    /**
     * Execute the SigningAction with elevated privilege.
     *
     */
    protected void doExec() throws CodeException {
        LOG.log(Level.FINE, "Signing with #{0}", certHash);
        final SignatureEvent sigLog = SignatureInformationHome.getInstance().getSignatureEvent();
        CodeException exceptionToThrow = null;
        try {
            CertificateDisplayDetails cd = TokenManager.getInstance().getCertificateDisplayName(certHash);
            if (cd!=null) {
                sigLog.setExtensions(cd.getExtensions());
                sigLog.setRecommended(Boolean.toString(cd.isRecommended()));
            }
            final AbstractDSSAction ada = getDSSActionInstance();    
            final DSSPrivateKeyEntry keyEntry = getKeyEntry(ada);
            collectInformation(ada);           
            sigLog.setApi(ada.getSignatureTokenType().name());
            sigLog.setKeyUsage(KeyUsage.decodeToString(keyEntry.getCertificate().getKeyUsage()));
            sigLog.setSigningMethod(SigningMethodHome.getInstance().getSigningMethod().name());
            
            LOG.log(Level.FINE, "Position of the certs list for token is : {0}", ada.getPosCertificates());
            LOG.log(Level.INFO, "Sign the document, connection is instance of {0}", ada.getClass().getSimpleName());
            LOG.log(Level.FINE, "Signing internal with #{0}", ada.getIndexCertificate());
            final DSSDocument toBeSigned = new InMemoryDocument(PDFHome.getInstance().getSealedPdf());
            DSSDocument signedDocument;
            AppletSigningMethod m = SigningMethodHome.getInstance().getSigningMethod();
            if ((AppletSigningMethod.sc.equals(m)||AppletSigningMethod.installed_cert.equals(m))
                      && (ada.getChosenDigestAlgo() == null )) {
                signedDocument = UnknownTokenSignature.getInstance().signWithSeveralDigestAlgo(ada, keyEntry,
                        toBeSigned);
            } else {
                signedDocument = ada.sign(toBeSigned, keyEntry);
            }       
            sigLog.setSigned(true);
            Event.getInstance().fire(new StatusRefreshed(MessagesEnum.dss_applet_message_signed,MessageLevel.INFO,TokenManager.getInstance().getCertificateDisplayName(certHash).getName()));
            
            //Turn off the card watcher service because the user might pull out his card at this point
            Event.getInstance().fire(new ActivateCardTerminalWatcher(false));
            //Output
            PDFHome.getInstance().setSignedPdf(IOUtils.toByteArray(signedDocument.openStream()));
            AsynchronousCallerHome.getInstance().getCaller().fire(
                    new ServerCall(AsynchronousServerCall.callServer, ServerCallId.uploadSignedPdf, SignedPDFFactory.get(PDFHome.getInstance()
                            .getSignedPdf())));
        } 
        catch (CodeException e){ 
            exceptionToThrow = handleSignatureErrorEvent(sigLog, e);            
        }
        catch (Exception e){
            LOG.log(Level.SEVERE, "error in signing\n {0}", ExceptionUtils.getStackTrace(e));
            exceptionToThrow = handleSignatureErrorEvent(sigLog, e);                                               
        }
        
        if (exceptionToThrow !=null){
            ExceptionUtils.exception(exceptionToThrow, LOG);
        }
    }

    private CodeException handleSignatureErrorEvent(SignatureEvent sigLog, Exception e){
        CodeException exceptionToThrow;
        sigLog.setSigned(false);
        if (e instanceof CodeException &&!((CodeException) e).getCode().equals(MessagesEnum.dss_applet_message_technical_failure.name())){
            sigLog.setErrorDescription(((CodeException) e).getCode());
            AsynchronousCallerHome.getInstance().getCaller().fire(
                    new ServerCall(AsynchronousServerCall.callServer, ServerCallId.logStatistics, SignatureInformationHome.getInstance()
                    .getSignatureEvent()));
            exceptionToThrow = (CodeException)e; 
        } else if ((TokenManager.wasCancelled(e))){
            exceptionToThrow = null;
            sigLog.setErrorDescription(MessagesEnum.dss_applet_message_pin_entry_cancelled.name());
            AsynchronousCallerHome.getInstance().getCaller().fire(
                    new ServerCall(AsynchronousServerCall.callServer, ServerCallId.logStatistics, SignatureInformationHome.getInstance()
                    .getSignatureEvent()));
            Event.getInstance().fire(new StatusRefreshed(MessagesEnum.dss_applet_message_pin_entry_cancelled, MessageLevel.INFO));
            Event.getInstance().fire(new LoadingRefreshed(false, false));
        }
        else {
            //set the description here - it will be replaced with the stack trace if the user agrees to provide it
            if (e instanceof CodeException){
                sigLog.setErrorDescription(((CodeException) e).getCode());                                
            }
            else {
                sigLog.setErrorDescription(e.getClass().getSimpleName());
            }            
            Event.getInstance().fire(new SigningError(ExceptionUtils.getStackTrace(e)));
            exceptionToThrow = new EnumBasedCodeException(e,MessagesEnum.dss_applet_message_technical_failure);
        }
        return exceptionToThrow;
    }


    /**
     * Collect information on the DSSAction for statistical purpose.
     *
     * @param ada the instance of DSSAction
     */
    private void collectInformation(AbstractDSSAction ada) {
        
        
        SignatureEvent sigLog = SignatureInformationHome.getInstance().getSignatureEvent();
        CardProfile cp = ada.getCardProfile();
        if (cp!=null){
            sigLog.setApi(ada.getSignatureTokenType().name());
            sigLog.setAtr(cp.getAtr());
        }
        sigLog.setDetectedCardCount(SigningContextHome.getInstance().getSigningContext().getDetectedCardCount());
        if(cp!=null && (cp.getApi() == SignatureTokenType.MOCCA || cp.getApi() == SignatureTokenType.PKCS12)){
            sigLog.setNeedsUserInput(false);
            return;
        }
        //signature info is relevant when the user has used a smart card 
        //that was not in the data store
        if (cp!=null && cp.getAtr()!=null && cp.getAtr().length()>0){
            sigLog.setNeedsUserInput((cp.getCardDescription()==null || SigningContextHome.CARD_UNKNOWN_MSCAPI.equals(cp.getCardDescription()) || SigningContextHome.CARD_UNKNOWN_PKCS11.equals(cp.getCardDescription())||cp.getCardDescription().length()==0||cp.isSynthetic()));
        }
        //signature info is relevant when the user told us where the library is
        if (ada.getUserProvidedLib()){
            sigLog.setNeedsUserInput(true);
        }
                                
        if (ada.getUserProvidedLib()){
            sigLog.setUserSuppliedPkcs11Path(ada.getLibFilePath());    
        }
        
    }

    /**
     * Get the DSSAction instance that will be used for the SigningAction
     *
     * @return the DSSAction instance
     * @throws CodeException 
     */
    public AbstractDSSAction getDSSActionInstance() throws CodeException {
        return TokenManager.getInstanceFromCertificate(TokenManager.getInstance().getTokens(), certHash);
    }

    /**
     * Get the selected private key.
     *
     * @param ada the DSSAction instance
     * @return The selected key entry
     * @throws CodeException
     */
    public DSSPrivateKeyEntry getKeyEntry(AbstractDSSAction ada) throws CodeException {
        
        List<DSSPrivateKeyEntry> keys = ada.getCertificates();
        DSSPrivateKeyEntry k = null;
        for (DSSPrivateKeyEntry key:keys){
            if (CertificateDisplayUtils.digest(key.getCertificate()).equals(certHash)){
                k = key;
                break;
            }
        }
        if (k == null){
            ExceptionUtils.throwException(new EnumBasedCodeException(MessagesEnum.dss_applet_message_smartcard_removed), LOG);
        }
        return k;
    }

    /**
     * Default constructor for SigningAction
     *
     * @param certHash the hash of the selected certificate.
     */
    public SigningAction(String certHash) {
        this.certHash = certHash;
    }
}
