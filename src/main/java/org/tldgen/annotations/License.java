package org.tldgen.annotations;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

/**
 * License types supported by the platform
 * @author icoloma
 *
 */
public enum License {

	/** Apache v2 */ 
	APACHE, 
	
	/** GPL v3 */
	GPL, 
	
	/** LGPL v3 */
	LGPL, 
	
	/** MIT */
	MIT, 
	
	/** Mozilla license */
	MOZILLA,
	
	/** Creative Commons with attribution */
	CC,
	
	/** No license */
	NONE,
	
	/** Custom license. In this case, a file location must be provided */
	CUSTOM;
	
	private String licenseHeader;
	
	/**
	 * @return the License header to be included in generated files
	 */
	public String getLicenseHeader() {
		if (licenseHeader == null) {
			String filename = "licenses/" + name() + ".txt";
			InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
			try {
				if (input == null) {
					throw new RuntimeException("Could not find license filename '" + filename + "' in the classpath");
				}
				licenseHeader = IOUtils.toString(input);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(input);
			}
		}
		return licenseHeader;
	}

	public void setLicenseHeader(String licenseHeader) {
		this.licenseHeader = licenseHeader;
	}
	
}
