package org.tldgen.mock;

import org.tldgen.annotations.BodyContent;
import org.tldgen.annotations.License;
import org.tldgen.model.Attribute;
import org.tldgen.model.Function;
import org.tldgen.model.FunctionParameter;
import org.tldgen.model.Library;
import org.tldgen.model.LibrarySignature;
import org.tldgen.model.Tag;
import org.tldgen.model.Variable;

/**
 * Creates mock Library data for testing
 * 
 * @author ccoloma
 * 
 */
public class MockLibraryFactory {

	/**
	 * Create a mock {@link Library} for tests
	 */
	public Library createLibrary(String name) {
		LibrarySignature signature = new LibrarySignature();
		signature.setDisplayName("Loom Core Tag Library");
		signature.setLicense(License.APACHE);
		signature.setShortName(name);
		//builder.setTabSpaces("0");
		signature.setUri("http://loom.extrema-sistemas.org/loom-core.tld");
		
		Library library = new Library(signature);

		Tag tag = new Tag();
		tag.setName("dummy");
		tag.setExample("dummy example");
		tag.setDisplayName("dummy display name");
		tag.setClazz("org.tldgen.sample.DummyTag");
		tag.setBodyContent(BodyContent.EMPTY);
		tag.setDescription("tag description with <testing description escape");
		
		Attribute attribute = new Attribute();
		attribute.setName("foo");
		attribute.setDeprecated(true);
		attribute.setRequired(true);
		attribute.setRtexprvalue(false);
		attribute.setType("java.lang.Boolean");
		tag.addAttribute(attribute);
		
		Variable var = new Variable();
		var.setNameGiven("foo");
		tag.addVariable(var);
		
		library.add(tag);
		
		tag = new Tag();
		tag.setName("tag2");
		tag.setExample("tag2 example");
		tag.setDisplayName("tag2 display name");
		tag.setClazz("org.tldgen.sample.extension.Tag2");
		tag.setBodyContent(BodyContent.SCRIPTLESS);
		tag.setDescription("description of another tag");
		tag.setDeprecated(true);
		tag.setDeprecatedMessage("example of use");
		attribute = new Attribute();
		attribute.setName("foo");
		attribute.setRequired(false);
		attribute.setRtexprvalue(false);
		tag.addAttribute(attribute);
		library.add(tag);
		
		Function function = new Function();
		function.setName("function0");
		function.setExample("function0 example");
		function.setClazz("org.tldgen.functions.DummyFunction");
		library.add(function);
		function = new Function();
		function.setName("function1");
		function.setExample("function1 example");
		function.setSignature("java.lang.String function0(int param1)");
		FunctionParameter param = new FunctionParameter("int", "param1", "Mock description");
		function.setParameters(new FunctionParameter[] { param });
		function.setClazz("org.tldgen.targetfunctions.FunctionSampleUtil");
		function.setReturnDescription("Returns true if the argument is 42, false otherwise. Go figure.");
		library.add(function);

		return library;
	}
}
