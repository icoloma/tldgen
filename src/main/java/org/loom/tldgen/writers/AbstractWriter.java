package org.loom.tldgen.writers;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.saxon.TransformerFactoryImpl;
import net.sf.saxon.event.SaxonOutputKeys;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;

/**
 * Parent class of any library writer
 * @author icoloma
 *
 */
public abstract class AbstractWriter {
	
	/** TLD File ident */
	private String indentSpaces = "4";
	
	/** true to prettify output */
	private boolean formatOutput = true;

	private static Log log = LogFactory.getLog(AbstractWriter.class);
	
	static {
		// set saxon as the default XSLT engine
		System.setProperty("javax.xml.transform.TransformerFactory", TransformerFactoryImpl.class.getName());
	}
	
	/**
	 * Prettyprints the XML contents to the specified file  
	 */
	protected void formatAndWriteXml(String xml, String filename) {
		try {
			log.info("Formatting XML and writing to file " + filename);
			if (formatOutput) {
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				//transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", tabSpaces);
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");      
				transformer.setOutputProperty(OutputKeys.METHOD, "html");      
				transformer.setOutputProperty(SaxonOutputKeys.INDENT_SPACES /*"{http://saxon.sf.net/}indent-spaces" */, indentSpaces);
				InputSource input = new InputSource(new StringReader(xml));
				transformer.transform(new SAXSource(input), new StreamResult(new File(filename)));
				return;
			}
		} catch (TransformerException e) {
			log.warn("Error indenting output for '" + filename + "'. Either set formatOutput=false or check that javadoc contains only well-formed XML. The file will be saved as-is (" + e.getMessageAndLocation() + ")");
			log.debug(e.getMessageAndLocation() + "\n" + xml);
		}
		
		try {
			FileUtils.writeStringToFile(new File(filename), xml);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return;
		
	}
	/*
	protected void formatAndWriteXml(String xml, String filename) throws XMLStreamException {
		Tidy tidy = new Tidy();
        tidy.setXHTML(true);
        tidy.setQuiet(true);
        tidy.setSmartIndent(true);
        tidy.setSpaces(2);
        tidy.setTabsize(1);
        tidy.setDropEmptyParas(true);
        tidy.setWraplen(200);
        tidy.setOnlyErrors(true);
        
        FileOutputStream out = null;
        try {
			out = new FileOutputStream(filename);
			tidy.parse(new ByteArrayInputStream(xml.getBytes()), out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(out);
		}

	}*/

	public void setIndentSpaces(String tabSpaces) {
		this.indentSpaces = tabSpaces;
	}

	public void setFormatOutput(boolean prettyPrint) {
		this.formatOutput = prettyPrint;
	}
	
}
