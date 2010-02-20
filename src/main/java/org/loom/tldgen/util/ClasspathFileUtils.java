package org.loom.tldgen.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class ClasspathFileUtils {

	/**
	 * Read a file from the classpath and return its contents
	 * @param location the path relative to the classpath
	 * @return the file contents
	 */
	public static String readContents(String location) {
		InputStream inputStream = null;
		try {
			inputStream = getInputStream(location);
			return IOUtils.toString(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	public static InputStream getInputStream(String location) {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(location);
		if (inputStream == null) {
			throw new RuntimeException("File not found in classpath: " + location);
		}
		return inputStream;
	}

}
