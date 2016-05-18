package org.tldgen;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Find TLD's in the classpath
 */
public class TldFinder {
	private static Logger log = LoggerFactory.getLogger(TldFinder.class);

	private static final Pattern TLDFILE = Pattern.compile(".*\\.tld");

	/**
	 * find the TLD in the ClassLoader
	 *
	 * @param tldURI the pattern to match
	 * @return the resources in the order they are found
	 */
	public Document getTLDDocument(final String tldURI) {
		Document doc;

		URLClassLoader cl = (URLClassLoader) TldFinder.class.getClassLoader();
		for (URL url : cl.getURLs()) {

			final File file;
			try {
				file = new File(url.toURI());
			}
			catch (URISyntaxException e) {
				log.error("Malformded URI", e);
				continue;
			}

			if (!file.exists()) {
				log.debug("File '" + url.toExternalForm() + "' does not exist");
				continue;
			}

			if (file.isDirectory()) {
				doc = getResourcesFromDirectory(file, tldURI);
				if (doc != null) {
					return doc;
				}
			} else {
				doc = getResourcesFromJarFile(file, tldURI);
				if (doc != null) {
					return doc;
				}
			}
		}

		return null;
	}

	private Document getResourcesFromJarFile(final File file, String tldURI) {
		log.debug("Scanning " + file.getAbsolutePath());
		ZipFile zf;
		try {
			zf = new ZipFile(file);
		}
		catch (final IOException e) {
			throw new Error(e);
		}

		final Enumeration e = zf.entries();
		while (e.hasMoreElements()) {
			final ZipEntry ze = (ZipEntry) e.nextElement();
			final String fileName = ze.getName();
			final boolean accept = TLDFILE.matcher(fileName).matches();

			if (accept) {
				log.info("Found a TLD: " + fileName);
				try {
					Document doc = getDocument(zf.getInputStream(ze));
					if (isRightTLD(doc, tldURI)) {
						return doc;
					}
				}
				catch (IOException e1) {
					throw new Error(e1);
				}
			}
		}

		try {
			zf.close();
		}
		catch (final IOException e1) {
			throw new Error(e1);
		}

		return null;
	}

	private Document getResourcesFromDirectory(final File directory, String tldURI) {
		log.debug("Scanning " + directory.getAbsolutePath());
		final File[] fileList = directory.listFiles();
		for (final File file : fileList) {
			if (file.isDirectory()) {

				Document doc = getResourcesFromDirectory(file, tldURI);
				if (doc != null) {
					return doc;
				}
			} else {
				try {
					final String fileName = file.getCanonicalPath();
					final boolean accept = TLDFILE.matcher(fileName).matches();
					if (accept) {
						log.info("Found a TLD: " + fileName);
						Document doc = getDocument(file);
						if (isRightTLD(doc, tldURI)) {
							return doc;
						}
					}
				}
				catch (final IOException e) {
					throw new Error(e);
				}
			}
		}

		return null;
	}

	private Boolean isRightTLD(Document document, String tldURI) {
		return document.getDocumentElement().getElementsByTagName("uri").item(0).getTextContent().equals(tldURI);
	}

	private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		return factory.newDocumentBuilder();
	}

	private Document getDocument(final File file) {
		try {
			return getDocumentBuilder().parse(file);
		}
		catch (ParserConfigurationException | SAXException | IOException e) {
			throw new Error(e);
		}
	}

	private Document getDocument(final InputStream stream) {
		try {
			return getDocumentBuilder().parse(stream);
		}
		catch (ParserConfigurationException | SAXException | IOException e) {
			throw new Error(e);
		}
	}
}
