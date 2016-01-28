package eu.europa.ejusticeportal.dss.controller;

import eu.europa.ec.markt.dss.signature.token.SignatureTokenConnection;
import eu.europa.ec.markt.dss.validation102853.SignedDocumentValidator;
import eu.europa.ejusticeportal.dss.controller.signature.PdfUtils;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;

import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;

/**
 * Utils class used for testing.
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class TestUtils {

	/**
	 * Tests if a document is sealed.
	 *
	 */
	public static boolean isSealed(final SignedDocumentValidator val, final byte[] pdf, final SignatureTokenConnection token, final SealMethod sealMethod){
		switch (sealMethod){
			case SEAL:
				try {
					return PdfUtils.isSealedSeal(val, token);
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				} catch (SignatureException e) {
					e.printStackTrace();
					return false;
				} catch (KeyStoreException e) {
					e.printStackTrace();
					return false;
				} catch (CertificateEncodingException e) {
					e.printStackTrace();
					return false;
				}
			case NO_SEAL:
				return true;
			case SEAL_CUSTOM:
				return PdfUtils.isSealedCustom(pdf, token);
		}
		return false;
	}

	/**
	 * Gets the default configuration for mock.
	 * @return
	 */
	public static DocumentValidationConfig getDefaultDocumentValidationConfig(){
		DocumentValidationConfig config = Mockito.mock(DocumentValidationConfig.class);
		Mockito.when(config.getOriginValidationLevel()).thenReturn(ValidationLevel.DISABLED);
		Mockito.when(config.getTamperedValidationLevel()).thenReturn(ValidationLevel.WARN);
		Mockito.when(config.getRevokedValidationLevel()).thenReturn(ValidationLevel.WARN);
		Mockito.when(config.getSignedValidationLevel()).thenReturn(ValidationLevel.EXCEPTION);
		Mockito.when(config.getTrustedValidationLevel()).thenReturn(ValidationLevel.WARN);
		Mockito.when(config.getWorkflowValidationLevel()).thenReturn(ValidationLevel.DISABLED);
		Mockito.when(config.getExpiredValidationLevel()).thenReturn(ValidationLevel.EXCEPTION);
		Mockito.when(config.getLotlUrl()).thenReturn("https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl-mp.xml");
		Mockito.when(config.getRefreshPeriod()).thenReturn(3600);
		Mockito.when(config.getSealMethod()).thenReturn(SealMethod.SEAL_CUSTOM);
		return config;
	}
}
