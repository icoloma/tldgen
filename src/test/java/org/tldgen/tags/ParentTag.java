package org.tldgen.tags;

import org.tldgen.annotations.Attribute;
import org.tldgen.annotations.Tag;

/** Tag javadoc */
@Tag
public abstract class ParentTag {
	
	@SuppressWarnings("unused")
	@Attribute(name="overriden1")
	private String parent1;
	
	@Attribute(name="overriden2")
	public void setParent2(int tam) {
		
	}
	
}
