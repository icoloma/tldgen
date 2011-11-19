package org.tldgen.writers;

import static org.junit.Assert.assertTrue;
import static org.tldgen.factory.LibraryFactoryTest.OUTPUT_FOLDER;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.tldgen.DocletOptions;
import org.tldgen.mock.MockLibraryFactory;
import org.tldgen.model.Library;


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
		assertTrue(fichHtml.exists());
		String contents = FileUtils.readFileToString(fichHtml);
		assertTrue(contents != null && contents.length() > 0);
	}

}
