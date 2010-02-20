package org.tldgen.writers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tldgen.License;
import org.tldgen.model.Attribute;
import org.tldgen.model.Function;
import org.tldgen.model.Library;
import org.tldgen.model.Tag;
import org.tldgen.util.DirectoryUtils;

/**
 * Create TLD documentation
 * 
 * @author carlos
 * @author javier
 */

public class TldLibraryWriter extends AbstractWriter {

	private XMLStreamWriter writer;
	
	/** TLD File license */
	private License license;

	private static Log log = LogFactory.getLog(TldLibraryWriter.class);

	/**
	 * Write the TLD file
	 * 
	 * @param library the library to create the TLD documentation
	 * @param filename the name of the TLD file to create
	 * @throws XMLStreamException if there is any problem writing the TLD 
	 */
	public void writeTLD(Library library, String filename) throws XMLStreamException, IOException {

		log.info("Creating TLD file: " + filename);
		
		// write the TLD to memory
		StringWriter buffer = new StringWriter();
		writer = XMLOutputFactory.newInstance().createXMLStreamWriter(buffer);
		startTaglibElement(library);
		if (library.getTags() != null) {
			writeTags(library.getTags());
		}

		if (library.getFunctions() != null) {
			writeFunctions(library.getFunctions());
		}
		endTaglibElement();
		
		DirectoryUtils.createTldFolder(filename);
		// format the generated XML and write to disk
		formatAndWriteXml(buffer.toString(), filename);
	}
	

	/**
	 * Start the &lt;taglib> element and write the TLD file header
	 * 
	 * @param library the library to create the TLD taglib element information
	 */
	private void startTaglibElement(Library library) throws XMLStreamException, IOException {
		log.debug("Writing TLD file header");
		writer.writeStartDocument("UTF-8", "1.0");
		writer.writeComment(license.getLicenseHeader());
		startElement("taglib");
		writeAttribute("xmlns", "http://java.sun.com/xml/ns/j2ee");
		writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		writeAttribute("xsi:schemaLocation", "http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd");
		writeAttribute("version", "2.0");
		writeElement("display-name",library.getDisplayName());
		writeElement("tlib-version", "2.0");
		writeElement("short-name", library.getShortName());
		writeElement("uri", library.getUri());

	}

	/** 
	 * Closes the &lt;taglib> element
	 */
	private void endTaglibElement() throws XMLStreamException {
		endElement();
	}

	/**
	 * Close the output file
	 * 
	 * @throws IllegalStateException
	 *             if closing a output stream that it is not opened
	 */
	public void close() {
		try {
			if (writer != null) {
				writer.close();
			}
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Write the tags part of the TLD
	 * @param tags the {@link Tag} instances to write to the TLD
	 */
	private void writeTags(Collection<Tag> tags) throws XMLStreamException {
		for (Tag tag : tags) {
			log.debug("Writing tag '" + tag.getName() + "'");
			startElement("tag");
			writeCDataElement("description", tag.getDescription());
			writeElement("display-name", tag.getDisplayName());
			writeElement("icon", tag.getIcon());
			writeElement("name", tag.getName());
			writeElement("tag-class", tag.getClazz());
			writeElement("body-content", tag.getBodyContent().getTldValue());
			writeTagAttributes(tag.getName(), tag.getAttributes());
			writeElement("dynamic-attributes", tag.isDynamicAttributes()? tag.isDynamicAttributes() : null);
			writeElement("example", tag.getExample());
			endElement();
		}

	}

	/**
	 * Write the Function's info of the library
	 * 
	 * @param functions the {@link Function} instances to write to the TLD
	 * @throws XMLStreamException
	 *             if error writing output stream
	 */
	private void writeFunctions(Set<Function> functions)
			throws XMLStreamException {
		for (Function function : functions) {
			log.debug("Writing function '" + function.getName() + "'");
			startElement("function");
			writeCDataElement("description", function.getDescription());
			writeElement("display-name", function.getDisplayName());
			writeElement("icon", function.getIcon());
			writeElement("name", function.getName());
			writeElement("function-class", function.getClazz());
			writeElement("function-signature", function.getSignature());
			writeElement("example", function.getExample());
			endElement();
		}

	}

	/**
	 * Write the Attributes info of the corresponding Tag
	 * 
	 * @param attributes {@link Attribute} instances to write to the TLD
	 * @throws XMLStreamException
	 *             if error writing output stream
	 */
	private void writeTagAttributes(String tagName, Collection<Attribute> attributes) throws XMLStreamException {

		for (Attribute attr : attributes) {
			log.debug("Writing attribute '" + tagName + "." + attr.getName() + "'");
			startElement("attribute");
			writeCDataElement("description", attr.getDescription());
			writeElement("name", attr.getName());
			writeElement("required", attr.isRequired()? attr.isRequired() : null);
			writeElement("rtexprvalue", attr.isRtexprvalue()? attr.isRtexprvalue() : null);

			endElement();
		}
	}

	/**
	 * Write a XML tag and its value
	 * @param name the name of the element
	 * @param contents the contents of the tag. If not null, its toString() 
	 * representation will be used as the tag contents. If null,
	 * the entire tag will be skipped.
	 */
	private void writeElement(String name, Object contents) throws XMLStreamException {
		if (contents != null) {
			startElement(name);
			writer.writeCharacters(contents.toString());
			endElement();
		}
	}
	
	private void writeCDataElement(String name, String contents) throws XMLStreamException {
		if (contents != null) {
			startElement(name);
			writer.writeCData(contents);
			endElement();
		}
	}
	
	/**
	 * Write an attribute, provided its value is not null
	 */
	private void writeAttribute(String name, Object value) throws XMLStreamException {
		if (value !=  null) {
			writer.writeAttribute(name, value.toString());
		}
	}

	/**
	 * start an XML tag
	 */
	private void startElement(String nameTag) throws XMLStreamException {
		writer.writeStartElement(nameTag);
	}

	/**
	 * end an XML tag
	 */
	private void endElement() throws XMLStreamException {
		writer.writeEndElement();
	}
	
	public void setLicense(License license) {
		this.license = license;
	}
	
	
}
