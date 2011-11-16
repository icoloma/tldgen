package org.tldgen.tags;

import org.tldgen.annotations.Function;

public class DummyFunction {

	/** bar javadoc <p>this is a paragraph</p>*/
	@Function(
		displayName="bar display name",
		icon="foo",
		example="Function example"
	)
	public static void bar() {
		
	}

	/**
	 * 
	 * @param primitive
	 * @param unqualified Argument description
	 * @return Dubbity doo
	 */
	@Function
	public static Integer baz(int primitive, String unqualified, org.tldgen.tags.DummyFunction qualified) {
		return null;
	}
	
	@Function
	public static Integer baz2(int primitive, String unqualified, org.tldgen.tags.DummyFunction qualified) {
		return null;
	}
	
	/** bar javadoc <xml test>*/
	@Function
	@SuppressWarnings("unused")
	private static void hidden() {
		// this one should be skipped from the documentation since it's hidden
	}
	
}
