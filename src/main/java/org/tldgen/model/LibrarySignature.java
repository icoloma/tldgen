package org.tldgen.model;

import org.tldgen.annotations.License;
import org.tldgen.annotations.TldVersion;

/**
 * The information that identifies a Library
 * @author icoloma
 *
 */
public class LibrarySignature {
	
	/** the tag library name */
	private String shortName;
	
	/** a short name that is intended to be displayed by tools */
	private String displayName;
	
	/**  a uri uniquely identifying this taglib */
	private String uri;
	
	/** the version of the TLD to generate */
	private TldVersion version = TldVersion.VERSION_20;
	
	/** the license to use */
	private License license;

	/** optional small icon that can be used by tools */
	private String smallIcon;
	
	/** optional large icon that can be used by tools */
	private String largeIcon;

	/** a simple string describing the "use" of this taglib, should be user discernable */
	private String description;

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String name) {
		this.shortName = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public TldVersion getVersion() {
		return version;
	}

	public void setVersion(TldVersion version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public String getSmallIcon() {
		return smallIcon;
	}

	public void setSmallIcon(String smallIcon) {
		this.smallIcon = smallIcon;
	}

	public String getLargeIcon() {
		return largeIcon;
	}

	public void setLargeIcon(String largeIcon) {
		this.largeIcon = largeIcon;
	}
	
	
	
}
