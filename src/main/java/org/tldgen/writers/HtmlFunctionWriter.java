package org.tldgen.writers;

import java.io.IOException;

import org.tldgen.model.Function;
import org.tldgen.model.Library;

public class HtmlFunctionWriter extends AbstractHtmlWriter {
	
	//private static Log log = LogFactory.getLog(HtmlFunctionWriter.class);
	
	public HtmlFunctionWriter(String htmlFile) throws IOException {
		super(htmlFile);
	}
	
	
	/**
	 * @param library the library data model to process
	 */
	public void write(Library library) throws IOException{
		startDocument("Loom Functions");
		startBody();
		
		printHeader(1, "Functions");
		
		for (Function function: library.getFunctions()) {
			startTag("div", "id", function.getName(), "class","yui-g bottom-delimiter");
			writeJspFunction(function);
			endTag("div");
		}
		
		printMenu(library, library.getFunctions().iterator().next());
		
		endBody("Functions");
		endDocument();
	}
	
	/**
	 * Fill the HTML main content with Function information
	 * @param function {@link Function}
	 * @throws IOException 
	 */
	private void writeJspFunction(Function function) throws IOException {
		
		printHeader(2, function.getName());
		print(function.getHtmlDescription());
		startTag("table");
		startTag("thead");
		startTag("tr");
		startTag("th", "colspan","2");
		print("Function information");
		endTag("th");
		endTag("tr");
		endTag("thead");
		startTag("tbody");
		writeInfo(function);
		endTag("tbody");
		endTag("table");		
	}
	
	/**
	 * Write content of the Function information
	 * @param function {@link Function}
	 * @throws IOException 
	 */
	private void writeInfo(Function function) throws IOException {
		printTableEntry("Function Class", function.getClazz());
		printTableEntry("Display Name", function.getDisplayName());
		printTableEntry("Icon", function.getIcon());
		printTableEntry("Signature", function.getSignature());
		if (isPrintable(function.getExample())) {
			printTableEntry("Example", "<pre class=\"code\">" + function.getExample() + "</pre>");
		}
	}
	
}

