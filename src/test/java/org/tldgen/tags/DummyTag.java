package org.tldgen.tags;

import org.tldgen.annotations.Attribute;
import org.tldgen.annotations.BodyContent;
import org.tldgen.annotations.Tag;
import org.tldgen.annotations.Variable;
import org.tldgen.annotations.VariableScope;

/** Tag javadoc */
@Tag(
	bodyContent=BodyContent.SCRIPTLESS,
	displayName="Dummy display name",
	dynamicAttributes=true,
	icon="foo",
	example="Tag example",
	teiClass=DummyTei.class,
	variables={ @Variable(nameGiven="var1", declare=false, scope=VariableScope.AT_BEGIN) }
)
@SuppressWarnings("unused")
public class DummyTag {
	
	/** foo javadoc 
	 * @deprecated use example*/
	@Attribute(name="foo", required=true, rtexprvalue=false)
	private String foo;
	
	@Attribute(name="xxx")
	@Variable
	private String bar;
	
	/** hiddenFoo javadoc */
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
