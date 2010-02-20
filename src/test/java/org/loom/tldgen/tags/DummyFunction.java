package org.loom.tldgen.tags;

import org.loom.tldgen.annotations.Function;

public class DummyFunction {

	/** bar javadoc <p>this is a paragraph</p>*/
	@Function(
		displayName="bar display name",
		icon="foo",
		example="Function example"
	)
	public static void bar() {
		
	}
	
	@Function
	public static Integer baz(int primitive, String unqualified, org.loom.tldgen.tags.DummyFunction qualified) {
		return null;
	}
	
	/** bar javadoc <xml test>*/
	@Function
	@SuppressWarnings("unused")
	private static void hidden() {
		// this one should be skipped from the documentation since it's hidden
	}
	
}
