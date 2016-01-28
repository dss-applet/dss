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
package eu.europa.ejusticeportal.dss.applet.model.action;


import eu.europa.ejusticeportal.dss.common.AbstractPrivilegedExceptionAction;
import eu.europa.ejusticeportal.dss.applet.event.StatusRefreshed;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.service.SigningContextHome;
import eu.europa.ejusticeportal.dss.applet.model.token.TokenManager;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.MessageLevel;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;

import java.io.File;

/**
 * Load a file as a Library for PKSC11 connections.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class LoadLibraryAction extends AbstractPrivilegedExceptionAction {

    private File file;

    /**
     * The default constructor for LoadLibraryAction
     * @param file
     */
    public LoadLibraryAction(final File file) {
        this.file = file;
    }

    @Override
    protected void doExec() throws CodeException {
        boolean hasPkcs11 = false;

        for (CardProfile cp: SigningContextHome.getInstance().getSigningContext().getCardProfiles()){
            if (cp.getApi()!=null && SignatureTokenType.PKCS11.equals(cp.getApi())){
                hasPkcs11 = true;
            }
        }
        if (!hasPkcs11){
            //where there was no card with PKCS11, but the user has provided the library
            SigningContextHome.getInstance().addPkcs11Profile();
        }
        TokenManager tokenManager = TokenManager.getInstance();
        tokenManager.refreshTokens();
        tokenManager.refreshPKCS11Tokens(file);
        Event.getInstance().fire(new StatusRefreshed(MessagesEnum.dss_applet_message_pkcs11_path, MessageLevel.INFO, file.getAbsolutePath()));
    }
}
