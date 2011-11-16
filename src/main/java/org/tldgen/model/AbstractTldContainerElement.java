package org.tldgen.model;

import static org.tldgen.util.JavadocUtils.getStringAttribute;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ProgramElementDoc;

/**
 * A TLD entity that can contain child data 
 * @author icoloma
 *
 */
public abstract class AbstractTldContainerElement extends AbstractTldElement {

	/** the display name for automatic tools (optional) */
	private String displayName;
	
	/** the display icon for automatic tools (optional) */
	private String icon;
	
	/** an example of use (optional) */
	private String example;
	
	/** type of the annotated class */
	private String clazz;

	/** 
	 * Modify this instance, adding the fields presenrt in the doc instance
	 * @param doc
	 */
	@Override
	public void postProcessElement(ProgramElementDoc doc, AnnotationDesc annotation) {
		super.postProcessElement(doc,  annotation);
    	displayName = getStringAttribute(annotation, "displayName");
    	icon = getStringAttribute(annotation, "icon");
    	example = getStringAttribute(annotation, "example");
    	clazz = doc.isClass()? doc.qualifiedName() : doc.containingClass().qualifiedName();
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	
}
