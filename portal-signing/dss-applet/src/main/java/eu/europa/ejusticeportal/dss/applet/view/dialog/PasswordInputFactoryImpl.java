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
package eu.europa.ejusticeportal.dss.applet.view.dialog;

import eu.europa.ec.markt.dss.signature.token.PasswordInputCallback;
import eu.europa.ejusticeportal.dss.applet.common.JavaSixClassName;
import eu.europa.ejusticeportal.dss.applet.common.PasswordInputFactory;
import eu.europa.ejusticeportal.dss.applet.model.service.LoggingHome;
import eu.europa.ejusticeportal.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;

/**
 * Factory of PasswordInputDialog.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class PasswordInputFactoryImpl implements PasswordInputFactory {

    private static final DssLogger LOG = LoggingHome.getInstance().getLogger(
            PasswordInputFactoryImpl.class.getSimpleName());
    /**
     * Singleton reference of PasswordInputFactory.
     */
    private static PasswordInputFactoryImpl instance;

    /**
     * Constructs singleton instance of PasswordInputFactory.
     */
    private PasswordInputFactoryImpl() {
    }

    /**
     * Provides reference to singleton of PasswordInputFactory.
     * 
     * @return Singleton instance of PasswordInputFactory.
     */
    public static PasswordInputFactoryImpl getInstance() {
        if (instance == null) {
            instance = new PasswordInputFactoryImpl();
        }
        return instance;
    }

    /**
     * Get a password dialog
     * 
     * @param api the {@link SignatureTokenType} that may need a password dialog
     * @param message a message to put in the password dialog
     * @return a new PasswordInputDialog instance.
     */
    public PasswordInputCallback getPasswordInput(SignatureTokenType api, String message) {
        PasswordInputCallback cb;
        switch (api) {
        case PKCS11:
        case PKCS12:
        case MSCAPI:
            cb = new PasswordInputDialog(message);
            break;
        case MOCCA:
            cb = getPasswordInputMocca(message);
            break;
        default:
            cb = new PasswordInputDialog(message);

        }
        return cb;
    }

    private PasswordInputCallback getPasswordInputMocca(String message) {
        PasswordInputCallback cb = null;
        try {
            cb = (PasswordInputCallback) Class.forName(JavaSixClassName.MoccaPasswordInputDialog.getClassName())
                    .getConstructor(String.class).newInstance(message);
        } catch (Exception e) {
            ExceptionUtils.exception(new UnexpectedException(
                    "Can't load or instantiate classes related to MOCCA connection.", e,
                    JavaSixClassName.MOCCADSSAction.getClassName()), LOG);
        }
        return cb;
    }

}
