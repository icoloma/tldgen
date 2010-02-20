package org.loom.tldgen.tags;

import org.loom.tldgen.annotations.Attribute;
import org.loom.tldgen.annotations.Tag;

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
