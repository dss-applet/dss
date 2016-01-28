package eu.europa.ejusticeportal.dss.warsigning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Test class testing the update of the Codebase property of a MANIFEST file
 */
public class ManifestUpdateTest {

    /**
     * Tests the update of the codebase.
     */
    @Test
    public void updateCodebase() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {

        WarSigning underTest = new WarSigning();

        File originalManifest = new File(getClass().getResource("/MANIFEST.MF").getFile());
        File expectedResult = new File(getClass().getResource("/MANIFEST.MF.codebaseUpdated").getFile());
        File codebaseUpdateFile = new File(originalManifest.getAbsolutePath()+".codebase.edit");
        codebaseUpdateFile.deleteOnExit();
        copyFile(originalManifest, codebaseUpdateFile);

        //Update the codebase parameter
        Class[] parameterTypes = new Class[3];
        parameterTypes[0] = File.class;
        parameterTypes[1] = String.class;
        parameterTypes[2] = String.class;
        Method updateCodebaseMethod = underTest.getClass().getDeclaredMethod("updateCodebaseFromManifest", parameterTypes);
        updateCodebaseMethod.setAccessible(true);
        Object[] parameters = new Object[3];
        parameters[0] = codebaseUpdateFile;
        parameters[1] = WarSigning.CODEBASE;
        parameters[2] = "localhost:8080";
        updateCodebaseMethod.invoke(underTest, parameters);

        //Update the caller allowable codebase parameter
        parameterTypes = new Class[3];
        parameterTypes[0] = File.class;
        parameterTypes[1] = String.class;
        parameterTypes[2] = String.class;
        Method updateCallerAllowableCodebaseMethod = underTest.getClass().getDeclaredMethod("updateCodebaseFromManifest", parameterTypes);
        updateCallerAllowableCodebaseMethod.setAccessible(true);
        parameters = new Object[3];
        parameters[0] = codebaseUpdateFile;
        parameters[1] = WarSigning.CALLER_ALLOWABLE_CODEBASE;
        parameters[2] = "localhost:8080";
        updateCallerAllowableCodebaseMethod.invoke(underTest, parameters);

        InputStream isExpected = new FileInputStream(expectedResult);
        InputStream is = new FileInputStream(codebaseUpdateFile);
        Assert.assertTrue("The content should be the same.", IOUtils.contentEquals(isExpected, is));
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(isExpected);
    }

    /**
     * Tests the removal of the signatures.
     */
    @Test
    public void removeSignature() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {

        WarSigning underTest = new WarSigning();

        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = File.class;
        Method removeManifestSignatureMethod = underTest.getClass().getDeclaredMethod("removeManifestSignature", parameterTypes);
        removeManifestSignatureMethod.setAccessible(true);

        File originalManifest = new File(getClass().getResource("/MANIFEST.MF").getFile());
        File expectedResult = new File(getClass().getResource("/MANIFEST.MF.withoutSignature").getFile());

        File withoutSignature = new File(originalManifest.getAbsolutePath()+".withoutSignature.edit");
        withoutSignature.deleteOnExit();
        copyFile(originalManifest, withoutSignature);

        Object[] parameters = new Object[1];
        parameters[0] = withoutSignature;
        removeManifestSignatureMethod.invoke(underTest, parameters);

        InputStream isExpected = new FileInputStream(expectedResult);
        InputStream is = new FileInputStream(withoutSignature);
        Assert.assertTrue("The content should be the same.", IOUtils.contentEquals(isExpected, is));
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(isExpected);

    }

    private void copyFile(File in, File out) throws IOException {
        InputStream is = new FileInputStream(in);
        OutputStream os = new FileOutputStream(out);
        IOUtils.copy(is, os);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(os);
    }

}
