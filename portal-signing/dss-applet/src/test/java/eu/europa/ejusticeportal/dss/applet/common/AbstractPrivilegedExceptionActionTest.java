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

import eu.europa.ejusticeportal.dss.common.AbstractPrivilegedExceptionAction;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.EnumBasedCodeException;

import java.security.AllPermission;
import java.security.Permission;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test the AbstractPrivilegedExceptionAction
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class AbstractPrivilegedExceptionActionTest {

    /**
     * AbstractPrivilegedExceptionAction implementation
     */
    public class FakePrivilegedAction extends AbstractPrivilegedExceptionAction {

        @Override
        protected void doExec() throws EnumBasedCodeException {
            final SecurityManager security = System.getSecurityManager();
            if (security != null) {
                final Permission perm = new AllPermission();
                // Throws a security exception if not allowed
                security.checkPermission(perm);
            }
            throw new EnumBasedCodeException(MessagesEnum.dss_applet_message_card_detection_unavailable);
        }
    }

    /**
     * Test of the exec method for AbstractPrivilegedExceptionAction
     */
    @Test
    public void testExec() {
        boolean exceptionRaised = false;
        try {
            new FakePrivilegedAction().exec();
        } catch (CodeException ex) {
            exceptionRaised = true;
        } finally {
            if (!exceptionRaised) {
                fail("Exception was not raised");
            }
        }
    }
}
