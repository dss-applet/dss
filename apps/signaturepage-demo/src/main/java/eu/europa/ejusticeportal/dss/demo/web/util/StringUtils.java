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
package eu.europa.ejusticeportal.dss.demo.web.util;

/**
 *
 * Some methods for working with PDFs
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc. 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class StringUtils {
    
    
    public static String truncateMessageIn50000Characters(final String message) {
        return truncateMessageInCharacters(message, 50000);
    }
    
    public static String truncateMessageInCharacters(final String message, int maxNumberOfCharacters) {
        if(message == null || message.length()==0){
            return message;
        }
        
        //Only truncate if the length is higher than the max allowed.
        if(message.length()>maxNumberOfCharacters){
            return message.substring(0, maxNumberOfCharacters);
        }

        return message;
    }

}
