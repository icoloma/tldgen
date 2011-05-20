package org.tldgen.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * A tag library descriptor. When bound to a package, describes the tag library 
 * attributes. Example of use (package-info.java):
 * <pre>@org.tldgen.annotations.TagLibrary(uri = "http://acme.com/mytaglib.tld")
package com.acme.tags;
</pre>
 *  
 * @author icoloma
 */
@Target({ElementType.PACKAGE, ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface Library {

	/**
	 * The tag library name. Required.
	 */
	String shortName();
	
	/** a short name that is intended to be displayed by tools */
	String displayName() default "";
	
	/** a uri uniquely identifying this taglib */
	String uri();
	
	/** the version of the TLD to generate */
	TldVersion version() default TldVersion.VERSION_21;
	
	/** optional small icon that can be used by tools */
	String smallIcon() default "";
	
	/** optional large icon that can be used by tools */
	String largeIcon() default "";
	
	/** a simple string describing the "use" of this taglib, should be user discernable */
	String description() default "";
	
	/** the library license to use. Defaults to whatever was specified in the command line */
	License license() default License.NONE;

}
