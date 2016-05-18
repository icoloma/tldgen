package org.tldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tldgen.annotations.BodyContent;
import org.tldgen.annotations.VariableScope;
import org.tldgen.model.Attribute;
import org.tldgen.model.Function;
import org.tldgen.model.FunctionParameter;
import org.tldgen.model.Library;
import org.tldgen.model.Listener;
import org.tldgen.model.Tag;
import org.tldgen.model.Variable;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.tools.javadoc.ClassDocImpl;

/**
 * @author onigoetz
 */
public class TldInherit {

	private static String INHERITED_TEXT = "<p><strong>Inherited</strong></p>";

	private String tld;

	public TldInherit(String tld) {
		this.tld = tld;
	}

	public void inherit(Library library) {
		TldFinder finder = new TldFinder();
		Document doc = finder.getTLDDocument(tld);

		if (doc == null) {
			throw new Error("Could not inherit form TLD: '" + tld + "' did you include the dependency ?");
		}

		Set<Tag> parsedTags = getInheritedTags(doc);
		for (Tag tag : parsedTags) {
			if (library.getTag(tag.getName()) == null) {
				library.add(tag);
			}
		}

		Set<Function> parsedFunctions = getInheritedFunctions(doc);
		for (Function function : parsedFunctions) {
			if (library.getTag(function.getName()) == null) {
				library.add(function);
			}
		}

		Set<Listener> parsedListeners = getInheritedListeners(doc);
		for (Listener listener : parsedListeners) {
			if (library.getListener(listener.getName()) == null) {
				library.addListener(listener);
			}
		}
	}

	private Set<Listener> getInheritedListeners(Document document) {
		Set<Listener> parsedListeners = new TreeSet<>();
		NodeList functions = document.getDocumentElement().getElementsByTagName("listener");

		for (int i = 0; i < functions.getLength(); i++) {
			parsedListeners.add(getInheritedListener(functions.item(i)));
		}

		return parsedListeners;
	}

	private Listener getInheritedListener(Node node) {
		Listener listener = new Listener();
		NodeList nl = node.getChildNodes();

		for (int j = 0; j < nl.getLength(); j++) {
			if (nl.item(j).getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) {
				continue;
			}

			Node el = nl.item(j);
			switch (el.getNodeName()) {
				case "listener-class":
					listener.setListenerClass(el.getTextContent());
					break;
			}
		}

		return listener;
	}

	private Set<Function> getInheritedFunctions(Document document) {
		Set<Function> parsedFunctions = new TreeSet<>();
		NodeList functions = document.getDocumentElement().getElementsByTagName("function");

		for (int i = 0; i < functions.getLength(); i++) {
			parsedFunctions.add(getInheritedFunction(functions.item(i)));
		}

		return parsedFunctions;
	}

	private Function getInheritedFunction(Node node) {
		Function function = new Function();
		NodeList nl = node.getChildNodes();

		for (int j = 0; j < nl.getLength(); j++) {
			if (nl.item(j).getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) {
				continue;
			}

			Node el = nl.item(j);
			switch (el.getNodeName()) {
				case "description":
					function.setDescription(parseFunctionDescription(el, function ));
					break;
				case "name":
					function.setName(el.getTextContent());
					break;
				case "display-name":
					function.setDisplayName(el.getTextContent());
					break;
				case "icon":
					function.setIcon(el.getTextContent());
					break;
				case "function-class":
					function.setClazz(el.getTextContent());
					break;
				case "function-signature":
					function.setSignature(el.getTextContent());
					break;
				case "example":
					function.setExample(el.getTextContent());
					break;
			}
		}

		return function;
	}

	private static Pattern PARAMETER = Pattern.compile("\\s\\*\\s([a-zA-Z0-9_]*)\\s([a-zA-Z0-9_]*):\\s(.*)");
	private static Pattern RETURN = Pattern.compile("^\\s*Return:\\s(.*)", Pattern.MULTILINE);

	private String parseFunctionDescription(Node el, Function fun) {
		int start = 0;
		int end = 0;

		String content = el.getTextContent();

		List<FunctionParameter> parameters = new ArrayList<>();
		Matcher m = PARAMETER.matcher(content);
		while(m.find()) {
			if (start == 0) {
				start = m.start();
			}
			end = m.end();

			String description =  m.group(3);
			if (description.trim().equals("null")) {
				description = "";
			}

			parameters.add(new FunctionParameter(m.group(1), m.group(2), description));
		}

		// Limit the scope of the search to find the return information
		String returnSearch = (end == 0)? content : content.substring(end);
		Matcher m2 = RETURN.matcher(returnSearch);

		if (m2.find()) {
			fun.setReturnDescription(m2.group(1));
		}

		if (parameters.size() > 0) {
			fun.setParameters(parameters.toArray(new FunctionParameter[parameters.size()]));
			content = content.substring(0, start);
		}

		return content;
	}

	private Set<Tag> getInheritedTags(Document document) {
		Set<Tag> parsedTags = new TreeSet<>();
		NodeList tags = document.getDocumentElement().getElementsByTagName("tag");

		for (int i = 0; i < tags.getLength(); i++) {
			parsedTags.add(getInheritedTag(tags.item(i)));
		}

		return parsedTags;
	}

	private Tag getInheritedTag(Node node) {
		Tag tag = new Tag();
		tag.setBodyContent(BodyContent.SCRIPTLESS); //by default
		tag.setDescription(INHERITED_TEXT);

		NodeList nl = node.getChildNodes();

		for (int j = 0; j < nl.getLength(); j++) {
			if (nl.item(j).getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) {
				continue;
			}

			Node el = nl.item(j);
			switch (el.getNodeName()) {
				case "description":
					tag.setDescription(INHERITED_TEXT + el.getTextContent());
					break;
				case "display-name":
					tag.setDisplayName(el.getTextContent());
					break;
				case "icon":
					tag.setIcon(el.getTextContent());
					break;
				case "name":
					tag.setName(el.getTextContent());
					break;
				case "tag-class":
					tag.setClazz(el.getTextContent());
					break;
				case "tei-class":
					tag.setTeiClass(el.getTextContent());
					break;
				case "body-content":
					tag.setBodyContent(BodyContent.valueOf(el.getTextContent().toUpperCase()));
					break;
				case "variable":
					tag.addVariable(getInheritedVariable(el));
					break;
				case "attribute":
					tag.addAttribute(getInheritedAttribute(el));
					break;
				case "dynamic-attributes":
					tag.setDynamicAttributes(Boolean.parseBoolean(el.getTextContent()));
					break;
				case "example":
					tag.setExample(el.getTextContent());
			}
		}

		return tag;
	}

	private Attribute getInheritedAttribute(Node node) {
		Attribute attribute = new Attribute();
		attribute.setRequired(false);
		NodeList nl = node.getChildNodes();

		for (int j = 0; j < nl.getLength(); j++) {
			if (nl.item(j).getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) {
				continue;
			}

			Node el = nl.item(j);
			switch (el.getNodeName()) {
				case "description":
					attribute.setDescription(el.getTextContent());
					break;
				case "name":
					attribute.setName(el.getTextContent());
					break;
				case "required":
					attribute.setRequired(Boolean.parseBoolean(el.getTextContent()));
					break;
				case "rtexprvalue":
					attribute.setRtexprvalue(Boolean.parseBoolean(el.getTextContent()));
					break;
				case "type":
					attribute.setType(el.getTextContent());
					break;
			}
		}

		return attribute;
	}

	private Variable getInheritedVariable(Node node) {
		Variable variable = new Variable();
		NodeList nl = node.getChildNodes();

		for (int j = 0; j < nl.getLength(); j++) {
			if (nl.item(j).getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) {
				continue;
			}

			Node el = nl.item(j);
			switch (el.getNodeName()) {
				case "description":
					variable.setDescription(el.getTextContent());
					break;
				case "name-given":
					variable.setNameGiven(el.getTextContent());
					break;
				case "name-from-attribute":
					variable.setNameFromAttribute(el.getTextContent());
					break;
				case "variable-class":
					variable.setVariableClass(new SimpleClassDoc(el.getTextContent()));
					break;
				case "declare":
					variable.setDeclare(Boolean.parseBoolean(el.getTextContent()));
					break;
				case "scope":
					variable.setScope(VariableScope.valueOf(el.getTextContent().toUpperCase()));
			}
		}

		return variable;
	}

	/**
	 * The only method we use for the class generation is "qualifiedName" so we will simply use that to create the new tld
	 */
	private class SimpleClassDoc extends ClassDocImpl {
		private String qualifiedName;

		SimpleClassDoc(String qualifiedName) {
			super(null, null);
			this.qualifiedName = qualifiedName;
		}

		@Override
		public String qualifiedName() {
			return qualifiedName;
		}
	}
}
