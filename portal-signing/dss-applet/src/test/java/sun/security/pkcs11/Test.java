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
package sun.security.pkcs11;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Provider;
import java.security.Security;

/**
 * TODO
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class Test {

    public static void main(String[] args) {
        
        //String aPKCS11LibraryFileName = getPkcs11Path();
        String pkcs11ConfigSettings = "name = SmartCard\n" + "library = " + "\"c:\\programs\\Gemalto\\Classic Client\\BIN\\gclib.dll\"" + "\nslotListIndex = " + 0;
        byte[] pkcs11ConfigBytes = pkcs11ConfigSettings.getBytes();
        ByteArrayInputStream confStream = new ByteArrayInputStream(pkcs11ConfigBytes);

        sun.security.pkcs11.SunPKCS11 pkcs11 = new sun.security.pkcs11.SunPKCS11(confStream);
        Provider _pkcs11Provider = (Provider) pkcs11;

        Security.addProvider(_pkcs11Provider);
        
        

    }

}
