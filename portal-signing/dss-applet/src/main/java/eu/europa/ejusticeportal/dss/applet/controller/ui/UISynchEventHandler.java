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
package eu.europa.ejusticeportal.dss.applet.controller.ui;

import eu.europa.ejusticeportal.dss.applet.model.service.MessageBundleHome;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;

import java.util.logging.Level;

/**
 * Handle the callback of UI (browser side). More of an event dispatcher than handler.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class UISynchEventHandler extends UIEventHandler {
    private static final DssLogger LOG = DssLogger.getLogger(UISynchEventHandler.class.getSimpleName());

    /**
     * The default constructor for UISynchEventHandler.
     * 
     * @param arg
     */
    public UISynchEventHandler(String arg) {
        super(arg);
    }
    
    @Override
    public void handle(UIEvent event) {
        try {
            super.doHandle(event);
        } catch (CodeException e) {
            LOG.log(Level.SEVERE, ExceptionUtils.getStackTrace(e));
            LOG.log(Level.INFO, "Error in handling synchronous UI event \"{0}\": {1}.",
                    new Object[] { event.getLabel(), e.getCode() });
            throw new UISynchEventException(MessageBundleHome.getInstance().getMessage(e.getCode()), e);
        }
    }

}
