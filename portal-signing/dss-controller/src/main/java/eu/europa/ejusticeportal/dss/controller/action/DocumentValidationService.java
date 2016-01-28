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

import eu.europa.ec.markt.dss.signature.DSSDocument;
import eu.europa.ec.markt.dss.signature.InMemoryDocument;
import eu.europa.ec.markt.dss.validation102853.AdvancedSignature;
import eu.europa.ec.markt.dss.validation102853.CertificateToken;
import eu.europa.ec.markt.dss.validation102853.CommonCertificateVerifier;
import eu.europa.ec.markt.dss.validation102853.SignedDocumentValidator;
import eu.europa.ec.markt.dss.validation102853.crl.CRLSource;
import eu.europa.ec.markt.dss.validation102853.ocsp.OnlineOCSPSource;
import eu.europa.ec.markt.dss.validation102853.report.Conclusion;
import eu.europa.ec.markt.dss.validation102853.report.Reports;
import eu.europa.ec.markt.dss.validation102853.report.SimpleReport;
import eu.europa.ec.markt.dss.validation102853.rules.AttributeName;
import eu.europa.ec.markt.dss.validation102853.rules.Indication;
import eu.europa.ec.markt.dss.validation102853.rules.MessageTag;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.SignatureStatus;
import eu.europa.ejusticeportal.dss.controller.CommonsHttpLoaderFactory;
import eu.europa.ejusticeportal.dss.controller.DocumentValidationConfig;
import eu.europa.ejusticeportal.dss.controller.PortalFacade;
import eu.europa.ejusticeportal.dss.controller.SealMethod;
import eu.europa.ejusticeportal.dss.controller.ValidationLevel;
import eu.europa.ejusticeportal.dss.controller.signature.PdfUtils;
import eu.europa.ejusticeportal.dss.controller.signature.RefreshingTrustedListsCertificateSource;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Service to validate a signed document
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc. 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class DocumentValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentValidationService.class.getSimpleName());
    private RefreshingTrustedListsCertificateSource trustedListCertificateSource = RefreshingTrustedListsCertificateSource
            .getInstance();

    private static final DocumentValidationService INSTANCE = new DocumentValidationService();

    private static List<MessageTag> tampered = java.util.Arrays.asList(MessageTag.BBB_CV_ISI_ANS,
            MessageTag.BBB_ICS_ICDVV_ANS, MessageTag.BBB_ICS_AIDNASNE_ANS, MessageTag.BBB_ICS_ISCI_ANS);

    private static List<MessageTag> notTrusted = java.util.Arrays.asList(MessageTag.BBB_XCV_CCCBB_ANS,
            MessageTag.BBB_XCV_CMDCIQC_ANS, MessageTag.BBB_XCV_CMDCISSCD_ANS, MessageTag.BBB_XCV_IRDTFC_ANS,
            MessageTag.BBB_XCV_IRIF_ANS, MessageTag.BBB_XCV_ISCOH_ANS, MessageTag.BBB_XCV_ISCR_ANS,
            MessageTag.BBB_XCV_ICSI_ANS);

    private static List<MessageTag> revoked = java.util.Arrays.asList(MessageTag.BBB_XCV_IRDTFC_ANS,
            MessageTag.BBB_XCV_IRIF_ANS, MessageTag.BBB_XCV_ISCOH_ANS, MessageTag.BBB_XCV_ISCR_ANS,
            MessageTag.BBB_XCV_ICSI_ANS);

    /**
     * The constructor for DocumentValidationService.
     */
    private DocumentValidationService() {

    }

    /**
     * Get the instance of this service
     * 
     * @return the instance
     */
    public static DocumentValidationService getInstance() {
        return INSTANCE;
    }

    /**
     * Validate the pdf, never check if the file belongs in the workflow.
     * 
     * @param portal the PortalFacade
     * @param request the HttpServletRequest
     * @param signed the pdf or detached signature to validate
     * @param externalDocument the original document or null if the signature is not detached
     * @param signatureStatus the SignatureStatus, to collect validation codes
     * @throws Exception in case of unhandled error
     */
    public void validatePdf(final PortalFacade portal, final HttpServletRequest request, final byte[] signed,
            final byte[] externalDocument, final SignatureStatus signatureStatus) throws Exception {
        validatePdf(portal, request, signed, externalDocument, signatureStatus, portal.getDocumentValidationConfig(),
                portal.getCrlSource());

    }

    /**
     * Validate the pdf, never check if the file belongs in the workflow.
     * 
     * @param portal the PortalFacade
     * @param request the HttpServletRequest
     * @param signed the pdf or detached signature to validate
     * @param externalDocument the original document or null if the signature is not detached
     * @param signatureStatus the SignatureStatus, to collect validation codes
     * @param config the validation configuration
     * @param crlSource the storage mechanism for Certificate Revovation Lists
     * @throws Exception in case of unhandled error
     */
    public void validatePdf(final PortalFacade portal, final HttpServletRequest request, final byte[] signed,
            final byte[] externalDocument, final SignatureStatus signatureStatus, DocumentValidationConfig config,
            CRLSource crlSource) throws Exception {

        if (!config.getWorkflowValidationLevel().equals(ValidationLevel.DISABLED)) {
            validateWorkflowFile(portal, request, signed, signatureStatus, config);
        }
        this.validatePdf(signed, externalDocument, signatureStatus, config, crlSource,
                portal.getPDFDocumentName(request));

    }

    /**
     * Validate the pdf, including the check that the file belongs in the workflow (if enabled)
     * 
     * @param signed the signed pdf or detached signature to validate
     * @param externalDocument the original document or null if the signature is not detached
     * @param signatureStatus the SignatureStatus, to collect validation codes
     * @param config the validation configuration
     * @param crlSource the storage mechanism for Certificate Revocation Lists
     * @throws Exception in case of unhandled error
     */
    public void validatePdf(final byte[] signed, final byte[] externalDocument, final SignatureStatus signatureStatus,
            DocumentValidationConfig config, CRLSource crlSource, String pdfDocumentName) throws Exception {

        final SignedDocumentValidator val;
        if (externalDocument != null) {
            val = SignedDocumentValidator.fromDocument(new InMemoryDocument(signed));
            final InMemoryDocument imd = new InMemoryDocument(externalDocument);
            imd.setName(pdfDocumentName);
            List<DSSDocument> detachedContents = new ArrayList<DSSDocument>();
            detachedContents.add(imd);
            val.setDetachedContents(detachedContents);
        } else {
            val = SignedDocumentValidator.fromDocument(new InMemoryDocument(signed));
        }
        OnlineOCSPSource ocspSource = null;
        if (!config.getRevokedValidationLevel().equals(ValidationLevel.DISABLED)) {
            ocspSource = new OnlineOCSPSource();
            ocspSource.setDataLoader(CommonsHttpLoaderFactory.getInstance().newOcspLoader());
        }
        final CommonCertificateVerifier cv = new CommonCertificateVerifier(trustedListCertificateSource, crlSource,
                ocspSource, CommonsHttpLoaderFactory.getInstance().newLoader());
        val.setCertificateVerifier(cv);
        Reports reports = val.validateDocument(DocumentValidationService.class.getClassLoader().getResourceAsStream(
                "dssSignaturePolicy.xml"));

        if (!config.getOriginValidationLevel().equals(ValidationLevel.DISABLED) && externalDocument == null) {
            validatePdfOrigin(val, signed, signatureStatus, config);
        }

        if (!config.getSignedValidationLevel().equals(ValidationLevel.DISABLED) && externalDocument == null) {
            if ((val.getSignatures() == null || (val.getSignatures().isEmpty() && !config.getSealMethod().equals(
                    SealMethod.SEAL)))
                    || (val.getSignatures().size() < 2 && config.getSealMethod().equals(SealMethod.SEAL))) {
                setCode(config.getSignedValidationLevel(), MessagesEnum.dss_applet_message_pdf_not_signed,
                        signatureStatus);
            }
        } else {
            signatureStatus.setSignedByUser(true);
        }
        SimpleReport sr = reports.getSimpleReport();

        if (!config.getExpiredValidationLevel().equals(ValidationLevel.DISABLED)) {
            validateExpired(val.getSignatures(), signatureStatus, config);
        }
        if (!config.getSignBeforeSealValidationLevel().equals(ValidationLevel.DISABLED)) {
            validateSignBeforeSeal(val.getSignatures(), signatureStatus, config);
        }

        if (!(config.getTamperedValidationLevel().equals(ValidationLevel.DISABLED)
                && config.getTrustedValidationLevel().equals(ValidationLevel.DISABLED)
                && config.getRevokedValidationLevel().equals(ValidationLevel.DISABLED) && config
                .getSignedValidationLevel().equals(ValidationLevel.DISABLED))) {
            validate(sr, val.getSignatures(), signatureStatus, config);
            // validatePdfSignature(signatureStatus, config ,advSigs, externalDocument!=null, report);
        }

    }

    private void validateExpired(List<AdvancedSignature> signatures, SignatureStatus status,
            DocumentValidationConfig config) {

        for (AdvancedSignature s : signatures) {
            if (isSeal(s.getSigningCertificateToken().getCertificate(), config)) {
                continue;
            }
            if (s.getCertificates() != null) {
                for (CertificateToken t : s.getCertificates()) {
                    try {
                        t.getCertificate().checkValidity(s.getSigningTime());
                    } catch (CertificateExpiredException e) {
                        setCode(config.getExpiredValidationLevel(),
                                MessagesEnum.dss_applet_message_signature_cert_expired, status);
                    } catch (CertificateNotYetValidException e) {
                        setCode(config.getExpiredValidationLevel(),
                                MessagesEnum.dss_applet_message_signature_cert_expired, status);
                    }
                }
            }
        }
    }

    private void validateSignBeforeSeal(List<AdvancedSignature> signatures, SignatureStatus status,
            DocumentValidationConfig config) {
        if (SealMethod.SEAL.equals(config.getSealMethod())) {
            Date sealTime = null;
            for (AdvancedSignature s : signatures) {
                if (isSeal(s.getSigningCertificateToken().getCertificate(), config)) {
                    sealTime = s.getSigningTime();
                    break;
                }
            }
            if (sealTime != null) {
                for (AdvancedSignature s : signatures) {
                    if (s.getSigningTime().before(sealTime)) {
                        setCode(config.getSignBeforeSealValidationLevel(),
                                MessagesEnum.dss_applet_message_signed_before_seal, status);
                    }
                }
            }
        }
    }

    /**
     * Checks that the given PDF belongs to the current workflow
     * 
     * @param portal the PortalFacade
     * @param request the HttpServletRequest
     * @param pdf the PDF document to validate
     * @param signatureStatus to collect warnings
     * @param config the validation configuration
     * @throws Exception
     */
    private void validateWorkflowFile(final PortalFacade portal, final HttpServletRequest request, final byte[] pdf,
            final SignatureStatus signatureStatus, DocumentValidationConfig config) throws Exception {
        boolean valid = false;
        final String sessionXML = portal.getPDFDocumentXML(request);
        if (sessionXML != null && !sessionXML.isEmpty()) {
            final String embeddedXML = new String(PdfUtils.extractAttachment(pdf, SealedPDFService.EMBEDDED_XML_ID),
                    "UTF-8");
            if (embeddedXML.compareTo(sessionXML) == 0) {
                valid = true;
            }
            if (!valid) {
                setCode(config.getWorkflowValidationLevel(),
                        MessagesEnum.dss_applet_message_uploaded_pdf_not_in_workflow, signatureStatus);
            }
        } else {
            valid = true;
        }
        signatureStatus.setInWorkflow(valid);
    }

    /**
     * Sets the code in the SignatureStatus
     * 
     * @param l the validation level
     * @param m the code
     * @param signatureStatus the SignatureStatus
     */
    private void setCode(ValidationLevel l, MessagesEnum m, SignatureStatus signatureStatus) {
        switch (l) {
        case EXCEPTION:
            signatureStatus.addExceptionStatusCode(m);
            break;
        case WARN:
            signatureStatus.addWarningStatusCode(m);
            break;
        default:
        }

    }

    // NOTE the following is the "old" validation method, for DSS version 2
    // /**
    // * Validates the signature of the PDF by checking it is not definitely revoked and can be traced to the trusted
    // list
    // * @param signatureStatus to collect the validation errors
    // * @param config the validation configuration
    // * @param advancedSignatures the list of {@link AdvancedSignature}
    // * @throws IOException if the pdf document cannot be parsed
    // */
    // private void validatePdfSignature( final SignatureStatus signatureStatus, DocumentValidationConfig config,
    // List<AdvancedSignature> advancedSignatures, boolean isExternal, DetailedReport report) throws IOException {
    //
    //
    //
    // if (!config.getSignedValidationLevel().equals(ValidationLevel.DISABLED)){
    // if (advancedSignatures.size()<=0 && !isExternal){
    // setCode(config.getSignedValidationLevel(),MessagesEnum.dss_applet_message_pdf_not_signed,signatureStatus);
    // }
    // }
    // else {
    // signatureStatus.setSignedByUser(true);
    // }
    // //The signatureInformationList is empty if there is no signature in the document.
    // if (advancedSignatures.size()>1||(advancedSignatures.size()==1 && isExternal)) {
    // for (AdvancedSignature advSig:advancedSignatures) {
    // signatureStatus.setValidFormat(true);
    // // if (!config.getTamperedValidationLevel().equals(ValidationLevel.DISABLED)) {
    // // setCode(config.getTamperedValidationLevel(), MessagesEnum.dss_applet_message_uploaded_pdf_tampered,
    // signatureStatus);
    // // signatureStatus.setTamperedAfterSignature(true);
    // // }
    //
    //
    // // if (!config.getRevokedValidationLevel().equals(ValidationLevel.DISABLED) &&
    // sigInf.getCertPathRevocationAnalysis().getSummary().isInvalid()) {
    // // // Do not accept INVALID but accept UNKNOWN in case (for example) the OCSP server did not reply
    // // setCode(config.getRevokedValidationLevel(), MessagesEnum.dss_applet_message_signature_invalid,
    // signatureStatus);
    // // signatureStatus.setRevoked(true);
    // // }
    // // if (!config.getTrustedValidationLevel().equals(ValidationLevel.DISABLED) &&
    // !sigInf.getCertPathRevocationAnalysis().getTrustedListInformation().isServiceWasFound()) {
    // // setCode(config.getTrustedValidationLevel(), MessagesEnum.dss_applet_message_signature_not_trusted,
    // signatureStatus);
    // // signatureStatus.setUntrustedSignature(true);
    // // }
    // }
    // }
    // }

    /**
     * Validates that the first signature in the given signedPdf corresponds to the first private key in the given
     * token. This allows us to confirm the origin of the PDF assuming it was signed at the origin with the same token.
     * 
     * @param val the {@link SignedDocumentValidator}.
     * @param signed the {@link AdvancedSignature}s to validate
     * @param sigStatus the signature status to collect warning codes.
     * @param config the validation configuration
     * 
     */
    private void validatePdfOrigin(SignedDocumentValidator val, byte[] signed, final SignatureStatus sigStatus,
            DocumentValidationConfig config) {
        boolean isValid = true;
        try {
            switch (config.getSealMethod()) {
            case SEAL:
                isValid = PdfUtils.isSealedSeal(val, SealedPDFService.getInstance().getToken());
                break;
            case NO_SEAL:
                isValid = true;// Do not check if the document is not sealed.
                break;
            case SEAL_CUSTOM:
            default:
                isValid = PdfUtils.isSealedCustom(signed, SealedPDFService.getInstance().getToken());
            }

        } catch (Exception e) {
            isValid = false;
            LOGGER.error("Error checking the origin", e);
        }
        if (!isValid) {
            setCode(config.getOriginValidationLevel(), MessagesEnum.dss_applet_message_uploaded_pdf_not_from_server,
                    sigStatus);
        }
        sigStatus.setOriginatedInPortal(isValid);

    }

    // NOTE - the following method is the "old" way of doing it, when the PDF was signed by the server
    // /**
    // * Tests if the signature corresponds to the server signature, and if mathematically valid.
    // * @param advSig object containing the signature
    // * @return true if the signature comes from the server and is mathematically valid.
    // * @throws IOException
    // * @throws SignatureException
    // * @throws KeyStoreException
    // * @throws CertificateEncodingException
    // */
    // private boolean originatesOnServer(AdvancedSignature advSig) throws IOException, SignatureException,
    // KeyStoreException, CertificateEncodingException {
    //
    // boolean originatesOnServer = true;
    //
    // if (!Arrays.areEqual(advSig.getSigningCertificateToken().getEncoded(),
    // SealedPDFService.getInstance().getToken().getKeys().get(0)
    // .getCertificate().getEncoded())) {
    // originatesOnServer = false;
    // }
    // else if (!advSig.checkIntegrity(null).isSignatureIntact()) {
    // LOGGER.error("error verifying the signature - document was modified?");
    // originatesOnServer = false;
    // }
    // return originatesOnServer;
    // }

    private void validate(SimpleReport simpleReport, List<AdvancedSignature> signatures,
            final SignatureStatus sigStatus, DocumentValidationConfig config) {
        if (signatures == null || signatures.isEmpty()) {
            LOGGER.info("Signature list is empty");
            return;
        }
        final List<String> signatureIds = simpleReport.getSignatureIdList();
        List<String> errorCodes = new ArrayList<String>();
        List<String> warningCodes = new ArrayList<String>();
        Map<String, AdvancedSignature> signatureMap = new HashMap<String, AdvancedSignature>();
        for (AdvancedSignature signature : signatures) {
            signatureMap.put(signature.getId(), signature);
        }
        for (final String signatureId : signatureIds) {

            if (isSeal(signatureMap.get(signatureId).getSigningCertificateToken().getCertificate(), config)) {
                continue;
            }

            final String indication = simpleReport.getIndication(signatureId);
            if (!Indication.VALID.equals(indication)) {

                final List<Conclusion.BasicInfo> errors = simpleReport.getErrors(signatureId);
                // Normally you only have one error.
                Conclusion.BasicInfo error = errors.size() > 0 ? errors.get(0) : null;
                if (error != null) {
                    final String attributeValue = error.getAttributeValue(AttributeName.NAME_ID);
                    final Map<String, String> attributes = error.getAttributes();
                    for (final Map.Entry<String, String> entry : attributes.entrySet()) {
                        // ... you log or report…
                        LOGGER.debug("Validation error " + entry.getKey() + ":" + entry.getValue());
                    }
                    if (attributeValue != null) {
                        errorCodes.add(attributeValue);
                    }
                }
                break;
            } else {
                final List<Conclusion.BasicInfo> warnings = simpleReport.getWarnings(signatureId);
                final List<Conclusion.BasicInfo> infos = simpleReport.getInfo(signatureId);
                // ... you log or report…
                for (Conclusion.BasicInfo warning : warnings) {
                    String attributeValue = warning.getAttributeValue(AttributeName.NAME_ID);
                    if (attributeValue != null) {
                        warningCodes.add(attributeValue);
                    }
                    for (final Map.Entry<String, String> entry : warning.getAttributes().entrySet()) {
                        LOGGER.debug("Validation error " + entry.getKey() + ":" + entry.getValue());

                    }
                }
                for (Conclusion.BasicInfo info : infos) {
                    for (final Map.Entry<String, String> entry : info.getAttributes().entrySet()) {
                        LOGGER.debug("INFO:" + entry.getKey() + ":" + entry.getValue());
                    }
                }
                break;
            }
        }
        List<String> codes = new ArrayList<String>();
        codes.addAll(errorCodes);
        codes.addAll(warningCodes);
        for (String code : codes) {
            MessageTag messageTag = MessageTag.valueOf(code);
            if (revoked.contains(messageTag)) {
                setCode(config.getRevokedValidationLevel(), MessagesEnum.dss_applet_message_signature_invalid,
                        sigStatus);
            } else if (tampered.contains(messageTag)) {
                setCode(config.getTamperedValidationLevel(), MessagesEnum.dss_applet_message_uploaded_pdf_tampered,
                        sigStatus);
            } else if (notTrusted.contains(messageTag)) {
                setCode(config.getTrustedValidationLevel(), MessagesEnum.dss_applet_message_signature_not_trusted,
                        sigStatus);
            }
        }
    }

    private boolean isSeal(X509Certificate certDoc, DocumentValidationConfig config) {
        if (SealMethod.SEAL.equals(config.getSealMethod())) {
            X509Certificate certSeal = SealedPDFService.getInstance().getToken().getKeys().get(0).getCertificate();
            if (certDoc != null && certSeal != null && certDoc.equals(certSeal)) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }
}
