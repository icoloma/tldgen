package org.tldgen.mock;

import org.tldgen.annotations.BodyContent;
import org.tldgen.model.Attribute;
import org.tldgen.model.Function;
import org.tldgen.model.Library;
import org.tldgen.model.Tag;

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
	public Library createLibrary() {
		Library library = new Library();

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
		tag.addAttribute(attribute);
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
		function.setClazz("org.tldgen.targetfunctions.FunctionSampleUtil");
		library.add(function);

		return library;
	}
}
