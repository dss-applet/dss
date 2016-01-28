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
package eu.europa.ejusticeportal.dss.applet.controller.ui.filter;

import eu.europa.ejusticeportal.dss.applet.controller.ui.UIEvent;
import eu.europa.ejusticeportal.dss.applet.controller.ui.UIEventFilter;
import eu.europa.ejusticeportal.dss.applet.model.action.RefreshTokensAction;
import eu.europa.ejusticeportal.dss.applet.model.service.LoggingHome;
import eu.europa.ejusticeportal.dss.applet.model.service.PasswordHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SigningContextHome;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;

import java.util.logging.Level;

/**
 * 
 * This filter does an extra refresh of the signing context before signing if the password input was wrong.
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1697 $ - $Date: 2014-04-23 20:22:00 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class RefreshBeforeSignFilter implements UIEventFilter {

    private static final DssLogger LOG = LoggingHome.getInstance().getLogger(RefreshBeforeSignFilter.class.getSimpleName());
    /**
     * Apply the filter
     * 
     * @param event the event
     * @param arg the event argument
     * @return true if the filter was not applied, allowing the event to propagate
     */
    public boolean apply(UIEvent event, String arg) throws CodeException {        
        if (event.equals(UIEvent.signDocument)
                && PasswordHome.getInstance().getPasswordInputCallback()!=null
                && PasswordHome.getInstance().getPasswordInputCallback().wasWrongPin()) {
            LOG.log(Level.INFO,"The filter {0} is active for \"{1}\".", new Object[]{RefreshBeforeSignFilter.class.getSimpleName(),event.getLabel()});
            new RefreshTokensAction(SigningContextHome.getInstance().getSigningContext()).exec();
            //propagate
            return true;
        }
        return true;
    }
}
