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

import eu.europa.ec.markt.dss.validation102853.crl.CRLSource;
import eu.europa.ec.markt.dss.validation102853.crl.OnlineCRLSource;
import eu.europa.ejusticeportal.dss.common.DssEvent;
import eu.europa.ejusticeportal.dss.common.SignedForm;
import eu.europa.ejusticeportal.dss.controller.DocumentValidationConfig;
import eu.europa.ejusticeportal.dss.controller.PortalFacade;
import eu.europa.ejusticeportal.dss.controller.SealMethod;
import eu.europa.ejusticeportal.dss.controller.ValidationLevel;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

/**
 * 
 * Implementation of PortalFacade for unit testing
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class PortalFacadeTestImpl implements PortalFacade {

    private final String xml;
    private final String docLoc;
    private final String cardProfLoc;
    private  String cardProfileXML;
    private DocumentValidationConfig config;
    /**
     * 
     * The default constructor for PortalFacadeTestImpl.
     */
    public PortalFacadeTestImpl(){
        xml = "<test>some XML &amp; some more</test>";
        docLoc = "dss/hello-world.pdf";
        cardProfLoc = "TestSigningRepo.xml";
        cardProfileXML = initXML();
        initConfig();
    }
    /**
     * 
     * The constructor for PortalFacadeTestImpl.
     * @param xml The XML to embed
     * @param docLoc the location of test PDF
     * @param cardProfLoc the location of the card profile XML
     */
    public PortalFacadeTestImpl(final String xml, String docLoc, String cardProfLoc){
        this.xml = xml;
        this.docLoc = docLoc;
        this.cardProfLoc = cardProfLoc;
        cardProfileXML = initXML();
        initConfig();
    }
    /**
     * 
     * The constructor for PortalFacadeTestImpl.
     * @param xml The XML to embed
     * @param docLoc the location of test PDF
     */
    public PortalFacadeTestImpl(final String xml, String docLoc){
        this.xml = xml;
        this.docLoc = docLoc;
        this.cardProfLoc = "TestSigningRepo.xml";
        cardProfileXML = initXML();
        initConfig();
    }

    private void initConfig(){
        config = new DocumentValidationConfig() {

            @Override
            public ValidationLevel getWorkflowValidationLevel() {
                return ValidationLevel.EXCEPTION;
            }

            @Override
            public ValidationLevel getTrustedValidationLevel() {
                return ValidationLevel.WARN;
            }

            @Override
            public ValidationLevel getTamperedValidationLevel() {
                return ValidationLevel.WARN;
            }

            @Override
            public ValidationLevel getSignedValidationLevel() {
                return ValidationLevel.EXCEPTION;
            }

            @Override
            public ValidationLevel getSignatureFormatValidationLevel() {
                return ValidationLevel.EXCEPTION;
            }


            @Override
            public ValidationLevel getRevokedValidationLevel() {
                return ValidationLevel.EXCEPTION;
            }

            @Override
            public ValidationLevel getOriginValidationLevel() {
                return ValidationLevel.EXCEPTION;
            }

            @Override
            public String getLotlUrl() {
                return"https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl-mp.xml";
            }

            @Override
            public int getRefreshPeriod() {
                return 0;
            }

            @Override
            public boolean isCheckLotlSig() {
                return false;
            }

            @Override
            public ValidationLevel getExpiredValidationLevel() {
                return ValidationLevel.EXCEPTION;
            }

            @Override
            public SealMethod getSealMethod(){
                return SealMethod.SEAL_CUSTOM;
            }

            @Override
            public ValidationLevel getSignBeforeSealValidationLevel() {
                // TODO Auto-generated method stub
                return ValidationLevel.DISABLED;
            }
        };
    }
    
    @Override
    public String getUserCountry(HttpServletRequest request) {

        return "LU";
    }

    @Override
    public byte[] getPDFDocument(HttpServletRequest request) {

        InputStream is = null;
        try {
            is = PortalFacadeTestImpl.class.getClassLoader().getResourceAsStream(docLoc);
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }

    }

    @Override
    public String getPDFDocumentXML(HttpServletRequest request) {

        return xml;
    }

    @Override
    public String getPDFDocumentName(HttpServletRequest request) {
        return "MyPDF.PDF";
    }

    @Override
    public Map<String, String> getLocalisedMessages(HttpServletRequest request, List<String> codes) {
        
        Map<String,String> messages = new HashMap<String, String>();
        messages.put("message1", "messageValue1");
        messages.put("message2", "messageValue2");
        messages.put("message3", "messageValue3");
        return messages;
    }

    @Override
    public void storePDF(HttpServletRequest request, SignedForm form) {

    }

    @Override
    public void log(HttpServletRequest request, DssEvent log) {

    }

    @Override
    public String getCardProfileXML() {
        return cardProfileXML;
    }

    /**
     * Sets the XML
     * @param xml the XML to set
     */
    public void setCardProfileXML(String xml) {
        cardProfileXML = xml;
    }
    private String initXML (){
        InputStream is = null;
        try {
            is = PortalFacadeTestImpl.class.getClassLoader().getResourceAsStream(cardProfLoc);
            return new String(IOUtils.toByteArray(is),"UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            IOUtils.closeQuietly(is);
        }
     
    }
	@Override
	public DocumentValidationConfig getDocumentValidationConfig() {
		return config;
		
	}
	@Override
	public CRLSource getCrlSource() {
		return new OnlineCRLSource();
	}

    public DocumentValidationConfig getConfig() {
        return config;
    }

    public void setConfig(final DocumentValidationConfig config) {
        this.config = config;
    }
}
