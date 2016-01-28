package eu.europa.ejusticeportal.dss.warsigning;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The class aims to read a properties file in order to load the different properties to be used by the tool.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1071 $ - $Date: 2013-03-04 11:57:27 +0100 (Mon, 04 Mar 2013) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class Configuration {

    /**
     * The name of the properties file to be read.
     */
    private static final String PROPERTIES_FILE = "warsigning.properties";
    /**
     * The working directory in which the war file must be unpacked.
     */
    private static final String WORKING_DIRECTORY = "working";
    /**
     * The applet directory in the exploded war.
     */
    private static final String APPLET_JAR_DIRECTORY = "dss_applet";

    private File originalWarFile;
    private File newWarFile;
    private File workingDirectory;
    private File appletJarDirectory;
    private String codebase;
    private String jks;
    private String alias;
    private String storepass;
    private String keypass;
    private String jarSigner;
    private File originalFolder;
    private File newFolder;

    private File p12File;
    private String p12Password;

    public Configuration() throws IOException {
        loadProperties();
        initWorkingDirectory();
    }

    /**
     * Loads the properties file.
     * 
     * @throws IOException
     */
    private void loadProperties() throws IOException {
        File file = new File(PROPERTIES_FILE);
        Properties prop = new Properties();
        InputStream inputStream = new FileInputStream(file);
        prop.load(inputStream);

        String originalWarFilePath = prop.getProperty("original.war.file");
        if (originalWarFilePath != null) {
            this.originalWarFile = Utils.getExistingFile(originalWarFilePath);
        }

        String newWarFileNamePath = prop.getProperty("new.war.file.name");
        if (newWarFileNamePath != null) {
            this.newWarFile = new File(newWarFileNamePath);
        }

        this.codebase = prop.getProperty("codebase");
        this.jks = prop.getProperty("jks");
        this.alias = prop.getProperty("alias");
        this.storepass = prop.getProperty("storepass");
        this.keypass = prop.getProperty("keypass");
        this.jarSigner = prop.getProperty("jarsigner");

        // p12 file and password
        String p12FilePath = prop.getProperty("p12.file");
        this.p12File = Utils.getExistingFile(p12FilePath);
        this.p12Password = prop.getProperty("p12.password");
        String originalFolderPath = prop.getProperty("original.folder");
        if (originalFolderPath != null) {
            this.originalFolder = Utils.getExistingFile(originalFolderPath);
        }
        String newFolderPath = prop.getProperty("new.folder");
        if (newFolderPath != null) {
            this.newFolder = new File(newFolderPath);
            this.appletJarDirectory = newFolder;
        }

        if (originalWarFile != null && originalFolder != null) {
            throw new RuntimeException("Please provide wither original.folder OR original.war.file");
        }

    }

    /**
     * This initialises the working directory. If the directory does not exist, it creates it.
     * 
     * @throws IOException
     */
    private void initWorkingDirectory() throws IOException {
        this.workingDirectory = new File(WORKING_DIRECTORY);
        if (workingDirectory.exists()) {
            return;
        }
        // If the directory does not exist, create it.
        if (!workingDirectory.mkdirs()) {
            throw new IOException("Cannot create the temporary folder '" + workingDirectory.getAbsolutePath() + "'");
        }
    }

    /**
     * This methods returns the applet directory in the exploded war directory.
     * 
     * @return the applet directory in the exploded war directory.
     */
    public File getAppletJarDirectory() {
        if (appletJarDirectory == null) {
            appletJarDirectory = new File(WORKING_DIRECTORY, APPLET_JAR_DIRECTORY);
        }
        if (!appletJarDirectory.exists()) {
            throw new IllegalArgumentException("The directory '" + appletJarDirectory.getAbsolutePath()
                    + "' does not exist.");
        }
        if (!appletJarDirectory.isDirectory()) {
            throw new IllegalArgumentException("The directory '" + appletJarDirectory.getAbsolutePath()
                    + "' is not a directory.");
        }

        return appletJarDirectory;
    }

    /**
     * @return the workingDirectory
     */
    public File getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * @param workingDirectory the workingDirectory to set
     */
    public void setWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    /**
     * @return the originalWarFile
     */
    public File getOriginalWarFile() {
        return originalWarFile;
    }

    /**
     * @param originalWarFile the originalWarFile to set
     */
    public void setOriginalWarFile(File originalWarFile) {
        this.originalWarFile = originalWarFile;
    }

    /**
     * @return the codebase
     */
    public String getCodebase() {
        return codebase;
    }

    /**
     * @param codebase the codebase to set
     */
    public void setCodebase(String codebase) {
        this.codebase = codebase;
    }

    /**
     * @return the jks
     */
    public String getJks() {
        return jks;
    }

    /**
     * @param jks the jks to set
     */
    public void setJks(String jks) {
        this.jks = jks;
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * @return the storepass
     */
    public String getStorepass() {
        if (storepass == null || storepass.isEmpty()) {
            System.out.println("Please provide the password of the Java keystore:");
            try {
                // Scanner scanner = new Scanner(System.in);
                Console console = System.console();
                storepass = new String(console.readPassword());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return storepass;
    }

    /**
     * @param storepass the storepass to set
     */
    public void setStorepass(String storepass) {
        this.storepass = storepass;
    }

    /**
     * @return the keypass
     */
    public String getKeypass() {
        if (keypass == null || keypass.isEmpty()) {
            System.out.println("Please provide the key password:");
            try {
                Console console = System.console();
                keypass = new String(console.readPassword());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return keypass;
    }

    /**
     * @param keypass the keypass to set
     */
    public void setKeypass(String keypass) {
        this.keypass = keypass;
    }

    /**
     * @return the jarSigner
     */
    public String getJarSigner() {
        return jarSigner;
    }

    /**
     * @param jarSigner the jarSigner to set
     */
    public void setJarSigner(String jarSigner) {
        this.jarSigner = jarSigner;
    }

    /**
     * @return the newWarFile
     */
    public File getNewWarFile() {
        return newWarFile;
    }

    /**
     * @param newWarFile the newWarFile to set
     */
    public void setNewWarFile(File newWarFile) {
        this.newWarFile = newWarFile;
    }

    /**
     * @return the p12File
     */
    public File getP12File() {
        return p12File;
    }

    /**
     * @param p12File the p12File to set
     */
    public void setP12File(File p12File) {
        this.p12File = p12File;
    }

    /**
     * @return the p12Password
     */
    public String getP12Password() {
        if (p12Password == null || p12Password.isEmpty()) {
            System.out.println("Please provide the password for the PKCS12 certificate file:");
            try {
                Console console = System.console();
                p12Password = new String(console.readPassword());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return p12Password;
    }

    /**
     * @param p12Password the p12Password to set
     */
    public void setP12Password(String p12Password) {
        this.p12Password = p12Password;
    }

    /**
     * @return the originalFolder
     */
    public File getOriginalFolder() {
        return originalFolder;
    }

    /**
     * @return the newFolder
     */
    public File getNewFolder() {
        return newFolder;
    }
}