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
import eu.europa.ec.markt.dss.signature.DSSDocument;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ejusticeportal.dss.applet.common.OperationCancelledException;
import eu.europa.ejusticeportal.dss.applet.model.service.AsynchronousCallerHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SignatureInformationHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SigningContextHome;
import eu.europa.ejusticeportal.dss.applet.server.AsynchronousServerCall;
import eu.europa.ejusticeportal.dss.applet.server.ServerCall;
import eu.europa.ejusticeportal.dss.common.ServerCallId;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;

/**
 * Manage the signature operation for unknown Token. It will try several digest algorithm to perform the signing
 * operation.
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision$ - $Date$
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public final class UnknownTokenSignature {

    private static final DssLogger LOG = DssLogger.getLogger(UnknownTokenSignature.class.getSimpleName());
    /**
     * Singleton reference of UnknownTokenSignature.
     */
    private static UnknownTokenSignature instance;

    /**
     * Constructs singleton instance of UnknownTokenSignature.
     */
    private UnknownTokenSignature() {
    }

    /**
     * Provides reference to singleton getClass() of UnknownTokenSignature.
     *
     * @return Singleton instance of UnknownTokenSignature.
     */
    public static UnknownTokenSignature getInstance() {
        if (instance == null) {
            instance = new UnknownTokenSignature();
        }
        return instance;
    }

    /**
     * Sign with all Digest algorithm available. The same keyEntry is used so it will ask for a PIN only once.
     *
     * @param ada        the signer object
     * @param keyEntry   the key entry that will be used to sign
     * @param toBeSigned the document that will be signed
     * @return the signed Document
     * @throws CodeException
     * @throws NoSuchAlgorithmException 
     */
    public DSSDocument signWithSeveralDigestAlgo(AbstractDSSAction ada, final DSSPrivateKeyEntry keyEntry,
            final DSSDocument toBeSigned) throws CodeException, NoSuchAlgorithmException {
    	DSSDocument ret = null;
        boolean success = false;


        List<DigestAlgorithm> algosToTry = SigningContextHome.getInstance().getDefaultAlgorithms();

        for (DigestAlgorithm algo : algosToTry) {
            LOG.log(Level.INFO, "Unknown card - trying to sign with algorithm {0}.", algo.name());
            ada.setChosenSignAlgo(keyEntry.getEncryptionAlgorithm());
            ada.setChosenDigestAlgo(algo);
            try {
                ret = ada.sign(toBeSigned, keyEntry);
                success = true;
            } 
            catch (Exception ex) {
                if (TokenManager.wasCancelled(ex)){
                    throw new OperationCancelledException(ex);
                } else {
                    LOG.error("Error attempting to sign",ex);
                    LOG.info("Unknown Card: Error signing with {0}, {1}:{2}.", new Object[]{algo.name(), keyEntry.getEncryptionAlgorithm().name(), ExceptionUtils.getRootCause(ex).getMessage()});
                }
                
            } finally {
                if (!success) {
                    AsynchronousCallerHome.getInstance().getCaller().fire(
                            new ServerCall(AsynchronousServerCall.callServer, ServerCallId.logStatistics, SignatureInformationHome.getInstance()
                            .getSignatureEvent()));
                    ada.setChosenDigestAlgo(null);
                    ada.setChosenSignAlgo(null);
                } else {
                    break;
                }
                
            }
           
        }
        if (!success) {
            ExceptionUtils.exception(new UnexpectedException("The applet is not able to sign with the default token set."),
                    LOG);
        }
        return ret;
    }
}
