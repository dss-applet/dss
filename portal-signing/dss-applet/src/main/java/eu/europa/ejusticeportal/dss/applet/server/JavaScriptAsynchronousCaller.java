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
package eu.europa.ejusticeportal.dss.applet.server;

import java.util.logging.Level;

import eu.europa.ejusticeportal.dss.applet.common.JavaScriptExpressionEvaluator;
import eu.europa.ejusticeportal.dss.common.Utils;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;

/**
 * Fire asynchronous request to server, serializing object passed as parameters
 * to XML. It relies on
 * {@link JavaScriptExpressionEvaluator}.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public final class JavaScriptAsynchronousCaller implements AsynchronousCaller {

    private static final DssLogger LOG = DssLogger.getLogger(JavaScriptAsynchronousCaller.class.getSimpleName());

    /**
     * Fire the serverCall.
     *
     * @param serverCall the serverCall to fire
     */
    public void fire(ServerCall serverCall) {
        LOG.log(Level.FINE, "asynchronous caller fires({0})", serverCall.getAsynchronousServerCallName());        
        fire(serverCall.getAsynchronousServerCallName(),serverCall.getServerCallId(),
                (serverCall.getParams()==null||serverCall.getParams().length == 0 ? new Object[] { new Object() } : serverCall.getParams()));
    }

    private void fire(Enum<?> e, Enum<?>serverCallId, Object... params) {
        if (params == null) {
            JavaScriptExpressionEvaluator.getInstance().eval(e, serverCallId.name());
        } 
        else {
            String[] xmlParams = new String[params.length+1];
            xmlParams[0] = serverCallId.name();
            for (int i = 0; i < params.length; i++) {
                xmlParams[i+1] = Utils.escape(Utils.toString(params[i]));
                LOG.log(Level.FINE, "Param {0}", i);
                if (LOG.isLoggable(Level.FINE)){
                    LOG.log(Level.FINE, Utils.toString(params[i]));
                }
            }
            JavaScriptExpressionEvaluator.getInstance().eval(e, xmlParams);
        }
    }
}
