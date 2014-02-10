package org.tldgen.writers;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tldgen.DocletOptions;
import org.tldgen.annotations.TldVersion;
import org.tldgen.annotations.VariableScope;
import org.tldgen.model.Attribute;
import org.tldgen.model.Function;
import org.tldgen.model.Library;
import org.tldgen.model.LibrarySignature;
import org.tldgen.model.Listener;
import org.tldgen.model.Tag;
import org.tldgen.model.Variable;
import org.tldgen.util.DirectoryUtils;


/**
 * Create TLD documentation
 * 
 * @author carlos
 * @author javier
 */

public class TldLibraryWriter extends AbstractWriter {

	private XMLStreamWriter writer;
	
	private boolean doNotOverwrite;

	private static Logger log = LoggerFactory.getLogger(TldLibraryWriter.class);

	/**
	 * Write the TLD file
	 * 
	 * @param library the library to create the TLD documentation
	 * @param folder the folder where the TLD file should be created 
	 * @throws XMLStreamException if there is any problem writing the TLD 
	 */
	public void writeTLD(Library library, String folder) throws XMLStreamException, IOException {

		String filename = folder + "/" + library.getLibrarySignature().getShortName() + ".tld";
		if (warnIfExists(filename) && doNotOverwrite) {
			return;
		}
		log.info("Creating TLD file: " + filename);
		
		// write the TLD to memory
		StringWriter buffer = new StringWriter();
		try {
			writer = XMLOutputFactory.newInstance().createXMLStreamWriter(buffer);
			startTaglibElement(library);
			if (library.getTags() != null) {
				writeTags(library.getTags());
			}

			if (library.getFunctions() != null) {
				writeFunctions(library.getFunctions());
			}

			if(library.getListeners() != null) {
				writeListeners(library.getListeners());
			}

			endTaglibElement();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
		
		DirectoryUtils.createParentFolder(filename);
		// format the generated XML and write to disk
		formatAndWriteXml(buffer.toString(), filename);
	}
	
	/**
	 * Logs a warning if the output file/folder already exists
	 * @param filename the file name to check the existence of
	 * @return true if the given file exists, false otherwise
	 */
	private boolean warnIfExists(String filename) {
		if (new File(filename).exists()) {
			log.warn(filename + " already exists. It will " + (doNotOverwrite ? "NOT" : "") + " be overwritten");
			return true;
		}
		return false;
	}

	/**
	 * Start the &lt;taglib> element and write the TLD file header
	 * 
	 * @param library the library to create the TLD taglib element information
	 */
	private void startTaglibElement(Library library) throws XMLStreamException, IOException {
		LibrarySignature signature = library.getLibrarySignature();
		TldVersion tldVersion = signature.getVersion();
		
		log.debug("Writing TLD file header");
		writer.writeStartDocument("UTF-8", "1.0");
		String licenseContent = signature.getLicense().getLicenseHeader();
		if (!StringUtils.isEmpty(licenseContent)) {
			writer.writeComment(licenseContent);
		}
		startElement("taglib");
		writeAttribute("xmlns", "http://java.sun.com/xml/ns/javaee");
		writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		writeAttribute("xsi:schemaLocation", tldVersion.getSchemaLocation());
		writeAttribute("version", tldVersion.getId());
		writeElement("display-name",signature.getDisplayName());
		writeElement("tlib-version", tldVersion.getId());
		writeElement("short-name", signature.getShortName());
		writeElement("uri", signature.getUri());
		writeElement("description", signature.getDescription());
		if (signature.getSmallIcon() != null || signature.getLargeIcon() != null) {
			startElement("icon");
			writeElement("small-icon", signature.getSmallIcon());
			writeElement("large-icon", signature.getLargeIcon());
			endElement();
		}
	}

	/** 
	 * Closes the &lt;taglib> element
	 */
	private void endTaglibElement() throws XMLStreamException {
		endElement();
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
			writeElement("tei-class", tag.getTeiClass());
			writeElement("body-content", tag.getBodyContent().getTldValue());
			writeTagAttributes(tag.getName(), tag.getAttributes());
			writeTagVariables(tag.getName(), tag.getVariables());
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
			writeCDataElement("description", function.getTldDescription());
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
	 * Write the listeners to the tld file
	 *
	 * @param listeners the listeners to include in the tld file
	 * @throws XMLStreamException on error writing to out stream
	 */
	private void writeListeners(Set<Listener> listeners)
		throws XMLStreamException {
		for(Listener listener : listeners) {
			log.debug("writing Listener for '"+listener.getListenerClass() +"'");
			startElement("listener");
			writeElement("listener-class", listener.getListenerClass());
			endElement();
		}
	}
	/**
	 * Write the Attributes info
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
			if(attr.hasType()) {
				writeElement("type", attr.getType());
			}

			endElement();
		}
	}

	/**
	 * Write the Variables info
	 * 
	 * @param variables {@link Variable} instances to write to the TLD
	 * @throws XMLStreamException
	 *             if error writing output stream
	 */
	private void writeTagVariables(String tagName, Collection<Variable> variables) throws XMLStreamException {
		for (Variable var : variables) {
			log.debug("Writing variable '" + tagName + "." + (var.getNameFromAttribute() == null? var.getNameGiven() : var.getNameFromAttribute()) + "'");
			startElement("variable");
			writeCDataElement("description", var.getDescription());
			writeElement("name-given", var.getNameGiven());
			writeElement("name-from-attribute", var.getNameFromAttribute());
			if (var.getVariableClass() != null && !String.class.getName().equals(var.getVariableClass().qualifiedName())) {
				writeElement("variable-class", var.getVariableClass());
			}
			if (!var.isDeclare()) {
				writeElement("declare", var.isDeclare());
			}
			if (var.getScope() != null && var.getScope() != VariableScope.NESTED) {
				writeElement("scope", var.getScope());
			}
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
	
	public void setOptions(DocletOptions options) {
		setIndentSpaces(options.getIndentSpaces());
		setFormatOutput(options.isFormatOutput());
		this.doNotOverwrite = options.doNotOverwrite();
	}
	
}
