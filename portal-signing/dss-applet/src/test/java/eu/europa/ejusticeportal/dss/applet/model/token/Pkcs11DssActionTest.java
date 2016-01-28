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
import eu.europa.ec.markt.dss.signature.token.PasswordInputCallback;
import eu.europa.ec.markt.dss.signature.token.Pkcs11SignatureToken;
import eu.europa.ejusticeportal.dss.applet.common.OperationCancelledException;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 * Test for PKCS12DssAction
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */

public class Pkcs11DssActionTest {

    private static String dllFileName = "C:/projects/dgmarkt/trunk/ants/signing-demo/trunk/applet/src/main/resources/bin32/p11siglib.dll";

    public static void main(String[] args) throws FileNotFoundException, NoSuchAlgorithmException, CodeException, IOException {
        new Pkcs11DssActionTest().testSign();
    }

    /**
     * Test that we can sign with p12/ECDSA
     * @throws CodeException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public void testSign() throws CodeException, FileNotFoundException, IOException, NoSuchAlgorithmException{
        CardProfile cp = new CardProfile();
        cp.setApi(SignatureTokenType.PKCS11.name());
        cp.setAtr("00 00 00 00 00 00 00 00 00 00");
        cp.setCardDescription("A test card");
        cp.setDigestAlgo(DigestAlgorithm.SHA1.name());
        cp.setLibraryPath(Arrays.asList(dllFileName));
        cp.setTerminalIndex(0);
        
        PKCS11DSSAction action = new PKCS11DSSAction(cp, new Pkcs11PasswordProvider());
        action.setLibFilePath(dllFileName);
        action.connect();        
        DSSPrivateKeyEntry key = action.getCertificates().get(0);
        action.close();
        
        action = new PKCS11DSSAction(cp, new Pkcs11PasswordProvider());
        action.setLibFilePath(dllFileName);
        action.connect();
        key = action.getCertificates().get(0);
        action.close();
        
        //        m.setLibFilePath(file.getAbsolutePath());
//        m.connect();
       // assertTrue(m.getCertificates()!=null);

    //    DSSPrivateKeyEntry key = m.getCertificates().get(0);
        
      //  String name = key.getCertificate().getSubjectDN().getName();
        //QcStatementCondition cond = new QcStatementCondition(ETSIQCObjectIdentifiers.id_etsi_qcs_QcSSCD);

        //InMemoryDocument toBeSigned = new InMemoryDocument(IOUtils.toByteArray(new FileInputStream(new File("src/test/resources/hello-world.pdf"))));
        //DSSDocument d2 = m.sign(toBeSigned, key);
        //IOUtils.write(IOUtils.toByteArray(d2.openStream()), new FileOutputStream("src/test/resources/hello-world-signed.pdf"));
        
        
    }
    
    /**
     * 
     * Password dialog
     */
    static class Pkcs11PasswordProvider implements PasswordInputCallback{

        /* (non-Javadoc)
         * @see eu.europa.ec.markt.dss.signature.token.PasswordInputCallback#getPassword()
         */
        public char[] getPassword() {
            JPasswordField pass = new JPasswordField();
            int x = JOptionPane.showConfirmDialog(null, new Object[]{new JLabel("Password"), pass},"Please enter your password",JOptionPane.OK_CANCEL_OPTION);
            if (x==JOptionPane.OK_OPTION && pass.getPassword()!=null){
                return pass.getPassword();
            } else {
                throw new OperationCancelledException();
            }
            
        }
        
    }
    
    public void testSign2() {
//        Security.addProvider(new BouncyCastleProvider());
        Pkcs11SignatureToken token = new Pkcs11SignatureToken(dllFileName, new Pkcs11PasswordProvider());
        List<DSSPrivateKeyEntry> keys = token.getKeys();


    }
}


