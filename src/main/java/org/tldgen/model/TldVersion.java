package org.tldgen.model;

/**
 * The version of the TLD schema to use
 * @author icoloma
 */
public enum TldVersion {

	VERSION_20("2.0", "http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd"),
	
	VERSION_21("2.1", "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-jsptaglibrary_2_1.xsd");
	
	private String id;
	
	private String schemaLocation;

	private TldVersion(String version, String schemaLocation) {
		this.id = version;
		this.schemaLocation = schemaLocation;
	}

	public String getId() {
		return id;
	}

	public String getSchemaLocation() {
		return schemaLocation;
	}
	
}
