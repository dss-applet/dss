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
 * DSS - Digital Signature Services
 *
 * Copyright (C) 2013 European Commission, Directorate-General Internal Market and Services (DG MARKT), B-1049 Bruxelles/Brussel
 *
 * Developed by: 2013 ARHS Developments S.A. (rue Nicolas Bové 2B, L-1253 Luxembourg) http://www.arhs-developments.com
 *
 * This file is part of the "DSS - Digital Signature Services" project.
 *
 * "DSS - Digital Signature Services" is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * DSS is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * "DSS - Digital Signature Services".  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ejusticeportal.dss.applet.model.token;

import eu.europa.ec.markt.dss.exception.DSSException;
import eu.europa.ec.markt.dss.signature.token.AbstractSignatureTokenConnection;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.token.KSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.token.PasswordInputCallback;
import eu.europa.ec.markt.dss.signature.token.PrefilledPasswordCallback;

import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.ProtectionParameter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Class holding all MS CAPI API access logic.
 *
 * @version $Revision: 3311 $ - $Date: 2014-01-09 14:36:35 +0100 (Thu, 09 Jan 2014) $
 */

public class NewMSCAPISignatureToken extends AbstractSignatureTokenConnection {


    private final String keystoreName;
    
    /**
     * Create the token
     * @param keystoreName the name of the MSCAPI keystore
     */
    public NewMSCAPISignatureToken (String keystoreName) {
    	this.keystoreName = keystoreName;
    }
    
    
    private static class CallbackPasswordProtection extends KeyStore.PasswordProtection {
        private PasswordInputCallback passwordCallback;

        public CallbackPasswordProtection(PasswordInputCallback callback) {
            super(null);
            this.passwordCallback = callback;
        }

        @Override
        public synchronized char[] getPassword() {
            if (passwordCallback == null) {
                throw new RuntimeException("MSCAPI: No callback provided for entering the PIN/password");
            }
            return passwordCallback.getPassword();
        }
    }

	

	
    @Override
    public void close() {
    }

    @Override
    public List<DSSPrivateKeyEntry> getKeys() {

        List<DSSPrivateKeyEntry> list = new ArrayList<DSSPrivateKeyEntry>();

        try {
            ProtectionParameter protectionParameter = new CallbackPasswordProtection(new PrefilledPasswordCallback("nimp".toCharArray()));

            KeyStore keyStore = KeyStore.getInstance(keystoreName);
            keyStore.load(null, null);
            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                if (keyStore.isKeyEntry(alias)) {
                    PrivateKeyEntry entry = (PrivateKeyEntry) keyStore.getEntry(alias, protectionParameter);
                    list.add(new KSPrivateKeyEntry(entry));
                }
            }

        } catch (Exception e) {
            throw new DSSException(e);
        }
        return list;
    }
}
