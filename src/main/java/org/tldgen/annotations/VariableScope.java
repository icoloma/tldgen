package org.tldgen.annotations;

/**
 * Scope of JSP variables. For more information see {@link javax.servlet.jsp.tagext.TagExtraInfo}
 * @author icoloma
 *
 */
public enum VariableScope {

	/**
     * Scope information that scripting variable is visible only within the
     * start/end tags.
     */
	NESTED, 
	
	/**
	 * Scope information that scripting variable is visible after start tag.
	 */
	AT_BEGIN, 
	
	/**
	 * Scope information that scripting variable is visible after end tag.
	 */
	AT_END;
	
}
