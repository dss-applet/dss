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

import eu.europa.ec.markt.dss.signature.token.Pkcs12SignatureToken;
import eu.europa.ec.markt.dss.signature.token.SignatureTokenConnection;
import eu.europa.ejusticeportal.dss.controller.SealMethod;
import eu.europa.ejusticeportal.dss.controller.exception.DssInitialisationException;
import eu.europa.ejusticeportal.dss.controller.exception.SigningException;
import eu.europa.ejusticeportal.dss.controller.signature.PdfUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

/**
 * Service to seal the PDF and embed XML within 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc. 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class SealedPDFService {

    private final SignatureTokenConnection token;
    /**
     * The name of the attachment in the PDF
     */
    public static final String EMBEDDED_XML_ID = "xmldata.xml";
    /** The system property that tells us where the PKCS12 certificate file is located */
    static final String CERTIFICATE_FILE_PROPERTY = "dss_applet_server_seal_cert";
    /**
     * The system property that tells us where the file containing the password to the PKCS12 certificate file is
     * located
     */
    static final String PASSWORD_FILE_PROPERTY = "dss_applet_server_seal_pwd";

    private static final SealedPDFService INSTANCE = new SealedPDFService();
    /**
     * 
     * The constructor for GetSealedPdf.
     */
    private  SealedPDFService() {
        this.token = initToken();
    }
    
    /**
     * Get the instance of this service
     * @return the instance.
     */
    public static SealedPDFService getInstance(){
        return INSTANCE;
    }

    /**
     * Seals the PDF document
     * @param documentToSign the PDF document
     * @param xmlAttachment the XML representation of the form that will be added as an attachment
     * @param disclaimer text to be added to the digest
     * @throws IOException 
     * @throws NoSuchAlgorithmException 
     * @throws KeyStoreException 
     */
    public byte[] sealDocument(final byte [] documentToSign, final String xmlAttachment, String disclaimer, SealMethod sealMethod)  {


        if (documentToSign == null || documentToSign.length == 0) {
            throw new IllegalStateException("Error getting the PDF document - empty or null.");
        }
        byte [] bytes = documentToSign;
        if (xmlAttachment!=null && !xmlAttachment.isEmpty()){
            bytes = PdfUtils.appendAttachment(documentToSign, xmlAttachment.getBytes(Charset.forName("UTF-8")), EMBEDDED_XML_ID);
        }
        try {
            switch (sealMethod) {
            case NO_SEAL://returns the bytes with attachment.
                break;
            case SEAL:
                bytes = PdfUtils.sealPdf(bytes, getToken());
                break;
            case SEAL_CUSTOM:
            default: //SEAL CUSTOM
                bytes = PdfUtils.sealPDFCustom(bytes, getToken(),"The text below allows us to ensure that the correct file was signed.");
                break;
            }
        
        } catch (Exception e){
            throw new SigningException(e);
        }
        return bytes;
    }

    /**
     * Get the token used to seal the PDF
     * 
     * @return the token
     */
    protected SignatureTokenConnection getToken() {
        return token;
    }

    /**
     * Initialise the token used to seal the PDF
     * 
     * @return the token
     */
    private SignatureTokenConnection initToken() {
        InputStream is = null;
        try {
            is = getInputStream(CERTIFICATE_FILE_PROPERTY);
            return new Pkcs12SignatureToken(getPassword().toCharArray(), IOUtils.toByteArray(is));
        } catch (Exception e) {
            throw new SigningException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }

    }

    /**
     * Gets the password for the server seal p12 file
     * 
     * @return the password
     */
    private String getPassword() {
        String password = null;
        InputStream is = getInputStream(PASSWORD_FILE_PROPERTY);
        try {
            List<String> lines = IOUtils.readLines(is);
            if (lines.size() != 0) {
                String b64Password = lines.get(0);
                password = new String(Base64.decodeBase64(b64Password), "UTF-8");
            } else {
                throw new DssInitialisationException("The password is not in the file.");
            }
        } catch (Exception e) {
            throw new DssInitialisationException("Error getting the password for the server seal - please set -D"
                    + PASSWORD_FILE_PROPERTY, e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return password;
    }

    /**
     * Gets an input stream from the system property
     * 
     * @param systemProperty the system property - if it starts with "classpath:" then the resource will be loaded from
     *            the classpath otherwise a java.net.URL will be used to open the resource.
     * @return the InputStream
     */
    private InputStream getInputStream(String systemProperty) {
        InputStream is = null;
        String p = System.getProperty(systemProperty);
        if (p != null) {
            try {
                if (p.startsWith("classpath:")) {
                    is = SealedPDFService.class.getClassLoader().getResourceAsStream(p.substring(10));
                } else {
                    URL url = new URL(p);
                    is = url.openStream();
                }
            } catch (Exception e) {
                throw new DssInitialisationException("Error getting the resource for the server seal - please set -D"
                        + systemProperty, e);
            }
        } else {
            throw new DssInitialisationException("Please set -D" + systemProperty);
        }
        return is;
    }

}
