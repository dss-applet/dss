package eu.europa.ejusticeportal.dss.warsigning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * 
 * Utils class with static methods.
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1071 $ - $Date: 2013-03-04 11:57:27 +0100 (Mon, 04 Mar 2013) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class Utils {
    
    
    /**
     * This methods deletes recursively the given directory.
     * 
     * @param directory the directory to delete.
     */
    public static void emptyDirectory(File directory){
        try {
            FileUtils.deleteDirectory(directory);
        } catch (IOException e) {
            System.out.println("Unable to delete folder "+directory.getAbsolutePath());
            e.printStackTrace(System.out);
        }
    }
    
    
    /**
     * This method returns a {@link File} from the path.
     * Checks if the file is existing.
     * @param path the path of the file.
     * @return the existing {@link File}.
     * @throws IOException in case the file cannot be found.
     */
    public static File getExistingFile(String path) throws IOException{
        File file = new File(path);
        if(!file.exists()){
            throw new IOException("Cannot find the file '"+path+"'");
        }
        return file;
    }

    /**
     * This methods unpacks an archive into a given directory. The directory is created if it does not exist.
     * @param archive the archive to be unpacked.
     * @param directory the directory in which the archive must be unpacked.
     * @throws IOException any IO exception due to the extraction.
     */
    public static void unpackToDirectory(File archive, File directory) throws IOException{
        JarFile jar = new JarFile(archive);
        Enumeration<JarEntry> enumEntries = jar.entries();
        while (enumEntries.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumEntries.nextElement();
            File file = new File(directory.getAbsolutePath()+ File.separator+ jarEntry.getName());
            if (jarEntry.isDirectory()) {// if its a directory, create it 
                file.mkdirs();
                continue;
            }else{// If it's a final leaf, create the hierarchy.
                File parent = file.getParentFile();
                parent.mkdirs();
            }
            copyFile(jar.getInputStream(jarEntry), new FileOutputStream(file));
        }
        jar.close();//Close the archive.
    }
    
    /**
     * This methods copy the content of the inputStream to a physical file. 
     * @param inputStream the inputStream to read.
     * @param outputStream the outputStream to write into. 
     * @throws IOException and exception due to the copy process.
     */
    private static void  copyFile(InputStream inputStream, OutputStream outputStream) throws IOException{
        try {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }
    
    /**
     * This methods archives the content of a given folder.
     * @param folderToArchive the folder to archive.
     * @param archiveFile the archive file to created.
     * @throws IOException any exception due to the archive process.
     */
    public static void zipFolder(File folderToArchive, File archiveFile) throws IOException {
        ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(archiveFile));
        zipDir(folderToArchive, outputStream, "");
        IOUtils.closeQuietly(outputStream);
    }

    /**
     * This methods archives the content of the given directory under a certain path in the archive.
     * @param directory the directory to archive.
     * @param outputStream the archive output stream.
     * @param path the path within the archive.
     * @throws IOException any IO Exception due to the archive process.
     */
    private static void zipDir(File directory, ZipOutputStream outputStream, String path) throws IOException {
        File zipDir = directory;
        // get a listing of the directory content
        String[] dirList = zipDir.list();
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        // loop through dirList, and zip the files
        for (int i = 0; i < dirList.length; i++) {
            File f = new File(zipDir, dirList[i]);
            if (f.isDirectory()) {
                zipDir(new File(f.getPath()), outputStream, path + f.getName() + "/");
                continue;
            }
            FileInputStream fis = new FileInputStream(f);
            try {
                ZipEntry anEntry = new ZipEntry(path + f.getName());
                outputStream.putNextEntry(anEntry);
                bytesIn = fis.read(readBuffer);
                while (bytesIn != -1) {
                    outputStream.write(readBuffer, 0, bytesIn);
                    bytesIn = fis.read(readBuffer);
                }
            } finally {
                fis.close();
            }
        }
    }
    
    public static String getFileNameWithoutExtension(String fileName){
        int lastPointIndex = fileName.lastIndexOf("."); 
        if(lastPointIndex<0){
            return fileName;
        }
        if(lastPointIndex<fileName.length()-1){
            return fileName.substring(0, lastPointIndex);
        }
        return fileName;
    }

}
