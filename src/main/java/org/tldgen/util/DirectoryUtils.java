package org.tldgen.util;

import java.io.File;

public class DirectoryUtils {

	/**
	 * @param htmlFolder path of the html folder
	 * @throws RuntimeException if error cleaning target folder
	 */
	public static void createHtmlFolder(String htmlFolder) {
		createFolder(htmlFolder);
	}
	
	/**
	 * @param tldFile path of the tld file
	 * @throws RuntimeException if error cleaning target folder
	 */
	public static void createTldFolder(String tldFile) {
		File file = new File(tldFile);
		createFolder(file.getParent());
	}
	
	private static void createFolder(String targetFolder) {
		File folder = new File(targetFolder);
		if (!folder.isDirectory()) {
			folder.mkdirs();
		}
	}
}
