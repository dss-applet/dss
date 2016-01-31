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
 * $HeadURL: https://forge.aris-lux.lan/svn/dgjustice-dss/trunk/portal-signing/dss-controller/src/test/java/eu/europa/ejusticeportal/dss/controller/signature/MemoryCacheCRLSourceTest.java $
 * $Revision: 1279 $
 * $Date: 2013-04-02 12:27:04 +0200 (Tue, 02 Apr 2013) $
 * $Author: MacFarPe $
 */
package eu.europa.ejusticeportal.dss.controller.signature;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.parameter.SignatureParameters;
import eu.europa.ec.markt.dss.signature.DSSDocument;
import eu.europa.ec.markt.dss.signature.InMemoryDocument;
import eu.europa.ec.markt.dss.signature.SignatureLevel;
import eu.europa.ec.markt.dss.signature.pades.PAdESService;
import eu.europa.ec.markt.dss.signature.token.Pkcs12SignatureToken;
import eu.europa.ec.markt.dss.validation102853.CertificateVerifier;
import eu.europa.ec.markt.dss.validation102853.CommonCertificateVerifier;
import eu.europa.ejusticeportal.dss.controller.SealMethod;
import eu.europa.ejusticeportal.dss.controller.TestUtils;
import eu.europa.ejusticeportal.dss.controller.action.SealedPDFService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * 
 * Test {@link PdfUtils}
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1071 $ - $Date: 2013-03-04 11:57:27 +0100 (Mon, 04 Mar 2013) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith(JUnit4.class)
public class PdfUtilsTest {

    /**
     * Test PDF seal
     * @throws COSVisitorException
     * @throws IOException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testSeal() throws COSVisitorException, IOException, KeyStoreException, NoSuchAlgorithmException {
        //load our test document - an iText PDF as generated by the Portal report service
        InputStream is = PdfUtils.class.getClassLoader().getResourceAsStream("dss/testseal.pdf");
        byte [] pdf = IOUtils.toByteArray(is);
        is.close();
        
        //Attach the form XML
        String attachment = "<xmldata></xmldata>";
        byte [] doc = PdfUtils.appendAttachment(pdf, attachment.getBytes(), "form.xml");
        
        //Seal the document 
        InputStream isP12 = PdfUtilsTest.class.getClassLoader().getResourceAsStream("dss/test.p12");
        Pkcs12SignatureToken token = new Pkcs12SignatureToken("password",isP12);
        byte [] sealed = PdfUtils.sealPDFCustom(doc, token, "This file allows us to check that the form is authentic");
        
        InputStream isse = new ByteArrayInputStream(sealed);
        PDDocument sealedPDD = PDDocument.load(isse);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        sealedPDD.save(baos);
        sealed = baos.toByteArray();
        //Sign the document (like a portal user should)
        CertificateVerifier cv = new CommonCertificateVerifier();
        PAdESService p = new PAdESService(cv);
        DSSDocument dssDoc = new InMemoryDocument(sealed);

        SignatureParameters params = new SignatureParameters();
        params.setPrivateKeyEntry(token.getKeys().get(0));
        params.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);
        params.setSigningToken(token);
        params.setDigestAlgorithm(DigestAlgorithm.SHA512);
        Calendar c = GregorianCalendar.getInstance();
        c.set(Calendar.YEAR, 2011);        
        params.bLevel().setSigningDate(c.getTime());
        DSSDocument sDoc = p.signDocument(dssDoc, params);
        //Test that the seal is OK.
        assertTrue(TestUtils.isSealed(null, sDoc.getBytes(), token, SealMethod.SEAL_CUSTOM));
    }
    
    /**
     * Test PDF seal
     * @throws COSVisitorException
     * @throws IOException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     */
   // @Test
    public void testSealNegative() throws COSVisitorException, IOException, KeyStoreException, NoSuchAlgorithmException {
        //load our test document - an iText PDF as generated by the Portal report service
        InputStream is = PdfUtils.class.getClassLoader().getResourceAsStream("dss/testseal.pdf");
        byte [] pdf = IOUtils.toByteArray(is);
        is.close();
        
        //Attach the form XML
        String attachment = "<xmldata></xmldata>";
        byte [] doc = PdfUtils.appendAttachment(pdf, attachment.getBytes(), "form.xml");
        
        //Seal the document 
        InputStream isP12 = PdfUtilsTest.class.getClassLoader().getResourceAsStream("dss/test.p12");
        Pkcs12SignatureToken token = new Pkcs12SignatureToken("password",isP12);
        byte [] sealed = PdfUtils.sealPDFCustom(doc, token, "This file allows us to check that the form is authentic");
        //adding another attachment will break the seal
        sealed = PdfUtils.appendAttachment(sealed, "break the seal".getBytes(), "break the seal");
        //Sign the document (like a portal user should)
        CertificateVerifier cv = new CommonCertificateVerifier();
        PAdESService p = new PAdESService(cv);
        DSSDocument dssDoc = new InMemoryDocument(sealed);
        SignatureParameters params = new SignatureParameters();
        params.setPrivateKeyEntry(token.getKeys().get(0));
        params.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);
        params.setSigningToken(token);
        params.setDigestAlgorithm(DigestAlgorithm.SHA512);
        Calendar c = GregorianCalendar.getInstance();
        c.set(Calendar.YEAR, 2011);        
        params.bLevel().setSigningDate(c.getTime());
        DSSDocument sDoc = p.signDocument(dssDoc, params);
        
        //Test that the seal is OK.
        assertFalse(TestUtils.isSealed(null, sDoc.getBytes(), token, SealMethod.SEAL_CUSTOM));
        
    }
}
