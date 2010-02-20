package org.loom.tldgen.tags;

import org.tldgen.annotations.Attribute;
import org.tldgen.annotations.BodyContent;
import org.tldgen.annotations.Tag;

/** Tag javadoc */
@Tag(
	bodyContent=BodyContent.SCRIPTLESS,
	displayName="Dummy display name",
	dynamicAttributes=true,
	icon="foo",
	example="Tag example"
)
public class DummyTag {
	
	/** foo javadoc 
	 * @deprecated use example*/
	@SuppressWarnings("unused")
	@Attribute(name="foo", required=true, rtexprvalue=false)
	private String foo;
	
	/** hiddenFoo javadoc */
	@SuppressWarnings("unused")
	private String hiddenFoo;
	
	public void p() {
		
	}
	
	@Attribute
	public void setProperty(int tam) {
		
	}
	
	@Attribute(name="overwritten")
	public void setXXX() {
		
	}

}
