package org.loom.tldgen.annotations;

/**
 * The <a href="http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd">body-content</a> 
 * possible values
 * @author javi
 *
 */
public enum BodyContent {

	/** 
	 * the body must be empty 
	 */ 
	EMPTY, 
	
	/** 
	 * The body accepts only template text, EL Expressions, and JSP action 
	 * elements.  No scripting elements are allowed.
	 */
	SCRIPTLESS,
	
	/** 
	 * The body of the tag is interpreted by the tag implementation itself, 
	 * and is most likely in a different "language", e.g embedded SQL statements.	
	 */
	TAGDEPENDENT,
	
	/** 
	 * The body of the tag contains nested JSP syntax. 
	 */
	JSP("JSP");
	
	/** the value that will be written in the TLD */
	private String tldValue;
	
	BodyContent() {
		this(null);
	}
	
	BodyContent(String tldValue) {
		this.tldValue = tldValue == null? name().toLowerCase() : tldValue;
	}

	public String getTldValue() {
		return tldValue;
	}
}
