package org.tldgen.writers;

import static org.junit.Assert.assertTrue;
import static org.tldgen.factory.LibraryFactoryTest.OUTPUT_FOLDER;

import java.io.FileReader;

import org.custommonkey.xmlunit.Diff;
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
		FileReader expected = new FileReader( "src/test/resources/org/tldgen/writers/expected-output.tld");
		Diff myDiff = new Diff( new FileReader(tldFolder + "/loom.tld"), expected);
		assertTrue("XSL transformation worked as expected " + myDiff, myDiff.similar());
	}
}
