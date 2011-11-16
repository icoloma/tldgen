package org.tldgen.writers;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.tldgen.model.Library;
import org.tldgen.model.LibrarySignature;

public class HtmlIndexWriter extends AbstractHtmlWriter {
	
	//private static Log log = LogFactory.getLog(HtmlIndexWriter.class);
	
	public HtmlIndexWriter(String htmlFile) throws IOException {
		super(htmlFile);
	}
	
	public void write(Library library) throws IOException{
		startDocument("Library");
		startBody();
		
		startTag("div", "class","yui-g bottom-delimiter");
		printHeader(1, library.getLibrarySignature().getShortName());
		endTag("div");
		startTag("div", "class","yui-g bottom-delimiter");

		writeLibrarySignature(library.getLibrarySignature());
		
		endTag("div");
		
		printMenu(library, null);
		
		endBody("Library");
		endDocument();
	}
	
	private void writeLibrarySignature(LibrarySignature library) throws IOException {
		startTag("table");
		startTag("thead");
		startTag("tr");
		startTag("th", "colspan","2");
		print("Library information");
		endTag("th");
		endTag("tr");
		endTag("thead");
		startTag("tbody");
		if (!StringUtils.isEmpty(library.getDisplayName())) {
			printTableRow("Name", library.getDisplayName());
		}
		printTableRow("Uri", library.getUri());
		printTableRow("TLD Version", library.getVersion().getId());
		if (!StringUtils.isEmpty(library.getSmallIcon()) || !StringUtils.isEmpty(library.getLargeIcon())) {
			printTableRow("Icons", library.getSmallIcon() + " " + library.getLargeIcon());
		}
		if (!StringUtils.isEmpty(library.getDescription())) {
			printTableRow("Description", library.getDescription());
		}
		endTag("tbody");
		endTag("table");		

	}

}
