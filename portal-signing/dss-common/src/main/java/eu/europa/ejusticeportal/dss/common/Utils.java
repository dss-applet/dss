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
package eu.europa.ejusticeportal.dss.common;

import eu.europa.ec.markt.dss.exception.DSSException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;


/**
 *
 * Utility for serialising javascript method parameters
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 262 $ - $Date: 2012-11-15 17:27:42 +0100 (jeu., 15 nov.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */

public final class Utils {
    private static final Logger LOG = Logger.getLogger(Utils.class.getSimpleName());
    /**version assumed when not found in the system property*/
    private static double defaultJreVersion = 8d;

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    
    private static final Pattern IS_NUMBER = Pattern.compile("(\\d+)?");
    /**
     * 
     * The default constructor for Utils.
     */
    private Utils(){
        
    }

    /**
     * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters
     * (U+0000 through U+001F).
     *
     * @param s
     * @return
     */
    public static String escape(String s) {
        if (s == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        escape(s, sb);
        return sb.toString();
    }

    /**
     * @param s  - Must not be null.
     * @param sb
     */
    static void escape(String s, StringBuffer sb) {
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
            case '"':
                sb.append("\\\"");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '\'':
				sb.append("\\'");
                break; 				
            case '/':
                sb.append("\\/");
                break;
            default:
                //Reference: http://www.unicode.org/versions/Unicode5.1.0/
                if ((ch >= '\u0000' && ch <= '\u001F') || (ch >= '\u007F' && ch <= '\u009F')
                        || (ch >= '\u2000' && ch <= '\u20FF')) {
                    String ss = Integer.toHexString(ch);
                    sb.append("\\u");
                    for (int k = 0; k < 4 - ss.length(); k++) {
                        sb.append('0');
                    }
                    sb.append(ss.toUpperCase());
                } else {
                    sb.append(ch);
                }
            }
        }//for
    }

    /**
     * Serialises an object to string
     *
     * @param o the object
     * @return the string
     */
    public static String toString(Object o) {
        ToString ts = new ToString(o);
        ts.exec();
        return ts.getResult();
    }

    /**
     * Deserialises an object from string
     *
     * @param s the string
     * @return the object
     */
    public static Object fromString(String s) {
        FromString fs = new FromString(s);
        fs.doExec();
        return fs.getResult();
    }
    /**
     * Parses the JRE version
     * @param v the version string
     * @return the version
     */
    public static Double parseJreVersion(final String v) {

        String version = v.replace("_", ".");
        final String parts[] = version.split("\\.");
        
        if (parts.length==0||!IS_NUMBER.matcher(parts[0]).matches()){
            LOG.log(Level.INFO,"Unexpected JRE version - defaulting to {0}", new Object[]{defaultJreVersion});
            return defaultJreVersion;
        } else {
            StringBuilder s = new StringBuilder(parts[0]);
            s.append(".");
            for (int i=1;i<parts.length;i++){
                if (IS_NUMBER.matcher(parts[i]).matches()){
                    s.append(parts[i]);
                } else {
                    s.append("0");
                }
            }
            return Double.valueOf(s.toString());
        }
    }
    /**
     * Gets the signature algorithm name from the java name which is like [DigestAlgo]with[SigAlgo]
     * @param combName the java name
     * @return the signature algorithm
     * @throws NoSuchAlgorithmException 
     */
    public static String getSignatureAlgorithmName(String combName) throws NoSuchAlgorithmException {


        if("EC".equals(combName))  {
            return "ECDSA";
        }

        if("RSA".equals(combName))  {
            return "RSA";
        }


        int i = combName.indexOf("with");
        if (i<0){
            throw new NoSuchAlgorithmException(combName+" is not supported");
        }
        return combName.substring(i+4);        
        
    }
    
    /**
     * Format a date
     * @param date the date
     * @return the date as string
     */
    public static String formatDate(Date date){
        return date == null?"":new SimpleDateFormat(DATE_FORMAT).format(date);
    }
    
    /**
     * Gets the OS
     *
     * @param fp the {@link Fingerprint}
     * @return the OS the {@link OS}
     */
    public static OS getOs(Fingerprint fp) {
        OS os = OS.getFromSystemProperty(fp.getOs());
        if (os == null) {
            os = OS.getFromUserAgent(fp.getUserAgent());
        }
        return os == null ? OS.UNSUPPORTED : os;
    }
    
    private static class FromString extends AbstractPrivilegedAction {

        private Object result;
        private String source;
        public FromString(String s) {
            source = s;
        }
        @Override
        protected void doExec() {
            try {                
                result =  JsonReader.jsonToJava(source);
            } catch (IOException e) {
                throw new DSSException(e);
            }
            
        }
        
        public Object getResult(){
            return result;
        }
    }
    
    private static class ToString extends AbstractPrivilegedAction {

        private String result;
        private Object source;
        public ToString(Object s) {
            source = s;
        }
        @Override
        protected void doExec() {
            try {
                result =  JsonWriter.objectToJson(source);
            } catch (IOException e) {
                throw new DSSException(e);
            }
            
        }
        
        public String getResult(){
            return result;
        }
    }
}
