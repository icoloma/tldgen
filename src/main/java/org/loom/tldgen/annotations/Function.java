package org.loom.tldgen.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * A JSP <a href="http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd">function</a>.
 * @author jralonso
 *
 */
@Target(ElementType.METHOD)
@Documented
public @interface Function {

	/**
	 * Optional icon element that can be used by tools, default none
	 */
	String icon() default "";
	
	/**
	 * Optional informal description of an example of a use of this function
	 */
	String example() default "";

	/**
	 * A short name that is intended to be displayed by tools, default none
	 */
	String displayName() default "";

}
