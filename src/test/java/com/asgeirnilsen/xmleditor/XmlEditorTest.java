package com.asgeirnilsen.xmleditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;


public class XmlEditorTest {
	
	@Test
	public void updateVersionNumber() throws URISyntaxException, XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		File f = new File(getClass().getResource("/test1.xml").toURI());
		Assert.assertTrue(getFileContents(f).contains("<version>1.0-SNAPSHOT</version>"));
		XmlEditor.main("/project/version:=4.4", f.getCanonicalPath());
		Assert.assertTrue(getFileContents(f).contains("<version>4.4</version>"));
	}
	
	@Test
	public void removeVersionTag() throws URISyntaxException, XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		File f = new File(getClass().getResource("/test2.xml").toURI());
		Assert.assertTrue(getFileContents(f).contains("<version>1.0-SNAPSHOT</version>"));
		XmlEditor.main("/project/version:=&", f.getCanonicalPath());
		Assert.assertFalse(getFileContents(f).contains("<version>"));
	}
	
	@Test
	public void editAttribute() throws URISyntaxException, XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		File f = new File(getClass().getResource("/test3.xml").toURI());
		Assert.assertTrue(getFileContents(f).contains("<name lang=\"hmf"));
		XmlEditor.main("/project/name/@lang:=jalla", f.getCanonicalPath());
		Assert.assertTrue(getFileContents(f).contains("<name lang=\"jalla"));
	}

	private static String getFileContents(File f) throws FileNotFoundException {
		return new Scanner(f).useDelimiter("\\A").next().replace('\r', ' ').replace('\n', ' ');
	}

}
