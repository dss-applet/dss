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

import java.util.Arrays;

/**
 *
 * Generic Server call for the DssApplet.
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision: 367 $ - $Date: 2012-11-30 21:00:37 +0100 (Fri, 30 Nov
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class ServerCall {

    private Enum<?> asynchronousServerCallName;
    private Object[] params;
	private Enum<?> serverCallId;

    /**
     * Get the asynchronousServerCallName
     *
     * @return asynchronousServerCallName
     */
    public Enum<?> getAsynchronousServerCallName() {
        return asynchronousServerCallName;
    }

    /**
     * Get the serverCallId
     *
     * @return serverCallId
     */
    public Enum<?> getServerCallId() {
        return serverCallId;
    }

    /**
     * Get the parameters of the asynchronous server call.
     *
     * @return the parameters
     */
    public Object[] getParams() {
        return params;
    }

    /**
     * Default constructor of ServerCall.
     *
     * @param asynchronousServerCallName the serverCall
     * @param params                     the parameters
     */
    public ServerCall(Enum<?> asynchronousServerCallName, Enum <?>serverCallId, Object... params) {
        this.asynchronousServerCallName = asynchronousServerCallName;
        this.serverCallId = serverCallId;
        this.params = params;
    }

    @Override
    public String toString() {
        return "ServerCall [asynchronousServerCallName=" + asynchronousServerCallName + ", params="
                + Arrays.toString(params) + "]";
    }
}
