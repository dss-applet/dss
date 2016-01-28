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
package eu.europa.ejusticeportal.dss.applet.common;

import eu.europa.ejusticeportal.dss.applet.model.service.LoggingHome;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
/**
 * Home class for obtaining the instance of {@link UIController}
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision$ - $Date$
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class UIControllerHome {

	private static final UIControllerHome INSTANCE = new UIControllerHome();
	private UIController uiController;
	private static final DssLogger LOG = LoggingHome.getInstance().getLogger(UIControllerHome.class.getSimpleName());
	private UIControllerHome() {
	}
	
	/**
	 * Initialise the instance of {@link UIController}
	 * @param clazz the class name of the {@link UIController} implementation
	 */
	public void init(String clazz){
		if (uiController == null && clazz!=null){
			try {
				uiController = (UIController) Class.forName(clazz).newInstance();
			} catch (Exception e) {
				ExceptionUtils.throwRuntimeException(e, LOG);
				uiController = null;
			} 
		} else if (clazz== null){
			uiController = new JavaScriptUiController();
		} 
	}
	/**
	 * Get the static instance of {@link UIControllerHome}
	 * @return the instance
	 */
	public static UIControllerHome getInstance(){

		return INSTANCE;
	}
	/**
	 * Get the {@link UIController} implementation to use
	 * @return the {@link UIController}
	 */
	public UIController getUiController(){
		if (uiController == null){
			uiController = new JavaScriptUiController();
		}
		return uiController;
	}
}
