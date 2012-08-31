package org.tldgen;

import static org.tldgen.util.JavadocUtils.getEnumAttribute;
import static org.tldgen.util.JavadocUtils.getStringAttribute;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.tldgen.util.ArrayUtilities;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.tldgen.annotations.License;
import org.tldgen.annotations.TldVersion;
import org.tldgen.model.Library;
import org.tldgen.model.LibrarySignature;
import org.tldgen.util.JavadocUtils;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;

/**
 * Parse doclet options and create TLD documentation
 * 
 * @author ccoloma
 */
public class TldDoclet {
	
	private static String DEFAULT_HTML_FOLDER = "build/docs/tlddoc";
	private static String DEFAULT_TLD_FOLDER = "src/main/resources/META-INF";

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
	
	/** TLD target directory */
	private static String tldFolder = DEFAULT_TLD_FOLDER;
	
	/** TLD File ident */
	private static String indentSpaces;
	
	/** TLD File license */
	private static String license;
	
	/** true to format output, false otherwise. Default true */
	private static String formatOutput;

	/** options accepted by this Doclet */
	private static Set<String> options;
	
	/** for testing purposes */
	public static Library library;
	
	static {
		options = new TreeSet<String>();
		options.add("displayName");
		options.add("formatOutput");
		options.add("htmlFolder");
		options.add("license");	
		options.add("name");
		options.add("tabSpaces");
		options.add("tldFolder");
		options.add("uri");
		options.add("version");
		
	}
	
	/**
	 * @throws IllegalArgumentException
	 *             if illegal or inappropriate TLD filename or creating output
	 *             stream.
	 * @throws IllegalStateException
	 *             if closing a output stream that it is not opened
	 */
	public static boolean start(RootDoc root) {
		boolean result = false;
		try {
			DocletOptions options = parseOptions(root.options());
			TldWorker worker = new TldWorker(options);
			if (name != null && uri != null) {
				library = worker.processLibrary(root.classes(), createLibrarySignatureFromCommandLine(), tldFolder, htmlFolder);
				result = true;
			} else {
				for (PackageDoc tagsPackage : root.specifiedPackages()) {
					AnnotationDesc libraryAnnotation = JavadocUtils.getAnnotation(tagsPackage, org.tldgen.annotations.Library.class);
					if (libraryAnnotation != null) {
						library = worker.processLibrary(tagsPackage.allClasses(), createLibrarySignatureFromAnnotation(libraryAnnotation), tldFolder, htmlFolder);
						
						// if uri is not specified then use the reverse package name
						if (library.getLibrarySignature().getUri() == null) {
							library.getLibrarySignature().setUri("http://"
									+ new String(ArrayUtilities.reverseTokens(tagsPackage.name().toCharArray(), '.')));
						}
						
						result = true;
					}
				}
			}
		} finally {
			if (!result) {
				showUsage();
			}
		}
		return result;
		
	}
	
	public static LibrarySignature createLibrarySignatureFromCommandLine() {
		LibrarySignature librarySignature = new LibrarySignature();
		librarySignature.setDisplayName(displayName);
		librarySignature.setLicense(convertLicense());
		librarySignature.setShortName(name);
		librarySignature.setUri(uri);
		librarySignature.setVersion(convertVersion());
		return librarySignature;
	}
	
	public static LibrarySignature createLibrarySignatureFromAnnotation(AnnotationDesc libraryAnnotation) {
		LibrarySignature librarySignature = new LibrarySignature();
		librarySignature.setDescription(getStringAttribute(libraryAnnotation, "description"));
		librarySignature.setDisplayName(getStringAttribute(libraryAnnotation, "displayName"));
		librarySignature.setSmallIcon(getStringAttribute(libraryAnnotation, "smallIcon"));
		librarySignature.setLargeIcon(getStringAttribute(libraryAnnotation, "largeIcon"));
		librarySignature.setShortName(getStringAttribute(libraryAnnotation, "shortName"));
		librarySignature.setUri(getStringAttribute(libraryAnnotation, "uri"));
		license = getEnumAttribute(libraryAnnotation, "license");
		librarySignature.setLicense(convertLicense());
		version = TldVersion.valueOf(getEnumAttribute(libraryAnnotation, "version")).getId();
		librarySignature.setVersion(convertVersion());
		return librarySignature;
	}
	
	private static License convertLicense() {
		try {
			if (license == null) {
				return License.NONE;
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
		TldVersion v = TldVersion.convert(version);
		return v == null? TldVersion.VERSION_20 : v;
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
		out.println("        -tldFolder {TLD folder name}");  
		out.println("        -uri {uri name}");
		out.println("        -version {TLD version}");
		out.println(""); 
		out.println("This doclet accepts the following options:");
		out.println(""); 
		out.println("  -name: the <name> element of the TLD file");
		out.println("  -uri: the <uri> element of the TLD file");
		out.println("  -displayName (optional): the <display-name> element of the TLD file");
		out.println("  -formatOutput (optional, default true): Indent the generated files.");
		out.println("  -htmlFolder (optional, default " + DEFAULT_HTML_FOLDER + "): the folder \n" +
					"   where HTML documentation will be stored");
		out.println("  -indentSpaces (optional, default 4): spaces used for indenting XML content.");
		out.println("  -license (optional, default NONE): The license to include.");
		out.println("  -tldFolder (optional, default src/main/resources/META-INF/): \n" +
					"   the folder where the TLD file will be stored.");
		out.println("  -version (optional, default 2.0): The TLD version to use.");
		out.println("");
		out.println("If -name or -uri is not present, TldGen will search for @Library annotations in ");
		out.println("the root packages");
		out.println("");
	}
	
	/**
	 * Parse the user options received by this doclet
	 * @param userOptions the list of extra options accepted by javadoc
	 */
	private static DocletOptions parseOptions(String[][] userOptions) {
		try {
			for (String[] option : userOptions) {		
				String fieldName = option[0].substring(1);
				if (options.contains(fieldName)) {
					Field field = TldDoclet.class.getDeclaredField(fieldName);
					field.set(null, option[1]);
				}
			}
			
			DocletOptions options = new DocletOptions();
			if (formatOutput != null) {
				options.withFormatOutput(Boolean.valueOf(formatOutput));
			}
			if (indentSpaces != null) {
				options.withIndentSpaces(indentSpaces);
			}
			if (license != null) {
				options.withLicense(convertLicense());
			}
			if (version != null) {
				options.withVersion(convertVersion()) ;
			}
			return options;
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

	public static void reset() {
		displayName = version = name = uri = indentSpaces = license = formatOutput = null;
		library = null;
		
		htmlFolder = DEFAULT_HTML_FOLDER;
		tldFolder = DEFAULT_TLD_FOLDER;
		
	}
	

}
