package org.tldgen.writers;

import static org.junit.Assert.assertEquals;
import static org.tldgen.factory.LibraryFactoryTest.OUTPUT_FOLDER;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.tldgen.mock.MockLibraryFactory;
import org.tldgen.model.Library;

public class TldLibraryWriterTest {

	/** created TLD file */
	private String tldFolder = OUTPUT_FOLDER + "TldWriterTest-output";

	private Library library;

	@Before
	public void initLibrary() {
		library = new MockLibraryFactory().createLibrary("loom");
	}

	@Test
	public void writeTldTest() throws Exception {
		TldLibraryWriter writer = new TldLibraryWriter();
		writer.writeTLD(library, tldFolder);
		String expected = FileUtils.readFileToString(new File("src/test/resources/org/tldgen/writers/expected-output.tld"));
		String actual = FileUtils.readFileToString(new File(tldFolder + "/loom.tld"));
		assertEquals(expected, actual);
	}
}
