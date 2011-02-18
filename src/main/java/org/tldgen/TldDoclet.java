package org.tldgen;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.tldgen.factory.LibraryFactory;
import org.tldgen.model.Library;
import org.tldgen.model.TldVersion;

import com.sun.javadoc.RootDoc;

/**
 * Parse doclet options and create TLD documentation
 * 
 * @author ccoloma
 */
public class TldDoclet {
	
	private static String DEFAULT_HTML_FOLDER = "build/docs/tlddoc";

	/** TLD output file */
	private static String tldFile;
	
	/** TLD file display-name */
	private static String displayName;
	
	/** TLD version */
	private static String version;
	
	/** TLD file short-name */
	private static String name;
	
	/** TLD file uri */
	private static String uri;
	
	/** HTML documentation target directory */
	private static String htmlFolder = DEFAULT_HTML_FOLDER;
	
	/** TLD File ident */
	private static String indentSpaces;
	
	/** TLD File license */
	private static String license;
	
	/** true to format output, false otherwise. Default true */
	private static boolean formatOutput = true;

	private static Library library;	
	
	/** options accepted by this Doclet */
	private static Set<String> options;
	
	/** required options accepted by this Doclet */
	private static Set<String> requiredOptions;
	
	static {
		options = new TreeSet<String>();
		options.add("tldFile");
		options.add("version");
		options.add("name");
		options.add("uri");
		options.add("htmlFolder");
		options.add("license");	
		options.add("displayName");
		options.add("tabSpaces");
		options.add("formatOutput");
		
		requiredOptions = new TreeSet<String>();
		requiredOptions.add("name");
		requiredOptions.add("uri");
	}
	
	
	
	/**
	 * @throws IllegalArgumentException
	 *             if illegal or inappropriate TLD filename or creating output
	 *             stream.
	 * @throws IllegalStateException
	 *             if closing a output stream that it is not opened
	 */
	public static boolean start(RootDoc root) {
		
		parseOptions(root.options());
		if (!checkRequiredOptions()) {
			showUsage();
			return false;
		}
		
		TldBuilder builder = new TldBuilder();
		library = new LibraryFactory().parse(root);
		builder.setLibrary(library);

		builder.setDisplayName(displayName);
		builder.setShortName(name);
		if (indentSpaces != null) {
			builder.setIndentSpaces(indentSpaces);
		}
		builder.setVersion(convertVersion());
		builder.setTldUri(uri);
		builder.setFormatOutput(formatOutput);
		builder.setLicense(convertLicense());
		builder.createTLD(tldFile);
		builder.createHtmlDoc(htmlFolder);
		return true;
		
	}
	
	private static License convertLicense() {
		try {
			if (license == null) {
				return License.APACHE;
			}
			return License.valueOf(license.toUpperCase());
		} catch (IllegalArgumentException e) {
			try {
				File f = new File(license);
				if (!f.exists() || !f.isFile()) {
					String licenseNames = StringUtils.join(ArrayUtils.removeElement(License.values(), License.CUSTOM), ", ");
					throw new IllegalArgumentException("Invalid license. Available licenses are: " + licenseNames + 
							", or any valid file location.");
				}
				
				String licenseHeader = FileUtils.readFileToString(f);
				License license = License.CUSTOM;
				license.setLicenseHeader(licenseHeader);
				return license;
			} catch (IOException e1) {
				throw new RuntimeException(e1);
			}
		}
	}
	
	private static TldVersion convertVersion() {
		if (version == null || "2.0".equals(version)) {
			return TldVersion.VERSION_20;
		} else if ("2.1".equals(version)) {
			return TldVersion.VERSION_21;
		} else {
			throw new IllegalArgumentException("Unknown TLD version. Available values are: '2.0', '2.1'");
		}
	}

	/**
	 * Check that all required fields are present
	 * @return true if all required fields  are present, false otherwise
	 */
	private static boolean checkRequiredOptions() {
		try {
			boolean result = true;
			for (String option : requiredOptions) {
				Field field = TldDoclet.class.getDeclaredField(option);
				Object value = field.get(null);
				if (value == null) {
					System.out.println("ERROR: Missing -" + option + " argument");
					result = false;
				}
			}
			return result;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	private static void showUsage() {
		PrintStream out = System.out;
		out.println("Usage: ");
		out.println("tldgen");
		out.println("        -sourcepath {path to java source files}");
		out.println("        -subpackages {tags package name}");
		out.println(""); 
		out.println("        -displayName {name}");
		out.println("        -formatOutput {true | false}");
		out.println("        -htmlFolder {HTML documentation directory}");
		out.println("        -indentSpaces {number of indent spaces}");
		out.println("        -license {APACHE | GPL | LGPL | MIT | MOZILLA | CC | NONE | [file location]}");
		out.println("        -name {name}");
		out.println("        -tldfile {TLD file name}");  
		out.println("        -uri {uri name}");
		out.println("        -version {TLD version}");
		out.println(""); 
		out.println("This doclet accepts the following options:");
		out.println(""); 
		out.println("  -name (required): the <name> element of the TLD file");
		out.println("  -uri (required): the <uri> element of the TLD file");
		out.println("  -displayName (optional): the <display-name> element of the TLD file");
		out.println("  -formatOutput (optional, default true): Indent the generated files.");
		out.println("  -htmlFolder (optional, default " + DEFAULT_HTML_FOLDER + "): the folder \n" +
					"   where HTML documentation will be stored");
		out.println("  -indentSpaces (optional, default 4): spaces used for indenting XML content.");
		out.println("  -license (optional, default APACHE): The license to include.");
		out.println("  -tldfile (optional, default src/main/resources/META-INF/{name}.tld): \n" +
					"   the name of the generated TLD file");
		out.println("  -version (optional, default 2.0): The TLD version to use.");
		out.println("");
	}
	
	/**
	 * Parse the user options received by this doclet
	 * @param userOptions the list of extra options accepted by javadoc
	 */
	private static void parseOptions(String[][] userOptions) {
		try {
			for (String[] option : userOptions) {		
				String fieldName = option[0].substring(1);
				if (options.contains(fieldName)) {
					Field field = TldDoclet.class.getDeclaredField(fieldName);
					field.set(null, option[1]);
				}
			}
			if (tldFile == null) {
				tldFile = "src/main/resources/META-INF/" + name + ".tld";
			}
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}			
	}

	/**
	 * Return the number of parameters available for each option. See
	 * http://java
	 * .sun.com/j2se/1.5.0/docs/guide/javadoc/doclet/overview.html#options
	 * 
	 * @param option
	 *            the name of the option parameter
	 * @return the number of expected value, 0 if unrecognized
	 */
	public static int optionLength(String option) {
		return options.contains("-" + option)? 0 : 2; 
	}
	
	public static Library getLibrary() {
		return library;
	}
	
	

}
