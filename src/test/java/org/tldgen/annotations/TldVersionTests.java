package org.tldgen.annotations;

import static org.junit.Assert.*;

import org.junit.Test;

public class TldVersionTests {

	@Test
	public void testConvert() {
		assertEquals(TldVersion.VERSION_20, TldVersion.convert("2.0"));
		assertEquals(TldVersion.VERSION_20, TldVersion.convert(TldVersion.VERSION_20.getSchemaLocation()));
		assertEquals(TldVersion.VERSION_20, TldVersion.convert(TldVersion.VERSION_20.toString()));
		
		assertEquals(TldVersion.VERSION_21, TldVersion.convert("2.1"));
		assertEquals(TldVersion.VERSION_21, TldVersion.convert(TldVersion.VERSION_21.getSchemaLocation()));
		assertEquals(TldVersion.VERSION_21, TldVersion.convert(TldVersion.VERSION_21.toString()));
	}
}
