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
package eu.europa.ejusticeportal.dss.applet.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import eu.europa.ejusticeportal.dss.applet.view.component.Component;
import eu.europa.ejusticeportal.dss.applet.view.component.Link;
import eu.europa.ejusticeportal.dss.applet.view.component.Message;
import eu.europa.ejusticeportal.dss.applet.view.component.Messages;
import eu.europa.ejusticeportal.dss.applet.view.component.Select;
import eu.europa.ejusticeportal.dss.applet.view.component.Text;
import eu.europa.ejusticeportal.dss.common.MessageLevel;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.Gson;

/**
 * 
 * Test the applet UI components.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 362 $ - $Date: 2012-11-27 18:36:12 +0100 (Tue, 27 Nov 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith(JUnit4.class)
public class UITest {

    private String testString = "ABC{{}}\"\',\u8888\u0001A\n\r\t<>^&/\\[[]][]12345";

    @Before
    public void setUp() {
        UIState.reset();
    }

    @Test
    public void testg(){
        Select s = new Select("sel");
        Text o = new Text("te");
        o.setText("here's an option");
        s.addOption(o);
        
        
        
         
        Gson g = new Gson();
    }
    /**
     * Test serialisation/deserialisation of Component
     */
    @Test
    public void testComponent() {

        Component c1 = new Component();
        c1.setEnabled(true);
        c1.setSelected(true);
        c1.setId("This is the id");
        c1.setVisible(false);
        assertTrue(c1.isChanged());
        String json = wrapJson(c1.toJson());

        Gson gson = new Gson();

        Component c2 = gson.fromJson(json, Component.class);

        assertTrue(c1.isEnabled() == c2.isEnabled());
        assertTrue(c1.isVisible() == c2.isVisible());
        assertTrue(c1.isSelected() == c2.isSelected());
        assertTrue(c1.getId().equals(c2.getId()));
        c1.reset();
        assertFalse(c1.isChanged());
        c1.unset();

    }

    /**
     * Test serialisation/deserialisation of Text
     */
    @Test
    public void testText() {
        Text c1 = new Text();
        assertFalse(c1.isChanged());
        c1.setEnabled(true);
        c1.setSelected(true);
        c1.setId("This is the id");
        c1.setVisible(false);
        c1.setText(testString);
        c1.setChanged(false);
        assertTrue(c1.isChanged());
        String json = wrapJson(c1.toJson());

        Gson gson = new Gson();

        Text c2 = gson.fromJson(json, Text.class);

        assertTrue(c1.isEnabled() == c2.isEnabled());
        assertTrue(c1.isVisible() == c2.isVisible());
        assertTrue(c1.isSelected() == c2.isSelected());
        assertTrue(c1.getId().equals(c2.getId()));
        assertTrue(c1.getText().equals(c2.getText()));

        c1.reset();
        assertFalse(c1.isChanged());
        c1.unset();
    }

    /**
     * Test serialisation/deserialisation of Select
     */
    @Test
    public void testSelect() {

        Select c1 = new Select();
        assertFalse(c1.isChanged());
        c1.setEnabled(true);
        c1.setSelected(true);
        c1.setId("This is the id");
        c1.setVisible(false);

        assertTrue(c1.isChanged());

        Text option1 = new Text();
        option1.setId("option1");
        option1.setText("Option 1 Text");

        Text option2 = new Text();
        option2.setId("option2");
        option2.setText(testString);

        c1.addOption(option1);
        c1.addOption(option2);

        String json = wrapJson(c1.toJson());

        Gson gson = new Gson();

        Select c2 = gson.fromJson(json, Select.class);

        assertTrue(c1.isEnabled() == c2.isEnabled());
        assertTrue(c1.isVisible() == c2.isVisible());
        assertTrue(c1.isSelected() == c2.isSelected());
        assertTrue(c1.getId().equals(c2.getId()));
        assertTrue(c1.getOptions().size() == c2.getOptions().size());
        assertTrue(c1.getOptions().get(0).getId().equals(c2.getOptions().get(0).getId()));

        assertTrue(c1.getIndexSelectedOption() == -1);

        c1.getOptions().get(1).setSelected(true);
        assertTrue(c1.getIndexSelectedOption() == 1);

        c1.reset();
        assertFalse(c1.isChanged());
        c1.unset();
    }

    /**
     * Test serialisation/deserialisation of Link
     */
    @Test
    public void testLink() {
        Link c1 = new Link();
        assertFalse(c1.isChanged());
        c1.setEnabled(true);
        c1.setSelected(true);
        c1.setId("This is the id");
        c1.setVisible(false);
        c1.setText(testString);
        c1.setChanged(false);
        c1.setUrl("http://www.google.fr/");
        assertTrue(c1.isChanged());
        String json = wrapJson(c1.toJson());

        Gson gson = new Gson();

        Link c2 = gson.fromJson(json, Link.class);

        assertTrue(c1.isEnabled() == c2.isEnabled());
        assertTrue(c1.isVisible() == c2.isVisible());
        assertTrue(c1.isSelected() == c2.isSelected());
        assertTrue(c1.getId().equals(c2.getId()));
        assertTrue(c1.getText().equals(c2.getText()));
        assertTrue(c1.getUrl().equals(c2.getUrl()));

        c1.reset();
        assertFalse(c1.isChanged());
        c1.unset();
    }


    /**
     * Test serialisation/deserialisation of Messages (and also Message)
     */
    @Test
    public void testMessages() {
        Messages messages = new Messages("messages");
        assertFalse(messages.isChanged());
        
        messages.setEnabled(true);
        messages.setSelected(true);
        messages.setId("This is the id");        
        messages.setVisible(false);

        assertTrue(messages.isChanged());

        Message m1 = new Message();
        m1.setTitle("A warning message");
        m1.setLevel(MessageLevel.WARNING);        
        messages.addMessage(m1);
        
        
        Message m2 = new Message();
        m2.setTitle("Another warning message");
        m2.setLevel(MessageLevel.WARNING);
        messages.addMessage(m2);
        
        Message m3 = new Message();
        m3.setTitle("An info message");
        m3.setLevel(MessageLevel.INFO);
        Text detail1 = new Text();
        detail1.setText("Detail 1");
        Text detail2 = new Text();
        detail2.setText("Detail 2");
        
        m3.addDetail(detail1);
        m3.addDetail(detail2);
        messages.addMessage(m3);
        
        assertTrue(messages.isChanged());

        String json = wrapJson(messages.toJson());

        Gson gson = new Gson();

        Messages messages2 = gson.fromJson(json, Messages.class);

        assertTrue(messages.isEnabled() == messages2.isEnabled());
        assertTrue(messages.isVisible() == messages2.isVisible());
        assertTrue(messages.isSelected() == messages2.isSelected());
        assertTrue(messages.getId().equals(messages2.getId()));
        assertTrue(messages.getMessages().size() == messages2.getMessages().size());
        assertTrue(messages.getMessages().size()>0);
        
        for (int i = 0;i<messages.getMessages().size();i++){
            Message om = messages.getMessages().get(i);
            Message nm = messages2.getMessages().get(i);
            assertEquals(om.getTitle(),nm.getTitle());
            assertEquals(om.getLevel(),nm.getLevel());
            assertEquals(om.getDetails().size(),nm.getDetails().size());
        }
        
        
        Message om = messages.getMessages().get(2);
        Message nm = messages2.getMessages().get(2);
        assertEquals(om.getDetails().size(), 2);
        for (int i=0;i<om.getDetails().size();i++){
            assertEquals(om.getDetails().get(i).getText(),
                        nm.getDetails().get(i).getText());
        }
        om.setDetails(new ArrayList<Text>());
        assertEquals(0,om.getDetails().size());
        messages.reset();
        assertFalse(messages.isChanged());
        messages.unset();
        
        messages.setClear(true);
        assertTrue(messages.isClear());
        messages.setClear(false);
        assertFalse(messages.isClear());
        
    }



    /** Test for state Initial */
    @Test
    public void testSI() {
        UIState.transition(UIState.SI____);
        assertVE(UI.BTN_BACK);
        assertVD(UI.BTN_NEXT);
        assertVE(UI.BTN_OPEN);
        assertNV(UI.BTN_REFRESH);
        assertNV(UI.BTN_SIGN);
        assertNV(UI.BTN_DL_SIGNED);
        assertNV(UI.BTN_DL_UNSIGNED);
        assertNV(UI.FILE_PKCS11);
        assertNV(UI.FILE_PKCS12);
        assertNV(UI.FILE_UPLOAD);
        assertNV(UI.MESSAGES);
        assertV(UI.SECTION);
        assertNV(UI.CERTIFICATES_RECOMMENDED);
        assertNV(UI.TXT_CARDINFO);
        assertNV(UI.TXT_USER_SURVEY);
    }

    /** Test for state Smart card signing method */
    @Test
    public void testSC() {
        UIState.transition(UIState.SI____);
        UIState.transition(UIState.SC____);
        assertVE(UI.BTN_BACK);
        assertVD(UI.BTN_NEXT);
        assertVE(UI.BTN_OPEN);
        
        assertNV(UI.BTN_SIGN);
        assertNV(UI.BTN_DL_SIGNED);
        assertNV(UI.BTN_DL_UNSIGNED);
        assertNV(UI.FILE_PKCS11);
        assertNV(UI.FILE_PKCS12);
        assertNV(UI.FILE_UPLOAD);;
        assertVE(UI.MESSAGES);
        assertV(UI.SECTION);
        assertNV(UI.TXT_CARDINFO);
        assertNV(UI.TXT_USER_SURVEY);
    }



    /** Test for state Smart card - certificate selected */
    @Test
    public void testSC_CS() {
        UIState.transition(UIState.SI____);
        UIState.transition(UIState.SC____);
        UIState.transition(UIState.SC_CS_);
        assertVE(UI.BTN_BACK);
        assertVD(UI.BTN_NEXT);
        assertVE(UI.BTN_OPEN);
        
        assertVE(UI.BTN_SIGN);
        assertNV(UI.BTN_DL_SIGNED);
        assertNV(UI.BTN_DL_UNSIGNED);
        assertNV(UI.FILE_PKCS11);
        assertNV(UI.FILE_PKCS12);
        assertNV(UI.FILE_UPLOAD);      
        assertVE(UI.MESSAGES);
        assertV(UI.SECTION);
        assertNV(UI.CERTIFICATES_RECOMMENDED);
        assertNV(UI.TXT_CARDINFO);
        assertNV(UI.TXT_USER_SURVEY);
    }

    /** Test for state Smart card - signed */
    @Test
    public void testSC_SG() {
        UIState.transition(UIState.SI____);
        UIState.transition(UIState.SC____);
        UIState.transition(UIState.SC_CS_);
        UIState.transition(UIState.SC_SG_);
        assertVE(UI.BTN_BACK);
        assertVE(UI.BTN_NEXT);
        assertVE(UI.BTN_OPEN);
        assertNV(UI.BTN_REFRESH);
        assertNV(UI.BTN_SIGN);
        assertVE(UI.BTN_DL_SIGNED);
        assertNV(UI.BTN_DL_UNSIGNED);
        assertNV(UI.FILE_PKCS11);
        assertNV(UI.FILE_PKCS12);
        assertNV(UI.FILE_UPLOAD);
        assertVE(UI.MESSAGES);
        assertV(UI.SECTION);
        assertNV(UI.CERTIFICATES_RECOMMENDED);
        assertNV(UI.TXT_CARDINFO);
        assertNV(UI.TXT_USER_SURVEY);
    }

    /** Test for state PKCS12 signing method */
    @Test
    public void testP12() {
        UIState.transition(UIState.SI____);
        UIState.transition(UIState.P12___);
        assertVE(UI.BTN_BACK);
        assertVD(UI.BTN_NEXT);
        assertVE(UI.BTN_OPEN);
        assertNV(UI.BTN_REFRESH);
        assertNV(UI.BTN_SIGN);
        assertNV(UI.BTN_DL_SIGNED);
        assertNV(UI.BTN_DL_UNSIGNED);
        assertNV(UI.FILE_PKCS11);
        assertVE(UI.FILE_PKCS12);
        assertNV(UI.FILE_UPLOAD);
        assertVE(UI.MESSAGES);
        assertV(UI.SECTION);
        assertNV(UI.CERTIFICATES_RECOMMENDED);
        assertNV(UI.TXT_CARDINFO);
        assertNV(UI.TXT_USER_SURVEY);
    }

    /** Test for state PKCS12 - Certificate file provided */
    @Test
    public void testP12_FP() {
        UIState.transition(UIState.SI____);
        UIState.transition(UIState.P12___);
        UIState.transition(UIState.P12_FP);
        assertVE(UI.BTN_BACK);
        assertVD(UI.BTN_NEXT);
        assertVE(UI.BTN_OPEN);
        assertNV(UI.BTN_REFRESH);
        assertVD(UI.BTN_SIGN);
        assertNV(UI.BTN_DL_SIGNED);
        assertNV(UI.BTN_DL_UNSIGNED);
        assertNV(UI.FILE_PKCS11);
        assertVE(UI.FILE_PKCS12);
        assertNV(UI.FILE_UPLOAD);
        assertVE(UI.MESSAGES);
        assertV(UI.SECTION);
        assertNV(UI.CERTIFICATES_RECOMMENDED);
        assertNV(UI.TXT_CARDINFO);
        assertNV(UI.TXT_USER_SURVEY);
    }

    /** Test for state PKCS12- certificate selected */
    @Test
    public void testP12_CS() {
        UIState.transition(UIState.SI____);
        UIState.transition(UIState.P12___);
        UIState.transition(UIState.P12_FP);
        UIState.transition(UIState.P12_CS);
        assertVE(UI.BTN_BACK);
        assertVD(UI.BTN_NEXT);
        assertVE(UI.BTN_OPEN);
        assertNV(UI.BTN_REFRESH);
        assertVE(UI.BTN_SIGN);
        assertNV(UI.BTN_DL_SIGNED);
        assertNV(UI.BTN_DL_UNSIGNED);
        assertNV(UI.FILE_PKCS11);
        assertVE(UI.FILE_PKCS12);
        assertNV(UI.FILE_UPLOAD);
        assertVE(UI.MESSAGES);
        assertV(UI.SECTION);
        assertNV(UI.CERTIFICATES_RECOMMENDED);
        assertNV(UI.TXT_CARDINFO);
        assertNV(UI.TXT_USER_SURVEY);
    }

    /** Test for state PKCS12 - signed */
    @Test
    public void testP12_SG() {
        UIState.transition(UIState.SI____);
        UIState.transition(UIState.P12___);
        UIState.transition(UIState.P12_FP);
        UIState.transition(UIState.P12_CS);
        UIState.transition(UIState.P12_SG);
        assertVE(UI.BTN_BACK);
        assertVE(UI.BTN_NEXT);
        assertVE(UI.BTN_OPEN);
        assertNV(UI.BTN_REFRESH);
        assertNV(UI.BTN_SIGN);
        assertVE(UI.BTN_DL_SIGNED);
        assertNV(UI.BTN_DL_UNSIGNED);
        assertNV(UI.FILE_PKCS11);
        assertNV(UI.FILE_PKCS12);
        assertNV(UI.FILE_UPLOAD);
        assertVE(UI.MESSAGES);
        assertV(UI.SECTION);
        assertNV(UI.CERTIFICATES_RECOMMENDED);
        assertNV(UI.TXT_CARDINFO);
        assertNV(UI.TXT_USER_SURVEY);
    }




    private void assertNV(Component c) {
        assertFalse(c.isVisible());
        assertFalse(c.isEnabled());
    }

    private void assertV(Component c) {
        assertTrue(c.isVisible());
    }

    private void assertVE(Component c) {
        assertTrue(c.isEnabled());
        assertTrue(c.isVisible());
    }

    private void assertVD(Component c) {
        assertTrue(c.isVisible());
        assertFalse(c.isEnabled());
    }

    private static String wrapJson(String j) {
        return "{" + j + "}";
    }

}
