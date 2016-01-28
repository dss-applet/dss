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
/*
 * Project: DG Justice - DSS
 * Contractor: ARHS-Developments.
 *
 * $HeadURL: http://forge.aris-lux.lan/svn/isammp/isamm-pd/trunk/app/buildtools/src/main/resources/eclipse/isamm-pd-java-code-template.xml $
 * $Revision: 6522 $
 * $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * $Author: naramsda $
 */
package eu.europa.ejusticeportal.dss.controller.eoss;

import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.SignatureEvent;
import eu.europa.ejusticeportal.dss.common.SignatureStatus;
import eu.europa.ejusticeportal.dss.common.SignedForm;
import eu.europa.ejusticeportal.dss.controller.PortalFacade;
import eu.europa.ejusticeportal.dss.controller.action.DocumentValidationService;
import eu.europa.ejusticeportal.dss.controller.action.PdfTempStore;
import eu.europa.ejusticeportal.dss.controller.action.SealedPDFService;
import eu.europa.ejusticeportal.dss.model.SigningMethod;
import eu.europa.ejusticeportal.dss.model.SigningMethodsHome;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * External online signature service
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class EossService {

    private static final String EOSS_TOKEN = "@@EOSS_TOKEN@@";
    private static final Logger LOGGER = LoggerFactory.getLogger(EossService.class);
    /** Key for the token put in the URLs */
    private static final String EOSS_TOKEN_NAME = "DSS_EOSS_TOKEN";
    /** Key for the sealed+unsigned PDF document that we keep in the session */
    private static final String EOSS_SEALED_PDF = "EOSS_SEALED_PDF";
    /** Key for keeping track of how often we check for the response to arrive */
    private static final String EOSS_COUNT = "EOSS_COUNT";

    private static final EossService INSTANCE = new EossService();
    /** Maximum number of times we will check if the response has arrived */
    private static final Integer MAX_COUNT = Integer.valueOf(5);

    private EossDataStore dataStore;


    private final SecureRandom random = new SecureRandom();

    private EossService() {

    }


    /**
     * Get the instance of this service
     * 
     * @return the instance
     */
    public static EossService getInstance(EossDataStore dataStore) {
    	if (INSTANCE.dataStore == null) {
    		INSTANCE.dataStore = dataStore;
    	}
        return INSTANCE;
    }

    /**
     * Gets the object that contains all information needed to make the request to the EOSS
     * @param portal the PortalFacade
     * @param request the request
     * @param methodCode the signing method code
     * @return the EossRequest
     */
    public EossRequest getRequest (PortalFacade portal, HttpServletRequest request, String methodCode){
        EossRequest r = new EossRequest();
        SigningMethod m = SigningMethodsHome.getInstance().getMethod(portal,methodCode);

        r.setData(getData(portal,request,m));
        r.setParameterMap(m.getParameterMap());
        r.setDataUrl(m.getParameterMap().get("DATA_URL").replace(EOSS_TOKEN, getToken(request)));
        r.setDataUrlTestPage(m.getParameterMap().get("DATA_URL_TESTPAGE").replace(EOSS_TOKEN, getToken(request)));
        r.setRedirectUrl(m.getParameterMap().get("REDIRECT_URL").replace(EOSS_TOKEN, getToken(request)));
        r.setRedirectUrlNoJs(m.getParameterMap().get("REDIRECT_URL_NO_JS").replace(EOSS_TOKEN, getToken(request)));
        r.setRedirectUrlTestPage(m.getParameterMap().get("REDIRECT_URL_TESTPAGE").replace(EOSS_TOKEN, getToken(request)));
        r.setRedirectUrlNoJsTestPage(m.getParameterMap().get("REDIRECT_URL_NO_JS_TESTPAGE").replace(EOSS_TOKEN, getToken(request)));

        r.setEossToken(getToken(request));
        r.setMethodCode(methodCode);
        return r;
    }
    /**
     * Get the data that will be sent to the EOSS server; also stores the PDF in the session
     * 
     * @param portal the portal
     * @param request the request
     * @param m the method
     * @return the data
     */
    private String getData(PortalFacade portal, HttpServletRequest request, SigningMethod m) {
        String data = m.getParameterMap().get("XML_REQUEST_DATA");
        byte[] doc = portal.getPDFDocument(request);
        String disclaimer = portal.getLocalisedMessages(request, null).get(MessagesEnum.dss_applet_digest_disclaimer.name());
        doc = SealedPDFService.getInstance().sealDocument(doc, portal.getPDFDocumentXML(request), disclaimer, portal.getDocumentValidationConfig().getSealMethod());
        request.getSession().setAttribute(EOSS_SEALED_PDF, doc);
        data = data.replace("@@BASE64DATA@@", Base64.encodeBase64String(doc));
        data = data.replace("@@FILENAME@@", portal.getPDFDocumentName(request));
        return data;
    }





    /**
     * Processes the data sent to us from Austria; stores it in the datastore
     * 
     * @param portal the portal
     * @param request the httprequest
     * @param method the signing method
     */
    public void processDataCallback(PortalFacade portal, HttpServletRequest request, String method) {
        SigningMethod m = SigningMethodsHome.getInstance().getMethod(portal,method);
        String token = request.getParameter(EOSS_TOKEN_NAME);
        String response = request.getParameter(m.getParameterMap().get("XML_RESPONSE_NAME"));
        if (response == null) {
        	LOGGER.error("Response received from EOSS does not have the data parameter");
        } else {
            EossResponse r = parseResponse(m, response);
            dataStore.setResponse(token, r);        	
        }
    }


    /**
     * Parse the EOSS response
     * @param response the response to parse
     * @return the parsed response
     */
    private EossResponse parseResponse(SigningMethod m, String response) {

        String parserClass = m.getParameterMap().get("EOSS_PARSER_CLASS");
        if (parserClass == null||parserClass.isEmpty()){
            throw new IllegalStateException("You must define the EOSS_PARSER_CLASS parameter for method "+m.getCode());
        }
        EossResponseParser parser;
        try {
             parser = (EossResponseParser)Class.forName(parserClass).newInstance();
        } catch (Exception e) {          
            LOGGER.error("Error instantiating the class "+parserClass,e);
            throw new IllegalStateException("The parser is not available "+parserClass);
        } 
                
        return parser.parseResponse(response);
    }

    /**
     * Get the response delivered to us by the EOSS server and stored in the datastore
     * @param portal the Portal
     * @param methodCode the code of the signing method
     * @param request the http request
     * @return the EOSS response - never null.
     */
    public EossResponse getResponse(PortalFacade portal, HttpServletRequest request, String methodCode) {
        EossResponse response = dataStore.getResponse(getToken(request));
        SignatureEvent evt = createSignatureEvent(methodCode);

        if (response == null) {
            response = new EossResponse();
            boolean timeout = timeout(request);
            response.setTimeout(timeout);
            if (timeout){
                evt.setSigned(false);
                evt.setErrorDescription("e-Justice portal EOSSService timed out.");
            }
        } else {
            try {
            	
                if (response.getSignature() != null && response.getSignatureStatus() == null) {
                    byte[] doc = (byte[]) request.getSession().getAttribute(EOSS_SEALED_PDF);
                    byte [] sig = response.getSignature().getBytes("UTF-8");
                	SigningMethod m = SigningMethodsHome.getInstance().getMethod(portal, methodCode);
                	
                	SignatureStatus s = validate(request, portal, doc,sig);
             	    if (!s.isValidFormat() && "TRUE".equals(m.getParameterMap().get("IGNORE_BAD_SIGNATURE_FORMAT")) ){
             	    	s.setValidFormat(true);
             	    	s.getExceptionStatusCodes().remove(MessagesEnum.dss_applet_message_signature_format_invalid);
             	    }
                    response.setSignatureStatus(s);
                    if (!s.isException()){
                        PdfTempStore.storeSignedForm(request, new SignedForm(s,doc,sig));            
                    }
                    evt.setSigned(true);
                }
            } catch (Exception e) {
                LOGGER.error("Exception when validating the eoss signature", e);
                response.setErrorCode("-2000");
                response.setErrorMessage(e.getMessage());
                evt.setSigned(false);
                evt.setErrorDescription(e.getMessage()==null?"Error signing the document":e.getMessage());
            } finally {
                dataStore.delete(getToken(request));
                request.getSession().removeAttribute(EOSS_TOKEN_NAME);
            }
        }
        if (evt.isSigned()||evt.getErrorDescription()!=null){
            portal.log(request, evt);
        }
        return response;

    }

    /**
     * Determines if the process should time out (no response received)
     * @param request the request
     * @return true if we should timeout
     */
    private boolean timeout(HttpServletRequest request) {
        boolean timeout;
        if (request.getSession().getAttribute(EOSS_COUNT) == null) {
            request.getSession().setAttribute(EOSS_COUNT, Integer.valueOf(1));
            timeout = false;
        } else {
            Integer i = (Integer) request.getSession().getAttribute(EOSS_COUNT);
            if (i > MAX_COUNT) {
                timeout = true;
                request.getSession().removeAttribute(EOSS_COUNT);
                request.getSession().removeAttribute(EOSS_TOKEN_NAME);
            } else {
                timeout = false;
                request.getSession().setAttribute(EOSS_COUNT, Integer.valueOf(i + 1));
            }
        }
        return timeout;
    }

    /**
     * Gets the token from the session, creating it if not already there. The token is sent to the EOSS server and
     * included in the callback URL, so we can link the document to the session
     * 
     * @param request the request
     * @return the token
     */
    private String getToken(HttpServletRequest request) {

        if (request.getSession().getAttribute(EOSS_TOKEN_NAME) == null) {
            String token = "EOSS" + new BigInteger(130, random).toString(32);
            request.getSession().setAttribute(EOSS_TOKEN_NAME, token);
        }
        return (String) request.getSession().getAttribute(EOSS_TOKEN_NAME);
    }

    /**
     * Set the datastore that is used to store the information returned by the EOSS server
     * 
     * @param dataStore the dataStore to set
     */
    public void setDataStore(EossDataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * Validate the signature
     * @param request http request
     * @param portal the PortalFacade
     * @param doc the document to sign
     * @param sig the signature
     * @return the SignatureStatus report
     * @throws Exception for various
     */
    private SignatureStatus validate(HttpServletRequest request, PortalFacade portal, byte [] doc, byte [] sig) throws Exception {
        SignatureStatus status = new SignatureStatus();
        DocumentValidationService.getInstance().validatePdf(sig, doc, status, portal.getDocumentValidationConfig(),portal.getCrlSource(), portal.getPDFDocumentName(request));        
        return status;
    }
    
    /**
     * Creates a SignatureEvent for logging
     * @param methodCode the code of the signing method
     * @return the SignatureEvent
     */
    private SignatureEvent createSignatureEvent(String methodCode){
        SignatureEvent event = new SignatureEvent();
        event.setApi("EOSS");
        event.setEventDate(new Date());
        event.setSigningMethod(methodCode);
        return event;
    }
}
