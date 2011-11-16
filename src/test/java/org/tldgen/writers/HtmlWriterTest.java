package org.tldgen.writers;

import static org.junit.Assert.assertTrue;
import static org.tldgen.factory.LibraryFactoryTest.OUTPUT_FOLDER;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.tldgen.DocletOptions;
import org.tldgen.mock.MockLibraryFactory;
import org.tldgen.model.Library;

import be.roam.hue.doj.Doj;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class HtmlWriterTest {
	
	private String htmlFolder = OUTPUT_FOLDER + "HtmlWriterTest-output";

	private Library library;

	@Before
	public void initLibrary() {
		library = new MockLibraryFactory().createLibrary("loom");
	}
	
	public String readFile(String path) throws IOException {
		return FileUtils.readFileToString(new File(path));
	}
	
	public Boolean containsValue (String expected, String[] values) {
		for (String value : values) {
			if (expected.equals(value)) {
				return true;
			}
				
		}
		return false;
	}

	@Test
	public void writeHtmlTest() throws Exception {
		
		DocletOptions options = new DocletOptions()
			.withFormatOutput(false)
		;
		HtmlLibraryWriter writer = new HtmlLibraryWriter();
		writer.setOptions(options);
		writer.writeHtml(library, htmlFolder);

		File fichHtml = new File(htmlFolder + "/functions.html");

		WebClient webClient = new WebClient();
		HtmlPage page = webClient.getPage("file:///" + fichHtml.getAbsolutePath());
		Doj pageDoj = Doj.on(page);

		Doj element = pageDoj .get("div#custom-doc div#bd div#yui-main div#wrapper div#function0 table tbody tr td");
		Log log = LogFactory.getLog(HtmlWriterTest.class);
		log.debug("element = " + element.texts());
		assertTrue(containsValue("org.tldgen.functions.DummyFunction",
				element.texts()));
	}

}
