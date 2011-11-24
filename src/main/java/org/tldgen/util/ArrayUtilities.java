package org.tldgen.util;

public class ArrayUtilities {

	/** Use only static methods. */
	private ArrayUtilities() {}
	
	/** 
	 * Reverse all the characters in the given array (in place) starting from the start index (inclusively) 
	 * to end index (exclusively). 
	 * @param s the char array to be reversed
	 * @param start the start index
	 * @param end the end index (exclusively)
	 * @return the same array reversed
	 * @throws ArrayOutOfBoundsException if start and end are not positive and smaller than array length
	 */
	public static char[] reverse(char[] s, int start, int end) {
		int mid = start + ((end - start) / 2);
		for (int i = start; i < mid; i++) {
			// swap s[i] with s[end - i - 1 + start]
			int j = end - i - 1 + start;
			s[i] ^= s[j];
			s[j] ^= s[i];
			s[i] ^= s[j];
		}
		return s;
	}
	
	/** 
	 * Reverse all the characters in the given array (in place).
	 * @param s the char array to be reversed
	 * @return the same array reversed
	 */
	public static char[] reverse(char[] s) {
		return reverse(s, 0, s.length);
	}
	
	/**
	 * Reverse all the tokens in the given string separated by the specified separator. The tokens will be reversed
	 * only between the start index (inclusively) and the end index (exclusively). 
	 * @param s the string of which tokens will be reversed.
	 * @param start the start index
	 * @param end the end index (exclusively)
	 * @param separator the character separator
	 * @return the same array with tokens reversed
	 */
	public static char[] reverseTokens(char[] s, int start, int end, char separator) {
		reverse(s, start, end);
		int lastIndex = start;
		for (int i = start; i < end; i++) {
			if (s[i] == separator) {
				if (lastIndex < i - 1)
					reverse(s, lastIndex, i);
				lastIndex = i + 1;
			}
		}
		if (lastIndex < s.length - 1)
			reverse(s, lastIndex, s.length);
		return s;
	}

	/**
	 * Reverse all the tokens in the given string separated by the specified separator. The tokens will be reversed
	 * only between the start index (inclusively) and the end index (exclusively). 
	 * @param s the string of which tokens will be reversed.
	 * @param separator the character separator
	 * @return the same array with tokens reversed
	 */
	public static char[] reverseTokens(char[] s, char separator) {
		return reverseTokens(s, 0, s.length, separator);
	}
}
