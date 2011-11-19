package org.tldgen.writers;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.tldgen.model.Function;
import org.tldgen.model.FunctionParameter;
import org.tldgen.model.Library;

public class HtmlFunctionWriter extends AbstractHtmlWriter {
	
	//private static Logger log = LoggerFactory.getLogger(HtmlFunctionWriter.class);
	
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
		printTableEntry("Signature", function.getSignature());
		printFunctionParams(function.getParameters());
		printTableEntry("Return", function.getReturnDescription());
		if (isPrintable(function.getExample())) {
			printTableEntry("Example", "<pre class=\"code\">" + function.getExample() + "</pre>");
		}
	}


	private void printFunctionParams(FunctionParameter[] parameters) throws IOException {
		if (parameters == null || parameters.length == 0) {
			return;
		}
		startTag("tr");
		startTag("td").print("Parameters").endTag("td");
		startTag("td").startTag("ul");
		for (FunctionParameter param : parameters) {
			startTag("li");
			startTag("strong").print(param.getType() + " " + param.getName()).endTag("strong");
			if (!StringUtils.isEmpty(param.getDescription())) {
				print(": ").print(param.getDescription());
			}
			endTag("li");
			
		}
		endTag("ul").endTag("td").endTag("tr");
	}
	
}

