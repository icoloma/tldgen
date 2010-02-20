package org.loom.tldgen;

import java.io.File;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.loom.tldgen.model.Library;
import org.loom.tldgen.writers.HtmlLibraryWriter;
import org.loom.tldgen.writers.TldLibraryWriter;

/**
 * Creates a TLD file and related HTML documentation
 * @author icoloma
 *
 */
public class TldBuilder {

	/** the parsed TLD library metadata */
	private Library library;
	
	private TldLibraryWriter tldWriter = new TldLibraryWriter();
	
	private HtmlLibraryWriter htmlWriter = new HtmlLibraryWriter();
	
	private static Log log = LogFactory.getLog(TldBuilder.class);
	
	/**
	 * Create a TLD file with the contents of the TLD library
	 * @param tldFilename the location of the TLD file to create
	 */
	public void createTLD(String tldFilename) {
		try {
			warnIfExists(tldFilename);
			tldWriter.writeTLD(library, tldFilename);
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			tldWriter.close();
		}
	}
	
	/**
	 * Logs a warning if the output file/folder already exists
	 */
	private void warnIfExists(String filename) {
		if (new File(filename).exists()) {
			log.warn(filename + " already exists. It will be overwritten");
		}
	}

	/**
	 * Create the HTML files with the documentation of this TLD library
	 * @param htmlFolder the folder where the documentation files should be written. If it does not exist, it will be created
	 */
	public void createHtmlDoc(String htmlFolder) {
		try {
			htmlWriter.setHtmlFolder(htmlFolder);
			htmlWriter.writeHtml(library);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void setLibrary(Library library) {
		this.library = library;
	}
	
	public void setDisplayName(String displayName) {
		library.setDisplayName(displayName);
	}
	
	public void setShortName(String shortName) {
		library.setShortName(shortName);
	}
	
	public void setIndentSpaces(String indentSpaces) {
		tldWriter.setIndentSpaces(indentSpaces);
	}
	
	public void setTldUri(String tldUri) {
		library.setUri(tldUri);
	}
	
	public void setLicense(License license) {
		tldWriter.setLicense(license);
	}

	public void setFormatOutput(boolean formatOutput) {
		htmlWriter.setFormatOutput(formatOutput);
		tldWriter.setFormatOutput(formatOutput);
	}
	
	
	
}
