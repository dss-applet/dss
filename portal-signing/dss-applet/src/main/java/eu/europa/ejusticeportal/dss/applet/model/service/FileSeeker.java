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

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import eu.europa.ejusticeportal.dss.common.exception.DssLogger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 * This utility class handle the search of specific files on the User's file system.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public final class FileSeeker {

    static final DssLogger LOG = DssLogger.getLogger(FileSeeker.class.getSimpleName());
    /**
     * Singleton reference of FileSeeker.
     */
    private static FileSeeker instance;

    /**
     * Constructs singleton instance of FileSeeker.
     */
    private FileSeeker() {
    }

    /**
     * Provides reference to singleton of FileSeeker.
     *
     * @return Singleton instance of FileSeeker.
     */
    public static FileSeeker getInstance() {
        if (instance == null) {
            instance = new FileSeeker();
        }
        return instance;
    }

    /**
     * Search for a list of Library Path.
     * A single library path can be configured with a wild card, similar to: "root/sub/filename.extension" where sub,
     * filename and extension can contain one ore more wildcard "*".
     *
     * @param listLibraryPath the list of Library Path in which the search will be performed.
     * @return the set of files found.
     */
    public Set<String> search(List<String> listLibraryPath) {
        Set<String> result = new HashSet<String>();
        for (String path : listLibraryPath) {
            result.addAll(search(path));
        }
        return result;
    }

    /**
     * Search for a library Path.
     * A library path can be configured with a wild card, similar to: "root/sub/filename.extension" where sub,
     * filename and extension can contain one ore more wildcard "*".
     * 
     * @param libraryPath the Library Path in which the search will be performed.
     * @return the set of files found.
     */
    public Set<String> search(String libraryPath) {
        LOG.log(Level.FINE, "searching for library path: {0}", libraryPath);
        Set<String> result = new HashSet<String>();

        TokenizedLibraryPath tokenLibPath = new TokenizedLibraryPath();
        tokenLibPath.tokenize(libraryPath);
        LOG.log(Level.FINE, "tokenized={0}", tokenLibPath.toString());
        WildcardFileFilter fileFilter = new WildcardFileFilter(tokenLibPath.getFileName(),IOCase.INSENSITIVE);
        final File rootDir = new File(tokenLibPath.getRootDirPath());
 
        if (rootDir.exists()) {
            LOG.log(Level.FINE, "Existing root directory: {0}", rootDir.getAbsolutePath());
            final IOFileFilter dirFilter;
            if (tokenLibPath.getWildcardSubDirPath().isEmpty()){
                dirFilter = null; 
            } else {
                dirFilter = new WildcardDirectoryFilter(rootDir,tokenLibPath.getWildcardSubDirPath());
            }
            result.addAll(search(fileFilter, rootDir, dirFilter));
        }
        return result;
    }

    /**
     * Search for files
     * @param fileFilter the filter for the file name
     * @param rootDir the root of the search
     * @param dirFilter the filter for the directories
     * @return the set of files found
     */
    private Set<String> search(WildcardFileFilter fileFilter, final File rootDir, IOFileFilter dirFilter) {
        Set<String> result = new HashSet<String>();
        LOG.log(Level.FINE,"Listing files for {0}, {1}, {2}",new Object[]{fileFilter, rootDir, dirFilter});        
        for (File f:FileUtils.listFiles(rootDir, fileFilter, dirFilter)){
            result.add(f.getAbsolutePath());
            LOG.log(Level.FINE, "Found matching file: {0}", f.getAbsolutePath());
        }
        return result;
    }
    /**
     * Filter for wildcard directories
     */
    private static class WildcardDirectoryFilter extends WildcardFileFilter {

        private static final long serialVersionUID = 1L;
        private String root;
        private List<String> wildcards;
        /**
         * 
         * The constructor for WildcardDirectoryFilter.
         * @param rootDir the root directory of the search
         * @param wildcards the wildcards, in path order
         */
        public WildcardDirectoryFilter(File rootDir, List<String> wildcards) {
            super(wildcards);
            WildcardDirectoryFilter.this.root = rootDir.getAbsolutePath();
            WildcardDirectoryFilter.this.wildcards = wildcards;
        }

        /* (non-Javadoc)
         * @see org.apache.commons.io.filefilter.WildcardFileFilter#accept(java.io.File, java.lang.String)
         */
        @Override
        public boolean accept(File dir, String name) {
            if (dir.isDirectory()){
                String dirName = dir.getAbsolutePath();
                if (!dirName.startsWith(root)){
                    return false;
                }
                dirName = dirName.substring(root.length());
                if (dirName.startsWith(File.separator)){
                    dirName = dirName.substring(1);
                }
                String []parts = dirName.split("\\"+File.separator);
                for (int i=0;i<parts.length;i++){
                    if (i<wildcards.size()){
                        return FilenameUtils.wildcardMatch(parts[i], wildcards.get(i),IOCase.INSENSITIVE);
                    }
                }
               return true;
            }
            return false;
        }

        /* (non-Javadoc)
         * @see org.apache.commons.io.filefilter.WildcardFileFilter#accept(java.io.File)
         */
        @Override
        public boolean accept(File file) {
            return accept(file,null);
        }

        
    }

}
