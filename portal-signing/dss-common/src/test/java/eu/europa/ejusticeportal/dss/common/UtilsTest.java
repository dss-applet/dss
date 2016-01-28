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

import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * 
 * Test Utils
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1747 $ - $Date: 2014-10-06 10:39:58 +0200 (Mon, 06 Oct 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith(JUnit4.class)
public class UtilsTest {

    /**
     * Test getting the signature algo name
     * @throws NoSuchAlgorithmException 
     */
    @Test
    public void testSigAlgoName() throws NoSuchAlgorithmException {                
        assertEquals("RSA",Utils.getSignatureAlgorithmName("SHA1withRSA"));
        assertEquals("DSA",Utils.getSignatureAlgorithmName("SHA256withDSA"));
    }
    
    /**
     * Test the unexpected condition
     * @throws NoSuchAlgorithmException 
     */
    @Test (expected = NoSuchAlgorithmException.class)
    public void testUnexpected() throws NoSuchAlgorithmException{
        Utils.getSignatureAlgorithmName("SHA256avecDSA");
    }
    
    /**
     * Test parsing the JRE string
     */
    @Test
    public void testJreVersion(){
        String jre = "1.7.1_9";
        assertEquals(1.719,Utils.parseJreVersion(jre).doubleValue(), 0.001d);
        jre = "2";
        assertEquals(2d,Utils.parseJreVersion(jre).doubleValue(), 0.001d);
        jre = "2.12.3.4.5";
        assertEquals(2.12345,Utils.parseJreVersion(jre).doubleValue(), 0.001d);
        jre = "2.12.3.rev4.5";
        assertEquals(2.123,Utils.parseJreVersion(jre).doubleValue(), 0.001d);
        jre = "something unexpected";
        assertEquals(8d,Utils.parseJreVersion(jre).doubleValue(), 0.001d);
        jre = "20.";
        assertEquals(20,Utils.parseJreVersion(jre).doubleValue(), 0.001d);
        jre = ".20";
        assertEquals(0.2,Utils.parseJreVersion(jre).doubleValue(), 0.001d);  
    }

    @Test 
    public void testToString() {
        SigningContext sc = new SigningContext();
        sc.setArchitecture(Arch.B64.name());
        sc.setJreVersion(11.45d);
        sc.setOs(OS.WINDOWS.name());
        String s = Utils.toString(sc);
        System.out.println(s);        
        SigningContext sc2 = (SigningContext)Utils.fromString(s);        
        assertEquals(sc.getJreVersion(), sc2.getJreVersion(), 0.001d);
        assertEquals(sc.getOs(), sc2.getOs());
    }
    
    @Test 
    public void testToString2() {
        SealedPDF sp = new SealedPDF();
        sp.setFileName("My File Name");
        sp.setPdfBase64("b64fej[oijr");
        sp.setSignDate(new Date());
        
        String s = Utils.toString(sp);
        System.out.println(s);        
        SealedPDF sp2 = (SealedPDF)Utils.fromString(s);        
        assertEquals(sp.getFileName(), sp2.getFileName());
        assertEquals(sp.getSignDate(), sp2.getSignDate());
    }
    
    public void test3() {
        String s = "{\"@type\":\"eu.europa.ejusticeportal.dss.common.SealedPDF\",\"fileName\":\"Test.pdf\",\"pdfBase64\":\"JVBERi0xLjQKJfbk/N8KMSAwIG9iago8PAovVHlwZSAvQ2F0YWxvZwovVmVyc2lvbiAvMS40Ci9Q\r\nYWdlcyAyIDAgUgovTmFtZXMgMyAwIFIKPj4KZW5kb2JqCjIgMCBvYmoKPDwKL1R5cGUgL1BhZ2Vz\r\nCi9LaWRzIFs0IDAgUl0KL0NvdW50IDEKPj4KZW5kb2JqCjMgMCBvYmoKPDwKL0VtYmVkZGVkRmls\r\nZXMgNSAwIFIKPj4KZW5kb2JqCjQgMCBvYmoKPDwKL1R5cGUgL1BhZ2UKL01lZGlhQm94IFswLjAg\r\nMC4wIDYxMi4wIDc5Mi4wXQovUGFyZW50IDIgMCBSCi9SZXNvdXJjZXMgNiAwIFIKL0NvbnRlbnRz\r\nIDcgMCBSCj4+CmVuZG9iago1IDAgb2JqCjw8Ci9OYW1lcyBbKGRpZ2VzdC50eHQpIDggMCBSICh4\r\nbWxkYXRhLnhtbCkgOSAwIFJdCj4+CmVuZG9iago2IDAgb2JqCjw8Ci9Gb250IDEwIDAgUgo+Pgpl\r\nbmRvYmoKNyAwIG9iago8PAovRmlsdGVyIFsvRmxhdGVEZWNvZGVdCi9MZW5ndGggMTEgMCBSCj4+\r\nCnN0cmVhbQ0KeJwVxjEOQEAQRuF+TvGXNCyNXlAr5gIbBivLyu4o3B7J+5LXMpWDQVWDF6qMQfPh\r\nmTLeXMKXRbLH5QVjN0ADNvEXnnBDJek/EeJl0hhONyG59bR6Ryly8E490wtEJR3kDQplbmRzdHJl\r\nYW0KZW5kb2JqCjggMCBvYmoKPDwKL1R5cGUgL0ZpbGVzcGVjCi9GIChkaWdlc3QudHh0KQovRUYg\r\nMTIgMCBSCj4+CmVuZG9iago5IDAgb2JqCjw8Ci9UeXBlIC9GaWxlc3BlYwovRiAoeG1sZGF0YS54\r\nbWwpCi9FRiAxMyAwIFIKPj4KZW5kb2JqCjEwIDAgb2JqCjw8Ci9GMCAxNCAwIFIKPj4KZW5kb2Jq\r\nCjExIDAgb2JqCjk2CmVuZG9iagoxMiAwIG9iago8PAovRiAxNSAwIFIKPj4KZW5kb2JqCjEzIDAg\r\nb2JqCjw8Ci9GIDE2IDAgUgo+PgplbmRvYmoKMTQgMCBvYmoKPDwKL1R5cGUgL0ZvbnQKL1N1YnR5\r\ncGUgL1R5cGUxCi9CYXNlRm9udCAvSGVsdmV0aWNhLUJvbGQKL0VuY29kaW5nIC9XaW5BbnNpRW5j\r\nb2RpbmcKPj4KZW5kb2JqCjE1IDAgb2JqCjw8Ci9UeXBlIC9FbWJlZGRlZEZpbGUKL1BhcmFtcyAx\r\nNyAwIFIKL0xlbmd0aCAxOCAwIFIKPj4Kc3RyZWFtDQpUaGUgdGV4dCBiZWxvdyBhbGxvd3MgdXMg\r\ndG8gZW5zdXJlIHRoYXQgdGhlIGNvcnJlY3QgZmlsZSB3YXMgc2lnbmVkLg0KDQojIyMjIyMjIyMj\r\nIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjCnBjWmc0eGhpY0l3RkJC\r\nRlpVRWNsa1FFaHB1Z2hIcFZWdDlFc3diZVZQblZ4c1l6QnlpbjdxSWx2bUI4Q0xjWmlCZWp2cTVm\r\naWh5Z1YNCk4zb1hFeE8wZVhIeGxxQmJleFY3VFdUNVlvTUFOYVNLTDM3cC9zbW1kNnV3WElpZVdz\r\naVVzdFhlV21nbkp4aGFTdGF6WXNuaFZRZHgNCkZuU0RULzd1TnlncmVEcGRLejFHWGtkbkZuNGlu\r\nZGxGZzlqdHBxRUU1bERoMlQ3eUY2VWhPZnRyZkxNcllKZTZ4dHVzZWtQVkt4ZXkNCk0xUnJoS1VJ\r\naHhvQUtKV053T3c2Tk02Y2NiMWxjTU1IemRDODF0WVZmTXI2TnFVMEtGU1ZMVExOUEoweUd3UEcz\r\ndXpZdmhEeTBCVHcNCjFjSTZYNXl4RTBTV0RVZlVJVENZRGkzN0hJTUZPOGc4RXpIU0VnPT0NCgoj\r\nIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjDQplbmRz\r\ndHJlYW0KZW5kb2JqCjE2IDAgb2JqCjw8Ci9UeXBlIC9FbWJlZGRlZEZpbGUKL1BhcmFtcyAxOSAw\r\nIFIKL0xlbmd0aCAyMCAwIFIKPj4Kc3RyZWFtDQo8dGVzdC8+DQplbmRzdHJlYW0KZW5kb2JqCjE3\r\nIDAgb2JqCjw8Ci9TaXplIDUzMgo+PgplbmRvYmoKMTggMCBvYmoKNTMyCmVuZG9iagoxOSAwIG9i\r\nago8PAovU2l6ZSA3Cj4+CmVuZG9iagoyMCAwIG9iago3CmVuZG9iagp4cmVmCjAgMjEKMDAwMDAw\r\nMDAwMCA2NTUzNSBmDQowMDAwMDAwMDE1IDAwMDAwIG4NCjAwMDAwMDAwOTEgMDAwMDAgbg0KMDAw\r\nMDAwMDE0OCAwMDAwMCBuDQowMDAwMDAwMTkwIDAwMDAwIG4NCjAwMDAwMDAzMDIgMDAwMDAgbg0K\r\nMDAwMDAwMDM3MSAwMDAwMCBuDQowMDAwMDAwNDA1IDAwMDAwIG4NCjAwMDAwMDA1ODAgMDAwMDAg\r\nbg0KMDAwMDAwMDY0NCAwMDAwMCBuDQowMDAwMDAwNzA5IDAwMDAwIG4NCjAwMDAwMDA3NDIgMDAw\r\nMDAgbg0KMDAwMDAwMDc2MSAwMDAwMCBuDQowMDAwMDAwNzkzIDAwMDAwIG4NCjAwMDAwMDA4MjUg\r\nMDAwMDAgbg0KMDAwMDAwMDkyOCAwMDAwMCBuDQowMDAwMDAxNTUyIDAwMDAwIG4NCjAwMDAwMDE2\r\nNTEgMDAwMDAgbg0KMDAwMDAwMTY4MyAwMDAwMCBuDQowMDAwMDAxNzAzIDAwMDAwIG4NCjAwMDAw\r\nMDE3MzMgMDAwMDAgbg0KdHJhaWxlcgo8PAovUm9vdCAxIDAgUgovSUQgWzw4NUYzMkIxM0IzRUQz\r\nNTYxN0YyMjgwNkY2MDVCNDdCNj4gPDg1RjMyQjEzQjNFRDM1NjE3RjIyODA2RjYwNUI0N0I2Pl0K\r\nL1NpemUgMjEKPj4Kc3RhcnR4cmVmCjE3NTEKJSVFT0YK\r\n\",\"signDate\":1412348922853}";
        SealedPDF o = (SealedPDF)Utils.fromString(s);
        System.out.println(o.getFileName());
    }
}
