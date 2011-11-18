package org.tldgen.annotations;

/**
 * The version of the TLD schema to use
 * @author icoloma
 */
public enum TldVersion {

	VERSION_20("2.0", "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd"),
	
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
	
	@Override
	public String toString() {
		return id;
	}

	/** Convert from the given string to a value of this enum.
	 * @param version the TLD version given as this enum name or TLD version or schema location
	 * @return the TldVersion or null if the given string does not match any
	 */
	public static TldVersion convert(String version) {
		if (version == null)
			return null;
		for (TldVersion v : values()) {
			if (version.equals(v.name()) || version.equals(v.id) || version.equals(v.schemaLocation))
				return v;
		}
		return null;
	}
}
