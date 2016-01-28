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
package eu.europa.ejusticeportal.dss.applet.model.service;

import eu.europa.ejusticeportal.dss.applet.server.AsynchronousCaller;
import eu.europa.ejusticeportal.dss.applet.server.JavaScriptAsynchronousCaller;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;

/**
 * Home for {@link AsynchronousCaller} implementation used by the applet
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision$ - $Date$
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public final class AsynchronousCallerHome {

	private static final AsynchronousCallerHome INSTANCE = new AsynchronousCallerHome();
	private AsynchronousCaller caller;
	private static final DssLogger LOG = LoggingHome.getInstance().getLogger(AsynchronousCallerHome.class.getSimpleName());
	private AsynchronousCallerHome(){
		
	}
	
	public void init(String clazz){
		if (caller == null && clazz!=null){
			try {
				caller = (AsynchronousCaller) Class.forName(clazz).newInstance();
			} catch (Exception e) {
				ExceptionUtils.throwRuntimeException(e, LOG);
				caller = null;
			} 
		} else if (clazz== null){
			caller = new JavaScriptAsynchronousCaller();
		} 
	}
	public static AsynchronousCallerHome getInstance(){
		
		return INSTANCE;
	}
	
	public AsynchronousCaller getCaller(){
		if (caller == null) {
			caller = new JavaScriptAsynchronousCaller();
		}
		return caller;
	}
}
