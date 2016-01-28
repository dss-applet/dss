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

import eu.europa.ec.markt.dss.signature.InMemoryDocument;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.token.PasswordInputCallback;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test for PKCS12DssAction
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith(JUnit4.class)
public class PKCS12DssActionTest {

    /**
     * Test that we can sign with p12/ECDSA
     * @throws CodeException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testSign() throws CodeException, FileNotFoundException, IOException, NoSuchAlgorithmException{
//        PKCS12DSSAction p = new PKCS12DSSAction("src/test/resources/ecdsa.p12", new PasswordInputCallback() {
//            
//            public char[] getPassword() {
//                return "password".toCharArray();
//            }
//        });
//        Security.addProvider(new BouncyCastleProvider());
//        p.connect();
//        DSSPrivateKeyEntry key = p.getCertificates().get(0);
//        InMemoryDocument toBeSigned = new InMemoryDocument(IOUtils.toByteArray(new FileInputStream(new File("src/test/resources/hello-world.pdf"))));
//        Document d2 = p.sign(toBeSigned, key);
    }
}
