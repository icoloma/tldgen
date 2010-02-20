package org.loom.tldgen.tags;

import org.loom.tldgen.annotations.Attribute;
import org.loom.tldgen.annotations.Tag;

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
