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
package eu.europa.ejusticeportal.dss.controller.action;

import eu.europa.ec.markt.dss.validation102853.crl.CRLSource;
import eu.europa.ejusticeportal.dss.common.SignatureStatus;
import eu.europa.ejusticeportal.dss.controller.DocumentValidationConfig;
import eu.europa.ejusticeportal.dss.controller.HttpProxyConfig;
import eu.europa.ejusticeportal.dss.controller.SealMethod;
import eu.europa.ejusticeportal.dss.controller.ValidationLevel;
import eu.europa.ejusticeportal.dss.controller.signature.RefreshingTrustedListsCertificateSource;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

/**
 * TODO
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith (JUnit4.class)
public class DocumentValidationServiceTest {

    
    @Test
    public void testSignatureValidation() throws Exception{
        System.setProperty("dss_applet_server_seal_cert", "classpath:server.p12");
        System.setProperty("dss_applet_server_seal_pwd","classpath:server.pwd");
        SignatureStatus status = new SignatureStatus();
        DocumentValidationConfig config = Mockito.mock(DocumentValidationConfig.class);
        Mockito.when(config.getLotlUrl()).thenReturn("http://www/google.com");
        Mockito.when(config.getOriginValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getRevokedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getSignatureFormatValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getSignedValidationLevel()).thenReturn(ValidationLevel.EXCEPTION);
        Mockito.when(config.getTamperedValidationLevel()).thenReturn(ValidationLevel.EXCEPTION);
        Mockito.when(config.getWorkflowValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getTrustedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getExpiredValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getExpiredValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getSignBeforeSealValidationLevel()).thenReturn(ValidationLevel.DISABLED);
        Mockito.when(config.getSealMethod()).thenReturn(SealMethod.NO_SEAL);
        HttpProxyConfig proxy = Mockito.mock(HttpProxyConfig.class);
        RefreshingTrustedListsCertificateSource.init(proxy,config);

        CRLSource crlSource = Mockito.mock(CRLSource.class);
        String pdfDocumentName = "Test.pdf";
        byte [] signed = IOUtils.toByteArray(DocumentValidationServiceTest.class.getClassLoader().getResourceAsStream("dss/FR_ANTS_signed_Test.pdf"));
        DocumentValidationService.getInstance().validatePdf(signed, null, status, config, crlSource, pdfDocumentName);
    }
}
