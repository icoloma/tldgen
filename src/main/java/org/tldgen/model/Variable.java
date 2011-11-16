package org.tldgen.model;

import org.tldgen.annotations.VariableScope;

import com.sun.javadoc.ClassDoc;

/**
 * A JSP <a href="http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_1.xsd">variable</a>.
 * Variables are explained <a href="http://download.oracle.com/docs/cd/B25221_05/web.1013/b14430/taglibs.htm#sthref456">here</a>. 
 * @author icoloma
 */
public class Variable {

	/** The variable name as a constant. */
	private String nameGiven;
	
	/** The name of an attribute whose (translation time) value will give the name of the
	 * variable.  One of name-given or name-from-attribute is required.
	 */
	private String nameFromAttribute;

	/** The description of the variable */
	private String description;
	
	/** Whether the variable is declared or not. */
	private boolean declare = true;
	
	/** The scope of the scripting variable. */
	private VariableScope scope;
	
	/** Class of the variable, default String */
	private ClassDoc variableClass;

	public String getNameGiven() {
		return nameGiven;
	}

	public void setNameGiven(String nameGiven) {
		this.nameGiven = nameGiven;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDeclare() {
		return declare;
	}

	public void setDeclare(boolean declare) {
		this.declare = declare;
	}

	public VariableScope getScope() {
		return scope;
	}

	public void setScope(VariableScope scope) {
		this.scope = scope;
	}

	public ClassDoc getVariableClass() {
		return variableClass;
	}

	public void setVariableClass(ClassDoc variableClass) {
		this.variableClass = variableClass;
	}

	public String getNameFromAttribute() {
		return nameFromAttribute;
	}

	public void setNameFromAttribute(String nameFromAttribute) {
		this.nameFromAttribute = nameFromAttribute;
	}
	
	
}
