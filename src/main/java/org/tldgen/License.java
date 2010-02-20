package org.tldgen;

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
	CC;
	
	/**
	 * @return the License header to be included in generated files
	 */
	public String getLicenseHeader() {
		String filename = "licenses/" + name() + ".txt";
		InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
		try {
			if (input == null) {
				throw new RuntimeException("Could not find license filename '" + filename + "' in the classpath");
			}
			return IOUtils.toString(input);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}
	
}
