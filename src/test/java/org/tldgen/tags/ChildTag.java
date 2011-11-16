package org.tldgen.tags;

import org.tldgen.annotations.Attribute;
import org.tldgen.annotations.Tag;

/** Tag javadoc */
@Tag(name="overriden-tag")
public class ChildTag extends ParentTag {
	
	@Attribute
	@SuppressWarnings("unused")
	private String child1;
	
	@Attribute(required=true)
	public void setChild2(int tam) {
		
	}
	
}
