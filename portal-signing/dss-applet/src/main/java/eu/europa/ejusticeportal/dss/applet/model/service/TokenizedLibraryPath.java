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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Class used to tokenize a Library Path similar to "root/sub/filename.extension" where sub, filename and extension can
 * contain one or more wildcard "*".
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class TokenizedLibraryPath {

    private static final String DELIMITER = "/";
    private static final String WILDCARD = "*";
    private String rootDirPath;
    private List<String> wildcardSubDirPath = new ArrayList<String>();
    private String fileName;

    /**
     * Default constructor of TokenizedLibraryPath.
     */
    public TokenizedLibraryPath() {
    }

    /**
     * Tokenize the given absolutePath with the default DELIMITER "/".
     *
     * @param absolutePath Path similar to "root/sub/filename.extension"
     */
    public void tokenize(final String absolutePath) {
        tokenize(absolutePath, DELIMITER);
    }

    /**
     * Tokenize the given absolutePath with the given delimiter.
     *
     * @param absolutePath Path similar to "root/sub/filename.extension" if "/" is the delimiter.
     * @param delimiter    the delimiter used in the absolutePath.
     */
    public void tokenize(final String absolutePath, final String delimiter) {
        boolean rootInitialized = false;
        String fileSeparator = System.getProperty("file.separator");
        String fsPrefixe = "";
        if ("/".equals(fileSeparator)){
            fsPrefixe = "/";
        }

        StringTokenizer st = new StringTokenizer(absolutePath, delimiter);
        StringBuilder rootDirPathBuff = new StringBuilder();
        rootDirPathBuff.append(fsPrefixe);
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            if (!st.hasMoreTokens()) {
                fileName = s;
            } else if (s.contains(WILDCARD) || rootInitialized) {
                rootInitialized = true;
                wildcardSubDirPath.add(s);
            } else {
                rootDirPathBuff.append(s).append(fileSeparator);
            }
        }        
        rootDirPath = rootDirPathBuff.toString();
    }

    /**
     * @return root directory path in "root/sub/filename.extension"
     */
    public String getRootDirPath() {
        return rootDirPath;
    }

    /**
     * @param rootDirPath the root directory path to set.
     */
    public void setRootDirPath(String rootDirPath) {
        this.rootDirPath = rootDirPath;
    }

    /**
     * @return the file name and extension in "root/sub/filename.extension"
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the file name to set.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the subs directory in "root/sub/filename.extension"
     */
    public List<String> getWildcardSubDirPath() {
        return Collections.unmodifiableList(wildcardSubDirPath);
    }

    /**
     * @param wildcardSubDirPath the subs directory to set
     */
    public void setWildcardSubDirPath(List<String> wildcardSubDirPath) {
        this.wildcardSubDirPath = wildcardSubDirPath;
    }

    @Override
    public String toString() {
        return "TokenizedLibraryPath[" + getRootDirPath() + ", " + getWildcardSubDirPath().toString() + ", "
                + getFileName() + "]";
    }
}
