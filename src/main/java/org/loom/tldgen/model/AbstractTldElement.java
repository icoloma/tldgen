package org.loom.tldgen.model;

import static org.loom.tldgen.util.JavadocUtils.getAnnotation;
import static org.loom.tldgen.util.JavadocUtils.getStringAttribute;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.ProgramElementDoc;

/**
 * Minimum data managed by any javadoc entity
 * @author icoloma
 *
 */
public abstract class AbstractTldElement implements Comparable<AbstractTldElement> {

	/** the attribute name (required) */
	private String name;

	/** the javadoc documentation (optional) */
	private String description;
	
	/** true if the attribute is deprecated (optional)*/
	private Boolean deprecated = false;
	
	/** null if javadoc deprecated annotation does not have any text (optional)*/
	private String deprecatedMessage;
	
	private static Log log = LogFactory.getLog(AbstractTldElement.class);
	
	/**
	 * Extract common elements from the doc data
	 */
	
	public void postProcessElement(ProgramElementDoc doc, AnnotationDesc annotation) {
    	String name = getStringAttribute(annotation, "name");
    	if (name == null) {
    		name = calculateDefaultElementName(doc);
    	}
    	
    	log.debug("Processing element " + name);
		this.name = name;
    	this.description = StringUtils.isEmpty(doc.commentText())? null : doc.commentText();
    	this.deprecated = doc.tags("@deprecated").length > 0 || getAnnotation(doc, Deprecated.class) != null;
    	if (this.deprecated == true) {
    		// calculate the value of the deprecated attribute
			this.deprecatedMessage = doc.tags("@deprecated")[0].toString().replaceFirst("@deprecated:", "");
		}
	}
	
	/** 
	 * Calculate the default name of a doc element
	 */
	protected abstract String calculateDefaultElementName(Doc doc);

	
	/**
	 * @return the html escaped description adding deprecated information if exists 	
	 */
	public String getHtmlDescription() {
		StringBuilder buffer = new StringBuilder();
		if (description != null) {
			buffer.append(description);
		}
		if (deprecated) {
			buffer.append("<p><strong> Deprecated. </strong>");
			if (deprecatedMessage != null) {
				buffer.append(deprecatedMessage);
			}
			buffer.append("</p>");
		}
		return buffer.length() > 0? buffer.toString() : null;
	}
	
	public int compareTo(AbstractTldElement o) {
		return name.compareTo(o.getName());
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Boolean isDeprecated() {
		return deprecated;
	}
	
	public void setDeprecated(Boolean deprecated) {
		this.deprecated = deprecated;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getDeprecatedMessage() {
		return deprecatedMessage;
	}

	public void setDeprecatedMessage(String deprecatedMessage) {
		this.deprecatedMessage = deprecatedMessage;
	}
	
	

}
