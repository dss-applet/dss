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
package eu.europa.ejusticeportal.dss.demo.web.portal;

import eu.europa.ec.markt.dss.validation102853.crl.CRLSource;
import eu.europa.ec.markt.dss.validation102853.crl.OnlineCRLSource;
import eu.europa.ejusticeportal.dss.common.AppletCloseEvent;
import eu.europa.ejusticeportal.dss.common.DssEvent;
import eu.europa.ejusticeportal.dss.common.SignatureEvent;
import eu.europa.ejusticeportal.dss.common.SignedForm;
import eu.europa.ejusticeportal.dss.common.SigningContextEvent;
import eu.europa.ejusticeportal.dss.controller.DocumentValidationConfig;
import eu.europa.ejusticeportal.dss.controller.PortalFacade;
import eu.europa.ejusticeportal.dss.controller.profile.DssEventAnalyser;
import eu.europa.ejusticeportal.dss.demo.web.controller.sign.DefaultDocumentValidationConfig;
import eu.europa.ejusticeportal.dss.demo.web.controller.sign.SignController;
import eu.europa.ejusticeportal.dss.demo.web.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

public class PortalFacadeImpl implements PortalFacade {

    private static final Logger LOG = LoggerFactory.getLogger(PortalFacadeImpl.class.getSimpleName());
    private MessageSource messageSource;
    private static final DefaultDocumentValidationConfig DOC_VALIDATION_CONFIG = new DefaultDocumentValidationConfig();
    // Specific loggers for DSS statistics
    private static final Logger SIG_LOGGER = LoggerFactory
            .getLogger("eu.europa.ejusticeportal.dss.controller.statistics.sign");
    private static final Logger REP_LOGGER = LoggerFactory
            .getLogger("eu.europa.ejusticeportal.dss.controller.statistics.repo");
    private static final Logger NEW_LOGGER = LoggerFactory
            .getLogger("eu.europa.ejusticeportal.dss.controller.statistics.newprofile");
    private static final Logger ERROR_LOGGER = LoggerFactory
            .getLogger("eu.europa.ejusticeportal.dss.controller.statistics.error");
    private static final Logger APPLET_LOGGER = LoggerFactory
            .getLogger("eu.europa.ejusticeportal.dss.controller.statistics.applet");

    /**
     * The user country is not used for anything.
     * 
     * @return the user country.
     */
    @Override
    public String getUserCountry(HttpServletRequest request) {
        return "";
    }

    /**
     * The implementation just takes the document from where it was stored in session by the {@link SignController}.
     */
    @Override
    public byte[] getPDFDocument(HttpServletRequest request) {

        return (byte[]) request.getSession().getAttribute(SignController.UPLOADED_FILE_KEY);
    }

    /**
     * Set the message source
     * 
     * @param messageSource
     */
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * This implementation returns an empty string, so there will be no embedded xml file in the document.
     */
    @Override
    public String getPDFDocumentXML(HttpServletRequest request) {
        return "";
    }

    /**
     * The name of the document is here just hard coded.
     */
    @Override
    public String getPDFDocumentName(HttpServletRequest request) {
        return "mydocument.pdf";
    }

    @Override
    public Map<String, String> getLocalisedMessages(HttpServletRequest request, List<String> codes) {
        HashMap<String, String> map = new HashMap<String, String>();
        Properties p = new Properties();
        InputStream is = PortalFacadeImpl.class.getClassLoader().getResourceAsStream("i18n/messages_en.xml");
        try {
            p.loadFromXML(is);
            for (Object o : p.keySet()) {
                map.put((String) o, messageSource.getMessage((String) o, null, request.getLocale()));
            }
        } catch (Exception e) {
            LOG.error("Error getting messages", e);
        }
        return map;

    }

    /**
     * We do not provide an implementation for the storePDF method. Practically, it should handle the uploaded signed
     * PDF file according to business requirements.
     */
    @Override
    public void storePDF(HttpServletRequest request, SignedForm signedForm) {

    }

    @Override
    public void log(HttpServletRequest request, DssEvent event) {
        String id = request.getSession().getId();
        switch (event.getLogEntryType()) {
        case SG:
            logSignature(id, (SignatureEvent) event);
            break;
        case SC:
            logRepositoryAccess(id, (SigningContextEvent) event);
            break;
        case AC:
            logAppletLog(id, (AppletCloseEvent) event);
            break;
        default:
        }
    }

    /**
     * Log the applet close event (for UAT testing only)
     * 
     * @param event the event
     */
    private void logAppletLog(String id, AppletCloseEvent event) {
        APPLET_LOGGER.info(StringUtils.truncateMessageIn50000Characters(MessageFormat.format("Applet log for session {0}.", new Object[] { id })));
        APPLET_LOGGER.info(StringUtils.truncateMessageIn50000Characters(event.getAppletLog()));
    }

    /**
     * Log an access to the CardProfileRepository
     * 
     * @param event
     */
    private void logRepositoryAccess(String id, SigningContextEvent event) {

        final String message = MessageFormat.format("{0}\tATR={1}\tfound={2}\tfingerprint={3}", new Object[] {
                getCommonMessage(id, event), event.getAtr(), event.isAtrFound(), event.getFingerprint().toString() });

        REP_LOGGER.info(message);
    }

    /**
     * Log a signature event
     * 
     * @param event the event to log
     */
    private void logSignature(String id, final SignatureEvent event) {
        final boolean newProfile = DssEventAnalyser.newProfilePossible(event);
        // Log information about signature attempt, whether successful or not.
        final String message = MessageFormat
                .format("{0}\tnewProfile={1}\tsigned={2}\tmethod={3}\tatr={4}\t\tapi={5}\tdigAlgo={6}\tsigAlgo={7}\tkeyUsage={8}\tissuer={9}\tpath={10}\tcardCount={11}\terror={12}\textensions={13}\trecommended={14}",
                        new Object[] { getCommonMessage(id, event), newProfile, event.isSigned(),
                                event.getSigningMethod(), event.getAtr(), event.getApi(), event.getDigestAlgorithm(),
                                event.getSignatureAlgorithm(), event.getKeyUsage(), event.getUserSuppliedCardIssuer(),
                                event.getUserSuppliedPkcs11Path(), event.getDetectedCardCount(),
                                event.getErrorDescription(), event.getExtensions(), event.getRecommended() });
        SIG_LOGGER.info(message);
        // Log the XML corresponding to the possible new profile.
        if (newProfile) {
            NEW_LOGGER.info(new DssEventAnalyser().transform(event));
        }
        // LOG the error stack trace from the Applet if the signature failed.
        if (event.getErrorDescription() != null && !event.getErrorDescription().isEmpty()) {
            ERROR_LOGGER.info(MessageFormat.format("{0}/n{1}",
                    new Object[] { getCommonMessage(id, event), event.getErrorDescription() }));
        }
    }

    /**
     * Get the common part of the log entry
     * 
     * 
     * @param event the event
     * @return the message
     */
    private String getCommonMessage(String id, DssEvent event) {
        final String message = MessageFormat.format("{0} {1}\t[{2,date} {2,time}]\tarch={3}\tos={4}\tjre={5,number}",
                new Object[] { event.getLogEntryType().name(), id, event.getEventDate(), event.getArch(),
                        event.getOs(), event.getJreVersion() });
        return message;

    }

    /**
     * We get the card profile XML, which contains information about all supported cards, from the classpath.
     */
    @Override
    public String getCardProfileXML() {
        InputStream is = PortalFacadeImpl.class.getClassLoader().getResourceAsStream("SigningRepo.xml");
        String xml = null;
        byte[] bytes = null;
        try {
            bytes = IOUtils.toByteArray(is);
            xml = new String(bytes, "UTF-8");
        } catch (IOException e) {
            LOG.error("error getting the XML file", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return xml;
    }

    /**
     * The {@link DocumentValidationConfig} is the {@link DefaultDocumentValidationConfig} which gets the configuration
     * from the classpath.
     */
    @Override
    public DocumentValidationConfig getDocumentValidationConfig() {
        return DOC_VALIDATION_CONFIG;
    }

    /**
     * We return a new {@link CRLSource} each time
     */
    @Override
    public CRLSource getCrlSource() {
        return new OnlineCRLSource();
    }

}
