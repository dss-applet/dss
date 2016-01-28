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
import eu.europa.ejusticeportal.dss.applet.model.service.AsynchronousCallerHome;
import eu.europa.ejusticeportal.dss.applet.model.service.FingerprintHome;
import eu.europa.ejusticeportal.dss.applet.model.token.TokenManager;
import eu.europa.ejusticeportal.dss.applet.server.AsynchronousServerCall;
import eu.europa.ejusticeportal.dss.applet.server.ServerCall;
import eu.europa.ejusticeportal.dss.common.ServerCallId;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;

/**
 * Action with elevated privileged to refresh the signing context.
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision$ - $Date$
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class RefreshSigningContextAction extends AbstractPrivilegedExceptionAction {

    /**
     *
     * Execute the RefreshSigningContextAction.
     */
    @Override
    public void doExec() throws CodeException {
        TokenManager.getInstance().closeTokenConnections();
        FingerprintHome.getInstance().refresh();
        AsynchronousCallerHome.getInstance().getCaller()
                .fire(
                        new ServerCall(AsynchronousServerCall.callServer, ServerCallId.getSigningContext, FingerprintHome.getInstance()
                                .getFingerprint()));
    }
}
