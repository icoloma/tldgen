package org.loom.tldgen.writers;

import static org.junit.Assert.assertTrue;
import static org.loom.tldgen.factory.LibraryFactoryTest.OUTPUT_FOLDER;

import java.io.FileReader;

import org.custommonkey.xmlunit.Diff;
import org.junit.Before;
import org.junit.Test;
import org.loom.tldgen.License;
import org.loom.tldgen.TldBuilder;
import org.loom.tldgen.mock.MockLibraryFactory;
import org.loom.tldgen.model.Library;

public class TldWriterTest {

	/** created TLD file */
	private String tldFilename = OUTPUT_FOLDER + "TldWriterTest-output/tldgen-test.tld";

	private Library library;

	@Before
	public void initLibrary() {
		library = new MockLibraryFactory().createLibrary();
	}

	@Test
	public void writeTldTest() throws Exception {
		TldBuilder builder = new TldBuilder();
		builder.setLibrary(library);
		
		builder.setDisplayName("Loom Core Tag Library");
		builder.setLicense(License.APACHE);
		builder.setShortName("loom");
		//builder.setTabSpaces("0");
		builder.setTldUri("http://loom.extrema-sistemas.org/loom-core.tld");
		builder.createTLD(tldFilename);

		FileReader expected = new FileReader( "src/test/resources/org/loom/tldgen/writers/expected-output.tld");
		Diff myDiff = new Diff( new FileReader(tldFilename), expected);
		assertTrue("XSL transformation worked as expected " + myDiff, myDiff.similar());
	}
}
