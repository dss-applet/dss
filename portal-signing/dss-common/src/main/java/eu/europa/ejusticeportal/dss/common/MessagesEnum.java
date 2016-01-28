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
package eu.europa.ejusticeportal.dss.common;

import java.io.Serializable;

/**
 * 
 * Messages used in the applet. The corresponding label is obtained from the {@link MessageBundle} using
 * the name of the MessagesEnum as a key.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision$ - $Date$
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public enum MessagesEnum implements Serializable{
    /**
     * The auto detection of the smartcard is not available.
     */
    dss_applet_message_card_detection_unavailable,
    /**
     * The pdf is temporarly stored at: "?path?"
     */
    dss_applet_message_pdf_path,
    /**
     * The signed document was received correctly
     */
    dss_applet_message_signedpdf_ok,
    /**
     * The smartcard has been removed since the certificates were loaded. Please re-insert your smartcard and try again.
     */
    dss_applet_message_smartcard_removed,
    /**
     * There were a technical failure in the digital signing service.
     */
    dss_applet_message_technical_failure,
    /**
     * Your PIN code is not valid.
     */
    dss_applet_message_wrong_pin,
    /**
     * The selected driver does not correspond to the inserted smartcard. You may have been selected the wrong driver.
     */
    dss_applet_message_wrong_pkcs11_lib,
    /**
     * The selected file does not contain any certificate. You may have been selected the wrong file.
     */
    dss_applet_message_wrong_pkcs12_file,
    /**
     * The uploaded Pdf is not the one provided by the server.
     */
    dss_applet_message_uploaded_pdf_not_from_server,
    /**
     * Your digital signature could be invalid.
     */
    dss_applet_message_signature_invalid,
    /**
     * The uploaded Pdf has been tampered after it was signed.
     */
    dss_applet_message_uploaded_pdf_tampered,
    /**
     * There has been a technical error in the digital signing service.
     */
    dss_applet_message_controller_error,
    /**
     * Your session has expired.
     */
    dss_applet_message_session_expired,
    /**
     * The uploaded pdf is not in the workflow.
     */
    dss_applet_message_uploaded_pdf_not_in_workflow,
    /**
     * The digital signature cannot be trusted.
     */
    dss_applet_message_signature_not_trusted,
    /**
     * The certificate has expired or not yet valid.
     */
    dss_applet_message_signature_cert_expired,
    /**
     * The signed Pdf has not been uploaded.
     */
    dss_applet_message_signed_pdf_not_uploaded,
    /**
     * The uploaded Pdf is valid.
     */
    dss_applet_message_uploaded_pdf_ok,
    /**
     * Library Path:
     */
    dss_applet_message_library_path,
    /**
     * Folder
     */
    dss_applet_message_folder,

    /**
     * Your digital signature may be invalid for the following reasons:
     */
    dss_applet_message_signature_status_warning,
    /**
     * Your digital signature is invalid for the following reasons:
     */
    dss_applet_message_signature_status_error,
    
    /**
     * Signature method - using a smart card or installed certificate
     * */
    dss_applet_message_method_sc_installed_cert,
    /**
     * Signature method - using a PKCS12 certificate
     * */
    dss_applet_message_method_p12,

    /**
     * The user cancelled the PIN entry dialog
     */
    dss_applet_message_pin_entry_cancelled,
    /***
     * The PKCS11 library was loaded from the following file:
     */
    dss_applet_message_pkcs11_path,
    /**
     * The certificates were read from the following file:
     */
    dss_applet_message_pkcs12_path,
    /**
     * The certificates list was refreshed
     */
    dss_applet_message_certs_refreshed,
    /**
     * The PDF was saved to the following file:
     */
    dss_applet_message_pdf_saved_path,
    /**
     * Why do we want your password (PKCS11) - to list the certificates, but you can change your mind later
     */
    dss_applet_message_why_password_card,
    /**
     * Why do we want your password (PKCS12) - to open the file and list the certificates
     */
    dss_applet_message_why_password_pkcs12,
    /**
     * Why do we want your password (MOCCA) - to sign the document
     */
    dss_applet_message_why_password_card_mocca,
    /**
     * Warning when more than one card using the PKCS11 or MOCCA API detected
     */
    dss_applet_message_more_than_one_card,

    /**
     * Thanks for providing the information about how you signed!
     */
    dss_applet_message_user_survey_thanks,
    /**
     * The PIN entry was cancelled
     */
    dss_applet_message_pin_entry_cancelled_mocca,
    /**
     * Wrong pin for MOCCA
     */
    dss_applet_message_wrong_pin_mocca,
    /**
     * Can't save the file
     */
    dss_applet_message_pdf_not_saved,
    /**
     * You have signed the document with certificate {0}
     */
    dss_applet_message_signed,

    /**
     * You have not signed the document
     */
    dss_applet_message_pdf_not_signed,

    /**
     * Thanks for providing the error report !
     */
    dss_applet_message_error_report_thanks,
   
    /**
     * Please select a signing method
     */
    dss_applet_message_please_select_method, 
    
    /**Please wait - the operation can take some time to complete*/
    dss_applet_message_long_operation, 
    
    /**You entered the wrong PIN. Please try again. Remember that if you enter the wrong PIN too many times you can block your card.*/
    dss_applet_message_wrong_pin_mocca_pin_pad, 
    
    /**In order to sign the form, please enter your PIN into your PIN pad.*/
    dss_applet_message_why_password_mocca_pin_pad,
    /**The client signature sign time is before the seal time*/
    dss_applet_message_signed_before_seal, 
    
    /**Title for the upload applet log dialog*/
    dss_applet_title_upload_log, 
    /**Intro for the upload applet log dialog*/
    dss_applet_intro_upload_log, 
    /**Question for the upload applet log dialog*/
    dss_applet_ask_upload_log,
    /**The format of the signature is not valid*/
    dss_applet_message_signature_format_invalid,
    /**The password dialog may appear behind this window*/
    dss_applet_message_password_dialog_hidden,
    /**Operation cancelled by user*/
    dss_applet_operation_cancelled, 
    /**The file already exists. Do you want to replace it?
     **/
    dss_applet_overwrite_file, 
    /**Confirm overwrite file*/
    dss_applet_overwrite_file_title,
    
    /*Certificate details - for the mouseover on the info icon*/
    /**Certificate issued issued to*/
    dss_applet_certificate_summary_issued_to,
    /**Certificate issued by*/
    dss_applet_certificate_summary_issued_by,
    /**Certificate serial number*/
    dss_applet_certificate_summary_serial_no,
    /**Certificate is valid from*/
    dss_applet_certificate_summary_valid_from,
    /**Certificate is valid to*/
    dss_applet_certificate_summary_valid_to,
    
    dss_applet_certificate_summary_issuer_statement,
    /**Text that goes in front of the digest attached to the PDF*/
    dss_applet_digest_disclaimer,
    /**Text that goes in the PDF generated for the test page*/
    dss_applet_test_page_text, 
    
    /**Open JDK not supported*/
    dss_applet_message_unsupported_vendor,
    /**Problem with the version*/
    dss_applet_message_bug_java_version,
    /**Safari needs to be in unsafe mode*/
    dss_applet_mac_unsafe,
    /**OK*/
    dss_applet_message_ok, 
    /**Yes*/
    dss_applet_message_yes,
    /**No*/
    dss_applet_message_no,
    /** Warning message indicating that a popup asking to insert the card may be behind the brower. **/
    dss_applet_message_insert_card
    ;
}
