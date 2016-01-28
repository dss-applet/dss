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
package eu.europa.ejusticeportal.dss.demo.web.tags;

import java.util.Locale;

import javax.servlet.jsp.JspException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.tags.MessageTag;

public class LocalMessageTag extends MessageTag {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static MessageSource messageSource = null;

    @Override
    protected Object[] resolveArguments(Object arguments) throws JspException {
        Object[] args = super.resolveArguments(arguments);
        if (args != null) {
            initialize();
            Object[] resolvedArgs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
            	resolvedArgs[i] = messageSource.getMessage((String)resolvedArgs[i], resolvedArgs, getLocale());
            }
        }
        return args;
    }

	private void initialize() {
		  if (messageSource == null) {
	            messageSource = getApplicationContext().getBean(MessageSource.class);
	        }		
	}
    private ApplicationContext getApplicationContext() {
        return getRequestContext().getWebApplicationContext();
    }

    private Locale getLocale() {
        return getRequestContext().getLocale();
    }

}
