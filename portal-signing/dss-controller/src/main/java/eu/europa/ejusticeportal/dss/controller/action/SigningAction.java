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
package eu.europa.ejusticeportal.dss.controller.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.SignatureAlgorithm;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.token.SignatureTokenConnection;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.controller.PortalFacade;
import eu.europa.ejusticeportal.dss.controller.exception.SigningBusinessException;
import eu.europa.ejusticeportal.dss.controller.exception.SigningException;

/**
 *
 * Base class for a dss signing action
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public abstract class SigningAction {

    /**
     * Parameter used to transmit the request data object
     */
    public static final String DATA_PARAM = "data";
    private static final Logger LOGGER = LoggerFactory.getLogger(SigningAction.class);

    /**
     * The constructor for SigningAction.
     *
     */
    public SigningAction() {
    }

    /**
     * Execute the action
     * @param portal the portal
     * @param request  the request
     * @param response the response
     */
    public void elaborate(final PortalFacade portal, final HttpServletRequest request,
            final HttpServletResponse response) {
        try {
            writeResponse(portal, request, response);
        } catch (SigningBusinessException e) {
            writeErrorResponse(response, e.getId());
        } catch (Exception e) {
            LOGGER.error("Exception in signature service", e);
            writeErrorResponse(response, MessagesEnum.dss_applet_message_controller_error.name());
        }
    }

    private void writeResponse(final PortalFacade portal, final HttpServletRequest request,
            final HttpServletResponse response) {
        try {
            response.setContentType("application/json");
            final OutputStream os = response.getOutputStream();
            final Object reqObject = getRequestObject(request);
            final Object resObject = getResponseObject(portal, request, reqObject);
            final DataWrapper w = new DataWrapper(getResponseString(resObject));
            final Gson gson = new Gson();
            os.write(gson.toJson(w).getBytes("UTF-8"));
            os.close();
        } catch (IOException e) {
            LOGGER.error("Error writeResponse", e);
            throw new SigningException(e);
        }
    }

    private void writeErrorResponse(final HttpServletResponse response,
            final String errorCode) {
        OutputStream os = null;
        try {
            LOGGER.error("Error in the SigningAction "+errorCode);
            os = response.getOutputStream();
            response.setContentType("application/json");
            os = response.getOutputStream();
            final DataWrapper w = new DataWrapper();
            w.setErrorCode(errorCode);
            final Gson gson = new Gson();
            os.write(gson.toJson(w).getBytes("UTF-8"));
        } catch (IOException e) {
            LOGGER.error("Error writeResponse", e);
            throw new SigningException(e);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    /**
     * Gets the object that will be serialised and sent back to the requester
     *
     * @param portal  the PortalFacade
     * @param request the request
     * @param o       the request object
     * @return the response object
     */
    protected abstract Object getResponseObject(PortalFacade portal, HttpServletRequest request, Object o);

    /**
     * Gets the serialised object from the request
     *
     * @param request the request
     * @return the object
     */
    private Object getRequestObject(final HttpServletRequest request) throws IOException {
        return eu.europa.ejusticeportal.dss.common.Utils.fromString(request.getParameter(DATA_PARAM));
    }

    private String getResponseString(final Object o) throws IOException {
        return eu.europa.ejusticeportal.dss.common.Utils.toString(o);
    }

    static class DataWrapper {

        private String errorCode;
        private String data;
        private String hash;
        private String algo;

        /**
         *
         * The default constructor for DataWrapper.
         */
        public DataWrapper() {
        }

        /**
         *
         * The default constructor for DataWrapper.
         *
         * @param data the data to wrap
         */
        public DataWrapper(String data) {
            setData(data);
        }

        /**
         * @return the data
         */
        public String getData() {
            return data;
        }

        /**
         * @param data the data to set
         */
        public void setData(String data) {
            this.data = data;
            hashData();
        }
        
        private void hashData(){
            if(data==null){
                return;
            }
            final SignatureTokenConnection token = SealedPDFService.getInstance().getToken();
            DSSPrivateKeyEntry privateKey = token.getKeys().get(0);
            SignatureAlgorithm sa = SignatureAlgorithm.getAlgorithm(privateKey.getEncryptionAlgorithm(), DigestAlgorithm.SHA512);
			try {
            byte[] signed = token.sign(data.getBytes("UTF-8"), sa.getDigestAlgorithm(), privateKey);
            hash = Base64.encodeBase64String(signed);
            algo = Base64.encodeBase64String(sa.getJCEId().getBytes());
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("Error hashing data", e);
			}
        }

        /**
         * @return the erroCode
         */
        public String getErrorCode() {
            return errorCode;
        }

        /**
         * @param errorCOde the errorCode to set
         */
        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        /**
         * @return the hash
         */
        public String getHash() {
            return hash;
        }

        /**
         * @param hash the hash to set
         */
        public void setHash(String hash) {
            this.hash = hash;
        }

        /**
         * @return the algo
         */
        public String getAlgo() {
            return algo;
        }

        /**
         * @param algo the algo to set
         */
        public void setAlgo(String algo) {
            this.algo = algo;
        }
    }
}
