package org.tldgen.model;

import com.sun.javadoc.Doc;
import com.sun.javadoc.MethodDoc;

/**
 * Information about one tag attribute 
 * @author icoloma
 *
 */
public class Attribute extends AbstractTldElement {
	
	/** true if the attribute is required (required) */
	private Boolean required;
	
	/** true if the attribute will accept EL expressions (optional) */
	private Boolean rtexprvalue;
	
	/**
	 * Sort starting with the required attributes 
	 */
	@Override
	public int compareTo(AbstractTldElement o) {
		Attribute other = (Attribute) o;
		if (Boolean.TRUE.equals(required) && !Boolean.TRUE.equals(other.isRequired())) {
			return -1;
		} else if (!Boolean.TRUE.equals(required) && Boolean.TRUE.equals(other.isRequired())) {
			return 1;
		} else {
			return super.compareTo(o);
		}
	}
	
	@Override
	protected String calculateDefaultElementName(Doc doc) {
		String name = doc.name();
		if (!(doc instanceof MethodDoc)) {
			return name;
		}
		
		// if it's a setter, remove the "set" prefix
		if (!name.matches("set\\w+")) {
			throw  new IllegalArgumentException("Method " + ((MethodDoc)doc).qualifiedName() + "() cannot be annotated as an @Attribute since it is not a setter method");
		}
		return Character.toLowerCase(name.charAt(3)) + name.substring(4);
	}
	
	public Boolean isRequired() {
		return required;
	}
	
	public void setRequired(Boolean required) {
		this.required = required;
	}
	
	public Boolean isRtexprvalue() {
		return rtexprvalue;
	}
	
	public void setRtexprvalue(Boolean rtexprvalue) {
		this.rtexprvalue = rtexprvalue;
	}
	
}
