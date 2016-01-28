package eu.europa.ejusticeportal.dss.warsigning;

import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.token.Pkcs12SignatureToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMEncryptor;
import org.bouncycastle.openssl.jcajce.JcaMiscPEMGenerator;
import org.bouncycastle.util.io.pem.PemWriter;

/**
 * Tool allowing to resign the DSS applet jar files. 
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1071 $ - $Date: 2013-03-04 11:57:27 +0100 (Mon, 04 Mar 2013) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class WarSigning {


    public final static String CODEBASE = "Codebase: ";
    public final static String CALLER_ALLOWABLE_CODEBASE = "Caller-Allowable-Codebase: ";
    
    /**
     * The configuration containing the properties to be used by the tool.
     */
    private Configuration configuration;
    
    public static void main (String[] args){
        WarSigning warSigning = new WarSigning();
        try {
            warSigning.run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        
    }

    public void run() throws IOException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException{
        //Load the configuration from the properties file.
        this.configuration = new Configuration();
        
        //Delete the working directory if it exists.
        Utils.emptyDirectory(configuration.getWorkingDirectory());
        
        //Unarchive the war file into the working directory.
        if (configuration.getOriginalWarFile()!=null) {
            unpackWar();
        } else {
            FileUtils.copyDirectory(configuration.getOriginalFolder(),configuration.getNewFolder());
        }
        
        //Lists the applet jar files to be resigned.
        List<File> appletJarFiles = getAppletsJarFiles();
        
        //Unarchive each applet jar file into folders. One foler by jar file.
        List<File> jarFolders = unpackAppletJarFiles(appletJarFiles);
        
        //Deletes the RSA and SF files for every applet jar folder.
        deleteRsaAndSfFiles(jarFolders);
        
        //Update the MANIFEST of every applet jar folder.
        updateManifestFiles(jarFolders);
        
        //Generates the pemFile from the p12 file and put in the applet jars.
        copyPemFileIntoAppletResources(jarFolders);
        
        //Archive all the unarchived folder.
        appletJarFiles = repackJarFolders(jarFolders); 
        
        //Sign every applet jar file using the properties.
        signJars(appletJarFiles);
        
        if (configuration.getOriginalFolder()==null) {
            //Archive the content of the working directory to a new archive.
            Utils.zipFolder(configuration.getWorkingDirectory(), configuration.getNewWarFile());
            System.out.println("\n\nThe war file '"+configuration.getNewWarFile()+"' has been signed successfully.");            
            //Delete the working directory.
            Utils.emptyDirectory(configuration.getWorkingDirectory());
        }
        
    }
    
    /**
     * This method unarchives the war file into the working directory.
     * @throws IOException
     */
    private void unpackWar() throws IOException{
        System.out.println("\nUnpacking the war file '"+configuration.getOriginalWarFile().getAbsolutePath()+"' into '"+configuration.getWorkingDirectory().getAbsolutePath()+"'.");
        Utils.unpackToDirectory(configuration.getOriginalWarFile(), configuration.getWorkingDirectory());
    }
    
    /**
     * This methods returns the list of the applet jar files.
     * @return list of the applet jar files.
     */
    private List<File> getAppletsJarFiles(){
        List<File> jarFiles = new ArrayList<File>();
        
        //Fetch the appletFolder.
        
        File appletFolder;
        if (configuration.getOriginalFolder()==null){
           appletFolder = configuration.getAppletJarDirectory();
        } else {
            appletFolder = configuration.getNewFolder();
        }
        
        //Search for the ".jar" files in the applet folder. 
        File [] list = appletFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                //Only take the jar files.
                if(pathname.getName().toLowerCase().endsWith(".jar")){
                    return true;
                }
                return false;
            }
        });
        for(File f : list){
            jarFiles.add(f);
        }
        return jarFiles;
    }
    
    /**
     * For every applet jar file, unarchive it into a directory with the same name as the jar file.
     * The jar file is deleted at the end.
     * @param appletJarFiles the list of jar files to unarchive.
     * @return the list of the directories containing the content of the archives.
     * @throws IOException
     */
    private List<File> unpackAppletJarFiles(List<File> appletJarFiles) throws IOException{
        System.out.println("\nUnpacking the applet jar files:");
        List<File> jarList = new ArrayList<File>();
        for(File jarFile : appletJarFiles){
            File newFileLocation = new File(jarFile.getAbsolutePath()+".jar");
            if(!jarFile.renameTo(newFileLocation)){
                throw new IOException("Impossible to rename the file '"+jarFile.getAbsolutePath()+"' into '"+newFileLocation.getAbsolutePath()+"'");
            }
            File jarFolder = new File (configuration.getAppletJarDirectory(), jarFile.getName());
            if(!jarFolder.mkdirs()){
                throw new IOException("Impossible to create the folder '"+jarFolder.getAbsolutePath()+"'");
            }
            System.out.println("  - Unpacking the file '"+jarFile.getAbsolutePath()+"' to '"+jarFolder.getAbsolutePath()+"'.");
            Utils.unpackToDirectory(newFileLocation, jarFolder);
            if(!newFileLocation.delete()){
                throw new IOException("Impossible to delete the file '"+newFileLocation.getAbsolutePath()+"'");
            }
            jarList.add(jarFolder);
        }
        return jarList;
    }
    
    /**
     * Deletes the RSA and SF files from the directory META-INF.
     * @param jarFolderList the directory list in which the files must be deleted.
     * @throws IOException
     */
    private void deleteRsaAndSfFiles(List<File> jarFolderList) throws IOException{
        System.out.println("\nDeleting the RAS and SF files:");
        final List<File> toDelete = new ArrayList<File>();
        for(File jarFolder : jarFolderList){
            //Fetch the META-INF directory
            File metaInfFolder = new File(jarFolder, "META-INF");
            if(!metaInfFolder.exists()){
                throw new IllegalArgumentException("The folder '"+metaInfFolder.getAbsolutePath()+"' does not exist.");
            }
            //Lists the ".rsa" and ".sf" files to be deleted.
            metaInfFolder.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if(pathname.getName().toLowerCase().endsWith(".rsa") || pathname.getName().toLowerCase().endsWith(".sf")){
                        toDelete.add(pathname);
                    }
                    return false;
                }
            });
        }
        //Physically deletes the files.
        for(File f : toDelete){
            System.out.println("  - Deleting the file '"+f.getAbsolutePath()+"'");
            if(!f.delete()){
                throw new IOException("Impossible to delete the file '"+f.getAbsolutePath()+"'");
            }
        }
    }

    /**
     * This methods updates the MANIFEST.MF files for every applet jar directory.
     * The SHA are removed.
     * The Codebase is replaced (if any) by the one coming from the properties file.
     * @param jarFolderList the directory list for which the MANIFEST.MF files must be updated.
     * @throws IOException
     */
    private void updateManifestFiles(List<File> jarFolderList) throws IOException{
        System.out.println("\nUpdating manifests:");
        for(File jarFolder : jarFolderList){
            //Fetch the META-INF folder
            File metaInfFolder = new File(jarFolder, "META-INF");
            if(!metaInfFolder.exists()){
                throw new IllegalArgumentException("The folder '"+metaInfFolder.getAbsolutePath()+"' does not exist.");
            }
            
            //Fetch the MANIFEST.MF file
            File manifestFile = new File(metaInfFolder, "MANIFEST.MF");
            removeManifestSignature(manifestFile);
            updateCodebaseFromManifest(manifestFile, CODEBASE, configuration.getCodebase());
            updateCodebaseFromManifest(manifestFile, CALLER_ALLOWABLE_CODEBASE, configuration.getCodebase());

        }
    }

    private void removeManifestSignature(File manifestFile) throws IOException {
        System.out.println("  - Removing signatures from manifest '"+manifestFile.getAbsolutePath()+"'");
        //Change the name.
        File copiedManifestFile = new File(manifestFile.getAbsolutePath()+".MF");

        //Create a new MANIFEST.MF that will be the updated file.
        if(!manifestFile.renameTo(copiedManifestFile)){
            throw new IOException("Impossible to rename the file '"+manifestFile.getAbsolutePath()+"' into '"+copiedManifestFile+"'");
        }

        //Update the file.
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(copiedManifestFile), "UTF8"));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(manifestFile), "UTF8"));
        String str;
        boolean removeTheRest = false;
        while ((str = in.readLine()) != null) {
            //Remove the lines after "Name :"
            if(str.startsWith("Name: ")){
                removeTheRest = true;
            }

            if(!removeTheRest){
                out.write(str);
                out.newLine();
            }
        }
        IOUtils.closeQuietly(in);
        IOUtils.closeQuietly(out);

        //Delete the copied MANIFEST.MF file.
        if(!copiedManifestFile.delete()){
            throw new IOException("Impossible to delete the file '"+copiedManifestFile.getAbsolutePath()+"'");
        }
    }

    private void updateCodebaseFromManifest(File manifestFile, String key, String newValue) throws IOException {
        System.out.println("  - Updating '"+key+"' with value '"+newValue+"' from manifest '"+manifestFile.getAbsolutePath()+"'");
        //Change the name.
        File copiedManifestFile = new File(manifestFile.getAbsolutePath()+".MF");

        //Create a new MANIFEST.MF that will be the updated file.
        if(!manifestFile.renameTo(copiedManifestFile)){
            throw new IOException("Impossible to rename the file '"+manifestFile.getAbsolutePath()+"' into '"+copiedManifestFile+"'");
        }

        //Update the file.
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(copiedManifestFile), "UTF8"));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(manifestFile), "UTF8"));
        String str;
        boolean inCodebase = false;
        boolean writeLine = true;
        while ((str = in.readLine()) != null) {
            writeLine = true;
            //Replace the codebase value.
            if(str.startsWith(key)){
                str = formatCodebase(key,newValue);
                inCodebase=true;
            }else {
                if (inCodebase) {
                    if (str.startsWith(" ")) {
                        writeLine = false;
                    } else {
                        inCodebase = false;
                    }
                }
            }
            if(writeLine) {
                out.write(str);
                out.newLine();
            }
        }
        IOUtils.closeQuietly(in);
        IOUtils.closeQuietly(out);

        //Delete the copied MANIFEST.MF file.
        if(!copiedManifestFile.delete()){
            throw new IOException("Impossible to delete the file '"+copiedManifestFile.getAbsolutePath()+"'");
        }
    }
    
    
    private String formatCodebase(String key, String newValue) throws IOException {
        Manifest mf = new Manifest();        
        Attributes attr = new Attributes();
        
        attr.putValue(key.substring(0,key.indexOf(":",0)).trim(), newValue);
        mf.getEntries().put("local", attr);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mf.write(baos);
        String s = new String(baos.toByteArray());
        String [] ss = s.split("\n");
        boolean write = false;
        StringBuilder codebase = new StringBuilder();
        for (String line:ss){
            if (line.toLowerCase().contains(key.toLowerCase())){
                write = true;
            }
            if (write&& !line.trim().isEmpty()){
                codebase.append("\n");
                codebase.append(line);
            }
        }        
        return codebase.toString().trim();
    }

    /**
     * This methods archive every directory by using the original archive name .jar.
     * @param jarFolderList the directories to be archived. 
     * @return the list of the jar files.
     * @throws IOException
     */
    private List<File> repackJarFolders(List<File> jarFolderList) throws IOException{
        System.out.println("\nRepacking the jar folders:");
        List<File> jarFileList = new ArrayList<File>();
        for(File jarFolder : jarFolderList){
            //Get the directory name without the extension ".jar"
            String path = jarFolder.getAbsolutePath();
            path = path.substring(0, path.indexOf(".jar"));
            File jarFile = new File(path);
            System.out.println("  - Repacking the directory '"+jarFolder.getAbsolutePath()+"' to the file : '"+jarFile.getAbsolutePath()+"'.");

            //Archive the directory (still without extension)
            Utils.zipFolder(jarFolder, jarFile);

            //Delete the archived directory
            Utils.emptyDirectory(jarFolder);
            
            //Rename the archive by adding the extension ".jar"
            File newFile = new File(path+".jar");
            if(!jarFile.renameTo(newFile)){
                throw new IOException("Impossible to rename the file '"+jarFile.getAbsolutePath()+"' into '"+newFile.getAbsolutePath()+"'");
            }
            jarFileList.add(newFile);
            
        }
        return jarFileList;
    }

    /**
     * This methods signs every applet jar file using the jarsigner properties specified in the properties file.
     * @param appletJarFiles the list of the jar files to be signed.
     * @throws IOException
     * @throws InterruptedException
     */
    private void signJars(List<File> appletJarFiles) throws IOException, InterruptedException{
        BufferedReader in = null;
        System.out.println("\nSigning jar files:");
        for(File jarFile : appletJarFiles){
            System.out.println("  - Signing jar file "+jarFile.getAbsolutePath());
            ArrayList<String> args = new ArrayList<String>();
            args.add(configuration.getJarSigner());
            args.add("-keystore");
            args.add(configuration.getJks());
            args.add("-storepass");
            args.add(configuration.getStorepass());
            args.add("-keypass");
            args.add(configuration.getKeypass());
            args.add(jarFile.getAbsolutePath());
            args.add(configuration.getAlias());

            Process pro = Runtime.getRuntime().exec(args.toArray(new String[args.size()]));
            //Print process log
            String line = null;
            in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            while ((line = in.readLine()) != null) {
                System.out.println("     "+line);
            }
            // Wait process end
            pro.waitFor();
        }
    }

    private static File extractPemFromCertificate(File p12File, String password) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException{
        Security.addProvider(new BouncyCastleProvider());
        
        //Load the signing certificate 
        InputStream isP12 = (new FileInputStream(p12File));
        Pkcs12SignatureToken token = new Pkcs12SignatureToken(password,isP12);  
        //Assume that the PKCS12 container only has one certificate in it
        DSSPrivateKeyEntry privateKey = token.getKeys().get(0);

        StringWriter writer = new StringWriter();
        PemWriter newPemWriter = new PemWriter(writer);
        newPemWriter.writeObject(new JcaMiscPEMGenerator(privateKey.getCertificate(), (PEMEncryptor)null));
        newPemWriter.close();
        String certInPemFormat = writer.toString();
        
        File pemFile = new File("certificate.pem");
        OutputStream out = new FileOutputStream(pemFile);
        IOUtils.write(certInPemFormat, out);
        IOUtils.closeQuietly(out);
        
        return pemFile;
    }
    
    /**
     * This methods reads p12 certificate, extracts the pem certificate into 'certificate.pem' and copy it in the 
     * applet resource.
     * 
     * @param appletFolders
     * @throws IOException 
     * @throws SignatureException 
     * @throws NoSuchProviderException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    private void copyPemFileIntoAppletResources(List<File> appletFolders) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException, IOException{
        System.out.println("\nExtracting the pem file and adding it to the applet.");
        File pemFile = extractPemFromCertificate(configuration.getP12File(), configuration.getP12Password());
        for(File folder : appletFolders){
            if(!folder.getName().toLowerCase().startsWith("ej-portal-dss-applet")){
                continue;
            }
            File outputFile = new File(folder.getAbsolutePath()+File.separator+pemFile.getName());
            InputStream is = new FileInputStream(pemFile);
            OutputStream os = new FileOutputStream(outputFile);
            IOUtils.copy(is, os);
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
        if(!pemFile.delete()){
            throw new IOException("Impossible to delete the file '"+pemFile.getAbsolutePath()+"'");
        }
    }
    
}
