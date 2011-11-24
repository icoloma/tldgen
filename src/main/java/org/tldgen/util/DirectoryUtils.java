package org.tldgen.util;

import java.io.File;

public class DirectoryUtils {
	
	/** Use only static methods. */
	private DirectoryUtils() {}
	
	/**
	 * Create a parent folder to the specified file name.
	 * @param targetFile path of the file
	 * @throws RuntimeException if error cleaning target folder
	 */
	public static void createParentFolder(String targetFile) {
		File file = new File(targetFile);
		createFolder(file.getParent());
	}
	
	public static void createFolder(String targetFolder) {
		File folder = new File(targetFolder);
		if (!folder.isDirectory()) {
			folder.mkdirs();
		}
	}
	
}
