package org.tldgen.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.tldgen.TldDoclet;
import org.tldgen.annotations.BodyContent;
import org.tldgen.model.Attribute;
import org.tldgen.model.Function;
import org.tldgen.model.FunctionParameter;
import org.tldgen.model.Library;
import org.tldgen.model.Tag;
import org.tldgen.tags.DummyTei;

import com.sun.tools.javadoc.Main;

/**
 * Test the Parser class by launching javadoc and retrieving the parse results
 * @author icoloma
 *
 */ 
public class LibraryFactoryTest {

	public static String OUTPUT_FOLDER = "build/test/";
	
	private Library library;
	
	@Before
	public void setup() {
		if (library == null) {
			int result = Main.execute(new String[] {
					"-private", "-doclet", TldDoclet.class.getName(), "-sourcepath", "src/test/java", "org.tldgen.tags", "-tldFile", OUTPUT_FOLDER + "LibraryFactoryTest-output/tldgen-test.tld",
					"-displayName", "Loom Core Tag Library", "-name", "loom", "-uri", "http://loom.extrema-sistemas.org/loom-core.tld", "-htmlFolder", OUTPUT_FOLDER + "LibraryFactoryTest-output", 
					"-indentSpaces", "4", "-license", "APACHE", "-version", "2.1"
			});
			assertEquals("The javadoc command did not exit successfully. Check the system log for details", 0, result);
			library = TldDoclet.library;
			assertNotNull(library);
		}
	}
	
	@Test
	public void testTagAttributes() throws Exception {
		Tag tag = library.getTag("dummy");
		assertNotNull(tag);
		
		// check tag annotation
		assertEquals(BodyContent.SCRIPTLESS, tag.getBodyContent());
		assertEquals("foo", tag.getIcon());
		assertTrue(tag.isDynamicAttributes());
		assertEquals("Tag example", tag.getExample());
		assertEquals("Dummy display name", tag.getDisplayName());
		assertEquals(DummyTei.class.getName(), tag.getTeiClass());
		
		// check tag attributes
		Attribute foo = getAttribute(tag, "foo");
		assertNotNull(foo);
		assertTrue(foo.isDeprecated());
		assertFalse(foo.isRtexprvalue());
		assertTrue(foo.isRequired());
		
		// method attributes
		assertNotNull(getAttribute(tag, "property"));
		assertNotNull(getAttribute(tag, "overwritten"));
	}
	
	@Test
	public void testParentTag() throws Exception {
		// check that abstract classes are ignored
		assertNull(library.getTag("parent"));
		
		// check inheritance
		Tag tag = library.getTag("overriden-tag");
		
		// check empty tag annotation
		assertEquals(BodyContent.SCRIPTLESS, tag.getBodyContent());
		assertNull(tag.getIcon());
		assertFalse(tag.isDynamicAttributes());
		assertNull(tag.getExample());
		assertNull(tag.getDisplayName());
		
		assertNull(getAttribute(tag, "parent1"));
		assertNull(getAttribute(tag, "parent2"));
		assertNotNull(getAttribute(tag, "overriden1"));
		assertNotNull(getAttribute(tag, "overriden2"));
		assertNotNull(getAttribute(tag, "child1"));
		assertNotNull(getAttribute(tag, "child2"));
		
		// "required" attributes go before the rest
		assertEquals("child2", tag.getAttributes().iterator().next().getName());
	}
		
	@Test
	public void testFunctions() throws Exception {
		// check functions
		Function bar = library.getFunction("bar");
		assertNotNull(bar);
		assertEquals("foo", bar.getIcon());
		assertEquals("Function example", bar.getExample());
		assertEquals("bar display name", bar.getDisplayName());
		
		// check function with parameters
		Function baz = library.getFunction("baz");
		assertNotNull(baz);
		assertEquals("Dubbity doo", baz.getReturnDescription());
		FunctionParameter[] parameters = baz.getParameters();
		assertEquals(3, parameters.length);
		assertEquals("int", parameters[0].getType());
		assertEquals("primitive", parameters[0].getName());
		assertEquals(0, parameters[0].getDescription().length());
		assertEquals("java.lang.String", parameters[1].getType());
		assertEquals("Argument description", parameters[1].getDescription());
		assertEquals("java.lang.Integer baz(int, java.lang.String, org.tldgen.tags.DummyFunction)", baz.getSignature());
		
		// check empty function
		Function baz2 = library.getFunction("baz2");
		assertNull(baz2.getReturnDescription());
		assertNotNull(baz2);
		assertNull(baz2.getIcon());
		assertNull(baz2.getExample());
		assertNull(baz2.getDisplayName());
		assertEquals("java.lang.Integer baz2(int, java.lang.String, org.tldgen.tags.DummyFunction)", baz2.getSignature());
		
		// check  annotated private function
		assertNull(library.getFunction("hidden"));
		
	}
	
	private Attribute getAttribute(Tag tag, String name) {
		for (Attribute attribute : tag.getAttributes()) {
			if (attribute.getName().equals(name)) {
				return attribute;
			}
		}
		return null;
	}

}
