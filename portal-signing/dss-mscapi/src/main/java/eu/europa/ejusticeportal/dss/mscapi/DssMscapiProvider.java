/*
 * Copyright (c) 2005, 2011, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 * 
 * # Modified by 
 */

package eu.europa.ejusticeportal.dss.mscapi;

import java.security.AccessController;
import java.security.Provider;
import java.util.HashMap;
import java.util.Map;

import sun.security.action.PutAllAction;


/**
 * A Cryptographic Service Provider for the Microsoft Crypto API.
 *
 * @since 1.6
 */

public final class DssMscapiProvider extends Provider {

	public static final String KEYSTORE_ID = "Windows-DSSMY";

    private static final String INFO = "ARHS's Microsoft Crypto API provider, based on Sun's";

    public DssMscapiProvider() {
        super("DSS4EJUSTMSCAPI", 1.7d, INFO);

        // if there is no security manager installed, put directly into
        // the provider. Otherwise, create a temporary map and use a
        // doPrivileged() call at the end to transfer the contents
        final Map<String,String> map = (System.getSecurityManager() == null)
                        ? (Map)this : new HashMap<String,String>();

        /*
         * Secure random
         */
        map.put("SecureRandom.Windows-PRNG", "eu.europa.ejusticeportal.dss.mscapi.PRNG");

        /*
         * Key store
         */
        map.put("KeyStore."+KEYSTORE_ID, "eu.europa.ejusticeportal.dss.mscapi.KeyStore$MY");
        map.put("KeyStore.Windows-DSSROOT", "eu.europa.ejusticeportal.dss.mscapi.KeyStore$ROOT");

        /*
         * Signature engines
         */
        // NONEwithRSA must be supplied with a pre-computed message digest.
        // Only the following digest algorithms are supported: MD5, SHA-1,
        // SHA-256, SHA-384, SHA-512 and a special-purpose digest algorithm
        // which is a concatenation of SHA-1 and MD5 digests.
        map.put("Signature.NONEwithRSA",
            "eu.europa.ejusticeportal.dss.mscapi.RSASignature$Raw");
        map.put("Signature.SHA1withRSA",
            "eu.europa.ejusticeportal.dss.mscapi.RSASignature$SHA1");
        map.put("Signature.SHA256withRSA",
            "eu.europa.ejusticeportal.dss.mscapi.RSASignature$SHA256");
        map.put("Signature.SHA384withRSA",
            "eu.europa.ejusticeportal.dss.mscapi.RSASignature$SHA384");
        map.put("Signature.SHA512withRSA",
            "eu.europa.ejusticeportal.dss.mscapi.RSASignature$SHA512");
        map.put("Signature.MD5withRSA",
            "eu.europa.ejusticeportal.dss.mscapi.RSASignature$MD5");
        map.put("Signature.MD2withRSA",
            "eu.europa.ejusticeportal.dss.mscapi.RSASignature$MD2");

        // supported key classes
        map.put("Signature.NONEwithRSA SupportedKeyClasses",
            "eu.europa.ejusticeportal.dss.mscapi.Key");
        map.put("Signature.SHA1withRSA SupportedKeyClasses",
            "eu.europa.ejusticeportal.dss.mscapi.Key");
        map.put("Signature.SHA256withRSA SupportedKeyClasses",
            "eu.europa.ejusticeportal.dss.mscapi.Key");
        map.put("Signature.SHA384withRSA SupportedKeyClasses",
            "eu.europa.ejusticeportal.dss.mscapi.Key");
        map.put("Signature.SHA512withRSA SupportedKeyClasses",
            "eu.europa.ejusticeportal.dss.mscapi.Key");
        map.put("Signature.MD5withRSA SupportedKeyClasses",
            "eu.europa.ejusticeportal.dss.mscapi.Key");
        map.put("Signature.MD2withRSA SupportedKeyClasses",
            "eu.europa.ejusticeportal.dss.mscapi.Key");

  
        /*
         * Key Pair Generator engines
         */
        map.put("KeyPairGenerator.RSA",
            "eu.europa.ejusticeportal.dss.mscapi.RSAKeyPairGenerator");
        map.put("KeyPairGenerator.RSA KeySize", "1024");

        /*
         * Cipher engines
         */
        map.put("Cipher.RSA", "eu.europa.ejusticeportal.dss.mscapi.RSACipher");
        map.put("Cipher.RSA/ECB/PKCS1Padding",
            "eu.europa.ejusticeportal.dss.mscapi.RSACipher");
        map.put("Cipher.RSA SupportedModes", "ECB");
        map.put("Cipher.RSA SupportedPaddings", "PKCS1PADDING");
        map.put("Cipher.RSA SupportedKeyClasses", "eu.europa.ejusticeportal.dss.mscapi.Key");

        if (map != (Map)this) {
            AccessController.doPrivileged(new PutAllAction(this, map));
        }
    }
}
