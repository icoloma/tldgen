package org.tldgen.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.tldgen.TldDoclet;
import org.tldgen.annotations.TldVersion;
import org.tldgen.model.Library;

import com.sun.tools.javadoc.Main;

/**
 * Test the Parser class by launching javadoc and retrieving the parse results
 * @author icoloma
 *
 */ 
public class LibraryAnnotationTest {

	public static String OUTPUT_FOLDER = "build/test/";
	
	private Library library;
	
	@Before
	public void setup() {
		int result = Main.execute(new String[] {
				"-private", "-doclet", TldDoclet.class.getName(), "-sourcepath", "src/test/java", "org.tldgen.libtags", "-tldFile", OUTPUT_FOLDER + "LibraryAnnotationTest-output/tldgen-test.tld",
				"-htmlFolder", OUTPUT_FOLDER + "LibraryAnnotationTest-output"
		});
		assertEquals("The javadoc command did not exit successfully. Check the system log for details", 0, result);
		library = TldDoclet.library;
		assertNotNull(library);
	}
	
	@Test
	public void testParse() throws Exception {
		assertEquals("foobar", library.getLibrarySignature().getShortName());
		assertEquals("http://acme.com/foobar", library.getLibrarySignature().getUri());
		assertEquals(TldVersion.VERSION_21, library.getLibrarySignature().getVersion());
		assertEquals(1, library.getTags().size());
	}
	
}
