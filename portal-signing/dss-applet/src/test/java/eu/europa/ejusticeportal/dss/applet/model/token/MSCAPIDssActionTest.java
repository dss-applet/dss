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
package eu.europa.ejusticeportal.dss.applet.model.token;

import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.validation102853.condition.QcStatementCondition;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.mscapi.DssMscapiProvider;
import eu.europa.ejusticeportal.dss.mscapi.LibraryLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import org.bouncycastle.asn1.x509.qualified.ETSIQCObjectIdentifiers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Test for PKCS12DssAction
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */

public class MSCAPIDssActionTest {

    
    public static void main(String[] args) throws FileNotFoundException, NoSuchAlgorithmException, CodeException, IOException {
        new MSCAPIDssActionTest().testSign();
    }
    /**
     * 
     * Test that we can sign with MSCAPI
     * @throws CodeException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */    
    public void testSign() throws CodeException, FileNotFoundException, IOException, NoSuchAlgorithmException{
        CardProfile cp = new CardProfile();
        cp.setApi(SignatureTokenType.MSCAPI.name());
        cp.setAtr("00 00 00 00 00 00 00 00 00 00");
        cp.setCardDescription("A test card");
        cp.setDigestAlgo(DigestAlgorithm.SHA512.name());
        Security.addProvider(new BouncyCastleProvider());
        Security.addProvider( new DssMscapiProvider());
        NewMSCAPIDSSAction m = new NewMSCAPIDSSAction(cp, DssMscapiProvider.KEYSTORE_ID);
        LibraryLoader.getInstance().run();
        m.connect();
        for (DSSPrivateKeyEntry key: m.getCertificates()){
        //DSSPrivateKeyEntry key = m.getCertificates().get(m.getCertificates().size()-1);
       // {
            //DSSPrivateKeyEntry key = m.getCertificates().get(1);
            String name = key.getCertificate().getSubjectDN().getName();
            QcStatementCondition cond = new QcStatementCondition(ETSIQCObjectIdentifiers.id_etsi_qcs_QcSSCD);
           // InMemoryDocument toBeSigned = new InMemoryDocument(IOUtils.toByteArray(new FileInputStream(new File("src/test/resources/hello-world.pdf"))));
           // DSSDocument d2 = m.sign(toBeSigned, key);
        	
        }
    }
}
