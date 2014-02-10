package org.tldgen.writers;

import org.tldgen.annotations.VariableScope;
import org.tldgen.model.Attribute;
import org.tldgen.model.Library;
import org.tldgen.model.Tag;
import org.tldgen.model.Variable;

import java.io.IOException;
import java.util.Collection;

public class HtmlTagWriter extends AbstractHtmlWriter {
	
	//private static Logger log = LoggerFactory.getLogger(HtmlLibraryWriter.class);
	
	public HtmlTagWriter(String htmlFile) throws IOException {
		super(htmlFile);
	}
	
	/**
	 * @param tag the tag corresponding to this HTML file
	 * @param library the library data model to process
	 * @throws IOException if an error occurred writing HTML file
	 */
	public void write(Tag tag, Library library) throws IOException {
		startDocument(tag.getName());
		startBody();
		
		printHeader(1, tag.getName());
		
		startTag("div","id", tag.getName(), "class","yui-g bottom-delimiter");
		writeJspTag(tag);
		endTag("div");
		
		printMenu(library, tag);
		
		endBody(tag.getName());
		endDocument();
	}
	
	/**
	 * Fill the HTML main content with Tag information
	 * @param tag {@link Tag}
	 */
	private void writeJspTag(Tag tag) throws IOException{

		String description = tag.getHtmlDescription();
		
		startTag("p").print(description).endTag("p");
		
		
		startTag("table", "class", "tag-info");
		startTag("thead");
		startTag("tr");
		startTag("th", "colspan", "2").print("Tag Information").endTag("th");
		endTag("tr");
		endTag("thead");
		startTag("tbody");
		printTableEntry("Tag Class", tag.getClazz());
		printTableEntry("Display Name", tag.getDisplayName());
		printTableEntry("Icon", tag.getIcon());
		printTableEntry("TagExtraInfo", tag.getTeiClass());
		printTableEntry("Body Content", tag.getBodyContent().getTldValue());
		printTableEntry("Dynamic Attributes",  tag.isDynamicAttributes().toString());
		if (isPrintable(tag.getExample())) {
			printTableEntry("Example", "<pre class=\"code\">" + tag.getExample() + "</pre>");
		}
		endTag("tbody");
		endTag("table");
		
		writeAttributes(tag.getAttributes());
		writeVariables(tag.getVariables());
		
	}

	/**
	 * Write content of the Attribute information
	 * @param attributes {@link Attribute}
	 */
	private void writeAttributes(Collection<Attribute> attributes) throws IOException{
		if (attributes.isEmpty()) {
			return;
		}
		
		printHeader(2, "Attributes");
		startTag("table", "class", "tag-attributes");
		startTag("thead");
		printTableHeaders("Name", "Description", "Type","Flags");
		endTag("thead");
		
		startTag("tbody");
		for (Attribute attribute : attributes) {
			StringBuilder span = new StringBuilder().append("<span id=\"").append(attribute.getName()).append("\"");
			if (attribute.isDeprecated()) {
				span.append(" class=\"deprecated\" title=\"deprecated\"");
			}
			span.append(">").append(attribute.getName()).append("</span>");
			printTableRow(
					span.toString(), 
					attribute.getHtmlDescription(),
					attribute.hasType() ? attribute.getType() : "",
					"<div class=\"flags\">" +
					createAttributeIcon("required", attribute.isRequired()) + 
					createAttributeIcon("rtexprvalue", attribute.isRtexprvalue()) +
					createAttributeIcon("deprecated", attribute.isDeprecated()) +
					"</div>"
			);
		}
		endTag("tbody");
		endTag("table");
	}
	
	/**
	 * Write content of the Variable information
	 * @param variables {@link Variable}
	 */
	private void writeVariables(Collection<Variable> variables) throws IOException{
		if (variables.isEmpty()) {
			return;
		}
		
		printHeader(2, "Variables");
		startTag("table", "class", "tag-variables");
		startTag("thead");
		printTableHeaders("Name", "Description", "Declare", "Scope");
		endTag("thead");
		
		startTag("tbody");
		for (Variable variable : variables) {
			String name = variable.getNameGiven() != null? variable.getNameGiven() : "given by " + variable.getNameFromAttribute();
			printTableRow(
					name, 
					variable.getDescription(), 
					String.valueOf(variable.isDeclare()),
					(variable.getScope() == null? VariableScope.NESTED : variable.getScope()).toString() 
			);
		}
		endTag("tbody");
		endTag("table");
	}
	
	/**
	 * Create an icon to display one flag of this attribute
	 * @param cssClassName the css class and text to use
	 * @param value true to display, false to hide
	 * @return
	 */
	private String createAttributeIcon(String cssClassName, Boolean value) {
		if (Boolean.TRUE.equals(value)) {
			return "<span class=\"" + cssClassName + "\" title=\"" + cssClassName + "\">" + cssClassName + "</span>";
		}
		return "";
	}

	
}
