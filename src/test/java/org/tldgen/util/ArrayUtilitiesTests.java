package org.tldgen.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArrayUtilitiesTests {

	@Test
	public void reverseTest() {
		assertEquals("", new String(ArrayUtilities.reverse("".toCharArray())));
		assertEquals("qwerty", new String(ArrayUtilities.reverse("ytrewq".toCharArray())));
	}
	
	@Test
	public void reverseWithIndexTest() {
		assertEquals("qwerty", new String(ArrayUtilities.reverse("qwrety".toCharArray(), 2, 4)));
		assertEquals("qwerty", new String(ArrayUtilities.reverse("qrewty".toCharArray(), 1, 4)));
	}
	
	@Test
	public void reverseTokenTest() {
		assertEquals("tldgen.org", new String(ArrayUtilities.reverseTokens("org.tldgen".toCharArray(), '.')));		
	}
	
}
