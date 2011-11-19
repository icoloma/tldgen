package org.tldgen;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tldgen.factory.LibraryFactory;
import org.tldgen.model.Library;
import org.tldgen.model.LibrarySignature;
import org.tldgen.writers.HtmlLibraryWriter;
import org.tldgen.writers.TldLibraryWriter;

import com.sun.javadoc.ClassDoc;

public class TldWorker {

	private LibraryFactory libraryFactory = new LibraryFactory();
	
	private DocletOptions options;
	
	private static Logger log = LoggerFactory.getLogger(TldWorker.class);
	
	public TldWorker(DocletOptions options) {
		this.options = options;
	}

	public Library processLibrary(ClassDoc[] classes, LibrarySignature librarySignature, String tldFolder, String htmlFolder) {
		Library library = libraryFactory.parse(classes, librarySignature);
		createTLD(library, tldFolder);
		createHtmlDoc(library, htmlFolder);
		return library;
	}
	
	/**
	 * Create a TLD file with the contents of the TLD library
	 */
	public void createTLD(Library library, String tldFolder) {
		TldLibraryWriter tldLibraryWriter = new TldLibraryWriter();
		try {
			tldLibraryWriter.setOptions(options);
			tldLibraryWriter.writeTLD(library, tldFolder);
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			tldLibraryWriter.close();
		}
	}
	

	
	/**
	 * Create the HTML files with the documentation of this TLD library
	 * @param htmlFolder the folder where the documentation files should be written. If it does not exist, it will be created
	 */
	public void createHtmlDoc(Library library, String htmlFolder) {
		try {
			HtmlLibraryWriter htmlLibraryWriter = new HtmlLibraryWriter();
			htmlLibraryWriter.setOptions(options);
			htmlLibraryWriter.writeHtml(library, htmlFolder);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Create a TLD file with the contents of the TLD library
	 * @param tldFoldername the location of the TLD file to create
	 * /
	
	private static final String TLD_SUFFIX = ".tld";
	public void createTLDs(Library[] libraries, String tldFoldername) {
		for (Library library : libraries) {
			// get the TLD name
			String tldFileName = library.getFileName();
			// if not specified, defaults to short name
			if (!tldFileName.endsWith(TLD_SUFFIX)) {
				tldFileName += TLD_SUFFIX;
			}
			// get the full path to the file
			tldFileName = new File(tldFoldername, tldFileName).getPath();
			createTLD(library, tldFileName);
		}		
	}
	
*/
}
