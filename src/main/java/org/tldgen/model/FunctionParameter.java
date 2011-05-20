package org.tldgen.model;


/**
 * A parameter of a Function declaration
 * @author icoloma
 *
 */
public class FunctionParameter {

	/** type of the function argument */
	private String type;
	
	/** name of the function argument */
	private String name;
	
	/** description specified in the javadoc. May be null */
	private String description;

	public FunctionParameter(String type, String name, String description) {
		this.type = type;
		this.name = name;
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
}
