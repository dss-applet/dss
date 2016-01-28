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
package eu.europa.ejusticeportal.dss.controller.signature;

import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.parameter.SignatureParameters;
import eu.europa.ec.markt.dss.signature.DSSDocument;
import eu.europa.ec.markt.dss.signature.InMemoryDocument;
import eu.europa.ec.markt.dss.signature.SignatureLevel;
import eu.europa.ec.markt.dss.signature.SignaturePackaging;
import eu.europa.ec.markt.dss.signature.pades.PAdESService;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.token.SignatureTokenConnection;
import eu.europa.ec.markt.dss.validation102853.AdvancedSignature;
import eu.europa.ec.markt.dss.validation102853.CommonCertificateVerifier;
import eu.europa.ec.markt.dss.validation102853.SignedDocumentValidator;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.controller.SealMethod;
import eu.europa.ejusticeportal.dss.controller.exception.SigningBusinessException;
import eu.europa.ejusticeportal.dss.controller.exception.SigningException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.common.COSArrayList;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public final class PdfUtils {

    private static final String DIGEST_FILE_NAME = "digest.txt";
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfUtils.class);

    private static final String SEP = "####################################################";

    /**
     * The constructor for PdfUtils.
     */
    private PdfUtils() {

    }

    /**
     * Adds an attachment to a PDF
     * 
     * @param pdf the PDF to attach to
     * @param attachment to attachment
     * @param name the name given to the attachment
     * @return the PDF with attachment
     */
    public static byte[] attach(final byte[] pdf, final byte[] attachment, final String name) {
        PDDocument doc = null;
        try {

            PDEmbeddedFilesNameTreeNode efTree = new PDEmbeddedFilesNameTreeNode();

            InputStream isDoc = new ByteArrayInputStream(pdf);
            doc = PDDocument.load(isDoc);
            PDComplexFileSpecification fs = new PDComplexFileSpecification();
            fs.setFile("Test.txt");
            InputStream isAttach = new ByteArrayInputStream(attachment);
            PDEmbeddedFile ef = new PDEmbeddedFile(doc, isAttach);
            ef.setSize(attachment.length);
            ef.setCreationDate(Calendar.getInstance());
            fs.setEmbeddedFile(ef);

            Map<String, PDComplexFileSpecification> efMap = new HashMap<String, PDComplexFileSpecification>();
            efMap.put(name, fs);
            efTree.setNames(efMap);
            PDDocumentNameDictionary names = new PDDocumentNameDictionary(doc.getDocumentCatalog());
            names.setEmbeddedFiles(efTree);
            doc.getDocumentCatalog().setNames(names);
            return toByteArray(doc);
        } catch (Exception e) {
            LOGGER.error("Error attaching.", e);
            throw new SigningException(e);
        } finally {
            closeQuietly(doc);
        }
    }

    private static void closeQuietly(PDDocument doc) {
        if (doc != null) {
            try {
                doc.close();
            } catch (IOException e) {
                LOGGER.error("Error closing the document.", e);
            }
        }

    }

    /**
     * Extracts an attachment from a PDF
     * 
     * @param pdf the PDF; this is unaffected by the method
     * @param name the name of the attachment
     * @return the attachment
     */
    public static byte[] extractAttachment(final byte[] pdf, final String name) {
        PDDocument doc = null;
        try {
            InputStream is = new ByteArrayInputStream(pdf);
            doc = PDDocument.load(is);
            PDDocumentNameDictionary namesDictionary = new PDDocumentNameDictionary(doc.getDocumentCatalog());
            PDEmbeddedFilesNameTreeNode efTree = namesDictionary.getEmbeddedFiles();
            Map<String, COSObjectable> names = efTree.getNames();
            byte[] data = null;
            if (names != null) {
                PDComplexFileSpecification attach = (PDComplexFileSpecification) names.get(name);
                data = attach.getEmbeddedFile().getByteArray();
            }

            // Remove the \r\n added by PdfBox (PDDocument.saveIncremental).
            if (data != null) {
                String newString = new String(data, "UTF-8");
                while (newString.endsWith("\r\n")) {
                    newString = newString.replaceAll("\\r\\n$", "");
                    data = newString.getBytes(Charset.forName("UTF-8"));
                }
            }

            return data;

        } catch (IOException e) {
            LOGGER.error("Error detaching.", e);
            throw new SigningException(e);
        } finally {
            closeQuietly(doc);
        }
    }

    /**
     * Signs the PDF using the given token.
     * 
     * @param bytes the PDF to sign
     * @param token the token to sign with
     * @return the sealed PDF
     */
    public static byte[] signPdf(final byte[] bytes, final SignatureTokenConnection token) {
        InputStream is = null;
        try {
            Security.addProvider(new BouncyCastleProvider());
            CommonCertificateVerifier certificateVerifier = new CommonCertificateVerifier();
            final PAdESService service = new PAdESService(certificateVerifier) {
                @Override
                protected void assertSigningDateInCertificateValidityRange(
                        eu.europa.ec.markt.dss.parameter.SignatureParameters parameters) {
                    // we don't care about the date because it's not a real signature
                }
            };
            final DSSPrivateKeyEntry key = token.getKeys().get(0);
            final DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA512;

            final SignatureParameters parameters = new SignatureParameters();
            parameters.setSigningCertificate(key.getCertificate());
            if (key.getCertificateChain() != null) {

                List<X509Certificate> chain = new ArrayList<X509Certificate>();
                for (X509Certificate c : key.getCertificateChain()) {
                    chain.add(c);
                }
                X509Certificate[] chainArray = new X509Certificate[chain.size()];
                chainArray = chain.toArray(chainArray);
                parameters.setCertificateChain(chainArray);
            }
            parameters.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);
            parameters.setSignaturePackaging(SignaturePackaging.ENVELOPED);
            parameters.bLevel().setSigningDate(new Date());
            parameters.setDigestAlgorithm(digestAlgorithm);
            parameters.setSigningToken(token);
            parameters.setPrivateKeyEntry(key);
            final DSSDocument document = new InMemoryDocument(bytes);
            final DSSDocument doc = service.signDocument(document, parameters);
            is = doc.openStream();
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            throw new SigningException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }

    }

    /**
     * Custom implementation for sealing the PDF. We don't use the standard PADES signature because we can't use a
     * qualified signature. So we make a hash of the PDF/attached XML, encrypt it, embed it in a text document, and
     * store the whole thing as an attachment in the PDF
     * 
     * @param bytes the PDF document bytes
     * @param token the {@link SignatureTokenConnection} to encrypt the digest
     * @param disclaimer text that is included in the digest, to explain what it is for
     * @return the bytes for the sealed PDF
     */
    public static byte[] sealPDFCustom(final byte[] bytes, final SignatureTokenConnection token, String disclaimer) {
        byte[] signed;
        try {
            signed = token.sign(bytes, DigestAlgorithm.SHA256, token.getKeys().get(0));
            signed = wrapDigest(signed, disclaimer);
        } catch (IOException e) {
            throw new SigningException(e);
        }
        return appendAttachment(bytes, signed, DIGEST_FILE_NAME);
    }

    /**
     * Signs the PDF using the given token.
     * 
     * @param bytes the PDF to sign
     * @param token the token to sign with
     * @return the sealed PDF
     * @throws KeyStoreException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static byte[] sealPdf(final byte[] bytes, final SignatureTokenConnection token) throws KeyStoreException,
            IOException, NoSuchAlgorithmException {
        InputStream is = null;
        try {
            Security.addProvider(new BouncyCastleProvider());
            CommonCertificateVerifier certificateVerifier = new CommonCertificateVerifier();
            final PAdESService service = new PAdESService(certificateVerifier) {
                // TODO fix this
                @Override
                protected void assertSigningDateInCertificateValidityRange(
                        eu.europa.ec.markt.dss.parameter.SignatureParameters parameters) {
                    // TODO Auto-generated method stub
                    // super.assertSigningDateInCertificateValidityRange(parameters);
                }
            };
            final DSSPrivateKeyEntry key = token.getKeys().get(0);
            final DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA512;

            final SignatureParameters parameters = new SignatureParameters();
            parameters.setPrivateKeyEntry(key);// Must be set before the certificate. Otherwise -->
                                               // UnsupportOperationException.
            parameters.setSigningCertificate(key.getCertificate());
            parameters.setCertificateChain(key.getCertificateChain());
            parameters.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);
            parameters.setSignaturePackaging(SignaturePackaging.ENVELOPED);
            parameters.bLevel().setSigningDate(new Date());
            parameters.setDigestAlgorithm(digestAlgorithm);
            parameters.setSigningToken(token);
            final DSSDocument document = new InMemoryDocument(bytes);
            final DSSDocument doc = service.signDocument(document, parameters);
            is = doc.openStream();
            return IOUtils.toByteArray(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    private static byte[] wrapDigest(byte[] digest, String disclaimer) throws IOException {
        String base64 = Base64.encodeBase64String(digest);
        return (disclaimer + "\r\n\r\n" + SEP + "\n" + base64 + "\n" + SEP).getBytes("UTF-8");
    }

    private static byte[] unwrapDigest(byte[] digest) throws IOException {
        InputStream is = new ByteArrayInputStream(digest);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = null;
        StringBuffer unwrapped = null;
        while ((line = reader.readLine()) != null) {
            if (SEP.equals(line)) {
                if (unwrapped == null) {
                    unwrapped = new StringBuffer();
                    continue;
                } else {
                    break;
                }
            }
            if (unwrapped != null) {
                unwrapped.append(line);
                unwrapped.append("\n");
            }
        }
        if (unwrapped == null) {
            throw new SigningBusinessException(MessagesEnum.dss_applet_message_uploaded_pdf_not_from_server);
        }
        return Base64.decodeBase64(unwrapped.toString());

    }

    /**
     * Test if the PDF is correctly sealed using the {@link SealMethod.SEAL}
     * 
     * @return true if sealed, false if not
     */
    public static boolean isSealedSeal(final SignedDocumentValidator val, final SignatureTokenConnection token)
            throws IOException, SignatureException, KeyStoreException, CertificateEncodingException {
        List<AdvancedSignature> signatures = val.getSignatures();
        for (AdvancedSignature signature : signatures) {
            DSSPrivateKeyEntry privateKeyEntry = token.getKeys().get(0);
            if (!Arrays.equals(signature.getSigningCertificateToken().getEncoded(), privateKeyEntry.getCertificate()
                    .getEncoded())) {
                continue;
            } else {
                signature.setDetachedContents(val.getDocument());
                if (!signature.checkSignatureIntegrity().isSignatureIntact()) {
                    LOGGER.error("error verifying the signature - document was modified?");
                    continue;
                }
            }
            return true;
        }

        return false;
    }

    /**
     * Test if the PDF is correctly sealed using the {@link SealMethod.SEAL_CUSTOM}
     * 
     * @return true if sealed, false if not
     */
    public static boolean isSealedCustom(final byte[] pdf, final SignatureTokenConnection token) {
        PDDocument doc = null;
        try {
            InputStream is = new ByteArrayInputStream(pdf);
            doc = PDDocument.load(is);
            boolean sealed = false;
            byte[] originalReceived;
            if (doc.getSignatureDictionaries() != null && doc.getSignatureDictionaries().size() != 0) {
                // Get the original file
                originalReceived = getOriginalBytes(doc, pdf);
            } else {
                originalReceived = pdf;
            }
            // Extract the original digest
            byte[] originalDigest = unwrapDigest(extractAttachment(originalReceived, DIGEST_FILE_NAME));
            // Extract the attachements
            Set<String> attachementNames = getAttachmentsNames(doc);
            Map<String, byte[]> attachements = new HashMap<String, byte[]>();
            for (String attachementName : attachementNames) {
                byte[] bytes = extractAttachment(originalReceived, attachementName);
                attachements.put(attachementName, new String(bytes, "UTF-8").getBytes());
            }
            doc = PDDocument.load(new ByteArrayInputStream(originalReceived));
            // Remove all the attachemente
            doc = removeAllAttachments(doc);
            originalReceived = toByteArray(doc);
            closeQuietly(doc);
            // Append the attachements we just removed. Because PdfBox is adding \r\n
            originalReceived = appendAttachment(originalReceived, attachements, DIGEST_FILE_NAME);
            byte[] checkDigest = token.sign(originalReceived, DigestAlgorithm.SHA256, token.getKeys().get(0));
            sealed = Arrays.equals(originalDigest, checkDigest);
            return sealed;
        } catch (COSVisitorException e) {
            throw new SigningException(e);
        } catch (IOException e) {
            throw new SigningException(e);
        } finally {
            closeQuietly(doc);
        }
    }

    /**
     * Append an attachment to the PDF. This method will only work for attachments with unique names
     * 
     * @param pdf the PDF document
     * @param attachment the bytes for the attachment
     * @param fileName the name of the attachment
     */
    public static byte[] appendAttachment(byte[] pdf, byte[] attachment, String fileName) {

        try {

            InputStream is = new ByteArrayInputStream(pdf);
            PDDocument doc = PDDocument.load(is);
            Map<String, byte[]> attachments = getAttachments(doc);
            doc = removeAllAttachments(doc);
            attachments.put(fileName, attachment);
            PDDocumentNameDictionary names2 = new PDDocumentNameDictionary(doc.getDocumentCatalog());
            Map<String, COSObjectable> efMap = new HashMap<String, COSObjectable>();
            for (String key : attachments.keySet()) {
                PDComplexFileSpecification afs = new PDComplexFileSpecification();
                afs.setFile(key);
                InputStream isa = new ByteArrayInputStream(attachments.get(key));
                PDEmbeddedFile ef = new PDEmbeddedFile(doc, isa);
                ef.setSize(attachments.get(key).length);
                afs.setEmbeddedFile(ef);
                efMap.put(key, afs);
            }

            PDEmbeddedFilesNameTreeNode efTree = new PDEmbeddedFilesNameTreeNode();
            efTree.setNames(efMap);
            names2.setEmbeddedFiles(efTree);
            doc.getDocumentCatalog().setNames(names2);
            return toByteArray(doc);
        } catch (COSVisitorException e) {
            throw new SigningException(e);
        } catch (IOException e) {
            throw new SigningException(e);
        }

    }

    /**
     * Append attachments to the PDF.
     * 
     * @param pdf the PDF document
     * @param attachments the bytes for the attachment
     * @param ignore the names to be ignored
     */
    public static byte[] appendAttachment(byte[] pdf, Map<String, byte[]> attachments, String... ignore) {

        try {
            List<String> ignoreList = Arrays.asList(ignore);
            InputStream is = new ByteArrayInputStream(pdf);
            PDDocument doc = PDDocument.load(is);
            PDEmbeddedFilesNameTreeNode tree = new PDDocumentNameDictionary(doc.getDocumentCatalog())
                    .getEmbeddedFiles();
            if (tree == null) {
                tree = new PDEmbeddedFilesNameTreeNode();
                PDDocumentNameDictionary names = new PDDocumentNameDictionary(doc.getDocumentCatalog());
                names.setEmbeddedFiles(tree);
                doc.getDocumentCatalog().setNames(names);
            }
            Map<String, COSObjectable> temp = tree.getNames();
            Map<String, COSObjectable> map = new HashMap<String, COSObjectable>();
            if (temp != null) {
                map.putAll(temp);
            }
            for (String fileName : attachments.keySet()) {
                if (ignoreList.contains(fileName)) {
                    continue;
                }
                byte[] bytes = attachments.get(fileName);
                if (bytes == null) {
                    continue;
                }
                PDComplexFileSpecification cosObject = new PDComplexFileSpecification();
                cosObject.setFile(fileName);
                InputStream isa = new ByteArrayInputStream(bytes);
                PDEmbeddedFile ef = new PDEmbeddedFile(doc, isa);
                ef.setSize(bytes.length);
                cosObject.setEmbeddedFile(ef);
                map.put(fileName, cosObject);
            }
            tree.setNames(map);
            return toByteArray(doc);
        } catch (COSVisitorException e) {
            throw new SigningException(e);
        } catch (IOException e) {
            throw new SigningException(e);
        }
    }

    /**
     * Save {@link PDDocument} to bytes array
     * 
     * @param doc the doc to save
     * @return the byte array
     * @throws COSVisitorException
     * @throws IOException
     */
    private static byte[] toByteArray(PDDocument doc) throws COSVisitorException, IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        doc.save(os);
        closeQuietly(doc);
        return os.toByteArray();
    }

    /**
     * Get a map of file name/byte for all embedded files in the pdf
     * 
     * @param doc the pdf
     * @return the map of attachments
     * @throws IOException
     */
    private static Map<String, byte[]> getAttachments(PDDocument doc) throws IOException {
        PDDocumentNameDictionary names = new PDDocumentNameDictionary(doc.getDocumentCatalog());
        Map<String, byte[]> attachments = new LinkedHashMap<String, byte[]>();
        if (names.getEmbeddedFiles() != null && names.getEmbeddedFiles().getNames() != null) {
            for (String key : names.getEmbeddedFiles().getNames().keySet()) {
                attachments.put(key, ((PDComplexFileSpecification) names.getEmbeddedFiles().getNames().get(key))
                        .getEmbeddedFile().getByteArray());
            }
        }
        return attachments;
    }

    /**
     * Get a map of file name/byte for all embedded files in the pdf
     * 
     * @param doc the pdf
     * @return the map of attachments
     * @throws IOException
     */
    private static Set<String> getAttachmentsNames(PDDocument doc) throws IOException {
        PDDocumentNameDictionary names = new PDDocumentNameDictionary(doc.getDocumentCatalog());
        Set<String> attachments = new HashSet<String>();
        if (names.getEmbeddedFiles() != null && names.getEmbeddedFiles().getNames() != null) {
            for (String key : names.getEmbeddedFiles().getNames().keySet()) {
                attachments.add(key);
            }
        }
        return attachments;
    }

    /**
     * Remove all embedded file attachments from the document
     * 
     * @param doc the document
     * @return the document with
     * @throws COSVisitorException
     * @throws IOException
     */
    private static PDDocument removeAllAttachments(PDDocument doc) throws COSVisitorException, IOException {
        PDDocumentNameDictionary namesDictionary = new PDDocumentNameDictionary(doc.getDocumentCatalog());
        namesDictionary.setEmbeddedFiles(null);
        doc.getDocumentCatalog().setNames(namesDictionary);
        InputStream is = new ByteArrayInputStream(toByteArray(doc));
        return PDDocument.load(is);
    }

    private static byte[] getOriginalBytes(PDDocument doc, byte[] signedBytes) throws IOException {
        PDSignature signature = doc.getSignatureDictionaries().get(0);
        final int length = signature.getByteRange()[1];
        final byte[] result = new byte[length];
        System.arraycopy(signedBytes, 0, result, 0, length);
        return result;
    }
}
